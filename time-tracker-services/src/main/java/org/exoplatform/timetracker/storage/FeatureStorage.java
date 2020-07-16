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

import java.util.List;
import java.util.stream.Collectors;

import org.gatein.api.EntityNotFoundException;

import org.exoplatform.timetracker.dao.FeatureDAO;
import org.exoplatform.timetracker.dto.Feature;
import org.exoplatform.timetracker.entity.FeatureEntity;

/**
 * Storage service to access / load and save Features. This service will be
 * used , as well, to convert from JPA entity to DTO.
 */
public class FeatureStorage {

  private final FeatureDAO featureDAO;

  public FeatureStorage(FeatureDAO featureDAO) {
    this.featureDAO = featureDAO;
  }

  public Feature createFeature(Feature feature) throws Exception {
    if (feature == null) {
      throw new IllegalArgumentException("Feature is mandatory");
    }
    FeatureEntity featureEntity = toEntity(feature);
    feature.setId(null);
    featureEntity = featureDAO.create(featureEntity);
    return toDTO(featureEntity);
  }

  public Feature updateFeature(Feature feature) throws Exception {
    if (feature == null) {
      throw new IllegalArgumentException("Feature is mandatory");
    }
    Long featureId = feature.getId();
    FeatureEntity featureEntity = featureDAO.find(feature.getId());
    if (featureEntity == null) {
      throw new EntityNotFoundException("Feature with id " + featureId + " wasn't found");
    }

    featureEntity = toEntity(feature);
    featureEntity = featureDAO.update(featureEntity);

    return toDTO(featureEntity);
  }

  public void deleteFeature(long featureId) throws EntityNotFoundException {
    if (featureId <= 0) {
      throw new IllegalArgumentException("FeatureId must be a positive integer");
    }
    FeatureEntity featureEntity = featureDAO.find(featureId);
    if (featureEntity == null) {
      throw new EntityNotFoundException("Feature with id " + featureId + " not found");
    }
    featureDAO.delete(featureEntity);
  }

  public Feature getFeatureById(long FeatureId) {
    if (FeatureId <= 0) {
      throw new IllegalArgumentException("FeatureId must be a positive integer");
    }
    FeatureEntity FeatureEntity = featureDAO.find(FeatureId);
    return toDTO(FeatureEntity);
  }

  public List<Feature> getFeatures() {
    List<FeatureEntity> applicatiions = featureDAO.findAll();
    return applicatiions.stream().map(this::toDTO).collect(Collectors.toList());
  }

  public long countFeatures() {
    return featureDAO.count();
  }

  public Feature toDTO(FeatureEntity featureEntity) {
    if (featureEntity == null) {
      return null;
    }
    return new Feature(featureEntity.getId(),
                        featureEntity.getCode(),
                        featureEntity.getLabel(),
                        featureEntity.getSpec(),
                        featureEntity.getExo());
  }

  public FeatureEntity toEntity(Feature feature) {
    if (feature == null) {
      return null;
    }
    return new FeatureEntity(feature.getId(),
                              feature.getCode(),
                              feature.getLabel(),
                              feature.getSpec(),
                              feature.getExo());
  }

}
