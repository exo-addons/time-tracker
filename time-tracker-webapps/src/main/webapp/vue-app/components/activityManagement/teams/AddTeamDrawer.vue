<template>
  <exo-drawer
    ref="addTeamDrawer"
    right
    class="">
    <template slot="title">
      {{ $t("exo.timeTracker.teams.text.add.team") }}
    </template>
    <template slot="content">
      <div>
        <v-form ref="form" v-model="valid">
          <div>
            <v-label for="name">
              {{ $t("exo.timeTracker.teams.TeamDrawer.drawerLabelName") }}
            </v-label>
            <input
              ref="name"
              v-model="team.name"
              type="text"
              name="name"
              class="input-block-level ignore-vuetify-classes my-3">
          </div>
          <div>
            <v-label for="description">
              {{ $t("exo.timeTracker.teams.TeamDrawer.drawerLabelDescription") }}
            </v-label>
            <input
              ref="description"
              v-model="team.description"
              type="text"
              name="description"
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
      client: {
        id: ''
      }
    },
    team: {
      code: '',
      label: '',
      client: {
        id: ''
      }
    },
  }),
  methods: {
    save() {
      this.$emit('save', this.team);
      this.team = this.defaultItem;
      this.$refs.addTeamDrawer.close();
    },
    cancel() {
      this.team = this.defaultItem;
      this.$refs.addTeamDrawer.close();
    },
    open() {
      this.team = this.defaultItem;
      this.$refs.addTeamDrawer.open();
    },
  }
};
</script>
