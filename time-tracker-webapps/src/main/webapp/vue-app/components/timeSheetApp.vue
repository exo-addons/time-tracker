<template>
  <v-app
    id="timeTrackingApp"
    class="VuetifyApp timeTrackingApp"
    color="transaprent">
    <main>
      <v-container px-0 py-0>
        <v-layout class="transparent">
          <v-btn
            icon
            small
            color="transparent"
            class="uiApplicationIconButton"
            @click="openTimeTrackingDrawer()">
            <v-icon left>mdi-timer</v-icon>
          </v-btn>
        </v-layout>
      </v-container>
      <div
        v-if="alert"
        id
        :class="alert_type"
        class="alert">
        <i :class="alertIcon"></i>
        {{ message }}
      </div>
      <time-sheet />
    </main>
  </v-app>
</template>

<script>
import TimeSheet from './timeSheet/TimeSheet.vue';
export default {
  components: {
    TimeSheet
  },
  data: () => ({
    alert: false,
    message: '',
    alert_type: '',
    alertIcon: '',
  }),
  created() {
    this.initialize();
  },
  methods: {
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
