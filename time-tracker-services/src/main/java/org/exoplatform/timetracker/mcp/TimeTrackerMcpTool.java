/**
 * Copyright (C) 2026 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.exoplatform.timetracker.mcp;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import org.exoplatform.commons.exception.ObjectNotFoundException;
import org.exoplatform.services.security.ConversationState;
import org.exoplatform.timetracker.dto.Activity;
import org.exoplatform.timetracker.dto.ActivityRecord;
import org.exoplatform.timetracker.dto.Client;
import org.exoplatform.timetracker.dto.Project;
import org.exoplatform.timetracker.dto.RecordsAccessList;
import org.exoplatform.timetracker.dto.SalesOrder;
import org.exoplatform.timetracker.dto.Team;
import org.exoplatform.timetracker.dto.TeamMember;
import org.exoplatform.timetracker.mcp.model.TimeActivityModel;
import org.exoplatform.timetracker.mcp.model.TimeEntryCollectionModel;
import org.exoplatform.timetracker.mcp.model.TimeEntryDuplicationModel;
import org.exoplatform.timetracker.mcp.model.TimeEntryModel;
import org.exoplatform.timetracker.mcp.model.TimeLocationsModel;
import org.exoplatform.timetracker.mcp.model.TimeReferenceModel;
import org.exoplatform.timetracker.mcp.model.TimesheetSummaryModel;
import org.exoplatform.timetracker.service.ActivityRecordService;
import org.exoplatform.timetracker.service.ActivityService;
import org.exoplatform.timetracker.service.ClientService;
import org.exoplatform.timetracker.service.ProjectService;
import org.exoplatform.timetracker.service.SalesOrderService;
import org.exoplatform.timetracker.service.TeamService;
import org.exoplatform.timetracker.service.TimeTrackerSettingsService;

import io.meeds.mcp.server.plugin.McpToolPlugin;

import jakarta.persistence.EntityNotFoundException;
import lombok.SneakyThrows;

/**
 * MCP tools exposing the Time Tracker add-on to the AI agent. Every method
 * acts as the current user. Writes only ever concern <strong>that user's</strong>
 * timesheet (the user name is never a write parameter). Reading someone else's
 * time, a team's or a user's, is reserved to time-tracking managers, as in the
 * UI. Deletion goes through the ownership check of
 * {@link ActivityRecordService}.
 */
@Service
@Profile("mcp-server")
public class TimeTrackerMcpTool implements McpToolPlugin {

  private static final String         MANAGERS_GROUP      = "/platform/time-tracking-managers";

  private static final String         DATE_FORMAT_HINT    = "'%s' must be a valid yyyy-MM-dd date";

  private static final String         SORT_FIELD          = "activityDate";

  private static final int            MAX_LIMIT           = 100;

  private static final int            MAX_RANGE_DAYS      = 366;

  private static final float          MAX_HOURS_PER_ENTRY = 24f;

  private final ActivityRecordService activityRecordService;

  private final ActivityService       activityService;

  private final TeamService           teamService;

  private final ClientService         clientService;

  private final ProjectService        projectService;

  private final SalesOrderService     salesOrderService;

  private final TimeTrackerSettingsService settingsService;

  @Autowired
  public TimeTrackerMcpTool(ActivityRecordService activityRecordService, // NOSONAR
                            ActivityService activityService,
                            TeamService teamService,
                            ClientService clientService,
                            ProjectService projectService,
                            SalesOrderService salesOrderService,
                            TimeTrackerSettingsService settingsService) {
    this.activityRecordService = activityRecordService;
    this.activityService = activityService;
    this.teamService = teamService;
    this.clientService = clientService;
    this.projectService = projectService;
    this.salesOrderService = salesOrderService;
    this.settingsService = settingsService;
  }

  // ==========================================================================
  // Reference data
  // ==========================================================================

  /**
   * Lists the activities the current user can book time on, i.e. those of the
   * teams the user belongs to.
   */
  @SneakyThrows
  public List<TimeActivityModel> getTimeTrackerActivities() {
    return getAllowedActivities().stream().map(this::toActivityModel).toList();
  }

