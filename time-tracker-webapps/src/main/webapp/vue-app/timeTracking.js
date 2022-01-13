import timeTrackingApp from './components/timeTrackingApp.vue';
import './../css/main.less';

const vuetify = new Vuetify({
  dark: true,
  iconfont: 'mdi',
});
const lang = eXo && eXo.env && eXo.env.portal && eXo.env.portal.language;
const resourceBundleName = 'locale.portlet.TimeTracker';
const url = `${eXo.env.portal.context}/${eXo.env.portal.rest}/i18n/bundle/${resourceBundleName}-${lang}.json`;

exoi18n.loadLanguageAsync(lang, url).then(i18n => {
  new Vue({
    render: (h) => h(timeTrackingApp),
    i18n,
    vuetify
  }).$mount('#timeTrackingApp');
});