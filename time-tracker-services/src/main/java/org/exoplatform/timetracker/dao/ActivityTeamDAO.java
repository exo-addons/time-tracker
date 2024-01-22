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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.exoplatform.commons.persistence.impl.GenericDAOJPAImpl;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.timetracker.entity.ActivityTeamEntity;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

/**
 * <p>ActivityTeamDAO class.</p>
 *
 * @author Krout MedAmine
 * @version $Id: $Id
 */
public class ActivityTeamDAO extends GenericDAOJPAImpl<ActivityTeamEntity, Serializable> {
  private static final Log LOG = ExoLogger.getLogger(ActivityTeamDAO.class);

  /**
   * <p>getActivitiesByTeams.</p>
   *
   * @param teams a {@link java.util.List} object.
   * @return a {@link java.util.List} object.
   */
  public List<ActivityTeamEntity> getActivitiesByTeams(List<String> teams) {

    TypedQuery<ActivityTeamEntity> query = getEntityManager().createNamedQuery("ActivityTeamEntity.getActivitiesByTeams", ActivityTeamEntity.class)
            .setParameter("teams", teams);
    try {
      return query.getResultList();
    } catch (NoResultException e) {
      return new ArrayList<ActivityTeamEntity>();
    } catch (Exception e) {
      LOG.error("Error occurred when trying to get list of Activities by user teams", e);
      return new ArrayList<ActivityTeamEntity>();
    }
  }
  /**
   * <p>getTeamsByActivity.</p>
   *
   * @param activityId a long.
   * @return a {@link java.util.List} object.
   */
  public List<ActivityTeamEntity> getTeamsByActivity(long activityId) {

    TypedQuery<ActivityTeamEntity> query = getEntityManager().createNamedQuery("ActivityTeamEntity.getTeamsByActivity", ActivityTeamEntity.class)
            .setParameter("activityId", activityId);
    try {
      return query.getResultList();
    } catch (NoResultException e) {
      return new ArrayList<ActivityTeamEntity>();
    } catch (Exception e) {
      LOG.error("Error occurred when trying to get list of Activities by user teams", e);
      return new ArrayList<ActivityTeamEntity>();
    }
  }

}
