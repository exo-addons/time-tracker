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
    message: '',
  }),
  methods: {
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
