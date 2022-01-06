<template>
  <exo-drawer
    ref="editClientDrawer"
    right
    class="">
    <template slot="title">
      {{ $t("exo.timeTracker.clients.editClientDrawer.drawerTitel") }}
    </template>
    <template slot="content">
      <div>
        <v-form ref="form" v-model="valid">
          <div>
            <v-label for="code">
              {{ $t("exo.timeTracker.clients.editClientDrawer.drawerLabelCode") }}
            </v-label>
            <input
              ref="code"
              v-model="client.code"
              type="text"
              name="code"
              class="input-block-level ignore-vuetify-classes my-3">
          </div>
          <div>
            <v-label for="label">
              {{ $t("exo.timeTracker.clients.editClientDrawer.drawerLabelClientLabel") }}
            </v-label>
            <input
              ref="label"
              v-model="client.label"
              type="text"
              name="label"
              class="input-block-level ignore-vuetify-classes my-3">
          </div>
          <div>
            <v-label for="salesOrders">
              {{ $t("exo.timeTracker.clients.editClientDrawer.drawerLabelSalesOrders") }}
            </v-label>
            <div class="text-center">
              <div v-if="client.salesOrders && client.salesOrders.length>0">  
                <v-chip
                  v-for="item in client.salesOrders"
                  :key="item.name"
                  class="ma-2"
                  color="blue"
                  outlined
                  pill
                  small>
                  <span class="pr-2">
                    {{ item.name }}
                  </span>
                  <v-icon
                    x-small
                    class="pr-0"
                    v-on="on"
                    @click="editSo()">
                    edit
                  </v-icon>
                  <v-icon
                    x-small
                    class="pr-0"
                    @click="deleteSo()">
                    close
                  </v-icon>
                </v-chip>
                <v-icon right @click="openAddSODrawer()">
                  mdi-plus
                </v-icon>
              </div>
              <div v-else>
                <span class="pr-2">There no Sales orders for this client</span>
                <v-icon right @click="openAddSODrawer()">
                  mdi-plus
                </v-icon>
              </div>
            </div>
          </div>
        </v-form>
        <add-so-drawer ref="addSODrawer" @save="addSo" />
      </div>
    </template>
    <template slot="footer">
      <div class="d-flex">
        <v-spacer />
        <v-btn class="btn mr-2" @click="cancel()">
          <template>
            {{ $t("exo.timeTracker.clients.clientDrawer.drawerButtonCancel") }}
          </template>
        </v-btn>
        <v-btn class="btn btn-primary" @click="save()">
          <template>
            {{ $t("exo.timeTracker.clients.clientDrawer.drawerButtonSave") }}
          </template>
        </v-btn>
      </div>
    </template>
  </exo-drawer>
</template>

<script>
import addSoDrawer from './AddSODrawer.vue';
export default {
  components: {
    addSoDrawer,
  },
  props: {
    client: {
      type: Object,
      default: ()=> {
        return {
          code: '',
          label: '',
        };
      }
    }
  },
  methods: {
    save() {
      this.$emit('save', this.client);
      this.$refs.editClientDrawer.close();
    },
    cancel() {
      this.$refs.editClientDrawer.close();
    },
    open() {
      this.$refs.editClientDrawer.open();
    },
    addSo(so) {
      this.addSalesOrder(so);          
    },
    deleteSo(so) {
      this.client.salesOrders.push(so);
    },
    openAddSODrawer() {
      this.$refs.addSODrawer.open();
    },
    addSalesOrder(so) {
      so.client=this.client;
      fetch('/portal/rest/timetracker/salesOrdersmgn/salesOrder', {
        method: 'post',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(so),
      })
        .then((result) => {
          if (!result.ok) {
            throw result;
          }
        })
        .then(() => {
          this.getSalesOrders();
          this.displaySusccessMessage('so added');
        })
        .catch((result) => {
          this.getSalesOrders();
          result.text().then((body) => {
            this.displayErrorMessage(body);
          });
        });
    },
    getSalesOrders() {
      fetch(`/portal/rest/timetracker/salesOrdersmgn/salesOrder/${this.client.id}`, {
        credentials: 'include',
      })
        .then((resp) => resp.json())
        .then((resp) => {
          this.client.salesOrders = resp;
        });
    },
  }
};
</script>
