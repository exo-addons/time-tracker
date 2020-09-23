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

import org.exoplatform.timetracker.dto.SubType;
import org.exoplatform.timetracker.entity.SubTypeEntity;
import org.gatein.api.EntityNotFoundException;

import org.exoplatform.timetracker.dao.ActivityCodeDAO;
import org.exoplatform.timetracker.dao.SubActivityCodeDAO;
import org.exoplatform.timetracker.dao.SubTypeDAO;
import org.exoplatform.timetracker.dao.TypeDAO;
import org.exoplatform.timetracker.dto.ActivityCode;
import org.exoplatform.timetracker.dto.SubActivityCode;
import org.exoplatform.timetracker.dto.Type;
import org.exoplatform.timetracker.entity.ActivityCodeEntity;
import org.exoplatform.timetracker.entity.SubActivityCodeEntity;
import org.exoplatform.timetracker.entity.TypeEntity;

/**
 * Storage service to access / load and save Codes. This service will be used ,
 * as well, to convert from JPA entity to DTO.
 *
 * @author medamine
 * @version $Id: $Id
 */
public class CodesStorage {

  private final ActivityCodeDAO    activityCodeDAO;

  private final SubActivityCodeDAO subActivityCodeDAO;

  private final TypeDAO            typeDAO;

  private final SubTypeDAO         subTypeDAO;

  /**
   * <p>Constructor for CodesStorage.</p>
   *
   * @param activityCodeDAO a {@link org.exoplatform.timetracker.dao.ActivityCodeDAO} object.
   * @param subActivityCodeDAO a {@link org.exoplatform.timetracker.dao.SubActivityCodeDAO} object.
   * @param typeDAO a {@link org.exoplatform.timetracker.dao.TypeDAO} object.
   * @param subTypeDAO a {@link org.exoplatform.timetracker.dao.SubTypeDAO} object.
   */
  public CodesStorage(ActivityCodeDAO activityCodeDAO,
                      SubActivityCodeDAO subActivityCodeDAO,
                      TypeDAO typeDAO,
                      SubTypeDAO subTypeDAO) {

    this.activityCodeDAO = activityCodeDAO;
    this.subActivityCodeDAO = subActivityCodeDAO;
    this.typeDAO = typeDAO;
    this.subTypeDAO = subTypeDAO;
  }

  ///////////////////////////// Activity Code Storage //////////////////////////

  /**
   * <p>createActivityCode.</p>
   *
   * @param activityCode a {@link org.exoplatform.timetracker.dto.ActivityCode} object.
   * @return a {@link org.exoplatform.timetracker.dto.ActivityCode} object.
   * @throws java.lang.Exception if any.
   */
  public ActivityCode createActivityCode(ActivityCode activityCode) throws Exception {
    if (activityCode == null) {
      throw new IllegalArgumentException("ActivityCode is mandatory");
    }
    ActivityCodeEntity activityCodeEntity = toActivityCodeEntity(activityCode);
    activityCode.setId(null);
    activityCodeEntity = activityCodeDAO.create(activityCodeEntity);
    return toActivityCodeDTO(activityCodeEntity);
  }

  /**
   * <p>updateActivityCode.</p>
   *
   * @param activityCode a {@link org.exoplatform.timetracker.dto.ActivityCode} object.
   * @return a {@link org.exoplatform.timetracker.dto.ActivityCode} object.
   * @throws java.lang.Exception if any.
   */
  public ActivityCode updateActivityCode(ActivityCode activityCode) throws Exception {
    if (activityCode == null) {
      throw new IllegalArgumentException("ActivityCode is mandatory");
    }
    Long activityCodeId = activityCode.getId();
    ActivityCodeEntity activityCodeEntity = activityCodeDAO.find(activityCode.getId());
    if (activityCodeEntity == null) {
      throw new EntityNotFoundException("ActivityCode with id " + activityCodeId + " wasn't found");
    }

    activityCodeEntity = toActivityCodeEntity(activityCode);
    activityCodeEntity = activityCodeDAO.update(activityCodeEntity);

    return toActivityCodeDTO(activityCodeEntity);
  }

