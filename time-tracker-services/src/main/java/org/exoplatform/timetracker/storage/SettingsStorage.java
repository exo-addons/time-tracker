/*
 * This file is part of the Meeds project (https://meeds.io/).
 * Copyright (C) 2020 Meeds Association
 * contact@meeds.io
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.exoplatform.timetracker.storage;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import org.gatein.api.EntityNotFoundException;

import org.exoplatform.timetracker.dao.LocationDAO;
import org.exoplatform.timetracker.dao.OfficeDAO;
import org.exoplatform.timetracker.dao.WorkTimeDAO;
import org.exoplatform.timetracker.dto.Location;
import org.exoplatform.timetracker.dto.Office;
import org.exoplatform.timetracker.dto.WorkTime;
import org.exoplatform.timetracker.entity.LocationEntity;
import org.exoplatform.timetracker.entity.OfficeEntity;
import org.exoplatform.timetracker.entity.WorkTimeEntity;

/**
 * Storage service to access / load and save Codes. This service will be used ,
 * as well, to convert from JPA entity to DTO.
 *
 * @author medamine
 * @version $Id: $Id
 */
public class SettingsStorage {

  private final WorkTimeDAO workTimeDAO;

  private final LocationDAO locationDAO;

  private final OfficeDAO   officeDAO;

  private final String DATE_FORMAT = "yyyy-MM-dd";

