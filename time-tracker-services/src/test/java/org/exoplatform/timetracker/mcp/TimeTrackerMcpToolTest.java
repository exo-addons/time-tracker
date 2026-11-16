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

import static io.meeds.mcp.server.util.McpToolUtils.toSnakeCase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.exoplatform.services.security.ConversationState;
import org.exoplatform.services.security.Identity;
import org.exoplatform.timetracker.dto.Activity;
import org.exoplatform.timetracker.dto.ActivityRecord;
import org.exoplatform.timetracker.dto.Team;
import org.exoplatform.timetracker.mcp.model.TimeEntryModel;
import org.exoplatform.timetracker.service.ActivityRecordService;
import org.exoplatform.timetracker.service.ActivityService;
import org.exoplatform.timetracker.service.ClientService;
import org.exoplatform.timetracker.service.ProjectService;
import org.exoplatform.timetracker.service.SalesOrderService;
import org.exoplatform.timetracker.service.TeamService;
import org.exoplatform.timetracker.service.TimeTrackerSettingsService;
import org.exoplatform.timetracker.dto.Client;
import org.exoplatform.timetracker.dto.RecordsAccessList;
import org.exoplatform.timetracker.dto.TeamMember;
import org.exoplatform.timetracker.mcp.model.TimeEntryCollectionModel;
import org.exoplatform.timetracker.mcp.model.TimesheetSummaryModel;

public class TimeTrackerMcpToolTest {

  private ActivityRecordService recordService = mock(ActivityRecordService.class);

  private ActivityService       activityService = mock(ActivityService.class);

  private TeamService           teamService = mock(TeamService.class);

  private ClientService         clientService = mock(ClientService.class);

  private ProjectService        projectService = mock(ProjectService.class);

  private SalesOrderService     salesOrderService = mock(SalesOrderService.class);

  private TimeTrackerSettingsService settingsService = mock(TimeTrackerSettingsService.class);

  private TimeTrackerMcpTool    tool;

  @Before
  public void setUp() throws Exception {
    ConversationState.setCurrent(new ConversationState(new Identity("john")));
    tool = new TimeTrackerMcpTool(recordService, activityService, teamService, clientService, projectService, salesOrderService, settingsService);
    Team team = new Team("t1", "Team", "");
    when(teamService.getTeamsList("john")).thenReturn(List.of(team));
    Activity activity = new Activity();
    activity.setId(5L);
    when(activityService.getActivitiesforUser(List.of("t1"))).thenReturn(List.of(activity));
  }

  @After
  public void tearDown() {
    ConversationState.setCurrent(null);
  }

  /** A tool method without an entry in the definitions is dropped silently by the server. */
  @Test
  public void everyToolMethodHasADefinitionAndViceVersa() throws Exception {
    Set<String> declared = Stream.of(TimeTrackerMcpTool.class.getDeclaredMethods())
                                 .filter(m -> Modifier.isPublic(m.getModifiers()) && !m.isSynthetic())
                                 .map(Method::getName)
                                 .map(n -> toSnakeCase(n))
                                 .collect(Collectors.toCollection(TreeSet::new));
    Set<String> defined = new TreeSet<>();
    try (InputStream in = getClass().getClassLoader().getResourceAsStream("ai-tool-definitions.json")) {
      for (JsonNode t : new ObjectMapper().readTree(in).get("tools")) {
        defined.add(t.get("name").asText());
      }
    }
    assertEquals(declared, defined);
  }

  @Test
  public void createTimeEntryAssignsCurrentUser() throws Exception {
    when(recordService.createActivityRecord(any())).thenAnswer(i -> {
      ActivityRecord r = i.getArgument(0);
      r.setId(9L);
      return r;
    });
    TimeEntryModel e = tool.createTimeEntry(5L, "2026-11-16", 7.5f, "work", null, null, null, null, null, null);
    assertEquals(Long.valueOf(9L), e.id());
    assertEquals("john", e.username());
    assertEquals(7.5f, e.hours(), 0);
  }

  @Test
  public void createTimeEntryRefusesActivityOutsideUserTeams() throws Exception {
    assertThrows(IllegalAccessException.class, () -> tool.createTimeEntry(99L, "2026-11-16", 1f, null, null, null, null, null, null, null));
    verify(recordService, never()).createActivityRecord(any());
  }

