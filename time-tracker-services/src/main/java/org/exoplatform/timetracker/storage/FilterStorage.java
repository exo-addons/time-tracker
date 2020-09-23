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

import org.exoplatform.timetracker.dao.FilterDAO;
import org.exoplatform.timetracker.dao.FilterFieldDAO;
import org.exoplatform.timetracker.dao.ProjectDAO;
import org.exoplatform.timetracker.dto.Filter;
import org.exoplatform.timetracker.dto.FilterField;
import org.exoplatform.timetracker.dto.Project;
import org.exoplatform.timetracker.entity.FilterEntity;
import org.exoplatform.timetracker.entity.FilterFieldEntity;
import org.exoplatform.timetracker.entity.ProjectEntity;
import org.gatein.api.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Storage service to access / load and save Filters. This service will be
 * used , as well, to convert from JPA entity to DTO.
 *
 * @author medamine
 * @version $Id: $Id
 */
public class FilterStorage {

  private final FilterDAO filterDAO;
  private final FilterFieldDAO filterFieldDAO;

  /**
   * <p>Constructor for FilterStorage.</p>
   *
   * @param filterDAO a {@link org.exoplatform.timetracker.dao.FilterDAO} object.
   * @param filterFieldDAO a {@link org.exoplatform.timetracker.dao.FilterFieldDAO} object.
   */
  public FilterStorage(FilterDAO filterDAO, FilterFieldDAO filterFieldDAO) {
    this.filterDAO = filterDAO;
    this.filterFieldDAO = filterFieldDAO;
  }


  /////////////////////////Filter storage /////////////////////////////////////////

  /**
   * <p>createFilter.</p>
   *
   * @param filter a {@link org.exoplatform.timetracker.dto.Filter} object.
   * @return a {@link org.exoplatform.timetracker.dto.Filter} object.
   * @throws java.lang.Exception if any.
   */
  public Filter createFilter(Filter filter) throws Exception {
    if (filter == null) {
      throw new IllegalArgumentException("Filter is mandatory");
    }
    FilterEntity filterEntity = toEntity(filter);
    filter.setId(null);
    filterEntity = filterDAO.create(filterEntity);
    return toDTO(filterEntity);
  }

  /**
   * <p>updateFilter.</p>
   *
   * @param filter a {@link org.exoplatform.timetracker.dto.Filter} object.
   * @return a {@link org.exoplatform.timetracker.dto.Filter} object.
   * @throws java.lang.Exception if any.
   */
  public Filter updateFilter(Filter filter) throws Exception {
    if (filter == null) {
      throw new IllegalArgumentException("Filter is mandatory");
    }
    Long filterId = filter.getId();
    FilterEntity filterEntity = filterDAO.find(filter.getId());
    if (filterEntity == null) {
      throw new EntityNotFoundException("Filter with id " + filterId + " wasn't found");
    }

    filterEntity = toEntity(filter);
    filterEntity = filterDAO.update(filterEntity);

    return toDTO(filterEntity);
  }

  /**
   * <p>deleteFilter.</p>
   *
   * @param filterId a long.
   * @throws org.gatein.api.EntityNotFoundException if any.
   */
  public void deleteFilter(long filterId) throws EntityNotFoundException {
    if (filterId <= 0) {
      throw new IllegalArgumentException("FilterId must be a positive integer");
    }
    FilterEntity filterEntity = filterDAO.find(filterId);
    if (filterEntity == null) {
      throw new EntityNotFoundException("Filter with id " + filterId + " not found");
    }
    filterDAO.delete(filterEntity);
  }

  /**
   * <p>getFilterById.</p>
   *
   * @param FilterId a long.
   * @return a {@link org.exoplatform.timetracker.dto.Filter} object.
   */
  public Filter getFilterById(long FilterId) {
    if (FilterId <= 0) {
      throw new IllegalArgumentException("FilterId must be a positive integer");
    }
    FilterEntity FilterEntity = filterDAO.find(FilterId);
    return toDTO(FilterEntity);
  }

  /**
   * <p>getFilters.</p>
   *
   * @return a {@link java.util.List} object.
   */
  public List<Filter> getFilters() {
    List<FilterEntity> applicatiions = filterDAO.findAll();
    return applicatiions.stream().map(this::toDTO).collect(Collectors.toList());
  }

  /**
   * <p>getFiltersByUserName.</p>
   *
   * @param userName a {@link java.lang.String} object.
   * @return a {@link java.util.List} object.
   */
  public List<Filter> getFiltersByUserName(String userName) {
    List<FilterEntity> applicatiions = filterDAO.getFiltersByUserName(userName);
    return applicatiions.stream().map(this::toDTO).collect(Collectors.toList());
  }

  /**
   * <p>countFilters.</p>
   *
   * @return a long.
   */
  public long countFilters() {
    return filterDAO.count();
  }

  /**
   * <p>toDTO.</p>
   *
   * @param filterEntity a {@link org.exoplatform.timetracker.entity.FilterEntity} object.
   * @return a {@link org.exoplatform.timetracker.dto.Filter} object.
   */
  public Filter toDTO(FilterEntity filterEntity) {
    if (filterEntity == null) {
      return null;
    }
    return new Filter(filterEntity.getId(),
            filterEntity.getName(),
            filterEntity.getUserName());
  }

  /**
   * <p>toEntity.</p>
   *
   * @param filter a {@link org.exoplatform.timetracker.dto.Filter} object.
   * @return a {@link org.exoplatform.timetracker.entity.FilterEntity} object.
   */
  public FilterEntity toEntity(Filter filter) {
    if (filter == null) {
      return null;
    }
    return new FilterEntity(filter.getId(),
            filter.getName(),
            filter.getUserName());
  }


/////////////////////////Filter Fields storage /////////////////////////////////////////

