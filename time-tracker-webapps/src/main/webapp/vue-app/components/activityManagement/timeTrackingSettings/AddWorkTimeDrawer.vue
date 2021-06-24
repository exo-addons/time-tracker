<template>
<exo-drawer ref="addWorkTimeDrawer" right class="">
    <template slot="title">
        Add WorkTime
    </template>
    <template slot="content">
        <div>
            <v-form ref="form" v-model="valid">

                <div>
                    <v-menu ref="menu" v-model="menu" :close-on-content-click="false" :return-value.sync="date" transition="scale-transition" offset-y min-width="290px">
                                    <template v-slot:activator="{ on }">
                                        <v-text-field v-model="dateRangeText" prepend-icon="event" readonly v-on="on"></v-text-field>
                                    </template>
                                    <v-date-picker v-model="date" range no-title scrollable>
                                        <v-spacer></v-spacer>
                                        <v-btn text color="primary" @click="menu = false">Cancel</v-btn>
                                        <v-btn text color="primary" @click="$refs.menu.save(date),setDates()">OK</v-btn>
                                    </v-date-picker>
                    </v-menu>
                </div>
            
                <div>
                    <v-label for="office">
                        Office
                    </v-label>
                    <select v-model="workTime.office" name="office" class="input-block-level ignore-vuetify-classes my-3">
                        <option v-for="item in offices" :key="item.id" :value="item">
                            {{ item.label}}
                        </option>
                    </select>
                </div>
                <div>
                    <v-label for="hours">
                        Label
                    </v-label>
                    <input ref="hours" v-model="workTime.hoursNumber" type="text" name="hoursNumber" class="input-block-level ignore-vuetify-classes my-3" />
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
    props:['offices'],

    data: () => ({
        date: [],
        dateRangeText: '',
        menu: false,
        workTime: {
            from: '',
            to: '',
            office: '',
            hoursNumber: '',
        }
    }),
    
    ceated(){
        const date = new Date();
        const firstDay = new Date(date.getFullYear(), date.getMonth(), 2);
        const lastDay = new Date(date.getFullYear(), date.getMonth() + 1, 1);
        this.date = [firstDay.toISOString().substr(0, 10), lastDay.toISOString().substr(0, 10)]
        this.dateRangeText = this.date.join(' ~ ')
        this.fromDate = this.date[0]
        this.toDate = this.date[1]

    },
    watch: {

        date(val) {
            console.log(val)
            this.dateRangeText = this.date.join(' ~ ')
            this.fromDate = this.date[0]
            this.toDate = this.date[0]
            if (typeof this.date[1] !== 'undefined') {
                this.toDate = this.date[1]
            }
        },

    },

    methods: {
        save() {
            this.workTime.from=this.fromDate
            this.workTime.to=this.toDate
            this.$emit('save', this.workTime)
            this.workTime = {code: '',label: ''}
            this.$refs.addWorkTimeDrawer.close()
        },
        cancel() {
            this.workTime = {code: '',label: ''}
            this.$refs.addWorkTimeDrawer.close()
        },
        open() {
            this.workTime = {code: '',label: ''}
            this.$refs.addWorkTimeDrawer.open()
        },

    }
}
</script>
