package org.exoplatform.timetracker.dto;

import lombok.Data;

import java.util.List;

@Data

public class RecordsAccessList {

    public RecordsAccessList() {
    }
    public RecordsAccessList(List<ActivityRecord> activityRecords, Long size) {
    this.activityRecords=activityRecords;
    this.size=size;
    }
    private List<ActivityRecord>  activityRecords;
    private Long size;
}

