<template>
  <div ref="ttTimeSheetAplication">
    <div
      v-if="alert"
      id
      :class="alert_type"
      class="alert">
      <i :class="alertIcon"></i> {{ message }}
    </div>
    <v-card elevation="0">
      <v-card-text>
        <v-layout>
          <v-data-table 
            ref="TTdataTable"
            :headers="headers"
            :items="activityRecordsList"
            elevation="0"
            :item-class="itemRowBackground"
            :loading="loading"
            loading-text="Loading... Please wait"
            disable-pagination
            hide-default-footer>
            <template v-slot:top>
              <v-toolbar
                color="white"
                flat
                id="TTAplicationToolbar">
                <template>
                  <button
                    class="btn btn-primary pull-left"
                    type="button"
                    @click="openAddTTEntryDrawer">
                    <i class="uiIconSocSimplePlus uiIconSocWhite"></i>
                    {{
                      $t(
                        'exo.timeTracker.timeSheet.timeSheet.buttonLabelAddActivity'
                      )
                    }}
                  </button>
                  <button
                    class="btn btn-export"
                    type="button"
                    @click="exportToExcel(json_fields, 'all')">
                    <i class="uiIconExport"></i>
                    {{
                      $t(
                        'exo.timeTracker.timeSheet.timeSheet.buttonLabelExportAll'
                      )
                    }}
                  </button>
                  <button
                    class="btn btn-export"
                    type="button"
                    @click="exportToExcel(json_fields_fr, 'fr')">
                    <i class="uiIconExport"></i>
                    {{
                      $t(
                        'exo.timeTracker.timeSheet.timeSheet.buttonLabelExportFR'
                      )
                    }}
                  </button>
                  <v-spacer />
                  <v-menu
                    ref="menu"
                    v-model="menu"
                    :close-on-content-click="false"
                    :return-value.sync="date"
                    transition="scale-transition"
                    offset-y
                    attach="#TTAplicationToolbar"
                    min-width="290px">
                    <template
                      v-slot:activator="{ on }">
                      <v-text-field
                        v-model="dateRangeText"
                        prepend-icon="event"
                        class="tt-datepicker-textfeild-cursor"
                        readonly
                        v-on="on" />
                    </template>
                    <v-hover v-slot="{ hover }" open-delay="200">
                      <v-date-picker
                        ref="picker"
                        v-if="isHover(hover)"
                        v-model="date"
                        range
                        no-title
                        :locale="localeLanguage"
                        :first-day-of-week="1"
                        scrollable>
                        <v-spacer />
                        <v-btn
                          text
                          color="primary"
                          @click="menu = false">
                          {{ $t('exo.timeTracker.drawerButtonCancel') }}
                        </v-btn>
                        <v-btn
                          text
                          color="primary"
                          @click="$refs.menu.save(date), setDates()">
                          {{ $t('exo.timeTracker.popupButtonOk') }}
                        </v-btn>
                      </v-date-picker>
                    </v-hover>
                  </v-menu>
                  <a
                    class="caption primary--text drawersBtn"
                    @click="toggleFilterDrawer">
                    <v-icon color="primary" left>mdi-tune</v-icon> Filter
                  </a>
                </template>
              </v-toolbar>
            </template>
            <template v-slot:item.time="{ item }">
              {{ roundVlaue(item) }}
            </template>
            <template v-slot:item.action="{ item }">
              <v-menu
                :id="item.__ob__.dep.id"
                close-on-click
                close-on-content-click
                bottom
                nudge-bottom="36"
                :ref="item.__ob__.dep.id"
                attach
                left>
                <template v-slot:activator="{ on, attrs }">
                  <v-btn
                    icon
                    v-bind="attrs"
                    v-on="on"
                    @blur="menuBlurred(item.__ob__.dep.id)"
                    @click="onclickitem()">
                    <v-icon>mdi-dots-vertical</v-icon>
                  </v-btn>
                </template>
                <v-list class="text-left" ref="TTlistRef">
                  <v-list-item 
                    @click="openAddTTEntryDrawer(item)">
                    <v-list-item-title class="subtitle-2">
                      <i class="uiIcon uiIconAdd"></i>
                      {{ $t("exo.timeTracker.label.add") }}
                    </v-list-item-title>
                  </v-list-item>
                  <v-list-item 
                    v-if="item.id"
                    @click="editActivityRecord(item)">
                    <v-list-item-title class="subtitle-2">
                      <i class="uiIcon uiIconEdit"></i>
                      {{ $t("exo.timeTracker.teams.teamsList.edit") }}
                    </v-list-item-title>
                  </v-list-item>
                  <v-list-item 
                    v-if="item.id"
                    :key="item.id"
                    @click="openConfirmDialogDeleteTeam(item)">
                    <v-list-item-title class="subtitle-2">
                      <i class="uiIcon uiIconTrash"></i>
                      {{ $t("exo.timeTracker.teams.teamsList.delete") }}
                    </v-list-item-title>
                  </v-list-item>
                  <v-list-item 
                    v-if="item.id"
                    :key="item.id"
                    @click="openAddTTEntryDrawer(item,true)">
                    <v-list-item-title class="subtitle-2">
                      <i class="uiIcon uiIconCloneNode"></i>
                      {{ $t("exo.timeTracker.label.duplicate") }}
                    </v-list-item-title>
                  </v-list-item>
                </v-list>
              </v-menu>
            </template>
            <template v-slot:no-data>
              {{ $t('exo.timeTracker.timeSheet.timeSheet.textIfNoActivities') }}
            </template>
          </v-data-table>
        </v-layout>
      </v-card-text>
    </v-card>
    <template>
      <exo-confirm-dialog
        ref="deleteItemTeamsList"
        message="Are you sure you want to delete this line?"
        title="Confirmation"
        cancel-label="Cancel"
        ok-label="Yes"
        @ok="deleteItem()" />
    </template>
    <add-tracking-entry-drawer
      ref="addTTEntryDrawer"
      :activities="activities"
      :offices="offices"
      :locations="locations"
      :clients="clients"
      :projects="projects"
      @save="save" />
    <edit-tracking-entry-drawer
      ref="editTTEntryDrawer"
      :activities="activities"
      :offices="offices"
      :locations="locations"
      :clients="clients"
      :projects="projects"
      :activity-record="activityRecord"
      @save="update" />
    <filter-drawer
      ref="filterDrawer"
      :activities="activities"
      :offices="offices"
      :locations="locations"
      :types="types"
      :projects="projects"
      :sub-types="subTypes"
      :activity-codes="activityCodes"
      :sub-activity-codes="subActivityCodes"
      :clients="clients"
      :features="features"
      :employees="employees"
      :teams="teams"
      :filters="filters"
      @addFilter="addFilter"
      @saveFilter="saveFilter"
      @deleteFilter="deleteFilter" />
  </div>
