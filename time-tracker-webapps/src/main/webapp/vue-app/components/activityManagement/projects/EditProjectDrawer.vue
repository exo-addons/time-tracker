<template>
<exo-drawer ref="editProjectDrawer" right class="">
    <template slot="title">
        Edit project
    </template>
    <template slot="content">
        <div>
            <v-form ref="form" v-model="valid">
                <div>
                    <v-label for="code">
                       Code
                    </v-label>
                    <input ref="code" v-model="project.code" type="text" name="code" class="input-block-level ignore-vuetify-classes my-3" />
                </div>
              
                <div>
                    <v-label for="label">
                       Label
                    </v-label>
                    <input ref="label" v-model="project.label" type="text" name="label" class="input-block-level ignore-vuetify-classes my-3" />
                </div>

                
                  <div>
                    <v-label for="client">
                        Client
                    </v-label>

                    <select v-model="project.client" name="client" class="input-block-level ignore-vuetify-classes my-3">
                        <option v-for="item in clients" :key="item.id" :value="item">
                            {{ item.label}}
                        </option>
                    </select>                </div>
               

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
    props: ['project','clients'],


    methods: {
        save() {
            this.$emit('save', this.project)
            this.$refs.editProjectDrawer.close()
        },
        cancel() {
            this.$refs.editProjectDrawer.close()
        },
        open() {
            this.$refs.editProjectDrawer.open()
        },

    }
}
</script>

