package org.exoplatform.timetracker.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.exoplatform.commons.utils.CommonsUtils;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.timetracker.dto.Activity;
import org.exoplatform.timetracker.dto.ActivityCode;
import org.exoplatform.timetracker.dto.ActivityRecord;
import org.exoplatform.timetracker.dto.Client;
import org.exoplatform.timetracker.dto.Feature;
import org.exoplatform.timetracker.dto.Project;
import org.exoplatform.timetracker.dto.RecordsAccessList;
import org.exoplatform.timetracker.dto.SalesOrder;
import org.exoplatform.timetracker.dto.SubActivityCode;
import org.exoplatform.timetracker.dto.SubType;
import org.exoplatform.timetracker.dto.Team;
import org.exoplatform.timetracker.dto.Type;
import org.exoplatform.timetracker.storage.ActivityRecordStorage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import junit.framework.TestCase;

public class ActivityRecordServiceTest extends TestCase {

	private ActivityRecordStorage activityRecordStorage;
	private TimeTrackerSettingsService timeTrackerSettingsService;
	private IdentityManager identityManager;
	private CommonsUtils commonsUtils;
	private TeamService teamService;
	private ActivityRecordService activityRecordService;

	@Before
	public void setUp() throws Exception {
		activityRecordStorage = mock(ActivityRecordStorage.class);
		commonsUtils = mock(CommonsUtils.class);
		identityManager = mock(IdentityManager.class);
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
		// Throw
		try {
			activityRecord = null;
			newActivityRecordService = activityRecordService.createActivityRecord(activityRecord);
			fail("ActivityRecord is mandatory");
		} catch (IllegalArgumentException e) {
			// Expected, ActivityRecord is mandatory
		}

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
		// Throw
		try {
			newActivityRecord = activityRecordService.updateActivityRecord(activityRecord, "");
			fail("username is mandatory");
		} catch (IllegalArgumentException e) {
			// Expected, username is mandatory
		}
		try {
			ActivityRecord activityRecordId = new ActivityRecord();
			activityRecordId.setId(1l);
			when(activityRecordStorage.getActivityRecordById(activityRecordId.getId())).thenReturn(null);
			newActivityRecord = activityRecordService.updateActivityRecord(activityRecord, "root");
			fail("bbbbb with id not found");
		} catch (Exception e) {
			// Expected, ActivityRecord with id not found
		}
		try {
			activityRecord.setId(null);
			newActivityRecord = activityRecordService.updateActivityRecord(activityRecord, "root");
			fail("ActivityRecord with null id wasn't found");
		} catch (EntityNotFoundException e) {
			// Expected, ActivityRecord with null id wasn't found
		}
		try {
			activityRecord = null;
			newActivityRecord = activityRecordService.updateActivityRecord(activityRecord, "root");
			fail("ActivityRecord with null id wasn't found");
		} catch (IllegalArgumentException e) {
			// Expected, ActivityRecord with null id wasn't found
		}
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

		// Throw
		try {
			activityRecordService.deleteActivityRecord(null, "root");
			fail("ActivityRecordId must be a positive integer");
		} catch (IllegalArgumentException e) {
			// Expected, ActivityRecordId must be a positive integer
		}
		try {
			activityRecordService.deleteActivityRecord(-1l, "root");
			fail("ActivityRecordId must be a positive integer");
		} catch (IllegalArgumentException e) {
			// Expected, ActivityRecordId must be a positive integer
		}
		try {
			activityRecordService.deleteActivityRecord(1l, "");
			fail("username is mandatory");
		} catch (IllegalArgumentException e) {
			// Expected, username is mandatory
		}
		try {
			when(activityRecordStorage.getActivityRecordById(activityRecord.getId())).thenReturn(null);
			activityRecordService.deleteActivityRecord(1l, "root");
			fail("ActivityRecord with id not found");
		} catch (EntityNotFoundException e) {
			// Expected, ActivityRecord with id not found
		}

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

		when(activityRecordStorage.countActivityRecords(searchParam, activityParam, typeParam, subTypeParam,
				activityCodeParam, subActivityCodeParam, clientParam, projectParam, featureParam, fromDateParam,
				toDateParam, userNameParam, locationParam, officeParam)).thenReturn(1l);
		// When
		long countActivityRecords = activityRecordService.countActivityRecords(searchParam, activityParam, typeParam,
				subTypeParam, activityCodeParam, subActivityCodeParam, clientParam, projectParam, featureParam,
				fromDateParam, toDateParam, userNameParam, locationParam, officeParam);
		// then
		assertNotNull(countActivityRecords);
		assertEquals(1l, countActivityRecords);
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
		when(activityRecordService.getUserActivityRecordsList("22/2/2022", "root")).thenReturn(activitiesRecords);
		// When
		List<ActivityRecord> newActivitiesRecords = activityRecordService.getUserActivityRecordsList("22/2/2022",
				"root");
		// Then
		assertNotNull(newActivitiesRecords);
		Assert.assertEquals(activitiesRecords, newActivitiesRecords);
		verify(activityRecordStorage, times(1)).getUserActivityRecords(any(), any());
	}