</template>

<script>
import filterDrawer from './FilterDrawer.vue';
import XLSX from 'xlsx';
import AddTrackingEntryDrawer from '../commons/AddTTEntryDrawer.vue';
import EditTrackingEntryDrawer from '../commons/EditTTEntryDrawer.vue';
export default {
  components: {
    filterDrawer,
    AddTrackingEntryDrawer,
    EditTrackingEntryDrawer
  },
  data: () => ({
    json_fields_fr: {
      Date: 'frDate',
      Month: 'month',
      'Week Day': 'weekDay',
      Time: 'time',
      'TS Code': 'tsCode',
      'Sales Order': 'salesOrderName',
      Location: 'location',
      Comment: 'comment',
      'Référence Jour Férié et Week end': 'comment',
      'jours ouvrés': 'comment',
      'jours ouvrables': 'comment',
      'User Name': 'userFullName'
    },
    json_fields: {
      'User Name': 'userFullName',
      Date: 'activityDate',
      Month: 'month',
      Day: 'day',
      Year: 'year',
      'Week Day': 'weekDay',
      'TS Code': 'tsCode',
      'Activity label': 'activityName',
      description: 'description',
      Time: 'time',
      Location: 'location',
      Type: 'typeName',
      'Sub Type': 'subTypeName',
      'Activity Code': 'activityCodeName',
      'Sub Activity Code': 'subActivityCodeName',
      Project: 'projectName',
      Client: 'clientName',
      Feature: 'featureName',
      'Sales Order': 'salesOrderName',
      'Project Version': 'projectVersion'
    },
    deleteItemTeams: {},
    idMenuItemRef: '',
    date: [],
    dateRangeText: '',
    menu: false,
    isHovered: false,
    totalRecords: 0,
    loading: true,
    options: {},
    activityRecord: {},
    activity: '',
    type: '',
    subType: '',
    activityCode: '',
    subActivityCode: '',
    client: '',
    project: '',
    feature: '',
    fromDate: '',
    toDate: '',
    location: '',
    office: '',
    valid: true,
    search: '',
    employee: '',
    team: '',
    awaitingSearch: false,
    dialog: false,
    itemToDelete: 0,
    alert: false,
    message: '',
    alert_type: '',
    alertIcon: '',
    alert_edit: false,
    message_edit: '',
    alert_type_add: '',
    activityRecordsList: [],
    editedIndex: -1,
    toExport: false,
    activities: [],
    types: [],
    subTypes: [],
    activityCodes: [],
    subActivityCodes: [],
    projects: [],
    clients: [],
    features: [],
    filters: {},
    employees: [],
    locations: [],
    offices: [],
    teams: [],
    existingActicitiesUser: '',
    localeLanguage: eXo.env.portal.language,
    day: 0,
    month: 0,
  }),
  computed: {
    headers() {
      return [
        {
          text: 'Date',
          align: 'center',
          sortable: true,
          value: 'activityDate'
        },
        {
          text: 'Description',
          align: 'center',
          sortable: true,
          value: 'description'
        },
        {
          text: 'Location',
          align: 'center',
          sortable: true,
          value: 'location'
        },
        {
          text: 'Time',
          align: 'center',
          sortable: true,
          value: 'time'
        },
        {
          text: 'Office',
          align: 'center',
          sortable: true,
          value: 'office'
        },
        {
          text: 'Activity',
          align: 'center',
          sortable: true,
          value: 'activity.label'
        },
        {
          text: 'SO',
          align: 'center',
          sortable: true,
          value: 'salesOrder.name'
        },
        {
          text: 'Type',
          align: 'center',
          sortable: true,
          value: 'activity.type.label'
        },
        {
          text: 'Sub Type',
          align: 'center',
          sortable: true,
          value: 'activity.subType.label'
        },
        {
          text: 'Client',
          align: 'center',
          sortable: true,
          value: 'activity.project.client.label'
        },
        {
          text: 'Project',
          align: 'center',
          sortable: true,
          value: 'activity.project.label'
        },
        {
          text: 'Activity Code',
          align: 'center',
          sortable: true,
          value: 'activity.activityCode.label'
        },
        {
          text: 'Sub Activity Code',
          align: 'center',
          sortable: true,
          value: 'activity.subActivityCode.label'
        },
        {
          text: 'User',
          align: 'center',
          sortable: true,
          value: 'userFullName'
        },
        {
          text: 'Actions',
          align: 'center',
          sortable: true,
          value: 'action'
        }
      ];
    },
  },
  watch: {
    dialog(val) {
      return val === true || this.close() === true;
    },
    date() {
      this.initDate();
    },
    search: function() {
      if (!this.awaitingSearch) {
        setTimeout(() => {
          this.getActivityRecords().then(data => {
            this.activityRecordsList = data.items;
            this.totalRecords = data.total;
          });
          this.awaitingSearch = false;
        }, 1000); // 1 sec delay
      }
      this.awaitingSearch = true;
    }
  },
  created() {
    this.initialize();
  },
  mounted() {
    if (this.$refs && 
    this.$refs.ttTimeSheetAplication &&
    this.$refs.ttTimeSheetAplication.__vue__ &&
    this.$refs.ttTimeSheetAplication.__vue__.$el) {
      $(this.$refs.ttTimeSheetAplication.__vue__.$el).click(() => {
        if (this.$refs && this.$refs.menu &&
        this.$refs.menu.isActive &&
        !this.isHovered) {
          this.$refs.menu.isActive = false;
        }
      });
    }
  },
  methods: {
    generateId(){
      return Date.now().toString(36) + Math.random().toString(36).substr(2);
    },
    menuBlurred(ref){
      if (this.$refs && this.$refs[this.idMenuItemRef] && this.$refs[this.idMenuItemRef].isActive){
        setTimeout(() => {
          this.$refs[this.idMenuItemRef].isActive= false;
          if (this.$refs && this.$refs[this.itemId] && this.$refs[this.itemId].isActive){
            this.$refs[this.itemId].isActive= false;
          }
        },300);
      }
      setTimeout(() => {
        this.itemId=this.idMenuItemRef;
        this.idMenuItemRef=ref;
      },500);
    },
    isHover(hover) {
      this.isHovered= hover;
      return true;
    },
    onclickitem(){
      if (this.$refs && this.$refs[this.idMenuItemRef] && this.$refs[this.idMenuItemRef].isActive){
        this.$refs[this.idMenuItemRef].isActive= false;
      }
    },
    addFilter(val) {
      this.activity = val.activity;
      this.type = val.type;
      this.subType = val.subType;
      this.activityCode = val.activityCode;
      this.subActivityCode = val.subActivityCode;
      this.client = val.client;
      this.project = val.project;
      this.feature = val.feature;
      this.location = val.location;
      this.office = val.office;
      this.employee = val.employee;
      this.team = val.team;
      this.getActivityRecords().then(data => {
        this.activityRecordsList = data.items;
        this.totalRecords = data.total;
      });
    },
    itemRowBackground (item) {
      if (item.activityTime.day === 6 || item.activityTime.day === 0) {
        return 'timeSheetDay-weekend';
      } else if (!item.activity) {
        return 'timeSheetDay-not-valid';
      } else if (item.location ==='eXo FR' && item.office ==='FR' &&
                item.dailyTimeSum !== 7 &&  item.activityTime.day === 5){
        return 'timeSheetDay-to-be-fixed';
      } else if ((!item.location || !item.office || item.dailyTimeSum !== 8) && 
      (item.activityTime.day !== 5 || item.location !=='eXo FR' || item.office !=='FR')) {
        return 'timeSheetDay-to-be-fixed';
      }
    },
    compare(a, b) {
      const userNameA = a.fullName.toUpperCase();
      const userNameB = b.fullName.toUpperCase();
      let comparison = 0;
      if (userNameA > userNameB) {
        comparison = 1;
      } else if (userNameA < userNameB) {
        comparison = -1;
      }
      return comparison;
    },
    toggleFilterDrawer() {
      this.$refs.filterDrawer.open();
    },
    openAddTTEntryDrawer(item,isDuplicate) {
      if (this.existingActicitiesUser !== item.userName) {
        this.getActivities(item.userName);
      }
      if (isDuplicate) {
        this.$refs.addTTEntryDrawer.open(item, true,true);
      } else {
        this.$refs.addTTEntryDrawer.open(item, true);
      }
      
    },
    initialize() {
      const date = new Date();
      const firstDay = new Date(date.getFullYear(), date.getMonth(), 2);
      const lastDay = new Date(date.getFullYear(), date.getMonth() + 1, 1);
      this.date = [
        firstDay.toISOString().substr(0, 10),
        lastDay.toISOString().substr(0, 10)
      ];
      this.initDate();
      this.getEmployees();
      this.getClients();
      this.getProjects();
      this.getActivityCodes();
      this.getSubActivityCodes();
      this.getTypes();
      this.getSubTypes();
      this.getActivities();
      this.getFilters();
      this.getOffices();
      this.getLocations();
      this.getActivityRecords().then(data => {
        this.activityRecordsList = data.items;
        this.totalRecords = data.total;
      });
    },
    initDate(){
      if ((this.fromDate !== this.date[0] || this.fromDate !== this.date[1]) &&
      (this.toDate !== this.date[0] || this.toDate !== this.date[1])){
        this.fromDate = this.date[0];
        this.toDate = this.date[0];
        let fromDateFormated = this.formatDate(this.fromDate);
        this.dateRangeText= fromDateFormated ;
        if (typeof this.date[1] !== 'undefined' && this.fromDate !== this.date[1]) {
          this.toDate = this.date[1];
          const fromDateInDay = this.day + this.month*30;
          let toDateFormated = this.formatDate(this.toDate);
          const toDateInDay = this.day + this.month*30;
          if (fromDateInDay > toDateInDay){
            let switchDate = this.fromDate;
            this.fromDate = this.toDate;
            this.toDate = switchDate;
            switchDate = toDateFormated;
            toDateFormated = fromDateFormated;
            fromDateFormated = switchDate;
            this.date.reverse();
          }
          this.dateRangeText = `${fromDateFormated} ~ ${toDateFormated}`;
        }        
      }
    },
    openConfirmDialogDeleteTeam(item) {
      this.deleteItemTeams = item;
      this.$refs.deleteItemTeamsList.open();
    },
    close() {
      this.dialog = false;
      this.editedItem = this.defaultItem;
      setTimeout(() => {
        this.editedIndex = -1;
        this.initialize();
      }, 300);
    },
    setDates() {
      this.getActivityRecords().then(data => {
        this.activityRecordsList = data.items;
        this.totalRecords = data.total;
      });
    },
    displaySusccessMessage(message) {
      this.message = message;
      this.alert_type = 'alert-success';
      this.alertIcon = 'uiIconSuccess';
      this.alert = true;
      setTimeout(() => (this.alert = false), 5000);
      this.editedItem = this.defaultItem;
    },
    displayErrorMessage(message) {
      this.isUpdating = false;
      this.message = message;
      this.alert_type = 'alert-error';
      this.alertIcon = 'uiIconError';
      this.alert = true;
      setTimeout(() => (this.alert = false), 5000);
    },
    getActivityRecords(toExport, dest) {
      this.loading = true;
      return new Promise(resolve => {
        const sort = '';
        const desc = false;
        const offset = 0;
        const limit_ = 0;
        fetch(
          `/portal/rest/timetracker/activityRecordrecordsmgn/activityrecord/list?search=${
            this.search
          }&userName=${this.employee}&activity=${this.activity}&type=${
            this.type
          }&subType=${this.subType}&activityCode=${
            this.activityCode
          }&subActivityCode=${this.subActivityCode}&client=${
            this.client
          }&project=${this.project}&feature=${this.feature}&fromDate=${
            this.fromDate
          }&toDate=${this.toDate}&location=${this.location}&office=${
            this.office
          }&team=${
            this.team
          }&sortby=${sort}&sortdesc=${desc}&page=${offset}&limit=${limit_}&export=${toExport}&exportType=${dest}`,
          {
            credentials: 'include'
          }
        )
          .then(resp => resp.json())
          .then(resp => {
            const items = resp;
            this.loading = false;
            resolve({
              items
            });
          });
      });
    },
    getTeams() {
      fetch('/portal/rest/timetracker/teamsmgn/team/all', {
        credentials: 'include'
      })
        .then(resp => resp.json())
        .then(resp => {
          this.teams = resp;
        });
    },
    getActivities() {
      let user = '';
      if (this.employees.length > 0) {
        user = 'all';
      }
      fetch(`/portal/rest/timetracker/activitymgn/activity?userName=${user}`, {
        credentials: 'include'
      })
        .then(resp => resp.json())
        .then(resp => {
          this.activities = resp;
          this.existingActicitiesUser = this.employee;
        });
    },
    roundVlaue(item) {
      return Math.round(item.time * 10) / 10;
    },
    getProjects() {
      fetch('/portal/rest/timetracker/projectsmgn/project', {
        credentials: 'include'
      })
        .then(resp => resp.json())
        .then(resp => {
          this.projects = resp;
        });
    },
    getEmployees() {
      fetch('/portal/rest/timetracker/teamsmgn/employees', {
        credentials: 'include'
      })
        .then(resp => resp.json())
        .then(resp => {
          this.employees = resp.sort(this.compare);
          if (this.employees.length > 0) {
            this.getTeams();
          }
          this.employees.unshift({ userName: 'all', fullName: 'All' });
        });
    },
    getClients() {
      fetch('/portal/rest/timetracker/clientsmgn/client', {
        credentials: 'include'
      })
        .then(resp => resp.json())
        .then(resp => {
          this.clients = resp;
        });
    },
    getFeatures() {
      fetch('/portal/rest/timetracker/featuresmgn/feature', {
        credentials: 'include'
      })
        .then(resp => resp.json())
        .then(resp => {
          this.features = resp;
        });
    },
    getActivityCodes() {
      fetch('/portal/rest/timetracker/codesmgn/activityCode', {
        credentials: 'include'
      })
        .then(resp => resp.json())
        .then(resp => {
          this.activityCodes = resp;
        });
    },
    getSubActivityCodes() {
      fetch('/portal/rest/timetracker/codesmgn/subActivityCode', {
        credentials: 'include'
      })
        .then(resp => resp.json())
        .then(resp => {
          this.subActivityCodes = resp;
        });
    },
    getTypes() {
      fetch('/portal/rest/timetracker/codesmgn/type', {
        credentials: 'include'
      })
        .then(resp => resp.json())
        .then(resp => {
          this.types = resp;
        });
    },
    getSubTypes() {
      fetch('/portal/rest/timetracker/codesmgn/subType', {
        credentials: 'include'
      })
        .then(resp => resp.json())
        .then(resp => {
          this.subTypes = resp;
        });
    },
    getFilters() {
      fetch('/portal/rest/timetracker/filtersmgn/filter', {
        credentials: 'include'
      })
        .then(resp => resp.json())
        .then(resp => {
          this.filters = resp;
        });
    },
    saveFilter(val) {
      this.activity = val.activity;
      this.type = val.type;
      this.subType = val.subType;
      this.activityCode = val.activityCode;
      this.subActivityCode = val.subActivityCode;
      this.client = val.client;
      this.project = val.project;
      this.feature = val.feature;
      this.location = val.location;
      this.office = val.office;
      this.empolyee = val.empolyee;
      this.team = val.team;
      const fields = [];
      fields.push({
        name: 'activity',
        value: val.activity
      });
      fields.push({
        name: 'type',
        value: val.type
      });
      fields.push({
        name: 'subType',
        value: val.subType
      });
      fields.push({
        name: 'activityCode',
        value: val.activityCode
      });
      fields.push({
        name: 'subActivityCode',
        value: val.subActivityCode
      });
      fields.push({
        name: 'client',
        value: val.client
      });
      fields.push({
        name: 'project',
        value: val.project
      });
      fields.push({
        name: 'feature',
        value: val.feature
      });
      fields.push({
        name: 'location',
        value: val.location
      });
      fields.push({
        name: 'office',
        value: val.office
      });
      fields.push({
        name: 'team',
        value: val.team
      });
      fields.push({
        name: 'employee',
        value: val.employee
      });
      const filter = {
        filter: {
          name: val.name
        },
        filterFields: fields
      };
      fetch('/portal/rest/timetracker/filtersmgn/filter', {
        headers: {
          'Content-Type': 'application/json'
        },
        credentials: 'include',
        method: 'post',
        body: JSON.stringify(filter)
      })
        .then(result => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.getFilters();
          this.displaySusccessMessage('filter added');
        })
        .catch(result => {
          this.getActivityRecords();
          result.text().then(body => {
            this.displayErrorMessage(body);
          });
        });
    },
    openTimeTrackingDrawer() {
      this.$refs.timeTrackingDrawer.open();
    },
    save(activityRecord) {
      fetch(
        '/portal/rest/timetracker/activityRecordrecordsmgn/activityrecord',
        {
          method: 'post',
          credentials: 'include',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(activityRecord)
        }
      )
        .then(result => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.getActivityRecords().then(data => {
            this.activityRecordsList = data.items;
          });
          this.displaySusccessMessage('activity added');
        })
        .catch(result => {
          this.getActivityRecords().then(data => {
            this.activityRecordsList = data.items;
          });
          result.text().then(body => {
            this.displayErrorMessage(body);
          });
        });
    },
    update(activityRecord) {
      fetch(
        '/portal/rest/timetracker/activityRecordrecordsmgn/activityrecord',
        {
          method: 'put',
          credentials: 'include',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(activityRecord)
        }
      )
        .then(result => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.getActivityRecords().then(data => {
            this.activityRecordsList = data.items;
          });
          this.displaySusccessMessage('activity added');
        })
        .catch(result => {
          this.getActivityRecords().then(data => {
            this.activityRecordsList = data.items;
          });
          result.text().then(body => {
            this.displayErrorMessage(body);
          });
        });
    },
    deleteFilter(item) {
      fetch(`/portal/rest/timetracker/filtersmgn/filter/${item.filter.id}`, {
        method: 'delete',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json'
        }
      })
        .then(result => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.confirmDialog = false;
          this.this.getFilters();
          this.displaySusccessMessage('filter deleted');
        })
        .catch(result => {
          this.confirmDialog = false;
          this.getFilters();
          result.text().then(body => {
            this.displayErrorMessage(body);
          });
        });
    },
    deleteItem() {
      fetch(
        `/portal/rest/timetracker/activityRecordrecordsmgn/activityrecord/${
          this.deleteItemTeams.id
        }`,
        {
          method: 'delete',
          credentials: 'include',
          headers: {
            'Content-Type': 'application/json'
          }
        }
      )
        .then(result => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.confirmDialog = false;
          this.getActivityRecords().then(data => {
            this.activityRecordsList = data.items;
          });
          this.displaySusccessMessage('client deleted');
        })
        .catch(result => {
          this.confirmDialog = false;
          this.getActivityRecords().then(data => {
            this.activityRecordsList = data.items;
          });
          result.text().then(body => {
            this.displayErrorMessage(body);
          });
        });
    },
    cancel() {
      this.$refs.timeTrackerDrawer.close();
    },
    open() {
      this.$refs.timeTrackerDrawer.open();
    },
    addActivityRecord() {
      this.$refs.addTTEntryDrawer.open();
    },
    editActivityRecord(item) {
      if (this.existingActicitiesUser !== item.userName) {
        this.getActivities(item.userName);
      }
      this.activityRecord = item;
      this.$refs.editTTEntryDrawer.open(item);
    },
    getOffices() {
      fetch('/portal/rest/timetracker/settings/office', {
        credentials: 'include'
      })
        .then(resp => resp.json())
        .then(resp => {
          this.offices = resp;
          this.offices.unshift({ code: '', label: '' });
        });
    },
    getLocations() {
      fetch('/portal/rest/timetracker/settings/location', {
        credentials: 'include'
      })
        .then(resp => resp.json())
        .then(resp => {
          this.locations = resp;
          this.locations.unshift({ code: '', label: '' });
        });
    },
    exportToExcel(fileds, office) {
      // On Click Excel download button
      const days = [
        'Sunday',
        'Monday',
        'Tuesday',
        'Wednesday',
        'Thursday',
        'Friday',
        'Saturday'
      ];
      this.getActivityRecords(true, office).then(data => {
        const items = data.items;
        items.forEach(function(item) {
          const date_ = new Date(item.activityTime.time);
          item.day = days[date_.getDay()];
          item.weekDay = date_.getDay();
          if (date_.getDay() === 0) {
            item.weekDay = 7;
          }
          item.month = date_.getMonth() + 1;
          item.year = date_.getFullYear();
          let dd = date_.getDate();
          let mm = date_.getMonth() + 1;

          if (dd < 10) {
            dd = `0${dd}`;
          }
          if (mm < 10) {
            mm = `0${mm}`;
          }
          item.frDate = `${dd}/${mm}/${date_.getFullYear()}`;
          if (item.activity) {
            item.activityName = item.activity.label;
            if (item.activity.type) {
              item.typeName = item.activity.type.label;
            }
            if (item.activity.subType) {
              item.activityCodeName = item.activity.subType.label;
            }
            if (item.activity.activityCode) {
              item.subTypeName = item.activity.activityCode.label;
            }
            if (item.activity.subActivityCode) {
              item.subActivityCodeName = item.activity.subActivityCode.label;
            }
            if (item.activity.project) {
              item.projectName = item.activity.project.label;
            }
            if (item.activity.project && item.activity.project.client) {
              item.clientName = item.activity.project.client.label;
            }
            if (item.activity.feature) {
              item.featureName = item.activity.feature.label;
            }
          }
          if (item.salesOrder) {
            item.salesOrderName = item.salesOrder.name;
          }
        });
        const newItems = [];
        items.forEach(function(item) {
          const obj = {};
          for (const [key, value] of Object.entries(fileds)) {
            obj[key] = item[value];
          }
          newItems.push(obj);
        });
        const records = XLSX.utils.json_to_sheet(newItems);
        let userName = this.employee;
        if (!userName) {
          userName = eXo.env.portal.userName;
        }
        const wb = XLSX.utils.book_new();
        XLSX.utils.book_append_sheet(wb, records, userName);
        XLSX.writeFile(wb, `TimeSheet_${userName}_${this.dateRangeText}.xlsx`);
      });
    },
    formatDate (date) {
      const [year, month, day] = date.split('-');
      this.day = parseInt(day);
      this.month = parseInt(month);
      if (this.localeLanguage === 'fr'){
        return `${day}/${month}/${year}`;
      } else {
        return `${year}/${month}/${day}`;
      }
    },
  }
};
</script>

<style>
.ConfWarning {
  border: 1px solid white;
  border-radius: 5px;
  padding: 8px;
  text-align: center;
  text-transform: uppercase;
  font-size: 16px;
}

#LeftNavigation {
  z-index: 1100;
}

.VuetifyApp .v-text-field input {
  padding: 0 !important;
}

.addBtn {
  align-items: center;
  color: white;
  display: flex;
  flex: 1 0 auto;
  justify-content: inherit;
  line-height: normal;
  position: relative;
}

.v-data-table__wrapper {
  padding-top: 30px;
}

.v-data-table-header {
  border-bottom: solid #d0d0d0;
}

.btn-export {
  border-style: solid !important;
  margin-left: 10px;
}

.tt-datepicker-textfeild-cursor input {
  cursor: pointer;
}
</style>
