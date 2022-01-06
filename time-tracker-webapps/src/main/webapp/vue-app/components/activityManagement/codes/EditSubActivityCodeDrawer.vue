<template>
  <exo-drawer
    ref="editSubActivityCodeDrawer"
    right
    class="">
    <template slot="title">
      Add SubActivityCode
    </template>
    <template slot="content">
      <div>
        <v-form ref="form" v-model="valid">
          <div>
            <v-label for="code">
              Code
            </v-label>
            <input
              ref="code"
              v-model="subActivityCode.code"
              type="text"
              name="code"
              class="input-block-level ignore-vuetify-classes my-3">
          </div>
          <v-row>
            <v-label for="label">
              Label
            </v-label>
            <input
              ref="label"
              v-model="subActivityCode.label"
              type="text"
              name="label"
              class="input-block-level ignore-vuetify-classes my-3">
          </v-row>
        </v-form>
      </div>
    </template>
    <template slot="footer">
      <div class="d-flex">
        <v-spacer />
        <v-btn class="btn mr-2" @click="cancel()">
          <template>
            Cancel
          </template>
        </v-btn>
        <v-btn class="btn btn-primary" @click="save()">
          <template>
            Save
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
    subActivityCode: {
      code: '',
      label: '',
    },
  }),
  methods: {
    save() {
      this.$emit('save', this.subActivityCode);
      this.subActivityCode = this.defaultItem;
      this.$refs.editSubActivityCodeDrawer.close();
    },
    cancel() {
      this.subActivityCode = this.defaultItem;
      this.$refs.editSubActivityCodeDrawer.close();
    },
    open(subActivityCode) {
      this.subActivityCode = subActivityCode;
      this.$refs.editSubActivityCodeDrawer.open();
    },
  }
};
</script>