	@Test
	public void testGetActivity() {
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
		String activityReturn = "";
		// When
		activityReturn = activityRecordService.getActivity(activityRecord);
		// Then
		assertEquals("_test", activityReturn);

	}

	@Test
	public void testGetProject() {
		Client client = new Client(1l, "test", "test");
		Project project = new Project(1l, "test", "test", client);
		Project projectPRJ = new Project(1l, "<PRJ>", "test", client);
		SalesOrder salesOrder = new SalesOrder(1l, "test", "test", client);
		Type type = new Type(1l, "test", "test");
		SubType subType = new SubType(1l, "test", "test", type);
		ActivityCode activityCode = new ActivityCode(1l, "test", "test");
		SubActivityCode subActivityCode = new SubActivityCode(1l, "test", "test");
		Feature feature = new Feature(1l, "test", "test", "test", "test", project);
		Feature featurePRJ = new Feature(1l, "test", "test", "test", "test", projectPRJ);
		Team team = new Team("1", "test", "test");
		List<Team> teams = new ArrayList<>();
		teams.add(team);
		Activity activity = new Activity(1l, type, subType, activityCode, subActivityCode, "test", project, feature,
				teams);
		Activity activityPRJ = new Activity(1l, type, subType, activityCode, subActivityCode, "test", projectPRJ,
				featurePRJ, teams);
		Date testDate = new Date();
		ActivityRecord activityRecord = new ActivityRecord(1l, "root", "12/12/2020", testDate, "test", "test", "test",
				5f, "test", client, activity, salesOrder, testDate, "RootRoot", project);
		ActivityRecord activityRecordPRJ = new ActivityRecord(1l, "root", "12/12/2020", testDate, "test", "test",
				"test", 5f, "test", client, activityPRJ, salesOrder, testDate, "RootRoot", project);
		String projetReturn = "";
		String projetReturnPRJ = "";
		// When
		projetReturn = activityRecordService.getProject(activityRecord);
		projetReturnPRJ = activityRecordService.getProject(activityRecordPRJ);
		// Then
		assertEquals("_test", projetReturn);
		assertEquals("_test", projetReturnPRJ);

	}

