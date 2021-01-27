<template>
<exo-drawer ref="editClientDrawer" right class="">
    <template slot="title">
        Edit client
    </template>
    <template slot="content">
        <div>
            <v-form ref="form" v-model="valid">
                <div>
                    <v-label for="code">
                        Code
                    </v-label>
                    <input ref="code" v-model="client.code" type="text" name="code" class="input-block-level ignore-vuetify-classes my-3" />
                </div>

                <div>
                    <v-label for="label">
                        Label
                    </v-label>
                    <input ref="label" v-model="client.label" type="text" name="label" class="input-block-level ignore-vuetify-classes my-3" />
                </div>

            </v-form>
        </div>
    </template>
    <template slot="footer">
        <div class="d-flex">
            <v-spacer />
            <v-btn class="btn mr-2" @click="cancel()">
                <template>
                    Cancel}
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
    props: ['client'],

    methods: {
        save() {
            this.$emit('save', this.client)
            this.$refs.editClientDrawer.close()
        },
        cancel() {
            this.$refs.editClientDrawer.close()
        },
        open() {
            this.$refs.editClientDrawer.open()
        },

    }
}
</script>