  /** Lists the clients an entry can be attached to. */
  public List<TimeReferenceModel> listTimeTrackerClients() {
    return safe(clientService.getClientsList()).stream()
                                               .map(c -> new TimeReferenceModel(c.getId(), c.getCode(), c.getLabel(), null))
                                               .toList();
  }

  /** Lists the projects an entry can be attached to, optionally of one client. */
  public List<TimeReferenceModel> listTimeTrackerProjects(Long clientId) {
    return safe(projectService.getProjectsList()).stream()
                                                 .filter(p -> clientId == null
                                                              || p.getClient() != null && clientId.equals(p.getClient().getId()))
                                                 .map(p -> new TimeReferenceModel(p.getId(),
                                                                                  p.getCode(),
                                                                                  p.getLabel(),
                                                                                  p.getClient() == null ? null :
                                                                                                        p.getClient().getId()))
                                                 .toList();
  }

  /** Lists the sales orders an entry can be attached to, optionally of one client. */
  public List<TimeReferenceModel> listTimeTrackerSalesOrders(Long clientId) {
    List<SalesOrder> orders = clientId == null ? salesOrderService.getSalesOrdersList() :
                                                salesOrderService.getSalesOrderByClienId(clientId);
    return safe(orders).stream()
                       .map(o -> new TimeReferenceModel(o.getId(),
                                                        null,
                                                        o.getName(),
                                                        o.getClient() != null ? o.getClient().getId() :
                                                                              o.getClientId() > 0 ? o.getClientId() : null))
                       .toList();
  }

  /** Lists the locations and offices an entry can refer to. */
  public TimeLocationsModel getTimeTrackerLocations() {
    return new TimeLocationsModel(safe(settingsService.getLocationsList()).stream()
                                                                          .map(l -> new TimeReferenceModel(null,
                                                                                                           l.getCode(),
                                                                                                           l.getLabel(),
                                                                                                           null))
                                                                          .toList(),
                                  safe(settingsService.getOfficesList()).stream()
                                                                        .map(o -> new TimeReferenceModel(null,
                                                                                                         o.getCode(),
                                                                                                         o.getLabel(),
                                                                                                         null))
                                                                        .toList());
  }

  /** Lists all the teams. Reserved to time-tracking managers. */
  @SneakyThrows
  public List<TimeReferenceModel> listTimeTrackerTeams() {
    checkManager();
    return safe(teamService.getTeams()).stream()
                                       .map(t -> new TimeReferenceModel(null, t.getId(), t.getName(), null))
                                       .toList();
  }

  // ==========================================================================
  // Reading entries
  // ==========================================================================

  /**
   * Lists timesheet entries between two dates, newest first. By default the
   * current user's; a time-tracking manager can give a username or a team id.
   */
  @SneakyThrows
  public TimeEntryCollectionModel listTimeEntries(String fromDate,
                                                  String toDate,
                                                  Integer offset,
                                                  Integer limit,
                                                  String username,
                                                  String teamId) {
    String from = checkOptionalDate(fromDate, "from_date");
    String to = checkOptionalDate(toDate, "to_date");
    int safeOffset = offset == null || offset < 0 ? DEFAULT_OFFSET : offset;
    int safeLimit = limit == null || limit <= 0 ? DEFAULT_LIMIT : Math.min(limit, MAX_LIMIT);
    List<TimeEntryModel> entries;
    long total;
    if (StringUtils.isBlank(username) && StringUtils.isBlank(teamId)) {
      RecordsAccessList result = queryEntries(from, to, getCurrentUserName(), safeOffset, safeLimit);
      entries = safe(result.getActivityRecords()).stream().map(this::toEntryModel).toList();
      total = result.getSize() == null ? entries.size() : result.getSize();
    } else {
      List<ActivityRecord> all = new ArrayList<>();
      for (String user : resolveUsers(username, teamId)) {
        all.addAll(queryEntries(from, to, user, 0, 0).getActivityRecords());
      }
      all.sort(Comparator.comparing(ActivityRecord::getActivityDate, Comparator.nullsLast(Comparator.reverseOrder()))
                         .thenComparing(ActivityRecord::getId, Comparator.nullsLast(Comparator.reverseOrder())));
      total = all.size();
      entries = all.stream().skip(safeOffset).limit(safeLimit).map(this::toEntryModel).toList();
    }
    float totalHours = (float) entries.stream().mapToDouble(TimeEntryModel::hours).sum();
    return new TimeEntryCollectionModel(entries, total, safeOffset, safeLimit, totalHours);
  }

