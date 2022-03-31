package org.exoplatform.timetracker.dto;

import lombok.Data;

import java.util.List;

@Data

/**
 * <p>RecordsAccessList class.</p>
 *
 * @author medamine
 * @version $Id: $Id
 */
public class RecordsAccessList {
	private List<ActivityRecord>  activityRecords;
    private Long size;

    /**
     * <p>Constructor for RecordsAccessList.</p>
     */
    public RecordsAccessList() {
    }
    /**
     * <p>Constructor for RecordsAccessList.</p>
     *
     * @param activityRecords a {@link java.util.List} object.
     * @param size a {@link java.lang.Long} object.
     */
    public RecordsAccessList(List<ActivityRecord> activityRecords, Long size) {
    this.activityRecords=activityRecords;
    this.size=size;
    }
    
}

