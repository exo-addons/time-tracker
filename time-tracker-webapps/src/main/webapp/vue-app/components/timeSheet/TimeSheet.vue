<template>
<div>
    <div :class="alert_type" class="alert" id v-if="alert">
        <i :class="alertIcon"></i>
        {{message}}
    </div>
    <v-card elevation="0">
        <v-card-text>
            <v-layout>

                <v-data-table :headers="headers" :items="activityRecordsList" :options.sync="options" :server-items-length="totalRecords" :loading="loading" elevation="0">
                    <template v-slot:top>
                        <v-toolbar color="white" flat>
                            <template>

                                <button class="btn btn-primary pull-left" type="button" @click="openAddTTEntryDrawer">
                                    <i class="uiIconSocSimplePlus uiIconSocWhite"></i> Add Activity
                                </button>

                                <download-excel :fetch="exportData" :fields="json_fields" name="timeSheet.xls">
                                    <button class="btn btn-export" type="button">
                                        <i class="uiIconExport"></i> Export
                                    </button>
                                </download-excel>

                                <v-spacer />
                                <!-- <v-col cols="12" md="3" sm="6">
                                    <v-text-field placeholder="Look for Activities" prepend-inner-icon="mdi-filter" single-line label="" v-model="search"></v-text-field>
                                </v-col> -->
                                <v-menu ref="menu" v-model="menu" :close-on-content-click="false" :return-value.sync="date" transition="scale-transition" offset-y min-width="290px">
                                    <template v-slot:activator="{ on }">
                                        <v-text-field v-model="dateRangeText" prepend-icon="event" readonly v-on="on"></v-text-field>
                                    </template>
                                    <v-date-picker v-model="date" range no-title scrollable>
                                        <v-spacer></v-spacer>
                                        <v-btn text color="primary" @click="menu = false">Cancel</v-btn>
                                        <v-btn text color="primary" @click="$refs.menu.save(date),setDates()">OK</v-btn>
                                    </v-date-picker>
                                </v-menu>

                                <a class="caption primary--text drawersBtn" @click="toggleFilterDrawer">
                                    <v-icon color="primary" left>mdi-tune</v-icon> Filter
                                </a>

                            </template>
                        </v-toolbar>
                    </template>
                    <template v-slot:item.action="{ item }">
                        <v-icon small class="mr-2" @click="editActivityRecord(item)">
                            edit
                        </v-icon>
                        <v-icon small @click="deleteItem(item)">
                            delete
                        </v-icon>
                    </template>

                    <template v-slot:no-data>No Activities</template>
                </v-data-table>
            </v-layout>

        </v-card-text>
    </v-card>
    <add-tracking-entry-drawer ref="addTTEntryDrawer" :activities="activities" v-on:save="save"></add-tracking-entry-drawer>
    <edit-tracking-entry-drawer ref="editTTEntryDrawer" :activities="activities" :activityRecord="activityRecord" v-on:save="update"></edit-tracking-entry-drawer>
    <filter-drawer ref="filterDrawer" :activities="activities" :types="types" :subTypes="subTypes" :activityCodes="activityCodes" :subActivityCodes="subActivityCodes" :clients="clients" :features="features"  :filters="filters" v-on:addFilter="addFilter" v-on:saveFilter="saveFilter"  v-on:deleteFilter="deleteFilter" />
</div>
</template>

<script>
import filterDrawer from './FilterDrawer.vue';