  /**
   * Returns the current user's most recent entry. Having none yet is a normal
   * answer, not an error.
   */
  public Map<String, Object> getLastTimeEntry() {
    ActivityRecord last = activityRecordService.getLastActivityRecord(getCurrentUserName());
    Map<String, Object> result = new LinkedHashMap<>();
    if (last == null) {
      result.put("found", false);
      result.put("message", "The user has no time entry yet");
    } else {
      result.put("found", true);
      result.put("entry", toEntryModel(last));
    }
    return result;
  }

  /**
   * Totals a timesheet over a date range: per day, activity, project and
   * client, plus the weekdays that are empty or below an expected number of
   * hours. A time-tracking manager can ask for another user's.
   */
  @SneakyThrows
  public TimesheetSummaryModel getTimesheetSummary(String fromDate,
                                                   String toDate,
                                                   Float expectedDailyHours,
                                                   String username) {
    LocalDate from = LocalDate.parse(checkDate(fromDate, "from_date"));
    LocalDate to = LocalDate.parse(checkDate(toDate, "to_date"));
    if (to.isBefore(from)) {
      throw new IllegalArgumentException("'to_date' must not be before 'from_date'");
    }
    if (ChronoUnit.DAYS.between(from, to) > MAX_RANGE_DAYS) {
      throw new IllegalArgumentException("The date range must not exceed %s days".formatted(MAX_RANGE_DAYS));
    }
    if (expectedDailyHours != null && (expectedDailyHours <= 0 || expectedDailyHours > MAX_HOURS_PER_ENTRY)) {
      throw new IllegalArgumentException("'expected_daily_hours' must be greater than 0 and at most %s"
                                         .formatted(MAX_HOURS_PER_ENTRY));
    }
    String user = StringUtils.isBlank(username) ? getCurrentUserName() : resolveUsers(username, null).get(0);
    List<ActivityRecord> records = queryEntries(from.toString(), to.toString(), user, 0, 0).getActivityRecords();

    Map<String, Float> byDay = new TreeMap<>();
    Map<String, Float> byActivity = new LinkedHashMap<>();
    Map<String, Float> byProject = new LinkedHashMap<>();
    Map<String, Float> byClient = new LinkedHashMap<>();
    float total = 0f;
    for (ActivityRecord r : safe(records)) {
      float h = r.getTime() == null ? 0f : r.getTime();
      total += h;
      byDay.merge(r.getActivityDate(), h, Float::sum);
      addTo(byActivity, r.getActivity() == null ? null : r.getActivity().getLabel(), h);
      addTo(byProject, r.getProject() == null ? null : r.getProject().getLabel(), h);
      addTo(byClient, r.getClient() == null ? null : r.getClient().getLabel(), h);
    }
    List<String> empty = new ArrayList<>();
    List<String> under = new ArrayList<>();
    for (LocalDate d = from; !d.isAfter(to); d = d.plusDays(1)) {
      if (d.getDayOfWeek() == DayOfWeek.SATURDAY || d.getDayOfWeek() == DayOfWeek.SUNDAY) {
        continue;
      }
      Float hours = byDay.get(d.toString());
      if (hours == null) {
        empty.add(d.toString());
      }
      if (expectedDailyHours != null && (hours == null || hours < expectedDailyHours)) {
        under.add(d.toString());
      }
    }
    return new TimesheetSummaryModel(user,
                                     from.toString(),
                                     to.toString(),
                                     total,
                                     safe(records).size(),
                                     byDay,
                                     byActivity,
                                     byProject,
                                     byClient,
                                     empty,
                                     expectedDailyHours == null ? null : under);
  }

