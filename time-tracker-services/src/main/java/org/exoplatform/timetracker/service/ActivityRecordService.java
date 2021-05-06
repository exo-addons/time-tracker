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

import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang.StringUtils;

import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.timetracker.dto.ActivityRecord;
import org.exoplatform.timetracker.dto.RecordsAccessList;
import org.exoplatform.timetracker.storage.ActivityRecordStorage;

/**
 * A Service to access and store ActivityRecords
 *
 * @author medamine
 * @version $Id: $Id
 */
public class ActivityRecordService {

  private static final Log      LOG = ExoLogger.getLogger(ActivityRecordService.class);

  private final ActivityRecordStorage activityRecordstorage;

  /**
   * <p>Constructor for ActivityRecordService.</p>
   *
   * @param activityRecordstorage a {@link org.exoplatform.timetracker.storage.ActivityRecordStorage} object.
   */
  public ActivityRecordService(ActivityRecordStorage activityRecordstorage) {
    this.activityRecordstorage = activityRecordstorage;

  }

  /**
   * Create new ActivityRecord
   *
   * @param activityRecord ActivityRecord to create
   * @return stored {@link org.exoplatform.timetracker.dto.ActivityRecord} in datasource
   * @throws java.lang.Exception when ActivityRecord already exists or an error occurs while
   *           creating ActivityRecord or its attached image
   */
  public ActivityRecord createActivityRecord(ActivityRecord activityRecord) throws Exception {
    if (activityRecord == null) {
      throw new IllegalArgumentException("ActivityRecord is mandatory");
    }
    return activityRecordstorage.createActivityRecord(activityRecord);
  }

  /**
   * Update an existing ActivityRecord on datasource. If the ActivityRecord doesn't exit an
   * {@link javax.persistence.EntityNotFoundException} will be thrown.
   *
   * @param activityRecord dto to update on store
   * @param username username storing ActivityRecord
   * @return stored {@link org.exoplatform.timetracker.dto.ActivityRecord} in datasource
   * @throws java.lang.Exception when {@link java.lang.Exception} is thrown or an error
   *           occurs while saving ActivityRecord
   */
  public ActivityRecord updateActivityRecord(ActivityRecord activityRecord, String username) throws Exception {
    if (activityRecord == null) {
      throw new IllegalArgumentException("ActivityRecord is mandatory");
    }
    if (StringUtils.isBlank(username)) {
      throw new IllegalArgumentException("username is mandatory");
    }
    Long ActivityRecordId = activityRecord.getId();
    if (ActivityRecordId == null) {
      throw new EntityNotFoundException("ActivityRecord with null id wasn't found");
    }
    ActivityRecord storedActivityRecord = activityRecordstorage.getActivityRecordById(ActivityRecordId);
    if (storedActivityRecord == null) {
      throw new EntityNotFoundException("ActivityRecord with id " + ActivityRecordId + " wasn't found");
    }
    return activityRecordstorage.updateActivityRecord(activityRecord);
  }

  /**
   * Delete ActivityRecord identified by its id and check if username has permission to
   * delete it.
   *
   * @param activityRecordId technical identifier of ActivityRecord
   * @param username user currently deleting ActivityRecord
   * @throws javax.persistence.EntityNotFoundException if ActivityRecord wasn't found
   * @throws java.lang.IllegalAccessException if user is not allowed to delete ActivityRecord
   */
  public void deleteActivityRecord(Long activityRecordId, String username) throws EntityNotFoundException, IllegalAccessException {
    if (activityRecordId == null || activityRecordId <= 0) {
      throw new IllegalArgumentException("ActivityRecordId must be a positive integer");
    }
    if (StringUtils.isBlank(username)) {
      throw new IllegalArgumentException("username is mandatory");
    }

    ActivityRecord storedActivityRecord = activityRecordstorage.getActivityRecordById(activityRecordId);
    if (storedActivityRecord == null) {
      throw new EntityNotFoundException("ActivityRecord with id " + activityRecordId + " not found");
    }
    activityRecordstorage.deleteActivityRecord(activityRecordId);
  }

  /**
   * Retrieves the list of ActivityRecords with offset, limit and a keyword that can be
   * empty
   *
   * @return List of {@link org.exoplatform.timetracker.dto.ActivityRecord} that contains the list of ActivityRecords
   */
  public List<ActivityRecord> getActivityRecords() {
    return activityRecordstorage.getActivityRecords();
  }

