<template>
<exo-drawer ref="addActivityDrawer" right class="">
    <template slot="title">
        Add activity
    </template>
    <template slot="content">
        <div>
            <v-form ref="form" v-model="valid">
                 <v-row>
                    <v-label for="type">
                        Type
                    </v-label>
                    <select v-model="editedActivity.type" name="type" class="input-block-level ignore-vuetify-classes my-3">
                        <option v-for="item in types" :key="item.id" :value="item">
                            {{ item.label}}
                        </option>
                    </select>
                </v-row>
                 <v-row>
                    <v-label for="subType">
                        Sub Type
                    </v-label>
                    <select v-model="editedActivity.subType" name="subType" class="input-block-level ignore-vuetify-classes my-3">
                        <option v-for="item in subTypes" :key="item.id" :value="item">
                            {{ item.label}}
                        </option>
                    </select>
                </v-row>
                 <v-row>
                    <v-label for="activityCode">
                        Activity 
                    </v-label>
                    <select v-model="editedActivity.activityCode" name="activityCode" class="input-block-level ignore-vuetify-classes my-3">
                        <option v-for="item in activityCodes" :key="item.id" :value="item">
                            {{ item.label}}
                        </option>
                    </select>
                </v-row>
                 <v-row>
                    <v-label for="subActivityCode">
                       Sub Activity
                    </v-label>
                    <select v-model="editedActivity.subActivityCode" name="subActivityCode" class="input-block-level ignore-vuetify-classes my-3">
                        <option v-for="item in subActivityCodes" :key="item.id" :value="item">
                            {{ item.label}}
                        </option>
                    </select>
                </v-row>
                <v-row>
                    <v-label for="label">
                        Label
                    </v-label>
                    <input ref="label" v-model="editedActivity.label" type="text" name="label" class="input-block-level ignore-vuetify-classes my-3" />
                </v-row>
                <v-row>
                    <v-label for="project">
                       Project
                    </v-label>
                    <select v-model="editedActivity.project" name="project" class="input-block-level ignore-vuetify-classes my-3">
                        <option v-for="item in projects" :key="item.id" :value="item">
                            {{ item.label}}
                        </option>
                    </select>
                </v-row>
                <v-row>
                    <v-label for="feature">
                        Feature
                    </v-label>
                    <select v-model="editedActivity.feature" name="feature" class="input-block-level ignore-vuetify-classes my-3">
                        <option v-for="item in features" :key="item.id" :value="item">
                            {{ item.label}}
                        </option>
                    </select>
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
    props: ['projects', 'features','activityCodes','subActivityCodes','types','subTypes'],
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
        editedActivity: {
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
    }),

    methods: {
        save() {
            this.$emit('save', this.editedActivity)
            this.editedActivity = this.defaultItem
            this.$refs.addActivityDrawer.close()
        },
        cancel() {
            this.editedActivity = this.defaultItem
            this.$refs.addActivityDrawer.close()
        },
        open() {
            this.editedActivity = this.defaultItem
            this.$refs.addActivityDrawer.open()
        },

    }
}
</script>

<style>
.drawerContent {
    padding: 15px 27px;
}
</style>
