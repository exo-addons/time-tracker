<template>
  <div>
    <v-flex>
      <v-data-table
        :headers="officeHeaders"
        :items="offices"
        sort-by="label"
        class="elevation-1">
        <template v-slot:top>
          <v-toolbar flat color="white">
            <v-toolbar-title>
              {{ $t("exo.timeTracker.timeTrackingSettings.office.toolbarTitle") }}
            </v-toolbar-title>
            <v-divider
              class="mx-4"
              inset
              vertical />
            <v-spacer />
            <v-divider
              class="mx-4"
              inset
              vertical />
            <button
              class="btn btn-primary pull-left"
              type="button"
              @click="openAddOfficeDrawer">
              <i class="uiIconSocSimplePlus uiIconSocWhite"></i>
              {{ $t("exo.timeTracker.timeTrackingSettings.text.add.office") }}
            </button>
          </v-toolbar>
        </template>
        <template v-slot:item.action="{ item }">
          <v-icon
            small
            class="mr-2"
            @click="openEditOfficeDrawer(item)">
            edit
          </v-icon>
          <v-icon small @click="deleteOffice(item)">
            delete
          </v-icon>
        </template>
        <template v-slot:no-data>
          {{ $t("exo.timeTracker.timeTrackingSettings.textIfNoOffice") }}
        </template>
      </v-data-table>
    </v-flex>
    <v-flex>
      <v-data-table
        :headers="locationHeaders"
        :items="locations"
        sort-by="label"
        class="elevation-1">
        <template v-slot:top>
          <v-toolbar flat color="white">
            <v-toolbar-title>
              {{ $t("exo.timeTracker.timeTrackingSettings.location.toolbarTitle") }}
            </v-toolbar-title>
            <v-divider
              class="mx-4"
              inset
              vertical />
            <v-spacer />
            <v-divider
              class="mx-4"
              inset
              vertical />
            <button
              class="btn btn-primary pull-left"
              type="button"
              @click="openAddLocationDrawer">
              <i class="uiIconSocSimplePlus uiIconSocWhite"></i>
              {{ $t("exo.timeTracker.timeTrackingSettings.text.add.location") }}
            </button>
          </v-toolbar>
        </template>
        <template v-slot:item.action="{ item }">
          <v-icon
            small
            class="mr-2"
            @click="openEditLocationDrawer(item)">
            edit
          </v-icon>
          <v-icon small @click="deleteLocation(item)">
            delete
          </v-icon>
        </template>
        <template v-slot:no-data>
          {{ $t("exo.timeTracker.timeTrackingSettings.textIfNoLocation") }}
        </template>
      </v-data-table>
    </v-flex>
    <v-flex>
      <v-data-table
        :headers="workTimeHeaders"
        :items="workTimePlans"
        sort-by="label"
        class="elevation-1">
        <template v-slot:top>
          <v-toolbar flat color="white">
            <v-toolbar-title>
              {{ $t("exo.timeTracker.timeTrackingSettings.worckTime.toolbarTitle") }}
            </v-toolbar-title>
            <v-divider
              class="mx-4"
              inset
              vertical />
            <v-spacer />
            <v-divider
              class="mx-4"
              inset
              vertical />
            <button
              class="btn btn-primary pull-left"
              type="button"
              @click="openAddWorkTimeDrawer">
              <i class="uiIconSocSimplePlus uiIconSocWhite"></i>
              {{ $t("exo.timeTracker.timeTrackingSettings.text.add.worckTime") }}
            </button>
          </v-toolbar>
        </template>
        <template v-slot:item.action="{ item }">
          <v-icon
            small
            class="mr-2"
            @click="openEditWorkTimeDrawer(item)">
            edit
          </v-icon>
          <v-icon small @click="deleteWorkTime(item)">
            delete
          </v-icon>
        </template>
        <template v-slot:no-data>
          {{ $t("exo.timeTracker.timeTrackingSettings.textIfNoWorckTime") }}
        </template>
      </v-data-table>
    </v-flex>
    <v-flex>
      <v-card outlined style="padding: 16px;">
        <v-form ref="form">
          <div>
            <v-label for="label">
              {{ $t("exo.timeTracker.timeTrackingSettings.labelUsersSpace") }}
            </v-label>
            <input
              ref="label"
              v-model="otherSettings.usersSpace"
              type="text"
              name="label"
              class="input-block-level ignore-vuetify-classes my-3">
          </div>
          <div>
            <v-label for="subActivityCode">
              {{ $t("exo.timeTracker.timeTrackingSettings.labelDefaultSubActivity") }}
            </v-label>
            <select
              v-model="otherSettings.defaultFeatureSubActivity"
              name="subActivityCode"
              class="input-block-level ignore-vuetify-classes my-3">
              <option
                v-for="item in subActivityCodes"
                :key="item.id"
                :value="item">
                {{ item.displayLabel }}
              </option>
            </select>
          </div>
          <div>
            <v-label for="weekEndHolidayActivity">
              {{ $t("exo.timeTracker.timeTrackingSettings.labelWeekEndHolidayActivity") }}
            </v-label>
            <select
              v-model="otherSettings.weekEndHolidayActivity.id"
              name="weekEndHolidayActivity"
              class="input-block-level ignore-vuetify-classes my-3">
              <option
                v-for="item in activities"
                :key="item.id"
                :value="item.id">
                {{ item.label }}
              </option>
            </select>
          </div>
          <v-card-actions>
            <div class="flex-grow-1"></div>
            <div class="uiAction">
              <button
                :disabled="!valid"
                class="btn btn-primary"
                type="button"
                @click="saveSettings()">
                {{ $t("exo.timeTracker.drawerButtonSave") }}
              </button>
            </div>
          </v-card-actions>
        </v-form>
      </v-card>
    </v-flex>
    <add-office-drawer ref="addOfficeDrawer" @save="addOffice" />
    <add-location-drawer ref="addLocationDrawer" @save="addLocation" />
    <add-work-time-drawer
      ref="addWorkTimeDrawer"
      :offices="offices"
      :teams="teams"
      @save="addWorkTime" />      
    <edit-office-drawer ref="editOfficeDrawer" @save="editOffice" />
    <edit-location-drawer ref="editLocationDrawer" @save="editLocation" />
    <edit-work-time-drawer
      ref="editWorkTimeDrawer"
      :offices="offices"
      :teams="teams"
      @save="editWorkTime" />
  </div>
