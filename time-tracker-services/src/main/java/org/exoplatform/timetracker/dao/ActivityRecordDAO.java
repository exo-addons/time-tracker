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
package org.exoplatform.timetracker.dao;


import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import org.exoplatform.commons.persistence.impl.GenericDAOJPAImpl;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.timetracker.entity.ActivityRecordEntity;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

/**
 * <p>ActivityRecordDAO class.</p>
 *
 * @author Krout MedAmine
 * @version $Id: $Id
 */
public class ActivityRecordDAO extends GenericDAOJPAImpl<ActivityRecordEntity, Long> {
    private static final Log LOG = ExoLogger.getLogger(ActivityRecordDAO.class);

    private final String DATE_FORMAT = "yyyy-MM-dd";

    private final SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
    /** Constant <code>QUERY_DATE_FORMAT="yyyy-MM-dd HH:mm:ss"</code> */
    public static final String QUERY_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /** Constant <code>quryDateFormatter</code> */
    public static final SimpleDateFormat quryDateFormatter = new SimpleDateFormat(QUERY_DATE_FORMAT);

    /**
     * <p>getUserActivityRecordsList.</p>
     *
     * @param day a {@link java.lang.String} object.
     * @param userName a {@link java.lang.String} object.
     * @return a {@link java.util.List} object.
     */
    public List<ActivityRecordEntity> getUserActivityRecordsList(String day, String userName) {

        TypedQuery<ActivityRecordEntity> query = getEntityManager().createNamedQuery("ActivityRecordEntity.getActivityRecordsByDay", ActivityRecordEntity.class)
                .setParameter("day", day)
                .setParameter("userName", userName);
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return new ArrayList<ActivityRecordEntity>();
        } catch (Exception e) {
            LOG.error("Error occurred when trying to get list of activities by day {} and user {}", day, userName, e);
            return new ArrayList<ActivityRecordEntity>();
        }
    }

    /**
     * <p>get Last Activity Record.</p>
     *
     * @param userName a {@link java.lang.String} object.
     * @return a {@link java.util.List} object.
     */
    public ActivityRecordEntity getLastActivityRecord(String userName) {

        TypedQuery<ActivityRecordEntity> query = getEntityManager().createNamedQuery("ActivityRecordEntity.getLastActivityRecord", ActivityRecordEntity.class)
                .setParameter("userName", userName).setMaxResults(1);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            LOG.error("Error occurred when trying to get last activity by user {}",  userName, e);
            return null;
        }
    }

    /**
     * <p>getActivityRecords.</p>
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
     * @return a {@link java.util.List} object.
     */
    public List<ActivityRecordEntity> getActivityRecords(String search,
                                                         String activity,
                                                         String type,
                                                         String subType,
                                                         String activityCode,
                                                         String subActivityCode,
                                                         String client,
                                                         String project,
                                                         String feature,
                                                         String fromDate,
                                                         String toDate,
                                                         String userName,
                                                         String location,
                                                         String office,
                                                         int offset,
                                                         int limit,
                                                         String sortBy,
                                                         boolean sortDesc) {

        try {
            Date from = null;
            Date to = null;
            List<Long> activityList = new ArrayList<>();
            List<Long> typeList = new ArrayList<>();
            List<Long> subTypeList = new ArrayList<>();
            List<Long> activityCodeList = new ArrayList<>();
            List<Long> subActivityCodeList = new ArrayList<>();
            List<Long> clientList = new ArrayList<>();
            List<Long> projectList = new ArrayList<>();
            List<Long> featureList = new ArrayList<>();
            List<String> locationList = new ArrayList<>();
            List<String> officeList = new ArrayList<>();
            String queryString = "SELECT activityRecord FROM ActivityRecordEntity activityRecord";
            if (StringUtils.isNotEmpty(search) || StringUtils.isNotEmpty(activity)  || StringUtils.isNotEmpty(type)  || StringUtils.isNotEmpty(userName)
                    || StringUtils.isNotEmpty(subType)  || StringUtils.isNotEmpty(activityCode)  || StringUtils.isNotEmpty(subActivityCode)
                    || StringUtils.isNotEmpty(client)  || StringUtils.isNotEmpty(project)  || StringUtils.isNotEmpty(feature)  || StringUtils.isNotEmpty(fromDate) || StringUtils.isNotEmpty(toDate)
                    || StringUtils.isNotEmpty(location) || StringUtils.isNotEmpty(office)) {
                queryString = queryString + " where ";
                if (StringUtils.isNotEmpty(search)) {
                    search = search.toLowerCase();
                    queryString = queryString + " lower(activityRecord.description) LIKE '%' || '" + search
                            + "'|| '%' or lower(activityRecord.userName) LIKE '%' || '" + search + "' || '%'";

                    queryString = queryString + " and ";
                }
                if (StringUtils.isNotEmpty(activity )) {
                    activityList = convertLongList(activity);
                    if (!activityList.isEmpty()) {
                        queryString = queryString + " activityRecord.activityEntity.id in :activityList";
                        queryString = queryString + " and ";
                    }
                }
                if (StringUtils.isNotEmpty(type )) {
                    typeList = convertLongList(type);
                    if (!typeList.isEmpty()) {
                        queryString = queryString + " activityRecord.activityEntity.typeEntity.id in :typeList";
                        queryString = queryString + " and ";
                    }
                }

                if (StringUtils.isNotEmpty(subType) ) {
                    subTypeList = convertLongList(subType);
                    if (!subTypeList.isEmpty()) {
                        queryString = queryString + " activityRecord.activityEntity.subTypeEntity.id in :subTypeList";
                        queryString = queryString + " and ";
                    }
                }


                if (StringUtils.isNotEmpty(activityCode) ) {
                    activityCodeList = convertLongList(activityCode);
                    if (!activityCodeList.isEmpty()) {
                        queryString = queryString + " activityRecord.activityEntity.activityCodeEntity.id in :activityCodeList";
                        queryString = queryString + " and ";
                    }
                }


                if (StringUtils.isNotEmpty(subActivityCode )) {
                    subActivityCodeList = convertLongList(subActivityCode);
                    if (!subActivityCodeList.isEmpty()) {
                        queryString = queryString + " activityRecord.activityEntity.subActivityCodeEntity.id in :subActivityCodeList";
                        queryString = queryString + " and ";
                    }
                }

                if (StringUtils.isNotEmpty(client) ) {
                    clientList = convertLongList(client);
                    if (!clientList.isEmpty()) {
                        queryString = queryString + " activityRecord.activityEntity.projectEntity.clientEntity.id in :clientList";
                        queryString = queryString + " or activityRecord.clientEntity.id in :clientList";
                        queryString = queryString + " and ";
                    }
                }

                if (StringUtils.isNotEmpty(project) ) {
                    projectList = convertLongList(project);
                    if (!projectList.isEmpty()) {
                        queryString = queryString + " activityRecord.activityEntity.projectEntity.id in :projectList";
                        queryString = queryString + " or activityRecord.projectEntity.id in :projectList";
                        queryString = queryString + " and ";
                    }
                }

                if (StringUtils.isNotEmpty(feature) ) {
                    featureList = convertLongList(feature);
                    if (!featureList.isEmpty()) {
                        queryString = queryString + " activityRecord.activityEntity.featureEntity.id in :featureList";
                        queryString = queryString + " and ";
                    }
                }

                if (StringUtils.isNotEmpty(userName) && !userName.equals("all")) {
                    queryString = queryString + " activityRecord.userName = '" + userName + "'";
                    queryString = queryString + " and ";
                }


                if (StringUtils.isNotEmpty(location)) {
                    locationList = Arrays.asList(location.split(","));
                    if (!locationList.isEmpty()) {
                        queryString = queryString + " activityRecord.location in :locationList";
                        queryString = queryString + " and ";
                    }
                }

                if (StringUtils.isNotEmpty(office)) {
                    officeList = Arrays.asList(office.split(","));
                    if (!officeList.isEmpty()) {
                        queryString = queryString + " activityRecord.office in :officeList";
                        queryString = queryString + " and ";
                    }
                }


                if (StringUtils.isNotEmpty(fromDate)) {
                    try {
                        from = formatter.parse(fromDate);
                        queryString = queryString + " activityRecord.activityTime >=  :fromDate";
                        queryString = queryString + " and ";
                    } catch (Exception e) {
                        LOG.error("Cannot parse from date, the from date filer will not applied to get th list of activityRecords");
                    }
                }
                if (StringUtils.isNotEmpty(toDate)) {
                    try {
                        to = formatter.parse(toDate);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(to);
                        cal.set(Calendar.MINUTE, 59);
                        cal.set(Calendar.SECOND, 59);
                        cal.set(Calendar.HOUR_OF_DAY, 23);
                        to = cal.getTime();
                        queryString = queryString + " activityRecord.activityTime <= :toDate";
                        queryString = queryString + " and ";
                    } catch (Exception e) {
                        LOG.error("Cannot parse from date, the to date filer will not applied to get the list of activityRecords");
                    }
                }
                if (queryString.endsWith(" and ")) {
                    queryString = queryString.substring(0, queryString.length() - 5);
                }
            }
            if (StringUtils.isNotEmpty(sortBy)) {
                if (sortDesc) {
                    queryString = queryString + " ORDER BY activityRecord." + sortBy + " DESC";
                } else {
                    queryString = queryString + " ORDER BY activityRecord." + sortBy + " ASC";
                }
            } else {
                queryString = queryString + " ORDER BY activityRecord.activityTime DESC";
            }
            TypedQuery<ActivityRecordEntity> query = getEntityManager().createQuery(queryString, ActivityRecordEntity.class);
            if (from != null) {
                query.setParameter("fromDate",from);
            }
            if (to != null) {
                query.setParameter("toDate",to);
            }
            if (!activityList.isEmpty()) {
                query.setParameter("activityList", activityList);
            }
            if (!typeList.isEmpty()) {
                query.setParameter("typeList", typeList);
            }
            if (!subTypeList.isEmpty()) {
                query.setParameter("subTypeList", subTypeList);
            }
            if (!activityCodeList.isEmpty()) {
                query.setParameter("activityCodeList", activityCodeList);
            }
            if (!subActivityCodeList.isEmpty()) {
                query.setParameter("subActivityCodeList", subActivityCodeList);
            }
            if (!clientList.isEmpty()) {
                query.setParameter("clientList", clientList);
            }
            if (!projectList.isEmpty()) {
                query.setParameter("projectList", projectList);
            }
            if (!featureList.isEmpty()) {
                query.setParameter("featureList", featureList);
            }
            if (!locationList.isEmpty()) {
                query.setParameter("locationList", locationList);
            }
            if (!officeList.isEmpty()) {
                query.setParameter("officeList", officeList);
            }
            if (offset >= 0 && limit > 0) {
                query.setFirstResult(offset).setMaxResults(limit);
            }
            return query.getResultList();
        } catch (Exception e) {
            LOG.warn("Exception while attempting to get scores with offset = '" + offset + "' and limit = '" + limit + "'.", e);
            throw e;
        }
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
                                     String activity,
                                     String type,
                                     String subType,
                                     String activityCode,
                                     String subActivityCode,
                                     String client,
                                     String project,
                                     String feature,
                                     String fromDate,
                                     String toDate,
                                     String userName,
                                     String location,
                                     String office)
    {

            try {
                Date to = null;
                Date from = null;
                List<Long> activityList = new ArrayList<>();
                List<Long> typeList = new ArrayList<>();
                List<Long> subTypeList = new ArrayList<>();
                List<Long> activityCodeList = new ArrayList<>();
                List<Long> subActivityCodeList = new ArrayList<>();
                List<Long> clientList = new ArrayList<>();
                List<Long> projectList = new ArrayList<>();
                List<Long> featureList = new ArrayList<>();
                List<String> locationList = new ArrayList<>();
                List<String> officeList = new ArrayList<>();

                String queryString = "SELECT count(activityRecord.id) FROM  ActivityRecordEntity activityRecord";
                if (StringUtils.isNotEmpty(search) || StringUtils.isNotEmpty(activity)  || StringUtils.isNotEmpty(type)  || StringUtils.isNotEmpty(userName)
                        || StringUtils.isNotEmpty(subType)  || StringUtils.isNotEmpty(activityCode)  || StringUtils.isNotEmpty(subActivityCode)
                        || StringUtils.isNotEmpty(client)  || StringUtils.isNotEmpty(project)  || StringUtils.isNotEmpty(feature)  || StringUtils.isNotEmpty(fromDate) || StringUtils.isNotEmpty(toDate)
                        || StringUtils.isNotEmpty(location) || StringUtils.isNotEmpty(office)) {
                    queryString = queryString + " where ";
                    if (StringUtils.isNotEmpty(search)) {
                        search = search.toLowerCase();
                        queryString = queryString + " lower(activityRecord.description) LIKE '%' || '" + search
                                + "'|| '%' or lower(activityRecord.userName) LIKE '%' || '" + search + "' || '%'";

                        queryString = queryString + " and ";
                    }
                    if (StringUtils.isNotEmpty(activity )) {
                        activityList = convertLongList(activity);
                        if (!activityList.isEmpty()) {
                            queryString = queryString + " activityRecord.activityEntity.id in :activityList";
                            queryString = queryString + " and ";
                        }
                    }
                    if (StringUtils.isNotEmpty(type )) {
                        typeList = convertLongList(type);
                        if (!typeList.isEmpty()) {
                            queryString = queryString + " activityRecord.activityEntity.typeEntity.id in :typeList";
                            queryString = queryString + " and ";
                        }
                    }

                    if (StringUtils.isNotEmpty(subType) ) {
                        subTypeList = convertLongList(subType);
                        if (!subTypeList.isEmpty()) {
                            queryString = queryString + " activityRecord.activityEntity.subTypeEntity.id in :subTypeList";
                            queryString = queryString + " and ";
                        }
                    }


                    if (StringUtils.isNotEmpty(activityCode) ) {
                        activityCodeList = convertLongList(activityCode);
                        if (!activityCodeList.isEmpty()) {
                            queryString = queryString + " activityRecord.activityEntity.activityCodeEntity.id in :activityCodeList";
                            queryString = queryString + " and ";
                        }
                    }


                    if (StringUtils.isNotEmpty(subActivityCode )) {
                        subActivityCodeList = convertLongList(subActivityCode);
                        if (!subActivityCodeList.isEmpty()) {
                            queryString = queryString + " activityRecord.activityEntity.subActivityCodeEntity.id in :subActivityCodeList";
                            queryString = queryString + " and ";
                        }
                    }

                    if (StringUtils.isNotEmpty(client) ) {
                        clientList = convertLongList(client);
                        if (!clientList.isEmpty()) {
                            queryString = queryString + " activityRecord.activityEntity.projectEntity.clientEntity.id in :clientList";
                            queryString = queryString + " or activityRecord.clientEntity.id in :clientList";
                            queryString = queryString + " and ";
                        }
                    }

                    if (StringUtils.isNotEmpty(project) ) {
                        projectList = convertLongList(project);
                        if (!projectList.isEmpty()) {
                            queryString = queryString + " activityRecord.activityEntity.projectEntity.id in :projectList";
                            queryString = queryString + " or activityRecord.projectEntity.id in :projectList";
                            queryString = queryString + " and ";
                        }
                    }

                    if (StringUtils.isNotEmpty(feature) ) {
                        featureList = convertLongList(feature);
                        if (!featureList.isEmpty()) {
                            queryString = queryString + " activityRecord.activityEntity.featureEntity.id in :featureList";
                            queryString = queryString + " and ";
                        }
                    }

                    if (StringUtils.isNotEmpty(userName) && !userName.equals("all")) {
                        queryString = queryString + " activityRecord.userName = '" + userName + "'";
                        queryString = queryString + " and ";
                    }

                    if (StringUtils.isNotEmpty(location)) {
                        locationList = Arrays.asList(location.split(","));
                        if (!locationList.isEmpty()) {
                            queryString = queryString + " activityRecord.location in :locationList";
                            queryString = queryString + " and ";
                        }
                    }
                    if (StringUtils.isNotEmpty(office)) {
                        officeList = Arrays.asList(office.split(","));
                        if (!officeList.isEmpty()) {
                            queryString = queryString + " activityRecord.office in :officeList";
                            queryString = queryString + " and ";
                        }
                    }

                    if (StringUtils.isNotEmpty(fromDate)) {
                        try {
                            from = formatter.parse(fromDate);
                            queryString = queryString + " activityRecord.activityTime >=  :fromDate";
                            queryString = queryString + " and ";
                        } catch (Exception e) {
                            LOG.error("Cannot parse from date, the from date filer will not applied to get th list of activityRecords");
                        }
                    }
                    if (StringUtils.isNotEmpty(toDate)) {
                        try {
                            to = formatter.parse(toDate);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(to);
                            cal.set(Calendar.MINUTE, 59);
                            cal.set(Calendar.SECOND, 59);
                            cal.set(Calendar.HOUR_OF_DAY, 23);
                            to = cal.getTime();
                            queryString = queryString + " activityRecord.activityTime <= :toDate";
                            queryString = queryString + " and ";
                        } catch (Exception e) {
                            LOG.error("Cannot parse from date, the to date filer will not applied to get the list of activityRecords");
                        }
                    }
                    if (queryString.endsWith(" and ")) {
                        queryString = queryString.substring(0, queryString.length() - 5);
                    }
                }
                TypedQuery<Long> query = getEntityManager().createQuery(queryString, Long.class);
                if (from != null) {
                   query.setParameter("fromDate",from);
                }
                if (to != null) {
                    query.setParameter("toDate",to);
                }
                if (!activityList.isEmpty()) {
                    query.setParameter("activityList", activityList);
                }
                if (!typeList.isEmpty()) {
                    query.setParameter("typeList", typeList);
                }
                if (!subTypeList.isEmpty()) {
                    query.setParameter("subTypeList", subTypeList);
                }
                if (!activityCodeList.isEmpty()) {
                    query.setParameter("activityCodeList", activityCodeList);
                }
                if (!subActivityCodeList.isEmpty()) {
                    query.setParameter("subActivityCodeList", subActivityCodeList);
                }
                if (!clientList.isEmpty()) {
                    query.setParameter("clientList", clientList);
                }
                if (!projectList.isEmpty()) {
                    query.setParameter("projectList", projectList);
                }
                if (!featureList.isEmpty()) {
                    query.setParameter("featureList", featureList);
                }
                if (!locationList.isEmpty()) {
                    query.setParameter("locationList", locationList);
                }
                if (!officeList.isEmpty()) {
                    query.setParameter("officeList", officeList);
                }
            return query.getSingleResult();
        } catch (Exception e) {
            LOG.warn("Exception while attempting to get activityRecords count.", e);
            throw e;
        }
    }

    List<Long> convertLongList(String field) {
        return Stream.of(field.split(","))
                .map(String::trim)
                .map(Long::parseLong).toList();

    }    

}
