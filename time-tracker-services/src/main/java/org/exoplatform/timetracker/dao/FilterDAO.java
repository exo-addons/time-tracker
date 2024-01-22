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

import java.util.ArrayList;
import java.util.List;

import org.exoplatform.commons.persistence.impl.GenericDAOJPAImpl;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.timetracker.entity.FilterEntity;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

/**
 * <p>FilterDAO class.</p>
 *
 * @author Krout MedAmine
 * @version $Id: $Id
 */
public class FilterDAO extends GenericDAOJPAImpl<FilterEntity, Long> {
  private static final Log LOG = ExoLogger.getLogger(FilterDAO.class);

  /**
   * <p>getFiltersByUserName.</p>
   *
   * @param userName a {@link java.lang.String} object.
   * @return a {@link java.util.List} object.
   */
  public List<FilterEntity> getFiltersByUserName(String userName) {

    TypedQuery<FilterEntity> query = getEntityManager().createNamedQuery("FilterEntity.getFiltersByUserName", FilterEntity.class)
            .setParameter("userName",userName);
    try {
      return query.getResultList();
    } catch (NoResultException e) {
      return new ArrayList<FilterEntity>();
    } catch (Exception e) {
      LOG.error("Error occurred when trying to get list of fields by filter {}", userName, e);
      return new ArrayList<FilterEntity>();
    }
  }

}
