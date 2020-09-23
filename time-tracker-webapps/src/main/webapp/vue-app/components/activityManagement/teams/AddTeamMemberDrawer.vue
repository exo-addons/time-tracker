<template>
<exo-drawer ref="addTeamMemberDrawer" right class="">
    <template slot="title">
        Add TeamMember
    </template>
    <template slot="content">
        <div>
            <v-form ref="form" v-model="valid">

                <v-row>
                    <v-label for="role">
                        Role
                    </v-label>
                    <select v-model="role" name="role" class="input-block-level ignore-vuetify-classes my-3">
                        <option v-for="item in roles" :key="item" :value="item">
                            {{ item}}
                        </option>
                    </select>
                </v-row>

                <v-row>
                    <v-label for="userName">
                        Members
                    </v-label>
                    <exo-identity-suggester ref="autoFocusInput3" v-model="suggestedMembers" :labels="suggesterLabels" name="inviteMembers" type-of-relations="member_of_space" :search-options="searchOptions" include-users multiple />
                </v-row>

            </v-form>
        </div>
    </template>
    <template slot="footer">
        <div class="d-flex">
            <v-spacer />
            <v-btn class="btn mr-2" @click="cancel()">
                <template>
                    Cancelœ
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
    props: [],
    data: () => ({
        roles: ["Member", "InWard", "OutWard"],
        defaultItem: {
            userName: '',
            role: '',
        },
        teamMember: {
            userName: '',
            role: '',
        },
        suggestedMembers: [],
        role: "Member",
        searchOptions:        {
                  spaceURL: "exo_employees",
                  currentUser:""
                }
    }),

    computed: {

        suggesterLabels() {
            return {
                placeholder: "Invite Members",
                noDataLabel: "No data",
            };
        }
    },

    methods: {
        save() {
            this.$emit('save', this.suggestedMembers, this.role)
            this.suggestedMembers = []
            this.$refs.addTeamMemberDrawer.close()
        },
        cancel() {
            this.teamMember = this.defaultItem
            this.$refs.addTeamMemberDrawer.close()
        },
        open() {
            this.teamMember = this.defaultItem
            this.$refs.addTeamMemberDrawer.open()
        },

    }
}
</script>

<style>
.drawerContent {
    padding: 15px 27px;
}
</style>
