/*
 * This file is part of the Meeds project (https://meeds.io/).
 * Copyright (C) 2020 Meeds Association
 * contact@meeds.io
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.exoplatform.timetracker.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.QueryParam;

import org.apache.commons.lang.StringUtils;

import org.exoplatform.commons.utils.CommonsUtils;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.timetracker.dto.Activity;
import org.exoplatform.timetracker.dto.ActivityRecord;
import org.exoplatform.timetracker.dto.RecordsAccessList;
import org.exoplatform.timetracker.dto.Team;
import org.exoplatform.timetracker.storage.ActivityRecordStorage;

/**
 * A Service to access and store ActivityRecords
 *
 * @author medamine
 * @version $Id: $Id
 */
public class ActivityRecordService {

  private static final Log            LOG = ExoLogger.getLogger(ActivityRecordService.class);

  private final ActivityRecordStorage activityRecordstorage;

  private final TimeTrackerSettingsService timeTrackerSettingsService;

  private final TeamService teamService;

  private final String DATE_FORMAT = "yyyy-MM-dd";

  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

  /**
   * <p>
   * Constructor for ActivityRecordService.
   * </p>
   *
   * @param activityRecordstorage a
   *          {@link org.exoplatform.timetracker.storage.ActivityRecordStorage}
   *          object.
   */
  public ActivityRecordService(ActivityRecordStorage activityRecordstorage, TimeTrackerSettingsService timeTrackerSettingsService, TeamService teamService) {
    this.activityRecordstorage = activityRecordstorage;
    this.timeTrackerSettingsService = timeTrackerSettingsService;
    this.teamService = teamService;

  }

  /**
   * Create new ActivityRecord
   *
   * @param activityRecord ActivityRecord to create
   * @return stored {@link org.exoplatform.timetracker.dto.ActivityRecord} in
   *         datasource
   * @throws java.lang.Exception when ActivityRecord already exists or an error
   *           occurs while creating ActivityRecord or its attached image
   */
  public ActivityRecord createActivityRecord(ActivityRecord activityRecord) throws Exception {
    if (activityRecord == null) {
      throw new IllegalArgumentException("ActivityRecord is mandatory");
    }
    return activityRecordstorage.createActivityRecord(activityRecord);
  }

  /**
   * Update an existing ActivityRecord on datasource. If the ActivityRecord
   * doesn't exit an {@link javax.persistence.EntityNotFoundException} will be
   * thrown.
   *
   * @param activityRecord dto to update on store
   * @param username username storing ActivityRecord
   * @return stored {@link org.exoplatform.timetracker.dto.ActivityRecord} in
   *         datasource
   * @throws java.lang.Exception when {@link java.lang.Exception} is thrown or an
   *           error occurs while saving ActivityRecord
   */
  public ActivityRecord updateActivityRecord(ActivityRecord activityRecord, String username) throws Exception {
    if (activityRecord == null) {
      throw new IllegalArgumentException("ActivityRecord is mandatory");
    }
    if (StringUtils.isBlank(username)) {
      throw new IllegalArgumentException("username is mandatory");
    }
    Long ActivityRecordId = activityRecord.getId();
    if (ActivityRecordId == null) {
      throw new EntityNotFoundException("ActivityRecord with null id wasn't found");
    }
    ActivityRecord storedActivityRecord = activityRecordstorage.getActivityRecordById(ActivityRecordId);
    if (storedActivityRecord == null) {
      throw new EntityNotFoundException("ActivityRecord with id " + ActivityRecordId + " wasn't found");
    }
    return activityRecordstorage.updateActivityRecord(activityRecord);
  }

  /**
   * Delete ActivityRecord identified by its id and check if username has
   * permission to delete it.
   *
   * @param activityRecordId technical identifier of ActivityRecord
   * @param username user currently deleting ActivityRecord
   * @throws javax.persistence.EntityNotFoundException if ActivityRecord wasn't
   *           found
   * @throws java.lang.IllegalAccessException if user is not allowed to delete
   *           ActivityRecord
   */
  public void deleteActivityRecord(Long activityRecordId, String username) throws EntityNotFoundException,
                                                                           IllegalAccessException {
    if (activityRecordId == null || activityRecordId <= 0) {
      throw new IllegalArgumentException("ActivityRecordId must be a positive integer");
    }
    if (StringUtils.isBlank(username)) {
      throw new IllegalArgumentException("username is mandatory");
    }

    ActivityRecord storedActivityRecord = activityRecordstorage.getActivityRecordById(activityRecordId);
    if (storedActivityRecord == null) {
      throw new EntityNotFoundException("ActivityRecord with id " + activityRecordId + " not found");
    }
    activityRecordstorage.deleteActivityRecord(activityRecordId);
  }

