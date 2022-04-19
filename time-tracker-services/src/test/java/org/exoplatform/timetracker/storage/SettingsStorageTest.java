package org.exoplatform.timetracker.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.exoplatform.timetracker.dto.Location;
import org.exoplatform.timetracker.dto.Office;
import org.exoplatform.timetracker.dto.WorkTime;
import org.exoplatform.timetracker.service.BaseTimeTrackerTest;
import org.gatein.api.EntityNotFoundException;
import org.junit.Test;

import java.lang.reflect.UndeclaredThrowableException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SettingsStorageTest extends BaseTimeTrackerTest {
  @Test
  public void testCreateLocation() throws Exception {
    Location location = new Location("test1", "test06");
    // When
    Location locationNew = createLocation(location);

    // Then
    assertNotNull(locationNew);
    assertEquals(location.getCode(), locationNew.getCode());
    assertEquals(location.getLabel(), locationNew.getLabel());
    // Throw
    assertThrows(IllegalArgumentException.class, () -> settingsStorage.createLocation(null));
  }

  @Test
  public void testUpdateLocation() throws Exception {
    Location location = new Location("test1", "test05");
    Location storedLocation = createLocation(location);
    storedLocation.setLabel("test2 label");
    Location updatedLocation = settingsStorage.updateLocation(storedLocation);
    assertNotNull(updatedLocation);
    assertEquals("test1", updatedLocation.getCode());
    assertEquals("test2 label", updatedLocation.getLabel());
    assertThrows(IllegalArgumentException.class, () -> settingsStorage.updateLocation(null));
    Location updatedLocationFromDB = settingsStorage.getLocationByCode(updatedLocation.getCode());
    assertNotNull(updatedLocationFromDB);
    assertEquals("test1", updatedLocationFromDB.getCode());
    assertEquals("test2 label", updatedLocationFromDB.getLabel());
  }

  @Test
  public void testDeleteLocation() throws Exception {
    Location location = new Location("test1", "test04");
    Location storedLocation = createLocation(location);
    String locationCode = storedLocation.getCode();
    settingsStorage.deleteLocation(locationCode);
    cleanupLocations.remove(storedLocation);
    List<Location> locations = settingsStorage.getLocations();
    assertNotNull(locations);
    assertTrue(locations.isEmpty());
    assertThrows(EntityNotFoundException.class, () -> settingsStorage.deleteLocation(""));
    assertThrows(EntityNotFoundException.class, () -> settingsStorage.deleteLocation("test5000"));

  }

  @Test
  public void testGetLocationByCode() throws Exception {
    Location location = new Location("test1", "test03");
    Location storedLocation = createLocation(location);
    String locationId = storedLocation.getCode();
    String locationIdNull = null;
    Location newLocationUpdated = settingsStorage.getLocationByCode(locationId);
    assertNotNull(newLocationUpdated);
    assertEquals(storedLocation.getCode(), newLocationUpdated.getCode());
    assertEquals(storedLocation.getCode(), newLocationUpdated.getCode());
    assertThrows(UndeclaredThrowableException.class, () -> settingsStorage.getLocationByCode(locationIdNull));
  }

  @Test
  public void testGetLocations() throws Exception {
    Location location = new Location("test1", "test02");
    createLocation(location);
    List<Location> locations = settingsStorage.getLocations();
    assertNotNull(locations);
    assertTrue(!locations.isEmpty());
    assertEquals(1, locations.size());
  }

  @Test
  public void testCountLocations() throws Exception {
    Location location = new Location("test1", "test01");
    createLocation(location);
    Long countLocations = settingsStorage.countLocations();
    assertTrue(countLocations == 1l);
  }

  @Test
  public void testCreateOffice() throws Exception {
    Office office = new Office("test1", "test06");
    Office officeNew = createOffice(office);
    assertNotNull(officeNew);
    assertEquals(office.getCode(), officeNew.getCode());
    assertEquals(office.getLabel(), officeNew.getLabel());
    assertThrows(IllegalArgumentException.class, () -> settingsStorage.createOffice(null));
  }

  @Test
  public void testUpdateOffice() throws Exception {
    Office office = new Office("test1", "test05");
    Office storedOffice = createOffice(office);
    storedOffice.setLabel("test2 label");
    Office updatedOffice = settingsStorage.updateOffice(storedOffice);
    assertNotNull(updatedOffice);
    assertEquals("test1", updatedOffice.getCode());
    assertEquals("test2 label", updatedOffice.getLabel());
    assertThrows(IllegalArgumentException.class, () -> settingsStorage.updateOffice(null));
    Office updatedOfficeFromDB = settingsStorage.getOfficeByCode(updatedOffice.getCode());
    assertNotNull(updatedOfficeFromDB);
    assertEquals("test1", updatedOfficeFromDB.getCode());
    assertEquals("test2 label", updatedOfficeFromDB.getLabel());
  }

  @Test
  public void testDeleteOffice() throws Exception {
    Office office = new Office("test1", "test04");
    Office storedOffice = createOffice(office);
    String officeCode = storedOffice.getCode();
    settingsStorage.deleteOffice(officeCode);
    cleanupOffices.remove(storedOffice);
    List<Office> offices = settingsStorage.getOffices();
    assertNotNull(offices);
    assertTrue(offices.isEmpty());
    assertThrows(EntityNotFoundException.class, () -> settingsStorage.deleteOffice(""));
    assertThrows(EntityNotFoundException.class, () -> settingsStorage.deleteOffice("test5000"));

  }

  @Test
  public void testGetOfficeByCode() throws Exception {
    Office office = new Office("test1", "test03");
    Office storedOffice = createOffice(office);
    String officeId = storedOffice.getCode();
    String officeIdNull = null;
    Office newOfficeUpdated = settingsStorage.getOfficeByCode(officeId);
    assertNotNull(newOfficeUpdated);
    assertEquals(storedOffice.getCode(), newOfficeUpdated.getCode());
    assertEquals(storedOffice.getCode(), newOfficeUpdated.getCode());
    assertThrows(UndeclaredThrowableException.class, () -> settingsStorage.getOfficeByCode(officeIdNull));
  }

  @Test
  public void testGetOffices() throws Exception {
    Office office = new Office("test1", "test02");
    createOffice(office);
    List<Office> offices = settingsStorage.getOffices();
    assertNotNull(offices);
    assertTrue(!offices.isEmpty());
    assertEquals(1, offices.size());
  }

  @Test
  public void testCountOffices() throws Exception {
    Office office = new Office("test1", "test01");
    createOffice(office);
    Long countOffices = settingsStorage.countOffices();
    assertTrue(countOffices == 1l);
  }

  @Test
  public void testCreateWorkTime() throws Exception {
    Office office = new Office("test1", "test06");
    Office officeNew = createOffice(office);
    WorkTime workTime = new WorkTime(null,
                                     new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"),
                                     new SimpleDateFormat("dd/MM/yyyy").parse("01/02/2020"),
                                     "test8",
                                     "test8",
                                     0,
                                     "test8",
                                     "test8",
                                     "test8",
                                     officeNew,
                                     false);
    WorkTime workTimeNew = createWorkTime(workTime);
    assertNotNull(workTimeNew);
    assertEquals(workTime.getFrom(), workTimeNew.getFrom());
    assertEquals(workTime.getOffice(), workTimeNew.getOffice());
    assertEquals(workTime.getTeam(), workTimeNew.getTeam());
    assertEquals(workTime.getPeriod(), workTimeNew.getPeriod());
    assertEquals(workTime.getTime(), workTimeNew.getTime());
    assertThrows(IllegalArgumentException.class, () -> settingsStorage.createWorkTime(null));
  }

  @Test
  public void testUpdateWorkTime() throws Exception {
    Office office = new Office("test1", "test06");
    Office officeNew = createOffice(office);
    WorkTime workTime = new WorkTime(null,
                                     new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"),
                                     new Date(),
                                     "test8",
                                     "test8",
                                     0,
                                     "test8",
                                     "test8",
                                     "test8",
                                     officeNew,
                                     false);
    WorkTime workTimeNew = createWorkTime(workTime);
    workTimeNew.setUserId("test2 label");
    WorkTime updatedWorkTime = settingsStorage.updateWorkTime(workTimeNew);
    assertNotNull(workTimeNew);
    assertEquals("test2 label", workTimeNew.getUserId());
    assertThrows(IllegalArgumentException.class, () -> settingsStorage.updateWorkTime(null));
    WorkTime updatedWorkTimeFromDB = settingsStorage.getWorkTimeById(updatedWorkTime.getId());
    assertNotNull(updatedWorkTimeFromDB);
    assertEquals("test2 label", updatedWorkTimeFromDB.getUserId());
  }

  @Test
  public void testDeleteWorkTime() throws Exception {
    Office office = new Office("test1", "test06");
    Office officeNew = createOffice(office);
    WorkTime workTime = new WorkTime(null,
                                     new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"),
                                     new Date(),
                                     "test8",
                                     "test8",
                                     0,
                                     "test8",
                                     "test8",
                                     "test8",
                                     officeNew,
                                     false);
    WorkTime storedWorkTime = createWorkTime(workTime);
    Long workTimeId = storedWorkTime.getId();
    settingsStorage.deleteWorkTime(workTimeId);
    settingsStorage.deleteOffice(officeNew.getCode());
    cleanupOffices.remove(storedWorkTime.getOffice());
    cleanupWorkTimes.remove(storedWorkTime);
    List<WorkTime> workTimes = settingsStorage.getWorkTimes();
    assertNotNull(workTimes);
    assertThrows(IllegalArgumentException.class, () -> settingsStorage.deleteWorkTime(0l));
    assertThrows(EntityNotFoundException.class, () -> settingsStorage.deleteWorkTime(5000l));
  }

  @Test
  public void testGetWorkTimeById() throws Exception {
    Office office = new Office("test1", "test06");
    Office officeNew = createOffice(office);
    WorkTime workTime = new WorkTime(null,
                                     new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"),
                                     new SimpleDateFormat("dd/MM/yyyy").parse("01/02/2020"),
                                     "test8",
                                     "test8",
                                     0,
                                     "test8",
                                     "test8",
                                     "test8",
                                     officeNew,
                                     false);
    WorkTime workTimeNew = createWorkTime(workTime);
    Long workTimeId = workTimeNew.getId();
    Long workTimeIdNull = 0l;
    WorkTime newWorkTimeUpdated = settingsStorage.getWorkTimeById(workTimeId);
    assertNotNull(newWorkTimeUpdated);
    assertNotNull(workTimeNew);
    assertEquals(workTime.getFrom(), workTimeNew.getFrom());
    assertEquals(workTime.getOffice(), workTimeNew.getOffice());
    assertEquals(workTime.getTeam(), workTimeNew.getTeam());
    assertEquals(workTime.getPeriod(), workTimeNew.getPeriod());
    assertEquals(workTime.getTime(), workTimeNew.getTime());
    assertThrows(IllegalArgumentException.class, () -> settingsStorage.getWorkTimeById(workTimeIdNull));
  }

  @Test
  public void testGetWorkTimes() throws Exception {
    Office office = new Office("test1", "test06");
    Office officeNew = createOffice(office);
    WorkTime workTime = new WorkTime(null,
                                     new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"),
                                     new SimpleDateFormat("dd/MM/yyyy").parse("01/02/2020"),
                                     "test8",
                                     "test8",
                                     0,
                                     "test8",
                                     "test8",
                                     "test8",
                                     officeNew,
                                     false);
    createWorkTime(workTime);
    List<WorkTime> workTimes = settingsStorage.getWorkTimes();
    assertNotNull(workTimes);
    assertTrue(!workTimes.isEmpty());
    assertEquals(1, workTimes.size());
  }

  @Test
  public void testCountWorkTimes() throws Exception {
    Office office = new Office("test1", "test06");
    Office officeNew = createOffice(office);
    WorkTime workTime = new WorkTime(null,
                                     new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2020"),
                                     new SimpleDateFormat("dd/MM/yyyy").parse("01/02/2020"),
                                     "test8",
                                     "test8",
                                     0,
                                     "test8",
                                     "test8",
                                     "test8",
                                     officeNew,
                                     false);
    createWorkTime(workTime);
    Long countWorkTimes = settingsStorage.countWorkTimes();
    assertTrue(countWorkTimes == 1l);
  }

  private Location createLocation(Location location) throws Exception {
    List<Location> locations = settingsStorage.getLocations();
    assertNotNull(locations);
    assertTrue(locations.isEmpty());
    Location storedLocation = settingsStorage.createLocation(location);
    assertNotNull(storedLocation);
    cleanupLocations.add(storedLocation);
    return storedLocation;
  }

  private WorkTime createWorkTime(WorkTime workTime) throws Exception {
    List<WorkTime> workTimes = settingsStorage.getWorkTimes();
    assertNotNull(workTimes);
    WorkTime storedWorkTime = settingsStorage.createWorkTime(workTime);
    assertNotNull(storedWorkTime);
    cleanupWorkTimes.add(storedWorkTime);
    return storedWorkTime;
  }
  
  private Office createOffice(Office office) throws Exception {
    List<Office> offices = settingsStorage.getOffices();
    assertNotNull(offices);
    assertTrue(offices.isEmpty());
    Office storedOffice = settingsStorage.createOffice(office);
    assertNotNull(storedOffice);
    cleanupOffices.add(storedOffice);
    return storedOffice;
  }

}
