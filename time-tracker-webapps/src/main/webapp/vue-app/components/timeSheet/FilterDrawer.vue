<template>
<exo-drawer ref="filterDrawer" right class="">
    <template slot="title">
        Filter
    </template>
    <template slot="titleIcons">
        <v-menu offset-y>
            <template v-slot:activator="{ on, attrs }">
                <v-btn text small v-bind="attrs" v-on="on">
                    Saved Filters
                </v-btn>
            </template>
            <v-list>
                <v-list-item v-for="(item, index) in filters" :key="index">
                    <v-list-item-title  @click="setFilter(item)" class="pointer">{{ item.filter.name }}</v-list-item-title>
                      <v-list-item-action>
              <v-btn icon               
                @click="deleteFilter(item)"
              >
                <v-icon>mdi-delete</v-icon>
              </v-btn>
            </v-list-item-action>
                </v-list-item>
            </v-list>
        </v-menu>

        <v-menu v-model="menu" :close-on-content-click="false" :nudge-width="200" offset-x>
            <template v-slot:activator="{ on, attrs }">
                <v-btn text small v-bind="attrs" v-on="on">
                    <v-icon>mdi-plus</v-icon>
                    Add Filter
                </v-btn>

            </template>

            <v-card>

                <v-list>
                    <v-list-item>
                        <v-list-item-content>
                            <v-label for="filterName">
                                Filter Name
                            </v-label>
                            <input ref="filterName" v-model="filterName" type="text" name="filterName" class="input-block-level ignore-vuetify-classes my-3" />

                        </v-list-item-content>

                    </v-list-item>
                </v-list>

                <v-card-actions>
                    <v-spacer></v-spacer>

                    <v-btn text @click="menu = false">Cancel</v-btn>
                    <v-btn color="primary" text @click="menu = false;saveFilter()">Save</v-btn>
                </v-card-actions>
            </v-card>
        </v-menu>

    </template>
    <template slot="content">
        <div>
            <form ref="form1">

                <v-row>
                    <v-label for="activity">
                        Activity
                    </v-label>
                    <select v-model="activity" name="activity" class="input-block-level ignore-vuetify-classes my-3">
                        <option v-for="item in activities" :key="item.id" :value="item.id">
                            {{ item.label}}
                        </option>
                    </select>
                </v-row>
                <v-row>
                    <v-label for="location">
                        Location
                    </v-label>
                    <select v-model="location" name="location" class="input-block-level ignore-vuetify-classes my-3">
                        <option v-for="item in locations" :key="item" :value="item">
                            {{ item}}
                        </option>
                    </select>
                </v-row>
                <v-row>
                    <v-label for="office">
                        Office
                    </v-label>
                    <select v-model="office" name="office" class="input-block-level ignore-vuetify-classes my-3">
                        <option v-for="item in offices" :key="item" :value="item">
                            {{ item}}
                        </option>
                    </select>
                </v-row>
                <v-row>
                    <v-label for="type">
                        Type
                    </v-label>
                    <select v-model="type" name="type" class="input-block-level ignore-vuetify-classes my-3">
                        <option v-for="item in types" :key="item.id" :value="item.id">
                            {{ item.label}}
                        </option>
                    </select>
                </v-row>

                <v-row>
                    <v-label for="subType">
                        Sub type
                    </v-label>
                    <select v-model="subType" name="subType" class="input-block-level ignore-vuetify-classes my-3">
                        <option v-for="item in subTypes" :key="item.id" :value="item.id">
                            {{ item.label}}
                        </option>
                    </select>
                </v-row>

                <v-row>
                    <v-label for="activityCode">
                        Activity Code
                    </v-label>
                    <select v-model="activityCode" name="activityCode" class="input-block-level ignore-vuetify-classes my-3">
                        <option v-for="item in activityCodes" :key="item.id" :value="item.id">
                            {{ item.label}}
                        </option>
                    </select>
                </v-row>

                <v-row>
                    <v-label for="subActivityCode">
                        Sub Activity Code
                    </v-label>
                    <select v-model="subActivityCode" name="subActivityCode" class="input-block-level ignore-vuetify-classes my-3">
                        <option v-for="item in subActivityCodes" :key="item.id" :value="item.id">
                            {{ item.label}}
                        </option>
                    </select>
                </v-row>

                <v-row>
                    <v-label for="client">
                        Client
                    </v-label>
                    <select v-model="client" name="client" class="input-block-level ignore-vuetify-classes my-3">
                        <option v-for="item in clients" :key="item.id" :value="item.id">
                            {{ item.label}}
                        </option>
                    </select>
                </v-row>

                <v-row>
                    <v-label for="project">
                        Project
                    </v-label>
                    <select v-model="project" name="project" class="input-block-level ignore-vuetify-classes my-3">
                        <option v-for="item in projects" :key="item.id" :value="item.id">
                            {{ item.label}}
                        </option>
                    </select>
                </v-row>

                <v-row>
                    <v-label for="feature">
                        Feature
                    </v-label>
                    <select v-model="project" name="feature" class="input-block-level ignore-vuetify-classes my-3">
                        <option v-for="item in features" :key="item.id" :value="item.id">
                            {{ item.label}}
                        </option>
                    </select>
                </v-row>

            </form>
        </div>
    </template>
    <template slot="footer">
        <div class="d-flex">

            <v-spacer />
            <v-btn class="btn mr-2" @click="reset()">
                <template>
                    Reset
                </template>
            </v-btn>
            <v-btn class="btn mr-2" @click="cancel()">
                <template>
                    Cancel
                </template>
            </v-btn>
            <v-btn class="btn btn-primary" @click="aplyFilter()">
                <template>
                    Apply
                </template>
            </v-btn>
        </div>
    </template>