	@Test
	public void testGetClient() {
		Client client = new Client(1l, "test", "test");
		Client clientCLNT = new Client(1l, "<CLNT>", "test");
		Project project = new Project(1l, "test", "test", client);
		Project projectCLNT = new Project(1l, "test", "test", clientCLNT);
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
		Activity activityCLNT = new Activity(1l, type, subType, activityCode, subActivityCode, "test", projectCLNT,
				feature, teams);
		Date testDate = new Date();
		ActivityRecord activityRecord = new ActivityRecord(1l, "root", "12/12/2020", testDate, "test", "test", "test",
				5f, "test", client, activity, salesOrder, testDate, "RootRoot", project);
		ActivityRecord activityRecordPRJ = new ActivityRecord(1l, "root", "12/12/2020", testDate, "test", "test",
				"test", 5f, "test", client, activityCLNT, salesOrder, testDate, "RootRoot", project);
		String clientReturn = "";
		String clientReturnPRJ = "";
		// When
		clientReturn = activityRecordService.getClient(activityRecord);
		clientReturnPRJ = activityRecordService.getClient(activityRecordPRJ);
		// Then
		assertEquals("_test", clientReturn);
		assertEquals("_test", clientReturnPRJ);

	}

	@Test
	public void testGeSubActivity() {
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
		Activity nullActivity = null;
		ActivityRecord activityRecordNullActivity = new ActivityRecord(1l, "root", "12/12/2020", testDate, "test",
				"test", "test", 5f, "test", client, nullActivity, salesOrder, testDate, "RootRoot", project);
		ActivityRecord activityRecord = new ActivityRecord(1l, "root", "12/12/2020", testDate, "test", "test", "test",
				5f, "test", client, activity, salesOrder, testDate, "RootRoot", project);
		String subActivityReturn = "";
		String subActivityReturnnullActivity = "";
		// When
		subActivityReturn = activityRecordService.geSubActivity(activityRecord);
		subActivityReturnnullActivity = activityRecordService.geSubActivity(activityRecordNullActivity);
		// Then
		assertEquals("_test", subActivityReturn);
		assertEquals("", subActivityReturnnullActivity);
	}