  /**
   * Retrieves the list of ActivityRecords with offset, limit and a keyword that
   * can be empty
   *
   * @return List of {@link org.exoplatform.timetracker.dto.ActivityRecord} that
   *         contains the list of ActivityRecords
   */
  public List<ActivityRecord> getActivityRecords() {
    return activityRecordstorage.getActivityRecords();
  }

  /**
   * Retrieves the list of ActivityRecordsListAccess with offset, limit and a
   * keyword that can be empty
   *
   * @return List of {@link org.exoplatform.timetracker.dto.ActivityRecord} that
   *         contains the list of ActivityRecords
   * @param search a {@link java.lang.String} object.
   * @param activity a {@link java.lang.Long} object.
   * @param type a {@link java.lang.Long} object.
   * @param subType a {@link java.lang.Long} object.
   * @param activityCode a {@link java.lang.Long} object.
   * @param subActivityCode a {@link java.lang.Long} object.
   * @param client a {@link java.lang.Long} object.
   * @param project a {@link java.lang.Long} object.
   * @param feature a {@link java.lang.Long} object.
   * @param fromDate a {@link java.lang.String} object.
   * @param toDate a {@link java.lang.String} object.
   * @param userName a {@link java.lang.String} object.
   * @param location a {@link java.lang.String} object.
   * @param office a {@link java.lang.String} object.
   * @param offset a int.
   * @param limit a int.
   * @param sortBy a {@link java.lang.String} object.
   * @param sortDesc a boolean.
   */
  public RecordsAccessList getActivityRecordsList(String search,
                                                  Long activity,
                                                  Long type,
                                                  Long subType,
                                                  Long activityCode,
                                                  Long subActivityCode,
                                                  Long client,
                                                  Long project,
                                                  Long feature,
                                                  String fromDate,
                                                  String toDate,
                                                  String userName,
                                                  String location,
                                                  String office,
                                                  int offset,
                                                  int limit,
                                                  String sortBy,
                                                  boolean sortDesc) {
    return activityRecordstorage.getActivityRecordsList(search,
                                                        activity,
                                                        type,
                                                        subType,
                                                        activityCode,
                                                        subActivityCode,
                                                        client,
                                                        project,
                                                        feature,
                                                        fromDate,
                                                        toDate,
                                                        userName,
                                                        location,
                                                        office,
                                                        offset,
                                                        limit,
                                                        sortBy,
                                                        sortDesc);
  }

  /**
   * Retrieves the list of ActivityRecordsListAccess with offset, limit and a
   * keyword that can be empty
   *
   * @return List of {@link org.exoplatform.timetracker.dto.ActivityRecord} that
   *         contains the list of ActivityRecords
   * @param userName a {@link java.lang.String} object.
   */
  public ActivityRecord getLastActivityRecord(String userName) {
    return activityRecordstorage.getLastActivityRecord(userName);
  }

  /**
   * Retrieves the list of ActivityRecordsListAccess with offset, limit and a
   * keyword that can be empty
   *
   * @return List of {@link org.exoplatform.timetracker.dto.ActivityRecord} that
   *         contains the list of ActivityRecords
   * @param search a {@link java.lang.String} object.
   * @param activity a {@link java.lang.Long} object.
   * @param type a {@link java.lang.Long} object.
   * @param subType a {@link java.lang.Long} object.
   * @param activityCode a {@link java.lang.Long} object.
   * @param subActivityCode a {@link java.lang.Long} object.
   * @param client a {@link java.lang.Long} object.
   * @param project a {@link java.lang.Long} object.
   * @param feature a {@link java.lang.Long} object.
   * @param fromDate a {@link java.lang.String} object.
   * @param toDate a {@link java.lang.String} object.
   * @param userName a {@link java.lang.String} object.
   * @param location a {@link java.lang.String} object.
   * @param office a {@link java.lang.String} object.
   */
  public long countActivityRecords(String search,
                                   Long activity,
                                   Long type,
                                   Long subType,
                                   Long activityCode,
                                   Long subActivityCode,
                                   Long client,
                                   Long project,
                                   Long feature,
                                   String fromDate,
                                   String toDate,
                                   String userName,
                                   String location,
                                   String office) {
    return activityRecordstorage.countActivityRecords(search,
                                                      activity,
                                                      type,
                                                      subType,
                                                      activityCode,
                                                      subActivityCode,
                                                      client,
                                                      project,
                                                      feature,
                                                      fromDate,
                                                      toDate,
                                                      userName,
                                                      location,
                                                      office);
  }

