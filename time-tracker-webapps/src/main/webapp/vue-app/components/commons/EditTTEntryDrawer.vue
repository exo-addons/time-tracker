<template>
<exo-drawer ref="editDrawer" right class="">
    <template slot="title">
        Edit Entry
    </template>
    <template slot="content">
        <div>

            <v-form ref="form">

                 <div>
                    <v-label for="description">
                        description
                    </v-label>
                    <input ref="description" v-model="activityRecord.description" type="text" name="description" placeholder="What are you working on ?" class="input-block-level ignore-vuetify-classes my-3" />
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
                
                <div v-if="activityRecord.project || selectedActivity && (!selectedActivity.project || (selectedActivity.project && (selectedActivity.project.code==='<PRJ>'||selectedActivity.project.code==='<EXO>')))">
                    <v-label for="project">
                        Project
                    </v-label>
                    <select  v-model="activityRecord.project" name="project" class="input-block-level ignore-vuetify-classes my-3">
                        <option v-for="item in projects" :key="item.id" :value="item">
                            {{ item.displayLabel}}
                        </option>
                    </select>
               </div>
                <div v-if="activityRecord.client || activityRecord.project || selectedActivity && ( (selectedActivity.project && selectedActivity.project.client && (selectedActivity.project.client.code==='<CLNT>')))">
                    <v-label for="client">
                        Client
                    </v-label>
                    <select  v-model="activityRecord.client" name="client" class="input-block-level ignore-vuetify-classes my-3">
                        <option v-for="item in clients" :key="item.id" :value="item">
                            {{ item.displayLabel}}
                        </option>
                    </select>
                </div>
                <div>
                    <v-label for="projectVersion">
                        Project Version
                    </v-label>
                    <input ref="projectVersion" v-model="activityRecord.projectVersion" type="text" name="projectVersion" class="input-block-level ignore-vuetify-classes my-3" />
                </div>
                <div>
                    <v-label for="salesOrder">
                        Sales Order
                    </v-label>
                    <select v-model="so" name="salesOrder" class="input-block-level ignore-vuetify-classes my-3">
                        <option v-for="item in salesOrders" :key="item.id" :value="item.id">
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
    props: ['activities','locations','offices','clients','projects'],
    data: () => ({
        salesOrders: [],
        activityRecord:{},
        so:"",
        selectedActivity:{}
    }),
    created() {
        //  this.initialize()
    },

    watch:{
        'activityRecord.activity'(newVal){
            if(newVal && !newVal.id){
                newVal=this.activities.find(act => act.id === newVal)
                this.selectedActivity=newVal
            }
            if(newVal && newVal.project && newVal.project.client){
                this.salesOrders=newVal.project.client.salesOrders         
            }else{
                this.salesOrders=[]
            }
        }
    },
    methods: {

        save() {
            if(!this.activityRecord.activity.id) {
            this.activityRecord.activity = {id:this.activityRecord.activity}
            }
            if(this.so) {
            this.activityRecord.salesOrder = {id:this.so}
            }
            this.$emit('save', this.activityRecord)
            this.$refs.editDrawer.close()
        },
        cancel() {
            this.$refs.editDrawer.close()
        },
        open(activityRecord) {
            this.salesOrders=activityRecord.activity.project.client.salesOrders    
            this.activityRecord=activityRecord
            this.selectedActivity=activityRecord.activity
            if(this.activityRecord.salesOrder){
                this.so=this.activityRecord.salesOrder.id
            }else{
                this.so=""
            }
            this.$refs.editDrawer.open()
        },
    }
}
</script>
