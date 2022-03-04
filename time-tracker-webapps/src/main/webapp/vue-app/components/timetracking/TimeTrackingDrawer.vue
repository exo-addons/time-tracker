<template>
  <div data-app>
    <exo-drawer
      ref="timeTrackerDrawer"
      right
      class="logTimeDrawer">
      <div
        v-if="alert"
        :class="alert_type"
        class="alert">
        <i :class="alertIcon"></i> {{ message }}
      </div>
      <template slot="title">
        {{ $t("exo.timeTracker.timeTracking.timeTrackingDrawer.toolbarTitle") }}
      </template>
      <template slot="titleIcons">
        <v-btn
          text
          small
          @click="addActivityRecord()">
          <v-icon>mdi-plus</v-icon>
          {{ $t("exo.timeTracker.timeTracking.text.add.entry") }}
        </v-btn>
      </template>
      <template slot="content">
        <div align="center" justify="center">
          <v-menu
            v-model="activityRecordMenuDatePicker"
            :close-on-content-click="false"
            :nudge-right="40"
            transition="scale-transition"
            offset-y
            min-width="290px">
            <template v-slot:activator="{ on, attrs }">
              <v-text-field
                v-model="dateRangeText"
                centered
                prepend-icon="event"
                readonly
                v-bind="attrs"
                v-on="on" />
            </template>
            <v-date-picker
              v-model="date"
              :locale="localeLanguage"
              :first-day-of-week="1"
              @input="activityRecordMenuDatePicker = false" />
          </v-menu>
        </div>
        <div align="center" justify="center">
          <h4>
            {{ $t("exo.timeTracker.timeTracking.timeTrackingDrawer.text.totlal") }}
            {{ total }}
          </h4>
        </div>
        <div align="center" justify="center">
          <v-list v-if="activityRecords.length > 0" class="actList">
            <v-list-item
              v-for="item in activityRecords"
              :key="item.id"
              class="actItem">
              <v-list-item-action @click="editActivityRecord(item)">
                <v-list-item-action-text class="numberHr" v-text="item.time" />
              </v-list-item-action>
              <v-tooltip
                bottom 
                max-width="200px">
                <template v-slot:activator="{ on, attrs }">
                  <v-list-item-content
                    v-bind="attrs"
                    v-on="on"
                    @click="editActivityRecord(item)">
                    <v-list-item-title
                      v-if="item.activity"
                      class="text-truncate text-left"
                      v-text="item.activity.label" />
                    <span class=" TimeTrackingDrawerSpan d-inline-block text-truncate text-left">
                      <v-list-item-subtitle v-text="item.description" />
                    </span>
                  </v-list-item-content>
                </template>
                <span class="TimeTrackingDrawerSpan text-left text-justify text-break">{{ item.description }}</span>
              </v-tooltip>
              <v-icon small @click="deleteActivityRecord(item.id)">
                delete
              </v-icon>
            </v-list-item>
          </v-list>
        </div>
      </template>
      <template slot="footer"> </template>
    </exo-drawer>
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
    <template>
      <exo-confirm-dialog
        ref="deleteTTEntryDrawer"
        message="Are you sure you want to delete this line?"
        title="Confirmation"
        cancel-label="Cancel"
        ok-label="Yes"
        @ok="deleteActivity" />
    </template>
  </div>
</template>

