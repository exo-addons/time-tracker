import timeSheetApp from './components/timeSheetApp.vue';
import './../css/main.less';

Vue.use(Vuetify);


const vuetify = new Vuetify({
    dark: true,
    iconfont: 'mdi',
});

$(document).ready(() => {
    const lang = eXo && eXo.env && eXo.env.portal && eXo.env.portal.language;
    const url = `${eXo.env.portal.context}/${eXo.env.portal.rest}/i18n/bundle/locale.addon.timeTracking-${lang}.json`;
  
    exoi18n.loadLanguageAsync(lang, url).then(i18n => {
        const vueApp = new Vue({
            render: (h) => h(timeSheetApp),
            i18n,
            vuetify
        }).$mount('#timeSheetApp');
    });
  });