package org.exoplatform.timetracker.storage;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.exoplatform.timetracker.dto.Activity;
import org.exoplatform.timetracker.dto.ActivityCode;
import org.exoplatform.timetracker.dto.Client;
import org.exoplatform.timetracker.dto.Feature;
import org.exoplatform.timetracker.dto.Project;
import org.exoplatform.timetracker.dto.SubActivityCode;
import org.exoplatform.timetracker.dto.SubType;
import org.exoplatform.timetracker.dto.Team;
import org.exoplatform.timetracker.dto.Type;
import org.exoplatform.timetracker.entity.ActivityEntity;
import org.exoplatform.timetracker.service.BaseTimeTrackerTest;
import org.gatein.api.EntityNotFoundException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ActivityStorageTest extends BaseTimeTrackerTest {
  
  @Test
  public void testCreateActivity() throws Exception {
    Activity activity = createActivityLocal();
    Activity newActivity = createActivity(activity);
    assertNotNull(newActivity);
    assertThrows(IllegalArgumentException.class, () -> activityStorage.createActivity(null));
  }


  @Test
  public void testUpdateActivity() throws Exception {
    Activity activity = createActivityLocal();
    Activity newActivity = createActivity(activity);
    newActivity.setLabel("testUpdate5");
    Activity activityNotFound = activityStorage.getActivities().get(0);
    activityNotFound.setId(5000l);

    Activity newActivityUpdated = activityStorage.updateActivity(newActivity);

    // Throw
    assertNotNull(newActivityUpdated);
    assertThrows(IllegalArgumentException.class, () -> activityStorage.updateActivity(null));
    assertThrows(EntityNotFoundException.class, () -> activityStorage.updateActivity(activityNotFound));
  }

  @Test
  public void testDeleteActivity() throws Exception {
    Activity activity = createActivityLocal();
    Activity newActivity = createActivity(activity);
    Long activityId = newActivity.getId();
    Long activityIdNull = 0l;
    Long activityIdNotFound = 5l;
    cleanupActivities.remove(newActivity);
    activityStorage.deleteActivity(activityId);

    // Throw
    assertThrows(IllegalArgumentException.class, () -> activityStorage.deleteActivity(activityIdNull));
    assertThrows(EntityNotFoundException.class, () -> activityStorage.deleteActivity(activityIdNotFound));

  }

  @Test
  public void testGetActivityById() throws Exception {
    Activity activity = createActivityLocal();
    Activity ActivityNew = createActivity(activity);
    Long activityId = ActivityNew.getId();
    Long activityIdNull = 0l;

    Activity newActivity = activityStorage.getActivityById(activityId);

    // Throw
    assertNotNull(newActivity);
    assertThrows(IllegalArgumentException.class, () -> activityStorage.getActivityById(activityIdNull));

  }

  @Test
  public void testGetActivitiesByTeams() throws Exception {
    Activity activity = createActivityLocal();
    Activity ActivityNew = createActivity(activity);
    List<String> activityId = ActivityNew.getTeams().stream().map(Team::getId).collect(Collectors.toList());
    List<String> activityIdNull = new ArrayList<String>();

    List<Activity> newActivity = activityStorage.getActivitiesByTeams(activityId);
    activityStorage.getActivitiesByTeams(activityIdNull);
    // Throw
    assertNotNull(newActivity);

  }

  @Test
  public void testGetActivities() throws Exception {
    Activity activity = createActivityLocal();
    createActivity(activity);
    List<Activity> activitys = activityStorage.getActivities();
    assertNotNull(activitys);
    assertTrue(!activitys.isEmpty());

  }

  @Test
  public void testCountActivities() throws Exception {
    Activity activity = createActivityLocal();
    createActivity(activity);
    Long countActivity = activityStorage.countActivities();
    assertNotNull(countActivity);
  }

  @Test
  public void toDTO() {
    ActivityEntity activityEntityNull = null;
    Activity activityDTO = activityStorage.toDTO(activityEntityNull);
    assertNull(activityDTO);
  }

  @Test
  public void toEntity() {
    Activity activityNull = null;
    ActivityEntity activityEntity = activityStorage.toEntity(activityNull);
    assertNull(activityEntity);
  }

  private Activity createActivity(Activity activity) throws Exception {
    List<Activity> activitys = activityStorage.getActivities();
    assertNotNull(activitys);
    Activity storedActivity = activityStorage.createActivity(activity);
    assertNotNull(storedActivity);
    cleanupActivities.add(storedActivity);
    return storedActivity;
  }

  private Project createProject(Project project) throws Exception {
    List<Project> projects = projectStorage.getProjects();
    assertNotNull(projects);
    Project storedProject = projectStorage.createProject(project);
    assertNotNull(storedProject);
    cleanupProjects.add(storedProject);
    return storedProject;
  }

  private Client createClient(Client client) throws Exception {
    List<Client> clients = clientStorage.getClients();
    assertNotNull(clients);
    Client storedClient = clientStorage.createClient(client);
    assertNotNull(storedClient);
    cleanupClients.add(storedClient);
    return storedClient;
  }

  private Feature createFeature(Feature feature) throws Exception {
    List<Feature> features = featureStorage.getFeatures();
    assertNotNull(features);
    Feature storedFeature = featureStorage.createFeature(feature);
    assertNotNull(storedFeature);
    cleanupFeatures.add(storedFeature);
    return storedFeature;
  }

  private ActivityCode createActivityCode(ActivityCode activityCode) throws Exception {
    List<ActivityCode> activityCodes = codesStorage.getActivityCodes();
    assertNotNull(activityCodes);
    assertTrue(activityCodes.isEmpty());
    ActivityCode storedActivityCode = codesStorage.createActivityCode(activityCode);
    assertNotNull(storedActivityCode);
    cleanupActivityCodes.add(storedActivityCode);
    return storedActivityCode;
  }

  private SubActivityCode createSubActivityCode(SubActivityCode subActivityCode) throws Exception {
    List<SubActivityCode> subActivityCodes = codesStorage.getSubActivityCodes();
    assertNotNull(subActivityCodes);
    assertTrue(subActivityCodes.isEmpty());
    SubActivityCode storedSubActivityCode = codesStorage.createSubActivityCode(subActivityCode);
    assertNotNull(storedSubActivityCode);
    cleanupSubActivityCodes.add(storedSubActivityCode);
    return storedSubActivityCode;
  }

  private Type createType(Type type) throws Exception {
    List<Type> types = codesStorage.getTypes();
    assertNotNull(types);
    Type storedType = codesStorage.createType(type);
    assertNotNull(storedType);
    cleanupTypes.add(storedType);
    return storedType;
  }

  private SubType createSubType(SubType subType) throws Exception {
    List<SubType> subTypes = codesStorage.getSubTypes();
    assertNotNull(subTypes);
    SubType storedSubType = codesStorage.createSubType(subType);
    assertNotNull(storedSubType);
    cleanupSubTypes.add(storedSubType);
    return storedSubType;
  }

  private Team createTeam(Team team) throws Exception {
    List<Team> teams = teamStorage.getTeams();
    assertNotNull(teams);
    Team storedTeam = teamStorage.createTeam(team);
    assertNotNull(storedTeam);
    cleanupTeams.add(storedTeam);
    return storedTeam;
  }
  
  private Activity createActivityLocal() throws Exception {
    Client client = new Client(null, "test", "test");
    Client newClient = createClient(client);
    Project projectCreated = new Project(null, "test", "test", newClient);
    Project project = createProject(projectCreated);
    Type typeCreated = new Type(null, "test", "test");
    Type type = createType(typeCreated);
    SubType subTypeCreated = new SubType(null, "test", "test", type);
    SubType subType = createSubType(subTypeCreated);
    ActivityCode activityCodeCreated = new ActivityCode(null, "test", "test");
    ActivityCode activityCode = createActivityCode(activityCodeCreated);
    SubActivityCode subActivityCodeCreated = new SubActivityCode(null, "test", "test");
    SubActivityCode subActivityCode = createSubActivityCode(subActivityCodeCreated);
    Feature featureCreated = new Feature(null, "test", "test", "test", "test", project);
    Feature feature = createFeature(featureCreated);
    Team teamCreated = new Team(null, "test", "test");
    Team team = createTeam(teamCreated);
    List<Team> teams = new ArrayList<>();
    teams.add(team);
    return new Activity(null, type, subType, activityCode, subActivityCode, "test", project, feature, teams);
  }

}
