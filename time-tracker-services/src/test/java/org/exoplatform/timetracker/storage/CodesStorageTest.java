/*
 * Copyright (C) 2020 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.timetracker.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.exoplatform.timetracker.dto.ActivityCode;
import org.exoplatform.timetracker.dto.SubActivityCode;
import org.exoplatform.timetracker.dto.SubType;
import org.exoplatform.timetracker.dto.Type;
import org.exoplatform.timetracker.service.BaseTimeTrackerTest;
import org.gatein.api.EntityNotFoundException;
import org.junit.Test;

import java.util.List;

public class CodesStorageTest extends BaseTimeTrackerTest {

  @Test
  public void testCreateActivityCode() throws Exception {
    ActivityCode activityCode = new ActivityCode(null, "test06", "test06");
    // When
    ActivityCode activityCodeNew = createActivityCode(activityCode);

    // Then
    assertNotNull(activityCodeNew);
    assertEquals(activityCode.getCode(), activityCodeNew.getCode());
    assertEquals(activityCode.getDisplayLabel(), activityCodeNew.getDisplayLabel());
    assertEquals(activityCode.getLabel(), activityCodeNew.getLabel());
    // Throw
    assertThrows(IllegalArgumentException.class, () -> codesStorage.createActivityCode(null));
  }

  @Test
  public void testUpdateActivityCode() throws Exception {
    ActivityCode activityCode = new ActivityCode(null, "test05", "test05");
    ActivityCode storedActivityCode = createActivityCode(activityCode);

    storedActivityCode.setCode("test2");
    storedActivityCode.setLabel("test2 label");

    ActivityCode updatedActivityCode = codesStorage.updateActivityCode(storedActivityCode);
    assertNotNull(updatedActivityCode);
    assertEquals("test2", updatedActivityCode.getCode());
    assertEquals("test2 label", updatedActivityCode.getLabel());
    assertThrows(IllegalArgumentException.class, () -> codesStorage.updateActivityCode(null));
    ActivityCode updatedActivityCodeFromDB = codesStorage.getActivityCodeById(updatedActivityCode.getId());
    assertNotNull(updatedActivityCodeFromDB);
    assertEquals("test2", updatedActivityCodeFromDB.getCode());
    assertEquals("test2 label", updatedActivityCodeFromDB.getLabel());
  }

  @Test
  public void testDeleteActivityCode() throws Exception {
    ActivityCode activityCode = new ActivityCode(null, "test04", "test04");
    ActivityCode storedActivityCode = createActivityCode(activityCode);
    Long activityCodeId = storedActivityCode.getId();
    codesStorage.deleteActivityCode(activityCodeId);
    cleanupActivityCodes.remove(storedActivityCode);
    List<ActivityCode> activityCodes = codesStorage.getActivityCodes();
    assertNotNull(activityCodes);
    assertTrue(activityCodes.isEmpty());
    assertThrows(IllegalArgumentException.class, () -> codesStorage.deleteActivityCode(0l));
    assertThrows(EntityNotFoundException.class, () -> codesStorage.deleteActivityCode(5000l));

  }

  @Test
  public void testGetActivityCodeById() throws Exception {
    ActivityCode activityCode = new ActivityCode(null, "test03", "test03");
    ActivityCode storedActivityCode = createActivityCode(activityCode);
    Long activityCodeId = storedActivityCode.getId();
    Long activityCodeIdNull = 0l;
    ActivityCode newActivityCodeUpdated = codesStorage.getActivityCodeById(activityCodeId);
    assertNotNull(newActivityCodeUpdated);
    assertEquals(storedActivityCode.getId(), newActivityCodeUpdated.getId());
    assertEquals(storedActivityCode.getCode(), newActivityCodeUpdated.getCode());
    assertEquals(storedActivityCode.getDisplayLabel(), newActivityCodeUpdated.getDisplayLabel());
    assertThrows(IllegalArgumentException.class, () -> codesStorage.getActivityCodeById(activityCodeIdNull));
  }

  @Test
  public void testGetActivityCodes() throws Exception {
    ActivityCode activityCode = new ActivityCode(null, "test02", "test02");
    createActivityCode(activityCode);
    List<ActivityCode> activityCodes = codesStorage.getActivityCodes();
    assertNotNull(activityCodes);
    assertTrue(!activityCodes.isEmpty());
    assertEquals(1, activityCodes.size());
  }

  @Test
  public void testCountActivityCodes() throws Exception {
    codesStorage.toActivityCodeEntity(null);
    ActivityCode activityCode = new ActivityCode(null, "test01", "test01");
    createActivityCode(activityCode);
    Long countActivityCodes = codesStorage.countActivityCodes();
    assertTrue(countActivityCodes == 1l);
  }

  @Test
  public void testCreateSubActivityCode() throws Exception {
    SubActivityCode subActivityCode = new SubActivityCode(null, "test25", "test25");
    SubActivityCode subActivityCodeNew = createSubActivityCode(subActivityCode);
    // Then
    assertNotNull(subActivityCodeNew);
    assertEquals(subActivityCode.getCode(), subActivityCodeNew.getCode());
    assertEquals(subActivityCode.getDisplayLabel(), subActivityCodeNew.getDisplayLabel());
    assertEquals(subActivityCode.getLabel(), subActivityCodeNew.getLabel());
    // Throw
    assertThrows(IllegalArgumentException.class, () -> codesStorage.createSubActivityCode(null));
  }

  @Test
  public void testUpdateSubActivityCode() throws Exception {
    SubActivityCode subActivityCode = new SubActivityCode(null, "test24", "test24");
    SubActivityCode storedSubActivityCode = createSubActivityCode(subActivityCode);
    storedSubActivityCode.setCode("test2");
    storedSubActivityCode.setLabel("test2 label");
    SubActivityCode updatedSubActivityCode = codesStorage.updateSubActivityCode(storedSubActivityCode);
    assertNotNull(updatedSubActivityCode);
    assertEquals("test2", updatedSubActivityCode.getCode());
    assertEquals("test2 label", updatedSubActivityCode.getLabel());
    assertThrows(IllegalArgumentException.class, () -> codesStorage.updateSubActivityCode(null));
    SubActivityCode updatedSubActivityCodeFromDB = codesStorage.getSubActivityCodeById(updatedSubActivityCode.getId());
    assertNotNull(updatedSubActivityCodeFromDB);
    assertEquals("test2", updatedSubActivityCodeFromDB.getCode());
    assertEquals("test2 label", updatedSubActivityCodeFromDB.getLabel());
  }

  @Test
  public void testDeleteSubActivityCode() throws Exception {
    SubActivityCode subActivityCode = new SubActivityCode(null, "test23", "test23");
    SubActivityCode storedSubActivityCode = createSubActivityCode(subActivityCode);
    Long subActivityCodeId = storedSubActivityCode.getId();
    codesStorage.deleteSubActivityCode(subActivityCodeId);
    cleanupSubActivityCodes.remove(storedSubActivityCode);
    List<SubActivityCode> subActivityCodes = codesStorage.getSubActivityCodes();
    assertNotNull(subActivityCodes);
    assertTrue(subActivityCodes.isEmpty());
    assertThrows(IllegalArgumentException.class, () -> codesStorage.deleteSubActivityCode(0l));
    assertThrows(EntityNotFoundException.class, () -> codesStorage.deleteSubActivityCode(5000l));
  }

  @Test
  public void testGetSubActivityCodeById() throws Exception {
    SubActivityCode subActivityCode = new SubActivityCode(null, "test22", "test22");
    SubActivityCode storedSubActivityCode = createSubActivityCode(subActivityCode);
    Long subActivityCodeId = storedSubActivityCode.getId();
    Long subActivityCodeIdNull = 0l;
    SubActivityCode newSubActivityCodeUpdated = codesStorage.getSubActivityCodeById(subActivityCodeId);
    assertNotNull(newSubActivityCodeUpdated);
    assertEquals(storedSubActivityCode.getId(), newSubActivityCodeUpdated.getId());
    assertEquals(storedSubActivityCode.getCode(), newSubActivityCodeUpdated.getCode());
    assertEquals(storedSubActivityCode.getDisplayLabel(), newSubActivityCodeUpdated.getDisplayLabel());
    assertThrows(IllegalArgumentException.class, () -> codesStorage.getSubActivityCodeById(subActivityCodeIdNull));
  }

  @Test
  public void testGetSubActivityCodes() throws Exception {
    SubActivityCode subActivityCode = new SubActivityCode(null, "test21", "test21");
    createSubActivityCode(subActivityCode);
    List<SubActivityCode> subActivityCodes = codesStorage.getSubActivityCodes();
    assertNotNull(subActivityCodes);
    assertTrue(!subActivityCodes.isEmpty());
    assertEquals(1, subActivityCodes.size());
  }

  @Test
  public void testCountSubActivityCodes() throws Exception {
    SubActivityCode subActivityCode = new SubActivityCode(null, "test20", "test20");
    createSubActivityCode(subActivityCode);
    Long countSubActivityCodes = codesStorage.countSubActivityCodes();
    assertTrue(countSubActivityCodes == 1l);
  }

  @Test
  public void testCreateType() throws Exception {
    Type type = new Type(null, "test17", "test17");
    Type typeNew = createType(type);
    List<Type> types = codesStorage.getTypes();
    assertNotNull(types);
    assertTrue(!types.isEmpty());
    assertNotNull(typeNew);
    assertEquals(type.getCode(), typeNew.getCode());
    assertEquals(type.getDisplayLabel(), typeNew.getDisplayLabel());
    assertEquals(type.getLabel(), typeNew.getLabel());
    assertThrows(IllegalArgumentException.class, () -> codesStorage.createType(null));
  }

  @Test
  public void testUpdateType() throws Exception {
    Type type = new Type(null, "test19", "test19");
    Type storedType = createType(type);
    storedType.setCode("test2");
    storedType.setLabel("test2 label");
    Type updatedType = codesStorage.updateType(storedType);
    assertNotNull(updatedType);
    assertEquals("test2", updatedType.getCode());
    assertEquals("test2 label", updatedType.getLabel());
    assertThrows(IllegalArgumentException.class, () -> codesStorage.updateType(null));
    Type updatedTypeFromDB = codesStorage.getTypeById(updatedType.getId());
    List<Type> types = codesStorage.getTypes();
    assertNotNull(types);
    assertTrue(!types.isEmpty());
    assertNotNull(updatedTypeFromDB);
    assertEquals("test2", updatedTypeFromDB.getCode());
    assertEquals("test2 label", updatedTypeFromDB.getLabel());
  }

  @Test
  public void testDeleteType() throws Exception {
    Type type = new Type(null, "test12", "test12");
    Type storedType = createType(type);
    Long typeId = storedType.getId();
    codesStorage.deleteType(typeId);
    cleanupTypes.remove(storedType);
    List<Type> types = codesStorage.getTypes();
    assertNotNull(types);
    assertTrue(!types.isEmpty());
    assertThrows(IllegalArgumentException.class, () -> codesStorage.deleteType(0l));
    assertThrows(EntityNotFoundException.class, () -> codesStorage.deleteType(5000l));
  }

  @Test
  public void testGetTypeById() throws Exception {
    Type type = new Type(null, "test11", "test11");
    Type storedType = createType(type);
    Long typeId = storedType.getId();
    Long typeIdNull = 0l;
    Type newTypeUpdated = codesStorage.getTypeById(typeId);
    List<Type> types = codesStorage.getTypes();
    assertNotNull(types);
    assertTrue(!types.isEmpty());
    assertNotNull(newTypeUpdated);
    assertEquals(storedType.getId(), newTypeUpdated.getId());
    assertEquals(storedType.getCode(), newTypeUpdated.getCode());
    assertEquals(storedType.getDisplayLabel(), newTypeUpdated.getDisplayLabel());
    assertThrows(IllegalArgumentException.class, () -> codesStorage.getTypeById(typeIdNull));
  }

  @Test
  public void testGetTypes() throws Exception {
    Type type = new Type(null, "test15", "test15");
    createType(type);
    List<Type> types = codesStorage.getTypes();
    assertNotNull(types);
    assertTrue(!types.isEmpty());
  }

  @Test
  public void testCountTypes() throws Exception {
    Type type = new Type(null, "test7", "test7");
    createType(type);
    Long countTypes = codesStorage.countTypes();
    List<Type> types = codesStorage.getTypes();
    assertNotNull(types);
    assertTrue(!types.isEmpty());
    assertTrue(countTypes == 1l);
  }

  @Test
  public void testCreateSubType() throws Exception {
    Type type = new Type(null, "test8", "test8");
    Type typeNew = createType(type);
    SubType subType = new SubType(null, "test8", "test8", typeNew);
    SubType subTypeNew = createSubType(subType);
    assertNotNull(subTypeNew);
    assertEquals(subType.getCode(), subTypeNew.getCode());
    assertEquals(subType.getDisplayLabel(), subTypeNew.getDisplayLabel());
    assertEquals(subType.getLabel(), subTypeNew.getLabel());
    assertThrows(IllegalArgumentException.class, () -> codesStorage.createSubType(null));
  }

  @Test
  public void testUpdateSubType() throws Exception {
    Type type = new Type(null, "test6", "test6");
    Type typeNew = createType(type);
    SubType subType = new SubType(null, "test6", "test6", typeNew);
    SubType storedSubType = createSubType(subType);
    storedSubType.setCode("test2");
    storedSubType.setLabel("test2 label");
    SubType updatedSubType = codesStorage.updateSubType(storedSubType);
    assertNotNull(updatedSubType);
    assertEquals("test2", updatedSubType.getCode());
    assertEquals("test2 label", updatedSubType.getLabel());
    assertThrows(IllegalArgumentException.class, () -> codesStorage.updateSubType(null));
    SubType updatedSubTypeFromDB = codesStorage.getSubTypeById(updatedSubType.getId());
    assertNotNull(updatedSubTypeFromDB);
    assertEquals("test2", updatedSubTypeFromDB.getCode());
    assertEquals("test2 label", updatedSubTypeFromDB.getLabel());
  }

  @Test
  public void testDeleteSubType() throws Exception {
    Type type = new Type(null, "test5", "test5");
    Type typeNew = codesStorage.createType(type);
    SubType subType = new SubType(null, "test5", "test5", typeNew);
    SubType storedSubType = createSubType(subType);
    Long subTypeId = storedSubType.getId();
    codesStorage.deleteSubType(subTypeId);
    cleanupTypes.remove(storedSubType.getType());
    cleanupSubTypes.remove(storedSubType);
    List<SubType> subTypes = codesStorage.getSubTypes();
    assertNotNull(subTypes);
    assertThrows(IllegalArgumentException.class, () -> codesStorage.deleteSubType(0l));
    assertThrows(EntityNotFoundException.class, () -> codesStorage.deleteSubType(5000l));
  }

  @Test
  public void testGetSubTypeById() throws Exception {
    Type type = new Type(null, "test3", "test3");
    Type typeNew = codesStorage.createType(type);
    SubType subType = new SubType(null, "test4", "test4", typeNew);
    SubType storedSubType = createSubType(subType);
    Long subTypeId = storedSubType.getId();
    Long subTypeIdNull = 0l;
    SubType newSubTypeUpdated = codesStorage.getSubTypeById(subTypeId);
    assertNotNull(newSubTypeUpdated);
    assertEquals(storedSubType.getId(), newSubTypeUpdated.getId());
    assertEquals(storedSubType.getCode(), newSubTypeUpdated.getCode());
    assertEquals(storedSubType.getDisplayLabel(), newSubTypeUpdated.getDisplayLabel());
    assertThrows(IllegalArgumentException.class, () -> codesStorage.getSubTypeById(subTypeIdNull));
  }

  @Test
  public void testGetSubTypes() throws Exception {
    Type type = new Type(null, "test1", "test1");
    Type typeNew = codesStorage.createType(type);
    SubType subType = new SubType(null, "test1", "test1", typeNew);
    createSubType(subType);
    List<SubType> subTypes = codesStorage.getSubTypes();
    assertNotNull(subTypes);
    assertTrue(!subTypes.isEmpty());
    assertEquals(1, subTypes.size());
  }

  @Test
  public void testCountSubTypes() throws Exception {
    Type type = new Type(null, "test2", "test2");
    Type typeNew = createType(type);
    SubType subType = new SubType(null, "test1", "test1", typeNew);
    createSubType(subType);
    Long countSubTypes = codesStorage.countSubTypes();
    assertTrue(countSubTypes == 1l);
  }

  private ActivityCode createActivityCode(ActivityCode activityCode) throws Exception {
    List<ActivityCode> activityCodes = codesStorage.getActivityCodes();
    assertNotNull(activityCodes);
    assertTrue(activityCodes.isEmpty());
    ActivityCode storedActivityCode = codesStorage.createActivityCode(activityCode);
    assertNotNull(storedActivityCode);
    cleanupActivityCodes.add(storedActivityCode);
    return storedActivityCode;
  }

  private SubActivityCode createSubActivityCode(SubActivityCode subActivityCode) throws Exception {
    List<SubActivityCode> subActivityCodes = codesStorage.getSubActivityCodes();
    assertNotNull(subActivityCodes);
    assertTrue(subActivityCodes.isEmpty());
    SubActivityCode storedSubActivityCode = codesStorage.createSubActivityCode(subActivityCode);
    assertNotNull(storedSubActivityCode);
    cleanupSubActivityCodes.add(storedSubActivityCode);
    return storedSubActivityCode;
  }

  private Type createType(Type type) throws Exception {
    List<Type> types = codesStorage.getTypes();
    assertNotNull(types);
    Type storedType = codesStorage.createType(type);
    assertNotNull(storedType);
    cleanupTypes.add(storedType);
    return storedType;
  }

  private SubType createSubType(SubType subType) throws Exception {
    List<SubType> subTypes = codesStorage.getSubTypes();
    assertNotNull(subTypes);
    SubType storedSubType = codesStorage.createSubType(subType);
    assertNotNull(storedSubType);
    cleanupSubTypes.add(storedSubType);
    return storedSubType;
  }

}
