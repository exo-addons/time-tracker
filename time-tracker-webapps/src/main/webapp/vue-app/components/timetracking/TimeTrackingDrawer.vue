<template>
  <div>
    <exo-drawer
      ref="timeTrackerDrawer"
      right
      class="logTimeDrawer">
      <div
        v-if="alert"
        id
        :class="alert_type"
        class="alert">
        <i :class="alertIcon"></i> {{ message }}
      </div>
      <template slot="title">
        Log Time
      </template>
      <template slot="titleIcons">
        <v-btn
          text
          small
          @click="addActivityRecord()">
          <v-icon>mdi-plus</v-icon>
          Add entry
        </v-btn>
      </template>
      <template slot="content">
        <div align="center" justify="center">
          <v-menu
            :nudge-right="40"
            transition="scale-transition"
            offset-y
            min-width="290px">
            <template v-slot:activator="{ on, attrs }">
              <v-text-field
                v-model="date"
                centered
                prepend-icon="event"
                readonly
                v-bind="attrs"
                v-on="on" />
            </template>
            <v-date-picker v-model="date" />
          </v-menu>
        </div>

        <div align="center" justify="center">
          <h4>Total number of hours: {{ total }}</h4>
        </div>
        <div>
          <v-list v-if="activityRecords.length > 0" class="actList">
            <v-list-item
              v-for="item in activityRecords"
              :key="item.id"
              class="actItem">
              <v-row @click="editActivityRecord(item)">
                <v-col cols="2">
                  <v-list-item-action>
                    <v-list-item-action-text
                      class="numberHr"
                      v-text="item.time" />
                  </v-list-item-action>
                </v-col>
                <v-col cols="10" class="d-flex align-end">
                  <v-list-item-content>
                    <v-list-item-title
                      v-if="item.activity"
                      v-text="item.activity.label" />
                    <v-list-item-subtitle v-text="item.description" />
                  </v-list-item-content>
                </v-col>
              </v-row>
              <v-icon small @click="deleteActivityRecord(item)"> delete </v-icon>
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
  </div>
</template>

<script>
import AddTrackingEntryDrawer from "../commons/AddTTEntryDrawer.vue";
import EditTrackingEntryDrawer from "../commons/EditTTEntryDrawer.vue";
export default {
  components: {
    AddTrackingEntryDrawer,
    EditTrackingEntryDrawer
  },
  data: () => ({
    date: new Date().toISOString().substr(0, 10),
    menu2: false,
    activities: [],
    offices: [],
    locations: [],
    projects: [],
    clients: [],
    activityRecords: [],
    activityRecord: {},
    alert: false,
    message: "",
    alert_type: "",
    alertIcon: "",
    total: 0
  }),

  watch: {
    date: function() {
      this.getActivityRecords();
    }
    // menu2: function() {
    //   this.outsideMenu2();
    // }
  },
  created() {
    console.log(this.date);
    $(document).ready(function() {
      $(document).mousedown(function() {
        console.log("menue:", this.menu2);
        this.menu2 = false;
        console.log("menue:", this.menu2);
      });
    });
  },
  methods: {
    getActivityRecords() {
      fetch(
        `/portal/rest/timetracker/activityRecordrecordsmgn/activityrecord/${
          this.date
        }`,
        {
          credentials: "include"
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
    },

    getActivities() {
      fetch(`/portal/rest/timetracker/activitymgn/activity`, {
        credentials: "include"
      })
        .then(resp => resp.json())
        .then(resp => {
          this.activities = resp;
        });
    },

    getOffices() {
      fetch(`/portal/rest/timetracker/settings/office`, {
        credentials: "include"
      })
        .then(resp => resp.json())
        .then(resp => {
          this.offices = resp;
        });
    },

    getLocations() {
      fetch(`/portal/rest/timetracker/settings/location`, {
        credentials: "include"
      })
        .then(resp => resp.json())
        .then(resp => {
          this.locations = resp;
        });
    },

    getProjects() {
      fetch(`/portal/rest/timetracker/projectsmgn/project`, {
        credentials: "include"
      })
        .then(resp => resp.json())
        .then(resp => {
          this.projects = resp.sort(this.compare);
        });
    },

    getClients() {
      fetch(`/portal/rest/timetracker/clientsmgn/client`, {
        credentials: "include"
      })
        .then(resp => resp.json())
        .then(resp => {
          this.clients = resp.sort(this.compare);
        });
    },

    save(activityRecord) {
      //  this.activityRecords.push(activity)
      activityRecord.activityDate = this.date;
      fetch(
        `/portal/rest/timetracker/activityRecordrecordsmgn/activityrecord`,
        {
          method: "post",
          credentials: "include",
          headers: {
            "Content-Type": "application/json"
          },
          body: JSON.stringify(activityRecord)
        }
      )
        .then(result => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(response => {
          this.getActivityRecords();
          this.displaySusccessMessage("activity added");
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
        `/portal/rest/timetracker/activityRecordrecordsmgn/activityrecord`,
        {
          method: "put",
          credentials: "include",
          headers: {
            "Content-Type": "application/json"
          },
          body: JSON.stringify(activityRecord)
        }
      )
        .then(result => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(response => {
          this.getActivityRecords();
          this.displaySusccessMessage("activity added");
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
      this.menu2 = false;
    },
    open() {
      this.getOffices();
      this.getLocations();
      this.getActivityRecords();

      this.getActivities();
      this.getProjects();
      this.getClients();
      this.$refs.timeTrackerDrawer.open();
    },
    addActivityRecord() {
      this.$refs.addTTEntryDrawer.open();
    },
    editActivityRecord(item) {
      this.activityRecord = item;
      this.$refs.editTTEntryDrawer.open(item);
    },
    deleteActivityRecord(item){
      fetch(
        `/portal/rest/timetracker/activityRecordrecordsmgn/activityrecord/${
          item.id
        }`,
        {
          method: "delete",
          credentials: "include",
          headers: {
            "Content-Type": "application/json"
          }
        }
      )
        .then(result => {
          if (!result.ok) {
            throw result;
          }
        })
        .catch(result => {
          this.getActivityRecords().then(data => {
            this.activityRecords = data.items;
          });
        });
    },
    displaySusccessMessage(message) {
      this.message = message;
      this.alert_type = "alert-success";
      this.alertIcon = "uiIconSuccess";
      this.alert = true;
      setTimeout(() => (this.alert = false), 5000);
      this.editedItem = this.defaultItem;
    },
    displayErrorMessage(message) {
      this.isUpdating = false;

      this.message = message;
      this.alert_type = "alert-error";
      this.alertIcon = "uiIconError";
      this.alert = true;
      setTimeout(() => (this.alert = false), 5000);
    },
    outsideMenu2() {
      if (this.menu2) {
        console.log(this.date);
        $(document).ready(function() {
          $(document).mousedown(function() {
            this.menu2 = false;
            console.log("menu2:", this.menu2);
          });
        });
      }
    }
  }
};
</script>

<style>
div#middle-topNavigation-container .UIIntermediateContainer > .UIRowContainer {
  display: flex;
}
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

h4 {
  text-align: center;
}
</style>
