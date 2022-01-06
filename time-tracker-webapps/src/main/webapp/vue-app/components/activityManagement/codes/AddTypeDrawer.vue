<template>
  <exo-drawer
    ref="addTypeDrawer"
    right
    class="">
    <template slot="title">
      Add Type
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
              v-model="type.code"
              type="text"
              name="code"
              class="input-block-level ignore-vuetify-classes my-3">
          </div>
          <div>
            <v-label for="label">
              Label
            </v-label>
            <input
              ref="label"
              v-model="type.label"
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
      label: ''},
    type: {
      code: '',
      label: ''
    }
  }),
  methods: {
    save() {
      this.$emit('save', this.type);
      this.type = {code: '',label: ''};
      this.$refs.addTypeDrawer.close();
    },
    cancel() {
      this.type = {code: '',label: ''};
      this.$refs.addTypeDrawer.close();
    },
    open() {
      this.type = {code: '',label: ''};
      this.$refs.addTypeDrawer.open();
    },
  }
};
</script>
