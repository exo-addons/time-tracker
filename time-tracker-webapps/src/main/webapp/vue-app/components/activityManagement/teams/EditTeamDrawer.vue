<template>
<exo-drawer ref="editTeamDrawer" right class="">
    <template slot="title">
        Edit team
    </template>
    <template slot="content">
        <div>
            <v-form ref="form" v-model="valid">
                <v-row>
                    <v-label for="name">
                       Name
                    </v-label>
                    <input ref="name" v-model="team.name" type="text" name="name" class="input-block-level ignore-vuetify-classes my-3" />
                </v-row>
              
                <v-row>
                    <v-label for="description">
                       Description
                    </v-label>
                    <input ref="description" v-model="team.description" type="text" name="description" class="input-block-level ignore-vuetify-classes my-3" />
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
    props: ['team'],


    methods: {
        save() {
            this.$emit('save', this.team)
            this.$refs.editTeamDrawer.close()
        },
        cancel() {
            this.$refs.editTeamDrawer.close()
        },
        open() {
            this.$refs.editTeamDrawer.open()
        },

    }
}
</script>

<style>
.drawerContent {
    padding: 15px 27px;
}
</style>
