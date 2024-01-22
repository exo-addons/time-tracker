package org.exoplatform.timetracker.service;

import junit.framework.TestCase;
import org.exoplatform.timetracker.dto.ActivityCode;
import org.exoplatform.timetracker.storage.CodesStorage;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class CodesServiceTest extends TestCase {

  private CodesService codesService;

  private CodesStorage codesStorage;

  @Before
  public void setUp() throws Exception {
    codesStorage = mock(CodesStorage.class);
    codesService = new CodesService(codesStorage);
  }

  public void testCreateActivityCode() throws Exception {
    // Given
    ActivityCode activityCode = null;
    ActivityCode newActivityCode = new ActivityCode(1l, "testCode", "testLabel");
    when(codesStorage.createActivityCode(any())).thenReturn(newActivityCode);

    // When
    activityCode = codesService.createActivityCode(newActivityCode);

    // Then
    assertNotNull(activityCode);
    verify(codesStorage, times(1)).createActivityCode(any());
  }

  public void testUpdateActivityCode() throws Exception {
    // Given
    ActivityCode activityCode = new ActivityCode(1l, "testCode", "testLabel");
    ActivityCode activityCodeUpdated = new ActivityCode(1l, "testCodeUpdated", "testLabelUpdated");
    when(codesStorage.getActivityCodeById(anyLong())).thenReturn(activityCode);
    when(codesStorage.updateActivityCode(activityCode)).thenReturn(activityCodeUpdated);

    // When
    activityCodeUpdated = codesService.updateActivityCode(activityCode, "root");

    // Then
    assertEquals(activityCodeUpdated.getCode(), "testCodeUpdated");
    assertEquals(activityCodeUpdated.getLabel(), "testLabelUpdated");
    assertEquals((long) activityCodeUpdated.getId(), 1l);
  }

  public void testDeleteActivityCode() throws Exception {
    // Given
    ActivityCode activityCode = new ActivityCode(1l, "testCode", "testLabel");
    doNothing().when(codesStorage).deleteActivityCode(activityCode.getId());
    when(codesStorage.getActivityCodeById(activityCode.getId())).thenReturn(activityCode);

    // When
    codesService.deleteActivityCode(activityCode.getId(), "root");

    // Then
    verify(codesStorage, times(1)).deleteActivityCode(anyLong());
  }

  public void testGetActivityCodesList() {
    // Given
    ActivityCode activityCode = new ActivityCode(1l, "testCode", "testLabel");
    ActivityCode activityCode1 = new ActivityCode(2l, "testCode1", "testLabel1");
    ActivityCode activityCode2 = new ActivityCode(3l, "testCode2", "testLabel2");
    ActivityCode activityCode3 = new ActivityCode(4l, "testCode3", "testLabel3");
    List<ActivityCode> activityCodes = new ArrayList<>();
    activityCodes.add(activityCode);
    activityCodes.add(activityCode1);
    activityCodes.add(activityCode2);
    activityCodes.add(activityCode3);
    when(codesStorage.getActivityCodes()).thenReturn(activityCodes);

    // When
    List<ActivityCode> activityCodeList = codesService.getActivityCodesList();

    // Then
    assertEquals(4, activityCodeList.size());
    verify(codesStorage, times(1)).getActivityCodes();
  }
}
