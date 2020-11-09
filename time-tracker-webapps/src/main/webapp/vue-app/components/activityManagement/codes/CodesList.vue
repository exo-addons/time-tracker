<template>
    <div>
        <v-flex>
            <v-data-table :headers="activityHeaders" :items="activityCodes" sort-by="id" sort-desc class="elevation-1">
                <template v-slot:top>
                    <v-toolbar flat color="white">
                        <v-toolbar-title>Activity Code list</v-toolbar-title>
                        <v-divider class="mx-4" inset vertical></v-divider>
                        <v-spacer></v-spacer>
                        <button class="btn btn-primary pull-left" type="button" @click="openAddActivityCodeDrawer">
                                    <i class="uiIconSocSimplePlus uiIconSocWhite"></i> Add ActivityCode
                                </button>

                    </v-toolbar>
                </template>
                <template v-slot:item.action="{ item }">
                     <v-icon small class="mr-2" @click="openEditActivityCodeDrawer(item)">
                        edit
                    </v-icon>
                    <v-icon small @click="deleteActivityCode(item)">
                        delete
                    </v-icon>
                </template>
                <template v-slot:no-data>No activity codes</template>
            </v-data-table>
        </v-flex>




        <v-flex>
            <v-data-table :headers="subActivityCodeHeaders" :items="subActivityCodes" sort-by="id" sort-desc class="elevation-1">
                <template v-slot:top>
                    <v-toolbar flat color="white">
                        <v-toolbar-title>Sub Activity Code list</v-toolbar-title>
                        <v-divider class="mx-4" inset vertical></v-divider>
                        <v-spacer></v-spacer>
                        <button class="btn btn-primary pull-left" subActivityCode="button" @click="openAddSubActivityCodeDrawer">
                                    <i class="uiIconSocSimplePlus uiIconSocWhite"></i> Add Sub Activity Code
                                </button>

                    </v-toolbar>
                </template>
                <template v-slot:item.action="{ item }">
                     <v-icon small class="mr-2" @click="openEditSubActivityCodeDrawer(item)">
                        edit
                    </v-icon>
                    <v-icon small @click="deleteSubActivityCode(item)">
                        delete
                    </v-icon>
                </template>
                <template v-slot:no-data>No Sub Activity Codes</template>
            </v-data-table>
        </v-flex>


        <v-flex>
            <v-data-table :headers="typeHeaders" :items="types" sort-by="id" sort-desc class="elevation-1">
                <template v-slot:top>
                    <v-toolbar flat color="white">
                        <v-toolbar-title>Type list</v-toolbar-title>
                        <v-divider class="mx-4" inset vertical></v-divider>
                        <v-spacer></v-spacer>
                        <button class="btn btn-primary pull-left" type="button" @click="openAddTypeDrawer">
                                    <i class="uiIconSocSimplePlus uiIconSocWhite"></i> Add Type
                                </button>

                    </v-toolbar>
                </template>
                <template v-slot:item.action="{ item }">
                     <v-icon small class="mr-2" @click="openEditTypeDrawer(item)">
                        edit
                    </v-icon>
                    <v-icon small @click="deleteType(item)">
                        delete
                    </v-icon>
                </template>
                <template v-slot:no-data>No types</template>
            </v-data-table>
        </v-flex>


        <v-flex>
            <v-data-table :headers="subTypeHeaders" :items="subTypes" sort-by="id" sort-desc class="elevation-1">
                <template v-slot:top>
                    <v-toolbar flat color="white">
                        <v-toolbar-title>Sub Type list</v-toolbar-title>
                        <v-divider class="mx-4" inset vertical></v-divider>
                        <v-spacer></v-spacer>
                        <button class="btn btn-primary pull-left" type="button" @click="openAddSubTypeDrawer">
                                    <i class="uiIconSocSimplePlus uiIconSocWhite"></i> Add Sub Type Code
                                </button>

                    </v-toolbar>
                </template>
                <template v-slot:item.action="{ item }">
                     <v-icon small class="mr-2" @click="openEditSubTypeDrawer(item)">
                        edit
                    </v-icon>
                    <v-icon small @click="deleteSubType(item)">
                        delete
                    </v-icon>
                </template>
                <template v-slot:no-data>No Sub Types</template>
            </v-data-table>
        </v-flex>



        <add-activity-code-drawer ref="addActivityCodeDrawer" v-on:save="addActivityCode" />

        <add-sub-activity-code-drawer ref="addSubActivityCodeDrawer"  v-on:save="addSubActivityCode" />

        <add-type-drawer ref="addTypeDrawer"  :activityCodes="activityCodes" v-on:save="addType" />
       
        <add-sub-type-drawer ref="addSubTypeDrawer" :types="types" v-on:save="addSubType" />

        <edit-activity-code-drawer ref="editActivityCodeDrawer" v-on:save="editActivityCode" />

        <edit-sub-activity-code-drawer ref="editSubActivityCodeDrawer"  v-on:save="editSubActivityCode" />

        <edit-type-drawer ref="editTypeDrawer" v-on:save="editType" />
       
        <edit-sub-type-drawer ref="editSubTypeDrawer"  :types="types" v-on:save="editSubType" />
    </div>