	@Test
	public void testGenerateTSCode() {
		Client client = new Client(1l, "test", "test");
		Project project = new Project(1l, "test", "test", client);
		SalesOrder salesOrder = new SalesOrder(1l, "test", "test", client);
		Type type = new Type(1l, "test", "test");
		SubType subType = new SubType(1l, "test", "test", type);
		ActivityCode activityCode = new ActivityCode(1l, "test", "test");
		SubActivityCode subActivityCode = new SubActivityCode(1l, "test", "test");
		Feature feature = new Feature(1l, "test", "test", "test", "test", project);
		Team team = new Team("1", "Designers", "Designers");
		List<Team> teams = new ArrayList<>();
		teams.add(team);
		Activity activity = new Activity(1l, type, subType, activityCode, subActivityCode, "test", project, feature,
				teams);
		Date testDate = new Date();
		ActivityRecord activityRecord = new ActivityRecord(1l, "root", "12/12/2020", testDate, "test", "test", "test",
				5f, "test", client, activity, salesOrder, testDate, "RootRoot", project);
		Client client1 = new Client(1l, "test", "test");
		Project project1 = new Project(1l, "test", "test", client1);
		SalesOrder salesOrder1 = new SalesOrder(1l, "test", "test", client1);
		Type type1 = new Type(1l, "test", "test");
		SubType subType1 = new SubType(1l, "test", "test", type1);
		ActivityCode activityCode1 = new ActivityCode(1l, "test", "test");
		SubActivityCode subActivityCode1 = new SubActivityCode(1l, "test", "test");
		Feature feature1 = new Feature(1l, "test", "test", "test", "test", project1);
		Team team1 = new Team("1", "Architects", "Architects");
		List<Team> teams1 = new ArrayList<>();
		teams1.add(team1);
		Activity activity1 = new Activity(1l, type1, subType1, activityCode1, subActivityCode1, "test", project1,
				feature1, teams1);
		Date testDate1 = new Date();
		ActivityRecord record = new ActivityRecord(1l, "root", "12/12/2020", testDate, "test", "test", "test", 5f,
				"test", client1, activity1, salesOrder1, testDate1, "RootRoot", project1);
		Team teamAnalyst = new Team("1", "Analysts", "Analysts");
		Team teamSupport = new Team("1", "Support", "Support");
		Team teamQA = new Team("1", "QA", "QA");
		List<Team> teamsQA = new ArrayList<>();
		List<Team> teamsNew = new ArrayList<>();
		List<Team> teamsSupport = new ArrayList<>();
		List<Team> teamsManagement = new ArrayList<>();
		List<Team> teamsAnalysts = new ArrayList<>();
		List<Team> teamsITOP = new ArrayList<>();
		teamsSupport.add(teamSupport);
		teamsQA.add(teamQA);
		teamsAnalysts.add(teamAnalyst);
		record.getActivity().getSubType().setCode("FIXP");
		record.getActivity().getType().setCode("SERV");
		String exportType = "";
		String tsCode = "";
		String tsCodeDesigners = "";
		String tsCodeManagement = "";
		String tsCodeAnalyst = "";
		String tsCodeITOP = "";
		String tsCodeExportType = "";
		String tsCodeNew = "";
		String tsCodeExportTypeFIXP = "";
		String exportTypeFR = "fr";
		String tsCodeSupport = "";

		// When
		tsCode = activityRecordService.generateTSCode(teams, activityRecord, exportType);
		tsCodeNew = activityRecordService.generateTSCode(teamsNew, activityRecord, exportType);
		tsCodeExportType = activityRecordService.generateTSCode(teams, activityRecord, exportTypeFR);
		tsCodeExportTypeFIXP = activityRecordService.generateTSCode(teams1, record, exportTypeFR);
		tsCodeDesigners = activityRecordService.generateTSCode(teams1, activityRecord, exportTypeFR);
		tsCodeManagement = activityRecordService.generateTSCode(teamsManagement, activityRecord, exportTypeFR);
		activityRecord.getActivity().getType().setCode("SERV");
		tsCodeITOP = activityRecordService.generateTSCode(teamsITOP, activityRecord, exportTypeFR);
		activityRecord.getActivity().getType().setCode("Support");
		activityRecord.getActivity().setTeams(teamsAnalysts);
		tsCodeSupport = activityRecordService.generateTSCode(teamsSupport, activityRecord, exportType);
		activityRecord.getActivity().getType().setCode("QA");
		tsCodeAnalyst = activityRecordService.generateTSCode(teamsQA, activityRecord, exportType);

		// Then
		assertEquals("2022_test_test_test_test", tsCode);
		assertEquals("2022_test_test_test_test", tsCodeNew);
		assertEquals("2022_test_SERV_CONS_test", tsCodeExportTypeFIXP);
		assertEquals("2022_test_test_test", tsCodeExportType);
		assertEquals("2022_test_test_test", tsCodeDesigners);
		assertEquals("2022_test_test_test", tsCodeManagement);
		assertEquals("2022_test_SERV_test_test", tsCodeITOP);
		assertEquals("2022_test_Support_test_test_test", tsCodeSupport);
		assertEquals("2022_test_QA_test_test_test", tsCodeAnalyst);
	}

	@Test
	public void testGetDatesBetween() {
		LocalDate startDate = LocalDate.of(2022, 04, 06);
		LocalDate endDate = LocalDate.of(2022, 04, 07);
		List<LocalDate> datesBetween = new ArrayList<>();
		List<LocalDate> datesBetweenNew = new ArrayList<>();

		datesBetween.add(startDate);
		datesBetween.add(endDate);
		long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate);

		// When
		datesBetweenNew = ActivityRecordService.getDatesBetween(startDate, endDate);

