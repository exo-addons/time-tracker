package org.exoplatform.timetracker.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.exoplatform.timetracker.dto.ActivityCode;
import org.exoplatform.timetracker.dto.SubActivityCode;
import org.exoplatform.timetracker.dto.SubType;
import org.exoplatform.timetracker.dto.Type;
import org.exoplatform.timetracker.storage.CodesStorage;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

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

	public void testCreateSubActivityCode() throws Exception {
		// Given
		SubActivityCode subActivityCode = null;
		SubActivityCode newSubActivityCode = new SubActivityCode(1l, "testCode", "testLabel");
		when(codesStorage.createSubActivityCode(any())).thenReturn(newSubActivityCode);

		// When
		subActivityCode = codesService.createSubActivityCode(newSubActivityCode);

		// Then
		assertNotNull(subActivityCode);
		verify(codesStorage, times(1)).createSubActivityCode(any());
	}

	public void testUpdateSubActivityCode() throws Exception {
		// Given
		SubActivityCode subActivityCode = new SubActivityCode(1l, "testCode", "testLabel");
		SubActivityCode subActivityCodeUpdated = new SubActivityCode(1l, "testCodeUpdated", "testLabelUpdated");
		when(codesStorage.getSubActivityCodeById(anyLong())).thenReturn(subActivityCode);
		when(codesStorage.updateSubActivityCode(subActivityCode)).thenReturn(subActivityCodeUpdated);

		// When
		subActivityCodeUpdated = codesService.updateSubActivityCode(subActivityCode, "root");

		// Then
		assertEquals(subActivityCodeUpdated.getCode(), "testCodeUpdated");
		assertEquals(subActivityCodeUpdated.getLabel(), "testLabelUpdated");
		assertEquals((long) subActivityCodeUpdated.getId(), 1l);
	}

	public void testDeleteSubActivityCode() throws Exception {
		// Given
		SubActivityCode subActivityCode = new SubActivityCode(1l, "testCode", "testLabel");
		doNothing().when(codesStorage).deleteSubActivityCode(subActivityCode.getId());
		when(codesStorage.getSubActivityCodeById(subActivityCode.getId())).thenReturn(subActivityCode);

		// When
		codesService.deleteSubActivityCode(subActivityCode.getId(), "root");

		// Then
		verify(codesStorage, times(1)).deleteSubActivityCode(anyLong());
	}

	public void testGetSubActivityCodesList() {
		// Given
		SubActivityCode subActivityCode = new SubActivityCode(1l, "testCode", "testLabel");
		SubActivityCode subActivityCode1 = new SubActivityCode(2l, "testCode1", "testLabel1");
		SubActivityCode subActivityCode2 = new SubActivityCode(3l, "testCode2", "testLabel2");
		SubActivityCode subActivityCode3 = new SubActivityCode(4l, "testCode3", "testLabel3");
		List<SubActivityCode> subActivityCodes = new ArrayList<>();
		subActivityCodes.add(subActivityCode);
		subActivityCodes.add(subActivityCode1);
		subActivityCodes.add(subActivityCode2);
		subActivityCodes.add(subActivityCode3);
		when(codesStorage.getSubActivityCodes()).thenReturn(subActivityCodes);

		// When
		List<SubActivityCode> subActivityCodeList = codesService.getSubActivityCodesList();

		// Then
		assertEquals(4, subActivityCodeList.size());
		verify(codesStorage, times(1)).getSubActivityCodes();
	}

	public void testCreateType() throws Exception {
		// Given
		Type type = null;
		Type newType = new Type(1l, "testCode", "testLabel");
		when(codesStorage.createType(any())).thenReturn(newType);

		// When
		type = codesService.createType(newType);

		// Then
		assertNotNull(type);
		verify(codesStorage, times(1)).createType(any());
	}

	public void testUpdateType() throws Exception {
		// Given
		Type type = new Type(1l, "testCode", "testLabel");
		Type typeUpdated = new Type(1l, "testCodeUpdated", "testLabelUpdated");
		when(codesStorage.getTypeById(anyLong())).thenReturn(type);
		when(codesStorage.updateType(type)).thenReturn(typeUpdated);

		// When
		typeUpdated = codesService.updateType(type, "root");

		// Then
		assertEquals(typeUpdated.getCode(), "testCodeUpdated");
		assertEquals(typeUpdated.getLabel(), "testLabelUpdated");
		assertEquals((long) typeUpdated.getId(), 1l);
	}

	public void testDeleteType() throws Exception {
		// Given
		Type type = new Type(1l, "testCode", "testLabel");
		doNothing().when(codesStorage).deleteType(type.getId());
		when(codesStorage.getTypeById(type.getId())).thenReturn(type);

		// When
		codesService.deleteType(type.getId(), "root");

		// Then
		verify(codesStorage, times(1)).deleteType(anyLong());
	}

	public void testGetTypesList() {
		// Given
		Type type = new Type(1l, "testCode", "testLabel");
		Type type1 = new Type(2l, "testCode1", "testLabel1");
		Type type2 = new Type(3l, "testCode2", "testLabel2");
		Type type3 = new Type(4l, "testCode3", "testLabel3");
		List<Type> types = new ArrayList<>();
		types.add(type);
		types.add(type1);
		types.add(type2);
		types.add(type3);
		when(codesStorage.getTypes()).thenReturn(types);

		// When
		List<Type> typeList = codesService.getTypesList();

		// Then
		assertEquals(4, typeList.size());
		verify(codesStorage, times(1)).getTypes();
	}

	public void testCreateSubType() throws Exception {
		// Given
		SubType subType = null;
		Type type = new Type(1l, "testCode", "testLabel");
		SubType newSubType = new SubType(1l, "testCode", "testLabel", type);
		when(codesStorage.createSubType(any())).thenReturn(newSubType);

		// When
		subType = codesService.createSubType(newSubType);

		// Then
		assertNotNull(subType);
		verify(codesStorage, times(1)).createSubType(any());
	}

	public void testUpdateSubType() throws Exception {
		// Given
		Type type = new Type(1l, "testCode", "testLabel");
		SubType subType = new SubType(1l, "testCode", "testLabel", type);
		SubType subTypeUpdated = new SubType(1l, "testCodeUpdated", "testLabelUpdated", type);
		when(codesStorage.getSubTypeById(anyLong())).thenReturn(subType);
		when(codesStorage.updateSubType(subType)).thenReturn(subTypeUpdated);

		// When
		subTypeUpdated = codesService.updateSubType(subType, "root");

		// Then
		assertEquals(subTypeUpdated.getCode(), "testCodeUpdated");
		assertEquals(subTypeUpdated.getLabel(), "testLabelUpdated");
		assertEquals((long) subTypeUpdated.getId(), 1l);
	}

	public void testDeleteSubType() throws Exception {
		// Given
		Type type = new Type(1l, "testCode", "testLabel");
		SubType subType = new SubType(1l, "testCode", "testLabel", type);
		doNothing().when(codesStorage).deleteSubType(subType.getId());
		when(codesStorage.getSubTypeById(subType.getId())).thenReturn(subType);

		// When
		codesService.deleteSubType(subType.getId(), "root");

		// Then
		verify(codesStorage, times(1)).deleteSubType(anyLong());
	}

	public void testGetSubTypesList() {
		// Given
		Type type = new Type(1l, "testCode", "testLabel");
		SubType subType = new SubType(1l, "testCode", "testLabel", type);
		SubType subType1 = new SubType(2l, "testCode1", "testLabel1", type);
		SubType subType2 = new SubType(3l, "testCode2", "testLabel2", type);
		SubType subType3 = new SubType(4l, "testCode3", "testLabel3", type);
		List<SubType> subTypes = new ArrayList<>();
		subTypes.add(subType);
		subTypes.add(subType1);
		subTypes.add(subType2);
		subTypes.add(subType3);
		when(codesStorage.getSubTypes()).thenReturn(subTypes);

		// When
		List<SubType> subTypeList = codesService.getSubTypesList();

		// Then
		assertEquals(4, subTypeList.size());
		verify(codesStorage, times(1)).getSubTypes();
	}
}
