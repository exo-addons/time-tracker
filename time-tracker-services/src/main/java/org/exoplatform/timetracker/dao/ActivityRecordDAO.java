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


import org.apache.commons.lang.StringUtils;
import org.exoplatform.commons.persistence.impl.GenericDAOJPAImpl;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.timetracker.entity.ActivityRecordEntity;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Krout MedAmine
 */
public class ActivityRecordDAO extends GenericDAOJPAImpl<ActivityRecordEntity, Long> {
    private static final Log LOG = ExoLogger.getLogger(ActivityRecordDAO.class);

    private final String DATE_FORMAT = "yyyy-MM-dd";

    private final SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
    public static final String QUERY_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final SimpleDateFormat quryDateFormatter = new SimpleDateFormat(QUERY_DATE_FORMAT);

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

    public List<ActivityRecordEntity> getActivityRecords(String search,
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

        try {
            String queryString = "SELECT activityRecord FROM ActivityRecordEntity activityRecord";
            if (StringUtils.isNotEmpty(search) || isEmpty(activity)  || isEmpty(type)
                    || isEmpty(subType)  || isEmpty(activityCode)  || isEmpty(subActivityCode)
                    || isEmpty(client)  || isEmpty(project)  || isEmpty(feature)  || StringUtils.isNotEmpty(fromDate) || StringUtils.isNotEmpty(toDate)
                    || StringUtils.isNotEmpty(location) || StringUtils.isNotEmpty(office)) {
                queryString = queryString + " where ";
                if (StringUtils.isNotEmpty(search)) {
                    search = search.toLowerCase();
                    queryString = queryString + " lower(activityRecord.description) LIKE '%' || '" + search
                            + "'|| '%' or lower(activityRecord.userName) LIKE '%' || '" + search + "' || '%'";

                    queryString = queryString + " and ";
                }
                if (isEmpty(activity )) {
                    queryString = queryString + " activityRecord.activityEntity.id = '" + activity + "'";
                    queryString = queryString + " and ";
                }
                if (isEmpty(type )) {
                    queryString = queryString + " activityRecord.activityEntity.typeEntity.id = '" + type + "'";
                    queryString = queryString + " and ";
                }

                if (isEmpty(subType) ) {
                    queryString = queryString + " activityRecord.activityEntity.subTypeEntity.id = '" + subType + "'";
                    queryString = queryString + " and ";
                }


                if (isEmpty(activityCode) ) {
                    queryString = queryString + " activityRecord.activityEntity.activityCode.id = '" + activityCode + "'";
                    queryString = queryString + " and ";
                }


                if (isEmpty(subActivityCode )) {
                    queryString = queryString + " activityRecord.activityEntity.subActivityCode.id = '" + subActivityCode + "'";
                    queryString = queryString + " and ";
                }

                if (isEmpty(client) ) {
                    queryString = queryString + " activityRecord.activityEntity.projectEntity.clientEntity.id = '" + client + "'";
                    queryString = queryString + " and ";
                }

                if (isEmpty(project) ) {
                    queryString = queryString + " activityRecord.activityEntity.projectEntity.id = '" + project + "'";
                    queryString = queryString + " and ";
                }

                if (isEmpty(feature) ) {
                    queryString = queryString + " activityRecord.activityEntity.featureEntity.id = '" + feature + "'";
                    queryString = queryString + " and ";
                }

                if (StringUtils.isNotEmpty(userName)) {
                    queryString = queryString + " activityRecord.userName = '" + userName + "'";
                    queryString = queryString + " and ";
                }


                if (StringUtils.isNotEmpty(location)) {
                    queryString = queryString + " activityRecord.location = '" + location + "'";
                    queryString = queryString + " and ";
                }

                if (StringUtils.isNotEmpty(office)) {
                    queryString = queryString + " activityRecord.office= '" + office + "'";
                    queryString = queryString + " and ";
                }


                if (StringUtils.isNotEmpty(fromDate)) {
                    try {
                        long from = formatter.parse(fromDate).getTime();
                        String date = quryDateFormatter.format(from);
                        queryString = queryString + " TIMESTAMP(activityRecord.from) >= '" + date + "'";
                        queryString = queryString + " and ";
                    } catch (Exception e) {
                        LOG.error("Cannot parse from date, the from date filer will not applied to get th list of activityRecords");
                    }
                }
                if (StringUtils.isNotEmpty(toDate)) {
                    try {
                        Date to = formatter.parse(toDate);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(to);
                        cal.set(Calendar.MINUTE, 59);
                        cal.set(Calendar.SECOND, 59);
                        cal.set(Calendar.HOUR_OF_DAY, 23);
                        String date = quryDateFormatter.format(cal.getTime());
                        queryString = queryString + " TIMESTAMP(activityRecord.from) <= '" + date + "'";
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
                queryString = queryString + " ORDER BY activityRecord.from DESC";
            }
            TypedQuery<ActivityRecordEntity> query = getEntityManager().createQuery(queryString, ActivityRecordEntity.class);
            if (offset >= 0 && limit > 0) {
                query.setFirstResult(offset).setMaxResults(limit);
            }
            return query.getResultList();
        } catch (Exception e) {
            LOG.warn("Exception while attempting to get scores with offset = '" + offset + "' and limit = '" + limit + "'.", e);
            throw e;
        }
    }

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
                                     String office)
    {

            try {
                String queryString = "SELECT count(activityRecord.id) FROM  ActivityRecordEntity activityRecord";
                if (StringUtils.isNotEmpty(search) || isEmpty(activity)  || isEmpty(type)
                        || isEmpty(subType)  || isEmpty(activityCode)  || isEmpty(subActivityCode)
                        || isEmpty(client)  || isEmpty(project)  || isEmpty(feature)  || StringUtils.isNotEmpty(fromDate) || StringUtils.isNotEmpty(toDate)
                        || StringUtils.isNotEmpty(location) || StringUtils.isNotEmpty(office)) {
                    queryString = queryString + " where ";
                    if (StringUtils.isNotEmpty(search)) {
                        search = search.toLowerCase();
                        queryString = queryString + " lower(activityRecord.description) LIKE '%' || '" + search
                                + "'|| '%' or lower(activityRecord.userName) LIKE '%' || '" + search + "' || '%'";

                        queryString = queryString + " and ";
                    }
                    if (isEmpty(activity )) {
                        queryString = queryString + " activityRecord.activityEntity.id = '" + activity + "'";
                        queryString = queryString + " and ";
                    }
                    if (isEmpty(type )) {
                        queryString = queryString + " activityRecord.activityEntity.typeEntity.id = '" + type + "'";
                        queryString = queryString + " and ";
                    }

                    if (isEmpty(subType) ) {
                        queryString = queryString + " activityRecord.activityEntity.subTypeEntity.id = '" + subType + "'";
                        queryString = queryString + " and ";
                    }


                    if (isEmpty(activityCode) ) {
                        queryString = queryString + " activityRecord.activityEntity.activityCode.id = '" + activityCode + "'";
                        queryString = queryString + " and ";
                    }


                    if (isEmpty(subActivityCode )) {
                        queryString = queryString + " activityRecord.activityEntity.subActivityCode.id = '" + subActivityCode + "'";
                        queryString = queryString + " and ";
                    }

                    if (isEmpty(client) ) {
                        queryString = queryString + " activityRecord.activityEntity.projectEntity.clientEntity.id = '" + client + "'";
                        queryString = queryString + " and ";
                    }

                    if (isEmpty(project) ) {
                        queryString = queryString + " activityRecord.activityEntity.projectEntity.id = '" + project + "'";
                        queryString = queryString + " and ";
                    }

                    if (isEmpty(feature) ) {
                        queryString = queryString + " activityRecord.activityEntity.featureEntity.id = '" + feature + "'";
                        queryString = queryString + " and ";
                    }

                    if (StringUtils.isNotEmpty(userName)) {
                    queryString = queryString + " activityRecord.userName = '" + userName + "'";
                    queryString = queryString + " and ";
                }


                if (StringUtils.isNotEmpty(location)) {
                    queryString = queryString + " activityRecord.location = '" + location + "'";
                    queryString = queryString + " and ";
                }

                if (StringUtils.isNotEmpty(office)) {
                    queryString = queryString + " activityRecord.office= '" + office + "'";
                    queryString = queryString + " and ";
                }


                if (StringUtils.isNotEmpty(fromDate)) {
                    try {
                        long from = formatter.parse(fromDate).getTime();
                        String date = quryDateFormatter.format(from);
                        queryString = queryString + " TIMESTAMP(activityRecord.from) >= '" + date + "'";
                        queryString = queryString + " and ";
                    } catch (Exception e) {
                        LOG.error("Cannot parse from date, the from date filer will not applied to get th list of activityRecords");
                    }
                }
                if (StringUtils.isNotEmpty(toDate)) {
                    try {
                        Date to = formatter.parse(toDate);
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(to);
                        cal.set(Calendar.MINUTE, 59);
                        cal.set(Calendar.SECOND, 59);
                        cal.set(Calendar.HOUR_OF_DAY, 23);
                        String date = quryDateFormatter.format(cal.getTime());
                        queryString = queryString + " TIMESTAMP(activityRecord.from) <= '" + date + "'";
                        queryString = queryString + " and ";
                    } catch (Exception e) {
                        LOG.error("Cannot parse from date, the to date filer will not applied to get the list of activityRecords");
                    }
                }
                    if (queryString.endsWith(" and ")) {
                        queryString = queryString.substring(0, queryString.length() - 5);
                    }
            }
            return getEntityManager().createQuery(queryString, Long.class).getSingleResult();
        } catch (Exception e) {
            LOG.warn("Exception while attempting to get activityRecords count.", e);
            throw e;
        }
    }
    
    boolean isEmpty (Long value){
        if(value!=null  && value > 0) return true;
        return false;
    }
}
