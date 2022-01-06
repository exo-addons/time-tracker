<template>
  <exo-drawer
    ref="editFeatureDrawer"
    right
    class="">
    <template slot="title">
      Edit feature
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
              v-model="feature.code"
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
              v-model="feature.label"
              type="text"
              name="label"
              class="input-block-level ignore-vuetify-classes my-3">
          </div>
          <div>
            <v-label for="spec">
              Spec
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
              eXo
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
  props: {
    feature: {
      type: Object,
      default: ()=> {
        return {
          code: '',
          label: '',
          spec: '',
          exo: '',
        };
      }
    }
  },
  methods: {
    save() {
      this.$emit('save', this.feature);
      this.$refs.editFeatureDrawer.close();
    },
    cancel() {
      this.$refs.editFeatureDrawer.close();
    },
    open() {
      this.$refs.editFeatureDrawer.open();
    },
  }
};
</script>