</template>

<script>
import addActivityCodeDrawer from './AddActivityCodeDrawer.vue';
import addSubActivityCodeDrawer from './AddSubActivityCodeDrawer.vue';
import addTypeDrawer from './AddTypeDrawer.vue';
import addSubTypeDrawer from './AddSubTypeDrawer.vue';
import editActivityCodeDrawer from './EditActivityCodeDrawer.vue';
import editSubActivityCodeDrawer from './EditSubActivityCodeDrawer.vue';
import editTypeDrawer from './EditTypeDrawer.vue';
import editSubTypeDrawer from './EditSubTypeDrawer.vue';
export default {
    components: {
        addActivityCodeDrawer,
        addSubActivityCodeDrawer,
        addTypeDrawer,
        addSubTypeDrawer,
        editActivityCodeDrawer,
        editSubActivityCodeDrawer,
        editTypeDrawer,
        editSubTypeDrawer,
    },
    props:['activityCodes','subActivityCodes','types','subTypes'],
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
             
    }),

    computed: {
            activityHeaders() {
            return [{
                text: 'label',
                align: 'center',
                sortable: true,
                value: 'label',
            },
            {
                text: 'code',
                align: 'center',
                sortable: true,
                value: 'code',
            },
            {
                text: 'Actions',
                align: 'center',
                sortable: true,
                value: 'action',
            },
            ]

        },
        subActivityCodeHeaders() {
            return [{
                text: 'label',
                align: 'center',
                sortable: true,
                value: 'label',
            },
            {
                text: 'code',
                align: 'center',
                sortable: true,
                value: 'code',
            },
            {
                text: 'Actions',
                align: 'center',
                sortable: true,
                value: 'action',
            },
            ]

        },
        typeHeaders() {
            return [{
                text: 'label',
                align: 'center',
                sortable: true,
                value: 'label',
            },
            {
                text: 'code',
                align: 'center',
                sortable: true,
                value: 'activityCode.label',
            },
            {
                text: 'Actions',
                align: 'center',
                sortable: true,
                value: 'action',
            }
            ]

        },
        subTypeHeaders() {
            return [{
                text: 'label',
                align: 'center',
                sortable: true,
                value: 'label',
            },
            {
                text: 'code',
                align: 'center',
                sortable: true,
                value: 'code',
            },
            {
                text: 'type',
                align: 'center',
                sortable: true,
                value: 'type.label',
            },
            {
                text: 'Actions',
                align: 'center',
                sortable: true,
                value: 'action',
            }
            ]

        }
    },


    methods: {

       


        deleteActivityCode(item) {
            const index = this.activityCodes.indexOf(item)
            this.activityCodes.splice(index, 1)
            this.$emit('deleteActivityCode', item)
        },
        
        deleteSubActivityCode(item) {
            const index = this.subActivityCodes.indexOf(item)
            this.subActivityCodes.splice(index, 1)
            this.$emit('deleteSubActivityCode', item)
        },
        
        deleteType(item) {
            const index = this.types.indexOf(item)
            this.types.splice(index, 1)
            this.$emit('deleteType', item)
        },
        
        deleteSubType(item) {
            const index = this.subTypes.indexOf(item)
            this.subTypes.splice(index, 1)
            this.$emit('deleteSubType', item)
        },

        openAddActivityCodeDrawer() {
            this.$refs.addActivityCodeDrawer.open()
        },
        
        openAddSubActivityCodeDrawer() {
            this.$refs.addSubActivityCodeDrawer.open()
        },
        
        openAddTypeDrawer() {
            this.$refs.addTypeDrawer.open()
        },
        
        openAddSubTypeDrawer() {
            this.$refs.addSubTypeDrawer.open()
        },


        addActivityCode(activityCode) {
           // this.activityCodes.push(activityCode)
            this.$emit('addActivityCode', activityCode)
        },

        

        addSubActivityCode(subActivityCode) {
           // this.subActivityCodes.push(subActivityCode)
            this.$emit('addSubActivityCode', subActivityCode)
        },



        addType(type) {
            //this.types.push(type)
            this.$emit('addType', type)
        },



        addSubType(subType) {
           // this.subTypes.push(subType)
            this.$emit('addSubType', subType)
        },

        openEditActivityCodeDrawer(item) {
            this.$refs.editActivityCodeDrawer.open(item)
        },

        openEditSubActivityCodeDrawer(item) {
            this.$refs.editSubActivityCodeDrawer.open(item)
        },

        openEditTypeDrawer(item) {
            this.$refs.editTypeDrawer.open(item)
        },

        openEditSubTypeDrawer(item) {
            this.$refs.editSubTypeDrawer.open(item)
        }
,
          editActivityCode(activityCode) {
           // this.activityCodes.push(activityCode)
            this.$emit('editActivityCode', activityCode)
        },

        

        editSubActivityCode(subActivityCode) {
           // this.subActivityCodes.push(subActivityCode)
            this.$emit('editSubActivityCode', subActivityCode)
        },



        editType(type) {
            //this.types.push(type)
            this.$emit('editType', type)
        },



        editSubType(subType) {
           // this.subTypes.push(subType)
            this.$emit('editSubType', subType)
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
</style>
