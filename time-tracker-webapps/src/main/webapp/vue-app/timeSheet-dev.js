import timeSheetApp from './components/timeSheetApp.vue';
Vue.use(Vuetify);
new Vue({
  el: '#timeSheetApp',
  render: (h) => h(timeSheetApp),
});
