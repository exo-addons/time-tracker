<template>
  <div>
    <v-card-text v-if="error" class="errorMessage">
      <v-alert type="error">
        {{ error }}
      </v-alert>
    </v-card-text>
    <v-card class="d-flex flex py-4 ma-4" flat>
      <v-flex class="sm12 md4" flat>
        <v-list>
          <v-subheader>
            <v-spacer />
            <button
              class="btn btn-primary pull-left"
              type="button"
              @click="openAddTeamDrawer">
              <i class="uiIconSocSimplePlus uiIconSocWhite"></i>
              {{ $t("exo.timeTracker.teams.text.add.team") }}
            </button>
          </v-subheader>
          <v-list-item-group v-model="item" color="primary">
            <v-list-item v-for="(item, i) in teams" :key="i">
              <v-list-item-content class="pointer" @click="getTeamMember(item)">
                <v-list-item-title v-html="item.name" />
                <v-list-item-subtitle class="caption text-truncate">
                  {{ item.description }}
                </v-list-item-subtitle>
              </v-list-item-content>
              <v-list-item-action>
                <v-menu 
                  bottom 
                  left
                  ref="menuItemUpdate">
                  <template v-slot:activator="{ on, attrs }">
                    <v-btn
                      icon
                      v-bind="attrs"
                      v-on="on">
                      <v-icon @click="menuItemUpdateFunction(i)">mdi-dots-vertical</v-icon>
                    </v-btn>
                  </template>
                  <v-list>
                    <v-list-item @click="openEditDrawer(item)">
                      <v-list-item-title class="subtitle-2">
                        <i class="uiIcon uiIconEdit"></i>
                        {{ $t("exo.timeTracker.teams.teamsList.edit") }}
                      </v-list-item-title>
                    </v-list-item>
                    <v-list-item @click="deleteItem(item)">
                      <v-list-item-title class="subtitle-2">
                        <i class="uiIcon uiIconTrash"></i>
                        {{ $t("exo.timeTracker.teams.teamsList.delete") }}
                      </v-list-item-title>
                    </v-list-item>
                  </v-list>
                </v-menu>
              </v-list-item-action>
            </v-list-item>
          </v-list-item-group>
        </v-list>
      </v-flex>
      <v-divider vertical />
      <v-flex class="sm12 md8" flat>
        <v-data-table
          v-if="selectedTeam!=null"
          :headers="headers"
          :items="members"
          sort-by="fullName">
          <template v-slot:top>
            <v-toolbar flat color="white">
              <v-spacer />
              <button
                class="btn pull-left"
                type="button"
                @click="openAddTeamMemberDrawer">
                <i class="uiIconSocSimplePlus"></i> {{ $t("exo.timeTracker.teams.text.add.member") }}
              </button>
            </v-toolbar>
          </template>
          <template v-slot:item.action="{ item }">
            <v-icon small @click="deleteTeamMember(item)">
              delete
            </v-icon>
          </template>
          <template v-slot:no-data>
            {{ $t("exo.timeTracker.teams.teamsList.textIfNoMember") }}
          </template>
        </v-data-table>
      </v-flex>
    </v-card>
    <add-team-drawer ref="addTeamDrawer" @save="addTeam" />
    <edit-team-drawer
      ref="editTeamDrawer"
      :team="editedItem"
      @save="updateTeam" />
    <add-team-member-drawer ref="addTeamMemberDrawer" @save="addTeamMember" />
  </div>
</template>