  /**
   * <p>deleteActivityCode.</p>
   *
   * @param activityCodeId a long.
   * @throws org.gatein.api.EntityNotFoundException if any.
   */
  public void deleteActivityCode(long activityCodeId) throws EntityNotFoundException {
    if (activityCodeId <= 0) {
      throw new IllegalArgumentException("ActivityCodeId must be a positive integer");
    }
    ActivityCodeEntity activityCodeEntity = activityCodeDAO.find(activityCodeId);
    if (activityCodeEntity == null) {
      throw new EntityNotFoundException("ActivityCode with id " + activityCodeId + " not found");
    }
    activityCodeDAO.delete(activityCodeEntity);
  }

  /**
   * <p>getActivityCodeById.</p>
   *
   * @param ActivityCodeId a long.
   * @return a {@link org.exoplatform.timetracker.dto.ActivityCode} object.
   */
  public ActivityCode getActivityCodeById(long ActivityCodeId) {
    if (ActivityCodeId <= 0) {
      throw new IllegalArgumentException("ActivityCodeId must be a positive integer");
    }
    ActivityCodeEntity ActivityCodeEntity = activityCodeDAO.find(ActivityCodeId);
    return toActivityCodeDTO(ActivityCodeEntity);
  }

  /**
   * <p>getActivityCodes.</p>
   *
   * @return a {@link java.util.List} object.
   */
  public List<ActivityCode> getActivityCodes() {
    List<ActivityCodeEntity> applicatiions = activityCodeDAO.findAll();
    return applicatiions.stream().map(this::toActivityCodeDTO).collect(Collectors.toList());
  }

  /**
   * <p>countActivityCodes.</p>
   *
   * @return a long.
   */
  public long countActivityCodes() {
    return activityCodeDAO.count();
  }

  /**
   * <p>toActivityCodeDTO.</p>
   *
   * @param activityCodeEntity a {@link org.exoplatform.timetracker.entity.ActivityCodeEntity} object.
   * @return a {@link org.exoplatform.timetracker.dto.ActivityCode} object.
   */
  public ActivityCode toActivityCodeDTO(ActivityCodeEntity activityCodeEntity) {
    if (activityCodeEntity == null) {
      return null;
    }
    return new ActivityCode(activityCodeEntity.getId(), activityCodeEntity.getCode(), activityCodeEntity.getLabel());
  }

  /**
   * <p>toActivityCodeEntity.</p>
   *
   * @param activityCode a {@link org.exoplatform.timetracker.dto.ActivityCode} object.
   * @return a {@link org.exoplatform.timetracker.entity.ActivityCodeEntity} object.
   */
  public ActivityCodeEntity toActivityCodeEntity(ActivityCode activityCode) {
    if (activityCode == null) {
      return null;
    }
    return new ActivityCodeEntity(activityCode.getId(), activityCode.getCode(), activityCode.getLabel());
  }

  ///////////////////////////// Activity Code Storage //////////////////////////

  /**
   * <p>createSubActivityCode.</p>
   *
   * @param subActivityCode a {@link org.exoplatform.timetracker.dto.SubActivityCode} object.
   * @return a {@link org.exoplatform.timetracker.dto.SubActivityCode} object.
   * @throws java.lang.Exception if any.
   */
  public SubActivityCode createSubActivityCode(SubActivityCode subActivityCode) throws Exception {
    if (subActivityCode == null) {
      throw new IllegalArgumentException("SubActivityCode is mandatory");
    }
    SubActivityCodeEntity subActivityCodeEntity = toSubActivityCodeEntity(subActivityCode);
    subActivityCode.setId(null);
    subActivityCodeEntity = subActivityCodeDAO.create(subActivityCodeEntity);
    return toSubActivityCodeDTO(subActivityCodeEntity);
  }

