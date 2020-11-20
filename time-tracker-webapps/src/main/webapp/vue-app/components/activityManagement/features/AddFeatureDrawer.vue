<template>
<exo-drawer ref="addFeatureDrawer" right class="">
    <template slot="title">
        Add Feature
    </template>
    <template slot="content">
        <div>
            <v-form ref="form" v-model="valid">
                <v-row>
                    <v-label for="code">
                        Code
                    </v-label>
                    <input ref="code" v-model="feature.code" type="text" name="code" class="input-block-level ignore-vuetify-classes my-3" />
                </v-row>

                <v-row>
                    <v-label for="label">
                        Label
                    </v-label>
                    <input ref="label" v-model="feature.label" type="text" name="label" class="input-block-level ignore-vuetify-classes my-3" />
                </v-row>
                <v-row>
                    <v-label for="spec">
                        Spec
                    </v-label>
                    <input ref="label" v-model="feature.spec" type="text" name="spec" class="input-block-level ignore-vuetify-classes my-3" />
                </v-row>
                <v-row>
                    <v-label for="exo">
                        eXo
                    </v-label>
                    <input ref="label" v-model="feature.exo" type="text" name="label" class="input-block-level ignore-vuetify-classes my-3" />
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

    data: () => ({
        defaultItem: {
            code: '',
            label: '',
            spec: '',
            exo: '',
        },
        feature: {
            code: '',
            label: '',
            spec: '',
            exo: '',
        },
    }),

    methods: {
        save() {
            this.$emit('save', this.feature)
            this.feature = this.defaultItem
            this.$refs.addFeatureDrawer.close()
        },
        cancel() {
            this.feature = this.defaultItem
            this.$refs.addFeatureDrawer.close()
        },
        open() {
            this.feature = this.defaultItem
            this.$refs.addFeatureDrawer.open()
        },

    }
}
</script>