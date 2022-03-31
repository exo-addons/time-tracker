package org.exoplatform.timetracker.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.exoplatform.timetracker.dto.*;

import org.exoplatform.timetracker.storage.ActivityRecordStorage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public class ActivityRecordServiceTest extends TestCase {

	private ActivityRecordStorage activityRecordStorage;
	private TimeTrackerSettingsService timeTrackerSettingsService;
	private TeamService teamService;
	private ActivityRecordService activityRecordService;

	@Before
	public void setUp() throws Exception {
		activityRecordStorage = mock(ActivityRecordStorage.class);
		activityRecordService = new ActivityRecordService(activityRecordStorage, timeTrackerSettingsService,
				teamService);
	}

	@Test
	public void testCreateActivityRecord() throws Exception {
		Client client = new Client(1l, "test", "test");
		Project project = new Project(1l, "test", "test", client);
		SalesOrder salesOrder = new SalesOrder(1l, "test", "test", client);
		Type type = new Type(1l, "test", "test");
		SubType subType = new SubType(1l, "test", "test", type);
		ActivityCode activityCode = new ActivityCode(1l, "test", "test");
		SubActivityCode subActivityCode = new SubActivityCode(1l, "test", "test");
		Feature feature = new Feature(1l, "test", "test", "test", "test", project);
		Team team = new Team("1", "test", "test");
		List<Team> teams = new ArrayList<>();
		teams.add(team);
		Activity activity = new Activity(1l, type, subType, activityCode, subActivityCode, "test", project, feature,
				teams);
		Date testDate = new Date();
		ActivityRecord activityRecord = new ActivityRecord(1l, "root", "12/12/2020", testDate, "test", "test", "test",
				5f, "test", client, activity, salesOrder, testDate, "RootRoot", project);
		when(activityRecordStorage.createActivityRecord(activityRecord)).thenReturn(activityRecord);
		// When
		ActivityRecord newActivityRecordService = null;
		newActivityRecordService = activityRecordService.createActivityRecord(activityRecord);
		// Then
		assertNotNull(newActivityRecordService);
		verify(activityRecordStorage, times(1)).createActivityRecord(any());
	}

	@Test
	public void testUpdateActivityRecord() throws Exception {
		Client client = new Client(1l, "test", "test");
		Project project = new Project(1l, "test", "test", client);
		SalesOrder salesOrder = new SalesOrder(1l, "test", "test", client);
		Type type = new Type(1l, "test", "test");
		SubType subType = new SubType(1l, "test", "test", type);
		ActivityCode activityCode = new ActivityCode(1l, "test", "test");
		SubActivityCode subActivityCode = new SubActivityCode(1l, "test", "test");
		Feature feature = new Feature(1l, "test", "test", "test", "test", project);
		Team team = new Team("1", "test", "test");
		List<Team> teams = new ArrayList<>();
		teams.add(team);
		Activity activity = new Activity(1l, type, subType, activityCode, subActivityCode, "test", project, feature,
				teams);
		Date testDate = new Date();
		Client clientUpdated = new Client(1l, "test", "test");
		Project projectUpdated = new Project(1l, "test", "test", clientUpdated);
		SalesOrder salesOrderUpdated = new SalesOrder(1l, "test", "test", clientUpdated);
		Type typeUpdated = new Type(1l, "test", "test");
		SubType subTypeUpdated = new SubType(1l, "test", "test", typeUpdated);
		ActivityCode activityCodeUpdated = new ActivityCode(1l, "test", "test");
		SubActivityCode subActivityCodeUpdated = new SubActivityCode(1l, "test", "test");
		Feature featureUpdated = new Feature(1l, "test", "test", "test", "test", projectUpdated);
		Team teamUpdated = new Team("1", "test", "test");
		List<Team> teamsUpdated = new ArrayList<>();
		teamsUpdated.add(teamUpdated);
		Activity activityUpdated = new Activity(1l, typeUpdated, subTypeUpdated, activityCodeUpdated,
				subActivityCodeUpdated, "test", projectUpdated, featureUpdated, teamsUpdated);
		Date testDateUpdated = new Date();

		ActivityRecord newActivityRecord = null;
		ActivityRecord activityRecord = new ActivityRecord(1l, "root", "12/12/2020", testDate, "test", "test", "test",
				5f, "test", client, activity, salesOrder, testDate, "RootRoot", project);
		ActivityRecord activityRecordUpdated = new ActivityRecord(activityRecord.getId(), "root", "12/12/2020",
				testDateUpdated, "test", "test", "test", 5f, "test", clientUpdated, activityUpdated, salesOrderUpdated,
				testDateUpdated, "RootRoot", projectUpdated);

		when(activityRecordStorage.getActivityRecordById(anyLong())).thenReturn(activityRecord);
		when(activityRecordService.updateActivityRecord(activityRecord, "root")).thenReturn(activityRecordUpdated);

		// When
		newActivityRecord = activityRecordService.updateActivityRecord(activityRecord, "root");

		// Then
		assertNotNull(newActivityRecord);
		assertEquals((long) newActivityRecord.getId(), 1l);
		assertEquals(newActivityRecord, activityRecordUpdated);
		verify(activityRecordStorage, times(1)).updateActivityRecord(any());
	}

	@Test
	public void testDeleteActivityRecord() throws EntityNotFoundException, IllegalAccessException {
		Client client = new Client(1l, "test", "test");
		Project project = new Project(1l, "test", "test", client);
		SalesOrder salesOrder = new SalesOrder(1l, "test", "test", client);
		Type type = new Type(1l, "test", "test");
		SubType subType = new SubType(1l, "test", "test", type);
		ActivityCode activityCode = new ActivityCode(1l, "test", "test");
		SubActivityCode subActivityCode = new SubActivityCode(1l, "test", "test");
		Feature feature = new Feature(1l, "test", "test", "test", "test", project);
		Team team = new Team("1", "test", "test");
		List<Team> teams = new ArrayList<>();
		teams.add(team);
		Activity activity = new Activity(1l, type, subType, activityCode, subActivityCode, "test", project, feature,
				teams);
		Date testDate = new Date();
		ActivityRecord activityRecord = new ActivityRecord(1l, "root", "12/12/2020", testDate, "test", "test", "test",
				5f, "test", client, activity, salesOrder, testDate, "RootRoot", project);
		doNothing().when(activityRecordStorage).deleteActivityRecord(activityRecord.getId());
		when(activityRecordStorage.getActivityRecordById(activityRecord.getId())).thenReturn(activityRecord);

		// When
		activityRecordService.deleteActivityRecord(activityRecord.getId(), "root");

		// Then
		verify(activityRecordStorage, times(1)).deleteActivityRecord(anyLong());
	}

	@Test
	public void testGetActivityRecords() {
		Client client = new Client(1l, "test", "test");
		Project project = new Project(1l, "test", "test", client);
		SalesOrder salesOrder = new SalesOrder(1l, "test", "test", client);
		Type type = new Type(1l, "test", "test");
		SubType subType = new SubType(1l, "test", "test", type);
		ActivityCode activityCode = new ActivityCode(1l, "test", "test");
		SubActivityCode subActivityCode = new SubActivityCode(1l, "test", "test");
		Feature feature = new Feature(1l, "test", "test", "test", "test", project);
		Team team = new Team("1", "test", "test");
		List<Team> teams = new ArrayList<>();
		teams.add(team);
		Activity activity = new Activity(1l, type, subType, activityCode, subActivityCode, "test", project, feature,
				teams);
		Date testDate = new Date();
		ActivityRecord activityRecord = new ActivityRecord(1l, "root", "12/12/2020", testDate, "test", "test", "test",
				5f, "test", client, activity, salesOrder, testDate, "RootRoot", project);
		List<ActivityRecord> activitiesRecords = new ArrayList<>();
		activitiesRecords.add(activityRecord);
		when(activityRecordStorage.getActivityRecords()).thenReturn(activitiesRecords);

		// When
		List<ActivityRecord> activitiesRecordsList = activityRecordService.getActivityRecords();

		// Then
		assertNotNull(activitiesRecordsList);
		assertEquals(1, activitiesRecordsList.size());
		verify(activityRecordStorage, times(1)).getActivityRecords();
	}

	@Test
	public void testGetActivityRecordsList() {
		String searchParam = "test";
		String activityParam = "test";
		String typeParam = "test";
		String subTypeParam = "test";
		String activityCodeParam = "test";
		String subActivityCodeParam = "test";
		String clientParam = "test";
		String projectParam = "test";
		String featureParam = "test";
		String fromDateParam = "test";
		String toDateParam = "test";
		String userNameParam = "root";
		String locationParam = "test";
		String officeParam = "test";
		int offsetParam = 1;
		int limitParam = 1;
		boolean sortDescParam = true;
		Client client = new Client(1l, "test", "test");
		Project project = new Project(1l, "test", "test", client);
		SalesOrder salesOrder = new SalesOrder(1l, "test", "test", client);
		Type type = new Type(1l, "test", "test");
		SubType subType = new SubType(1l, "test", "test", type);
		ActivityCode activityCode = new ActivityCode(1l, "test", "test");
		SubActivityCode subActivityCode = new SubActivityCode(1l, "test", "test");
		Feature feature = new Feature(1l, "test", "test", "test", "test", project);
		Team team = new Team("1", "test", "test");
		List<Team> teams = new ArrayList<>();
		teams.add(team);
		Activity activity = new Activity(1l, type, subType, activityCode, subActivityCode, "test", project, feature,
				teams);
		Date testDate = new Date();
		ActivityRecord activityRecord = new ActivityRecord(1l, "root", "12/12/2020", testDate, "test", "test", "test",
				5f, "test", client, activity, salesOrder, testDate, "RootRoot", project);
		List<ActivityRecord> activitiesRecords = new ArrayList<>();
		activitiesRecords.add(activityRecord);
		RecordsAccessList recordsAccess = new RecordsAccessList(activitiesRecords, 1l);
		when(activityRecordStorage.getActivityRecordsList(searchParam, activityParam, typeParam, subTypeParam,
				activityCodeParam, subActivityCodeParam, clientParam, projectParam, featureParam, fromDateParam,
				toDateParam, userNameParam, locationParam, officeParam, offsetParam, limitParam, officeParam,
				sortDescParam)).thenReturn(recordsAccess);
		// When
		RecordsAccessList newRecordsAccess = activityRecordService.getActivityRecordsList(searchParam, activityParam,
				typeParam, subTypeParam, activityCodeParam, subActivityCodeParam, clientParam, projectParam,
				featureParam, fromDateParam, toDateParam, userNameParam, locationParam, officeParam, offsetParam,
				limitParam, officeParam, sortDescParam);
		// Then
		assertNotNull(newRecordsAccess);
		assertEquals(recordsAccess.getSize(), newRecordsAccess.getSize());
		assertEquals(recordsAccess, newRecordsAccess);
	}

	@Test
	public void testGetLastActivityRecord() {
		Client client = new Client(1l, "test", "test");
		Project project = new Project(1l, "test", "test", client);
		SalesOrder salesOrder = new SalesOrder(1l, "test", "test", client);
		Type type = new Type(1l, "test", "test");
		SubType subType = new SubType(1l, "test", "test", type);
		ActivityCode activityCode = new ActivityCode(1l, "test", "test");
		SubActivityCode subActivityCode = new SubActivityCode(1l, "test", "test");
		Feature feature = new Feature(1l, "test", "test", "test", "test", project);
		Team team = new Team("1", "test", "test");
		List<Team> teams = new ArrayList<>();
		teams.add(team);
		Activity activity = new Activity(1l, type, subType, activityCode, subActivityCode, "test", project, feature,
				teams);
		Date testDate = new Date();
		ActivityRecord lastActivityRecord = new ActivityRecord(1l, "root", "12/12/2020", testDate, "test", "test",
				"test", 5f, "test", client, activity, salesOrder, testDate, "RootRoot", project);
		when(activityRecordStorage.getLastActivityRecord("root")).thenReturn(lastActivityRecord);
		// When
		ActivityRecord newLastActivityRecord = activityRecordService.getLastActivityRecord("root");
		// Then
		assertNotNull(newLastActivityRecord);
		assertEquals(newLastActivityRecord, lastActivityRecord);
		verify(activityRecordStorage, times(1)).getLastActivityRecord(any());
	}
	@Test
	public void testCountActivityRecords() {
		String searchParam = "test";
		String activityParam = "test";
		String typeParam = "test";
		String subTypeParam = "test";
		String activityCodeParam = "test";
		String subActivityCodeParam = "test";
		String clientParam = "test";
		String projectParam = "test";
		String featureParam = "test";
		String fromDateParam = "test";
		String toDateParam = "test";
		String userNameParam = "root";
		String locationParam = "test";
		String officeParam = "test";

		when(activityRecordStorage.countActivityRecords(searchParam, activityParam, typeParam, subTypeParam, activityCodeParam, subActivityCodeParam, clientParam, projectParam, featureParam, fromDateParam, toDateParam, userNameParam, locationParam, officeParam))
		.thenReturn(1l);
		//When
		long countActivityRecords = activityRecordService.countActivityRecords(searchParam, activityParam, typeParam, subTypeParam, activityCodeParam, subActivityCodeParam, clientParam, projectParam, featureParam, fromDateParam, toDateParam, userNameParam, locationParam, officeParam);
		//then
		assertNotNull(countActivityRecords);
		assertEquals(1l,countActivityRecords);
	}
	@Test
	public void testGetUserActivityRecordsList() {
		Client client = new Client(1l, "test", "test");
		Project project = new Project(1l, "test", "test", client);
		SalesOrder salesOrder = new SalesOrder(1l, "test", "test", client);
		Type type = new Type(1l, "test", "test");
		SubType subType = new SubType(1l, "test", "test", type);
		ActivityCode activityCode = new ActivityCode(1l, "test", "test");
		SubActivityCode subActivityCode = new SubActivityCode(1l, "test", "test");
		Feature feature = new Feature(1l, "test", "test", "test", "test", project);
		Team team = new Team("1", "test", "test");
		List<Team> teams = new ArrayList<>();
		teams.add(team);
		Activity activity = new Activity(1l, type, subType, activityCode, subActivityCode, "test", project, feature,
				teams);
		Date testDate = new Date();
		ActivityRecord activityRecord = new ActivityRecord(1l, "root", "12/12/2020", testDate, "test", "test", "test",
				5f, "test", client, activity, salesOrder, testDate, "RootRoot", project);
		List<ActivityRecord> activitiesRecords = new ArrayList<>();
		activitiesRecords.add(activityRecord);
		when(activityRecordService.getUserActivityRecordsList("22/2/2022","root")).thenReturn(activitiesRecords);
		//When
		List<ActivityRecord> newActivitiesRecords= activityRecordService.getUserActivityRecordsList("22/2/2022","root");
		//Then
		assertNotNull(newActivitiesRecords);
		Assert.assertEquals(activitiesRecords, newActivitiesRecords);
		verify(activityRecordStorage, times(1)).getUserActivityRecords(any(),any());
	}
	

}
