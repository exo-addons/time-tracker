<template>
  <exo-drawer
    ref="addWorkTimeDrawer"
    right
    class="">
    <template slot="title">
      {{ $t("exo.timeTracker.timeTrackingSettings.text.add.worckTime") }}
    </template>
    <template slot="content">
      <div>
        <v-form ref="form" v-model="valid">
          <div>
            <v-menu
              ref="menu"
              v-model="menu"
              :close-on-content-click="false"
              :return-value.sync="date"
              transition="scale-transition"
              offset-y
              min-width="290px">
              <template v-slot:activator="{ on }">
                <v-text-field
                  v-model="dateRangeText"
                  prepend-icon="event"
                  readonly
                  v-on="on" />
              </template>
              <v-date-picker
                v-model="date"
                range
                no-title
                scrollable>
                <v-spacer />
                <v-btn
                  text
                  color="primary"
                  @click="menu = false">
                  {{ $t("exo.timeTracker.drawerButtonCancel") }}
                </v-btn>
                <v-btn
                  text
                  color="primary"
                  @click="$refs.menu.save(date)">
                  {{ $t("exo.timeTracker.popupButtonOk") }}
                </v-btn>
              </v-date-picker>
            </v-menu>
          </div>
          <div>
            <v-label for="office">
              {{ $t("exo.timeTracker.label.office") }}
            </v-label>
            <select
              v-model="workTime.office"
              name="office"
              class="input-block-level ignore-vuetify-classes my-3">
              <option
                v-for="item in offices"
                :key="item.id"
                :value="item">
                {{ item.label }}
              </option>
            </select>
          </div>
          <div>
            <v-label for="userName">
              {{ $t("exo.timeTracker.label.employee") }}
            </v-label>
            <exo-identity-suggester
              ref="autoFocusInput3"
              v-model="suggestedMembers"
              :labels="suggesterLabels"
              name="inviteMembers"
              type-of-relations="member_of_space"
              :search-options="searchOptions"
              include-users
              multiple />
          </div>
          <div>
            <v-label for="hours">
              {{ $t("exo.timeTracker.label.hours") }}
            </v-label>
            <input
              ref="hours"
              v-model="workTime.time"
              type="text"
              name="hoursNumber"
              class="input-block-level ignore-vuetify-classes my-3">
          </div>
          <div>
            <v-label for="period">
              {{ $t("exo.timeTracker.label.periode") }}
            </v-label>
            <select
              v-model="workTime.period"
              name="period"
              class="input-block-level ignore-vuetify-classes my-3">
              <option
                v-for="item in periods"
                :key="item"
                :value="item">
                {{ item }}
              </option>
            </select>
          </div>
          <div class="d-flex flex-wrap pt-2">
            <form class="switchEnabled">
              <label class="col-form-label pt-0" max-rows="6">
                {{ $t("exo.timeTracker.label.default") }}
              </label>
              <label class="switch">
                <input
                  v-model="workTime.defaultTime"
                  type="checkbox">
              </label>
            </form>
          </div>
        </v-form>
      </div>
    </template>
    <template slot="footer">
      <div class="d-flex">
        <v-spacer />
        <v-btn class="btn mr-2" @click="cancel()">
          <template>
            {{ $t("exo.timeTracker.drawerButtonCancel") }}
          </template>
        </v-btn>
        <v-btn class="btn btn-primary" @click="save()">
          <template>
            {{ $t("exo.timeTracker.drawerButtonSave") }}
          </template>
        </v-btn>
      </div>
    </template>
  </exo-drawer>
</template>

<script>
export default {
  props: {
    offices: {
      type: Array,
      default: () => null,
    },
    teams: {
      type: Array,
      default: () => null,
    }
  },
  data: () => ({
    date: [],
    dateRangeText: '',
    menu: false,
    workTime: {
      from: '',
      to: '',
      office: '',
      time: '',
      defaultTime: false,
    },
    periods: ['Daily', 'Weekly', 'Monthly'],
    suggestedMembers: [],
    searchOptions: {
      spaceURL: 'exo_employees',
      currentUser: ''
    }
  }),
  ceated(){
    const date = new Date();
    const firstDay = new Date(date.getFullYear(), date.getMonth(), 2);
    const lastDay = new Date(date.getFullYear(), date.getMonth() + 1, 1);
    this.date = [firstDay.toISOString().substr(0, 10), lastDay.toISOString().substr(0, 10)];
    this.dateRangeText = this.date.join(' ~ ');
    this.fromDate = this.date[0];
    this.toDate = this.date[1];
  },
  watch: {
    date() {
      this.dateRangeText = this.date.join(' ~ ');
      this.fromDate = this.date[0];
      this.toDate = this.date[0];
      if (typeof this.date[1] !== 'undefined') {
        this.toDate = this.date[1];
      }
    },
  },
  methods: {
    save() {
      this.workTime.fromDate=this.fromDate;
      this.workTime.toDate=this.toDate;
      if (this.workTime.office && !this.workTime.office.code) {
        this.workTime.office = {code: this.workTime.office};
      }
      this.$emit('save', this.workTime);
      this.workTime = {code: '',label: ''};
      this.$refs.addWorkTimeDrawer.close();
    },
    cancel() {
      this.workTime = {code: '',label: ''};
      this.$refs.addWorkTimeDrawer.close();
    },
    open() {
      this.workTime = {code: '',label: ''};
      this.$refs.addWorkTimeDrawer.open();
    },
  }
};
</script>
<style>
.slider {
    position: absolute;
    cursor: pointer;
    overflow: hidden;
    top: 5px;
    left: 0;
    right: 0;
    bottom: 0;
    width: 60px;
    height: 20px;
    background-color: #f2f2f2;
    -webkit-transition: .4s;
    transition: .4s;
}
</style>
