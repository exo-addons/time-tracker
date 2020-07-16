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

import java.util.List;
import java.util.stream.Collectors;

import org.gatein.api.EntityNotFoundException;

import org.exoplatform.timetracker.dao.ActivityDAO;
import org.exoplatform.timetracker.dto.Activity;
import org.exoplatform.timetracker.entity.ActivityEntity;

/**
 * Storage service to access / load and save Activitys. This service will be
 * used , as well, to convert from JPA entity to DTO.
 */
public class ActivityStorage {

  private final ActivityDAO    activityDAO;

  private final ProjectStorage projectStorage;

  private final FeatureStorage featureStorage;

  private final CodesStorage   codesStorage;

  public ActivityStorage(ActivityDAO activityDAO,
                         ProjectStorage projectStorage,
                         FeatureStorage featureStorage,
                         CodesStorage codesStorage) {
    this.activityDAO = activityDAO;
    this.projectStorage = projectStorage;
    this.featureStorage = featureStorage;
    this.codesStorage = codesStorage;
  }

  public Activity createActivity(Activity activity) throws Exception {
    if (activity == null) {
      throw new IllegalArgumentException("Activity is mandatory");
    }
    ActivityEntity activityEntity = toEntity(activity);
    activity.setId(null);
    activityEntity = activityDAO.create(activityEntity);
    return toDTO(activityEntity);
  }

  public Activity updateActivity(Activity activity) throws Exception {
    if (activity == null) {
      throw new IllegalArgumentException("Activity is mandatory");
    }
    Long activityId = activity.getId();
    ActivityEntity activityEntity = activityDAO.find(activity.getId());
    if (activityEntity == null) {
      throw new EntityNotFoundException("Activity with id " + activityId + " wasn't found");
    }

    activityEntity = toEntity(activity);
    activityEntity = activityDAO.update(activityEntity);

    return toDTO(activityEntity);
  }

  public void deleteActivity(long activityId) throws EntityNotFoundException {
    if (activityId <= 0) {
      throw new IllegalArgumentException("ActivityId must be a positive integer");
    }
    ActivityEntity activityEntity = activityDAO.find(activityId);
    if (activityEntity == null) {
      throw new EntityNotFoundException("Activity with id " + activityId + " not found");
    }
    activityDAO.delete(activityEntity);
  }

  public Activity getActivityById(long ActivityId) {
    if (ActivityId <= 0) {
      throw new IllegalArgumentException("ActivityId must be a positive integer");
    }
    ActivityEntity ActivityEntity = activityDAO.find(ActivityId);
    return toDTO(ActivityEntity);
  }

  public List<Activity> getActivities() {
    List<ActivityEntity> applicatiions = activityDAO.findAll();
    return applicatiions.stream().map(this::toDTO).collect(Collectors.toList());
  }

  public long countActivities() {
    return activityDAO.count();
  }

  public Activity toDTO(ActivityEntity activityEntity) {
    if (activityEntity == null) {
      return null;
    }
    return new Activity(activityEntity.getId(),
                        codesStorage.toTypeDTO(activityEntity.getTypeEntity()),
                        codesStorage.toSubTypeDTO(activityEntity.getSubTypeEntity()),
                        codesStorage.toActivityCodeDTO(activityEntity.getActivityCodeEntity()),
                        codesStorage.toSubActivityCodeDTO(activityEntity.getSubActivityCodeEntity()),
                        activityEntity.getLabel(),
                        projectStorage.toDTO(activityEntity.getProjectEntity()),
                        featureStorage.toDTO(activityEntity.getFeatureEntity()));
  }

  public ActivityEntity toEntity(Activity activity) {
    if (activity == null) {
      return null;
    }
    return new ActivityEntity(activity.getId(),
                              activity.getLabel(),
                              projectStorage.toEntity(activity.getProject()),
                              codesStorage.toActivityCodeEntity(activity.getActivityCode()),
                              codesStorage.toSubActivityCodeEntity(activity.getSubActivityCode()),
                              codesStorage.toTypeEntity(activity.getType()),
                              codesStorage.toSubTypeEntity(activity.getSubType()),
                              featureStorage.toEntity(activity.getFeature()));
  }

}
