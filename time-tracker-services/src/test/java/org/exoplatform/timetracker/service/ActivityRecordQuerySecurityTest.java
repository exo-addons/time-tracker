/**
 * Copyright (C) 2026 eXo Platform SAS.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.exoplatform.timetracker.service;

import java.util.List;
import java.util.stream.Collectors;

import org.exoplatform.commons.utils.CommonsUtils;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.component.RequestLifeCycle;
import org.exoplatform.timetracker.dto.ActivityRecord;
import org.exoplatform.timetracker.dto.RecordsAccessList;

import junit.framework.TestCase;

/**
 * The records query is built from text at runtime. The free text filters
 * ({@code search} and {@code userName}) must neither change the query nor let
 * one user read the records of another.
 */
public class ActivityRecordQuerySecurityTest extends TestCase {

  private static final String ALICE = "root";

  private static final String BOB   = "john";

  private static final String TOKEN = "zzsecuritytoken";

  private ActivityRecordService activityRecordService;

  private PortalContainer       container;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    container = PortalContainer.getInstance();
    activityRecordService = CommonsUtils.getService(ActivityRecordService.class);
    RequestLifeCycle.begin(container);
    // idempotent: every test creates the same two records once
    if (list(TOKEN, null).isEmpty()) {
      create(ALICE, "alice " + TOKEN + " private");
      create(BOB, "bob " + TOKEN + " public");
    }
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    RequestLifeCycle.end();
  }

  /** A search that matches several users must stay inside the user filter. */
  public void testSearchDoesNotEscapeTheUserFilter() {
    List<String> users = list(TOKEN, BOB).stream().map(ActivityRecord::getUserName).collect(Collectors.toList());
    assertEquals("only the records of the asked user", List.of(BOB), users);
    assertEquals(1L, count(TOKEN, BOB));
  }

  /** The same, with the other filters: a date range must also still apply. */
  public void testSearchDoesNotEscapeTheDateFilter() {
    assertTrue(activityRecordService.getActivityRecordsList(TOKEN, null, null, null, null, null, null, null, null,
                                                            "2001-01-01", "2001-01-31", null, null, null, 0, 0, null, false)
                                    .getActivityRecords()
                                    .isEmpty());
  }

  /** A user name is a value, never a piece of query. */
  public void testUserNameInjectionFindsNothing() {
    for (String evil : new String[] { "x' OR '1'='1", "' OR 1=1 OR userName='", BOB + "' OR '1'='1" }) {
      assertTrue("no record for: " + evil, list(null, evil).isEmpty());
      assertEquals("no count for: " + evil, 0L, count(null, evil));
    }
  }

  /** A search text is a value, never a piece of query. */
  public void testSearchInjectionFindsNothing() {
    for (String evil : new String[] { "x' OR '1'='1", "%' or lower(activityRecord.userName) like '%" }) {
      assertTrue("no record for: " + evil, list(evil, BOB).isEmpty());
      assertEquals("no count for: " + evil, 0L, count(evil, BOB));
    }
  }

  /** What stays possible: the search of the description and the user name, and a single quote in it. */
  public void testSearchStillWorks() {
    assertEquals(2, list(TOKEN, null).size());
    assertEquals(1, list("alice ", null).size());
    assertTrue(list(BOB, null).size() >= 1);
    assertTrue(list("it's not there", null).isEmpty());
  }

  private List<ActivityRecord> list(String search, String userName) {
    RecordsAccessList result = activityRecordService.getActivityRecordsList(search, null, null, null, null, null, null,
                                                                            null, null, null, null, userName, null, null,
                                                                            0, 0, null, false);
    return result.getActivityRecords();
  }

  private long count(String search, String userName) {
    return activityRecordService.countActivityRecords(search, null, null, null, null, null, null, null, null, null, null,
                                                      userName, null, null);
  }

  private void create(String user, String description) throws Exception {
    ActivityRecord record = new ActivityRecord();
    record.setUserName(user);
    record.setDescription(description);
    record.setTime(1f);
    record.setLocation("TUN");
    record.setOffice("TUN");
    record.setActivityDate("2020-03-02");
    activityRecordService.createActivityRecord(record);
  }
}