		// Then
		assertEquals(datesBetweenNew, datesBetween);

	}
	

	/*@Test
	public void testGetUserActivityRecords() {
		Client client0 = new Client(1l, "test", "test");
		Project project0 = new Project(1l, "test", "test", client0);
		SalesOrder salesOrder0 = new SalesOrder(1l, "test", "test", client0);
		Type type0 = new Type(1l, "test", "test");
		SubType subType0 = new SubType(1l, "test", "test", type0);
		ActivityCode activityCode0 = new ActivityCode(1l, "test", "test");
		SubActivityCode subActivityCode0 = new SubActivityCode(1l, "test", "test");
		Feature feature0 = new Feature(1l, "test", "test", "test", "test", project0);
		Team team = new Team("1", "Designers", "Designers");
		List<Team> teams = new ArrayList<>();
		teams.add(team);
		Activity activity0 = new Activity(1l, type0, subType0, activityCode0, subActivityCode0, "test", project0,
				feature0, teams);
		Date testDate = new Date();
		ActivityRecord activityRecord = new ActivityRecord(1l, "root", "12/12/2020", testDate, "test", "test", "test",
				5f, "test", client0, activity0, salesOrder0, testDate, "RootRoot", project0);
		Client client1 = new Client(1l, "test", "test");
		Project project1 = new Project(1l, "test", "test", client1);
		SalesOrder salesOrder1 = new SalesOrder(1l, "test", "test", client1);
		Type type1 = new Type(1l, "test", "test");
		SubType subType1 = new SubType(1l, "test", "test", type1);
		ActivityCode activityCode1 = new ActivityCode(1l, "test", "test");
		SubActivityCode subActivityCode1 = new SubActivityCode(1l, "test", "test");
		Feature feature1 = new Feature(1l, "test", "test", "test", "test", project1);
		Team team1 = new Team("1", "Architects", "Architects");
		List<Team> teams1 = new ArrayList<>();
		teams1.add(team1);
		Activity activity1 = new Activity(1l, type1, subType1, activityCode1, subActivityCode1, "test", project1,
				feature1, teams1);
		Date testDate1 = new Date();
		ActivityRecord record = new ActivityRecord(1l, "root", "12/12/2020", testDate, "test", "test", "test", 5f,
				"test", client1, activity1, salesOrder1, testDate1, "RootRoot", project1);
		List<ActivityRecord> activityRecords = new ArrayList<>();
		String search = "test";
		String activity = "test";
		String type = "test";
		String subType = "test";
		String activityCode = "test";
		String subActivityCode = "test";
		String client = "test";
		String project = "test";
		String feature = "test";
		String fromDate = "test";
		String toDate = "test";
		String userName = "test";
		String location = "test";
		String office = "test";
		String sortBy = "test";
		Boolean sortDesc = true;
		Boolean export = false;
		String exportType = "fr";
		RecordsAccessList recordsAccessList = activityRecordService.getActivityRecordsList(search, activity, type,
				subType, activityCode, subActivityCode, client, project, feature, fromDate, toDate, userName, location,
				office, 0, 0, sortBy, sortDesc);
		activityRecords.add(activityRecord);
		activityRecords.add(record);
		when(CommonsUtils.getService(IdentityManager.class).getOrCreateUserIdentity(userName).getProfile().getFullName()).thenReturn("root");
		when(activityRecordStorage.getActivityRecordsList(search, activity, type, subType, activityCode,
				subActivityCode, client, project, feature, fromDate, toDate, userName, location, office, 0, 10, sortBy,
				sortDesc)).thenReturn(recordsAccessList);
		when(activityRecordService.getUserActivityRecords(search, activity, type, subType, activityCode,
				subActivityCode, client, project, feature, fromDate, toDate, userName, location, office, sortBy,
				sortDesc, export, exportType)).thenReturn(activityRecords);
		// When
		List<ActivityRecord> userActivityRecords = activityRecordService.getUserActivityRecords(search, activity, type,
				subType, activityCode, subActivityCode, client, project, feature, fromDate, toDate, userName, location,
				office, sortBy, sortDesc, export, exportType);

		// Then
		assertEquals(activityRecords.size(), userActivityRecords.size());
	}*/
}
