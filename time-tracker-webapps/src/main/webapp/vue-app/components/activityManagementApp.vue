<template>
  <v-app
    id="activityManagementApp"
    class="VuetifyApp timeTrackingApp "
    color="transaprent">
    <main>
      <div
        v-if="alert"
        id
        :class="alert_type"
        class="alert">
        <i :class="alertIcon"></i>
        {{ message }}
      </div>
      <template>
        <v-tabs
          v-model="selectedTab"
          class="tabContainer"
          grow>
          <v-tabs-slider color="primary" />
          <v-tab key="activities" href="#activities">
            {{ $t("exo.timeTracker.activityManagementApp.vTab.activities") }}
          </v-tab>
          <v-tab key="clients" href="#clients">
            {{ $t("exo.timeTracker.activityManagementApp.vTab.Clients") }}
          </v-tab>
          <v-tab key="projects" href="#projects">
            {{ $t("exo.timeTracker.activityManagementApp.vTab.Projects") }}
          </v-tab>
          <v-tab key="features" href="#features">
            {{ $t("exo.timeTracker.activityManagementApp.vTab.Features") }}
          </v-tab>
          <v-tab key="activitiesCodes" href="#activitiesCodes">
            {{ $t("exo.timeTracker.activityManagementApp.vTab.ActivitiesCodes") }}
          </v-tab>
          <v-tab key="typesCodes" href="#typesCodes">
            {{ $t("exo.timeTracker.activityManagementApp.vTab.TypesCodes") }}
          </v-tab>
          <v-tab key="teams" href="#teams">
            {{ $t("exo.timeTracker.activityManagementApp.vTab.Teams") }}
          </v-tab>
          <v-tab key="ttsettings" href="#ttsettings">
            {{ $t("exo.timeTracker.activityManagementApp.vTab.Settings") }}
          </v-tab>
        </v-tabs>
        <v-tabs-items v-model="selectedTab" class="infoContent">
          <v-tab-item
            id="activities"
            class="tabContent"
            eager
            value="activities">
            <activities-list
              :projects="projects"
              :features="features"
              :activity-codes="activityCodes"
              :sub-activity-codes="subActivityCodes"
              :types="types"
              :sub-types="subTypes"
              :teams="teams"
              :other-settings="otherSettings" />
          </v-tab-item>
          <v-tab-item
            id="clients"
            class="tabContent"
            eager
            value="clients">
            <clients-list
              :clients="clients"
              @addClient="addClient"
              @editClient="editClient"
              @delete="deleteClient" />
          </v-tab-item>
          <v-tab-item
            id="projects"
            class="tabContent"
            eager
            value="projects">
            <projects-list
              :projects="projects"
              :clients="clients"
              @addProject="addProject"
              @editProject="editProject"
              @delete="deleteProject" />
          </v-tab-item>
          <v-tab-item
            id="features"
            class="tabContent"
            eager
            value="features">
            <features-list
              :features="features"
              @addFeature="addFeature"
              @editFeature="editFeature"
              @delete="deleteFeature" />
          </v-tab-item>
          <v-tab-item
            id="activitiesCodes"
            class="tabContent"
            eager
            value="activitiesCodes">
            <codes-list
              :activity-codes="activityCodes"
              :sub-activity-codes="subActivityCodes"
              @addActivityCode="addActivityCode"
              @editActivityCode="editActivityCode"
              @deleteActivityCode="deleteActivityCode"
              @addSubActivityCode="addSubActivityCode"
              @editSubActivityCode="editSubActivityCode"
              @deleteSubActivityCode="deleteSubActivityCode" />
          </v-tab-item>
          <v-tab-item
            id="typesCodes"
            class="tabContent"
            eager
            value="typesCodes">
            <types-codes-list
              :types="types"
              :sub-types="subTypes"
              @addType="addType"
              @editType="editType"
              @deleteType="deleteType"
              @addSubType="addSubType"
              @editSubType="editSubType"
              @deleteSubType="deleteSubType" />
          </v-tab-item>
          <v-tab-item
            id="teams"
            class="tabContent"
            eager
            value="teams">
            <teams-list
              :teams="teams"
              @addTeam="addTeam"
              @editTeam="editTeam"
              @deleteTeam="deleteTeam" />
          </v-tab-item>
          <v-tab-item
            id="ttsettings"
            class="tabContent"
            eager
            value="ttsettings">
            <time-tracking-settings
              :other-settings="otherSettings"
              :offices="offices"
              :locations="locations"
              :work-time-plans="workTimePlans"
              :sub-activity-codes="subActivityCodes"
              :teams="teams"
              @saveOtherSettings="saveOtherSettings"
              @getOtherSettings="getOtherSettings"
              @addOffice="addOffice"
              @editOffice="editOffice"
              @deleteOffice="deleteOffice"
              @addLocation="addLocation"
              @editLocation="editLocation"
              @deleteLocation="deleteLocation"
              @addWorkTime="addWorkTimePlan"
              @editWorkTime="editWorkTimePlan"
              @deleteWorkTime="deleteWorkTimePlan" />
          </v-tab-item>
        </v-tabs-items>
      </template>
    </main>
  </v-app>
