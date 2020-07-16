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
package org.exoplatform.timetracker.service;

import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang.StringUtils;

import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.timetracker.dto.*;
import org.exoplatform.timetracker.storage.CodesStorage;

/**
 * A Service to access and store Activities
 */
public class CodesService {

  private static final Log      LOG = ExoLogger.getLogger(CodesService.class);

  private final CodesStorage codesStorage;

  public CodesService(CodesStorage codesStorage) {
    this.codesStorage = codesStorage;

  }

  /**
   * Create new ActivityCode. If the ActivityCode
   * already exits an {@link EntityExistsException} will be thrown.
   *
   * @param activityCode ActivityCode to create
   * @return stored {@link ActivityCode} in datasource
   * @throws Exception when ActivityCode already exists or an error occurs while
   *           creating ActivityCode or its attached image
   */
  public ActivityCode createActivityCode(ActivityCode activityCode) throws Exception {
    return codesStorage.createActivityCode(activityCode);
  }

  /**
   * Update an existing ActivityCode on datasource. If the ActivityCode doesn't exit an
   * {@link EntityNotFoundException} will be thrown.
   *
   * @param activityCode to update on store
   * @param username username storing ActivityCode
   * @return stored {@link ActivityCode} in datasource
   * @throws Exception when {@link EntityNotFoundException} is thrown or an error
   *           occurs while saving ActivityCode
   */
  public ActivityCode updateActivityCode(ActivityCode activityCode, String username) throws Exception {

    return codesStorage.updateActivityCode(activityCode);
  }

  /**
   * Delete ActivityCode identified by its id and check if username has permission to
   * delete it.
   *
   * @param activityCodeId technical identifier of ActivityCode
   * @param username user currently deleting ActivityCode
   * @throws EntityNotFoundException if ActivityCode wasn't found
   * @throws IllegalAccessException if user is not allowed to delete ActivityCode
   */
  public void deleteActivityCode(Long activityCodeId, String username) throws EntityNotFoundException, IllegalAccessException {
    codesStorage.deleteActivityCode(activityCodeId);
  }

  /**
   * Retrieves the list of Activities with offset, limit and a keyword that can be
   * empty
   *
   * @return List of {@link ActivityCode} that contains the list of Activities
   */
  public List<ActivityCode> getActivityCodesList() {
    return codesStorage.getActivityCodes();
  }





  /**
   * Create new SubActivityCode that will be available for all users. If the SubActivityCode
   * already exits an {@link EntityExistsException} will be thrown.
   *
   * @param subActivityCode SubActivityCode to create
   * @return stored {@link SubActivityCode} in datasource
   * @throws Exception when SubActivityCode already exists or an error occurs while
   *           creating SubActivityCode or its attached image
   */
  public SubActivityCode createSubActivityCode(SubActivityCode subActivityCode) throws Exception {
    return codesStorage.createSubActivityCode(subActivityCode);
  }

  /**
   * Update an existing SubActivityCode on datasource. If the SubActivityCode doesn't exit an
   * {@link EntityNotFoundException} will be thrown.
   *
   * @param SubActivityCode dto to update on store
   * @param username username storing SubActivityCode
   * @return stored {@link SubActivityCode} in datasource
   * @throws Exception when {@link EntityNotFoundException} is thrown or an error
   *           occurs while saving SubActivityCode
   */
  public SubActivityCode updateSubActivityCode(SubActivityCode SubActivityCode, String username) throws Exception {
    return codesStorage.updateSubActivityCode(SubActivityCode);
  }

  /**
   * Delete SubActivityCode identified by its id and check if username has permission to
   * delete it.
   *
   * @param subActivityCodeId technical identifier of SubActivityCode
   * @param username user currently deleting SubActivityCode
   * @throws EntityNotFoundException if SubActivityCode wasn't found
   * @throws IllegalAccessException if user is not allowed to delete SubActivityCode
   */
  public void deleteSubActivityCode(Long subActivityCodeId, String username) throws EntityNotFoundException, IllegalAccessException {
    codesStorage.deleteSubActivityCode(subActivityCodeId);
  }

  /**
   * Retrieves the list of Activities with offset, limit and a keyword that can be
   * empty
   *
   * @return List of {@link SubActivityCode} that contains the list of Activities
   */
  public List<SubActivityCode> getSubActivityCodesList() {
    return codesStorage.getSubActivityCodes();
  }




  /**
   * Create new Type that will be available for all users. If the Type
   * already exits an {@link EntityExistsException} will be thrown.
   *
   * @param type Type to create
   * @return stored {@link Type} in datasource
   * @throws Exception when Type already exists or an error occurs while
   *           creating Type or its attached image
   */
  public Type createType(Type type) throws Exception {

    return codesStorage.createType(type);
  }

  /**
   * Update an existing Type on datasource. If the Type doesn't exit an
   * {@link EntityNotFoundException} will be thrown.
   *
   * @param type dto to update on store
   * @param username username storing Type
   * @return stored {@link Type} in datasource
   * @throws Exception when {@link EntityNotFoundException} is thrown or an error
   *           occurs while saving Type
   */
  public Type updateType(Type type, String username) throws Exception {

    return codesStorage.updateType(type);
  }

  /**
   * Delete Type identified by its id and check if username has permission to
   * delete it.
   *
   * @param typeId technical identifier of Type
   * @param username user currently deleting Type
   * @throws EntityNotFoundException if Type wasn't found
   * @throws IllegalAccessException if user is not allowed to delete Type
   */
  public void deleteType(Long typeId, String username) throws EntityNotFoundException, IllegalAccessException {
    codesStorage.deleteType(typeId);
  }

  /**
   * Retrieves the list of Activities with offset, limit and a keyword that can be
   * empty
   *
   * @return List of {@link Type} that contains the list of Activities
   */
  public List<Type> getTypesList() {
    return codesStorage.getTypes();
  }



  /**
   * Create new SubType that will be available for all users. If the SubType
   * already exits an {@link EntityExistsException} will be thrown.
   *
   * @param subType SubType to create
   * @return stored {@link SubType} in datasource
   * @throws Exception when SubType already exists or an error occurs while
   *           creating SubType or its attached image
   */
  public SubType createSubType(SubType subType) throws Exception {
    return codesStorage.createSubType(subType);
  }

  /**
   * Update an existing SubType on datasource. If the SubType doesn't exit an
   * {@link EntityNotFoundException} will be thrown.
   *
   * @param SubType dto to update on store
   * @param username username storing SubType
   * @return stored {@link SubType} in datasource
   * @throws Exception when {@link EntityNotFoundException} is thrown or an error
   *           occurs while saving SubType
   */
  public SubType updateSubType(SubType SubType, String username) throws Exception {
    return codesStorage.updateSubType(SubType);
  }

  /**
   * Delete SubType identified by its id and check if username has permission to
   * delete it.
   *
   * @param subTypeId technical identifier of SubType
   * @param username user currently deleting SubType
   * @throws EntityNotFoundException if SubType wasn't found
   * @throws IllegalAccessException if user is not allowed to delete SubType
   */
  public void deleteSubType(Long subTypeId, String username) throws EntityNotFoundException, IllegalAccessException {
    codesStorage.deleteSubType(subTypeId);
  }

  /**
   * Retrieves the list of Activities with offset, limit and a keyword that can be
   * empty
   *
   * @return List of {@link SubType} that contains the list of Activities
   */
  public List<SubType> getSubTypesList() {
    return codesStorage.getSubTypes();
  }


}
