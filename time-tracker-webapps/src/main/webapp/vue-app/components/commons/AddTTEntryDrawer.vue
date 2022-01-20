<template>
  <exo-drawer ref="addTTEntryDrawer" right>
    <template slot="title">
      {{ $t("exo.timeTracker.timeTracking.text.add.entry") }}
    </template>
    <template slot="content">
      <div>
        <v-form ref="form">
          <div
            v-if="showDPicker"
            align="center"
            justify="center">
            <v-menu
              v-model="menu2"
              :close-on-content-click="false"
              :nudge-right="40"
              transition="scale-transition"
              offset-y
              min-width="290px">
              <template v-slot:activator="{ on, attrs }">
                <v-text-field
                  v-model="date"
                  centered
                  prepend-icon="event"
                  readonly
                  v-bind="attrs"
                  v-on="on" />
              </template>
              <v-date-picker v-model="date" @input="menu2 = false" />
            </v-menu>
          </div>
          <div>
            <v-label for="description">
              {{ $t("exo.timeTracker.commons.TTEntryDrawer.label.description") }}
            </v-label>
            <extended-textarea
              id="desc"
              ref="description"
              v-model="activityRecord.description"
              :max-length="250"
              class="input-block-level ignore-vuetify-classes my-3"
              placeholder="What are you working on ?" />
          </div>
          <div>
            <v-label for="time">
              {{ $t("exo.timeTracker.commons.TTEntryDrawer.label.hours") }}
            </v-label>
            <input
              ref="time"
              v-model="activityRecord.time"
              type="text"
              name="time"
              class="input-block-level ignore-vuetify-classes my-3">
          </div>
          <div
            id="accessPermissionAutoCompleteActivity"
            class=" contactAutoComplete">
            <v-label for="activity">
              {{ $t("exo.timeTracker.commons.TTEntryDrawer.label.activity") }}
            </v-label>
            <v-autocomplete
              ref="autocompleteActivityItemAdd"
              v-model="activityRecord.activity"
              :items="activities"
              class="input-block-level"
              outlined
              height="30"
              dense
              chips
              small-chips
              item-text="label"
              item-value="id"
              attach="#accessPermissionAutoCompleteActivity" />
          </div>
          <div
            v-if="
              activityRecord.project ||
                (selectedActivity &&
                (!selectedActivity.project ||
                (selectedActivity.project &&
                selectedActivity.project.code === '<PRJ>')))
            ">
            <v-label for="project">
              {{ $t("exo.timeTracker.commons.TTEntryDrawer.label.project") }}
            </v-label>
            <select
              v-model="activityRecord.project"
              name="project"
              class="input-block-level ignore-vuetify-classes my-3">
              <option
                v-for="item in projects"
                :key="item.id"
                :value="item">
                {{ item.displayLabel }}
              </option>
            </select>
          </div>
          <div
            v-if="
              activityRecord.project ||
                (selectedActivity &&
                (!selectedActivity.project ||
                (selectedActivity.project &&
                selectedActivity.project.code === '<PRJ>')))
            ">
            <v-label for="client"> Client </v-label>
            <select
              v-model="activityRecord.client"
              name="client"
              class="input-block-level ignore-vuetify-classes my-3">
              <option
                v-for="item in clients"
                :key="item.id"
                :value="item">
                {{ item.displayLabel }}
              </option>
            </select>
          </div>
          <div>
            <v-label for="projectVersion">
              {{ $t("exo.timeTracker.commons.TTEntryDrawer.label.projectVersion") }}
            </v-label>
            <input
              ref="projectVersion"
              v-model="activityRecord.projectVersion"
              type="text"
              name="projectVersion"
              class="input-block-level ignore-vuetify-classes my-3">
          </div>
          <div v-if="salesOrders.length > 0">
            <v-label for="salesOrder">
              {{ $t("exo.timeTracker.commons.TTEntryDrawer.label.salesOrder") }}
            </v-label>
            <select
              v-model="activityRecord.salesOrder"
              name="salesOrder"
              class="input-block-level ignore-vuetify-classes my-3">
              <option
                v-for="item in salesOrders"
                :key="item.id"
                :value="item">
                {{ item.name }}
              </option>
            </select>
          </div>
          <div>
            <v-label for="location">
              {{ $t("exo.timeTracker.commons.TTEntryDrawer.label.location") }}
            </v-label>
            <select
              v-model="activityRecord.location"
              name="location"
              class="input-block-level ignore-vuetify-classes my-3">
              <option
                v-for="item in locations"
                :key="item.code"
                :value="item.code">
                {{ item.label }}
              </option>
            </select>
          </div>
          <div>
            <v-label for="office">
              {{ $t("exo.timeTracker.commons.TTEntryDrawer.label.office") }}
            </v-label>
            <select
              v-model="activityRecord.office"
              name="office"
              class="input-block-level ignore-vuetify-classes my-3">
              <option
                v-for="item in offices"
                :key="item.code"
                :value="item.code">
                {{ item.label }}
              </option>
            </select>
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
        <v-btn
          :disabled="isDisabled"
          class="btn btn-primary"
          @click="save()">
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
    activities: {
      type: Array,
      default: () => null,
    },
    locations: {
      type: Array,
      default: () => null,
    },
    offices: {
      type: Array,
      default: () => null,
    },
    clients: {
      type: Array,
      default: () => null,
    },
    projects: {
      type: Array,
      default: () => null,
    }
  },
  data: () => ({
    date: new Date().toISOString().substr(0, 10),
    activityRecord: {},
    salesOrders: [],
    showDPicker: false,
    selectedActivity: {},
    userName: '',
    hiddenauto: false
  }),
  computed: {
    isDisabled: function() {
      const isEmptyDescription = this.isNotEmpty(
        this.activityRecord.description
      )
        ? this.activityRecord.description.length <= 250
        : true;
      return !(
        this.isNotEmpty(this.activityRecord.office) &&
        this.isNotEmpty(this.activityRecord.location) &&
        this.isNotEmpty(this.activityRecord.time) &&
        this.activityRecord.time >= 0 &&
        this.activityRecord.time <= 8 &&
        isEmptyDescription
      );
    }
  },
  watch: {
    'activityRecord.activity'(newVal) {
      if (newVal && !newVal.id) {
        newVal = this.activities.find(act => act.id === newVal);
        this.selectedActivity = newVal;
      }
      if (newVal && newVal.project && newVal.project.client) {
        this.salesOrders = newVal.project.client.salesOrders;
      } else {
        this.salesOrders = [];
      }
    }
  },
  mounted () {
    $(this.$refs.addTTEntryDrawer.$el).mousedown(()=> {
      if (
        this.$refs &&
        this.$refs.autocompleteActivityItemAdd &&
        this.$refs.autocompleteActivityItemAdd.isFocused
      ) {
        setTimeout(() => {
          this.$refs.autocompleteActivityItemAdd.isFocused = false;
          this.$refs.autocompleteActivityItemAdd.isMenuActive = false;
        }, 100);
      }
    });
  },
  methods: {
    getLastActivityRecord() {
      return new Promise((resolve) => {
        fetch(
          '/portal/rest/timetracker/activityRecordrecordsmgn/activityrecord/last',
          {
            credentials: 'include'
          }
        )
          .then(resp => resp.json())
          .then(resp => {
            const item = resp;
            this.loading = false;
            resolve({
              item
            });
          });
      });
    },
    isNotEmpty(str) {
      return str != null && str !== '';
    },
    save() {
      this.activityRecord.activityDate = this.date;
      this.activityRecord.userName = this.userName;
      if (this.activityRecord.activity && !this.activityRecord.activity.id) {
        this.activityRecord.activity = { id: this.activityRecord.activity };
      }
      this.$emit('save', this.activityRecord);
      this.activityRecord = {};
      this.$refs.addTTEntryDrawer.close();
    },
    cancel() {
      this.$refs.addTTEntryDrawer.close();
    },
    open(timeRecord, showDPicker) {
      if (showDPicker) {
        this.showDPicker = true;
      }
      if (!this.activityRecord.time) {
        this.getLastActivityRecord().then(data => {
          if (data.item) {
            this.activityRecord = data.item;
            this.activityRecord.time = null;
            this.activityRecord.description = '';
            this.activityRecord.salesOrder = null;
          }
        });
      } else {
        this.activityRecord.time = null;
      }
      if (timeRecord) {
        this.activityRecord.activityTime = timeRecord.activityTime;
        this.activityRecord.activityDate = timeRecord.activityDate;
        this.activityRecord.userName = timeRecord.userName;
        this.activityRecord.userFullName = timeRecord.userFullName;
        this.userName = timeRecord.userName;
        this.date = timeRecord.activityDate;
      }
      this.$refs.addTTEntryDrawer.open();
    }
  }
};
</script>