<script>
import addTeamDrawer from './AddTeamDrawer.vue';
import editTeamDrawer from './EditTeamDrawer.vue';
import addTeamMemberDrawer from './AddTeamMemberDrawer.vue';
export default {
  components: {
    addTeamDrawer,
    editTeamDrawer,
    addTeamMemberDrawer,
  },
  props: {
    teams: {
      type: Array,
      default: () => null,
    }
  },
  data: () => ({
    menuItemUpdateIndex: -1,
    alert: false,
    menuItemUpdate: false,
    message: '',
    alert_type: '',
    alertIcon: '',
    valid: true,
    members: [],
    selectedTeam: null,
    editedIndex: -1,
    editedItem: {
      name: '',
      description: ''
    },
    defaultItem: {
      name: '',
      description: ''

    }, 
    selectedMemberTeam: {},
    editedMemberIndex: -1,
    editedMemberItem: {
      userName: '',
      role: ''
    },
    defaultMemberItem: {
      userName: '',
      role: ''
    },
  }),
  computed: {
    headers() {
      return [{
        text: 'Full Name',
        align: 'center',
        sortable: true,
        value: 'fullName',
      },
      {
        text: 'role',
        align: 'center',
        sortable: true,
        value: 'role',
      },
      {
        text: 'Actions',
        align: 'center',
        sortable: true,
        value: 'action',
      },
      ];
    }
  },
  mounted () {
    $(document).click(()=> { 
      if (this.$refs && this.$refs.menuItemUpdate && this.$refs.menuItemUpdate[this.menuItemUpdateIndex] ) {
        if (this.$refs.menuItemUpdate[this.menuItemUpdateIndex].isActive){
          this.$refs.menuItemUpdate[this.menuItemUpdateIndex].isActive = false;
        }
      }
    });
  },
  methods: {
    menuItemUpdateFunction(i){
      if (this.$refs && this.$refs.menuItemUpdate) {
        if (this.$refs.menuItemUpdate[this.menuItemUpdateIndex]){
          this.$refs.menuItemUpdate[this.menuItemUpdateIndex].isActive = false;
        }
        this.menuItemUpdateIndex=i;
      }
    },
    deleteItem(item) {
      const index = this.teams.indexOf(item);
      this.teams.splice(index, 1);
      this.$emit('deleteTeam', item);
    },
    openAddTeamDrawer() {
      this.$refs.addTeamDrawer.open();
    },
    openEditDrawer(item) {
      this.editedIndex = this.teams.indexOf(item);
      this.editedItem = item;
      this.$refs.editTeamDrawer.open();
    },
    openAddTeamMemberDrawer() {
      this.$refs.addTeamMemberDrawer.open();
    },
    addTeam(team) {
      this.$emit('addTeam', team);
    },
    updateTeam(team) {
      Object.assign(this.teams[this.editedIndex], team);
      this.$emit('editTeam', this.editedItem);
    },
    getTeamMember(item) {
      this.selectedTeam=item;
      fetch(`/portal/rest/timetracker/teamsmgn/teamMember?teamId=${  item.id}`, {
        credentials: 'include',
      })
        .then((resp) => resp.json())
        .then((resp) => {
          this.members = resp;
        });
    },
    deleteTeamMember(item) {
      fetch(`/portal/rest/timetracker/teamsmgn/teamMember?teamMemberId=${  item.id}`, {
        method: 'delete',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
      })
        .then((result) => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.confirmDialog = false;
          this.getTeamMember(this.selectedTeam);
          this.displaySusccessMessage('teamMember deleted');
        })
        .catch((result) => {
          this.confirmDialog = false;
          this.getTeamMember(this.selectedTeam);
          result.text().then((body) => {
            this.displayErrorMessage(body);
          });
        });
    },
    addTeamMember(teamMembers,role) {
      const members = [];
      for (const member of teamMembers){
        const t_member={
          userName: member.remoteId,
          role: role,
          team: this.selectedTeam
        };
        members.push(t_member);
      }
      fetch('/portal/rest/timetracker/teamsmgn/teamMember/all', {
        method: 'post',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(members),
      })
        .then((result) => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.getTeamMember(this.selectedTeam);
          this.displaySusccessMessage('teamMember added');
        })
        .catch((result) => {
          this.getTeamMember(this.selectedTeam);
          result.text().then((body) => {
            this.displayErrorMessage(body);
          });
        });
    },
    displaySusccessMessage(message) {
      this.message = message;
      this.alert_type = 'alert-success';
      this.alertIcon = 'uiIconSuccess';
      this.alert = true;
      setTimeout(() => (this.alert = false), 5000);
    },
    displayErrorMessage(message) {
      this.isUpdating = false;
      this.message = message;
      this.alert_type = 'alert-error';
      this.alertIcon = 'uiIconError';
      this.alert = true;
      setTimeout(() => (this.alert = false), 5000);
    },
  }
};
</script>

<style>
#teamManagementApp {
    overflow: hidden;
    padding: 10px 20px;
}

select {
    width: auto;
}

#teamManagementApp .v-input input {
    margin-bottom: 0;
    border: 0;
    box-shadow: none;
}

#teamManagementApp .v-toolbar .v-input {
    margin-left: 18px;
}

#teamManagementApp .v-data-table {
    width: 100%;
}

.pointer {
    cursor: pointer;
}

.mtable {
    padding-left: 10px;
}
</style>
