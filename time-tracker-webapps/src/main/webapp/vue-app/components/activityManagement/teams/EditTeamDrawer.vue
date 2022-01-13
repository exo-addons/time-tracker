<template>
  <exo-drawer
    ref="editTeamDrawer"
    right
    class="">
    <template slot="title">
      {{ $t("exo.timeTracker.teams.editTeamDrawer.toolbarTitle") }}
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
  props: {
    team: {
      type: Object,
      default: ()=> {
        return {
          name: '',
          description: ''
        };
      }
    }
  },
  methods: {
    save() {
      this.$emit('save', this.team);
      this.$refs.editTeamDrawer.close();
    },
    cancel() {
      this.$refs.editTeamDrawer.close();
    },
    open() {
      this.$refs.editTeamDrawer.open();
    },
  }
};
</script>