  /**
   * <p>createFilterField.</p>
   *
   * @param filterField a {@link org.exoplatform.timetracker.dto.FilterField} object.
   * @return a {@link org.exoplatform.timetracker.dto.FilterField} object.
   * @throws java.lang.Exception if any.
   */
  public FilterField createFilterField(FilterField filterField) throws Exception {
    if (filterField == null) {
      throw new IllegalArgumentException("FilterField is mandatory");
    }
    FilterFieldEntity filterFieldEntity = toEntity(filterField);
    filterField.setId(null);
    filterFieldEntity = filterFieldDAO.create(filterFieldEntity);
    return toDTO(filterFieldEntity);
  }

  /**
   * <p>updateFilterField.</p>
   *
   * @param filterField a {@link org.exoplatform.timetracker.dto.FilterField} object.
   * @return a {@link org.exoplatform.timetracker.dto.FilterField} object.
   * @throws java.lang.Exception if any.
   */
  public FilterField updateFilterField(FilterField filterField) throws Exception {
    if (filterField == null) {
      throw new IllegalArgumentException("FilterField is mandatory");
    }
    Long filterFieldId = filterField.getId();
    FilterFieldEntity filterFieldEntity = filterFieldDAO.find(filterField.getId());
    if (filterFieldEntity == null) {
      throw new EntityNotFoundException("FilterField with id " + filterFieldId + " wasn't found");
    }

    filterFieldEntity = toEntity(filterField);
    filterFieldEntity = filterFieldDAO.update(filterFieldEntity);

    return toDTO(filterFieldEntity);
  }

  /**
   * <p>deleteFilterField.</p>
   *
   * @param filterFieldId a long.
   * @throws org.gatein.api.EntityNotFoundException if any.
   */
  public void deleteFilterField(long filterFieldId) throws EntityNotFoundException {
    if (filterFieldId <= 0) {
      throw new IllegalArgumentException("FilterFieldId must be a positive integer");
    }
    FilterFieldEntity filterFieldEntity = filterFieldDAO.find(filterFieldId);
    if (filterFieldEntity == null) {
      throw new EntityNotFoundException("FilterField with id " + filterFieldId + " not found");
    }
    filterFieldDAO.delete(filterFieldEntity);
  }

  /**
   * <p>deleteAllFilterFieldsByFilter.</p>
   *
   * @param filterFieldId a long.
   * @throws org.gatein.api.EntityNotFoundException if any.
   */
  public void deleteAllFilterFieldsByFilter(long filterFieldId) throws EntityNotFoundException {
    if (filterFieldId <= 0) {
      throw new IllegalArgumentException("FilterFieldId must be a positive integer");
    }
    List<FilterFieldEntity> filterFieldEntities = filterFieldDAO.getFieldsByFilter(filterFieldId);
    filterFieldDAO.deleteAll(filterFieldEntities);
  }


  /**
   * <p>getFilterFieldById.</p>
   *
   * @param FilterFieldId a long.
   * @return a {@link org.exoplatform.timetracker.dto.FilterField} object.
   */
  public FilterField getFilterFieldById(long FilterFieldId) {
    if (FilterFieldId <= 0) {
      throw new IllegalArgumentException("FilterFieldId must be a positive integer");
    }
    FilterFieldEntity FilterFieldEntity = filterFieldDAO.find(FilterFieldId);
    return toDTO(FilterFieldEntity);
  }

  /**
   * <p>getFilterFields.</p>
   *
   * @return a {@link java.util.List} object.
   */
  public List<FilterField> getFilterFields() {
    List<FilterFieldEntity> applicatiions = filterFieldDAO.findAll();
    return applicatiions.stream().map(this::toDTO).collect(Collectors.toList());
  }

  /**
   * <p>getFilterFieldsByFilter.</p>
   *
   * @param filterId a long.
   * @return a {@link java.util.List} object.
   */
  public List<FilterField> getFilterFieldsByFilter(long filterId) {
    List<FilterFieldEntity> applicatiions = filterFieldDAO.getFieldsByFilter(filterId);
    return applicatiions.stream().map(this::toDTO).collect(Collectors.toList());
  }

  /**
   * <p>countFilterFields.</p>
   *
   * @return a long.
   */
  public long countFilterFields() {
    return filterFieldDAO.count();
  }

  /**
   * <p>toDTO.</p>
   *
   * @param filterFieldEntity a {@link org.exoplatform.timetracker.entity.FilterFieldEntity} object.
   * @return a {@link org.exoplatform.timetracker.dto.FilterField} object.
   */
  public FilterField toDTO(FilterFieldEntity filterFieldEntity) {
    if (filterFieldEntity == null) {
      return null;
    }
    return new FilterField(filterFieldEntity.getId(),
            filterFieldEntity.getName(),
            filterFieldEntity.getValue(),
            toDTO(filterFieldEntity.getFilterEntity()));
  }

  /**
   * <p>toEntity.</p>
   *
   * @param filterField a {@link org.exoplatform.timetracker.dto.FilterField} object.
   * @return a {@link org.exoplatform.timetracker.entity.FilterFieldEntity} object.
   */
  public FilterFieldEntity toEntity(FilterField filterField) {
    if (filterField == null) {
      return null;
    }
    return new FilterFieldEntity(filterField.getId(),
            filterField.getName(),
            filterField.getValue(),
            toEntity(filterField.getFilter()));
  }


}
