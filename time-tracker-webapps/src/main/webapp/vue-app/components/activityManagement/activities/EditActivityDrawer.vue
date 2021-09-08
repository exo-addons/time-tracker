<template>
<exo-drawer ref="editActivityDrawer" right class="">
    <template slot="title">
        Edit activity
    </template>
    <template slot="content">
        <div>
            <v-form ref="form" v-model="valid">
                <div>
                    <v-label for="type">
                        Type
                    </v-label>
                    <select v-model="activity.type" name="type" class="input-block-level ignore-vuetify-classes my-3">
                        <option v-for="item in types" :key="item.id" :value="item">
                            {{ item.displayLabel}}
                        </option>
                    </select>
                </div>
                <div>
                    <v-label for="subType">
                        Sub Type
                    </v-label>
                    <select v-model="activity.subType" name="subType" :disabled="!activity.type.id" class="input-block-level ignore-vuetify-classes my-3">
                        <option v-for="item in filtredSubTypes" :key="item.id" :value="item">
                            {{ item.displayLabel}}
                        </option>
                    </select>
                </div>
                <div>
                    <v-label for="activityCode">
                        Activity
                    </v-label>
                    <select v-model="activity.activityCode" name="activityCode" class="input-block-level ignore-vuetify-classes my-3">
                        <option v-for="item in activityCodes" :key="item.id" :value="item">
                            {{ item.displayLabel}}
                        </option>
                    </select>
                </div>
                <div>
                    <v-label for="subActivityCode">
                        Sub Activity
                    </v-label>
                    <select v-model="activity.subActivityCode" name="subActivityCode" class="input-block-level ignore-vuetify-classes my-3">
                        <option v-for="item in subActivityCodes" :key="item.id" :value="item">
                            {{ item.displayLabel}}
                        </option>
                    </select>
                </div>
                <div>
                    <v-label for="label">
                        Label
                    </v-label>
                    <input ref="label" v-model="activity.label" type="text" name="label" class="input-block-level ignore-vuetify-classes my-3" />
                </div>
                <div>
                    <v-label for="project">
                        Project
                    </v-label>
                    <select v-model="activity.project" name="project" class="input-block-level ignore-vuetify-classes my-3">
                        <option v-for="item in projects" :key="item.id" :value="item">
                            {{ item.displayLabel}}
                        </option>
                    </select>
                </div>
                <div>
                    <v-label for="feature">
                        Feature
                    </v-label>
                    <select v-model="activity.feature" name="feature" class="input-block-level ignore-vuetify-classes my-3">
                        <option v-for="item in features" :key="item.id" :value="item">
                            {{ item.displayLabel}}
                        </option>
                    </select>
                </div>

                <div>
                    <v-label for="teals">
                        Teams
                    </v-label>
                  <v-autocomplete
                      ref="selectEdit"
                      v-model="teamIds"
                      :items="teams"
                      outlined
                      dense
                      chips
                      small-chips
                      multiple
                      menu-props="closeOnClick"
                      item-text="name"
                      item-value="id"
                      @click.stop/>
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
    props: ['activity', 'projects', 'features', 'activityCodes', 'subActivityCodes', 'types', 'subTypes', 'teams', 'otherSettings'],
    data: () => ({
        defaultItem: {
            type: '',
            subType: '',
            activity: '',
            subActivity: '',
            label: '',
            project: {
                id: '',
                label: ''
            },
            feature: {
                id: '',
                label: ''
            },
        },
        teamIds: []
    }),

     computed: {
         filtredSubTypes() {
      return this.subTypes.filter(subType => {
        return (
          this.activity.type.id && subType.type.id===this.activity.type.id
        );
      });
    }
        },
  mounted() {
    window.addEventListener("click",() => {
      //this.$refs.selectEdit.blur();
    });
  },
    methods: {
        save() {
            const teams = []
            for (const team of this.teamIds) {
                const t_ = {
                    id: team,
                }
                teams.push(t_)
            }
            this.activity.teams = teams
            this.$emit('save', this.activity)
            this.$refs.editActivityDrawer.close()
        },
        cancel() {
            this.$refs.editActivityDrawer.close()
        },
        open() {
            setTimeout(() => {
            this.teamIds=[]
            
            if (this.activity.teams!== undefined && this.activity.teams.length > 0) {
                this.teamIds = this.activity.teams.map(a => a.id);
            }
            }, 100);
            this.$refs.editActivityDrawer.open()
        },

    }
}
</script>
