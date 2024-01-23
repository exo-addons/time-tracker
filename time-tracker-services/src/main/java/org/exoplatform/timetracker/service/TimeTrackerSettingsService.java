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
package org.exoplatform.timetracker.service;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import org.exoplatform.commons.api.settings.SettingService;
import org.exoplatform.commons.api.settings.SettingValue;
import org.exoplatform.commons.api.settings.data.Context;
import org.exoplatform.commons.api.settings.data.Scope;
import org.exoplatform.timetracker.dto.Location;
import org.exoplatform.timetracker.dto.Office;
import org.exoplatform.timetracker.dto.TimeTrackerSetting;
import org.exoplatform.timetracker.dto.WorkTime;
import org.exoplatform.timetracker.storage.SettingsStorage;
import org.exoplatform.ws.frameworks.json.JsonGenerator;
import org.exoplatform.ws.frameworks.json.JsonParser;
import org.exoplatform.ws.frameworks.json.impl.JsonDefaultHandler;
import org.exoplatform.ws.frameworks.json.impl.JsonException;
import org.exoplatform.ws.frameworks.json.impl.JsonGeneratorImpl;
import org.exoplatform.ws.frameworks.json.impl.JsonParserImpl;
import org.exoplatform.ws.frameworks.json.impl.ObjectBuilder;

import jakarta.persistence.EntityNotFoundException;

/**
 * A Service to access and store Activities
 *
 * @author medamine
 * @version $Id: $Id
 */
public class TimeTrackerSettingsService {

  private final SettingsStorage settingsStorage;

  private final SettingService settingService;

  private TimeTrackerSetting configuredTimeTrackerSetting;

  public static final String           TIME_TRACKER_SCOPE_NAME              = "ADDONS_TIME_TRACKER_SCOPE";

  public static final String           TIME_TRACKER_CONTEXT_NAME            = "ADDONS_TIME_TRACKER_CONTEXT";

  public static final Context TIME_TRACKER_CONTEXT                 = Context.GLOBAL.id(TIME_TRACKER_CONTEXT_NAME);

  public static final Scope TIME_TRACKER_SCOPE                   = Scope.APPLICATION.id(TIME_TRACKER_SCOPE_NAME);

  public static final String           TIME_TRACKER_SETTINGS_KEY_NAME       = "TIME_TRACKER_SETTINGS";

  public static final JsonParser JSON_PARSER                          = new JsonParserImpl();

  public static final JsonGenerator JSON_GENERATOR                       = new JsonGeneratorImpl();

  /**
   * <p>
   * Constructor for SettingsService.
   * </p>
   *
   * @param settingsStorage  {@link SettingsStorage} object.
   * @param settingService  {@link SettingService} object.
   */
  public TimeTrackerSettingsService(SettingsStorage settingsStorage, SettingService settingService) {
    this.settingsStorage = settingsStorage;
    this.settingService = settingService;

  }

  /**
   * Create new WorkTime. If the WorkTime already exits an
   * {@link jakarta.persistence.EntityExistsException} will be thrown.
   *
   * @param workTime WorkTime to create
   * @return stored {@link WorkTime} in
   *         datasource
   * @throws Exception when WorkTime already exists or an error
   *           occurs while creating WorkTime or its attached image
   */
  public WorkTime createWorkTime(WorkTime workTime) throws Exception {
    return settingsStorage.createWorkTime(workTime);
  }

  /**
   * Update an existing WorkTime on datasource. If the WorkTime doesn't
   * exit an {@link EntityNotFoundException} will be thrown.
   *
   * @param workTime to update on store
   * @param username username storing WorkTime
   * @return stored {@link WorkTime} in
   *         datasource
   * @throws Exception when {@link Exception} is thrown or an
   *           error occurs while saving WorkTime
   */
  public WorkTime updateWorkTime(WorkTime workTime, String username) throws Exception {

    return settingsStorage.updateWorkTime(workTime);
  }

  /**
   * Delete WorkTime identified by its id and check if username has permission
   * to delete it.
   *
   * @param workTimeId technical identifier of WorkTime
   * @param username user currently deleting WorkTime
   * @throws EntityNotFoundException if WorkTime wasn't
   *           found
   * @throws IllegalAccessException if user is not allowed to delete
   *           WorkTime
   */
  public void deleteWorkTime(Long workTimeId, String username) throws EntityNotFoundException, IllegalAccessException {
    settingsStorage.deleteWorkTime(workTimeId);
  }

  /**
   * Retrieves the list of Activities with offset, limit and a keyword that can be
   * empty
   *
   * @return List of {@link WorkTime} that
   *         contains the list of Activities
   */
  public List<WorkTime> getWorkTimesList() {
    return settingsStorage.getWorkTimes();
  }

  /**
   * Create new Location that will be available for all users. If the
   * Location already exits an
   * {@link jakarta.persistence.EntityExistsException} will be thrown.
   *
   * @param location Location to create
   * @return stored {@link Location} in
   *         datasource
   * @throws Exception when Location already exists or an error
   *           occurs while creating Location or its attached image
   */
  public Location createLocation(Location location) throws Exception {
    return settingsStorage.createLocation(location);
  }