  /**
   * Retrieves the list of ActivityRecords wby day
   *
   * @param day day of activity
   * @return List of {@link org.exoplatform.timetracker.dto.ActivityRecord} that
   *         contains the list of ActivityRecords
   * @param userName a {@link java.lang.String} object.
   */
  public List<ActivityRecord> getUserActivityRecordsList(String day, String userName) {
    return activityRecordstorage.getUserActivityRecords(day, userName);
  }

  public String generateTSCode(List<Team> teams, ActivityRecord record, String exportType) {
    String team = "";
    String tsCode = "";
    if (teams != null && teams.size() > 0) {
      team = teams.get(0).getName();
    }
    tsCode = String.valueOf(record.getActivityTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear());
    tsCode = tsCode + "_" + record.getOffice();
    String type="";
    String subType="";
    if (record.getActivity() != null && record.getActivity().getType() != null) {
      type= record.getActivity().getType().getCode();
      tsCode = tsCode + "_" + type;
    }
    if(exportType.equals("fr")){
      if (record.getActivity() != null && record.getActivity().getSubType() != null) {
        subType = record.getActivity().getSubType().getCode();
        if (subType.equals("FIXP")) {
          subType = "CONS";
        }
      }
      if (team.equals("Architects") || team.equals("Designers") || team.equals("Management") || team.equals("Analysts") || team.equals("ITOP")) {
        if(type.equals("SERV")  || ( subType.equals("FIXP") || subType.equals("CONS") || subType.equals("HOST"))){
          tsCode = tsCode + getClient(record);
        }
      } else  {
        if(type.equals("SERV")  || ( subType.equals("FIXP") || subType.equals("CONS"))){
          tsCode = tsCode + getClient(record);
        }
      }

    }else {
      if (record.getActivity() != null && record.getActivity().getSubType() != null) {
        tsCode = tsCode + "_" + record.getActivity().getSubType().getCode();
      }
      if (team.equals("Analysts") || team.equals("ITOP") || team.equals("Dev Squad")) {
        tsCode = tsCode + getClient(record);
      } else if (team.equals("Architects") || team.equals("Designers")) {
        tsCode = tsCode + getClient(record) + getProject(record) + getActivity(record) + geSubActivity(record);
      } else if (team.equals("Support")) {
        tsCode = tsCode + getClient(record) + getActivity(record);
      } else if (team.equals("QA")) {
        tsCode = tsCode + getClient(record) + getProject(record);
      }
    }
    return tsCode;
  }

  String getProject(ActivityRecord record) {
    String project = "";
    if (record.getActivity() != null && record.getActivity().getProject() != null) {
      if (record.getActivity().getProject().getCode().equals("<PRJ>")) {
        if (record.getProject() != null) {
          project = "_" + record.getProject().getCode();
        }
      } else {
        project = "_" + record.getActivity().getProject().getCode();
      }
    }
    return project;
  }

  String getClient(ActivityRecord record) {
    String client = "";
    if (record.getActivity() != null && record.getActivity().getProject() != null
        && record.getActivity().getProject().getClient() != null) {
      if (record.getActivity().getProject().getClient().getCode().equals("<CLNT>")) {
        if (record.getClient() != null) {
          client = "_" + record.getClient().getCode();
        }
      } else {
        client = "_" + record.getActivity().getProject().getClient().getCode();
      }
    }
    return client;
  }

  String getActivity(ActivityRecord record) {
    String activity = "";
    if (record.getActivity() != null && record.getActivity().getActivityCode() != null) {
      activity = "_" + record.getActivity().getActivityCode().getCode();
    }
    return activity;
  }