</template>

<script>
import ActivitiesList from './activityManagement/activities/ActivitiesList.vue';
import ClientsList from './activityManagement/clients/ClientsList.vue';
import ProjectsList from './activityManagement/projects/ProjectsList.vue';
import FeaturesList from './activityManagement/features/FeaturesList.vue';
import CodesList from './activityManagement/codes/CodesList.vue';
import TypesCodesList from './activityManagement/codes/TypesCodesList.vue';
import TeamsList from './activityManagement/teams/TeamsList.vue';
import TimeTrackingSettings from './activityManagement/timeTrackingSettings/TimeTrakingSettings.vue';
export default {
  components: {
    ActivitiesList,
    ClientsList,
    ProjectsList,
    FeaturesList,
    CodesList,
    TypesCodesList,
    TeamsList,
    TimeTrackingSettings,
  },
  data: () => ({
    alert: false,
    message: '',
    alert_type: '',
    alertIcon: '',
    selectedTab: 'activities',
    clients: [],
    projects: [],
    features: [],
    activityCodes: [],
    subActivityCodes: [],
    types: [],
    subTypes: [],
    teams: [],
    offices: [],
    locations: [],
    workTimePlans: [],
    otherSettings: {weekEndHolidayActivity: {id: ''}},
  }),
  created() {
    this.initialize();
  },
  methods: {
    initialize() {
      this.getWorkTimePlans();
      this.getLocations();
      this.getOffices();
      this.getOtherSettings();
      this.getClients();
      this.getProjects();
      this.getActivityCodes();
      this.getSubActivityCodes();
      this.getTypes();
      this.getSubTypes();
      this.getFeatures();
      this.getTeams();
    },
    compare(a, b) {
      const displayLabelA = a.displayLabel.toUpperCase();
      const displayLabelB = b.displayLabel.toUpperCase();
      let comparison = 0;
      if (displayLabelA > displayLabelB) {
        comparison = 1;
      } else if (displayLabelA < displayLabelB) {
        comparison = -1;
      }
      return comparison;
    },
    getClients() {
      fetch('/portal/rest/timetracker/clientsmgn/client', {
        credentials: 'include',
      })
        .then((resp) => resp.json())
        .then((resp) => {
          this.clients = resp.sort(this.compare);
        });
    },
    deleteClient(item) {
      fetch(`/portal/rest/timetracker/clientsmgn/client/${  item.id}`, {
        method: 'delete',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
      })
        .then((result) => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.confirmDialog = false;
          this.getClients();
          this.displaySusccessMessage('client deleted');
        })
        .catch((result) => {
          this.confirmDialog = false;
          this.getClients();
          result.text().then((body) => {
            this.displayErrorMessage(body);
          });
        });
    },
    addClient(client) {
      this.clients.push(client);
      fetch('/portal/rest/timetracker/clientsmgn/client', {
        method: 'post',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(client),
      })
        .then((result) => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.getClients();
          this.displaySusccessMessage('client added');
        })
        .catch((result) => {
          this.getClients();
          result.text().then((body) => {
            this.displayErrorMessage(body);
          });
        });
    },
    editClient(client) {
      fetch('/portal/rest/timetracker/clientsmgn/client', {
        method: 'put',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(client),
      })
        .then((result) => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.getClients();
          this.displaySusccessMessage('client updated');
        })
        .catch((result) => {
          this.getClients();
          result.text().then((body) => {
            this.displayErrorMessage(body);
          });
        });
    },
    getProjects() {
      fetch('/portal/rest/timetracker/projectsmgn/project', {
        credentials: 'include',
      })
        .then((resp) => resp.json())
        .then((resp) => {
          this.projects = resp.sort(this.compare);
        });
    },
    deleteProject(item) {
      fetch(`/portal/rest/timetracker/projectsmgn/project/${  item.id}`, {
        method: 'delete',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
      })
        .then((result) => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.confirmDialog = false;
          this.getProjects();
          this.displaySusccessMessage('project deleted');
        })
        .catch((result) => {
          this.confirmDialog = false;
          this.getProjects();
          result.text().then((body) => {
            this.displayErrorMessage(body);
          });
        });
    },
    addProject(project) {
      this.projects.push(project);
      fetch('/portal/rest/timetracker/projectsmgn/project', {
        method: 'post',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(project),
      })
        .then((result) => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.getProjects();
          this.displaySusccessMessage('project added');
        })
        .catch((result) => {
          this.getProjects();
          result.text().then((body) => {
            this.displayErrorMessage(body);
          });
        });
    },
    editProject(project) {
      fetch('/portal/rest/timetracker/projectsmgn/project', {
        method: 'put',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(project),
      })
        .then((result) => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.getProjects();
          this.displaySusccessMessage('project updated');
        })
        .catch((result) => {
          this.getProjects();
          result.text().then((body) => {
            this.displayErrorMessage(body);
          });
        });
    },
    getFeatures() {
      fetch('/portal/rest/timetracker/featuresmgn/feature', {
        credentials: 'include',
      })
        .then((resp) => resp.json())
        .then((resp) => {
          this.features = resp.sort(this.compare);
        });
    },
    deleteFeature(item) {
      fetch(`/portal/rest/timetracker/featuresmgn/feature/${  item.id}`, {
        method: 'delete',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
      })
        .then((result) => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.confirmDialog = false;
          this.getFeatures();
          this.displaySusccessMessage('feature deleted');
        })
        .catch((result) => {
          this.confirmDialog = false;
          this.getFeatures();
          result.text().then((body) => {
            this.displayErrorMessage(body);
          });
        });
    },
    addFeature(feature) {
      this.features.push(feature);
      fetch('/portal/rest/timetracker/featuresmgn/feature', {
        method: 'post',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(feature),
      })
        .then((result) => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.getFeatures();
          this.displaySusccessMessage('feature added');
        })
        .catch((result) => {
          this.getFeatures();
          result.text().then((body) => {
            this.displayErrorMessage(body);
          });
        });
    },
    editFeature(feature) {
      fetch('/portal/rest/timetracker/featuresmgn/feature', {
        method: 'put',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(feature),
      })
        .then((result) => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.getFeatures();
          this.displaySusccessMessage('feature updated');
        })
        .catch((result) => {
          this.getFeatures();
          result.text().then((body) => {
            this.displayErrorMessage(body);
          });
        });
    },
    getActivityCodes() {
      fetch('/portal/rest/timetracker/codesmgn/activityCode', {
        credentials: 'include',
      })
        .then((resp) => resp.json())
        .then((resp) => {
          this.activityCodes = resp.sort(this.compare);
        });
    },
    deleteActivityCode(item) {
      fetch(`/portal/rest/timetracker/codesmgn/activityCode/${  item.id}`, {
        method: 'delete',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
      })
        .then((result) => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.confirmDialog = false;
          this.getActivityCodes();
          this.displaySusccessMessage('activityCode deleted');
        })
        .catch((result) => {
          this.confirmDialog = false;
          this.getActivityCodes();
          result.text().then((body) => {
            this.displayErrorMessage(body);
          });
        });
    },
    addActivityCode(activityCode) {
      this.activityCodes.push(activityCode);
      fetch('/portal/rest/timetracker/codesmgn/activityCode', {
        method: 'post',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(activityCode),
      })
        .then((result) => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.getActivityCodes();
          this.displaySusccessMessage('activityCode added');
        })
        .catch((result) => {
          this.getActivityCodes();
          result.text().then((body) => {
            this.displayErrorMessage(body);
          });
        });
    },
    editActivityCode(activityCode) {
      fetch('/portal/rest/timetracker/codesmgn/activityCode', {
        method: 'put',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(activityCode),
      })
        .then((result) => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.getActivityCodes();
          this.displaySusccessMessage('activityCode updated');
        })
        .catch((result) => {
          this.getActivityCodes();
          result.text().then((body) => {
            this.displayErrorMessage(body);
          });
        });
    },
    getSubActivityCodes() {
      fetch('/portal/rest/timetracker/codesmgn/subActivityCode', {
        credentials: 'include',
      })
        .then((resp) => resp.json())
        .then((resp) => {
          this.subActivityCodes = resp.sort(this.compare);
        });
    },
    deleteSubActivityCode(item) {
      fetch(`/portal/rest/timetracker/codesmgn/subActivityCode/${  item.id}`, {
        method: 'delete',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
      })
        .then((result) => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.confirmDialog = false;
          this.getSubActivityCodes();
          this.displaySusccessMessage('subActivityCode deleted');
        })
        .catch((result) => {
          this.confirmDialog = false;
          this.getSubActivityCodes();
          result.text().then((body) => {
            this.displayErrorMessage(body);
          });
        });
    },
    addSubActivityCode(subActivityCode) {
      this.subActivityCodes.push(subActivityCode);
      fetch('/portal/rest/timetracker/codesmgn/subActivityCode', {
        method: 'post',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(subActivityCode),
      })
        .then((result) => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.getSubActivityCodes();
          this.displaySusccessMessage('subActivityCode added');
        })
        .catch((result) => {
          this.getSubActivityCodes();
          result.text().then((body) => {
            this.displayErrorMessage(body);
          });
        });
    },
    editSubActivityCode(subActivityCode) {
      fetch('/portal/rest/timetracker/codesmgn/subActivityCode', {
        method: 'put',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(subActivityCode),
      })
        .then((result) => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.getSubActivityCodes();
          this.displaySusccessMessage('subActivityCode updated');
        })
        .catch((result) => {
          this.getSubActivityCodes();
          result.text().then((body) => {
            this.displayErrorMessage(body);
          });
        });
    },
    getTypes() {
      fetch('/portal/rest/timetracker/codesmgn/type', {
        credentials: 'include',
      })
        .then((resp) => resp.json())
        .then((resp) => {
          this.types = resp.sort(this.compare);
        });
    },
    deleteType(item) {
      fetch(`/portal/rest/timetracker/codesmgn/type/${  item.id}`, {
        method: 'delete',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
      })
        .then((result) => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.confirmDialog = false;
          this.getTypes();
          this.displaySusccessMessage('type deleted');
        })
        .catch((result) => {
          this.confirmDialog = false;
          this.getTypes();
          result.text().then((body) => {
            this.displayErrorMessage(body);
          });
        });
    },
    addType(type) {
      this.types.push(type);
      fetch('/portal/rest/timetracker/codesmgn/type', {
        method: 'post',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(type),
      })
        .then((result) => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.getTypes();
          this.displaySusccessMessage('type added');
        })
        .catch((result) => {
          this.getTypes();
          result.text().then((body) => {
            this.displayErrorMessage(body);
          });
        });
    },
    editType(type) {
      fetch('/portal/rest/timetracker/codesmgn/type', {
        method: 'put',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(type),
      })
        .then((result) => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.getTypes();
          this.displaySusccessMessage('type updated');
        })
        .catch((result) => {
          this.getTypes();
          result.text().then((body) => {
            this.displayErrorMessage(body);
          });
        });
    },
    getSubTypes() {
      fetch('/portal/rest/timetracker/codesmgn/subType', {
        credentials: 'include',
      })
        .then((resp) => resp.json())
        .then((resp) => {
          this.subTypes = resp.sort(this.compare);
        });
    },
    deleteSubType(item) {
      fetch(`/portal/rest/timetracker/codesmgn/subType/${  item.id}`, {
        method: 'delete',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
      })
        .then((result) => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.confirmDialog = false;
          this.getSubTypes();
          this.displaySusccessMessage('subType deleted');
        })
        .catch((result) => {
          this.confirmDialog = false;
          this.getSubTypes();
          result.text().then((body) => {
            this.displayErrorMessage(body);
          });
        });
    },
    addSubType(subType) {
      this.subTypes.push(subType);
      fetch('/portal/rest/timetracker/codesmgn/subType', {
        method: 'post',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(subType),
      })
        .then((result) => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.getSubTypes();
          this.displaySusccessMessage('subType added');
        })
        .catch((result) => {
          this.getSubTypes();
          result.text().then((body) => {
            this.displayErrorMessage(body);
          });
        });
    },
    editSubType(subType) {
      fetch('/portal/rest/timetracker/codesmgn/subType', {
        method: 'put',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(subType),
      })
        .then((result) => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.getSubTypes();
          this.displaySusccessMessage('subType updated');
        })
        .catch((result) => {
          this.getSubTypes();
          result.text().then((body) => {
            this.displayErrorMessage(body);
          });
        });
    },
    getTeams() {
      fetch('/portal/rest/timetracker/teamsmgn/team/all', {
        credentials: 'include',
      })
        .then((resp) => resp.json())
        .then((resp) => {
          this.teams = resp;
        });
    },
    deleteTeam(item) {
      fetch(`/portal/rest/timetracker/teamsmgn/team?teamId=${  item.id}`, {
        method: 'delete',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
      })
        .then((result) => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.confirmDialog = false;
          this.getTeams();
          this.displaySusccessMessage('team deleted');
        })
        .catch((result) => {
          this.confirmDialog = false;
          this.getTeams();
          result.text().then((body) => {
            this.displayErrorMessage(body);
          });
        });
    },
    addTeam(team) {
      this.teams.push(team);
      fetch('/portal/rest/timetracker/teamsmgn/team', {
        method: 'post',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(team),
      })
        .then((result) => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.getTeams();
          this.displaySusccessMessage('team added');
        })
        .catch((result) => {
          this.getTeams();
          result.text().then((body) => {
            this.displayErrorMessage(body);
          });
        });
    },
    editTeam(team) {
      fetch('/portal/rest/timetracker/teamsmgn/team', {
        method: 'put',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(team),
      })
        .then((result) => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.getTeams();
          this.displaySusccessMessage('team updated');
        })
        .catch((result) => {
          this.getTeams();
          result.text().then((body) => {
            this.displayErrorMessage(body);
          });
        });
    },
    getWorkTimePlans() {
      fetch('/portal/rest/timetracker/settings/worktime', {
        credentials: 'include',
      })
        .then((resp) => resp.json())
        .then((resp) => {
          this.workTimePlans = resp;
        });
    },
    deleteWorkTimePlan(item) {
      fetch(`/portal/rest/timetracker/settings/worktime/${  item.id}`, {
        method: 'delete',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
      })
        .then((result) => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.confirmDialog = false;
          this.getWorkTimePlans();
          this.displaySusccessMessage('workTimePlan deleted');
        })
        .catch((result) => {
          this.confirmDialog = false;
          this.getWorkTimePlans();
          result.text().then((body) => {
            this.displayErrorMessage(body);
          });
        });
    },
    addWorkTimePlan(workTimePlan) {
      this.workTimePlans.push(workTimePlan);
      fetch('/portal/rest/timetracker/settings/worktime', {
        method: 'post',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(workTimePlan),
      })
        .then((result) => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.getWorkTimePlans();
          this.displaySusccessMessage('workTimePlan added');
        })
        .catch((result) => {
          this.getWorkTimePlans();
          result.text().then((body) => {
            this.displayErrorMessage(body);
          });
        });
    },
    editWorkTimePlan(workTimePlan) {
      fetch('/portal/rest/timetracker/settings/worktime', {
        method: 'put',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(workTimePlan),
      })
        .then((result) => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.getWorkTimePlans();
          this.displaySusccessMessage('workTimePlan updated');
        })
        .catch((result) => {
          this.getWorkTimePlans();
          result.text().then((body) => {
            this.displayErrorMessage(body);
          });
        });
    },
    getLocations() {
      fetch('/portal/rest/timetracker/settings/location', {
        credentials: 'include',
      })
        .then((resp) => resp.json())
        .then((resp) => {
          this.locations = resp;
        });
    },
    deleteLocation(item) {
      fetch(`/portal/rest/timetracker/settings/location/${  item.code}`, {
        method: 'delete',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
      })
        .then((result) => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.confirmDialog = false;
          this.getLocations();
          this.displaySusccessMessage('location deleted');
        })
        .catch((result) => {
          this.confirmDialog = false;
          this.getLocations();
          result.text().then((body) => {
            this.displayErrorMessage(body);
          });
        });
    },
    addLocation(location) {
      this.locations.push(location);
      fetch('/portal/rest/timetracker/settings/location', {
        method: 'post',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(location),
      })
        .then((result) => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.getLocations();
          this.displaySusccessMessage('location added');
        })
        .catch((result) => {
          this.getLocations();
          result.text().then((body) => {
            this.displayErrorMessage(body);
          });
        });
    },
    editLocation(location) {
      fetch('/portal/rest/timetracker/settings/location', {
        method: 'put',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(location),
      })
        .then((result) => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.getLocations();
          this.displaySusccessMessage('location updated');
        })
        .catch((result) => {
          this.getLocations();
          result.text().then((body) => {
            this.displayErrorMessage(body);
          });
        });
    },
    getOffices() {
      fetch('/portal/rest/timetracker/settings/office', {
        credentials: 'include',
      })
        .then((resp) => resp.json())
        .then((resp) => {
          this.offices = resp;
        });
    },
    deleteOffice(item) {
      fetch(`/portal/rest/timetracker/settings/office/${  item.code}`, {
        method: 'delete',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
      })
        .then((result) => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.confirmDialog = false;
          this.getOffices();
          this.displaySusccessMessage('office deleted');
        })
        .catch((result) => {
          this.confirmDialog = false;
          this.getOffices();
          result.text().then((body) => {
            this.displayErrorMessage(body);
          });
        });
    },
    addOffice(office) {
      this.offices.push(office);
      fetch('/portal/rest/timetracker/settings/office', {
        method: 'post',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(office),
      })
        .then((result) => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.getOffices();
          this.displaySusccessMessage('office added');
        })
        .catch((result) => {
          this.getOffices();
          result.text().then((body) => {
            this.displayErrorMessage(body);
          });
        });
    },
    editOffice(office) {
      fetch('/portal/rest/timetracker/settings/office', {
        method: 'put',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(office),
      })
        .then((result) => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.getOffices();
          this.displaySusccessMessage('office updated');
        })
        .catch((result) => {
          this.getOffices();
          result.text().then((body) => {
            this.displayErrorMessage(body);
          });
        });
    },
    getOtherSettings() {
      fetch('/portal/rest/timetracker/settings/other', {
        credentials: 'include',
      })
        .then((resp) => resp.json())
        .then((resp) => {
          this.otherSettings = resp;
          if (!this.otherSettings.weekEndHolidayActivity || !this.otherSettings.weekEndHolidayActivity.id){
            this.otherSettings.weekEndHolidayActivity ={id: ''};
          }
        });
    },
    saveOtherSettings(otherSettings) {
      fetch('/portal/rest/timetracker/settings/others', {
        method: 'PATCH',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(otherSettings),
      })
        .then((result) => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.getOtherSettings();
          this.displaySusccessMessage('office updated');
        })
        .catch((result) => {
          this.getOffices();
          result.text().then((body) => {
            this.displayErrorMessage(body);
          });
        });
    },
    displaySusccessMessage(message) {
      this.message = message;
      this.alert_type = 'alert-success';
      this.alertIcon = 'uiIconSuccess';
      this.alert = true;
      setTimeout(() => (this.alert = false), 5000);
    },
    displayErrorMessage(message) {
      this.isUpdating = false;
      this.message = message;
      this.alert_type = 'alert-error';
      this.alertIcon = 'uiIconError';
      this.alert = true;
      setTimeout(() => (this.alert = false), 5000);
    },
  },
};
</script>