  /**
   * <p>updateSubActivityCode.</p>
   *
   * @param subActivityCode a {@link org.exoplatform.timetracker.dto.SubActivityCode} object.
   * @return a {@link org.exoplatform.timetracker.dto.SubActivityCode} object.
   * @throws java.lang.Exception if any.
   */
  public SubActivityCode updateSubActivityCode(SubActivityCode subActivityCode) throws Exception {
    if (subActivityCode == null) {
      throw new IllegalArgumentException("SubActivityCode is mandatory");
    }
    Long subActivityCodeId = subActivityCode.getId();
    SubActivityCodeEntity subActivityCodeEntity = subActivityCodeDAO.find(subActivityCode.getId());
    if (subActivityCodeEntity == null) {
      throw new EntityNotFoundException("SubActivityCode with id " + subActivityCodeId + " wasn't found");
    }

    subActivityCodeEntity = toSubActivityCodeEntity(subActivityCode);
    subActivityCodeEntity = subActivityCodeDAO.update(subActivityCodeEntity);

    return toSubActivityCodeDTO(subActivityCodeEntity);
  }

  /**
   * <p>deleteSubActivityCode.</p>
   *
   * @param subActivityCodeId a long.
   * @throws org.gatein.api.EntityNotFoundException if any.
   */
  public void deleteSubActivityCode(long subActivityCodeId) throws EntityNotFoundException {
    if (subActivityCodeId <= 0) {
      throw new IllegalArgumentException("SubActivityCodeId must be a positive integer");
    }
    SubActivityCodeEntity subActivityCodeEntity = subActivityCodeDAO.find(subActivityCodeId);
    if (subActivityCodeEntity == null) {
      throw new EntityNotFoundException("SubActivityCode with id " + subActivityCodeId + " not found");
    }
    subActivityCodeDAO.delete(subActivityCodeEntity);
  }

  /**
   * <p>getSubActivityCodeById.</p>
   *
   * @param SubActivityCodeId a long.
   * @return a {@link org.exoplatform.timetracker.dto.SubActivityCode} object.
   */
  public SubActivityCode getSubActivityCodeById(long SubActivityCodeId) {
    if (SubActivityCodeId <= 0) {
      throw new IllegalArgumentException("SubActivityCodeId must be a positive integer");
    }
    SubActivityCodeEntity SubActivityCodeEntity = subActivityCodeDAO.find(SubActivityCodeId);
    return toSubActivityCodeDTO(SubActivityCodeEntity);
  }

  /**
   * <p>getSubActivityCodes.</p>
   *
   * @return a {@link java.util.List} object.
   */
  public List<SubActivityCode> getSubActivityCodes() {
    List<SubActivityCodeEntity> applicatiions = subActivityCodeDAO.findAll();
    return applicatiions.stream().map(this::toSubActivityCodeDTO).collect(Collectors.toList());
  }

  /**
   * <p>countSubActivityCodes.</p>
   *
   * @return a long.
   */
  public long countSubActivityCodes() {
    return subActivityCodeDAO.count();
  }

  /**
   * <p>toSubActivityCodeDTO.</p>
   *
   * @param subActivityCodeEntity a {@link org.exoplatform.timetracker.entity.SubActivityCodeEntity} object.
   * @return a {@link org.exoplatform.timetracker.dto.SubActivityCode} object.
   */
  public SubActivityCode toSubActivityCodeDTO(SubActivityCodeEntity subActivityCodeEntity) {
    if (subActivityCodeEntity == null) {
      return null;
    }
    return new SubActivityCode(subActivityCodeEntity.getId(),
                               subActivityCodeEntity.getCode(),
                               subActivityCodeEntity.getLabel(),
                               toActivityCodeDTO(subActivityCodeEntity.getActivityCodeEntity()));
  }

  /**
   * <p>toSubActivityCodeEntity.</p>
   *
   * @param subActivityCode a {@link org.exoplatform.timetracker.dto.SubActivityCode} object.
   * @return a {@link org.exoplatform.timetracker.entity.SubActivityCodeEntity} object.
   */
  public SubActivityCodeEntity toSubActivityCodeEntity(SubActivityCode subActivityCode) {
    if (subActivityCode == null) {
      return null;
    }
    return new SubActivityCodeEntity(subActivityCode.getId(),
                                     subActivityCode.getCode(),
                                     subActivityCode.getLabel(),
                                     toActivityCodeEntity(subActivityCode.getActivityCode()));
  }

