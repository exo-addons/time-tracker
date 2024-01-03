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
            class="uiApplicationIconButton"
            @click="openTimeTrackingDrawer()">
            <v-icon class="timeTrackingIcon" size="22">
              fa-clock
            </v-icon>
          </v-btn>
        </v-layout>
      </v-container>
      
      <time-tracking-drawer ref="timeTrackingDrawer" />
    </main>
  </v-app>
</template>

<script>
import TimeTrackingDrawer from './timetracking/TimeTrackingDrawer.vue';
export default {
  components: {
    TimeTrackingDrawer
  },
  data: () => ({
    message: '',
  }),
  
  methods: {
    openTimeTrackingDrawer() {
      this.$refs.timeTrackingDrawer.open();
    },
    displaySusccessMessage(message) {
      document.dispatchEvent(new CustomEvent('alert-message', {
        detail: {
          alertMessage: message,
          alertType: 'success',
        }
      }));
    },
    displayErrorMessage(message) {
      document.dispatchEvent(new CustomEvent('alert-message', {
        detail: {
          alertMessage: message,
          alertType: 'error'
        }
      }));
    },
  },
};
</script>
