import timeTrackingApp from './components/timeTrackingApp.vue';
Vue.use(Vuetify);
new Vue({
  el: '#timeTrackingApp',
  render: (h) => h(timeTrackingApp),
});
