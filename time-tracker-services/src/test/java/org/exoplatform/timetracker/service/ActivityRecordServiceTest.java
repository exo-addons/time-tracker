package org.exoplatform.timetracker.service;

import junit.framework.TestCase;
import org.exoplatform.commons.utils.CommonsUtils;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.component.RequestLifeCycle;
import org.exoplatform.timetracker.dto.ActivityRecord;
import org.exoplatform.timetracker.dto.RecordsAccessList;

public class ActivityRecordServiceTest extends TestCase {

    ActivityRecordService activityRecordService;
    private PortalContainer container;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        container = PortalContainer.getInstance();
        activityRecordService = CommonsUtils.getService(ActivityRecordService.class);
        begin();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        end();
    }

    private void begin() {
        RequestLifeCycle.begin(container);
    }

    private void end() {
        RequestLifeCycle.end();
    }

    public void testGetActivityRecordsListWithoutFilters() {
        // the records query is built at runtime with fetch joins: execute it
        // for real so Hibernate parses and runs it against the database
        RecordsAccessList records = activityRecordService.getActivityRecordsList(null, null, null, null, null, null, null,
                                                                                 null, null, null, null, null, null, null,
                                                                                 0, 0, null, false);
        assertNotNull(records);
        assertNotNull(records.getActivityRecords());
        // without pagination the size must match the returned list, without a count query
        assertEquals(Long.valueOf(records.getActivityRecords().size()), records.getSize());
    }

    public void testGetActivityRecordsListWithAllFilters() {
        RecordsAccessList records = activityRecordService.getActivityRecordsList("search", "1", "1", "1", "1", "1", "1",
                                                                                 "1", "1", "2020-01-01", "2020-12-31", "root",
                                                                                 "Tunis", "TUN", 0, 20, "activityDate", true);
        assertNotNull(records);
        assertNotNull(records.getActivityRecords());
        assertNotNull(records.getSize());
    }

    public void testCreateActivityRecordsOverRange() throws Exception {
        ActivityRecord record = new ActivityRecord();
        record.setUserName("root");
        record.setDescription("range duplication test");
        record.setTime(8f);
        record.setLocation("TUN");
        record.setOffice("TUN");
        record.setActivityDate("2020-01-06");

        // 2020-01-06 is a Monday, 2020-01-12 a Sunday: 5 workdays, 7 with weekends
        assertEquals(5, activityRecordService.createActivityRecords(record, "2020-01-06", "2020-01-12", false));
        assertEquals(7, activityRecordService.createActivityRecords(record, "2020-01-06", "2020-01-12", true));

        try {
            activityRecordService.createActivityRecords(record, "2020-01-12", "2020-01-06", false);
            fail("reversed range should be rejected");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            activityRecordService.createActivityRecords(record, "2020-01-06", "2021-02-06", false);
            fail("range over a year should be rejected");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            activityRecordService.createActivityRecords(record, "not-a-date", "2020-01-06", false);
            fail("invalid date should be rejected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}
