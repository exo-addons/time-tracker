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
import org.exoplatform.timetracker.entity.ActivityRecordEntity;
import org.exoplatform.timetracker.entity.FilterEntity;
import org.exoplatform.timetracker.entity.FilterFieldEntity;
import org.exoplatform.timetracker.entity.TypeEntity;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Krout MedAmine
 */
public class FilterFieldDAO extends GenericDAOJPAImpl<FilterFieldEntity, Long> {
  private static final Log LOG = ExoLogger.getLogger(FilterFieldDAO.class);




  public List<FilterFieldEntity> getFieldsByFilter(Long filerId) {

    TypedQuery<FilterFieldEntity> query = getEntityManager().createNamedQuery("FilterFieldEntity.getFieldsByFilter", FilterFieldEntity.class)
            .setParameter("filerId", filerId);
    try {
      return query.getResultList();
    } catch (NoResultException e) {
      return new ArrayList<FilterFieldEntity>();
    } catch (Exception e) {
      LOG.error("Error occurred when trying to get list of fields by filter {}", filerId, e);
      return new ArrayList<FilterFieldEntity>();
    }
  }

}