import downloadExcel from "vue-json-excel";
import AddTrackingEntryDrawer from '../commons/AddRecordDrawer.vue';
import EditTrackingEntryDrawer from '../commons/EditRecorddrawer.vue';
export default {
    components: {
        downloadExcel,
        filterDrawer,
        AddTrackingEntryDrawer,
        EditTrackingEntryDrawer,
    },

    data: () => ({
        json_fields: {

            'Date': 'activityDate',
            'Activity label': 'activity.label',
            'description': 'description',
            'Time': 'time',
            'Location': 'location',
            'Type': 'activity.type.label',
            'Sub Type': 'activity.subType.label',
            'Activity Code': 'activity.activityCode.label',
            'Sub Activity Code': 'activity.subActivityCode.label',
            'Project': 'activity.project.label',
            'Client': 'activity.project.client.label',
            'Feature': 'activity.feature.label',
            'Sales Order': 'salesOrder',
            'Project Version': 'projectVersion',
            'User Name': 'userName',

        },
        //filtered: "grey-color",
        date: [],
        dateRangeText: '',
        menu: false,
        totalRecords: 0,
        loading: true,
        options: {},
        activityRecord: {},
        activity: 0,
        type: 0,
        subType: 0,
        activityCode: 0,
        subActivityCode: 0,
        client: 0,
        project: 0,
        feature: 0,
        fromDate: '',
        toDate: '',
        location: '',
        office: '',
        valid: true,
        search: '',
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
        clients: [],
        features: [],
        filters: [],

    }),

    created() {
        this.initialize()
    },
    watch: {
        options: {
            handler() {
                this.getActivityRecords()
                    .then(data => {
                        this.activityRecordsList = data.items
                        this.totalRecords = data.total
                    })

            },
            deep: true,
        },
        dialog(val) {
            return val === true || this.close() === true;
        },

        date(val) {
            console.log(val)
            this.dateRangeText = this.date.join(' ~ ')
            this.fromDate = this.date[0]
            this.toDate = this.date[0]
            if (typeof this.date[1] !== 'undefined') {
                this.toDate = this.date[1]
            }
        },

        search: function (val) {
            if (!this.awaitingSearch) {
                setTimeout(() => {
                    this.getActivityRecords().then(data => {
                        this.activityRecordsList = data.items
                        this.totalRecords = data.total
                    })
                    this.awaitingSearch = false;
                }, 1000); // 1 sec delay
            }
            this.awaitingSearch = true;

        },

    },
    computed: {
        headers() {
            return [{
                    text: 'Date',
                    align: 'center',
                    sortable: true,
                    value: 'activityDate',
                },
                {
                    text: 'Description',
                    align: 'center',
                    sortable: true,
                    value: 'description',
                },
                {
                    text: 'Location',
                    align: 'center',
                    sortable: true,
                    value: 'location',
                },
                {
                    text: 'Time',
                    align: 'center',
                    sortable: true,
                    value: 'time',
                },
                {
                    text: 'Office',
                    align: 'center',
                    sortable: true,
                    value: 'office',
                },
                {
                    text: 'Activity',
                    align: 'center',
                    sortable: true,
                    value: 'activity.label',
                },
                {
                    text: 'SO',
                    align: 'center',
                    sortable: true,
                    value: 'salesOrder',
                },
                {
                    text: 'Type',
                    align: 'center',
                    sortable: true,
                    value: 'activity.type.label',
                },
                {
                    text: 'Sub Type',
                    align: 'center',
                    sortable: true,
                    value: 'activity.subType.label',
                },
                {
                    text: 'Client',
                    align: 'center',
                    sortable: true,
                    value: 'activity.project.client.label',
                },
                {
                    text: 'Project',
                    align: 'center',
                    sortable: true,
                    value: 'activity.project.label',
                },
                {
                    text: 'Activity Code',
                    align: 'center',
                    sortable: true,
                    value: 'activity.activityCode.label',
                },
                {
                    text: 'Sub Activity Code',
                    align: 'center',
                    sortable: true,
                    value: 'activity.subActivityCode.label',
                },
                {
                    text: 'Actions',
                    align: 'center',
                    sortable: true,
                    value: 'action',
                },
            ]
        }

    },

    methods: {
        addFilter(val) {
            this.activity = val.activity
            this.type = val.type
            this.subType = val.subType
            this.activityCode = val.activityCode
            this.subActivityCode = val.subActivityCode
            this.client = val.client
            this.project = val.project
            this.feature = val.feature
            this.location = val.location
            this.office = val.office
            this.getActivityRecords(true).then(data => {
                this.activityRecordsList = data.items
                this.totalRecords = data.total
            })
        },
        toggleFilterDrawer() {
            this.$refs.filterDrawer.open()
        },
        openAddTTEntryDrawer() {
            this.$refs.addTTEntryDrawer.open()
        },

        initialize() {
            const date = new Date();
            const firstDay = new Date(date.getFullYear(), date.getMonth(), 2);
            const lastDay = new Date(date.getFullYear(), date.getMonth() + 1, 0);
            this.date = [firstDay.toISOString().substr(0, 10), lastDay.toISOString().substr(0, 10)]
            this.dateRangeText = this.date.join(' ~ ')
            this.fromDate = this.date[0]
            this.toDate = this.date[1]

            this.getClients()
            this.getProjects();
            this.getActivityCodes();
            this.getSubActivityCodes();
            this.getTypes();
            this.getSubTypes();
            this.getActivities();
            this.getFilters();

            /*             this.getActivityRecords()
                            .then(data => {
                                this.activityRecordsList = data.items
                                this.totalRecords = data.total
                            }) */

        },

        /*         edit(item) {

                },

                delete_(item) {

                },
         */
        close() {
            this.dialog = false;
            this.editedItem = this.defaultItem;
            setTimeout(() => {
                this.editedIndex = -1;
                this.initialize();
            }, 300);
        },

        setDates() {
            this.getActivityRecords(true)
                .then(data => {
                    this.activityRecordsList = data.items
                    this.totalRecords = data.total
                })
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
        getActivityRecords(init) {
            this.loading = true
            return new Promise((resolve, reject) => {
                const {
                    sortBy,
                    sortDesc,
                    page,
                    itemsPerPage
                } = this.options
                let sort = ""
                let desc = false
                let offset = page
                let limit_ = itemsPerPage
                const toExport = false
                if (typeof sortBy !== 'undefined' && sortBy.length > 0) {
                    sort = sortBy[0]
                    if (sortDesc.length > 0) {
                        desc = sortDesc[0]
                    }
                }
                if (init) {
                    this.options.page = 1
                }
                if (typeof page === 'undefined') {
                    offset = 0
                } else {
                    offset = (page - 1) * itemsPerPage
                }
                if (typeof itemsPerPage === 'undefined') {
                    limit_ = 10
                }
                fetch(`/portal/rest/timetracker/activityRecordrecordsmgn/activityrecord/list?search=${this.search}&activity=${this.activity}&type=${this.type}&subType=${this.subType}&activityCode=${this.activityCode}&subActivityCode=${this.subActivityCode}&client=${this.client}&project=${this.project}&feature=${this.feature}&fromDate=${this.fromDate}&toDate=${this.toDate}&location=${this.location}&office=${this.office}&sortby=${sort}&sortdesc=${desc}&page=${offset}&limit=${limit_}&export=${toExport}`, {
                        credentials: 'include',
                    })
                    .then((resp) => resp.json())
                    .then((resp) => {
                        const items = resp.activityRecords
                        const total = resp.size
                        this.loading = false
                        resolve({
                            items,
                            total,
                        })

                    })
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

        getProjects() {
            fetch(`/portal/rest/timetracker/projectsmgn/project`, {
                    credentials: 'include',
                })
                .then((resp) => resp.json())
                .then((resp) => {
                    this.projects = resp;
                });

        },

        getClients() {
            fetch(`/portal/rest/timetracker/clientsmgn/client`, {
                    credentials: 'include',
                })
                .then((resp) => resp.json())
                .then((resp) => {
                    this.clients = resp;
                });

        },

        getFeatures() {
            fetch(`/portal/rest/timetracker/featuresmgn/feature`, {
                    credentials: 'include',
                })
                .then((resp) => resp.json())
                .then((resp) => {
                    this.features = resp;
                });

        },

        getActivityCodes() {
            fetch(`/portal/rest/timetracker/codesmgn/activityCode`, {
                    credentials: 'include',
                })
                .then((resp) => resp.json())
                .then((resp) => {
                    this.activityCodes = resp;
                });

        },

        getSubActivityCodes() {
            fetch(`/portal/rest/timetracker/codesmgn/subActivityCode`, {
                    credentials: 'include',
                })
                .then((resp) => resp.json())
                .then((resp) => {
                    this.subActivityCodes = resp;
                });

        },

        getTypes() {
            fetch(`/portal/rest/timetracker/codesmgn/type`, {
                    credentials: 'include',
                })
                .then((resp) => resp.json())
                .then((resp) => {
                    this.types = resp;
                });

        },

        getSubTypes() {
            fetch(`/portal/rest/timetracker/codesmgn/subType`, {
                    credentials: 'include',
                })
                .then((resp) => resp.json())
                .then((resp) => {
                    this.subTypes = resp;
                });

        },


        getFilters() {
            fetch(`/portal/rest/timetracker/filtersmgn/filter`, {
                    credentials: 'include',
                })
                .then((resp) => resp.json())
                .then((resp) => {
                    this.filters = resp;
                });

        },


        saveFilter(val) {
            this.activity = val.activity
            this.type = val.type
            this.subType = val.subType
            this.activityCode = val.activityCode
            this.subActivityCode = val.subActivityCode
            this.client = val.client
            this.project = val.project
            this.feature = val.feature
            this.location = val.location
            this.office = val.office

            const fields = [];
            fields.push({name:"activity", value:val.activity})
            fields.push({name:"type", value:val.type})
            fields.push({name:"subType", value:val.subType})
            fields.push({name:"activityCode", value:val.activityCode})
            fields.push({name:"subActivityCode", value:val.subActivityCode})
            fields.push({name:"client", value:val.client})
            fields.push({name:"project", value:val.project})
            fields.push({name:"feature", value:val.feature})
            fields.push({name:"location", value:val.location})
            fields.push({name:"office", value:val.office})

            const filter = {
                filter:{name:val.name}, 
                filterFields:fields
            }

            fetch(`/portal/rest/timetracker/filtersmgn/filter`, {
                    method: 'post',
                    credentials: 'include',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(filter),
                })
                .then((result) => {
                    if (!result.ok) {
                        throw result;
                    }
                })
                .then((response) => {
                    this.filters.push(response)
                    this.displaySusccessMessage('filter added');
                })
                .catch((result) => {
                    this.getActivityRecords();
                    result.text().then((body) => {
                        this.displayErrorMessage(body);
                    });
                });
        },


        openTimeTrackingDrawer() {
            this.$refs.timeTrackingDrawer.open()
        },

        save(activityRecord) {

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
                    this.getActivityRecords()
                        .then(data => {
                            this.activityRecordsList = data.items
                            this.totalRecords = data.total
                        })

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
                    this.getActivityRecords()
                        .then(data => {
                            this.activityRecordsList = data.items
                            this.totalRecords = data.total
                        })

                    this.displaySusccessMessage('activity added');
                })
                .catch((result) => {
                    this.getActivityRecords()
                        .then(data => {
                            this.activityRecordsList = data.items
                            this.totalRecords = data.total
                        })

                    result.text().then((body) => {
                        this.displayErrorMessage(body);
                    });
                });
        },

        deleteFilter(item) {
            fetch(`/portal/rest/timetracker/filtersmgn/filter/` + item.filter.id, {
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
                .then((response) => {
                    this.confirmDialog = false;
                    this.getFeatures()
            

                    this.displaySusccessMessage('filter deleted');
                })
                .catch((result) => {
                    this.confirmDialog = false;
                     this.getFeatures()

                    result.text().then((body) => {
                        this.displayErrorMessage(body);
                    });
                });
        }, 
        
        deleteItem(item) {
            fetch(`/portal/rest/timetracker/activityRecordrecordsmgn/activityrecord/` + item.id, {
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
                .then((response) => {
                    this.confirmDialog = false;
                    this.getActivityRecords()
                        .then(data => {
                            this.activityRecordsList = data.items
                            this.totalRecords = data.total
                        })

                    this.displaySusccessMessage('client deleted');
                })
                .catch((result) => {
                    this.confirmDialog = false;
                    this.getActivityRecords()
                        .then(data => {
                            this.activityRecordsList = data.items
                            this.totalRecords = data.total
                        })

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

        async exportData() {
            const response = await this.getActivityRecords(true);
            console.log(response);
            return response.items;
        },

    },
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
</style>
