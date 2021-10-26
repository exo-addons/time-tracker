<template>
    <div>
        <v-flex>
            <v-data-table :headers="officeHeaders" :items="offices" sort-by="label" class="elevation-1">
                <template v-slot:top>
                    <v-toolbar flat color="white">
                        <v-toolbar-title>Offices list</v-toolbar-title>
                        <v-divider class="mx-4" inset vertical></v-divider>
                        <v-spacer></v-spacer>
                        <v-divider class="mx-4" inset vertical></v-divider>
                        <button class="btn btn-primary pull-left" type="button" @click="openAddOfficeDrawer">
                            <i class="uiIconSocSimplePlus uiIconSocWhite"></i> Add office
                        </button>
                    </v-toolbar>
                </template>
                 <template v-slot:item.action="{ item }">
                     <v-icon small class="mr-2" @click="openEditOfficeDrawer(item)">
                        edit
                    </v-icon>
                    <v-icon small @click="deleteOffice(item)">
                        delete
                    </v-icon>
                </template>
                <template v-slot:no-data>No offices</template>
            </v-data-table>
        </v-flex>


        <v-flex>
            <v-data-table :headers="locationHeaders" :items="locations" sort-by="label"  class="elevation-1">
                <template v-slot:top>
                    <v-toolbar flat color="white">
                        <v-toolbar-title>Locations list</v-toolbar-title>
                        <v-divider class="mx-4" inset vertical></v-divider>
                        <v-spacer></v-spacer>
                        <v-divider class="mx-4" inset vertical></v-divider>
                        <button class="btn btn-primary pull-left" type="button" @click="openAddLocationDrawer">
                                    <i class="uiIconSocSimplePlus uiIconSocWhite"></i> Add location
                                </button>

                    </v-toolbar>
                </template>
                <template v-slot:item.action="{ item }">
                     <v-icon small class="mr-2" @click="openEditLocationDrawer(item)">
                        edit
                    </v-icon>
                    <v-icon small @click="deleteLocation(item)">
                        delete
                    </v-icon>
                </template>
                <template v-slot:no-data>No locations</template>
            </v-data-table>
        </v-flex>



        <v-flex>
            <v-data-table :headers="workTimeHeaders" :items="workTimePlans" sort-by="label"  class="elevation-1">
                <template v-slot:top>
                    <v-toolbar flat color="white">
                        <v-toolbar-title>Work times list</v-toolbar-title>
                        <v-divider class="mx-4" inset vertical></v-divider>
                        <v-spacer></v-spacer>
                        <v-divider class="mx-4" inset vertical></v-divider>
                        <button class="btn btn-primary pull-left" type="button" @click="openAddWorkTimeDrawer">
                                    <i class="uiIconSocSimplePlus uiIconSocWhite"></i> Add Work Time
                                </button>

                    </v-toolbar>
                </template>
                <template v-slot:item.action="{ item }">
                     <v-icon small class="mr-2" @click="openEditWorkTimeDrawer(item)">
                        edit
                    </v-icon>
                    <v-icon small @click="deleteWorkTime(item)">
                        delete
                    </v-icon>
                </template>
                <template v-slot:no-data>No Work Time plans</template>
            </v-data-table>
        </v-flex>

        <v-flex>
            <v-card outlined style="padding: 16px;">
                <v-form ref="form">
                    <div>
                        <v-label for="label">
                            Users Space 
                        </v-label>
                        <input ref="label" v-model="otherSettings.usersSpace" type="text" name="label" class="input-block-level ignore-vuetify-classes my-3" />
                    </div>
                    <div>
                        <v-label for="subActivityCode">
                            Default Sub Activity
                        </v-label>
                            <select v-model="otherSettings.defaultFeatureSubActivity" name="subActivityCode" class="input-block-level ignore-vuetify-classes my-3">
                            <option v-for="item in subActivityCodes" :key="item.id" :value="item">
                                {{ item.displayLabel}}
                            </option>
                            </select>
                    </div>
                    <div>
                        <v-label for="weekEndHolidayActivity">
                            Week End Holiday Activity
                        </v-label>
                            <select v-model="otherSettings.weekEndHolidayActivity.id" name="weekEndHolidayActivity" class="input-block-level ignore-vuetify-classes my-3">
                            <option v-for="item in activities" :key="item.id" :value="item.id">
                                {{ item.label}}
                            </option>
                            </select>
                    </div>
                    <v-card-actions>
                        <div class="flex-grow-1"></div>
                        <div class="uiAction">
                            <button :disabled="!valid" @click="saveSettings()" class="btn btn-primary" type="button">Save</button>
                        </div>
                    </v-card-actions>
                    </v-form>
                </v-card>
           
        </v-flex>

        <add-office-drawer ref="addOfficeDrawer" v-on:save="addOffice" />

        <add-location-drawer ref="addLocationDrawer"  v-on:save="addLocation" />

        <add-work-time-drawer ref="addWorkTimeDrawer"  v-on:save="addWorkTime" :offices="offices"  :teams="teams" />      

        <edit-office-drawer ref="editOfficeDrawer" v-on:save="editOffice" />

        <edit-location-drawer ref="editLocationDrawer"  v-on:save="editLocation" />

        <edit-work-time-drawer ref="editWorkTimeDrawer"  v-on:save="editWorkTime" :offices="offices"  :teams="teams"/>

       
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
    props:['offices','locations','workTimePlans','subActivityCodes','otherSettings','teams'],
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

    created() {
        this.getActivities()
    },

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
            ]

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
            ]

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
            ]

        },
        defaultFeatureSubActivityId(){
            if(this.otherSettings.defaultFeatureSubActivity){
            return this.otherSettings.defaultFeatureSubActivity.id
            }else{
                return ''
            }
        }
    },

    methods: {

       getActivities() {
            fetch(`/portal/rest/timetracker/activitymgn/activity`, {
                    credentials: 'include',
                })
                .then((resp) => resp.json())
                .then((resp) => {
                    this.activities = resp;
                });
        },

        deleteOffice(item) {
            const index = this.offices.indexOf(item)
            this.offices.splice(index, 1)
            this.$emit('deleteOffice', item)
        },
        
        deleteLocation(item) {
            const index = this.locations.indexOf(item)
            this.locations.splice(index, 1)
            this.$emit('deleteLocation', item)
        },
   
        deleteWorkTime(item) {
            const index = this.workTimePlans.indexOf(item)
            this.workTimePlans.splice(index, 1)
            this.$emit('deleteWorkTime', item)
        },
   

        openAddLocationDrawer() {
            this.$refs.addLocationDrawer.open()
        },
        
        openAddOfficeDrawer() {
            this.$refs.addOfficeDrawer.open()
        },

        openAddWorkTimeDrawer() {
            this.$refs.addWorkTimeDrawer.open()
        },

        addOffice(office) {
            this.offices.push(office)
            this.$emit('addOffice', office)
        },      

        addLocation(location) {
            this.locations.push(location)
            this.$emit('addLocation', location)
        },

        addWorkTime(workTime) {
            this.workTimePlans.push(workTime)
            this.$emit('addWorkTime', workTime)
        },

        openEditOfficeDrawer(item) {
            this.$refs.editOfficeDrawer.open(item)
        },

        openEditLocationDrawer(item) {
            this.$refs.editLocationDrawer.open(item)
        },

        openEditWorkTimeDrawer(item) {
            this.$refs.editWorkTimeDrawer.open(item)
        },

        editOffice(office) {
            this.offices.push(office)
            this.$emit('editOffice', office)
        },

        editLocation(location) {
            this.locations.push(location)
            this.$emit('editLocation', location)
        },
        
        editWorkTime(workTime) {
            this.$emit('editWorkTime', workTime)
        },

        saveSettings() {
            if(this.otherSettings.weekEndHolidayActivity && this.otherSettings.weekEndHolidayActivity.id){
                this.otherSettings.weekEndHolidayActivity =this.activities.find(act => act.id === this.otherSettings.weekEndHolidayActivity.id)
            }
            
            this.$emit('saveOtherSettings', this.otherSettings)
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
