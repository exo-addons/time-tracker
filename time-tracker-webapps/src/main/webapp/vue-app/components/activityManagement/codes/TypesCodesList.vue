<template>
    <div>
        <v-flex>
            <v-data-table :headers="typeHeaders" :items="types" :search="searchType" sort-by="label" class="elevation-1">
                <template v-slot:top>
                    <v-toolbar flat color="white">
                        <v-toolbar-title>Type list</v-toolbar-title>
                        <v-divider class="mx-4" inset vertical></v-divider>
                        <v-spacer></v-spacer>
                        <v-divider class="mx-4" inset vertical></v-divider>
                        <v-text-field v-model="searchType" placeholder="Filter" prepend-inner-icon="fa-filter" class="inputFilter pa-0 mr-3 my-auto"  clearable />  
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
            <v-data-table :headers="subTypeHeaders" :items="subTypes" :search="searchSubType" sort-by="label" class="elevation-1">
                <template v-slot:top>
                    <v-toolbar flat color="white">
                        <v-toolbar-title>Sub Type list</v-toolbar-title>
                        <v-divider class="mx-4" inset vertical></v-divider>
                        <v-spacer></v-spacer>
                        <v-divider class="mx-4" inset vertical></v-divider>
                        <v-text-field v-model="searchSubType" placeholder="Filter" prepend-inner-icon="fa-filter" class="inputFilter pa-0 mr-3 my-auto"  clearable />  
                        <button class="btn btn-primary pull-left" type="button" @click="openAddSubTypeDrawer">
                                    <i class="uiIconSocSimplePlus uiIconSocWhite"></i> Add Sub Type Code
                                </button>

                    </v-toolbar>
                </template>
                <template v-slot:item.type="{ item }">
                    {{item.code}} - {{item.label}}
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





        <add-type-drawer ref="addTypeDrawer"  :activityCodes="activityCodes" v-on:save="addType" />
       
        <add-sub-type-drawer ref="addSubTypeDrawer" :types="types" v-on:save="addSubType" />

        <edit-type-drawer ref="editTypeDrawer" v-on:save="editType" />
       
        <edit-sub-type-drawer ref="editSubTypeDrawer"  :types="types" v-on:save="editSubType" />
    </div>
</template>

<script>
import addTypeDrawer from './AddTypeDrawer.vue';
import addSubTypeDrawer from './AddSubTypeDrawer.vue';
import editTypeDrawer from './EditTypeDrawer.vue';
import editSubTypeDrawer from './EditSubTypeDrawer.vue';
export default {
    components: {
        addTypeDrawer,
        addSubTypeDrawer,
        editTypeDrawer,
        editSubTypeDrawer,
    },
    props:['types','subTypes'],
    data: () => ({
       searchType: '',
       searchSubType: '',
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
                value: 'code',
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
                value: 'type',
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

        openAddTypeDrawer() {
            this.$refs.addTypeDrawer.open()
        },
        
        openAddSubTypeDrawer() {
            this.$refs.addSubTypeDrawer.open()
        },

       

        addType(type) {
            //this.types.push(type)
            this.$emit('addType', type)
        },



        addSubType(subType) {
           // this.subTypes.push(subType)
            this.$emit('addSubType', subType)
        },

        openEditTypeDrawer(item) {
            this.$refs.editTypeDrawer.open(item)
        },

        openEditSubTypeDrawer(item) {
            this.$refs.editSubTypeDrawer.open(item)
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
