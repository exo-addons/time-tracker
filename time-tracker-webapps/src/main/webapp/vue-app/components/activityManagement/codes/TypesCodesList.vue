<template>
  <div>
    <v-flex>
      <v-data-table
        :headers="typeHeaders"
        :items="types"
        :search="searchType"
        sort-by="label"
        class="elevation-1">
        <template v-slot:top>
          <v-toolbar flat color="white">
            <v-toolbar-title>
              {{ $t("exo.timeTracker.codes.typesCodesList.type.toolbarTitle") }}
            </v-toolbar-title>
            <v-divider
              class="mx-4"
              inset
              vertical />
            <v-spacer />
            <v-divider
              class="mx-4"
              inset
              vertical />
            <v-text-field
              v-model="searchType"
              placeholder="Filter"
              prepend-inner-icon="fa-filter"
              class="inputFilter pa-0 mr-3 my-auto"
              clearable />  
            <button
              class="btn btn-primary pull-left"
              type="button"
              @click="openAddTypeDrawer">
              <i class="uiIconSocSimplePlus uiIconSocWhite"></i>
              {{ $t("exo.timeTracker.codes.text.add.type") }}
            </button>
          </v-toolbar>
        </template>
        <template v-slot:item.action="{ item }">
          <v-icon
            small
            class="mr-2"
            @click="openEditTypeDrawer(item)">
            edit
          </v-icon>
          <v-icon small @click="deleteType(item)">
            delete
          </v-icon>
        </template>
        <template v-slot:no-data>{{ $t("exo.timeTracker.codes.typesCodesList.textIfNoType") }}</template>
      </v-data-table>
    </v-flex>
    <v-flex>
      <v-data-table
        :headers="subTypeHeaders"
        :items="subTypes"
        :search="searchSubType"
        sort-by="label"
        class="elevation-1">
        <template v-slot:top>
          <v-toolbar flat color="white">
            <v-toolbar-title>
              {{ $t("exo.timeTracker.codes.typesCodesList.subType.toolbarTitle") }}
            </v-toolbar-title>
            <v-divider
              class="mx-4"
              inset
              vertical />
            <v-spacer />
            <v-divider
              class="mx-4"
              inset
              vertical />
            <v-text-field
              v-model="searchSubType"
              placeholder="Filter"
              prepend-inner-icon="fa-filter"
              class="inputFilter pa-0 mr-3 my-auto"
              clearable />  
            <button
              class="btn btn-primary pull-left"
              type="button"
              @click="openAddSubTypeDrawer">
              <i class="uiIconSocSimplePlus uiIconSocWhite"></i>
              {{ $t("exo.timeTracker.codes.text.add.SubType") }}
            </button>
          </v-toolbar>
        </template>
        <template v-slot:item.type="{ item }">
          {{ item.code }} - {{ item.label }}
        </template>
        <template v-slot:item.action="{ item }">
          <v-icon
            small
            class="mr-2"
            @click="openEditSubTypeDrawer(item)">
            edit
          </v-icon>
          <v-icon small @click="deleteSubType(item)">
            delete
          </v-icon>
        </template>
        <template v-slot:no-data>
          {{ $t("exo.timeTracker.codes.typesCodesList.textIfNoSubType") }}
        </template>
      </v-data-table>
    </v-flex>
    <add-type-drawer
      ref="addTypeDrawer"
      :activity-codes="activityCodes"
      @save="addType" />
    <add-sub-type-drawer
      ref="addSubTypeDrawer"
      :types="types"
      @save="addSubType" />
    <edit-type-drawer ref="editTypeDrawer" @save="editType" />
    <edit-sub-type-drawer
      ref="editSubTypeDrawer"
      :types="types"
      @save="editSubType" />
  </div>
</template>

<script>
import addTypeDrawer from './AddTypeDrawer.vue';
import addSubTypeDrawer from './AddSubTypeDrawer.vue';
import editTypeDrawer from './EditTypeDrawer.vue';
import editSubTypeDrawer from './EditSubTypeDrawer.vue';
export default {
  components: {
    addTypeDrawer,
    addSubTypeDrawer,
    editTypeDrawer,
    editSubTypeDrawer,
  },
  props: {
    types: {
      type: Array,
      default: () => null,
    },
    subTypes: {
      type: Array,
      default: () => null,
    }
  },
  data: () => ({
    searchType: '',
    searchSubType: '',
    valid: true,
    editedIndex: -1,
    editedItem: {
      code: '',
      label: ''
    },
    defaultItem: {
      code: '',
      label: ''
    },
  }),
  computed: {
    typeHeaders() {
      return [{
        text: 'label',
        align: 'center',
        sortable: true,
        value: 'label',
      },
      {
        text: 'code',
        align: 'center',
        sortable: true,
        value: 'code',
      },
      {
        text: 'Actions',
        align: 'center',
        sortable: true,
        value: 'action',
      }
      ];
    },
    subTypeHeaders() {
      return [{
        text: 'label',
        align: 'center',
        sortable: true,
        value: 'label',
      },
      {
        text: 'code',
        align: 'center',
        sortable: true,
        value: 'code',
      },
      {
        text: 'type',
        align: 'center',
        sortable: true,
        value: 'type',
      },
      {
        text: 'Actions',
        align: 'center',
        sortable: true,
        value: 'action',
      }
      ];
    }
  },
  methods: {
    deleteType(item) {
      const index = this.types.indexOf(item);
      this.types.splice(index, 1);
      this.$emit('deleteType', item);
    },
    deleteSubType(item) {
      const index = this.subTypes.indexOf(item);
      this.subTypes.splice(index, 1);
      this.$emit('deleteSubType', item);
    },
    openAddTypeDrawer() {
      this.$refs.addTypeDrawer.open();
    },
    openAddSubTypeDrawer() {
      this.$refs.addSubTypeDrawer.open();
    },
    addType(type) {
      //this.types.push(type)
      this.$emit('addType', type);
    },
    addSubType(subType) {
      // this.subTypes.push(subType)
      this.$emit('addSubType', subType);
    },
    openEditTypeDrawer(item) {
      this.$refs.editTypeDrawer.open(item);
    },
    openEditSubTypeDrawer(item) {
      this.$refs.editSubTypeDrawer.open(item);
    },
    editType(type) {
      //this.types.push(type)
      this.$emit('editType', type);
    },
    editSubType(subType) {
      // this.subTypes.push(subType)
      this.$emit('editSubType', subType);
    },
  }
};
</script>

<style>
#codesManagementApp {
    overflow: hidden;
    padding: 10px 20px;
}

select {
    width: auto;
}

#codesManagementApp .v-input input {
    margin-bottom: 0;
    border: 0;
    box-shadow: none;
}

#codesManagementApp .v-toolbar .v-input {
    margin-left: 18px;
}

#codesManagementApp .v-data-table {
    width: 100%;
}
</style>