  /**
   * Retrieves the list of ActivityRecordsListAccess with offset, limit and a keyword that can be
   * empty
   *
   * @return List of {@link org.exoplatform.timetracker.dto.ActivityRecord} that contains the list of ActivityRecords
   * @param search a {@link java.lang.String} object.
   * @param activity a {@link java.lang.Long} object.
   * @param type a {@link java.lang.Long} object.
   * @param subType a {@link java.lang.Long} object.
   * @param activityCode a {@link java.lang.Long} object.
   * @param subActivityCode a {@link java.lang.Long} object.
   * @param client a {@link java.lang.Long} object.
   * @param project a {@link java.lang.Long} object.
   * @param feature a {@link java.lang.Long} object.
   * @param fromDate a {@link java.lang.String} object.
   * @param toDate a {@link java.lang.String} object.
   * @param userName a {@link java.lang.String} object.
   * @param location a {@link java.lang.String} object.
   * @param office a {@link java.lang.String} object.
   * @param offset a int.
   * @param limit a int.
   * @param sortBy a {@link java.lang.String} object.
   * @param sortDesc a boolean.
   */
  public RecordsAccessList getActivityRecordsList(String search,
                                                  Long activity,
                                                  Long type,
                                                  Long subType,
                                                  Long activityCode,
                                                  Long subActivityCode,
                                                  Long client,
                                                  Long project,
                                                  Long feature,
                                                  String fromDate,
                                                  String toDate,
                                                  String userName,
                                                  String location,
                                                  String office,
                                                  int offset,
                                                  int limit,
                                                  String sortBy,
                                                  boolean sortDesc) {
    return activityRecordstorage.getActivityRecordsList(search, activity, type, subType, activityCode, subActivityCode, client, project, feature, fromDate, toDate, userName, location, office, offset, limit, sortBy, sortDesc);
  }

  /**
   * Retrieves the list of ActivityRecordsListAccess with offset, limit and a keyword that can be
   * empty
   *
   * @return List of {@link org.exoplatform.timetracker.dto.ActivityRecord} that contains the list of ActivityRecords
   * @param userName a {@link java.lang.String} object.
   */
  public ActivityRecord getLastActivityRecord( String userName) {
    return activityRecordstorage.getLastActivityRecord(userName);
  }

  /**
   * Retrieves the list of ActivityRecordsListAccess with offset, limit and a keyword that can be
   * empty
   *
   * @return List of {@link org.exoplatform.timetracker.dto.ActivityRecord} that contains the list of ActivityRecords
   * @param search a {@link java.lang.String} object.
   * @param activity a {@link java.lang.Long} object.
   * @param type a {@link java.lang.Long} object.
   * @param subType a {@link java.lang.Long} object.
   * @param activityCode a {@link java.lang.Long} object.
   * @param subActivityCode a {@link java.lang.Long} object.
   * @param client a {@link java.lang.Long} object.
   * @param project a {@link java.lang.Long} object.
   * @param feature a {@link java.lang.Long} object.
   * @param fromDate a {@link java.lang.String} object.
   * @param toDate a {@link java.lang.String} object.
   * @param userName a {@link java.lang.String} object.
   * @param location a {@link java.lang.String} object.
   * @param office a {@link java.lang.String} object.
   */
  public long countActivityRecords(String search,
                                   Long activity,
                                   Long type,
                                   Long subType,
                                   Long activityCode,
                                   Long subActivityCode,
                                   Long client,
                                   Long project,
                                   Long feature,
                                   String fromDate,
                                   String toDate,
                                   String userName,
                                   String location,
                                   String office) {
    return activityRecordstorage.countActivityRecords(search, activity, type, subType, activityCode, subActivityCode, client, project, feature, fromDate, toDate, userName, location,office);
  }

  /**
   * Retrieves the list of ActivityRecords wby day
   *
   * @param day day of activity
   * @return List of {@link org.exoplatform.timetracker.dto.ActivityRecord} that contains the list of ActivityRecords
   * @param userName a {@link java.lang.String} object.
   */
  public List<ActivityRecord> getUserActivityRecordsList(String day,String userName) {
    return activityRecordstorage.getUserActivityRecords(day,userName);
  }

}
