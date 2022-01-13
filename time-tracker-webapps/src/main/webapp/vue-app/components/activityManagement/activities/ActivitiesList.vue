<template>
  <div>
    <div
      v-if="alert"
      :class="alert_type"
      class="alert">
      <i :class="alertIcon"></i>
      {{ message }}
    </div>
    <v-flex>
      <v-data-table
        :headers="headers"
        :items="activities"
        :search="search"
        sort-by="label"
        class="elevation-1">
        <template v-slot:top>
          <v-toolbar flat color="white">
            <v-toolbar-title>{{ $t("exo.timeTracker.activities.activitiesList.toolbarTitle") }}</v-toolbar-title>
            <v-spacer />
            <v-divider
              class="mx-4"
              inset
              vertical />
            <v-text-field
              v-model="search"
              placeholder="Filter"
              prepend-inner-icon="fa-filter"
              class="inputFilter pa-0 mr-3 my-auto"
              clearable />                                            
            <button
              class="btn btn-primary pull-left"
              type="button"
              @click="openAddActivityDrawer">
              <i class="uiIconSocSimplePlus uiIconSocWhite"></i>{{ $t("exo.timeTracker.activities.text.add") }}
            </button>
          </v-toolbar>
        </template>
        <template v-slot:item.action="{ item }">
          <v-icon
            small
            class="mr-2"
            @click="openEditDrawer(item)">
            edit
          </v-icon>
          <v-icon small @click="deleteItem(item)">
            delete
          </v-icon>
        </template>
        <template v-slot:no-data>{{ $t("exo.timeTracker.activities.activitiesList.TextIfNoActivities") }}</template>
      </v-data-table>
    </v-flex>
    <add-activity-drawer
      ref="addActivityDrawer"
      :projects="projects"
      :features="features"
      :activity-codes="activityCodes"
      :sub-activity-codes="subActivityCodes"
      :types="types"
      :sub-types="subTypes"
      :teams="teams"
      :other-settings="otherSettings"
      @save="save" />
    <edit-activity-drawer
      ref="editActivityDrawer"
      :activity="editedItem"
      :projects="projects"
      :features="features"
      :activity-codes="activityCodes"
      :sub-activity-codes="subActivityCodes"
      :types="types"
      :sub-types="subTypes"
      :teams="teams"
      :other-settings="otherSettings"
      @save="update" />
  </div>
</template>

