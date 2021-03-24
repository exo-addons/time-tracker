<template>
<exo-drawer ref="addActivityDrawer" right class="">
    <template slot="title">
        Add activity
    </template>
    <template slot="content">
        <div>
            <v-form ref="form" v-model="valid">
                <div>
                    <v-label for="type">
                        Type *
                    </v-label>
                    <select v-model="editedActivity.type" name="type" class="input-block-level ignore-vuetify-classes my-3">
                        <option v-for="item in types" :key="item.id" :value="item">
                            {{ item.displayLabel}}
                        </option>
                    </select>
                </div>
                <div>
                    <v-label for="subType">
                        Sub Type *
                    </v-label>
                    <select v-model="editedActivity.subType" name="subType" :disabled="!editedActivity.type.id" class="input-block-level ignore-vuetify-classes my-3">
                        <option v-for="item in filtredSubTypes" :key="item.id" :value="item">
                            {{ item.displayLabel}}
                        </option>
                    </select>
                </div>
                <div>
                    <v-label for="activityCode">
                        Activity *
                    </v-label>
                    <select v-model="editedActivity.activityCode" name="activityCode" class="input-block-level ignore-vuetify-classes my-3">
                        <option v-for="item in activityCodes" :key="item.id" :value="item">
                            {{ item.displayLabel}}
                        </option>
                    </select>
                </div>
                <div>
                    <v-label for="subActivityCode">
                        Sub Activity *
                    </v-label>
                    <select v-model="editedActivity.subActivityCode" name="subActivityCode" class="input-block-level ignore-vuetify-classes my-3">
                        <option v-for="item in subActivityCodes" :key="item.id" :value="item">
                            {{ item.displayLabel}}
                        </option>
                    </select>
                </div>
                <div>
                    <v-label for="label">
                        Label *
                    </v-label>
                    <input ref="label" v-model="editedActivity.label" type="text" name="label" class="input-block-level ignore-vuetify-classes my-3" />
                </div>
                <div>
                    <v-label for="project">
                        Project *
                    </v-label>
                    <select v-model="editedActivity.project" name="project" class="input-block-level ignore-vuetify-classes my-3">
                        <option v-for="item in projects" :key="item.id" :value="item">
                            {{ item.displayLabel}}
                        </option>
                    </select>
                </div>
                <div>
                    <v-label for="feature">
                        Feature
                    </v-label>
                    <select v-model="editedActivity.feature" name="feature" class="input-block-level ignore-vuetify-classes my-3">
                        <option v-for="item in features" :key="item.id" :value="item">
                            {{ item.displayLabel}}
                        </option>
                    </select>
                </div>

                <div>
                    <v-label for="teams">
                        Teams *
                    </v-label>
                    <v-autocomplete v-model="editedActivity.teams" :items="teams" menu-props="closeOnClick"  outlined dense chips small-chips multiple item-text="name" item-value="id"></v-autocomplete>
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
            <v-btn :disabled="isDisabled" class="btn btn-primary" @click="save()">
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
    props: ['projects', 'features', 'activityCodes', 'subActivityCodes', 'types', 'subTypes', 'teams'],
    data: () => ({
        defaultItem: {
              type: {
                id: '',
                code: '',
                label: ''
            },
            subType: {
                id: '',
                code: '',
                label: ''
            },
            activity: {
                id: '',
                code: '',
                label: ''
            },
            subActivity: {
                id: '',
                code: '',
                label: ''
            },
            label: '',
            project: {
                id: '',
                code: '',
                label: ''
            },
            feature: {
                id: '',
                code: '',
                label: ''
            },
        },
        editedActivity: {
             type: {
                id: '',
                code: '',
                label: ''
            },
            subType: {
                id: '',
                code: '',
                label: ''
            },
            activity: {
                id: '',
                code: '',
                label: ''
            },
            subActivity: {
                id: '',
                code: '',
                label: ''
            },
            label: '',
            project: {
                id: '',
                code: '',
                label: ''
            },
            feature: {
                id: '',
                code: '',
                label: ''
            },
        },
    }),


     computed: {
         isDisabled: function(){
                return (this.editedActivity && !(
                this.isNotEmpty(this.editedActivity.project)
                && this.isNotEmpty(this.editedActivity.label)
                && this.isNotEmpty(this.editedActivity.activityCode)
                && this.isNotEmpty(this.editedActivity.subActivityCode)
                && this.isNotEmpty(this.editedActivity.subType
                && this.isNotEmpty(this.editedActivity.type))))
                },
         filtredSubTypes() {
      return this.subTypes.filter(subType => {
        return (
          this.editedActivity.type.id && subType.type.id===this.editedActivity.type.id
        );
      });
    }
        },
    methods: {
        isNotEmpty(str){
              return(str!=null && str.id!==null && str.id!=="")
            },
        save() {
            const teams = []
            for (const team of this.editedActivity.teams) {
                const t_ = {
                    id: team,
                }
                teams.push(t_)
            }
            this.editedActivity.teams = teams
            this.$emit('save', this.editedActivity)
            this.resetForm()
            this.$refs.addActivityDrawer.close()
        },
        cancel() {
            this.resetForm()
            this.$refs.addActivityDrawer.close()
        },
        open() {
            this.resetForm()
            this.$refs.addActivityDrawer.open()
        },
        resetForm(){
          this.editedActivity.type={id: '',  code: '', label: ''}
          this.editedActivity.subType={id: '',  code: '', label: ''}
          this.editedActivity.activityCode={id: '',  code: '', label: ''}
          this.editedActivity.subActivityCode={id: '',  code: '', label: ''}
          this.editedActivity.project={id: '',  code: '', label: ''}
          this.editedActivity.feature={id: '',  code: '', label: ''}
          this.editedActivity.label=''
          this.editedActivity.teams=[]
        }
    }
}
</script>