  /**
   * Update an existing Location on datasource. If the Location
   * doesn't exit an {@link EntityNotFoundException} will be
   * thrown.
   *
   * @param Location dto to update on store
   * @param username username storing Location
   * @return stored {@link Location} in
   *         datasource
   * @throws Exception when {@link Exception} is thrown or an
   *           error occurs while saving Location
   */
  public Location updateLocation(Location Location, String username) throws Exception {
    return settingsStorage.updateLocation(Location);
  }

  /**
   * Delete Location identified by its id and check if username has
   * permission to delete it.
   *
   * @param code technical identifier of Location
   * @param username user currently deleting Location
   * @throws EntityNotFoundException if Location wasn't
   *           found
   * @throws IllegalAccessException if user is not allowed to delete
   *           Location
   */
  public void deleteLocation(String code, String username) throws EntityNotFoundException,
                                                                             IllegalAccessException {
    settingsStorage.deleteLocation(code);
  }

  /**
   * Retrieves the list of Activities with offset, limit and a keyword that can be
   * empty
   *
   * @return List of {@link Location} that
   *         contains the list of Activities
   */
  public List<Location> getLocationsList() {
    return settingsStorage.getLocations();
  }

  /**
   * Create new Office that will be available for all users. If the Office already
   * exits an {@link jakarta.persistence.EntityExistsException} will be thrown.
   *
   * @param office Office to create
   * @return stored {@link Office} in datasource
   * @throws Exception when Office already exists or an error occurs while
   *           creating Office or its attached image
   */
  public Office createOffice(Office office) throws Exception {

    return settingsStorage.createOffice(office);
  }

  /**
   * Update an existing Office on datasource. If the Office doesn't exit an
   * {@link EntityNotFoundException} will be thrown.
   *
   * @param office dto to update on store
   * @param username username storing Office
   * @return stored {@link Office} in datasource
   * @throws Exception when {@link Exception} is thrown or an
   *           error occurs while saving Office
   */
  public Office updateOffice(Office office, String username) throws Exception {

    return settingsStorage.updateOffice(office);
  }

  /**
   * Delete Office identified by its id and check if username has permission to
   * delete it.
   *
   * @param code technical identifier of Office
   * @param username user currently deleting Office
   * @throws EntityNotFoundException if Office wasn't found
   * @throws IllegalAccessException if user is not allowed to delete
   *           Office
   */
  public void deleteOffice(String code, String username) throws EntityNotFoundException, IllegalAccessException {
    settingsStorage.deleteOffice(code);
  }

  /**
   * Retrieves the list of Activities with offset, limit and a keyword that can be
   * empty
   *
   * @return List of {@link Office} that contains
   *         the list of Activities
   */
  public List<Office> getOfficesList() {
    return settingsStorage.getOffices();
  }


  public TimeTrackerSetting getSettings() {
    if (this.configuredTimeTrackerSetting != null) {
      return this.configuredTimeTrackerSetting.clone();
    }
    SettingValue<?> settingsValue = settingService.get(TIME_TRACKER_CONTEXT, TIME_TRACKER_SCOPE, TIME_TRACKER_SETTINGS_KEY_NAME);
    String settingsValueString = settingsValue == null || settingsValue.getValue() == null ? null
            : settingsValue.getValue().toString();
    TimeTrackerSetting timeTrackerSetting = null;
    if (settingsValueString == null) {
      timeTrackerSetting = new TimeTrackerSetting();
    } else {
      timeTrackerSetting = fromJsonString(settingsValueString, TimeTrackerSetting.class);
    }
    this.configuredTimeTrackerSetting = timeTrackerSetting;
    return this.configuredTimeTrackerSetting;
  }

  public void saveSettings(TimeTrackerSetting timeTrackerSetting) throws Exception {
    if (timeTrackerSetting == null) {
      throw new IllegalArgumentException("Empty settings to save");
    }

    String settingsString = toJsonString(timeTrackerSetting);
    settingService.set(TIME_TRACKER_CONTEXT,
            TIME_TRACKER_SCOPE,
            TIME_TRACKER_SETTINGS_KEY_NAME,
            SettingValue.create(settingsString));

    // Purge cached settings
    this.configuredTimeTrackerSetting = null;

  }

  public static final String toJsonString(Object object) {
    try {
      return JSON_GENERATOR.createJsonObject(object).toString();
    } catch (JsonException e) {
      throw new IllegalStateException("Error parsing object to string " + object, e);
    }
  }

  public static final <T> T fromJsonString(String value, Class<T> resultClass) {
    try {
      if (StringUtils.isBlank(value)) {
        return null;
      }
      JsonDefaultHandler jsonDefaultHandler = new JsonDefaultHandler();
      JSON_PARSER.parse(new ByteArrayInputStream(value.getBytes()), jsonDefaultHandler);
      return ObjectBuilder.createObject(resultClass, jsonDefaultHandler.getJsonObject());
    } catch (JsonException e) {
      throw new IllegalStateException("Error creating object from string : " + value, e);
    }
  }


}
