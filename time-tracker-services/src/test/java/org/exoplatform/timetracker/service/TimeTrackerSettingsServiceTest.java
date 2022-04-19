package org.exoplatform.timetracker.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.exoplatform.commons.api.settings.SettingService;
import org.exoplatform.timetracker.dto.Location;
import org.exoplatform.timetracker.dto.Office;
import org.exoplatform.timetracker.dto.WorkTime;
import org.exoplatform.timetracker.storage.SettingsStorage;
import org.exoplatform.ws.frameworks.json.JsonGenerator;
import org.exoplatform.ws.frameworks.json.impl.JsonGeneratorImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TimeTrackerSettingsServiceTest {

	private TimeTrackerSettingsService settingService;
	
	private SettingService settingsService;

	private SettingsStorage settingsStorage;

	@Before
	public void setUp() throws Exception {
		settingsStorage = mock(SettingsStorage.class);
		settingsService = mock(SettingService.class);
		settingService = new TimeTrackerSettingsService(settingsStorage,settingsService);
	}

	@Test
	public void testCreateWorkTime() throws Exception {
		Date toDate = new Date();
		Date fromDate = new Date();
		Office office = new Office("test", "test");
		WorkTime workTime = new WorkTime(1L, toDate, fromDate, "test", "test", 8, "test", "test", "test", office, true);
		WorkTime workTimeNew = new WorkTime();
		when(settingsStorage.createWorkTime(any())).thenReturn(workTime);
		// When
		workTimeNew = settingService.createWorkTime(workTime);
		// Then
		assertEquals(workTime, workTimeNew);
		verify(settingsStorage, times(1)).createWorkTime(any());
	}

	@Test
	public void testUpdateWorkTime() throws Exception {
		Date toDate = new Date();
		Date fromDate = new Date();
		Office office = new Office("test", "test");
		Date toDateUpdated = new Date();
		Date fromDateUpdated = new Date();
		Office officeUpdated = new Office("testUpdated", "testUpdated");
		WorkTime workTime = new WorkTime(1L, toDate, fromDate, "test", "test", 8, "test", "test", "test", office, true);
		WorkTime workTimeUpdated = new WorkTime(1L, toDateUpdated, fromDateUpdated, "testUpdated", "testUpdated", 8,
				"testUpdated", "testUpdated", "testUpdated", officeUpdated, true);
		WorkTime workTimeNew = new WorkTime();
		when(settingsStorage.updateWorkTime(workTime)).thenReturn(workTimeUpdated);
		// When
		workTimeNew = settingService.updateWorkTime(workTime, "root");
		// Then
		verify(settingsStorage, times(1)).updateWorkTime(any());
	}

	@Test
	public void testDeleteWorkTime() throws Exception {
		Date toDate = new Date();
		Date fromDate = new Date();
		Office office = new Office("test", "test");
		WorkTime workTime = new WorkTime(1L, toDate, fromDate, "test", "test", 8, "test", "test", "test", office, true);
		WorkTime workTimeNew = new WorkTime();
		Long workTimeId = workTime.getId();
		doNothing().when(settingsStorage).deleteWorkTime(workTimeId);
		// When
		settingService.deleteWorkTime(workTimeId, "root");
		// Then
		verify(settingsStorage, times(1)).deleteWorkTime(anyLong());
	}

	@Test
	public void testGetWorkTimesList() throws Exception {
		Date toDate = new Date();
		Date fromDate = new Date();
		Office office = new Office("test", "test");
		WorkTime workTime = new WorkTime(1L, toDate, fromDate, "test", "test", 8, "test", "test", "test", office, true);
		List<WorkTime> workTimeList = new ArrayList<>();
		List<WorkTime> workTimeNewList = new ArrayList<>();
		workTimeList.add(workTime);
		when(settingsStorage.getWorkTimes()).thenReturn(workTimeList);
		// When
		workTimeNewList = settingService.getWorkTimesList();
		// Then
		verify(settingsStorage, times(1)).getWorkTimes();
	}

	@Test
	public void testCreateOffice() throws Exception {
		Office office = new Office("test", "test");
		Office officeNew = new Office();
		when(settingsStorage.createOffice(any())).thenReturn(office);
		// When
		officeNew = settingService.createOffice(office);
		// Then
		verify(settingsStorage, times(1)).createOffice(any());
	}

	@Test
	public void testUpdateOffice() throws Exception {
		Office office = new Office("test", "test");
		Office officeUpdate = new Office("testUpdate", "testUpdate");
		Office officeNew = new Office();
		when(settingsStorage.updateOffice(any())).thenReturn(office);
		// When
		officeNew = settingService.updateOffice(office,"root");
		// Then
		verify(settingsStorage, times(1)).updateOffice(any());
	}

	@Test
	public void testDeleteOffice() throws Exception {
		Office office = new Office("test", "test");
		Office officeNew = new Office();
		office.setCode("1");
		String officeCode = office.getCode();
		doNothing().when(settingsStorage).deleteOffice(officeCode);
		// When
		settingService.deleteOffice(officeCode, "root");
		// Then
		verify(settingsStorage, times(1)).deleteOffice(any());
	}

	@Test
	public void testGetOfficesList() throws Exception {
		Office office = new Office("test", "test");
		List<Office> officeList = new ArrayList<>();
		List<Office> officeNewList = new ArrayList<>();
		officeNewList.add(office);
		when(settingsStorage.getOffices()).thenReturn(officeList);
		// When
		officeNewList = settingService.getOfficesList();
		// Then
		verify(settingsStorage, times(1)).getOffices();
	}
	@Test
	public void testCreateLocation() throws Exception {
		Location location = new Location("test", "test");
		Location locationNew = new Location();
		when(settingsStorage.createLocation(any())).thenReturn(location);
		// When
		locationNew = settingService.createLocation(location);
		// Then
		verify(settingsStorage, times(1)).createLocation(any());
	}

	@Test
	public void testUpdateLocation() throws Exception {
		Location location = new Location("test", "test");
		Location locationUpdate = new Location("testUpdate", "testUpdate");
		Location locationNew = new Location();
		when(settingsStorage.updateLocation(any())).thenReturn(location);
		// When
		locationNew = settingService.updateLocation(location,"root");
		// Then
		verify(settingsStorage, times(1)).updateLocation(any());
	}

	@Test
	public void testDeleteLocation() throws Exception {
		Location location = new Location("test", "test");
		Location locationNew = new Location();
		location.setCode("1");
		String locationCode = location.getCode();
		doNothing().when(settingsStorage).deleteLocation(locationCode);
		// When
		settingService.deleteLocation(locationCode, "root");
		// Then
		verify(settingsStorage, times(1)).deleteLocation(any());
	}

	@Test
	public void testGetLocationsList() throws Exception {
		Location location = new Location("test", "test");
		List<Location> locationList = new ArrayList<>();
		List<Location> locationNewList = new ArrayList<>();
		locationNewList.add(location);
		when(settingsStorage.getLocations()).thenReturn(locationList);
		// When
		locationNewList = settingService.getLocationsList();
		// Then
		verify(settingsStorage, times(1)).getLocations();
	}
	
	@Test
	public void testToJsonString() throws Exception{
		Location location = new Location("test", "test");
		String result;
		JsonGenerator JSON_GENERATOR = new JsonGeneratorImpl();
		String resultNew= JSON_GENERATOR.createJsonObject(location).toString();
		//when
		result= settingService.toJsonString(location);
		//Then
		assertEquals(resultNew,result);
		/*when(settingService.toJsonString(location)).thenReturn(null);
		result= settingService.toJsonString(location);
		fail("Error parsing object to string ");*/
		
	}
	/*@Test
	public void testGetSettings() {
		Client client = new Client(1l, "test", "test");
		Project project = new Project(1l, "test", "test", client);
		Type type = new Type(1l, "test", "test");
		SubType subType = new SubType(1l, "test", "test", type);
		ActivityCode activityCode = new ActivityCode(1l, "test", "test");
		SubActivityCode subActivityCode = new SubActivityCode(1l, "test", "test");
		Feature feature = new Feature(1l, "test", "test", "test", "test", project);
		Team team = new Team("1", "test", "test");
		List<Team> teams = new ArrayList<>();
		teams.add(team);
		Activity activity = new Activity(1l, type, subType, activityCode, subActivityCode, "test", project, feature,
				teams);
		TimeTrackerSetting configuredTimeTrackerSetting = new TimeTrackerSetting() ;
		TimeTrackerSetting configuredTimeTrackerSettingNew = new TimeTrackerSetting() ;
		configuredTimeTrackerSettingNew.setWeekEndHolidayActivity(activity);
		configuredTimeTrackerSettingNew.setDefaultFeatureSubActivity(subActivityCode);
		configuredTimeTrackerSettingNew.setUsersSpace("test");
		when(settingsService.get(any(),any(),any())).thenReturn(null);
		
		//When
		configuredTimeTrackerSetting= settingService.getSettings();
		assertEquals(configuredTimeTrackerSetting.getWeekEndHolidayActivity().getId(),configuredTimeTrackerSettingNew.getWeekEndHolidayActivity().getId());
		
	}*/

}
