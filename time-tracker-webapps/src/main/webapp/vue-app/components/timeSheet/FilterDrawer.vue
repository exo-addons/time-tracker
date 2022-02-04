<template>
  <exo-drawer
    ref="filterDrawer"
    right
    class="timeSheetFilterDrawer">
    <template slot="title">
      {{ $t('exo.timeTracker.timeSheet.filterDrawer.toolbarTitle') }}
    </template>
    <template slot="titleIcons">
      <v-menu offset-y ref="filterDrawermenuFilter">
        <template v-slot:activator="{ on, attrs }">
          <v-btn
            text
            small
            v-bind="attrs"
            v-on="on">
            {{
              $t(
                'exo.timeTracker.timeSheet.filterDrawer.buttonLabelSavedFilters'
              )
            }}
          </v-btn>
        </template>
        <v-list ref="filterDrawerlistFilter" class="filterDrawerlistFilter">
          <v-list-item v-for="(item, index) in filters" :key="index">
            <v-list-item-title class="pointer" @click="setFilter(item)">
              {{ item.filter.name }}
            </v-list-item-title>
            <v-list-item-action>
              <v-btn icon @click="deleteFilter(item)">
                <v-icon>mdi-delete</v-icon>
              </v-btn>
            </v-list-item-action>
          </v-list-item>
        </v-list>
      </v-menu>
      <v-menu
        v-model="menu"
        :close-on-content-click="false"
        :nudge-width="200"
        offset-x>
        <template v-slot:activator="{ on, attrs }">
          <v-btn
            text
            small
            v-bind="attrs"
            v-on="on">
            <v-icon>mdi-plus</v-icon>
            {{
              $t('exo.timeTracker.timeSheet.filterDrawer.buttonLabelAddFilter')
            }}
          </v-btn>
        </template>
        <v-card ref="cardDrawerFilter">
          <v-list>
            <v-list-item>
              <v-list-item-content>
                <v-label for="filterName">
                  {{
                    $t('exo.timeTracker.timeSheet.filterDrawer.LabelFilterName')
                  }}
                </v-label>
                <input
                  ref="filterName"
                  v-model="filterName"
                  type="text"
                  name="filterName"
                  class="input-block-level ignore-vuetify-classes my-3">
              </v-list-item-content>
            </v-list-item>
          </v-list>
          <v-card-actions>
            <v-spacer />
            <v-btn
              text
              @click="
                menu = false;
                filterName = '';
              ">
              {{ $t('exo.timeTracker.drawerButtonCancel') }}
            </v-btn>
            <v-btn
              color="primary"
              :disabled="isDisabled"
              text
              @click="
                menu = false;
                saveFilter();
              ">
              {{ $t('exo.timeTracker.drawerButtonSave') }}
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-menu>
    </template>
    <template slot="content">
      <div>
        <form ref="form1">
          <div
            v-if="employees.length > 0"
            id="timeTrackerDivAutoCompleteIdemployee"
            ref="timeTrackerDivAutoComplete"
            class="contactAutoComplete">
            <v-label for="employee">
              {{ $t('exo.timeTracker.timeSheet.filterDrawer.labelEmployee') }}
            </v-label>
            <v-autocomplete
              ref="employee"
              v-model="employee"
              :items="employees"
              class="input-block-level text-left text-justify text-break text-truncate"
              multiple
              outlined
              dense
              chips
              small-chips
              item-text="fullName"
              item-value="id"
              attach="#timeTrackerDivAutoCompleteIdemployee"
              @blur="blurAutocomplete('employee')" />
          </div>
          <div
            id="timeTrackerDivAutoCompleteIdteam"
            ref="timeTrackerDivAutoComplete"
            class="contactAutoComplete">
            <v-label for="team">
              {{ $t('exo.timeTracker.timeSheet.filterDrawer.labelTeam') }}
            </v-label>
            <v-autocomplete
              ref="team"
              v-model="team"
              :items="teams"
              class="input-block-level text-left text-justify text-break text-truncate"
              multiple
              outlined
              dense
              chips
              small-chips
              item-text="name"
              item-value="id"
              attach="#timeTrackerDivAutoCompleteIdteam"
              @blur="blurAutocomplete('team')" />
          </div>
          <div
            id="timeTrackerDivAutoCompleteIdactivity"
            ref="timeTrackerDivAutoComplete"
            class="contactAutoComplete">
            <v-label for="activity">
              {{ $t('exo.timeTracker.timeSheet.filterDrawer.labelActivity') }}
            </v-label>
            <v-autocomplete
              ref="activity"
              v-model="activity"
              :items="activities"
              class="input-block-level text-left text-justify text-break text-truncate"
              outlined
              dense
              chips
              small-chips
              multiple
              item-text="label"
              item-value="id"
              attach="#timeTrackerDivAutoCompleteIdactivity"
              @blur="blurAutocomplete('activity')" />
          </div>
          <div
            id="timeTrackerDivAutoCompleteIdlocation"
            ref="timeTrackerDivAutoComplete"
            class="contactAutoComplete">
            <v-label for="location">
              {{ $t('exo.timeTracker.timeSheet.filterDrawer.labelLocation') }}
            </v-label>
            <v-autocomplete
              ref="location"
              v-model="location"
              :items="locations"
              class="input-block-level text-left text-justify text-break text-truncate"
              outlined
              dense
              chips
              small-chips
              multiple
              item-text="label"
              item-value="id"
              attach="#timeTrackerDivAutoCompleteIdlocation"
              @blur="blurAutocomplete('location')" />
          </div>
          <div
            id="timeTrackerDivAutoCompleteIdOffice"
            ref="timeTrackerDivAutoComplete"
            class="contactAutoComplete">
            <v-label for="office">
              {{ $t('exo.timeTracker.timeSheet.filterDrawer.labelOffice') }}
            </v-label>
            <v-autocomplete
              ref="office"
              v-model="office"
              :items="offices"
              class="input-block-level text-left text-justify text-break text-truncate"
              outlined
              dense
              chips
              small-chips
              multiple
              item-text="label"
              item-value="id"
              attach="#timeTrackerDivAutoCompleteIdOffice"
              @blur="blurAutocomplete('office')" />
          </div>
          <div
            id="timeTrackerDivAutoCompleteIdtype"
            ref="timeTrackerDivAutoComplete"
            class="contactAutoComplete">
            <v-label for="type">
              {{ $t('exo.timeTracker.timeSheet.filterDrawer.labelType') }}
            </v-label>
            <v-autocomplete
              ref="type"
              v-model="type"
              :items="types"
              class="input-block-level text-left text-justify text-break text-truncate"
              outlined
              dense
              chips
              small-chips
              multiple
              item-text="label"
              item-value="id"
              attach="#timeTrackerDivAutoCompleteIdtype"
              @blur="blurAutocomplete('type')" />
          </div>
          <div
            id="timeTrackerDivAutoCompleteIdsubType"
            ref="timeTrackerDivAutoComplete"
            class="contactAutoComplete">
            <v-label for="subType">
              {{ $t('exo.timeTracker.timeSheet.filterDrawer.labelSubType') }}
            </v-label>
            <v-autocomplete
              ref="subType"
              v-model="subType"
              :items="subTypes"
              class="input-block-level text-left text-justify text-break text-truncate"
              menu-props="closeOnClick"
              outlined
              dense
              chips
              small-chips
              multiple
              item-text="label"
              item-value="id"
              attach="#timeTrackerDivAutoCompleteIdsubType"
              @blur="blurAutocomplete('subType')" />
          </div>
          <div
            id="timeTrackerDivAutoCompleteIdactivityCode"
            ref="timeTrackerDivAutoComplete"
            class="contactAutoComplete">
            <v-label for="activityCode">
              {{
                $t('exo.timeTracker.timeSheet.filterDrawer.labelActivityCode')
              }}
            </v-label>
            <v-autocomplete
              ref="activityCode"
              v-model="activityCode"
              :items="activityCodes"
              class="input-block-level text-left text-justify text-break text-truncate"
              outlined
              dense
              chips
              small-chips
              multiple
              item-text="label"
              item-value="id"
              attach="#timeTrackerDivAutoCompleteIdactivityCode"
              @blur="blurAutocomplete('activityCode')" />
          </div>
          <div
            id="timeTrackerDivAutoCompleteIdsubActivityCode"
            ref="timeTrackerDivAutoComplete"
            class="contactAutoComplete">
            <v-label for="subActivityCode">
              {{
                $t(
                  'exo.timeTracker.timeSheet.filterDrawer.labelSubActivityCode'
                )
              }}
            </v-label>
            <v-autocomplete
              ref="subActivityCode"
              v-model="subActivityCode"
              :items="subActivityCodes"
              class="input-block-level text-left text-justify text-break text-truncate"
              outlined
              dense
              chips
              small-chips
              multiple
              item-text="label"
              item-value="id"
              attach="#timeTrackerDivAutoCompleteIdsubActivityCode"
              @blur="blurAutocomplete('subActivityCode')" />
          </div>
          <div
            id="timeTrackerDivAutoCompleteIdclient"
            ref="timeTrackerDivAutoComplete"
            class="contactAutoComplete">
            <v-label for="client">
              {{ $t('exo.timeTracker.timeSheet.filterDrawer.labelClient') }}
            </v-label>
            <v-autocomplete
              ref="client"
              v-model="client"
              :items="clients"
              class="input-block-level text-left text-justify text-break text-truncate"
              outlined
              dense
              chips
              small-chips
              multiple
              item-text="label"
              item-value="id"
              attach="#timeTrackerDivAutoCompleteIdclient"
              @blur="blurAutocomplete('client')" />
          </div>
          <div
            id="timeTrackerDivAutoCompleteIdproject"
            ref="timeTrackerDivAutoComplete"
            class="contactAutoComplete">
            <v-label for="project">
              {{
                $t(
                  'exo.timeTracker.timeSheet.filterDrawer.labelSubActivityCode'
                )
              }}
            </v-label>
            <v-autocomplete
              ref="project"
              v-model="project"
              :items="projects"
              class="input-block-level text-left text-justify text-break text-truncate"
              outlined
              dense
              chips
              small-chips
              multiple
              item-text="label"
              item-value="id"
              attach="#timeTrackerDivAutoCompleteIdproject"
              @blur="blurAutocomplete('project')" />
          </div>
          <div
            id="timeTrackerDivAutoCompleteIdfeature"
            ref="timeTrackerDivAutoComplete"
            class="contactAutoComplete">
            <v-label for="feature">
              {{ $t('exo.timeTracker.timeSheet.filterDrawer.labelFeature') }}
            </v-label>
            <v-autocomplete
              ref="feature"
              v-model="feature"
              :items="features"
              class="input-block-level text-left text-justify text-break text-truncate"
              outlined
              dense
              chips
              small-chips
              multiple
              item-text="label"
              item-value="id"
              attach="#timeTrackerDivAutoCompleteIdfeature"
              @blur="blurAutocomplete('feature')" />
          </div>
        </form>
      </div>
    </template>
    <template slot="footer">
      <div class="d-flex">
        <v-spacer />
        <v-btn class="btn mr-2" @click="reset()">
          <template>
            {{ $t('exo.timeTracker.drawerButtonReset') }}
          </template>
        </v-btn>
        <v-btn class="btn mr-2" @click="cancel()">
          <template>
            {{ $t('exo.timeTracker.drawerButtonCancel') }}
          </template>
        </v-btn>
        <v-btn class="btn btn-primary" @click="aplyFilter()">
          <template>
            {{ $t('exo.timeTracker.drawerButtonApply') }}
          </template>
        </v-btn>
      </div>
    </template>
  </exo-drawer>