<script>
import addActivityDrawer from './AddActivityDrawer.vue';
import editActivityDrawer from './EditActivityDrawer.vue';
export default {
  components: {
    addActivityDrawer,
    editActivityDrawer,
  },
  props: { 
    projects: {
      type: Array,
      default: () => null,
    },
    features: {
      type: Array,
      default: () => null,
    },
    activityCodes: {
      type: Array,
      default: () => null,
    },
    subActivityCodes: {
      type: Array,
      default: () => null,
    },
    types: {
      type: Array,
      default: () => null,
    },
    subTypes: {
      type: Array,
      default: () => null,
    },
    teams: {
      type: Array,
      default: () => null,
    },
    otherSettings: {
      type: Object,
      default: () => null,
    }
  },
  data: () => ({
    search: '',
    alert: false,
    message: '',
    alert_type: '',
    alertIcon: '',
    valid: true,
    activities: [],
    editedIndex: -1,
    editedItem: {
      code: '',
      type: '',
      subType: true,
      activity: '',
      subActivity: '',
      label: '',
      project: {
        id: '',
      },
      feature: {
        id: '',
      },
    },
    defaultItem: {
      code: '',
      type: '',
      subType: '',
      activity: '',
      subActivity: '',
      label: '',
      project: {
        id: '',
        label: ''
      },
      feature: {
        id: '',
        label: ''
      },
    },
  }),
  computed: {
    headers() {
      return [{
        text: 'Label',
        align: 'center',
        sortable: true,
        value: 'label',
      },
      {
        text: 'Type',
        align: 'center',
        sortable: true,
        value: 'type.displayLabel',
      },{
        text: 'Sub Type',
        align: 'center',
        sortable: true,
        value: 'subType.displayLabel',
      },
      {
        text: 'Activity',
        align: 'center',
        sortable: true,
        value: 'activityCode.displayLabel',
      },
      {
        text: 'Sub Activity',
        align: 'center',
        sortable: true,
        value: 'subActivityCode.displayLabel',
      },
      {
        text: 'Client  ',
        align: 'center',
        sortable: true,
        value: 'project.client.displayLabel',
      },
      {
        text: 'Project',
        align: 'center',
        sortable: true,
        value: 'project.displayLabel',
      },
      {
        text: 'Feature',
        align: 'center',
        sortable: true,
        value: 'feature.displayLabel',
      },
      {
        text: 'Team',
        align: 'center',
        sortable: true,
        value: 'teamNames',
      },
      {
        text: 'Actions',
        align: 'center',
        sortable: true,
        value: 'action',
      }
      ];
    }
  },
  created() {
    this.initialize();
  },
  methods: {
    initialize() {
      fetch('/portal/rest/timetracker/activitymgn/activity/all', {
        credentials: 'include',
      })
        .then((resp) => resp.json())
        .then((resp) => {
          const activities = resp;
          activities.forEach(function(item) {
            const teams = item.teams.map(function (item) {
              return item['name'];
            });
            item.teamNames=teams.join();
          });
          this.activities=activities;
        });
    },
    editItem(item) {
      this.editedIndex = this.activities.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialog = true;
    },
    deleteItem(item) {
      const index = this.activities.indexOf(item);
      this.activities.splice(index, 1);
      this.delete_(item);
    },
    openAddActivityDrawer() {
      this.$refs.addActivityDrawer.open();
    },
    openEditDrawer(item) {
      this.editedItem=item;
      this.$refs.editActivityDrawer.open();
    },
    save(activity) {
      if (activity.feature.id===''){
        activity.feature=null;
      }
      this.activities.push(activity);
      fetch('/portal/rest/timetracker/activitymgn/activity', {
        method: 'post',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(activity),
      })
        .then((result) => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.initialize();
          this.displaySusccessMessage('activity added');
        })
        .catch((result) => {
          this.initialize();
          result.text().then((body) => {
            this.displayErrorMessage(body);
          });
        });
    },
    update() {
      fetch('/portal/rest/timetracker/activitymgn/activity', {
        method: 'put',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(this.editedItem),
      })
        .then((result) => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.initialize();
          this.displaySusccessMessage('activity updated');
        })
        .catch((result) => {
          this.initialize();
          result.text().then((body) => {
            this.displayErrorMessage(body);
          });
        });
    },
    delete_(item) {
      fetch(`/portal/rest/timetracker/activitymgn/activity/${  item.id}`, {
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
          this.initialize();
          this.displaySusccessMessage('activity deleted');
          this.showDetails = false;
          this.showTable = true;
        })
        .catch((result) => {
          this.confirmDialog = false;
          this.initialize();
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
    getTypeTitle(item) {
      const type =  this.fieldList.find(x => x.value === item.type);
      if (type!=null) {return type.title;}
      return item.type.value;
    },
    getTeamNames(item){
      const teams = item.teams.map(function (item) {
        return item['name'];
      });
      item.teamNames=teams.join();
      return teams.join();
    }
  }
};
</script>

<style>
#activityManagementApp {
    overflow: hidden;
    padding: 10px 20px;
}

select {
    width: auto;
}

#activityManagementApp .v-input input {
    margin-bottom: 0;
    border: 0;
    box-shadow: none;
}

#activityManagementApp .v-toolbar .v-input {
    margin-left: 18px;
}

#activityManagementApp .v-data-table {
 width: 100%;
}
</style>
