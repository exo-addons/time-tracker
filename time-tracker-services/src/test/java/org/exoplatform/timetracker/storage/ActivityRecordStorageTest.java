package org.exoplatform.timetracker.storage;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

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
import org.exoplatform.timetracker.service.BaseTimeTrackerTest;
import org.gatein.api.EntityNotFoundException;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityRecordStorageTest extends BaseTimeTrackerTest {

  @Test
  public void testCreateActivityRecord() throws Exception {
    ActivityRecord activityRecord = createActivityRecordLocal();
    ActivityRecord newActivityRecord = createActivityRecord(activityRecord);
    assertNotNull(newActivityRecord);
    assertThrows(IllegalArgumentException.class, () -> activityStorage.createActivity(null));
  }
  
  @Test
  public void testUpdateActivityRecord() throws Exception {
    ActivityRecord activityRecord = createActivityRecordLocal();
    ActivityRecord newActivityRecord = createActivityRecord(activityRecord);
    newActivityRecord.setDescription("testUpdate5");
    ActivityRecord activityRecordNotFound = activityRecordStorage.getActivityRecords().get(0);
    activityRecordNotFound.setId(5000l);

    ActivityRecord newActivityRecordUpdated = activityRecordStorage.updateActivityRecord(newActivityRecord);

    // Throw
    assertNotNull(newActivityRecordUpdated);
    assertThrows(IllegalArgumentException.class, () -> activityRecordStorage.updateActivityRecord(null));
    assertThrows(EntityNotFoundException.class, () -> activityRecordStorage.updateActivityRecord(activityRecordNotFound));
  }
  
  @Test
  public void testDeleteActivityRecord() throws Exception {
    ActivityRecord activityRecord = createActivityRecordLocal();
    ActivityRecord newActivityRecord = createActivityRecord(activityRecord);
    Long activityRecordId = newActivityRecord.getId();
    Long activityRecordIdNull = 0l;
    Long activityRecordIdNotFound = 5l;
    cleanupActivityRecord.remove(newActivityRecord);
    activityRecordStorage.deleteActivityRecord(activityRecordId);

    // Throw
    assertThrows(IllegalArgumentException.class, () -> activityRecordStorage.deleteActivityRecord(activityRecordIdNull));
    assertThrows(EntityNotFoundException.class, () -> activityRecordStorage.deleteActivityRecord(activityRecordIdNotFound));
  }
  
  @Test
  public void testGetActivityRecordById() throws Exception {
    ActivityRecord activityRecord = createActivityRecordLocal();
    ActivityRecord ActivityRecordNew = createActivityRecord(activityRecord);
    Long activityRecordId = ActivityRecordNew.getId();
    Long activityRecordIdNull = 0l;

    ActivityRecord newActivityRecord = activityRecordStorage.getActivityRecordById(activityRecordId);

    // Throw
    assertNotNull(newActivityRecord);
    assertThrows(IllegalArgumentException.class, () -> activityRecordStorage.getActivityRecordById(activityRecordIdNull));
  }
  
  @Test
  public void testGetUserActivityRecords() throws Exception {
    ActivityRecord activityRecord = createActivityRecordLocal();
    ActivityRecord ActivityRecordNew = createActivityRecord(activityRecord);
    String day = ActivityRecordNew.getActivityDate();
    String userName = ActivityRecordNew.getUserName();

    List<ActivityRecord> newActivityRecord = activityRecordStorage.getUserActivityRecords(day,userName);

    // Throw
    assertNotNull(newActivityRecord);
  }
  
  @Test
  public void testGetLastActivityRecord() throws Exception {
    ActivityRecord activityRecord = createActivityRecordLocal();
    ActivityRecord ActivityRecordNew = createActivityRecord(activityRecord);
    String userName = ActivityRecordNew.getUserName();

    ActivityRecord newActivityRecord = activityRecordStorage.getLastActivityRecord(userName);

    // Throw
    assertNotNull(newActivityRecord);
  }
  
  @Test
  public void testCountActivityRecords() throws Exception {
    ActivityRecord activityRecord = createActivityRecordLocal();
    createActivityRecord(activityRecord);
    String searchParam = "test";
    String activityParam = "";
    String typeParam = "";
    String subTypeParam = "";
    String activityCodeParam = "";
    String subActivityCodeParam = "";
    String clientParam = "";
    String projectParam = "";
    String featureParam = "";
    String fromDateParam = activityRecord.getActivityDate();
    String toDateParam = activityRecord.getActivityDate();
    String userNameParam = activityRecord.getUserName();
    String locationParam = "";
    String officeParam = "";
    
    long newRecordsAccess = activityRecordStorage.countActivityRecords(searchParam, activityParam,
                                                                                      typeParam, subTypeParam, activityCodeParam, subActivityCodeParam, clientParam, projectParam,
                                                                                      featureParam, fromDateParam, toDateParam, userNameParam, locationParam, officeParam);

    // Throw
    assertNotNull(newRecordsAccess);
  }
  
  @Test
  public void testGetActivityRecordsList() throws Exception {
    ActivityRecord activityRecord = createActivityRecordLocal();
    ActivityRecord ActivityRecordNew = createActivityRecord(activityRecord);
    String searchParam = "test";
    String activityParam = "";
    String typeParam = "";
    String subTypeParam = "";
    String activityCodeParam = "";
    String subActivityCodeParam = "";
    String clientParam = "";
    String projectParam = "";
    String featureParam = "";
    String fromDateParam = activityRecord.getActivityDate();
    String toDateParam = activityRecord.getActivityDate();
    String userNameParam = activityRecord.getUserName();
    String locationParam = "";
    String officeParam = "";
    int offsetParam = 1;
    int limitParam = 1;
    boolean sortDescParam = true;
    RecordsAccessList newRecordsAccess = activityRecordStorage.getActivityRecordsList(searchParam, activityParam,
                                                                                      typeParam, subTypeParam, activityCodeParam, subActivityCodeParam, clientParam, projectParam,
                                                                                      featureParam, fromDateParam, toDateParam, userNameParam, locationParam, officeParam, offsetParam,
                                                                                      limitParam, officeParam, sortDescParam);

    // Throw
    assertNotNull(newRecordsAccess);
  }

  private ActivityRecord createActivityRecordLocal() throws Exception {
    Client clientCreated = new Client(null, "test", "test");
    Client client = createClient(clientCreated);
    Project projectCreated = new Project(null, "test", "test", client);
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
    SalesOrder salesOrderCreated = new SalesOrder(null, "test", "test", client);
    SalesOrder salesOrder = createSalesOrder(salesOrderCreated);
    Team teamCreated = new Team(null, "test", "test");
    Team team = createTeam(teamCreated);
    List<Team> teams = new ArrayList<>();
    teams.add(team);
    Activity activityCreated= new Activity(null, type, subType, activityCode, subActivityCode, "test", project, feature, teams);
    Activity activity= createActivity(activityCreated);
    Date testDate = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020");
    return new ActivityRecord(null, "root","2022-05-07", testDate, "test", "test", "test",
        5f, "test", client, activity, salesOrder, testDate, "RootRoot", project);
  }

  private Activity createActivity(Activity activity) throws Exception {
    List<Activity> activitys = activityStorage.getActivities();
    assertNotNull(activitys);
    Activity storedActivity = activityStorage.createActivity(activity);
    assertNotNull(storedActivity);
    cleanupActivities.add(storedActivity);
    return storedActivity;
  }
  
  private ActivityRecord createActivityRecord(ActivityRecord activityRecord) throws Exception {
    List<ActivityRecord> activityRecords = activityRecordStorage.getActivityRecords();
    assertNotNull(activityRecords);
    ActivityRecord storedActivityRecord = activityRecordStorage.createActivityRecord(activityRecord);
    assertNotNull(storedActivityRecord);
    cleanupActivityRecord.add(storedActivityRecord);
    return storedActivityRecord;
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
  
  private SalesOrder createSalesOrder(SalesOrder salesOrder) throws Exception {
    List<SalesOrder> salesOrders = salesOrderStorage.getSalesOrders();
    assertNotNull(salesOrders);
    SalesOrder storedSalesOrder = salesOrderStorage.createSalesOrder(salesOrder);
    assertNotNull(storedSalesOrder);
    cleanupSalesOrders.add(storedSalesOrder);
    return storedSalesOrder;
  }
}
