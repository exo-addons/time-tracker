<template>
<v-app class="VuetifyApp timeTrackingApp" color="transaprent" id="timeTrackingApp">
    <main>
            <v-container px-0 py-0>
      <v-layout class="transparent">
        <v-btn icon small color="transparent" class="uiApplicationIconButton" @click="openTimeTrackingDrawer()">
          <v-icon left>mdi-timer</v-icon>
        </v-btn>
      </v-layout>
    </v-container>
        <div :class="alert_type" class="alert" id v-if="alert">
            <i :class="alertIcon"></i>
            {{message}}
        </div>




        <!-- <time-tracking-drawer  ref="timeTrackingDrawer" /> -->
        <time-sheet />

    </main>
</v-app>
</template>

<script>
//import TimeTrackingDrawer from './timetracking/TimeTrackingDrawer.vue';
import TimeSheet from './timeSheet/TimeSheet.vue';
export default {
    components: {
       // TimeTrackingDrawer,
        TimeSheet
    },
    data: () => ({
        alert: false,
        message: '',
        alert_type: '',
        alertIcon: '',
        }),

    created() {
        this.initialize()
    },

    methods: {

        initialize() {
          //  this.getClients()
        },

        displaySusccessMessage(message) {
            this.message = message;
            this.alert_type = 'alert-success';
            this.alertIcon = 'uiIconSuccess';
            this.alert = true;
            setTimeout(() => (this.alert = false), 5000);
        },
        displayErrorMessage(message) {
            this.isUpdating = false;
            this.message = message;
            this.alert_type = 'alert-error';
            this.alertIcon = 'uiIconError';
            this.alert = true;
            setTimeout(() => (this.alert = false), 5000);
        },

    },
};
</script>

<style>

</style>