  @Test
  public void createTimeEntryValidatesInput() {
    assertThrows(IllegalArgumentException.class, () -> tool.createTimeEntry(5L, "16/11/2026", 1f, null, null, null, null, null, null, null));
    assertThrows(IllegalArgumentException.class, () -> tool.createTimeEntry(5L, "2026-11-16", 0f, null, null, null, null, null, null, null));
    assertThrows(IllegalArgumentException.class, () -> tool.createTimeEntry(5L, "2026-11-16", 25f, null, null, null, null, null, null, null));
    assertThrows(IllegalArgumentException.class, () -> tool.createTimeEntry(null, "2026-11-16", 1f, null, null, null, null, null, null, null));
  }

  @Test
  public void deleteGoesThroughOwnershipCheckAsCurrentUser() throws Exception {
    tool.deleteTimeEntry(3L);
    verify(recordService).deleteActivityRecord(3L, "john");
    assertTrue(true);
  }

  private ActivityRecord entry(long id, String user) {
    ActivityRecord r = new ActivityRecord();
    r.setId(id);
    r.setUserName(user);
    r.setActivityDate("2026-11-10");
    r.setTime(2f);
    r.setDescription("old");
    return r;
  }

  @Test
  public void updateChangesOnlyGivenFields() throws Exception {
    when(recordService.getActivityRecordById(3L)).thenReturn(entry(3L, "john"));
    when(recordService.updateActivityRecord(any(), org.mockito.ArgumentMatchers.eq("john"))).thenAnswer(i -> i.getArgument(0));
    TimeEntryModel e = tool.updateTimeEntry(3L, null, "2026-11-12", 4f, null, null, null, null, null, null, null);
    assertEquals("2026-11-12", e.date());
    assertEquals(4f, e.hours(), 0);
    assertEquals("old", e.description());
    assertEquals("john", e.username());
  }

  @Test
  public void updateRefusesSomeoneElsesEntryAndForeignActivity() throws Exception {
    when(recordService.getActivityRecordById(3L)).thenReturn(entry(3L, "mary"));
    assertThrows(IllegalAccessException.class, () -> tool.updateTimeEntry(3L, null, null, 1f, null, null, null, null, null, null, null));
    when(recordService.getActivityRecordById(4L)).thenReturn(entry(4L, "john"));
    assertThrows(IllegalAccessException.class, () -> tool.updateTimeEntry(4L, 99L, null, null, null, null, null, null, null, null, null));
    verify(recordService, never()).updateActivityRecord(any(), any());
  }

  @Test
  public void duplicateCopiesOwnEntryOverRange() throws Exception {
    when(recordService.getActivityRecordById(3L)).thenReturn(entry(3L, "john"));
    when(recordService.createActivityRecords(any(), org.mockito.ArgumentMatchers.eq("2026-11-16"),
                                             org.mockito.ArgumentMatchers.eq("2026-11-20"),
                                             org.mockito.ArgumentMatchers.eq(false))).thenReturn(5);
    assertEquals(5, tool.duplicateTimeEntry(3L, "2026-11-16", "2026-11-20", null).created());
  }

  @Test
  public void duplicateRefusesSomeoneElsesEntry() throws Exception {
    when(recordService.getActivityRecordById(3L)).thenReturn(entry(3L, "mary"));
    assertThrows(IllegalAccessException.class, () -> tool.duplicateTimeEntry(3L, "2026-11-16", "2026-11-20", false));
    verify(recordService, never()).createActivityRecords(any(), any(), any(), org.mockito.ArgumentMatchers.anyBoolean());
  }

  private ActivityRecord dayEntry(long id, String day, float hours, String user) {
    ActivityRecord r = entry(id, user);
    r.setActivityDate(day);
    r.setTime(hours);
    return r;
  }

  private void asManager() {
    Identity id = new Identity("john");
    id.setMemberships(List.of(new org.exoplatform.services.security.MembershipEntry("/platform/time-tracking-managers", "*")));
    ConversationState.setCurrent(new ConversationState(id));
  }

  private RecordsAccessList page(ActivityRecord... records) {
    return new RecordsAccessList(List.of(records), (long) records.length);
  }

  @Test
  public void createAttachesClientProjectAndSalesOrderAndRejectsUnknownOnes() throws Exception {
    Client client = new Client();
    client.setId(7L);
    when(clientService.getClientsList()).thenReturn(List.of(client));
    when(recordService.createActivityRecord(any())).thenAnswer(i -> i.getArgument(0));
    TimeEntryModel e = tool.createTimeEntry(5L, "2026-11-16", 1f, null, null, null, 7L, null, null, "v1");
    assertEquals(Long.valueOf(7L), e.clientId());
    assertEquals("v1", e.projectVersion());
    assertThrows(IllegalArgumentException.class,
                 () -> tool.createTimeEntry(5L, "2026-11-16", 1f, null, null, null, 8L, null, null, null));
  }