  private final SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);

  /**
   * <p>
   * Constructor for SettingsStorage.
   * </p>
   *
   * @param workTimeDAO a {@link WorkTimeDAO} object.
   * @param locationDAO a {@link LocationDAO} object.
   * @param officeDAO a {@link OfficeDAO} object.
   */
  public SettingsStorage(WorkTimeDAO workTimeDAO, LocationDAO locationDAO, OfficeDAO officeDAO) {

    this.workTimeDAO = workTimeDAO;
    this.locationDAO = locationDAO;
    this.officeDAO = officeDAO;
  }

  ///////////////////////////// Location Storage //////////////////////////

  /**
   * <p>
   * createWorkTime.
   * </p>
   *
   * @param workTime a {@link WorkTime} object.
   * @return a {@link WorkTime} object.
   * @throws Exception if any.
   */
  public WorkTime createWorkTime(WorkTime workTime) throws Exception {
    if (workTime == null) {
      throw new IllegalArgumentException("WorkTime is mandatory");
    }
    WorkTimeEntity workTimeEntity = toWorkTimeEntity(workTime);
    workTime.setId(null);
    workTimeEntity = workTimeDAO.create(workTimeEntity);
    return toWorkTimeDTO(workTimeEntity);
  }

  /**
   * <p>
   * updateWorkTime.
   * </p>
   *
   * @param workTime a {@link WorkTime} object.
   * @return a {@link WorkTime} object.
   * @throws Exception if any.
   */
  public WorkTime updateWorkTime(WorkTime workTime) throws Exception {
    if (workTime == null) {
      throw new IllegalArgumentException("WorkTime is mandatory");
    }
    Long workTimeId = workTime.getId();
    WorkTimeEntity workTimeEntity = workTimeDAO.find(workTime.getId());
    if (workTimeEntity == null) {
      throw new EntityNotFoundException("WorkTime with id " + workTimeId + " wasn't found");
    }

    workTimeEntity = toWorkTimeEntity(workTime);
    workTimeEntity = workTimeDAO.update(workTimeEntity);

    return toWorkTimeDTO(workTimeEntity);
  }

  /**
   * <p>
   * deleteWorkTime.
   * </p>
   *
   * @param workTimeId a long.
   * @throws EntityNotFoundException if any.
   */
  public void deleteWorkTime(long workTimeId) throws EntityNotFoundException {
    if (workTimeId <= 0) {
      throw new IllegalArgumentException("WorkTimeId must be a positive integer");
    }
    WorkTimeEntity workTimeEntity = workTimeDAO.find(workTimeId);
    if (workTimeEntity == null) {
      throw new EntityNotFoundException("WorkTime with id " + workTimeId + " not found");
    }
    workTimeDAO.delete(workTimeEntity);
  }

  /**
   * <p>
   * getWorkTimeById.
   * </p>
   *
   * @param WorkTimeId a long.
   * @return a {@link WorkTime} object.
   */
  public WorkTime getWorkTimeById(long WorkTimeId) {
    if (WorkTimeId <= 0) {
      throw new IllegalArgumentException("WorkTimeId must be a positive integer");
    }
    WorkTimeEntity WorkTimeEntity = workTimeDAO.find(WorkTimeId);
    return toWorkTimeDTO(WorkTimeEntity);
  }

  /**
   * <p>
   * getWorkTimes.
   * </p>
   *
   * @return a {@link List} object.
   */
  public List<WorkTime> getWorkTimes() {
    List<WorkTimeEntity> applicatiions = workTimeDAO.findAll();
    return applicatiions.stream().map(this::toWorkTimeDTO).collect(Collectors.toList());
  }

  /**
   * <p>
   * countWorkTimes.
   * </p>
   *
   * @return a long.
   */
  public long countWorkTimes() {
    return workTimeDAO.count();
  }

  /**
   * <p>
   * toWorkTimeDTO.
   * </p>
   *
   * @param workTimeEntity a {@link WorkTimeEntity} object.
   * @return a {@link WorkTime} object.
   */
  public WorkTime toWorkTimeDTO(WorkTimeEntity workTimeEntity) {
    if (workTimeEntity == null) {
      return null;
    }
    return new WorkTime(workTimeEntity.getId(),
                        workTimeEntity.getFrom(),
                        workTimeEntity.getTo(),
                        formatter.format(workTimeEntity.getFrom()),
                        formatter.format(workTimeEntity.getTo()),
                        workTimeEntity.getTime(),
                        workTimeEntity.getPeriod(),
                        workTimeEntity.getTeam(),
                        workTimeEntity.getUserId(),
                        toOfficeDTO(workTimeEntity.getOfficeEntity()),
                        workTimeEntity.isDefaultTime());
  }

  /**
   * <p>
   * toWorkTimeEntity.
   * </p>
   *
   * @param workTime a {@link WorkTime} object.
   * @return a {@link WorkTimeEntity} object.
   */
  public WorkTimeEntity toWorkTimeEntity(WorkTime workTime) {
    if (workTime == null) {
      return null;
    }
    return new WorkTimeEntity(workTime.getId(),
                              workTime.getFrom(),
                              workTime.getTo(),
                              workTime.getTime(),
                              workTime.getPeriod(),
                              workTime.getTeam(),
                              workTime.getUserId(),
                              toOfficeEntity(workTime.getOffice()),
                              workTime.isDefaultTime());
  }

  ///////////////////////////// Activity Code Storage //////////////////////////

  /**
   * <p>
   * createLocation.
   * </p>
   *
   * @param location a {@link Location} object.
   * @return a {@link Location} object.
   * @throws Exception if any.
   */
  public Location createLocation(Location location) throws Exception {
    if (location == null) {
      throw new IllegalArgumentException("Location is mandatory");
    }
    LocationEntity locationEntity = toLocationEntity(location);
    locationEntity = locationDAO.create(locationEntity);
    return toLocationDTO(locationEntity);
  }

  /**
   * <p>
   * updateLocation.
   * </p>
   *
   * @param location a {@link Location} object.
   * @return a {@link Location} object.
   * @throws Exception if any.
   */
  public Location updateLocation(Location location) throws Exception {
    if (location == null) {
      throw new IllegalArgumentException("Location is mandatory");
    }
    LocationEntity locationEntity = locationDAO.find(location.getCode());
    if (locationEntity == null) {
      throw new EntityNotFoundException("Location with id " + location.getCode() + " wasn't found");
    }

    locationEntity = toLocationEntity(location);
    locationEntity = locationDAO.update(locationEntity);

    return toLocationDTO(locationEntity);
  }

  /**
   * <p>
   * deleteLocation.
   * </p>
   *
   * @param code a String.
   * @throws EntityNotFoundException if any.
   */
  public void deleteLocation(String code) throws EntityNotFoundException {

    LocationEntity locationEntity = locationDAO.find(code);
    if (locationEntity == null) {
      throw new EntityNotFoundException("Location with code " + code + " not found");
    }
    locationDAO.delete(locationEntity);
  }

  /**
   * <p>
   * getLocationById.
   * </p>
   *
   * @param code a String.
   * @return a {@link Location} object.
   */
  public Location getLocationByCode(String code) {

    LocationEntity LocationEntity = locationDAO.find(code);
    return toLocationDTO(LocationEntity);
  }

  /**
   * <p>
   * getLocations.
   * </p>
   *
   * @return a {@link List} object.
   */
  public List<Location> getLocations() {
    List<LocationEntity> applicatiions = locationDAO.findAll();
    return applicatiions.stream().map(this::toLocationDTO).collect(Collectors.toList());
  }

  /**
   * <p>
   * countLocations.
   * </p>
   *
   * @return a long.
   */
  public long countLocations() {
    return locationDAO.count();
  }

  /**
   * <p>
   * toLocationDTO.
   * </p>
   *
   * @param locationEntity a {@link LocationEntity} object.
   * @return a {@link Location} object.
   */
  public Location toLocationDTO(LocationEntity locationEntity) {
    if (locationEntity == null) {
      return null;
    }
    return new Location(locationEntity.getCode(), locationEntity.getLabel());
  }

  /**
   * <p>
   * toLocationEntity.
   * </p>
   *
   * @param location a {@link Location} object.
   * @return a {@link LocationEntity} object.
   */
  public LocationEntity toLocationEntity(Location location) {
    if (location == null) {
      return null;
    }
    return new LocationEntity(location.getCode(), location.getLabel());
  }

  ///////////////////////////// Office Storage //////////////////////////

  /**
   * <p>
   * createOffice.
   * </p>
   *
   * @param office a {@link Office} object.
   * @return a {@link Office} object.
   * @throws Exception if any.
   */
  public Office createOffice(Office office) throws Exception {
    if (office == null) {
      throw new IllegalArgumentException("Office is mandatory");
    }
    OfficeEntity officeEntity = toOfficeEntity(office);
    officeEntity = officeDAO.create(officeEntity);
    return toOfficeDTO(officeEntity);
  }

  /**
   * <p>
   * updateOffice.
   * </p>
   *
   * @param office a {@link Office} object.
   * @return a {@link Office} object.
   * @throws Exception if any.
   */
  public Office updateOffice(Office office) throws Exception {
    if (office == null) {
      throw new IllegalArgumentException("Office is mandatory");
    }
    OfficeEntity officeEntity = officeDAO.find(office.getCode());
    if (officeEntity == null) {
      throw new EntityNotFoundException("Office with id " + office.getCode() + " wasn't found");
    }

    officeEntity = toOfficeEntity(office);
    officeEntity = officeDAO.update(officeEntity);

    return toOfficeDTO(officeEntity);
  }

  /**
   * <p>
   * deleteOffice.
   * </p>
   *
   * @param code a String.
   * @throws EntityNotFoundException if any.
   */
  public void deleteOffice(String code) throws EntityNotFoundException {

    OfficeEntity officeEntity = officeDAO.find(code);
    if (officeEntity == null) {
      throw new EntityNotFoundException("Office with id " + code + " not found");
    }
    officeDAO.delete(officeEntity);
  }

  /**
   * <p>
   * getOfficeById.
   * </p>
   *
   * @param code a long.
   * @return a {@link Office} object.
   */
  public Office getOfficeByCode(String code) {
    OfficeEntity OfficeEntity = officeDAO.find(code);
    return toOfficeDTO(OfficeEntity);
  }

  /**
   * <p>
   * getOffices.
   * </p>
   *
   * @return a {@link List} object.
   */
  public List<Office> getOffices() {
    List<OfficeEntity> applicatiions = officeDAO.findAll();
    return applicatiions.stream().map(this::toOfficeDTO).collect(Collectors.toList());
  }

  /**
   * <p>
   * countOffices.
   * </p>
   *
   * @return a long.
   */
  public long countOffices() {
    return officeDAO.count();
  }

  /**
   * <p>
   * toOfficeDTO.
   * </p>
   *
   * @param officeEntity a {@link OfficeEntity} object.
   * @return a {@link Office} object.
   */
  public Office toOfficeDTO(OfficeEntity officeEntity) {
    if (officeEntity == null) {
      return null;
    }
    return new Office(officeEntity.getCode(), officeEntity.getLabel());
  }

  /**
   * <p>
   * toOfficeEntity.
   * </p>
   *
   * @param office a {@link Office} object.
   * @return a {@link OfficeEntity} object.
   */
  public OfficeEntity toOfficeEntity(Office office) {
    if (office == null) {
      return null;
    }
    return new OfficeEntity(office.getCode(), office.getLabel());
  }

}
