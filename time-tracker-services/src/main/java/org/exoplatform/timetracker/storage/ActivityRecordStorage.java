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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.exoplatform.timetracker.dto.RecordsAccessList;
import org.gatein.api.EntityNotFoundException;

import org.exoplatform.timetracker.dao.ActivityRecordDAO;
import org.exoplatform.timetracker.dto.ActivityRecord;
import org.exoplatform.timetracker.entity.ActivityRecordEntity;

/**
 * Storage service to access / load and save ActivityRecords. This service will
 * be used , as well, to convert from JPA entity to DTO.
 */
public class ActivityRecordStorage {

  private final ActivityRecordDAO activityRecordDAO;

  private final ClientStorage     clientStorage;

  private final ActivityStorage   activityStorage;

  private final SalesOrderStorage salesOrderStorage;

  private  final String           DATE_FORMAT                     = "yyyy-MM-dd";

  private  final SimpleDateFormat formatter                            = new SimpleDateFormat(DATE_FORMAT);


  public ActivityRecordStorage(ActivityRecordDAO activityRecordDAO,
                               ClientStorage clientStorage,
                               ActivityStorage activityStorage,
                               SalesOrderStorage salesOrderStorage) {
    this.activityRecordDAO = activityRecordDAO;
    this.clientStorage = clientStorage;
    this.activityStorage = activityStorage;
    this.salesOrderStorage = salesOrderStorage;
  }

  public ActivityRecord createActivityRecord(ActivityRecord activityRecord) throws Exception {
    if (activityRecord == null) {
      throw new IllegalArgumentException("ActivityRecord is mandatory");
    }
    ActivityRecordEntity activityRecordEntity = toEntity(activityRecord);
    activityRecordEntity.setId(null);
    activityRecordEntity.setCreatedDate(new Date());
    activityRecordEntity.setFrom(formatter.parse(activityRecordEntity.getActivityDate()));
    activityRecordEntity = activityRecordDAO.create(activityRecordEntity);
    return toDTO(activityRecordEntity);
  }

  public ActivityRecord updateActivityRecord(ActivityRecord activityRecord) throws Exception {
    if (activityRecord == null) {
      throw new IllegalArgumentException("ActivityRecord is mandatory");
    }
    Long activityRecordId = activityRecord.getId();
    ActivityRecordEntity activityRecordEntity = activityRecordDAO.find(activityRecord.getId());
    if (activityRecordEntity == null) {
      throw new EntityNotFoundException("ActivityRecord with id " + activityRecordId + " wasn't found");
    }

    activityRecordEntity = toEntity(activityRecord);
    activityRecordEntity = activityRecordDAO.update(activityRecordEntity);

    return toDTO(activityRecordEntity);
  }

  public void deleteActivityRecord(long activityRecordId) throws EntityNotFoundException {
    if (activityRecordId <= 0) {
      throw new IllegalArgumentException("ActivityRecordId must be a positive integer");
    }
    ActivityRecordEntity activityRecordEntity = activityRecordDAO.find(activityRecordId);
    if (activityRecordEntity == null) {
      throw new EntityNotFoundException("ActivityRecord with id " + activityRecordId + " not found");
    }
    activityRecordDAO.delete(activityRecordEntity);
  }

  public ActivityRecord getActivityRecordById(long ActivityRecordId) {
    if (ActivityRecordId <= 0) {
      throw new IllegalArgumentException("ActivityRecordId must be a positive integer");
    }
    ActivityRecordEntity ActivityRecordEntity = activityRecordDAO.find(ActivityRecordId);
    return toDTO(ActivityRecordEntity);
  }

  public List<ActivityRecord> getActivityRecords() {
    List<ActivityRecordEntity> applicatiions = activityRecordDAO.findAll();
    return applicatiions.stream().map(this::toDTO).collect(Collectors.toList());
  }

  public RecordsAccessList getActivityRecordsList() {
    List<ActivityRecordEntity> applicatiions = activityRecordDAO.findAll();
    RecordsAccessList recordsAccessList = new RecordsAccessList();
    recordsAccessList.setActivityRecords(applicatiions.stream().map(this::toDTO).collect(Collectors.toList()));
    recordsAccessList.setSize(Integer.toUnsignedLong(applicatiions.size()));
    return recordsAccessList;
  }

  public List<ActivityRecord> getActivityRecords(String day, String userName) {
    List<ActivityRecordEntity> applicatiions = activityRecordDAO.getActivityRecordsList(day, userName);
    return applicatiions.stream().map(this::toDTO).collect(Collectors.toList());
  }

  public long countActivityRecords() {
    return activityRecordDAO.count();
  }

  public ActivityRecord toDTO(ActivityRecordEntity activityRecordEntity) {
    if (activityRecordEntity == null) {
      return null;
    }
    return new ActivityRecord(activityRecordEntity.getId(),
                              activityRecordEntity.getUserName(),
                              activityRecordEntity.getActivityDate(),
                              activityRecordEntity.getFrom(),
                              activityRecordEntity.getTo(),
                              activityRecordEntity.getDescription(),
                              activityRecordEntity.getLocation(),
                              activityRecordEntity.getOffice(),
                              activityRecordEntity.getTime(),
                              activityRecordEntity.getProjectVersion(),
                              clientStorage.toDTO(activityRecordEntity.getClientEntity()),
                              activityStorage.toDTO(activityRecordEntity.getActivityEntity()),
                              salesOrderStorage.toDTO(activityRecordEntity.getSalesOrderEntity()),
                              activityRecordEntity.getCreatedDate());
  }

  public ActivityRecordEntity toEntity(ActivityRecord activityRecord) {
    if (activityRecord == null) {
      return null;
    }
    return new ActivityRecordEntity(activityRecord.getId(),
                                    activityRecord.getUserName(),
                                    activityRecord.getActivityDate(),
                                    activityRecord.getFrom(),
                                    activityRecord.getTo(),
                                    activityRecord.getDescription(),
                                    activityRecord.getLocation(),
                                    activityRecord.getOffice(),
                                    activityRecord.getTime(),
                                    activityRecord.getProjectVersion(),
                                    clientStorage.toEntity(activityRecord.getClient()),
                                    activityStorage.toEntity(activityRecord.getActivity()),
                                    salesOrderStorage.toEntity(activityRecord.getSalesOrder()),
                                    activityRecord.getCreatedDate());
  }
}
