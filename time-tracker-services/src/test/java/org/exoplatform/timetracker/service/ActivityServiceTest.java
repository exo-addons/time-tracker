package org.exoplatform.timetracker.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.exoplatform.timetracker.dto.*;
import org.exoplatform.timetracker.storage.ActivityStorage;
import org.junit.Before;
import org.junit.Test;
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
		Client client = new Client(1l,"test","test");
		Project project= new Project(1l,"test","test",client);
		Type type = new Type(1l,"test","test");
		SubType subType = new SubType(1l, "test","test",type);
		ActivityCode activityCode = new ActivityCode(1l,"test","test");
		SubActivityCode subActivityCode = new SubActivityCode(1l,"test","test");
		Feature feature = new Feature(1l,"test","test","test","test",project);
		Team team = new Team("1","test","test");
		List<Team> teams = new ArrayList<>();
		teams.add(team);
		Activity activity = new Activity(1l,type,subType,activityCode,subActivityCode,"test",project,feature,teams);
		when(activityStorage.createActivity(activity)).thenReturn(activity);
		// When
		Activity newActivity = null;
		newActivity = activityService.createActivity(activity);
		// Then
	    assertNotNull(newActivity);
	    verify(activityStorage, times(1)).createActivity(any());
	}
	
	@Test
	public void testUpdateActivity() throws Exception {
	    // Given
		Client client = new Client(1l,"test","test");
		Project project= new Project(1l,"test","test",client);
		Type type = new Type(1l,"test","test");
		SubType subType = new SubType(1l, "test","test",type);
		ActivityCode activityCode = new ActivityCode(1l,"test","test");
		SubActivityCode subActivityCode = new SubActivityCode(1l,"test","test");
		Feature feature = new Feature(1l,"test","test","test","test",project);
		Team team = new Team("1","test","test");
		List<Team> teams = new ArrayList<>();
		teams.add(team);
		Client clientUpdated = new Client(1l,"testUpdated","testUpdated");
		Project projectUpdated= new Project(1l,"testUpdated","testUpdated",clientUpdated);
		Type typeUpdated = new Type(1l,"testUpdated","testUpdated");
		SubType subTypeUpdated = new SubType(1l, "testUpdated","testUpdated",typeUpdated);
		ActivityCode activityCodeUpdated = new ActivityCode(1l,"testUpdated","testUpdated");
		SubActivityCode subActivityCodeUpdated = new SubActivityCode(1l,"testUpdated","testUpdated");
		Feature featureUpdated = new Feature(1l,"testUpdated","testUpdated","testUpdated","testUpdated",projectUpdated);
		Team teamUpdated = new Team("1","testUpdated","testUpdated");
		List<Team> teamsUpdated = new ArrayList<>();
		teamsUpdated.add(teamUpdated);
		Activity newActivity = null;
		Activity activity = new Activity(1l,type,subType,activityCode,subActivityCode,"test",project,feature,teams);
		Activity activityUpdated = new Activity(activity.getId(),typeUpdated,subTypeUpdated,activityCodeUpdated,subActivityCodeUpdated,"testUpdated",projectUpdated,featureUpdated,teamsUpdated);
	    when(activityStorage.getActivityById(anyLong())).thenReturn(activity);
	    when(activityStorage.updateActivity(activity)).thenReturn(activityUpdated);

	    // When
	    newActivity = activityService.updateActivity(activity, "root");

	    // Then
	    assertNotNull(newActivity);
	    assertEquals((long) newActivity.getId(), 1l);
	    assertEquals(newActivity, activityUpdated);
	    verify(activityStorage, times(1)).updateActivity(any());
	  }
	
	@Test
	public void testDeleteActivity() throws Exception {
	    // Given
		Client client = new Client(1l,"test","test");
		Project project= new Project(1l,"test","test",client);
		Type type = new Type(1l,"test","test");
		SubType subType = new SubType(1l, "test","test",type);
		ActivityCode activityCode = new ActivityCode(1l,"test","test");
		SubActivityCode subActivityCode = new SubActivityCode(1l,"test","test");
		Feature feature = new Feature(1l,"test","test","test","test",project);
		Team team = new Team("1","test","test");
		List<Team> teams = new ArrayList<>();
		teams.add(team);
		Activity activity = new Activity(1l,type,subType,activityCode,subActivityCode,"test",project,feature,teams);
	    doNothing().when(activityStorage).deleteActivity(activity.getId());
	    when(activityStorage.getActivityById(activity.getId())).thenReturn(activity);

	    // When
	    activityService.deleteActivity(activity.getId(), "root");

	    // Then
	    verify(activityStorage, times(1)).deleteActivity(anyLong());
	  }
	
	@Test
	public void testGetActivityList() {
	    // Given
		Client client = new Client(1l,"test","test");
		Project project= new Project(1l,"test","test",client);
		Type type = new Type(1l,"test","test");
		SubType subType = new SubType(1l, "test","test",type);
		ActivityCode activityCode = new ActivityCode(1l,"test","test");
		SubActivityCode subActivityCode = new SubActivityCode(1l,"test","test");
		Feature feature = new Feature(1l,"test","test","test","test",project);
		Team team = new Team("1","test","test");
		List<Team> teams = new ArrayList<>();
		teams.add(team);
		Activity activity = new Activity(1l,type,subType,activityCode,subActivityCode,"test",project,feature,teams);
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
		Client client = new Client(1l,"test","test");
		Project project= new Project(1l,"test","test",client);
		Type type = new Type(1l,"test","test");
		SubType subType = new SubType(1l, "test","test",type);
		ActivityCode activityCode = new ActivityCode(1l,"test","test");
		SubActivityCode subActivityCode = new SubActivityCode(1l,"test","test");
		Feature feature = new Feature(1l,"test","test","test","test",project);
		Team team = new Team("1","test","test");
		List<Team> teams = new ArrayList<>();
		teams.add(team);
		
		Activity activity = new Activity(1l,type,subType,activityCode,subActivityCode,"test",project,feature,teams);
		
		List<Activity> activities = new ArrayList<>();
		List<String> teamsList = new ArrayList<>();
		
		teamsList.add(team.getId());
		activities.add(activity);
		when(activityStorage.getActivitiesByTeams(teamsList)).thenReturn(activities);
		
		// When
		List<Activity> activitiesList = activityService.getActivitiesforUser(teamsList);
		
		//Then
		assertEquals(1, activitiesList.size());
		assertEquals("1", teamsList.get(0));
	    verify(activityStorage, times(1)).getActivitiesByTeams(teamsList);
	  }

}