  ///////////////////////////// Type Storage //////////////////////////

  /**
   * <p>createType.</p>
   *
   * @param type a {@link org.exoplatform.timetracker.dto.Type} object.
   * @return a {@link org.exoplatform.timetracker.dto.Type} object.
   * @throws java.lang.Exception if any.
   */
  public Type createType(Type type) throws Exception {
    if (type == null) {
      throw new IllegalArgumentException("Type is mandatory");
    }
    TypeEntity typeEntity = toTypeEntity(type);
    type.setId(null);
    typeEntity = typeDAO.create(typeEntity);
    return toTypeDTO(typeEntity);
  }

  /**
   * <p>updateType.</p>
   *
   * @param type a {@link org.exoplatform.timetracker.dto.Type} object.
   * @return a {@link org.exoplatform.timetracker.dto.Type} object.
   * @throws java.lang.Exception if any.
   */
  public Type updateType(Type type) throws Exception {
    if (type == null) {
      throw new IllegalArgumentException("Type is mandatory");
    }
    Long typeId = type.getId();
    TypeEntity typeEntity = typeDAO.find(type.getId());
    if (typeEntity == null) {
      throw new EntityNotFoundException("Type with id " + typeId + " wasn't found");
    }

    typeEntity = toTypeEntity(type);
    typeEntity = typeDAO.update(typeEntity);

    return toTypeDTO(typeEntity);
  }

  /**
   * <p>deleteType.</p>
   *
   * @param typeId a long.
   * @throws org.gatein.api.EntityNotFoundException if any.
   */
  public void deleteType(long typeId) throws EntityNotFoundException {
    if (typeId <= 0) {
      throw new IllegalArgumentException("TypeId must be a positive integer");
    }
    TypeEntity typeEntity = typeDAO.find(typeId);
    if (typeEntity == null) {
      throw new EntityNotFoundException("Type with id " + typeId + " not found");
    }
    typeDAO.delete(typeEntity);
  }

  /**
   * <p>getTypeById.</p>
   *
   * @param TypeId a long.
   * @return a {@link org.exoplatform.timetracker.dto.Type} object.
   */
  public Type getTypeById(long TypeId) {
    if (TypeId <= 0) {
      throw new IllegalArgumentException("TypeId must be a positive integer");
    }
    TypeEntity TypeEntity = typeDAO.find(TypeId);
    return toTypeDTO(TypeEntity);
  }

  /**
   * <p>getTypes.</p>
   *
   * @return a {@link java.util.List} object.
   */
  public List<Type> getTypes() {
    List<TypeEntity> applicatiions = typeDAO.findAll();
    return applicatiions.stream().map(this::toTypeDTO).collect(Collectors.toList());
  }

  /**
   * <p>countTypes.</p>
   *
   * @return a long.
   */
  public long countTypes() {
    return typeDAO.count();
  }

  /**
   * <p>toTypeDTO.</p>
   *
   * @param typeEntity a {@link org.exoplatform.timetracker.entity.TypeEntity} object.
   * @return a {@link org.exoplatform.timetracker.dto.Type} object.
   */
  public Type toTypeDTO(TypeEntity typeEntity) {
    if (typeEntity == null) {
      return null;
    }
    return new Type(typeEntity.getId(), typeEntity.getCode(), typeEntity.getLabel());
  }

  /**
   * <p>toTypeEntity.</p>
   *
   * @param type a {@link org.exoplatform.timetracker.dto.Type} object.
   * @return a {@link org.exoplatform.timetracker.entity.TypeEntity} object.
   */
  public TypeEntity toTypeEntity(Type type) {
    if (type == null) {
      return null;
    }
    return new TypeEntity(type.getId(), type.getCode(), type.getLabel());
  }

  ///////////////////////////// Sub Type Storage //////////////////////////


