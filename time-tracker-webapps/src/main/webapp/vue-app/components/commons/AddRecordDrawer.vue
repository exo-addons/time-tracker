<template>
<exo-drawer ref="addTTEntryDrawer" right class="">
    <template slot="title">
        Add Entry
    </template>
    <template slot="content">
        <div>

            <v-form ref="form">
                <v-row align="center" justify="center">

                    <v-menu v-model="menu2" :close-on-content-click="false" :nudge-right="40" transition="scale-transition" offset-y min-width="290px">
                        <template v-slot:activator="{ on, attrs }">
                            <v-text-field v-model="date" centered prepend-icon="event" readonly v-bind="attrs" v-on="on"></v-text-field>
                        </template>
                        <v-date-picker v-model="date" @input="menu2 = false"></v-date-picker>
                    </v-menu>
                </v-row>

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
                    <input id="desc" ref="description" v-model="activityRecord.description" type="text" name="description" class="input-block-level ignore-vuetify-classes my-3" />
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
            <v-btn :disabled="isDisabled" class="btn btn-primary" @click="save()">
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
        date: new Date().toISOString().substr(0, 10),
        menu2: false,
        activityRecord: {},
        salesOrders: [],
        locations: ["Home", "eXo TN", "eXo FR", "eXo", "Ext"],
        offices: ["FR", "TN", "LX", "VN", "UA"]
    }),
     computed: {
         isDisabled: function(){
                return !(this.isNotEmpty(this.activityRecord.office)&&this.isNotEmpty(this.activityRecord.location)&&this.isNotEmpty(this.activityRecord.time)&&this.activityRecord.time>=0&&this.activityRecord.time<=8)
                }
        },
    created() {

        //  this.initialize()
    },

    methods: {
                getLastActivityRecord() {
            return new Promise((resolve, reject) => {

                fetch(`/portal/rest/timetracker/activityRecordrecordsmgn/activityrecord/list?search=&activity=0&type=0&subType=0&activityCode=0&subActivityCode=0&client=0&project=0&feature=0&fromDate=&toDate=&location=&office=&sortby=id&sortdesc=true&page=0&limit=1&export=false`, {
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

        isNotEmpty(str){
              return(str!=null && str!=="")
            },

        save() {
            this.activityRecord.activityDate = this.date
            this.activityRecord.activity = {id:this.activityRecord.activity}
            this.$emit('save', this.activityRecord)
            this.activityRecord = {}
            this.$refs.addTTEntryDrawer.close()
        },
        cancel() {
            this.$refs.addTTEntryDrawer.close()
        },
        open() {           
        this.getLastActivityRecord()
            .then(data => {
                if (data.items.length > 0) {
                    this.activityRecord = data.items[0]
                    this.activityRecord.time = null
                }
            })
            this.$refs.addTTEntryDrawer.open()
            window.setTimeout(() => this.$refs.description.focus(), 200);
        },

    }
}
</script>
