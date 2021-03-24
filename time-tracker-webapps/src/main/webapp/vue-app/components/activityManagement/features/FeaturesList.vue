<template>
    <div>
        <v-flex>
            <v-data-table :headers="headers" :items="features" :search="search" sort-by="label" class="elevation-1">
                <template v-slot:top>
                    <v-toolbar flat color="white">
                        <v-toolbar-title>Feature list</v-toolbar-title>
                        <v-divider class="mx-4" inset vertical></v-divider>
                        <v-spacer></v-spacer>
                        <v-divider class="mx-4" inset vertical></v-divider>
                        <v-text-field v-model="search" placeholder="Filter" prepend-inner-icon="fa-filter" class="inputFilter pa-0 mr-3 my-auto"  clearable />  
                        <button class="btn btn-primary pull-left" type="button" @click="openAddFeatureDrawer">
                                    <i class="uiIconSocSimplePlus uiIconSocWhite"></i> Add Feature
                                </button>

                    </v-toolbar>
                </template>
                <template v-slot:item.action="{ item }">
                     <v-icon small class="mr-2" @click="openEditDrawer(item)">
                        edit
                    </v-icon>
                    <v-icon small @click="deleteItem(item)">
                        delete
                    </v-icon>
                </template>
                <template v-slot:no-data>No features</template>
            </v-data-table>
        </v-flex>
        <add-feature-drawer ref="addFeatureDrawer" v-on:save="add" />
        <edit-feature-drawer ref="editFeatureDrawer" :feature="editedItem" v-on:save="update" />
    </div>
</template>

<script>
import addFeatureDrawer from './AddFeatureDrawer.vue';
import editFeatureDrawer from './EditFeatureDrawer.vue';
export default {
    components: {
        addFeatureDrawer,
editFeatureDrawer,
    },
    props:['features'],
    data: () => ({
       search: '',
        valid: true,
        
        editedIndex: -1,
        editedItem: {
            code: '',
            label: '',
            spec: '',
            exo: '',
        },
        defaultItem: {
            code: '',
            label: '',
            spec: '',
            exo: '',
        },
             
    }),

    computed: {
            headers() {
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
                text: 'spec',
                align: 'center',
                sortable: true,
                value: 'spec',
            },
            {
                text: 'exo',
                align: 'center',
                sortable: true,
                value: 'exo',
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

       


        deleteItem(item) {
            const index = this.features.indexOf(item)
            this.features.splice(index, 1)
            this.$emit('delete', item)
        },
        openAddFeatureDrawer() {
            this.$refs.addFeatureDrawer.open()
        },

        openEditDrawer(item) {
            this.editedIndex = this.features.indexOf(item)
            this.editedItem=item
            this.$refs.editFeatureDrawer.open()
        },


        add(feature) {
            this.$emit('addFeature', feature)
        },

        update(feature) {
            Object.assign(this.features[this.editedIndex], feature)
            this.$emit('editFeature', this.editedItem)
        }

    }
};
</script>

<style>
#featureManagementApp {
    overflow: hidden;
    padding: 10px 20px;
}

select {
    width: auto;
}

#featureManagementApp .v-input input {
    margin-bottom: 0;
    border: 0;
    box-shadow: none;
}

#featureManagementApp .v-toolbar .v-input {
    margin-left: 18px;
}

#featureManagementApp .v-data-table {
 width: 100%;
}
</style>
