<template>
<exo-drawer ref="filterDrawer" right class="">
    <template slot="title">
        Filter
    </template>
    <template slot="content">
        <div>
            <form ref="form1">

                
                <v-row>
                    <v-menu ref="menu1" v-model="menu1" :close-on-content-click="false" :return-value.sync="fromDate" transition="scale-transition" offset-y min-width="290px">
                        <template v-slot:activator="{ on }">
                            <v-label for="fromDate">
                                From date
                            </v-label>
                            <input ref="fromDate" v-model="fromDate" type="text" name="fromDate" v-on="on" class="input-block-level ignore-vuetify-classes my-3" />
                        </template>
                        <v-date-picker v-model="fromDate" no-title scrollable>
                            <v-spacer></v-spacer>
                            <v-btn text color="primary" @click="menu1 = false">Cancel</v-btn>
                            <v-btn text color="primary" @click="$refs.menu1.save(fromDate)">OK</v-btn>
                        </v-date-picker>
                    </v-menu>
                </v-row>
                <v-row>
                    <v-menu ref="menu2" v-model="menu2" :close-on-content-click="false" :return-value.sync="toDate" transition="scale-transition" offset-y min-width="290px">
                        <template v-slot:activator="{ on }">

                            <v-label for="toDate">
                                To date
                            </v-label>
                            <input ref="toDate" v-model="toDate" type="text" name="toDate" v-on="on" class="input-block-level ignore-vuetify-classes my-3" />

                        </template>
                        <v-date-picker v-model="toDate" no-title scrollable>
                            <v-spacer></v-spacer>
                            <v-btn text color="primary" @click="menu2 = false">Cancel</v-btn>
                            <v-btn text color="primary" @click="$refs.menu2.save(toDate)">OK</v-btn>
                        </v-date-picker>
                    </v-menu>
                </v-row>

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

    props: ['activities', 'types', 'subTypes', 'activityCodes', 'subActivityCodes', 'clients', 'projects', 'features'],

    data: () => ({
        fromDate: "",
        toDate: "",
        menu1: null,
        menu2: null,
        activity: "",
        type: "",
        subType: "",
        activityCode: "",
        subActivityCode: "",
        client: "",
        project: "",
        feature: "",
        locations: ["Home", "eXo TN", "eXo FR", "eXo", "Ext"],
        offices: ["FR", "TN", "LX", "VN", "UA"]
    }),

    methods: {
        reset() {
            this.fromDate = ""
            this.toDate = ""
            this.menu1 = null
            this.menu2 = null
            this.activity = ""
            this.type = ""
            this.subType = ""
            this.activityCode = ""
            this.subActivityCode = ""
            this.client = ""
            this.project = ""
            this.feature = ""
            this.location = ""
            this.office = ""
        },
        aplyFilter() {
            const filter_ = {
                fromDate: this.fromDate,
                toDate: this.toDate,
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
        cancel() {
            this.$refs.filterDrawer.close();
        },
        open() {

            this.$refs.filterDrawer.open();

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
</style>
