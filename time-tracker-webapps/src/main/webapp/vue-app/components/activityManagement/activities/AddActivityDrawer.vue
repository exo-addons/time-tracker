<template>
  <exo-drawer
    ref="addActivityDrawer"
    right
    class="">
    <template slot="title">
      {{ $t("exo.timeTracker.activities.text.add") }}
    </template>
    <template slot="content">
      <div>
        <v-form ref="form" v-model="valid">
          <div>
            <v-label for="type">
              {{ $t("exo.timeTracker.activityManagement.drawerLabelType") }}
            </v-label>
            <select
              v-model="editedActivity.type"
              name="type"
              class="input-block-level ignore-vuetify-classes my-3">
              <option
                v-for="item in types"
                :key="item.id"
                :value="item">
                {{ item.displayLabel }}
              </option>
            </select>
          </div>
          <div>
            <v-label for="subType">
              {{ $t("exo.timeTracker.activities.addActivitiesDrawer.drawerLabelSubtype") }}
            </v-label>
            <select
              v-model="editedActivity.subType"
              name="subType"
              :disabled="!editedActivity.type.id"
              class="input-block-level ignore-vuetify-classes my-3">
              <option
                v-for="item in filtredSubTypes"
                :key="item.id"
                :value="item">
                {{ item.displayLabel }}
              </option>
            </select>
          </div>
          <div>
            <v-label for="activityCode">
              {{ $t("exo.timeTracker.activities.addActivitiesDrawer.drawerLabelActivity") }}
            </v-label>
            <select
              v-model="editedActivity.activityCode"
              name="activityCode"
              class="input-block-level ignore-vuetify-classes my-3">
              <option
                v-for="item in activityCodes"
                :key="item.id"
                :value="item">
                {{ item.displayLabel }}
              </option>
            </select>
          </div>
          <div>
            <v-label for="subActivityCode">
              {{ $t("exo.timeTracker.activities.addActivitiesDrawer.drawerLabelSubactivity") }}
            </v-label>
            <select
              v-model="editedActivity.subActivityCode"
              name="subActivityCode"
              class="input-block-level ignore-vuetify-classes my-3">
              <option
                v-for="item in subActivityCodes"
                :key="item.id"
                :value="item">
                {{ item.displayLabel }}
              </option>
            </select>
          </div>
          <div>
            <v-label for="label">
              {{ $t("exo.timeTracker.activityManagement.drawerLabelTextLabel") }}
            </v-label>
            <input
              ref="label"
              v-model="editedActivity.label"
              type="text"
              name="label"
              class="input-block-level ignore-vuetify-classes my-3">
          </div>
          <div>
            <v-label for="project">
              {{ $t("exo.timeTracker.activities.addActivitiesDrawer.drawerLabelProject") }}
            </v-label>
            <select
              v-model="editedActivity.project"
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
          <div>
            <v-label for="feature">
              {{ $t("exo.timeTracker.activities.addActivitiesDrawer.drawerLabelFeature") }}
            </v-label>
            <select
              v-model="editedActivity.feature"
              name="feature"
              class="input-block-level ignore-vuetify-classes my-3">
              <option
                v-for="item in features"
                :key="item.id"
                :value="item">
                {{ item.displayLabel }}
              </option>
            </select>
          </div>
          <div>
            <v-label for="teams">
              {{ $t("exo.timeTracker.activities.addActivitiesDrawer.drawerLabelTeams") }}
            </v-label>
            <v-autocomplete
              ref="select"
              v-model="editedActivity.teams"
              :items="teams"
              menu-props="closeOnClick"
              outlined
              dense
              chips
              small-chips
              multiple
              item-text="name"
              item-value="id"
              @click.stop />
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
    projects: {
      type: Array,
      default: () => null,
    },
    features: {
      type: Array,
      default: () => null,
    },
    activityCodes: {
      type: Array,
      default: () => null,
    },
    subActivityCodes: {
      type: Array,
      default: () => null,
    },
    types: {
      type: Array,
      default: () => null,
    },
    subTypes: {
      type: Array,
      default: () => null,
    },
    teams: {
      type: Array,
      default: () => null,
    },
    otherSettings: {
      type: Object,
      default: () => null,
    }
  },
  data: () => ({
    defaultItem: {
      type: {
        id: '',
        code: '',
        label: ''
      },
      subType: {
        id: '',
        code: '',
        label: ''
      },
      activity: {
        id: '',
        code: '',
        label: ''
      },
      subActivity: {
        id: '',
        code: '',
        label: ''
      },
      label: '',
      project: {
        id: '',
        code: '',
        label: ''
      },
      feature: {
        id: '',
        code: '',
        label: ''
      },
    },
    editedActivity: {
      type: {
        id: '',
        code: '',
        label: ''
      },
      subType: {
        id: '',
        code: '',
        label: ''
      },
      activity: {
        id: '',
        code: '',
        label: ''
      },
      subActivity: {
        id: '',
        code: '',
        label: ''
      },
      label: '',
      project: {
        id: '',
        code: '',
        label: ''
      },
      feature: {
        id: '',
        code: '',
        label: ''
      },
    },
  }),
  computed: {
    isDisabled: function(){
      return (this.editedActivity && !(
        this.isNotEmpty(this.editedActivity.project)
                && this.isNotEmpty(this.editedActivity.label)
                && this.isNotEmpty(this.editedActivity.activityCode)
                && this.isNotEmpty(this.editedActivity.subActivityCode)
                && this.isNotEmpty(this.editedActivity.subType
                && this.isNotEmpty(this.editedActivity.type))));
    },
    filtredSubTypes() {
      return this.subTypes.filter(subType => {
        return (
          this.editedActivity.type.id && subType.type.id===this.editedActivity.type.id
        );
      });
    }
  },
  watch: {
    'editedActivity.feature'(val) {
      if (!val.subActivityCode&& val){
        this.editedActivity.subActivityCode=this.otherSettings.defaultFeatureSubActivity;
      }
    }
  },
  methods: {
    isNotEmpty(str){
      return (str!=null && str.id!==null && str.id!=='');
    },
    save() {
      const teams = [];
      for (const team of this.editedActivity.teams) {
        const t_ = {
          id: team,
        };
        teams.push(t_);
      }
      this.editedActivity.teams = teams;
      this.$emit('save', this.editedActivity);
      this.resetForm();
      this.$refs.addActivityDrawer.close();
    },
    cancel() {
      this.resetForm();
      this.$refs.addActivityDrawer.close();
    },
    open() {
      this.resetForm();
      this.$refs.addActivityDrawer.open();
    },
    resetForm(){
      this.editedActivity.type={id: '',  code: '', label: ''};
      this.editedActivity.subType={id: '',  code: '', label: ''};
      this.editedActivity.activityCode={id: '',  code: '', label: ''};
      this.editedActivity.subActivityCode={id: '',  code: '', label: ''};
      this.editedActivity.project={id: '',  code: '', label: ''};
      this.editedActivity.feature={id: '',  code: '', label: ''};
      this.editedActivity.label='';
      this.editedActivity.teams=[];
    }
  }
};
</script>