</template>

<script>
import addOfficeDrawer from './AddOfficeDrawer.vue';
import addLocationDrawer from './AddLocationDrawer.vue';
import addWorkTimeDrawer from './AddWorkTimeDrawer.vue';
import editOfficeDrawer from './EditOfficeDrawer.vue';
import editLocationDrawer from './EditLocationDrawer.vue';
import editWorkTimeDrawer from './EditWorkTimeDrawer.vue'; 
export default {
  components: {
    addOfficeDrawer,
    addLocationDrawer,
    addWorkTimeDrawer,
    editOfficeDrawer,
    editLocationDrawer,
    editWorkTimeDrawer, 
  },
  props: {
    offices: {
      type: Array,
      default: () => null,
    },
    locations: {
      type: Array,
      default: () => null,
    },
    workTimePlans: {
      type: Array,
      default: () => null,
    },
    subActivityCodes: {
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
    },
  },
  data: () => ({
    valid: true,
    editedIndex: -1,
    editedItem: {
      code: '',
      label: ''
    },
    defaultItem: {
      code: '',
      label: ''
    },
    activities: [],         
  }),
  computed: {
    locationHeaders() {
      return [
        {
          text: 'code',
          align: 'center',
          sortable: true,
          value: 'code',
        },
        {
          text: 'label',
          align: 'center',
          sortable: true,
          value: 'label',
        },
        {
          text: 'Actions',
          align: 'center',
          sortable: true,
          value: 'action',
        },
      ];
    },
    officeHeaders() {
      return [            
        {
          text: 'code',
          align: 'center',
          sortable: true,
          value: 'code',
        },
        {
          text: 'label',
          align: 'center',
          sortable: true,
          value: 'label',
        },
        {
          text: 'Actions',
          align: 'center',
          sortable: true,
          value: 'action',
        },
      ];
    },
    workTimeHeaders() {
      return [
        {
          text: 'From',
          align: 'center',
          sortable: true,
          value: 'fromDate',
        },{
          text: 'To',
          align: 'center',
          sortable: true,
          value: 'toDate',
        },
        {
          text: 'Hours',
          align: 'center',
          sortable: true,
          value: 'time',
        },
        {
          text: 'Office',
          align: 'center',
          sortable: true,
          value: 'office.label',
        },
        {
          text: 'Employee',
          align: 'center',
          sortable: true,
          value: 'userId',
        },
        {
          text: 'Period',
          align: 'center',
          sortable: true,
          value: 'period',
        },
        {
          text: 'Default',
          align: 'center',
          sortable: true,
          value: 'defaultTime',
        },
        {
          text: 'Actions',
          align: 'center',
          sortable: true,
          value: 'action',
        },
      ];
    },
    defaultFeatureSubActivityId(){
      if (this.otherSettings.defaultFeatureSubActivity){
        return this.otherSettings.defaultFeatureSubActivity.id;
      } else {
        return '';
      }
    }
  },
  created() {
    this.getActivities();
  },
  methods: {
    getActivities() {
      fetch('/portal/rest/timetracker/activitymgn/activity', {
        credentials: 'include',
      })
        .then((resp) => resp.json())
        .then((resp) => {
          this.activities = resp;
        });
    },
    deleteOffice(item) {
      const index = this.offices.indexOf(item);
      this.offices.splice(index, 1);
      this.$emit('deleteOffice', item);
    },
    deleteLocation(item) {
      const index = this.locations.indexOf(item);
      this.locations.splice(index, 1);
      this.$emit('deleteLocation', item);
    },
    deleteWorkTime(item) {
      const index = this.workTimePlans.indexOf(item);
      this.workTimePlans.splice(index, 1);
      this.$emit('deleteWorkTime', item);
    },
    openAddLocationDrawer() {
      this.$refs.addLocationDrawer.open();
    },
    openAddOfficeDrawer() {
      this.$refs.addOfficeDrawer.open();
    },
    openAddWorkTimeDrawer() {
      this.$refs.addWorkTimeDrawer.open();
    },
    addOffice(office) {
      this.offices.push(office);
      this.$emit('addOffice', office);
    },      
    addLocation(location) {
      this.locations.push(location);
      this.$emit('addLocation', location);
    },
    addWorkTime(workTime) {
      this.workTimePlans.push(workTime);
      this.$emit('addWorkTime', workTime);
    },
    openEditOfficeDrawer(item) {
      this.$refs.editOfficeDrawer.open(item);
    },
    openEditLocationDrawer(item) {
      this.$refs.editLocationDrawer.open(item);
    },
    openEditWorkTimeDrawer(item) {
      this.$refs.editWorkTimeDrawer.open(item);
    },
    editOffice(office) {
      this.offices.push(office);
      this.$emit('editOffice', office);
    },
    editLocation(location) {
      this.locations.push(location);
      this.$emit('editLocation', location);
    },
    editWorkTime(workTime) {
      this.$emit('editWorkTime', workTime);
    },
    saveSettings() {
      if (this.otherSettings.weekEndHolidayActivity && this.otherSettings.weekEndHolidayActivity.id){
        this.otherSettings.weekEndHolidayActivity =this.activities.find(act => act.id === this.otherSettings.weekEndHolidayActivity.id);
      }
      this.$emit('saveOtherSettings', this.otherSettings);
    },
  }
};
</script>

<style>
#codesManagementApp {
    overflow: hidden;
    padding: 10px 20px;
}

select {
    width: auto;
}

#codesManagementApp .v-input input {
    margin-bottom: 0;
    border: 0;
    box-shadow: none;
}

#codesManagementApp .v-toolbar .v-input {
    margin-left: 18px;
}

#codesManagementApp .v-data-table {
    width: 100%;
}

.slider {
    position: absolute;
    cursor: pointer;
    overflow: hidden;
    top: 5px;
    left: 0;
    right: 0;
    bottom: 0;
    width: 60px;
    height: 20px;
    background-color: #f2f2f2;
    -webkit-transition: .4s;
    transition: .4s;
}
</style>