  // ==========================================================================
  // Writing entries (always the current user's own)
  // ==========================================================================

  /**
   * Books time for the current user on one of the activities returned by
   * {@link #getTimeTrackerActivities()}.
   */
  @SneakyThrows
  public TimeEntryModel createTimeEntry(Long activityId,
                                        String date,
                                        Float hours,
                                        String description,
                                        String location,
                                        String office,
                                        Long clientId,
                                        Long projectId,
                                        Long salesOrderId,
                                        String projectVersion) {
    if (activityId == null || activityId <= 0) {
      throw new IllegalArgumentException("Parameter 'activity_id' is mandatory");
    }
    checkHours(hours);
    String day = checkDate(date, "date");
    Activity activity = getAllowedActivity(activityId);

    ActivityRecord record = new ActivityRecord();
    record.setUserName(getCurrentUserName());
    record.setActivityDate(day);
    record.setTime(hours);
    record.setDescription(description);
    // both columns are mandatory in the database
    ActivityRecord last = activityRecordService.getLastActivityRecord(getCurrentUserName());
    record.setLocation(resolveLocationOrOffice(location,
                                               "location",
                                               safe(settingsService.getLocationsList()).stream().map(l -> l.getCode()).toList(),
                                               last == null ? null : last.getLocation()));
    record.setOffice(resolveLocationOrOffice(office,
                                             "office",
                                             safe(settingsService.getOfficesList()).stream().map(o -> o.getCode()).toList(),
                                             last == null ? null : last.getOffice()));
    record.setProjectVersion(projectVersion);
    Activity ref = new Activity();
    ref.setId(activity.getId());
    record.setActivity(ref);
    applyReferences(record, clientId, projectId, salesOrderId);
    return toEntryModel(activityRecordService.createActivityRecord(record));
  }

