<template>
<exo-drawer ref="timeTrackerDrawer" right class="">
    <div :class="alert_type" class="alert" id v-if="alert">
        <i :class="alertIcon"></i>
        {{message}}
    </div>
    <template slot="title">
        Time Tracking
    </template>
    <template slot="titleIcons">

        <v-btn text small @click="addActivityRecord()">
            <v-icon>mdi-plus</v-icon>
            Add entry
        </v-btn>
    </template>
    <template slot="content">
        <v-row align="center" justify="center">

            <v-menu v-model="menu2" :close-on-content-click="false" :nudge-right="40" transition="scale-transition" offset-y min-width="290px">
                <template v-slot:activator="{ on, attrs }">
                    <v-text-field v-model="date" centered prepend-icon="event" readonly v-bind="attrs" v-on="on"></v-text-field>
                </template>
                <v-date-picker v-model="date" @input="menu2 = false"></v-date-picker>
            </v-menu>
        </v-row>

        <v-row align="center" justify="center">
            <h4>Total number of hours: {{total}}</h4>
        </v-row>
        <v-row>
            <v-list class="actList" v-if="activityRecords.length>0">
                <v-list-item :key="item.id" v-for="item in activityRecords" @click="editActivityRecord(item)" class="actItem">
                    <v-list-item-action>
                        <v-list-item-action-text v-text="item.time" class="numberHr"></v-list-item-action-text>
                    </v-list-item-action>
                    <v-list-item-content>
                        <v-list-item-title v-text="item.activity.label"></v-list-item-title>
                        <v-list-item-subtitle v-text="item.description"></v-list-item-subtitle>
                    </v-list-item-content>
                </v-list-item>
            </v-list>

        </v-row>
        <add-tracking-entry-drawer ref="addTTEntryDrawer" :activities="activities" v-on:save="save"></add-tracking-entry-drawer>
        <edit-tracking-entry-drawer ref="editTTEntryDrawer" :activities="activities" :activityRecord="activityRecord" v-on:save="update"></edit-tracking-entry-drawer>
    </template>
    <template slot="footer">
    </template>
</exo-drawer>
</template>

<script>
import AddTrackingEntryDrawer from './AddTTEntryDrawer.vue';
import EditTrackingEntryDrawer from './EditTTEntryDrawer.vue';
export default {
    components: {
        AddTrackingEntryDrawer,
        EditTrackingEntryDrawer
    },
    data: () => ({
        date: new Date().toISOString().substr(0, 10),
        menu2: false,
        activities: [],
        activityRecords: [],
        activityRecord: {},
        alert: false,
        message: '',
        alert_type: '',
        alertIcon: '',
        total: 0
    }),

    watch: {
        date: function () {
            console.log(this.date)
            this.getActivityRecords()
        }
    },

    created() {
        this.getActivityRecords()
        this.getActivities()
    },

    methods: {

        getActivityRecords() {
            fetch(`/portal/rest/timetracker/activityRecordrecordsmgn/activityrecord/` + this.date, {
                    credentials: 'include',
                })
                .then((resp) => resp.json())
                .then((resp) => {
                    this.activityRecords = resp;
                    this.total = this.activityRecords.reduce((accum, item) => accum + item.time, 0)
                });
        },

        getActivities() {
            fetch(`/portal/rest/timetracker/activitymgn/activity`, {
                    credentials: 'include',
                })
                .then((resp) => resp.json())
                .then((resp) => {
                    this.activities = resp;
                });
        },

        save(activityRecord) {

            //  this.activityRecords.push(activity)
            activityRecord.activityDate = this.date
            fetch(`/portal/rest/timetracker/activityRecordrecordsmgn/activityrecord`, {
                    method: 'post',
                    credentials: 'include',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(activityRecord),
                })
                .then((result) => {
                    if (!result.ok) {
                        throw result;
                    }
                })
                .then((response) => {
                    this.getActivityRecords();
                    //this.activities.push(activity)
                    this.displaySusccessMessage('activity added');
                })
                .catch((result) => {
                    this.getActivityRecords();
                    result.text().then((body) => {
                        this.displayErrorMessage(body);
                    });
                });
        },

        update(activityRecord) {

            fetch(`/portal/rest/timetracker/activityRecordrecordsmgn/activityrecord`, {
                    method: 'put',
                    credentials: 'include',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(activityRecord),
                })
                .then((result) => {
                    if (!result.ok) {
                        throw result;
                    }
                })
                .then((response) => {
                    this.getActivityRecords();
                    this.displaySusccessMessage('activity added');
                })
                .catch((result) => {
                    this.getActivityRecords();
                    result.text().then((body) => {
                        this.displayErrorMessage(body);
                    });
                });
        },
        cancel() {
            this.$refs.timeTrackerDrawer.close()
        },
        open() {
            this.$refs.timeTrackerDrawer.open()
        },
        addActivityRecord() {
            this.$refs.addTTEntryDrawer.open()
        },

        editActivityRecord(item) {

            this.activityRecord = item
            this.$refs.editTTEntryDrawer.open()
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

    }
}
</script>

<style>
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

.drawerContent {
    padding: 15px 27px;
}

h4 {
    text-align: center;
}
</style>