  @Test
  public void lastEntryAndMissingLastEntry() throws Exception {
    when(recordService.getLastActivityRecord("john")).thenReturn(entry(3L, "john"));
    assertEquals(Long.valueOf(3L), tool.getLastTimeEntry().id());
    when(recordService.getLastActivityRecord("john")).thenReturn(null);
    assertThrows(org.exoplatform.commons.exception.ObjectNotFoundException.class, () -> tool.getLastTimeEntry());
  }

  @Test
  public void summaryTotalsAndFindsMissingWeekdays() throws Exception {
    // Mon 2026-11-16 to Sun 2026-11-22: Mon 8h, Tue 4h, rest empty
    when(recordService.getActivityRecordsList(any(), any(), any(), any(), any(), any(), any(), any(), any(),
                                              org.mockito.ArgumentMatchers.eq("2026-11-16"),
                                              org.mockito.ArgumentMatchers.eq("2026-11-22"),
                                              org.mockito.ArgumentMatchers.eq("john"), any(), any(),
                                              org.mockito.ArgumentMatchers.eq(0), org.mockito.ArgumentMatchers.eq(0),
                                              any(), org.mockito.ArgumentMatchers.anyBoolean())).thenReturn(page(dayEntry(1, "2026-11-16", 8f, "john"),
                                                                                                                   dayEntry(2, "2026-11-17", 4f, "john")));
    TimesheetSummaryModel s = tool.getTimesheetSummary("2026-11-16", "2026-11-22", 8f, null);
    assertEquals(12f, s.totalHours(), 0);
    assertEquals(List.of("2026-11-18", "2026-11-19", "2026-11-20"), s.emptyWeekdays());
    assertEquals(List.of("2026-11-17", "2026-11-18", "2026-11-19", "2026-11-20"), s.underExpectedWeekdays());
    assertThrows(IllegalArgumentException.class, () -> tool.getTimesheetSummary("2026-11-16", "2026-11-01", null, null));
    assertThrows(IllegalArgumentException.class, () -> tool.getTimesheetSummary("2025-01-01", "2026-11-01", null, null));
  }

  @Test
  public void otherUsersTimeIsReservedToManagers() throws Exception {
    assertThrows(IllegalAccessException.class, () -> tool.listTimeEntries(null, null, null, null, "mary", null));
    assertThrows(IllegalAccessException.class, () -> tool.listTimeEntries(null, null, null, null, null, "t1"));
    assertThrows(IllegalAccessException.class, () -> tool.getTimesheetSummary("2026-11-16", "2026-11-17", null, "mary"));
    assertThrows(IllegalAccessException.class, () -> tool.listTimeTrackerTeams());
  }

  @Test
  public void managerListsATeamsEntriesMergedAndPaged() throws Exception {
    asManager();
    TeamMember a = new TeamMember();
    a.setUserName("mary");
    TeamMember b = new TeamMember();
    b.setUserName("bob");
    when(teamService.getMembersList("t1")).thenReturn(List.of(a, b));
    when(recordService.getActivityRecordsList(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(),
                                              org.mockito.ArgumentMatchers.eq("mary"), any(), any(),
                                              org.mockito.ArgumentMatchers.anyInt(), org.mockito.ArgumentMatchers.anyInt(),
                                              any(), org.mockito.ArgumentMatchers.anyBoolean())).thenReturn(page(dayEntry(1, "2026-11-10", 1f, "mary")));
    when(recordService.getActivityRecordsList(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(),
                                              org.mockito.ArgumentMatchers.eq("bob"), any(), any(),
                                              org.mockito.ArgumentMatchers.anyInt(), org.mockito.ArgumentMatchers.anyInt(),
                                              any(), org.mockito.ArgumentMatchers.anyBoolean())).thenReturn(page(dayEntry(2, "2026-11-12", 2f, "bob")));
    TimeEntryCollectionModel r = tool.listTimeEntries(null, null, 0, 1, null, "t1");
    assertEquals(2, r.total());
    assertEquals(1, r.entries().size());
    assertEquals("bob", r.entries().get(0).username());
  }
}
