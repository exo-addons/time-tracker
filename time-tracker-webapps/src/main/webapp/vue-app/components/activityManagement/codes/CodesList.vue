<template>
  <div>
    <v-flex>
      <v-data-table
        :headers="activityHeaders"
        :items="activityCodes"
        :search="searchActivities"
        sort-by="label"
        class="elevation-1">
        <template v-slot:top>
          <v-toolbar flat color="white">
            <v-toolbar-title>
              {{ $t("exo.timeTracker.codes.codesList.activityCodes.toolbarTitle") }}
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
              v-model="searchActivities"
              placeholder="Filter"
              prepend-inner-icon="fa-filter"
              class="inputFilter pa-0 mr-3 my-auto"
              clearable />  
            <button
              class="btn btn-primary pull-left"
              type="button"
              @click="openAddActivityCodeDrawer">
              <i class="uiIconSocSimplePlus uiIconSocWhite"></i>
              {{ $t("exo.timeTracker.codes.codesList.button.openAddActivityCodeDrawer") }}
            </button>
          </v-toolbar>
        </template>
        <template v-slot:item.action="{ item }">
          <v-icon
            small
            class="mr-2"
            @click="openEditActivityCodeDrawer(item)">
            edit
          </v-icon>
          <v-icon small @click="deleteActivityCode(item)">
            delete
          </v-icon>
        </template>
        <template v-slot:no-data>
          {{ $t("exo.timeTracker.codes.codesList.textIfNoCode") }}
        </template>
      </v-data-table>
    </v-flex>
    <v-flex>
      <v-data-table
        :headers="subActivityCodeHeaders"
        :items="subActivityCodes"
        :search="searchSubActivities"
        sort-by="label"
        class="elevation-1">
        <template v-slot:top>
          <v-toolbar flat color="white">
            <v-toolbar-title>
              {{ $t("exo.timeTracker.codes.codesList.subActivityCode.toolbarTitle") }}
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
              v-model="searchSubActivities"
              placeholder="Filter"
              prepend-inner-icon="fa-filter"
              class="inputFilter pa-0 mr-3 my-auto"
              clearable />  
            <button
              class="btn btn-primary pull-left"
              subActivityCode="button"
              @click="openAddSubActivityCodeDrawer">
              <i class="uiIconSocSimplePlus uiIconSocWhite"></i>
              {{ $t("exo.timeTracker.codes.codesList.button.openAddSubActivityCodeDrawer") }}
            </button>
          </v-toolbar>
        </template>
        <template v-slot:item.action="{ item }">
          <v-icon
            small
            class="mr-2"
            @click="openEditSubActivityCodeDrawer(item)">
            edit
          </v-icon>
          <v-icon small @click="deleteSubActivityCode(item)">
            delete
          </v-icon>
        </template>
        <template v-slot:no-data>
          {{ $t("exo.timeTracker.codes.codesList.textIfNoSubActivityCode") }}
        </template>
      </v-data-table>
    </v-flex>
    <add-activity-code-drawer ref="addActivityCodeDrawer" @save="addActivityCode" />
    <add-sub-activity-code-drawer ref="addSubActivityCodeDrawer" @save="addSubActivityCode" />
    <edit-activity-code-drawer ref="editActivityCodeDrawer" @save="editActivityCode" />
    <edit-sub-activity-code-drawer ref="editSubActivityCodeDrawer" @save="editSubActivityCode" />
  </div>
</template>

<script>
import addActivityCodeDrawer from './AddActivityCodeDrawer.vue';
import addSubActivityCodeDrawer from './AddSubActivityCodeDrawer.vue';
import editActivityCodeDrawer from './EditActivityCodeDrawer.vue';
import editSubActivityCodeDrawer from './EditSubActivityCodeDrawer.vue';
export default {
  components: {
    addActivityCodeDrawer,
    addSubActivityCodeDrawer,
    editActivityCodeDrawer,
    editSubActivityCodeDrawer,
  },
  props: {
    activityCodes: {
      type: Array,
      default: () => null,
    },
    subActivityCodes: {
      type: Array,
      default: () => null,
    }
  },
  data: () => ({
    searchActivities: '',
    searchSubActivities: '',
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
    activityHeaders() {
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
      },
      ];
    },
    subActivityCodeHeaders() {
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
      },
      ];
    }
  },
  methods: {
    deleteActivityCode(item) {
      const index = this.activityCodes.indexOf(item);
      this.activityCodes.splice(index, 1);
      this.$emit('deleteActivityCode', item);
    },
    deleteSubActivityCode(item) {
      const index = this.subActivityCodes.indexOf(item);
      this.subActivityCodes.splice(index, 1);
      this.$emit('deleteSubActivityCode', item);
    },
    openAddActivityCodeDrawer() {
      this.$refs.addActivityCodeDrawer.open();
    },
    openAddSubActivityCodeDrawer() {
      this.$refs.addSubActivityCodeDrawer.open();
    },
    addActivityCode(activityCode) {
      // this.activityCodes.push(activityCode)
      this.$emit('addActivityCode', activityCode);
    },
    addSubActivityCode(subActivityCode) {
      // this.subActivityCodes.push(subActivityCode)
      this.$emit('addSubActivityCode', subActivityCode);
    },
    openEditActivityCodeDrawer(item) {
      this.$refs.editActivityCodeDrawer.open(item);
    },
    openEditSubActivityCodeDrawer(item) {
      this.$refs.editSubActivityCodeDrawer.open(item);
    },
    editActivityCode(activityCode) {
      // this.activityCodes.push(activityCode)
      this.$emit('editActivityCode', activityCode);
    },
    editSubActivityCode(subActivityCode) {
      // this.subActivityCodes.push(subActivityCode)
      this.$emit('editSubActivityCode', subActivityCode);
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
