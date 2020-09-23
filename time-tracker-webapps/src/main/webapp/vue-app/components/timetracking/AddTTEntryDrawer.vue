<template>
<exo-drawer ref="addTTEntryDrawer" right class="">
    <template slot="title">
        Add Entry
    </template>
    <template slot="content">
        <div>

            <v-form ref="form">

                <v-row>
                    <v-label for="activity">
                        Activity
                    </v-label>

                    <v-autocomplete v-model="activityRecord.activity" :items="activities" menu-props="closeOnClick" class="input-block-level ignore-vuetify-classes my-3" outlined dense chips small-chips item-text="label" item-value="id"></v-autocomplete>

                </v-row>

                <v-row>
                    <v-label for="description">
                        Description
                    </v-label>
                    <input ref="description_" v-model="activityRecord.description" type="text" name="description" class="input-block-level ignore-vuetify-classes my-3" />
                </v-row>
                <v-row>
                    <v-label for="time">
                        Time spend
                    </v-label>
                    <input ref="time" v-model="activityRecord.time" type="text" name="time" class="input-block-level ignore-vuetify-classes my-3" />
                </v-row>
                <v-row>
                    <v-label for="location">
                        Location
                    </v-label>
                    <select v-model="activityRecord.location" name="location" class="input-block-level ignore-vuetify-classes my-3">
                        <option v-for="item in locations" :key="item" :value="item">
                            {{ item}}
                        </option>
                    </select>
                </v-row>
                <v-row>
                    <v-label for="office">
                        Office
                    </v-label>
                    <select v-model="activityRecord.office" name="office" class="input-block-level ignore-vuetify-classes my-3">
                        <option v-for="item in offices" :key="item" :value="item">
                            {{item}}
                        </option>
                    </select>
                </v-row>
                <v-row>
                    <v-label for="projectVersion"> Project Version </v-label>
                    <input ref="projectVersion" v-model="activityRecord.projectVersion" type="text" name="projectVersion" class="input-block-level ignore-vuetify-classes my-3" />
                </v-row>
                <v-row>
                    <v-label for="salesOrder">
                        Sales Order
                    </v-label>
                    <select v-model="activityRecord.salesOrder" name="salesOrder" class="input-block-level ignore-vuetify-classes my-3">
                        <option v-for="item in salesOrders" :key="item.id" :value="item">
                            {{ item.name}}
                        </option>
                    </select>
                </v-row>

            </v-form>

        </div>
    </template>
    <template slot="footer">
        <div class="d-flex">
            <v-spacer />
            <v-btn class="btn mr-2" @click="cancel()">
                <template>
                    Cancel
                </template>
            </v-btn>
            <v-btn class="btn btn-primary" @click="save()">
                <template>
                    Save
                </template>
            </v-btn>
        </div>
    </template>
</exo-drawer>
</template>

<script>
export default {
    props: ['activities'],
    data: () => ({
        activityRecord: {},
        salesOrders: [],
        locations: ["Home", "eXo TN", "eXo FR", "eXo", "Ext"],
        offices: ["FR", "TN", "LX", "VN", "UA"]
    }),
    created() {

        //  this.initialize()
    },

    methods: {
        focusInput() {
            this.$refs.description_.focus();
        },

        save() {
            this.$emit('save', this.activityRecord)
            this.activityRecord = {}
            this.$refs.addTTEntryDrawer.close()
        },
        cancel() {
            this.$refs.addTTEntryDrawer.close()
        },
        open() {
            this.$refs.addTTEntryDrawer.open()
          //    this.focusInput();
        },

    }
}
</script>

<style>
.drawerContent {
    padding: 15px 27px;
}
</style>
