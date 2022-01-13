<template>
  <exo-drawer
    ref="addProjectDrawer"
    right
    class="">
    <template slot="title">
      {{ $t("exo.timeTracker.projects.text.add") }}
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
              v-model="project.code"
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
              v-model="project.label"
              type="text"
              name="label"
              class="input-block-level ignore-vuetify-classes my-3">
          </div>
          <div>
            <v-label for="client">
              {{ $t("exo.timeTracker.label.client") }}
            </v-label>
            <select
              v-model="project.client"
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
    clients: {
      type: Array,
      default: () => null,
    }
  },
  data: () => ({
    defaultItem: {
      code: '',
      label: '',
      client: {
        id: ''
      }
    },
    project: {
      code: '',
      label: '',
      client: {
        id: ''
      }
    },
  }),
  methods: {
    save() {
      this.$emit('save', this.project);
      this.resetForm();
      this.$refs.addProjectDrawer.close();
    },
    cancel() {
      this.resetForm();
      this.$refs.addProjectDrawer.close();
    },
    open() {
      this.resetForm();
      this.$refs.addProjectDrawer.open();
    },
    resetForm(){
      this.project.label='';
      this.project.code='';
      this.project.client={id: '',  code: '', label: ''};
    }
  }
};
</script>
