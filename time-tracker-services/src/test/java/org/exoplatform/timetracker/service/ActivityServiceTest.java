package org.exoplatform.timetracker.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import org.exoplatform.timetracker.dto.*;
import org.exoplatform.timetracker.storage.ActivityStorage;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import junit.framework.TestCase;

public class ActivityServiceTest extends TestCase {

	private ActivityStorage activityStorage;
	private ActivityService activityService;

	@Before
	public void setUp() throws Exception {
		activityStorage = mock(ActivityStorage.class);
		activityService = new ActivityService(activityStorage);
	}

	@Test
	public void testCreateActivity() throws Exception {
		Client client = new Client(1l, "test", "test");
		Project project = new Project(1l, "test", "test", client);
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
		when(activityStorage.createActivity(activity)).thenReturn(activity);
		// When
		Activity newActivity = null;
		Activity newActivityThrow = null;
		try {
			newActivity = activityService.createActivity(newActivityThrow);
			fail("Activity is mandatory");
		} catch (IllegalArgumentException e) {
			// Expected, activity remoteId is mandatory
		}
		newActivity = activityService.createActivity(activity);

		// Then
		verify(activityStorage, times(1)).createActivity(any());
		assertNotNull(newActivity);

	}

	@SuppressWarnings("null")
	@Test
	public void testUpdateActivity() throws Exception {
		// Given
		Client client = new Client(1l, "test", "test");
		Project project = new Project(1l, "test", "test", client);
		Type type = new Type(1l, "test", "test");
		SubType subType = new SubType(1l, "test", "test", type);
		ActivityCode activityCode = new ActivityCode(1l, "test", "test");
		SubActivityCode subActivityCode = new SubActivityCode(1l, "test", "test");
		Feature feature = new Feature(1l, "test", "test", "test", "test", project);
		Team team = new Team("1", "test", "test");
		List<Team> teams = new ArrayList<>();
		teams.add(team);
		Client clientUpdated = new Client(1l, "testUpdated", "testUpdated");
		Project projectUpdated = new Project(1l, "testUpdated", "testUpdated", clientUpdated);
		Type typeUpdated = new Type(1l, "testUpdated", "testUpdated");
		SubType subTypeUpdated = new SubType(1l, "testUpdated", "testUpdated", typeUpdated);
		ActivityCode activityCodeUpdated = new ActivityCode(1l, "testUpdated", "testUpdated");
		SubActivityCode subActivityCodeUpdated = new SubActivityCode(1l, "testUpdated", "testUpdated");
		Feature featureUpdated = new Feature(1l, "testUpdated", "testUpdated", "testUpdated", "testUpdated",
				projectUpdated);
		Team teamUpdated = new Team("1", "testUpdated", "testUpdated");
		List<Team> teamsUpdated = new ArrayList<>();
		teamsUpdated.add(teamUpdated);
		Activity newActivity = null;
		Activity activity = new Activity(1l, type, subType, activityCode, subActivityCode, "test", project, feature,
				teams);
		Activity activityUpdated = new Activity(activity.getId(), typeUpdated, subTypeUpdated, activityCodeUpdated,
				subActivityCodeUpdated, "testUpdated", projectUpdated, featureUpdated, teamsUpdated);
		Activity newActivityThrow = null;
		String userName = null;
		when(activityStorage.updateActivity(activity)).thenReturn(activityUpdated);
		when(activityStorage.getActivityById(anyLong())).thenReturn(activity);
		
		// When
		newActivity = activityService.updateActivity(activity, "root");
		
		// Then
		assertEquals((long) newActivity.getId(), 1l);
		assertEquals(newActivity, activityUpdated);
		verify(activityStorage, times(1)).updateActivity(any());
		
		//Throw
		try {
			newActivity = activityService.updateActivity(newActivityThrow, "root");
			fail("Activity is mandatory");
		} catch (IllegalArgumentException e) {
			// Expected, activity remoteId is mandatory
		}
		try {
			newActivity = activityService.updateActivity(activity, userName);
			fail("Username is mandatory");
		} catch (IllegalArgumentException e) {
			// Expected, activity userName is mandatory
		}
		try {
			Long ActivityId=1l;
			when(activityStorage.getActivityById(ActivityId)).thenReturn(null);
			activity = activityService.updateActivity(activity, "root");
			fail("Activity with id '1l' wasn't found");
		} catch (EntityNotFoundException e) {
			// Expected, activity remoteId is mandatory
		}
		try {
			newActivityThrow = activity;
			newActivityThrow.setId(null);
			newActivity = activityService.updateActivity(newActivityThrow, "root");
			fail("Activity with null id wasn't found");
		} catch (EntityNotFoundException e) {
			// Expected, activity remoteId is mandatory
		}
		
	}

	@Test
	public void testDeleteActivity() throws Exception {
		// Given
		Client client = new Client(1l, "test", "test");
		Project project = new Project(1l, "test", "test", client);
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
		String userName= null;
		doNothing().when(activityStorage).deleteActivity(activity.getId());
		when(activityStorage.getActivityById(activity.getId())).thenReturn(activity);

		// When
		activityService.deleteActivity(activity.getId(), "root");

		// Then
		verify(activityStorage, times(1)).deleteActivity(anyLong());
		
		//Throw
		try {
			activityService.deleteActivity(activity.getId(), userName);
			fail("Username is mandatory");
		} catch (IllegalArgumentException e) {
			// Expected, activity remoteId is mandatory
		}
		try {
			Long activityId=activity.getId();
			when(activityStorage.getActivityById(activityId)).thenReturn(null);
			activityService.deleteActivity(activityId, "root");
			fail("Activity with id wasn't found");
		} catch (EntityNotFoundException e) {
			// Expected, activity remoteId is mandatory
		}
		try {
			Long activityId=null;
			activityService.deleteActivity(activityId,"root");
			fail("ActivityId must be a positive integer");
		} catch (IllegalArgumentException e) {
			// Expected, activity remoteId is mandatory
		}

	}

	@Test
	public void testGetActivityList() {
		// Given
		Client client = new Client(1l, "test", "test");
		Project project = new Project(1l, "test", "test", client);
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
		List<Activity> activities = new ArrayList<>();
		activities.add(activity);
		when(activityStorage.getActivities()).thenReturn(activities);

		// When
		List<Activity> activitiesList = activityService.getActivitiesList();

		// Then
		assertEquals(1, activitiesList.size());
		verify(activityStorage, times(1)).getActivities();
	}

	@Test
	public void testGetActivitiesforUser() {
		// Given
		Client client = new Client(1l, "test", "test");
		Project project = new Project(1l, "test", "test", client);
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

		List<Activity> activities = new ArrayList<>();
		List<String> teamsList = new ArrayList<>();

		teamsList.add(team.getId());
		activities.add(activity);
		when(activityStorage.getActivitiesByTeams(teamsList)).thenReturn(activities);

		// When
		List<Activity> activitiesList = activityService.getActivitiesforUser(teamsList);

		// Then
		assertEquals(1, activitiesList.size());
		assertEquals("1", teamsList.get(0));
		verify(activityStorage, times(1)).getActivitiesByTeams(teamsList);
	}

}