<style>

select {
    width: auto;
}

#activityManagementApp .v-input input {
    margin-bottom: 0;
    border: 0;
    box-shadow: none;
}

#activityManagementApp .v-input textarea {
    margin-bottom: 0;
    border: 0 !important;
    box-shadow: none;
}

#activityManagementApp .v-toolbar .v-input {
    margin-left: 18px;
}

#activityManagementApp .v-data-table {
    width: 100%;
}

#activityManagementApp .v-navigation-drawer--temporary {
    z-index: 3000;
}

#activityManagementApp .drawer-title {
    font-size: 18px;
    font-weight: bold;
    color: #4d5466;
}

#activityManagementApp .drawerContent .v-label {
    font-weight: 700 !important;
    ;
}

#activityManagementApp .v-badge__badge {
    height: 10px !important;
    min-width: 10px !important;
}

.drawerTitle {
    font-size: 18px;
    font-weight: bold;
    color: @darkGrey;
}

.headerBorder {
    border-color: #f0f4fe !important;
}

.drawersBtn {
    border: 1px solid #1976d27a !important;
    margin-right: 5px;
    padding: 8px 15px;
    border-radius: 3px !important;
    cursor: pointer !important;
    letter-spacing: .5px;
}

.drawerIcons {
    flex-direction: row !important;
}

.infoContent {
    border-top: 1px solid #e1e8ee;
}

.inputFilter {
          max-width: 250px !important;
        }
</style>
