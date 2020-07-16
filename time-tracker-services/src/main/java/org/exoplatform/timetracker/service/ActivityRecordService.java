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
 */
public class ActivityRecordService {

  private static final Log      LOG = ExoLogger.getLogger(ActivityRecordService.class);

  private final ActivityRecordStorage activityRecordstorage;

  public ActivityRecordService(ActivityRecordStorage activityRecordstorage) {
    this.activityRecordstorage = activityRecordstorage;

  }

  /**
   * Create new ActivityRecord 
   * 
   * @param activityRecord ActivityRecord to create
   * @return stored {@link ActivityRecord} in datasource
   * @throws Exception when ActivityRecord already exists or an error occurs while
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
   * {@link EntityNotFoundException} will be thrown.
   * 
   * @param ActivityRecord dto to update on store
   * @param username username storing ActivityRecord
   * @return stored {@link ActivityRecord} in datasource
   * @throws Exception when {@link EntityNotFoundException} is thrown or an error
   *           occurs while saving ActivityRecord
   */
  public ActivityRecord updateActivityRecord(ActivityRecord ActivityRecord, String username) throws Exception {
    if (ActivityRecord == null) {
      throw new IllegalArgumentException("ActivityRecord is mandatory");
    }
    if (StringUtils.isBlank(username)) {
      throw new IllegalArgumentException("username is mandatory");
    }
    Long ActivityRecordId = ActivityRecord.getId();
    if (ActivityRecordId == null) {
      throw new EntityNotFoundException("ActivityRecord with null id wasn't found");
    }
    ActivityRecord storedActivityRecord = activityRecordstorage.getActivityRecordById(ActivityRecordId);
    if (storedActivityRecord == null) {
      throw new EntityNotFoundException("ActivityRecord with id " + ActivityRecordId + " wasn't found");
    }
    return activityRecordstorage.updateActivityRecord(ActivityRecord);
  }

  /**
   * Delete ActivityRecord identified by its id and check if username has permission to
   * delete it.
   * 
   * @param activityRecordId technical identifier of ActivityRecord
   * @param username user currently deleting ActivityRecord
   * @throws EntityNotFoundException if ActivityRecord wasn't found
   * @throws IllegalAccessException if user is not allowed to delete ActivityRecord
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
   * @return List of {@link ActivityRecord} that contains the list of ActivityRecords
   */
  public List<ActivityRecord> getActivityRecords() {
    return activityRecordstorage.getActivityRecords();
  }

  /**
   * Retrieves the list of ActivityRecordsListAccess with offset, limit and a keyword that can be
   * empty
   *
   * @return List of {@link ActivityRecord} that contains the list of ActivityRecords
   */
  public RecordsAccessList getActivityRecordsList() {
    return activityRecordstorage.getActivityRecordsList();
  }

  /**
   * Retrieves the list of ActivityRecords wby day
   * @param day day of activity
   *
   * @return List of {@link ActivityRecord} that contains the list of ActivityRecords
   */
  public List<ActivityRecord> getActivityRecordsList(String day,String userName) {
    return activityRecordstorage.getActivityRecords(day,userName);
  }

}