</exo-drawer>
</template>

<script>
export default {

    props: ['activities', 'types', 'subTypes', 'activityCodes', 'subActivityCodes', 'clients', 'projects', 'features', 'filters'],

    data: () => ({
        showName: false,
        menu: null,
        activity: 0,
        type: 0,
        subType: 0,
        activityCode: 0,
        subActivityCode: 0,
        client: 0,
        project: 0,
        feature: 0,
        location: "",
        office: "",
        locations: ["", "Home", "eXo TN", "eXo FR", "eXo", "Ext"],
        offices: ["", "FR", "TN", "LX", "VN", "UA"],
        filter: {},
        filterName: ""
    }),

    methods: {
        reset() {
            this.activity = 0
            this.type = 0
            this.subType = 0
            this.activityCode = 0
            this.subActivityCode = 0
            this.client = 0
            this.project = 0
            this.feature = 0
            this.location = ""
            this.office = ""
        },
        aplyFilter() {
            const filter_ = {
                activity: this.activity,
                type: this.type,
                subType: this.subType,
                activityCode: this.activityCode,
                subActivityCode: this.subActivityCode,
                client: this.client,
                project: this.project,
                feature: this.feature,
                location: this.location,
                office: this.office
            }
            this.$emit('addFilter', filter_);
            this.$refs.filterDrawer.close();
        },
        setFilter(filter) {
            this.activity = filter.fields.activity
            this.type = filter.fields.type
            this.subType = filter.fields.subType
            this.activityCode = filter.fields.activityCode
            this.subActivityCode = filter.fields.subActivityCode
            this.client = filter.fields.client
            this.project = filter.fields.project
            this.feature = filter.fields.feature
            this.location = filter.fields.location
            this.office = filter.fields.office
        },
        cancel() {
            this.$refs.filterDrawer.close();
        },
        open() {

            this.$refs.filterDrawer.open();

        },

        saveFilter() {

            const filter_ = {
                name: this.filterName,
                activity: this.activity,
                type: this.type,
                subType: this.subType,
                activityCode: this.activityCode,
                subActivityCode: this.subActivityCode,
                client: this.client,
                project: this.project,
                feature: this.feature,
                location: this.location,
                office: this.office
            }
            this.$emit('saveFilter', filter_);
        },
        deleteFilter(item) {

              this.$emit('deleteFilter', item);
        },

    }
}
</script>

<style>
.drawerContent {
    padding: 15px 27px;
}

.sliderValue .v-text-field__slot {
    max-width: 50px;
}

.pointer {cursor: pointer;}
</style>