</template>

<script>
export default {
  props: {
    activities: {
      type: Array,
      default: () => null
    },
    types: {
      type: Array,
      default: () => null
    },
    subTypes: {
      type: Array,
      default: () => null
    },
    activityCodes: {
      type: Array,
      default: () => null
    },
    subActivityCodes: {
      type: Array,
      default: () => null
    },
    clients: {
      type: Array,
      default: () => null
    },
    projects: {
      type: Array,
      default: () => null
    },
    features: {
      type: Array,
      default: () => null
    },
    employees: {
      type: Array,
      default: () => null
    },
    filters: {
      type: Array,
      default: () => null
    },
    locations: {
      type: Array,
      default: () => null
    },
    offices: {
      type: Array,
      default: () => null
    },
    teams: {
      type: Array,
      default: () => null
    }
  },
  data: () => ({
    showName: false,
    menu: null,
    activity: [],
    activityRecord: {},
    type: [],
    subType: [],
    activityCode: [],
    subActivityCode: [],
    client: [],
    project: [],
    feature: [],
    location: [],
    office: [],
    employee: [],
    team: [],
    filter: {},
    filterName: ''
  }),
  computed: {
    isDisabled: function() {
      return !this.isNotEmpty(this.filterName);
    }
  },
  mounted() {
    $(this.$refs.filterDrawer.$el).click(() => {
      if (
        this.$refs &&
        this.$refs.filterDrawermenuFilter &&
        this.$refs.filterDrawermenuFilter.isActive
      ) {
        this.$refs.filterDrawermenuFilter.isActive = false;
      }
    });
  },
  watch: {
    menu: function(val) {
      if (val) {
        this.$refs.filterDrawermenuFilter.isActive = false;
      }
    }
  },
  methods: {
    blurAutocomplete(ref) {
      if (this.$refs && this.$refs[ref] && this.$refs[ref].isFocused) {
        this.$refs[ref].isFocused = false;
        this.$refs[ref].isMenuActive = false;
      }
    },
    isNotEmpty(str) {
      let stre = '';
      for (let index = 0; index < str.length; index++) {
        const element = str[index];
        if (element !== ' ') {
          stre += element;
        }
      }
      return str != null && str !== '' && stre.length !== 0;
    },
    reset() {
      this.activity = [];
      this.type = [];
      this.subType = [];
      this.activityCode = [];
      this.subActivityCode = [];
      this.client = [];
      this.project = [];
      this.feature = [];
      this.location = [];
      this.office = [];
      this.employee = [];
      this.team = [];
    },
    aplyFilter() {
      const filter_ = {
        activity: this.activity,
        type: this.type,
        subType: this.subType,
        activityCode: this.activityCode,
        subActivityCode: this.subActivityCode,
        client: this.client,
        project: this.project,
        feature: this.feature,
        location: this.location,
        office: this.office,
        team: this.team,
        employee: this.employee
      };
      this.$emit('addFilter', filter_);
      this.$refs.filterDrawer.close();
    },
    setFilter(filter) {
      this.reset();
      this.find(filter.fields.activity, this.activity, this.activities);
      this.find(filter.fields.type, this.type, this.types);
      this.find(filter.fields.subType, this.subType, this.subTypes);
      this.find(
        filter.fields.activityCode,
        this.activityCode,
        this.activityCodes
      );
      this.find(
        filter.fields.subActivityCode,
        this.subActivityCode,
        this.subActivityCodes
      );
      this.find(filter.fields.client, this.client, this.clients);
      this.find(filter.fields.project, this.project, this.projects);
      this.find(filter.fields.feature, this.feature, this.features);
      this.find(filter.fields.team, this.team, this.teams);
      this.location = filter.fields.location;
      this.office = filter.fields.office;
      this.employee = filter.fields.employee;
    },
    find(filters, item, items) {
      for (const filter of filters) {
        const found = items.find(element => String(element.id) === filter);
        if (found) {
          item.push(found);
        }
      }
    },
    cancel() {
      this.$refs.filterDrawer.close();
    },
    open() {
      this.$refs.filterDrawer.open();
    },
    saveFilter() {
      const filter_ = {
        name: this.filterName,
        activity: this.activity,
        type: this.type,
        subType: this.subType,
        activityCode: this.activityCode,
        subActivityCode: this.subActivityCode,
        client: this.client,
        project: this.project,
        feature: this.feature,
        location: this.location,
        office: this.office,
        employee: this.employee,
        team: this.team
      };
      this.$emit('saveFilter', filter_);
      this.filterName = '';
    },
    deleteFilter(item) {
      this.$emit('deleteFilter', item);
    }
  }
};
</script>

<style>
.sliderValue .v-text-field__slot {
  max-width: 50px;
}

.pointer {
  cursor: pointer;
}

.filterDrawerlistFilter {
  max-width: 390px;
  max-height: 205px;
  overflow-y: scroll;
  background-color: white !important;
}
</style>
