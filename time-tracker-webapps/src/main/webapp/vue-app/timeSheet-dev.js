import timeSheetApp from './components/timeSheetApp.vue';
Vue.use(Vuetify);
const vueInstance = new Vue({
  el: '#timeSheetApp',
  render: (h) => h(timeSheetApp),
});
