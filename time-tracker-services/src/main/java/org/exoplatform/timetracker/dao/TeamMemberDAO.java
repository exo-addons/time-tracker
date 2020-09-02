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

import org.exoplatform.commons.persistence.impl.GenericDAOJPAImpl;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.timetracker.entity.FilterFieldEntity;
import org.exoplatform.timetracker.entity.SubTypeEntity;
import org.exoplatform.timetracker.entity.TeamMemberEntity;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Krout MedAmine
 */
public class TeamMemberDAO extends GenericDAOJPAImpl<TeamMemberEntity, Long> {
  private static final Log LOG = ExoLogger.getLogger(TeamMemberDAO.class);


  public List<TeamMemberEntity> getTeamsByUser(String userName) {

    TypedQuery<TeamMemberEntity> query = getEntityManager().createNamedQuery("TeamMemberEntity.getTeamsByUser", TeamMemberEntity.class)
            .setParameter("userName", userName);
    try {
      return query.getResultList();
    } catch (NoResultException e) {
      return new ArrayList<TeamMemberEntity>();
    } catch (Exception e) {
      LOG.error("Error occurred when trying to get list of Teams by user {}", userName, e);
      return new ArrayList<TeamMemberEntity>();
    }
  }

  public List<TeamMemberEntity> getMembersByTeam(long teamId) {

    TypedQuery<TeamMemberEntity> query = getEntityManager().createNamedQuery("TeamMemberEntity.getMembersByTeam", TeamMemberEntity.class)
            .setParameter("teamId", teamId);
    try {
      return query.getResultList();
    } catch (NoResultException e) {
      return new ArrayList<TeamMemberEntity>();
    } catch (Exception e) {
      LOG.error("Error occurred when trying to get list of users by teamId {}", teamId, e);
      return new ArrayList<TeamMemberEntity>();
    }
  }

  public TeamMemberEntity getMemberByTeamUserAndRole(long teamId, String userName, String role) {

    TypedQuery<TeamMemberEntity> query = getEntityManager().createNamedQuery("TeamMemberEntity.getMemberByTeamUserAndRole", TeamMemberEntity.class)
            .setParameter("teamId", teamId).setParameter("userName", userName).setParameter("role", role);
    try {
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    } catch (Exception e) {
      LOG.error("Error occurred when trying to get list of users by teamId {}", teamId, e);
      return null;
    }
  }

}
