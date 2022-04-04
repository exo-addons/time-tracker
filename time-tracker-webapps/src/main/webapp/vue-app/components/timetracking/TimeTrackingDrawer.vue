<template>
  <div data-app>
    <exo-drawer
      ref="timeTrackerDrawer"
      right
      class="logTimeDrawer">
      <template slot="title">
        {{ $t("exo.timeTracker.timeTracking.timeTrackingDrawer.toolbarTitle") }}
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
        <div 
          align="center" 
          justify="center">
          <h4 :class="itemRowBackground">
            {{ $t("exo.timeTracker.timeTracking.timeTrackingDrawer.text.totlal") }}
            {{ total }}
          </h4>
        </div>
        
        <div align="center" justify="center">
          <v-list v-if="activityRecords.length > 0" class="actList">
            <v-list-item
              v-for="(item,i) in activityRecords"
              :key="i"
              class="actItem pr-n6">
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
                      class="text-truncate text-left mb-2"
                      v-text="item.activity.label" />
                    <span class=" TimeTrackingDrawerSpan d-inline-block text-truncate text-left mb-n2">
                      <v-list-item-subtitle v-text="item.description" />
                    </span>                   
                  </v-list-item-content>
                </template>
                <span class="TimeTrackingDrawerSpan text-left text-justify text-break">{{ item.description }}</span>
              </v-tooltip>
              <v-list-item-action>
                <template>
                  <v-menu
                    ref="TTDrawerMenuBlured"
                    class="d-flex align-self-start"
                    close-on-click
                    close-on-content-click
                    bottom 
                    attach
                    nudge-bottom="29"
                    left>
                    <template v-slot:activator="{ on, attrs }">
                      <v-btn
                        class="d-flex mt-n3 mb-1 mr-n4"
                        small
                        icon
                        v-bind="attrs"
                        v-on="on"
                        @click="menuItemFunction(i)">
                        <v-icon small size="10">mdi-dots-vertical</v-icon>
                      </v-btn>
                    </template>
                    <v-list class="text-left TTbackgroundList mt-n2" ref="TTlistRef">
                      <v-list-item 
                        @click="editActivityRecord(item)">
                        <v-list-item-title class="subtitle-2">
                          <i class="uiIcon uiIconEdit"></i>
                          {{ $t("exo.timeTracker.teams.teamsList.edit") }}
                        </v-list-item-title>
                      </v-list-item>
                      <v-list-item 
                        @click="deleteActivityRecord(item.id)">
                        <v-list-item-title class="subtitle-2">
                          <i class="uiIcon uiIconTrash"></i>
                          {{ $t("exo.timeTracker.teams.teamsList.delete") }}
                        </v-list-item-title>
                      </v-list-item>
                    </v-list>
                  </v-menu>
                </template>
                <template>
                  <div
                    class="numberHr d-flex justify-content-flex-start mt-2">
                    <i class="text-left notranslate mdi mdi-clock"></i>
                    <span>
                      {{ item.time }}h
                    </span>
                  </div>
                </template>
              </v-list-item-action>
            </v-list-item>
          </v-list>
        </div>
        <div>
          <v-btn 
            class="TTDrawerButtonAdd"
            block
            large
            text
            plain
            @click="addActivityRecord()">
            <v-icon class=" text--lighten-1" left>mdi-plus</v-icon>
            <span>
              {{ $t("exo.timeTracker.timeSheet.timeSheet.buttonLabelAddActivity") }}
            </span>
          </v-btn>
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
        @ok="deleteConfirm()" />
    </template>
    <template>
      <v-alert
        id
        v-model="alert"
        :class="alert_type"
        :type="alert_type"
        border="left"
        elevation="2"
        colored-border
        outlined
        dismissible>
        {{ $t(message) }}
        <v-btn
          v-if="undo === true"
          class="primary--text"
          @click="deleteItemConfirm=true"
          text>
          Undo
        </v-btn>
      </v-alert>
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
    undo: false,
    deleteItemConfirm: false,
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
    deleteId: 0,
    isActivity: true,
    itemRowBackground: 'timeSheetTime-not-valid',
    menuItemIndex: -1,
    
  }),
  watch: {
    deleteItemConfirm(val){
      if (val){
        this.undo=false;
        this.alert=false;
        this.displaySusccessMessage('exo.timeTracker.label.displaySusccessMessageCancel');
        this.deleteItemConfirm= false;
      }
    },
    date (){
      this.getActivityRecords();
      this.formatDate(this.date);
    },
    total (){
      this.rowBackground();
    },
  },
  mounted () {
    $(this.$refs.timeTrackerDrawer.$el).click(()=> { 
      if (this.activityRecordMenuDatePicker) {
        this.activityRecordMenuDatePicker = false;
      }
      if (this.$refs && this.$refs.TTDrawerMenuBlured && this.$refs.TTDrawerMenuBlured[this.menuItemIndex] ) {
        if (this.$refs.TTDrawerMenuBlured[this.menuItemIndex].isActive){
          this.$refs.TTDrawerMenuBlured[this.menuItemIndex].isActive = false;
        }
      }
    });
  },
  methods: {
    deleteConfirm(){
      this.undo=true;
      this.displaySusccessMessage('exo.timeTracker.label.displaySusccessMessageDelete');
      setTimeout(()=> {
        if (!this.deleteItemConfirm){
          this.deleteActivity();
          this.undo=false;
        } else {
          this.deleteItemConfirm = false;
        }
      }, 5000);
    },
    menuItemFunction(i){
      if (this.$refs && this.$refs.TTDrawerMenuBlured) {
        if (this.$refs.TTDrawerMenuBlured[this.menuItemIndex]){
          this.$refs.TTDrawerMenuBlured[this.menuItemIndex].isActive = false;
        }
        this.menuItemIndex=i;
      }
    },
    rowBackground() {
      if (this.activityRecords[0] && this.isActivity){
        if (this.activityRecords[0].activityTime.day === 6 ||
            this.activityRecords[0].activityTime.day === 0) {
          this.itemRowBackground= 'timeSheetTime-weekend';
        } else if (this.activityRecords[0].location ==='eXo FR' &&
            this.activityRecords[0].office ==='FR' &&
            this.total !== 7 &&  this.activityRecords[0].activityTime.day === 5){
          this.itemRowBackground= 'timeSheetTime-to-be-fixed';
        } else if ((!this.activityRecords[0].location || !this.activityRecords[0].office || this.total !== 8) && 
        (this.activityRecords[0].activityTime.day !== 5 ||
        this.activityRecords[0].location !=='eXo FR' || this.activityRecords[0].office !=='FR')) {
          this.itemRowBackground= 'timeSheetTime-to-be-fixed';
        } else {
          this.itemRowBackground= '' ;
        }
      } else {
        this.itemRowBackground= 'timeSheetTime-not-valid';
      }
    },
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
          this.total=this.activityRecords.reduce((accum, item)=>{
            if (item.activity === null){
              this.isActivity = false ;
            }
            return accum + item.time;
          },0);
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
          this.displaySusccessMessage(('exo.timeTracker.label.displaySusccessMessageAdd'));
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
          this.displaySusccessMessage(('exo.timeTracker.label.displaySusccessMessageEdit'));
        })
        .catch(result => {
          this.getActivityRecords();
          result.text().then(body => {
            this.displayErrorMessage(body);
          });
        });
    },
    deleteActivity() {
      if (this.undo){
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
            this.deleteItemConfirm = false;
          })
          .catch(result => {
            this.getActivityRecords();
            result.text().then(body => {
              this.displayErrorMessage(body);
            });
          });
      }
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
      this.$refs.addTTEntryDrawer.openAddTTEntryDrawer();
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
      this.alert_type = 'success';
      this.alertIcon = 'uiIconSuccess';
      this.alert = true;
      setTimeout(() => (this.alert = false), 5000);
      this.editedItem = this.defaultItem;
    },
    displayErrorMessage(message) {
      this.isUpdating = false;
      this.message = message;
      this.alert_type = 'error';
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
  background: #f6f7fa;
  margin-bottom: 5px;
  border: 0.5px solid;
  border-radius: 6px;
  border-color: #e1e8ee;
}
.numberHr {
  min-width: 55px;
  font-size: 16px !important;
  color: #578dc9 !important;
  text-align: left;
}
.TTbackgroundList {
  background:#fff !important;
}
</style>
