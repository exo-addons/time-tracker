<template>
  <exo-drawer
    ref="editOfficeDrawer"
    right
    class="">
    <template slot="title">
      {{ $t("exo.timeTracker.timeTrackingSettings.editOfficeDrawer.toolbarTitle") }}
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
              v-model="office.code"
              type="text"
              name="code"
              class="input-block-level ignore-vuetify-classes my-3"
              disabled>
          </div>
          <div>
            <v-label for="label">
              {{ $t("exo.timeTracker.activityManagement.drawerLabelTextLabel") }}
            </v-label>
            <input
              ref="label"
              v-model="office.label"
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
    },
    office: {
      code: '',
      label: ''
    }
  }),
  methods: {
    save() {
      this.$emit('save', this.office);
      this.office = this.defaultItem;
      this.$refs.editOfficeDrawer.close();
    },
    cancel() {
      this.office = this.defaultItem;
      this.$refs.editOfficeDrawer.close();
    },
    open(office) {
      this.office = office;
      this.$refs.editOfficeDrawer.open();
    },
  }
};
</script>