  /**
   * Updates one of the current user's timesheet entries. Only the parameters
   * that are given change; the others keep their value.
   */
  @SneakyThrows
  public TimeEntryModel updateTimeEntry(Long entryId,
                                        Long activityId,
                                        String date,
                                        Float hours,
                                        String description,
                                        String location,
                                        String office,
                                        Long clientId,
                                        Long projectId,
                                        Long salesOrderId,
                                        String projectVersion) {
    ActivityRecord record = getOwnEntry(entryId);
    if (activityId != null && (record.getActivity() == null || !activityId.equals(record.getActivity().getId()))) {
      Activity ref = new Activity();
      ref.setId(getAllowedActivity(activityId).getId());
      record.setActivity(ref);
      // client, project and sales order belong to the previous activity
      record.setClient(null);
      record.setProject(null);
      record.setSalesOrder(null);
    }
    if (StringUtils.isNotBlank(date)) {
      String day = checkDate(date, "date");
      record.setActivityDate(day);
      record.setActivityTime(Date.from(LocalDate.parse(day).atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }
    if (hours != null) {
      checkHours(hours);
      record.setTime(hours);
    }
    if (description != null) {
      record.setDescription(description);
    }
    if (location != null) {
      record.setLocation(location);
    }
    if (office != null) {
      record.setOffice(office);
    }
    if (projectVersion != null) {
      record.setProjectVersion(projectVersion);
    }
    applyReferences(record, clientId, projectId, salesOrderId);
    return toEntryModel(activityRecordService.updateActivityRecord(record, getCurrentUserName()));
  }

  /**
   * Copies one of the current user's entries onto every day of a date range.
   */
  @SneakyThrows
  public TimeEntryDuplicationModel duplicateTimeEntry(Long entryId,
                                                      String fromDate,
                                                      String toDate,
                                                      Boolean includeWeekends) {
    String from = checkDate(fromDate, "from_date");
    String to = checkDate(toDate, "to_date");
    ActivityRecord source = getOwnEntry(entryId);
    TimeEntryModel sourceModel = toEntryModel(source);
    int created = activityRecordService.createActivityRecords(source, from, to, Boolean.TRUE.equals(includeWeekends));
    return new TimeEntryDuplicationModel(sourceModel, created, from, to);
  }

  /**
   * Deletes one of the current user's timesheet entries.
   */
  @SneakyThrows
  public void deleteTimeEntry(Long entryId) {
    if (entryId == null || entryId <= 0) {
      throw new IllegalArgumentException("Parameter 'entry_id' is mandatory");
    }
    try {
      // Same rule as the UI: the owner, or a time-tracking manager, may delete
      activityRecordService.deleteActivityRecord(entryId, getCurrentUserName());
    } catch (EntityNotFoundException e) {
      throw new ObjectNotFoundException("Time entry with id %s not found".formatted(entryId));
    }
  }

  // ==========================================================================
  // Helpers
  // ==========================================================================

  private RecordsAccessList queryEntries(String from, String to, String user, int offset, int limit) {
    return activityRecordService.getActivityRecordsList(null, null, null, null, null, null, null, null, null,
                                                        from, to, user, null, null, offset, limit, SORT_FIELD, true);
  }

  private boolean isManager() {
    ConversationState state = ConversationState.getCurrent();
    return state != null && state.getIdentity() != null && state.getIdentity().isMemberOf(MANAGERS_GROUP);
  }

  private void checkManager() throws IllegalAccessException {
    if (!isManager()) {
      throw new IllegalAccessException("Only time-tracking managers can access other users' time");
    }
  }

  /** Users whose time is asked for; anything but the current user needs a manager. */
  private List<String> resolveUsers(String username, String teamId) throws Exception {
    if (StringUtils.isNotBlank(teamId)) {
      checkManager();
      Set<String> users = new LinkedHashSet<>();
      for (TeamMember member : safe(teamService.getMembersList(teamId))) {
        users.add(member.getUserName());
      }
      return new ArrayList<>(users);
    }
    if (!getCurrentUserName().equals(username)) {
      checkManager();
    }
    return List.of(username);
  }

  private void applyReferences(ActivityRecord record, Long clientId, Long projectId, Long salesOrderId) {
    if (clientId != null) {
      Client client = safe(clientService.getClientsList()).stream()
                                                          .filter(c -> clientId.equals(c.getId()))
                                                          .findFirst()
                                                          .orElseThrow(() -> new IllegalArgumentException("Client %s doesn't exist"
                                                                                                          .formatted(clientId)));
      Client ref = new Client();
      ref.setId(client.getId());
      record.setClient(ref);
    }
    if (projectId != null) {
      Project project = safe(projectService.getProjectsList()).stream()
                                                              .filter(p -> projectId.equals(p.getId()))
                                                              .findFirst()
                                                              .orElseThrow(() -> new IllegalArgumentException("Project %s doesn't exist"
                                                                                                              .formatted(projectId)));
      Project ref = new Project();
      ref.setId(project.getId());
      record.setProject(ref);
    }
    if (salesOrderId != null) {
      SalesOrder order = safe(salesOrderService.getSalesOrdersList()).stream()
                                                                     .filter(o -> salesOrderId.equals(o.getId()))
                                                                     .findFirst()
                                                                     .orElseThrow(() -> new IllegalArgumentException("Sales order %s doesn't exist"
                                                                                                                     .formatted(salesOrderId)));
      SalesOrder ref = new SalesOrder();
      ref.setId(order.getId());
      record.setSalesOrder(ref);
    }
  }

  private void addTo(Map<String, Float> map, String key, float hours) {
    map.merge(StringUtils.defaultIfBlank(key, "(none)"), hours, Float::sum);
  }

  private <T> List<T> safe(List<T> list) {
    return list == null ? Collections.emptyList() : list;
  }

  /**
   * A given value must be one of the configured codes, when any are configured;
   * a missing one falls back to the user's last entry, then to an empty value
   * when nothing is configured, otherwise it is asked for.
   */
  private String resolveLocationOrOffice(String value, String name, List<String> allowed, String fromLastEntry) {
    if (StringUtils.isNotBlank(value)) {
      if (!allowed.isEmpty() && !allowed.contains(value)) {
        throw new IllegalArgumentException("'%s' must be one of %s".formatted(name, allowed));
      }
      return value;
    } else if (StringUtils.isNotBlank(fromLastEntry)) {
      return fromLastEntry;
    } else if (allowed.isEmpty()) {
      return "";
    } else {
      throw new IllegalArgumentException("'%s' is mandatory, one of %s".formatted(name, allowed));
    }
  }

  private void checkHours(Float hours) {
    if (hours == null || hours <= 0 || hours > MAX_HOURS_PER_ENTRY) {
      throw new IllegalArgumentException("Parameter 'hours' must be greater than 0 and at most %s".formatted(MAX_HOURS_PER_ENTRY));
    }
  }

  /**
   * Loads an entry that must belong to the current user: the agent only ever
   * edits or copies its own user's time, even for a time-tracking manager.
   */
  private ActivityRecord getOwnEntry(Long entryId) throws IllegalAccessException, ObjectNotFoundException {
    if (entryId == null || entryId <= 0) {
      throw new IllegalArgumentException("Parameter 'entry_id' is mandatory");
    }
    ActivityRecord record = activityRecordService.getActivityRecordById(entryId);
    if (record == null) {
      throw new ObjectNotFoundException("Time entry with id %s not found".formatted(entryId));
    }
    if (!getCurrentUserName().equals(record.getUserName())) {
      throw new IllegalAccessException("Time entry %s doesn't belong to the user".formatted(entryId));
    }
    return record;
  }

  private List<Activity> getAllowedActivities() throws Exception {
    List<Team> teams = teamService.getTeamsList(getCurrentUserName());
    if (teams == null || teams.isEmpty()) {
      return Collections.emptyList();
    }
    return safe(activityService.getActivitiesforUser(teams.stream().map(Team::getId).toList()));
  }

  private Activity getAllowedActivity(long activityId) throws Exception {
    return getAllowedActivities().stream()
                                 .filter(a -> a.getId() != null && a.getId() == activityId)
                                 .findFirst()
                                 .orElseThrow(() -> new IllegalAccessException("Activity %s doesn't exist or isn't available to the user"
                                                                                .formatted(activityId)));
  }

  private String checkOptionalDate(String value, String name) {
    return StringUtils.isBlank(value) ? null : checkDate(value, name);
  }

  private String checkDate(String value, String name) {
    try {
      return LocalDate.parse(StringUtils.trimToEmpty(value)).toString();
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException(DATE_FORMAT_HINT.formatted(name));
    }
  }

  private TimeActivityModel toActivityModel(Activity a) {
    Project project = a.getProject();
    Client client = project == null ? null : project.getClient();
    return new TimeActivityModel(a.getId(),
                                 a.getCode(),
                                 a.getLabel(),
                                 project == null ? null : project.getLabel(),
                                 client == null ? null : client.getLabel());
  }

  private TimeEntryModel toEntryModel(ActivityRecord r) {
    Activity activity = r.getActivity();
    return new TimeEntryModel(r.getId(),
                              r.getActivityDate(),
                              r.getTime() == null ? 0f : r.getTime(),
                              r.getDescription(),
                              activity == null ? null : activity.getId(),
                              activity == null ? null : activity.getCode(),
                              activity == null ? null : activity.getLabel(),
                              r.getClient() == null ? null : r.getClient().getId(),
                              r.getClient() == null ? null : r.getClient().getLabel(),
                              r.getProject() == null ? null : r.getProject().getId(),
                              r.getProject() == null ? null : r.getProject().getLabel(),
                              r.getSalesOrder() == null ? null : r.getSalesOrder().getId(),
                              r.getSalesOrder() == null ? null : r.getSalesOrder().getName(),
                              r.getProjectVersion(),
                              r.getLocation(),
                              r.getOffice(),
                              r.getUserName());
  }

}
