<template>
  <exo-drawer
    ref="filterDrawer"
    right
    class="timeSheetFilterDrawer">
    <template slot="title">
      Filter
    </template>
    <template slot="titleIcons">
      <v-menu offset-y>
        <template v-slot:activator="{ on, attrs }">
          <v-btn
            text
            small
            v-bind="attrs"
            v-on="on">
            Saved Filters
          </v-btn>
        </template>
        <v-list>
          <v-list-item v-for="(item, index) in filters" :key="index">
            <v-list-item-title class="pointer" @click="setFilter(item)">
              {{
                item.filter.name
              }}
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
            Add Filter
          </v-btn>
        </template>
        <v-card>
          <v-list>
            <v-list-item>
              <v-list-item-content>
                <v-label for="filterName"> Filter Name </v-label>
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
            <v-btn text @click="menu = false">Cancel</v-btn>
            <v-btn
              color="primary"
              text
              @click="
                menu = false;
                saveFilter();
              ">
              Save
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-menu>
    </template>
    <template slot="content">
      <div>
        <form ref="form1">
          <div v-if="employees.length > 0">
            <v-label for="employees"> Employee </v-label>

            <select
              v-model="employee"
              employee="team"
              class="input-block-level ignore-vuetify-classes my-3">
              <option
                v-for="item in employees"
                :key="item.id"
                :value="item.userName">
                {{ item.fullName }}
              </option>
            </select>
          </div>

          <div v-if="teams.length > 0">
            <v-label for="team"> Team </v-label>
            <select
              v-model="team"
              name="team"
              class="input-block-level ignore-vuetify-classes my-3">
              <option
                v-for="item in teams"
                :key="item.id"
                :value="item.id">
                {{ item.name }}
              </option>
            </select>
          </div>

          <div>
            <v-label for="activity"> Activity </v-label>
            <v-autocomplete
              ref="activity"
              v-model="activity"
              :items="activities"
              menu-props="closeOnClick"
              outlined
              dense
              chips
              small-chips
              multiple
              item-text="label"
              item-value="id"
              @click.stop />
          </div>
          <div>
            <v-label for="location"> Location </v-label>
            <v-autocomplete
              ref="location"
              v-model="location"
              :items="locations"
              menu-props="closeOnClick"
              outlined
              dense
              chips
              small-chips
              multiple
              item-text="label"
              item-value="id"
              @click.stop />
          </div>
          <div>
            <v-label for="office"> Office </v-label>
            <v-autocomplete
              ref="office"
              v-model="office"
              :items="offices"
              menu-props="closeOnClick"
              outlined
              dense
              chips
              small-chips
              multiple
              item-text="label"
              item-value="id"
              @click.stop />
          </div>
          <div>
            <v-label for="type"> Type </v-label>
            <v-autocomplete
              ref="type"
              v-model="type"
              :items="types"
              menu-props="closeOnClick"
              outlined
              dense
              chips
              small-chips
              multiple
              item-text="label"
              item-value="id"
              @click.stop />
          </div>

          <div>
            <v-label for="subType"> Sub type </v-label>
            <v-autocomplete
              ref="subType"
              v-model="subType"
              :items="subTypes"
              menu-props="closeOnClick"
              outlined
              dense
              chips
              small-chips
              multiple
              item-text="label"
              item-value="id"
              @click.stop />
          </div>

          <div>
            <v-label for="activityCode"> Activity Code </v-label>
            <v-autocomplete
              ref="activityCode"
              v-model="activityCode"
              :items="activityCodes"
              menu-props="closeOnClick"
              outlined
              dense
              chips
              small-chips
              multiple
              item-text="label"
              item-value="id"
              @click.stop />
          </div>

          <div>
            <v-label for="subActivityCode"> Sub Activity Code </v-label>
            <v-autocomplete
              ref="subActivityCode"
              v-model="subActivityCode"
              :items="subActivityCodes"
              menu-props="closeOnClick"
              outlined
              dense
              chips
              small-chips
              multiple
              item-text="label"
              item-value="id"
              @click.stop />
          </div>

          <div>
            <v-label for="client"> Client </v-label>
            <v-autocomplete
              ref="client"
              v-model="client"
              :items="clients"
              menu-props="closeOnClick"
              outlined
              dense
              chips
              small-chips
              multiple
              item-text="label"
              item-value="id"
              @click.stop />
          </div>

          <div>
            <v-label for="project"> Project </v-label>
            <v-autocomplete
              ref="project"
              v-model="project"
              :items="projects"
              menu-props="closeOnClick"
              outlined
              dense
              chips
              small-chips
              multiple
              item-text="label"
              item-value="id"
              @click.stop />
          </div>

          <div>
            <v-label for="feature"> Feature </v-label>
            <v-autocomplete
              ref="feature"
              v-model="feature"
              :items="features"
              menu-props="closeOnClick"
              outlined
              dense
              chips
              small-chips
              multiple
              item-text="label"
              item-value="id"
              @click.stop />
          </div>
        </form>
      </div>
    </template>
    <template slot="footer">
      <div class="d-flex">
        <v-spacer />
        <v-btn class="btn mr-2" @click="reset()">
          <template>
            Reset
          </template>
        </v-btn>
        <v-btn class="btn mr-2" @click="cancel()">
          <template>
            Cancel
          </template>
        </v-btn>
        <v-btn class="btn btn-primary" @click="aplyFilter()">
          <template>
            Apply
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
    activity: 0,
    type: 0,
    subType: 0,
    activityCode: 0,
    subActivityCode: 0,
    client: 0,
    project: 0,
    feature: 0,
    location: '',
    office: '',
    employee: '',
    team: '',
    filter: {},
    filterName: ''
  }),
  methods: {
    reset() {
      this.activity = 0;
      this.type = 0;
      this.subType = 0;
      this.activityCode = 0;
      this.subActivityCode = 0;
      this.client = 0;
      this.project = 0;
      this.feature = 0;
      this.location = '';
      this.office = '';
      this.employee = '';
      this.team = '';
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
      this.activity = filter.fields.activity;
      this.type = filter.fields.type;
      this.subType = filter.fields.subType;
      this.activityCode = filter.fields.activityCode;
      this.subActivityCode = filter.fields.subActivityCode;
      this.client = filter.fields.client;
      this.project = filter.fields.project;
      this.feature = filter.fields.feature;
      this.location = filter.fields.location;
      this.office = filter.fields.office;
      this.employee = filter.fields.employee;
      this.team = filter.fields.team;
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
</style>
