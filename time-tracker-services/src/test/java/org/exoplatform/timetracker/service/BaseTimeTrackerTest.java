package org.exoplatform.timetracker.service;

import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.component.RequestLifeCycle;
import org.exoplatform.timetracker.dto.Activity;
import org.exoplatform.timetracker.dto.ActivityCode;
import org.exoplatform.timetracker.dto.ActivityRecord;
import org.exoplatform.timetracker.dto.Client;
import org.exoplatform.timetracker.dto.Feature;
import org.exoplatform.timetracker.dto.Filter;
import org.exoplatform.timetracker.dto.FilterField;
import org.exoplatform.timetracker.dto.Location;
import org.exoplatform.timetracker.dto.Office;
import org.exoplatform.timetracker.dto.Project;
import org.exoplatform.timetracker.dto.SalesOrder;
import org.exoplatform.timetracker.dto.SubActivityCode;
import org.exoplatform.timetracker.dto.SubType;
import org.exoplatform.timetracker.dto.Team;
import org.exoplatform.timetracker.dto.Type;
import org.exoplatform.timetracker.dto.WorkTime;
import org.exoplatform.timetracker.storage.ActivityRecordStorage;
import org.exoplatform.timetracker.storage.ActivityStorage;
import org.exoplatform.timetracker.storage.ClientStorage;
import org.exoplatform.timetracker.storage.CodesStorage;
import org.exoplatform.timetracker.storage.FeatureStorage;
import org.exoplatform.timetracker.storage.FilterStorage;
import org.exoplatform.timetracker.storage.ProjectStorage;
import org.exoplatform.timetracker.storage.SalesOrderStorage;
import org.exoplatform.timetracker.storage.SettingsStorage;
import org.exoplatform.timetracker.storage.TeamStorage;
import org.junit.After;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

public class BaseTimeTrackerTest {

  protected PortalContainer             container;

  public ClientStorage                  clientStorage;

  public FeatureStorage                 featureStorage;

  public ProjectStorage                 projectStorage;

  public SalesOrderStorage              salesOrderStorage;

  public ActivityStorage                activityStorage;

  public ActivityRecordStorage          activityRecordStorage;

  public CodesStorage                   codesStorage;

  public SettingsStorage                settingsStorage;

  public TeamStorage                    teamStorage;

  public FilterStorage                  filterStorage;

  protected List<ActivityCode>          cleanupActivityCodes         = new ArrayList<>();

  protected List<SubActivityCode>       cleanupSubActivityCodes      = new ArrayList<>();

  protected List<Type>                  cleanupTypes                 = new ArrayList<>();

  protected List<SubType>               cleanupSubTypes              = new ArrayList<>();

  protected List<Location>              cleanupLocations             = new ArrayList<>();

  protected List<Office>                cleanupOffices               = new ArrayList<>();

  protected List<WorkTime>              cleanupWorkTimes             = new ArrayList<>();

  protected List<Project>               cleanupProjects              = new ArrayList<>();

  protected List<Client>                cleanupClients               = new ArrayList<>();

  protected List<Filter>                cleanupFilters               = new ArrayList<>();

  protected List<FilterField>           cleanupFilterFields          = new ArrayList<>();

  protected List<Activity>              cleanupActivities            = new ArrayList<>();

  protected List<Feature>               cleanupFeatures              = new ArrayList<>();

  protected List<Team>                  cleanupTeams                 = new ArrayList<>();

  protected List<SalesOrder>            cleanupSalesOrders           = new ArrayList<>();

  protected List<ActivityRecord> cleanupActivityRecord = new ArrayList<>();

  @Before
  public void setUp() throws Exception {
    container = PortalContainer.getInstance();
    initServices();
    begin();
  }

  private void initServices() {
    clientStorage = container.getComponentInstanceOfType(ClientStorage.class);
    featureStorage = container.getComponentInstanceOfType(FeatureStorage.class);
    projectStorage = container.getComponentInstanceOfType(ProjectStorage.class);
    salesOrderStorage = container.getComponentInstanceOfType(SalesOrderStorage.class);
    activityStorage = container.getComponentInstanceOfType(ActivityStorage.class);
    activityRecordStorage = container.getComponentInstanceOfType(ActivityRecordStorage.class);
    codesStorage = container.getComponentInstanceOfType(CodesStorage.class);
    settingsStorage = container.getComponentInstanceOfType(SettingsStorage.class);
    teamStorage = container.getComponentInstanceOfType(TeamStorage.class);
    filterStorage = container.getComponentInstanceOfType(FilterStorage.class);
  }

  @After
  public void tearDown() throws Exception {
    end();
    begin();
    try {
      for (ActivityRecord activityRecord : cleanupActivityRecord) {
        activityRecordStorage.deleteActivityRecord(activityRecord.getId());
      }
      for (SalesOrder salesOrder : cleanupSalesOrders) {
        salesOrderStorage.deleteSalesOrder(salesOrder.getId());
      }
      for (Activity activity : cleanupActivities) {
        activityStorage.deleteActivity(activity.getId());
      }
      for (SubActivityCode subActivityCode : cleanupSubActivityCodes) {
        codesStorage.deleteSubActivityCode(subActivityCode.getId());
      }
      for (ActivityCode activityCode : cleanupActivityCodes) {
        codesStorage.deleteActivityCode(activityCode.getId());
      }
      for (SubType subType : cleanupSubTypes) {
        codesStorage.deleteSubType(subType.getId());
      }
      for (Type type : cleanupTypes) {
        codesStorage.deleteType(type.getId());
      }
      for (WorkTime workTime : cleanupWorkTimes) {
        settingsStorage.deleteWorkTime(workTime.getId());
      }
      for (Location location : cleanupLocations) {
        settingsStorage.deleteLocation(location.getCode());
      }
      for (Office office : cleanupOffices) {
        settingsStorage.deleteOffice(office.getCode());
      }
      for (Project project : cleanupProjects) {
        projectStorage.deleteProject(project.getId());
      }
      for (Client client : cleanupClients) {
        clientStorage.deleteClient(client.getId());
      }
      for (FilterField filterField : cleanupFilterFields) {
        filterStorage.deleteFilterField(filterField.getId());
      }
      for (Filter filter : cleanupFilters) {
        filterStorage.deleteFilter(filter.getId());
      }
      for (Feature feature : cleanupFeatures) {
        featureStorage.deleteFeature(feature.getId());
      }
      for (Team team : cleanupTeams) {
        teamStorage.deleteTeam(team.getId());
      }
    } finally {
      end();
    }
  }

  protected void begin() {
    ExoContainerContext.setCurrentContainer(container);
    RequestLifeCycle.begin(container);
  }

  protected void end() {
    RequestLifeCycle.end();
  }

}
