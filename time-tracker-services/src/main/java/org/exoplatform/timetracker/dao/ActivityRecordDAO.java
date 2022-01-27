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
                    queryString = queryString + " activityRecord.activityEntity.id in (" + convert(activity) + ")";
                    queryString = queryString + " and ";
                }
                if (StringUtils.isNotEmpty(type )) {
                    queryString = queryString + " activityRecord.activityEntity.typeEntity.id in (" + convert(type) + ")";
                    queryString = queryString + " and ";
                }

                if (StringUtils.isNotEmpty(subType) ) {
                    queryString = queryString + " activityRecord.activityEntity.subTypeEntity.id in (" + convert(subType) + ")";
                    queryString = queryString + " and ";
                }


                if (StringUtils.isNotEmpty(activityCode) ) {
                    queryString = queryString + " activityRecord.activityEntity.activityCodeEntity.id in (" + convert(activityCode) + ")";
                    queryString = queryString + " and ";
                }


                if (StringUtils.isNotEmpty(subActivityCode )) {
                    queryString = queryString + " activityRecord.activityEntity.subActivityCodeEntity.id in (" + convert(subActivityCode) + ")";
                    queryString = queryString + " and ";
                }

                if (StringUtils.isNotEmpty(client) ) {
                    queryString = queryString + " activityRecord.activityEntity.projectEntity.clientEntity.id in (" + convert(client) + ")";
                    queryString = queryString + " or activityRecord.clientEntity.id in (" + convert(client) + ")";
                    queryString = queryString + " and ";
                }

                if (StringUtils.isNotEmpty(project) ) {
                    queryString = queryString + " activityRecord.activityEntity.projectEntity.id in (" + convert(project) + ")";
                    queryString = queryString + " or activityRecord.projectEntity.id in (" + convert(project) + ")";
                    queryString = queryString + " and ";
                }

                if (StringUtils.isNotEmpty(feature) ) {
                    queryString = queryString + " activityRecord.activityEntity.featureEntity.id in (" + convert(feature) + ")";
                    queryString = queryString + " and ";
                }

                if (StringUtils.isNotEmpty(userName) && !userName.equals("all")) {
                    queryString = queryString + " activityRecord.userName = '" + userName + "'";
                    queryString = queryString + " and ";
                }


                if (StringUtils.isNotEmpty(location)) {
                    queryString = queryString + " activityRecord.location in (" + convert(location)+ ")";
                    queryString = queryString + " and ";
                }

                if (StringUtils.isNotEmpty(office)) {
                    queryString = queryString + " activityRecord.office in (" + convert(office) + ")";
                    queryString = queryString + " and ";
                }


                if (StringUtils.isNotEmpty(fromDate)) {
                    try {
                        long from = formatter.parse(fromDate).getTime();
                        String date = quryDateFormatter.format(from);
                        queryString = queryString + " TIMESTAMP(activityRecord.activityTime) >= '" + date + "'";
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
                        queryString = queryString + " TIMESTAMP(activityRecord.activityTime) <= '" + date + "'";
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
                        queryString = queryString + " activityRecord.activityEntity.id in (" + convert(activity) + ")";
                        queryString = queryString + " and ";
                    }
                    if (StringUtils.isNotEmpty(type )) {
                        queryString = queryString + " activityRecord.activityEntity.typeEntity.id in (" + convert(type) + ")";
                        queryString = queryString + " and ";
                    }

                    if (StringUtils.isNotEmpty(subType) ) {
                        queryString = queryString + " activityRecord.activityEntity.subTypeEntity.id in (" + convert(subType) + ")";
                        queryString = queryString + " and ";
                    }


                    if (StringUtils.isNotEmpty(activityCode) ) {
                        queryString = queryString + " activityRecord.activityEntity.activityCodeEntity.id in (" + convert(activityCode) + ")";
                        queryString = queryString + " and ";
                    }


                    if (StringUtils.isNotEmpty(subActivityCode )) {
                        queryString = queryString + " activityRecord.activityEntity.subActivityCodeEntity.id in (" + convert(subActivityCode) + ")";
                        queryString = queryString + " and ";
                    }

                    if (StringUtils.isNotEmpty(client) ) {
                        queryString = queryString + " activityRecord.activityEntity.projectEntity.clientEntity.id in (" + convert(client) + ")";
                        queryString = queryString + " and ";
                    }

                    if (StringUtils.isNotEmpty(project) ) {
                        queryString = queryString + " activityRecord.activityEntity.projectEntity.id in (" + convert(project) + ")";
                        queryString = queryString + " and ";
                    }

                    if (StringUtils.isNotEmpty(feature) ) {
                        queryString = queryString + " activityRecord.activityEntity.featureEntity.id in (" + convert(feature) + ")";
                        queryString = queryString + " and ";
                    }

                    if (StringUtils.isNotEmpty(userName) && !userName.equals("all")) {
                        queryString = queryString + " activityRecord.userName = '" + userName + "'";
                        queryString = queryString + " and ";
                    }


                    if (StringUtils.isNotEmpty(location)) {
                        queryString = queryString + " activityRecord.location in (" + convert(location)+ ")";
                        queryString = queryString + " and ";
                    }

                    if (StringUtils.isNotEmpty(office)) {
                        queryString = queryString + " activityRecord.office in (" + convert(office) + ")";
                        queryString = queryString + " and ";
                    }


                    if (StringUtils.isNotEmpty(fromDate)) {
                        try {
                            long from = formatter.parse(fromDate).getTime();
                            String date = quryDateFormatter.format(from);
                            queryString = queryString + " TIMESTAMP(activityRecord.activityTime) >= '" + date + "'";
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
                            queryString = queryString + " TIMESTAMP(activityRecord.activityTime) <= '" + date + "'";
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
    
    boolean isNotEmpty (Long value){
        if(value!=null  && value > 0) return true;
        return false;
    }

    String convert(String field){
        return "'"+field.replaceAll(",","','")+"'";
    }
}
