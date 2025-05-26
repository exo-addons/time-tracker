import TimeTrackingDrawer from '../components/timetracking/TimeTrackingDrawer.vue';

const components = {
  'time-tracking-drawer': TimeTrackingDrawer,
};

for (const key in components) {
  Vue.component(key, components[key]);
}

extensionRegistry.registerExtension('QuickAction', 'Extension', {
  id: 'timeTracker',
  icon: 'fa-clock',
  name: 'quickActions.timeTracker.name',
  description: 'quickActions.timeTracker.description',
  click: () => new Promise(resolve => {
    window.require(['SHARED/eXoVueI18n', 'SHARED/timeTrackingjs'], exoi18n => initTimeTrackerDrawer(exoi18n, resolve));
  }),
});

async function initTimeTrackerDrawer(exoi18n, callback) {
  console.debug('Initializing Time Tracker Quick Action Drawer');
  const appId = 'time-tracker-actions';
  if (!document.querySelector(`#${appId}`)) {
    const parent = document.createElement('div');
    parent.id = appId;
    document.querySelector('#vuetify-apps').appendChild(parent);

    console.debug('Create Vue App for Time Tracker Quick Action Drawer');
    await initTimeTrackerDrawerApp(appId, exoi18n);
  }
  console.debug('Dispatching quick-action-time-tracker-drawer event in else block');
  document.dispatchEvent(new CustomEvent('quick-action-time-tracker-drawer', {detail: callback}));
  callback();

}

function initTimeTrackerDrawerApp(appId, exoi18n) {
  const lang = eXo.env.portal.language;
  const resourceBundleName = 'locale.portlet.TimeTracker';
  const url = `${eXo.env.portal.context}/${eXo.env.portal.rest}/i18n/bundle/${resourceBundleName}-${lang}.json`;

  console.debug("Calling loadLanguageAsync for Time Tracker Quick Action Drawer with URL:", url);
  return new Promise(resolve => exoi18n.loadLanguageAsync(lang, url)
    .then(i18n => {
      Vue.createApp({
        template: `
          <time-tracking-drawer
            id="${appId}"
            ref="timeTrackingDrawerComponent"  />
        `,
        created() {
          console.debug('In create : Adding event listener for quick-action-time-tracker-drawer');
          document.addEventListener('quick-action-time-tracker-drawer', this.openDrawer);
        },
        async mounted() {
          console.debug('In mounted : dispatching hideTopBarLoading event');
          document.dispatchEvent(new CustomEvent('hideTopBarLoading'));
          resolve();
        },
        beforeDestroy() {
          document.removeEventListener('quick-action-time-tracker-drawer', this.openDrawer);
        },
        methods: {
          openDrawer() {
            console.debug('In openDrawer method: Opening Time Tracking Drawer', this);
            console.debug('In openDrawer method: Opening Time Tracking Drawer', this.$refs, this.$refs.timeTrackingDrawerComponent);
            this.$refs.timeTrackingDrawerComponent.open();
          },
          open() {
            console.debug('In open method: Opening Time Tracking Drawer but not good one');
          },
        },
        vuetify: Vue.prototype.vuetifyOptions,
        i18n,
      }, `#${appId}`, 'Time Tracker Quick Action');
    }));
}
