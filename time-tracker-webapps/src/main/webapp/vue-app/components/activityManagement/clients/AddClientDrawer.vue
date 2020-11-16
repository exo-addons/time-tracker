<template>
<exo-drawer ref="addClientDrawer" right class="">
    <template slot="title">
        Add Client
    </template>
    <template slot="content">
        <div>
            <v-form ref="form" v-model="valid">
                <v-row>
                    <v-label for="code">
                        Code
                    </v-label>
                    <input ref="code" v-model="client.code" type="text" name="code" class="input-block-level ignore-vuetify-classes my-3" />
                </v-row>

                <v-row>
                    <v-label for="label">
                        Label
                    </v-label>
                    <input ref="label" v-model="client.label" type="text" name="label" class="input-block-level ignore-vuetify-classes my-3" />
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
        },
        client: {
            code: '',
            label: '',
        },
    }),

    methods: {
        save() {
            this.$emit('save', this.client)
            this.client = this.defaultItem
            this.$refs.addClientDrawer.close()
        },
        cancel() {
            this.client = this.defaultItem
            this.$refs.addClientDrawer.close()
        },
        open() {
            this.client = this.defaultItem
            this.$refs.addClientDrawer.open()
        },

    }
}
</script>
