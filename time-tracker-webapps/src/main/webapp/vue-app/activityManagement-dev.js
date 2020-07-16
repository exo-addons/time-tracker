import timeTrackingApp from './components/timeTrackingApp.vue';
Vue.use(Vuetify);
const vueInstance = new Vue({
  el: '#timeTrackingApp',
  render: (h) => h(timeTrackingApp),
});
