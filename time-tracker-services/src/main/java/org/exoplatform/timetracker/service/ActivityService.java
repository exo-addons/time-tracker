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
import org.exoplatform.timetracker.dto.Activity;
import org.exoplatform.timetracker.storage.ActivityStorage;

/**
 * A Service to access and store Activities
 *
 * @author medamine
 * @version $Id: $Id
 */
public class ActivityService {

  private static final Log      LOG = ExoLogger.getLogger(ActivityService.class);

  private final ActivityStorage activitiestorage;

  /**
   * <p>Constructor for ActivityService.</p>
   *
   * @param activitiestorage a {@link org.exoplatform.timetracker.storage.ActivityStorage} object.
   */
  public ActivityService(ActivityStorage activitiestorage) {
    this.activitiestorage = activitiestorage;

  }

  /**
   * Create new Activity that will be available for all users. If the Activity
   * already exits an {@link javax.persistence.EntityExistsException} will be thrown.
   *
   * @param activity Activity to create
   * @return stored {@link org.exoplatform.timetracker.dto.Activity} in datasource
   * @throws java.lang.Exception when Activity already exists or an error occurs while
   *           creating Activity or its attached image
   */
  public Activity createActivity(Activity activity) throws Exception {
    if (activity == null) {
      throw new IllegalArgumentException("Activity is mandatory");
    }
/*    Activity existingActivity = activitiestorage.getActivityById(activity.getId());
    if (existingActivity != null) {
      throw new EntityExistsException("An Activity with same code exists");

    }*/
    return activitiestorage.createActivity(activity);
  }

  /**
   * Update an existing Activity on datasource. If the Activity doesn't exit an
   * {@link javax.persistence.EntityNotFoundException} will be thrown.
   *
   * @param Activity dto to update on store
   * @param username username storing Activity
   * @return stored {@link org.exoplatform.timetracker.dto.Activity} in datasource
   * @throws java.lang.Exception when {@link javax.persistence.EntityNotFoundjava.lang.Exception} is thrown or an error
   *           occurs while saving Activity
   */
  public Activity updateActivity(Activity Activity, String username) throws Exception {
    if (Activity == null) {
      throw new IllegalArgumentException("Activity is mandatory");
    }
    if (StringUtils.isBlank(username)) {
      throw new IllegalArgumentException("username is mandatory");
    }
    Long ActivityId = Activity.getId();
    if (ActivityId == null) {
      throw new EntityNotFoundException("Activity with null id wasn't found");
    }
    Activity storedActivity = activitiestorage.getActivityById(ActivityId);
    if (storedActivity == null) {
      throw new EntityNotFoundException("Activity with id " + ActivityId + " wasn't found");
    }
    return activitiestorage.updateActivity(Activity);
  }

  /**
   * Delete Activity identified by its id and check if username has permission to
   * delete it.
   *
   * @param activityId technical identifier of Activity
   * @param username user currently deleting Activity
   * @throws javax.persistence.EntityNotFoundException if Activity wasn't found
   * @throws java.lang.IllegalAccessException if user is not allowed to delete Activity
   */
  public void deleteActivity(Long activityId, String username) throws EntityNotFoundException, IllegalAccessException {
    if (activityId == null || activityId <= 0) {
      throw new IllegalArgumentException("ActivityId must be a positive integer");
    }
    if (StringUtils.isBlank(username)) {
      throw new IllegalArgumentException("username is mandatory");
    }

    Activity storedActivity = activitiestorage.getActivityById(activityId);
    if (storedActivity == null) {
      throw new EntityNotFoundException("Activity with id " + activityId + " not found");
    }
    activitiestorage.deleteActivity(activityId);
  }

  /**
   * Retrieves the list of Activities with offset, limit and a keyword that can be
   * empty
   *
   * @return List of {@link org.exoplatform.timetracker.dto.Activity} that contains the list of Activities
   */
  public List<Activity> getActivitiesList() {
    return activitiestorage.getActivities();
  }

  /**
   * Retrieves the list of Activities with offset, limit and a keyword that can be
   * empty
   *
   * @return List of {@link org.exoplatform.timetracker.dto.Activity} that contains the list of Activities
   * @param teams a {@link java.util.List} object.
   */
  public List<Activity> getActivitiesforUser(List<String> teams) {

    return activitiestorage.getActivitiesByTeams(teams);
  }

}