<script>
import AddTrackingEntryDrawer from '../commons/AddTTEntryDrawer.vue';
import EditTrackingEntryDrawer from '../commons/EditTTEntryDrawer.vue';
export default {
  components: {
    AddTrackingEntryDrawer,
    EditTrackingEntryDrawer,
  },
  data: () => ({
    date: new Date().toISOString().substr(0, 10),
    activityRecordMenuDatePicker: false,
    localeLanguage: eXo.env.portal.language,
    dateRangeText: '',
    activities: [],
    offices: [],
    locations: [],
    projects: [],
    clients: [],
    activityRecords: [],
    activityRecord: {},
    alert: false,
    message: '',
    alert_type: '',
    alertIcon: '',
    total: 0,
    deleteId: 0
  }),
  watch: {
    'date' () {
      this.getActivityRecords();
      this.formatDate(this.date);
    }
  },
  mounted () {
    $(this.$refs.timeTrackerDrawer.$el).click(()=> { 
      if (this.activityRecordMenuDatePicker) {
        this.activityRecordMenuDatePicker = false;
      }
    });
  },
  methods: {
    getActivityRecords() {
      fetch(
        `/portal/rest/timetracker/activityRecordrecordsmgn/activityrecord/${
          this.date
        }`,
        {
          credentials: 'include'
        }
      )
        .then(resp => resp.json())
        .then(resp => {
          this.activityRecords = resp;
          this.total = this.activityRecords.reduce(
            (accum, item) => accum + item.time,
            0
          );
        });
      this.formatDate(this.date);
    },
    getActivities() {
      fetch('/portal/rest/timetracker/activitymgn/activity', {
        credentials: 'include'
      })
        .then(resp => resp.json())
        .then(resp => {
          this.activities = resp;
        });
    },
    getOffices() {
      fetch('/portal/rest/timetracker/settings/office', {
        credentials: 'include'
      })
        .then(resp => resp.json())
        .then(resp => {
          this.offices = resp;
        });
    },
    getLocations() {
      fetch('/portal/rest/timetracker/settings/location', {
        credentials: 'include'
      })
        .then(resp => resp.json())
        .then(resp => {
          this.locations = resp;
        });
    },
    getProjects() {
      fetch('/portal/rest/timetracker/projectsmgn/project', {
        credentials: 'include'
      })
        .then(resp => resp.json())
        .then(resp => {
          this.projects = resp.sort(this.compare);
        });
    },
    getClients() {
      fetch('/portal/rest/timetracker/clientsmgn/client', {
        credentials: 'include'
      })
        .then(resp => resp.json())
        .then(resp => {
          this.clients = resp.sort(this.compare);
        });
    },
    save(activityRecord) {
      activityRecord.activityDate = this.date;
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
          this.getActivityRecords();
          this.displaySusccessMessage('activity added');
        })
        .catch(result => {
          this.getActivityRecords();
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
          this.getActivityRecords();
          this.displaySusccessMessage('activity added');
        })
        .catch(result => {
          this.getActivityRecords();
          result.text().then(body => {
            this.displayErrorMessage(body);
          });
        });
    },
    deleteActivity() {
      fetch(
        `/portal/rest/timetracker/activityRecordrecordsmgn/activityrecord/${
          this.deleteId
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
          this.getActivityRecords();
          this.displaySusccessMessage('activity deleted');
        })
        .catch(result => {
          this.getActivityRecords();
          result.text().then(body => {
            this.displayErrorMessage(body);
          });
        });
    },
    cancel() {
      this.$refs.timeTrackerDrawer.close();
    },
    open() {
      this.date = new Date().toISOString().substr(0, 10);
      this.getOffices();
      this.getLocations();
      this.getActivityRecords();
      this.getActivities();
      this.getProjects();
      this.getClients();
      this.$refs.timeTrackerDrawer.open();
      this.formatDate(this.date);
    },
    addActivityRecord() {
      this.$refs.addTTEntryDrawer.open();
    },
    editActivityRecord(item) {
      this.activityRecord = item;
      this.$refs.editTTEntryDrawer.open(item);
    },
    deleteActivityRecord(id) {
      this.deleteId = id;
      this.$refs.deleteTTEntryDrawer.open();
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
    formatDate(val){
      const [year, month, day] = val.split('-');
      this.dateRangeText= `${year}/${month}/${day}`;
      if (this.localeLanguage === 'fr'){
        this.dateRangeText= `${day}/${month}/${year}`;
      }
    }
  }
};
</script>

<style scoped>
.actList {
  width: 100%;
}
.actItem {
  background-color: #c3e4f6;
  margin-bottom: 5px;
}
.numberHr {
  font-size: 30px !important;
  font-weight: 900;
  width: 46px !important;
  border-radius: 250px;
  color: #3f51b5 !important;
  text-align: center;
  background: #fff;
}
</style>
