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

import org.exoplatform.commons.utils.CommonsUtils;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.timetracker.dto.Activity;
import org.exoplatform.timetracker.dto.RecordsAccessList;
import org.gatein.api.EntityNotFoundException;

import org.exoplatform.timetracker.dao.ActivityRecordDAO;
import org.exoplatform.timetracker.dto.ActivityRecord;
import org.exoplatform.timetracker.entity.ActivityRecordEntity;

/**
 * Storage service to access / load and save ActivityRecords. This service will
 * be used , as well, to convert from JPA entity to DTO.
 *
 * @author medamine
 * @version $Id: $Id
 */
public class ActivityRecordStorage {

    private final ActivityRecordDAO activityRecordDAO;

    private final ClientStorage clientStorage;

    private final ProjectStorage projectStorage;

    private final ActivityStorage activityStorage;

    private final SalesOrderStorage salesOrderStorage;

    private final String DATE_FORMAT = "yyyy-MM-dd";

    private final SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);


    /**
     * <p>Constructor for ActivityRecordStorage.</p>
     *
     * @param activityRecordDAO a {@link org.exoplatform.timetracker.dao.ActivityRecordDAO} object.
     * @param clientStorage a {@link org.exoplatform.timetracker.storage.ClientStorage} object.
     * @param activityStorage a {@link org.exoplatform.timetracker.storage.ActivityStorage} object.
     * @param salesOrderStorage a {@link org.exoplatform.timetracker.storage.SalesOrderStorage} object.
     */
    public ActivityRecordStorage(ActivityRecordDAO activityRecordDAO,
                                 ClientStorage clientStorage,
                                 ActivityStorage activityStorage,
                                 SalesOrderStorage salesOrderStorage,
                                 ProjectStorage projectStorage) {
        this.activityRecordDAO = activityRecordDAO;
        this.clientStorage = clientStorage;
        this.activityStorage = activityStorage;
        this.salesOrderStorage = salesOrderStorage;
        this.projectStorage = projectStorage;
    }

    /**
     * <p>createActivityRecord.</p>
     *
     * @param activityRecord a {@link org.exoplatform.timetracker.dto.ActivityRecord} object.
     * @return a {@link org.exoplatform.timetracker.dto.ActivityRecord} object.
     * @throws java.lang.Exception if any.
     */
    public ActivityRecord createActivityRecord(ActivityRecord activityRecord) throws Exception {
        if (activityRecord == null) {
            throw new IllegalArgumentException("ActivityRecord is mandatory");
        }
        ActivityRecordEntity activityRecordEntity = toEntity(activityRecord);
        activityRecordEntity.setId(null);
        activityRecordEntity.setCreatedDate(new Date());
        activityRecordEntity.setActivityTime(formatter.parse(activityRecordEntity.getActivityDate()));
        if(activityRecord.getActivity()!=null) {
            Activity activity = activityStorage.getActivityById(activityRecord.getActivity().getId());
            if (activityRecord.getProject() != null && !activity.getProject().getCode().equals("<PRJ>")) {
                activityRecord.setProject(null);
            }
            if (activityRecord.getClient() != null && !activity.getProject().getClient().getCode().equals("<CLNT>")) {
                activityRecord.setClient(null);
            }
        }
        activityRecordEntity = activityRecordDAO.create(activityRecordEntity);
        return toDTO(activityRecordEntity);
    }

    /**
     * <p>updateActivityRecord.</p>
     *
     * @param activityRecord a {@link org.exoplatform.timetracker.dto.ActivityRecord} object.
     * @return a {@link org.exoplatform.timetracker.dto.ActivityRecord} object.
     * @throws java.lang.Exception if any.
     */
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

    /**
     * <p>deleteActivityRecord.</p>
     *
     * @param activityRecordId a long.
     * @throws org.gatein.api.EntityNotFoundException if any.
     */
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

    /**
     * <p>getActivityRecordById.</p>
     *
     * @param ActivityRecordId a long.
     * @return a {@link org.exoplatform.timetracker.dto.ActivityRecord} object.
     */
    public ActivityRecord getActivityRecordById(long ActivityRecordId) {
        if (ActivityRecordId <= 0) {
            throw new IllegalArgumentException("ActivityRecordId must be a positive integer");
        }
        ActivityRecordEntity ActivityRecordEntity = activityRecordDAO.find(ActivityRecordId);
        return toDTO(ActivityRecordEntity);
    }

    /**
     * <p>getActivityRecords.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<ActivityRecord> getActivityRecords() {
        List<ActivityRecordEntity> applicatiions = activityRecordDAO.findAll();
        return applicatiions.stream().map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * <p>getUserActivityRecords.</p>
     *
     * @param day a {@link java.lang.String} object.
     * @param userName a {@link java.lang.String} object.
     * @return a {@link java.util.List} object.
     */
    public List<ActivityRecord> getUserActivityRecords(String day, String userName) {
        List<ActivityRecordEntity> applicatiions = activityRecordDAO.getUserActivityRecordsList(day, userName);
        return applicatiions.stream().map(this::toDTO).collect(Collectors.toList());
    }


    /**
     * <p>getActivityRecordsList.</p>
     *
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
     * @return a {@link org.exoplatform.timetracker.dto.RecordsAccessList} object.
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
        List<ActivityRecordEntity> applicatiions = activityRecordDAO.getActivityRecords(search, activity, type, subType, activityCode, subActivityCode, client, project, feature, fromDate, toDate, userName, location, office, offset, limit, sortBy, sortDesc);
        RecordsAccessList recordsAccessList = new RecordsAccessList();
        recordsAccessList.setActivityRecords(applicatiions.stream().map(this::toDTO).collect(Collectors.toList()));
        recordsAccessList.setSize(activityRecordDAO.countActivityRecords(search, activity, type, subType, activityCode, subActivityCode, client, project, feature, fromDate, toDate, userName, location, office));
        return recordsAccessList;
    }

    /**
     * <p>get Last Activity Record.</p>
     *
     * @param userName a {@link String} object.
     * @return a {@link org.exoplatform.timetracker.dto.RecordsAccessList} object.
     */
    public ActivityRecord getLastActivityRecord(String userName) {
        return toDTO(activityRecordDAO.getLastActivityRecord(userName));
    }


    /**
     * <p>countActivityRecords.</p>
     *
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
     * @return a long.
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
        return activityRecordDAO.countActivityRecords(search, activity, type, subType, activityCode, subActivityCode, client, project, feature, fromDate, toDate, userName, location,office);
    }

    /**
     * <p>toDTO.</p>
     *
     * @param activityRecordEntity a {@link org.exoplatform.timetracker.entity.ActivityRecordEntity} object.
     * @return a {@link org.exoplatform.timetracker.dto.ActivityRecord} object.
     */
    public ActivityRecord toDTO(ActivityRecordEntity activityRecordEntity) {
        if (activityRecordEntity == null) {
            return null;
        }
        IdentityManager identityManager = CommonsUtils.getService(IdentityManager.class);
        return new ActivityRecord(activityRecordEntity.getId(),
                activityRecordEntity.getUserName(),
                activityRecordEntity.getActivityDate(),
                activityRecordEntity.getActivityTime(),
                activityRecordEntity.getDescription(),
                activityRecordEntity.getLocation(),
                activityRecordEntity.getOffice(),
                activityRecordEntity.getTime(),
                activityRecordEntity.getProjectVersion(),
                clientStorage.toDTO(activityRecordEntity.getClientEntity()),
                activityStorage.toDTO(activityRecordEntity.getActivityEntity()),
                salesOrderStorage.toDTO(activityRecordEntity.getSalesOrderEntity()),
                activityRecordEntity.getCreatedDate(),identityManager.getOrCreateUserIdentity(activityRecordEntity.getUserName()).getProfile().getFullName(),
                projectStorage.toDTO(activityRecordEntity.getProjectEntity()));
    }

    /**
     * <p>toEntity.</p>
     *
     * @param activityRecord a {@link org.exoplatform.timetracker.dto.ActivityRecord} object.
     * @return a {@link org.exoplatform.timetracker.entity.ActivityRecordEntity} object.
     */
    public ActivityRecordEntity toEntity(ActivityRecord activityRecord) {
        if (activityRecord == null) {
            return null;
        }
        return new ActivityRecordEntity(activityRecord.getId(),
                activityRecord.getUserName(),
                activityRecord.getActivityDate(),
                activityRecord.getActivityTime(),
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