  String geSubActivity(ActivityRecord record) {
    String subActivity = "";
    if (record.getActivity() != null && record.getActivity().getSubActivityCode() != null) {
      subActivity = "_" + record.getActivity().getSubActivityCode().getCode();
    }
    return subActivity;
  }


  public List<ActivityRecord> getUserActivityRecords(String search, long activity, long type, long subType, long activityCode, long subActivityCode, long client, long project, long feature, String fromDate, String toDate, String userName, String location, String office, String sortBy, Boolean sortDesc, Boolean export, String exportType) {

    RecordsAccessList recordsAccessList = getActivityRecordsList(search, activity, type, subType, activityCode, subActivityCode, client, project, feature, fromDate, toDate, userName, location, office, 0, 0, sortBy, sortDesc);
    IdentityManager identityManager = CommonsUtils.getService(IdentityManager.class);

    List<ActivityRecord> act = new ArrayList<>();
    List<ActivityRecord> activityRecordList = new ArrayList<>();
    LocalDate from_ = LocalDate.now();
    LocalDate to_ = LocalDate.now();
    Activity weekEndActivity = timeTrackerSettingsService.getSettings().getWeekEndHolidayActivity();
    try {
      if (StringUtils.isNotEmpty(fromDate)) {
        from_ = LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse(fromDate));
      } else {
        String from = recordsAccessList.getActivityRecords().get(recordsAccessList.getSize().intValue() - 1).getActivityDate();
        from_ = LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse(from));
      }
      if (StringUtils.isNotEmpty(toDate)) {
        to_ = LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse(toDate));
      } else {
        String to = recordsAccessList.getActivityRecords().get(0).getActivityDate();
        to_ = LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse(to));
      }
      String office_ = "";
      for (LocalDate d : getDatesBetween(from_, to_)) {
        String day = d.format(formatter);
        act = recordsAccessList.getActivityRecords().stream().filter(c -> c.getActivityDate().equals(day)).collect(Collectors.toList());
        if (act.size() > 0) {
          float TimeSum = act.stream().map(x -> x.getTime()).reduce(0.0f, (a, b) -> a + b);
          for (ActivityRecord activityRecord : act) {
            activityRecord.setDailyTimeSum(TimeSum);
            if (activityRecord.getActivity() != null && activityRecord.getProject() != null) {
              activityRecord.getActivity().setProject(activityRecord.getProject());
              if (activityRecord.getClient() != null) {
                activityRecord.getActivity().getProject().setClient(activityRecord.getClient());
              }
            }
            if (export) {
              activityRecord.setTsCode(generateTSCode(teamService.getTeamsList(userName), activityRecord, exportType));
            }
            if (StringUtils.isNotEmpty(activityRecord.getOffice())) {
              office_ = activityRecord.getOffice();
            }
            activityRecordList.add(activityRecord);
          }
        } else {
          Date actDate = Date.from(d.atStartOfDay(ZoneId.systemDefault()).toInstant());
          DayOfWeek dayOfWeek = d.getDayOfWeek();
          if (dayOfWeek.getValue() == 6 || dayOfWeek.getValue() == 7) {
            ActivityRecord weekEndRecord = new ActivityRecord(null, userName, day, actDate, "Week End", "", office_, null, "", null, weekEndActivity, null, null, identityManager.getOrCreateUserIdentity(userName).getProfile().getFullName(), null);
            if (export) {
              weekEndRecord.setTsCode(generateTSCode(teamService.getTeamsList(userName), weekEndRecord, exportType));
            }
            activityRecordList.add(weekEndRecord);
          } else {
            activityRecordList.add(new ActivityRecord(null, userName, day, actDate, "", "", "", null, "", null, null, null, null, identityManager.getOrCreateUserIdentity(userName).getProfile().getFullName(), null));
          }
        }
      }
    } catch (Exception e) {
      LOG.error("Cannot parse from date, the to date filer will not applied to get the list of activityRecords");
    }
    return activityRecordList;
  }

  public static List<LocalDate> getDatesBetween(
          LocalDate startDate, LocalDate endDate) {

    long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate);
    return IntStream.iterate(0, i -> i + 1)
            .limit(numOfDaysBetween+1)
            .mapToObj(i -> startDate.plusDays(i))
            .collect(Collectors.toList());
  }
}
