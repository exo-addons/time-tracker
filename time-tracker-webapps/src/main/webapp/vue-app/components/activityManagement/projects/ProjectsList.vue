<template>
    <div>
        <v-flex>
            <v-data-table :headers="headers" :items="projects" :search="search" sort-by="id" sort-desc class="elevation-1">
                <template v-slot:top>
                    <v-toolbar flat color="white">
                        <v-toolbar-title>Project list</v-toolbar-title>
                        <v-divider class="mx-4" inset vertical></v-divider>
                        <v-spacer></v-spacer>
                        <v-divider class="mx-4" inset vertical></v-divider>
                        <v-text-field v-model="search" placeholder="Filter" prepend-inner-icon="fa-filter" class="inputFilter pa-0 mr-3 my-auto"  clearable />  
                        <button class="btn btn-primary pull-left" type="button" @click="openAddProjectDrawer">
                                    <i class="uiIconSocSimplePlus uiIconSocWhite"></i> Add Project
                                </button>

                    </v-toolbar>
                </template>
                <template v-slot:item.client="{ item }">
                    {{item.code}} - {{item.label}}
                </template>
                <template v-slot:item.action="{ item }">
                     <v-icon small class="mr-2" @click="openEditDrawer(item)">
                        edit
                    </v-icon>
                    <v-icon small @click="deleteItem(item)">
                        delete
                    </v-icon>
                </template>
                <template v-slot:no-data>No projects</template>
            </v-data-table>
        </v-flex>
        <add-project-drawer ref="addProjectDrawer" :clients="clients" v-on:save="add" />
        <edit-project-drawer ref="editProjectDrawer" :project="editedItem" :clients="clients" v-on:save="update" />
    </div>
</template>

<script>
import addProjectDrawer from './AddProjectDrawer.vue';
import editProjectDrawer from './EditProjectDrawer.vue';
export default {
    components: {
        addProjectDrawer,
editProjectDrawer,
    },
    props:['projects','clients'],
    data: () => ({
       search: '',
        valid: true,
        
        editedIndex: -1,
        editedItem: {
            code: '',
            label: '',
            client:{id:''}
        },
        defaultItem: {
            code: '',
            label: '',
            client:{id:''}
           
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
                text: 'client',
                align: 'center',
                sortable: true,
                value: 'client',
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
            const index = this.projects.indexOf(item)
            this.projects.splice(index, 1)
            this.$emit('delete', item)
        },
        openAddProjectDrawer() {
            this.$refs.addProjectDrawer.open()
        },

        openEditDrawer(item) {
            this.editedIndex = this.projects.indexOf(item)
            this.editedItem=item
            this.$refs.editProjectDrawer.open()
        },


        add(project) {
            this.$emit('addProject', project)
        },

        update(project) {
            Object.assign(this.projects[this.editedIndex], project)
            this.$emit('editProject', this.editedItem)
        }

    }
};
</script>

<style>
#projectManagementApp {
    overflow: hidden;
    padding: 10px 20px;
}

select {
    width: auto;
}

#projectManagementApp .v-input input {
    margin-bottom: 0;
    border: 0;
    box-shadow: none;
}

#projectManagementApp .v-toolbar .v-input {
    margin-left: 18px;
}

#projectManagementApp .v-data-table {
 width: 100%;
}
</style>