  /**
   * <p>createSubType.</p>
   *
   * @param subType a {@link org.exoplatform.timetracker.dto.SubType} object.
   * @return a {@link org.exoplatform.timetracker.dto.SubType} object.
   * @throws java.lang.Exception if any.
   */
  public SubType createSubType(SubType subType) throws Exception {
    if (subType == null) {
      throw new IllegalArgumentException("SubType is mandatory");
    }
    SubTypeEntity subTypeEntity = toSubTypeEntity(subType);
    subType.setId(null);
    subTypeEntity = subTypeDAO.create(subTypeEntity);
    return toSubTypeDTO(subTypeEntity);
  }

  /**
   * <p>updateSubType.</p>
   *
   * @param subType a {@link org.exoplatform.timetracker.dto.SubType} object.
   * @return a {@link org.exoplatform.timetracker.dto.SubType} object.
   * @throws java.lang.Exception if any.
   */
  public SubType updateSubType(SubType subType) throws Exception {
    if (subType == null) {
      throw new IllegalArgumentException("SubType is mandatory");
    }
    Long subTypeId = subType.getId();
    SubTypeEntity subTypeEntity = subTypeDAO.find(subType.getId());
    if (subTypeEntity == null) {
      throw new EntityNotFoundException("SubType with id " + subTypeId + " wasn't found");
    }

    subTypeEntity = toSubTypeEntity(subType);
    subTypeEntity = subTypeDAO.update(subTypeEntity);

    return toSubTypeDTO(subTypeEntity);
  }

  /**
   * <p>deleteSubType.</p>
   *
   * @param subTypeId a long.
   * @throws org.gatein.api.EntityNotFoundException if any.
   */
  public void deleteSubType(long subTypeId) throws EntityNotFoundException {
    if (subTypeId <= 0) {
      throw new IllegalArgumentException("SubTypeId must be a positive integer");
    }
    SubTypeEntity subTypeEntity = subTypeDAO.find(subTypeId);
    if (subTypeEntity == null) {
      throw new EntityNotFoundException("SubType with id " + subTypeId + " not found");
    }
    subTypeDAO.delete(subTypeEntity);
  }

  /**
   * <p>getSubTypeById.</p>
   *
   * @param SubTypeId a long.
   * @return a {@link org.exoplatform.timetracker.dto.SubType} object.
   */
  public SubType getSubTypeById(long SubTypeId) {
    if (SubTypeId <= 0) {
      throw new IllegalArgumentException("SubTypeId must be a positive integer");
    }
    SubTypeEntity SubTypeEntity = subTypeDAO.find(SubTypeId);
    return toSubTypeDTO(SubTypeEntity);
  }

  /**
   * <p>getSubTypes.</p>
   *
   * @return a {@link java.util.List} object.
   */
  public List<SubType> getSubTypes() {
    List<SubTypeEntity> applicatiions = subTypeDAO.findAll();
    return applicatiions.stream().map(this::toSubTypeDTO).collect(Collectors.toList());
  }

  /**
   * <p>countSubTypes.</p>
   *
   * @return a long.
   */
  public long countSubTypes() {
    return subTypeDAO.count();
  }

  /**
   * <p>toSubTypeDTO.</p>
   *
   * @param subTypeEntity a {@link org.exoplatform.timetracker.entity.SubTypeEntity} object.
   * @return a {@link org.exoplatform.timetracker.dto.SubType} object.
   */
  public SubType toSubTypeDTO(SubTypeEntity subTypeEntity) {
    if (subTypeEntity == null) {
      return null;
    }
    return new SubType(subTypeEntity.getId(),
            subTypeEntity.getCode(),
            subTypeEntity.getLabel(),
            toTypeDTO(subTypeEntity.getTypeEntity()));
  }

  /**
   * <p>toSubTypeEntity.</p>
   *
   * @param subType a {@link org.exoplatform.timetracker.dto.SubType} object.
   * @return a {@link org.exoplatform.timetracker.entity.SubTypeEntity} object.
   */
  public SubTypeEntity toSubTypeEntity(SubType subType) {
    if (subType == null) {
      return null;
    }
    return new SubTypeEntity(subType.getId(),
            subType.getCode(),
            subType.getLabel(),
            toTypeEntity(subType.getType()));
  }


}
