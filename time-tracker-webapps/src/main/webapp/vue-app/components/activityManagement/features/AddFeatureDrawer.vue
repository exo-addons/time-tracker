<template>
  <exo-drawer
    ref="addFeatureDrawer"
    right
    class="">
    <template slot="title">
      {{ $t("exo.timeTracker.features.text.add") }}
    </template>
    <template slot="content">
      <div>
        <v-form ref="form" v-model="valid">
          <div>
            <v-label for="code">
              {{ $t("exo.timeTracker.activityManagement.drawerLabelCode") }}
            </v-label>
            <input
              ref="code"
              v-model="feature.code"
              type="text"
              name="code"
              class="input-block-level ignore-vuetify-classes my-3">
          </div>
          <div>
            <v-label for="label">
              {{ $t("exo.timeTracker.activityManagement.drawerLabelTextLabel") }}
            </v-label>
            <input
              ref="label"
              v-model="feature.label"
              type="text"
              name="label"
              class="input-block-level ignore-vuetify-classes my-3">
          </div>
          <div>
            <v-label for="spec">
              {{ $t("exo.timeTracker.features.projectDrawer.drawerLabelSpec") }}
            </v-label>
            <input
              ref="label"
              v-model="feature.spec"
              type="text"
              name="spec"
              class="input-block-level ignore-vuetify-classes my-3">
          </div>
          <div>
            <v-label for="exo">
              {{ $t("exo.timeTracker.features.projectDrawer.drawerLabelExo") }}
            </v-label>
            <input
              ref="label"
              v-model="feature.exo"
              type="text"
              name="label"
              class="input-block-level ignore-vuetify-classes my-3">
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
  data: () => ({
    defaultItem: {
      code: '',
      label: '',
      spec: '',
      exo: '',
    },
    feature: {
      code: '',
      label: '',
      spec: '',
      exo: '',
    },
  }),
  methods: {
    save() {
      this.$emit('save', this.feature);
      this.resetForm();
      this.$refs.addFeatureDrawer.close();
    },
    cancel() {
      this.resetForm();
      this.$refs.addFeatureDrawer.close();
    },
    open() {
      this.resetForm();
      this.$refs.addFeatureDrawer.open();
    },
    resetForm(){
      this.feature.label='';
      this.feature.code='';
      this.feature.spec='';
      this.feature.exo='';
    }
  }
};
</script>
