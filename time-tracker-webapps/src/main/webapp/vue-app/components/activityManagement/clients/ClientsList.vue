<template>
    <div>
        <v-flex>
            <v-data-table :headers="headers" :items="clients" :search="search" sort-by="label" class="elevation-1">
                <template v-slot:top>
                    <v-toolbar flat color="white">
                        <v-toolbar-title>Client list</v-toolbar-title>
                        <v-divider class="mx-4" inset vertical></v-divider>
                        <v-spacer></v-spacer>
                        <v-divider class="mx-4" inset vertical></v-divider>
                        <v-text-field v-model="search" placeholder="Filter" prepend-inner-icon="fa-filter" class="inputFilter pa-0 mr-3 my-auto"  clearable />  
                        <button class="btn btn-primary pull-left" type="button" @click="openAddClientDrawer">
                                    <i class="uiIconSocSimplePlus uiIconSocWhite"></i> Add Client
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
                <template v-slot:no-data>No clients</template>
            </v-data-table>
        </v-flex>
        <add-client-drawer ref="addClientDrawer" v-on:save="add" />
        <edit-client-drawer ref="editClientDrawer" :client="editedItem" v-on:save="update" />
    </div>
</template>

<script>
import addClientDrawer from './AddClientDrawer.vue';
import editClientDrawer from './EditClientDrawer.vue';
export default {
    components: {
        addClientDrawer,
editClientDrawer,
    },
    props:['clients'],
    data: () => ({
        search: '',       
        valid: true,        
        editedIndex: -1,
        editedItem: {
            code: '',
            label: '',
        },
        defaultItem: {
            code: '',
            label: '',
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
            const index = this.clients.indexOf(item)
            this.clients.splice(index, 1)
            this.$emit('delete', item)
        },
        openAddClientDrawer() {
            this.$refs.addClientDrawer.open()
        },

        openEditDrawer(item) {
            this.editedIndex = this.clients.indexOf(item)
            this.editedItem=item
            this.$refs.editClientDrawer.open()
        },


        add(client) {
            this.$emit('addClient', client)
        },

        update(client) {
            Object.assign(this.clients[this.editedIndex], client)
            this.$emit('editClient', this.editedItem)
        }

    }
};
</script>

<style>
#clientManagementApp {
    overflow: hidden;
    padding: 10px 20px;
}

select {
    width: auto;
}

#clientManagementApp .v-input input {
    margin-bottom: 0;
    border: 0;
    box-shadow: none;
}

#clientManagementApp .v-toolbar .v-input {
    margin-left: 18px;
}

#clientManagementApp .v-data-table {
 width: 100%;
}
</style>
