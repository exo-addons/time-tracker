<template>
<exo-drawer ref="addTTEntryDrawer" right class="addEntryDrawer">
    <template slot="title">
        Add Entry
    </template>
    <template slot="content">
        <div>

            <v-form ref="form">

                <div align="center" justify="center" v-if="showDPicker">
                    <v-menu v-model="menu2" :close-on-content-click="false" :nudge-right="40" transition="scale-transition" offset-y min-width="290px">
                        <template v-slot:activator="{ on, attrs }">
                            <v-text-field v-model="date" centered prepend-icon="event" readonly v-bind="attrs" v-on="on"></v-text-field>
                        </template>
                        <v-date-picker v-model="date" @input="menu2 = false"></v-date-picker>
                    </v-menu>
                </div>

                <div>
                    <v-label for="description">
                        Description
                    </v-label>
                    <input id="desc" ref="description" v-model="activityRecord.description" type="text" name="description" placeholder="What are you working on ?" class="input-block-level ignore-vuetify-classes my-3" />
                </div>

                <div>
                    <v-label for="time">
                        Time spent (hours)
                    </v-label>
                    <input ref="time" v-model="activityRecord.time" type="text" name="time" class="input-block-level ignore-vuetify-classes my-3" />
                </div>

                <div>
                    <v-label for="activity">
                        Activity
                    </v-label>

                    <v-autocomplete v-model="activityRecord.activity" :items="activities" menu-props="closeOnClick" class="input-block-level ignore-vuetify-classes my-3" outlined dense chips small-chips item-text="label" item-value="id"></v-autocomplete>

                </div>

                <div>
                    <v-label for="projectVersion"> Project Version </v-label>
                    <input ref="projectVersion" v-model="activityRecord.projectVersion" type="text" name="projectVersion" class="input-block-level ignore-vuetify-classes my-3" />
                </div>
                <div v-if="salesOrders.length>0">
                    <v-label for="salesOrder">
                        Sales Order
                    </v-label>
                    <select  v-model="activityRecord.salesOrder" name="salesOrder" class="input-block-level ignore-vuetify-classes my-3">
                        <option v-for="item in salesOrders" :key="item.id" :value="item">
                            {{ item.name}}
                        </option>
                    </select>
                </div>
                <div>
                    <v-label for="location">
                        Location
                    </v-label>
                    <select v-model="activityRecord.location" name="location" class="input-block-level ignore-vuetify-classes my-3">
                        <option v-for="item in locations" :key="item.code" :value="item.code">
                            {{ item.label}}
                        </option>
                    </select>
                </div>

                <div>
                    <v-label for="office">
                        Office
                    </v-label>
                    <select v-model="activityRecord.office" name="office" class="input-block-level ignore-vuetify-classes my-3">
                        <option v-for="item in offices" :key="item.code" :value="item.code">
                            {{item.label}}
                        </option>
                    </select>
                </div>

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
    props: ['activities','locations','offices'],
    data: () => ({
        date: new Date().toISOString().substr(0, 10),
        menu2: false,
        activityRecord: {},
        salesOrders: [],
        showDPicker:false,
    }),
     computed: {
         isDisabled: function(){
                return !(this.isNotEmpty(this.activityRecord.office)&&this.isNotEmpty(this.activityRecord.location)&&this.isNotEmpty(this.activityRecord.time)&&this.activityRecord.time>=0&&this.activityRecord.time<=8)
                }
        },
    created() {

        //  this.initialize()
    },

    watch:{
    'activityRecord.activity'(newVal){
        if(newVal && !newVal.id){
            newVal=this.activities.find(act => act.id === newVal)
        }
        if(newVal && newVal.project && newVal.project.client){
            this.salesOrders=newVal.project.client.salesOrders         
        }else{
            this.salesOrders=[]
        }
    }
   },

    methods: {
        getLastActivityRecord() {
            return new Promise((resolve, reject) => {

                fetch(`/portal/rest/timetracker/activityRecordrecordsmgn/activityrecord/last`, {
                        credentials: 'include',
                    })
                    .then((resp) => resp.json())
                    .then((resp) => {
                        const item = resp
                        this.loading = false
                        resolve({
                            item
                        })

                    })
            });

        },


        isNotEmpty(str){
              return(str!=null && str!=="")
            },
        focusInput() {
            this.$refs.description.focus();
        },

        save() {
            this.activityRecord.activityDate = this.date
            if(!this.activityRecord.activity.id) {
            this.activityRecord.activity = {id:this.activityRecord.activity}
            }
            this.$emit('save', this.activityRecord)
            this.activityRecord = {}
            this.$refs.addTTEntryDrawer.close()
        },
        cancel() {
            this.$refs.addTTEntryDrawer.close()
        },
        open(timeRecord,showDPicker) {
        if(showDPicker){
            this.showDPicker=true
        }       
        if(!this.activityRecord.time){
        this.getLastActivityRecord()
        .then(data => {
            if (data.item) {
                this.activityRecord = data.item
                this.activityRecord.time = null
                this.activityRecord.salesOrder = null
            }
        })
        }else{
            this.activityRecord.time = null
        }
        if(timeRecord){
        this.activityRecord.activityTime=timeRecord.activityTime
        this.activityRecord.activityDate=timeRecord.activityDate
        this.date=timeRecord.activityDate
        }
        this.$refs.addTTEntryDrawer.open()
        window.setTimeout(() => this.$refs.description.focus(), 200);
        },

    }
}
</script>
