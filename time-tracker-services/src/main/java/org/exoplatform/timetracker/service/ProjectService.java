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
import org.exoplatform.timetracker.dto.Project;
import org.exoplatform.timetracker.storage.ProjectStorage;

/**
 * A Service to access and store Activities
 *
 * @author medamine
 * @version $Id: $Id
 */
public class ProjectService {

  private static final Log      LOG = ExoLogger.getLogger(ProjectService.class);

  private final ProjectStorage projectStorage;

  /**
   * <p>Constructor for ProjectService.</p>
   *
   * @param projectStorage a {@link org.exoplatform.timetracker.storage.ProjectStorage} object.
   */
  public ProjectService(ProjectStorage projectStorage) {
    this.projectStorage = projectStorage;

  }

  /**
   * Create new Project that will be available for all users. If the Project
   * already exits an {@link javax.persistence.EntityExistsException} will be thrown.
   *
   * @param project Project to create
   * @return stored {@link org.exoplatform.timetracker.dto.Project} in datasource
   * @throws java.lang.Exception when Project already exists or an error occurs while
   *           creating Project or its attached image
   */
  public Project createProject(Project project) throws Exception {
    if (project == null) {
      throw new IllegalArgumentException("Project is mandatory");
    }
/*    Project existingProject = projectStorage.getProjectById(project.getId());
    if (existingProject != null) {
      throw new EntityExistsException("An Project with same code exists");

    }*/
    return projectStorage.createProject(project);
  }

  /**
   * Update an existing Project on datasource. If the Project doesn't exit an
   * {@link javax.persistence.EntityNotFoundException} will be thrown.
   *
   * @param Project dto to update on store
   * @param username username storing Project
   * @return stored {@link org.exoplatform.timetracker.dto.Project} in datasource
   * @throws java.lang.Exception when {@link javax.persistence.EntityNotFoundjava.lang.Exception} is thrown or an error
   *           occurs while saving Project
   */
  public Project updateProject(Project Project, String username) throws Exception {
    if (Project == null) {
      throw new IllegalArgumentException("Project is mandatory");
    }
    if (StringUtils.isBlank(username)) {
      throw new IllegalArgumentException("username is mandatory");
    }
    Long ProjectId = Project.getId();
    if (ProjectId == null) {
      throw new EntityNotFoundException("Project with null id wasn't found");
    }
    Project storedProject = projectStorage.getProjectById(ProjectId);
    if (storedProject == null) {
      throw new EntityNotFoundException("Project with id " + ProjectId + " wasn't found");
    }
    return projectStorage.updateProject(Project);
  }

  /**
   * Delete Project identified by its id and check if username has permission to
   * delete it.
   *
   * @param projectId technical identifier of Project
   * @param username user currently deleting Project
   * @throws javax.persistence.EntityNotFoundException if Project wasn't found
   * @throws java.lang.IllegalAccessException if user is not allowed to delete Project
   */
  public void deleteProject(Long projectId, String username) throws EntityNotFoundException, IllegalAccessException {
    if (projectId == null || projectId <= 0) {
      throw new IllegalArgumentException("ProjectId must be a positive integer");
    }
    if (StringUtils.isBlank(username)) {
      throw new IllegalArgumentException("username is mandatory");
    }

    Project storedProject = projectStorage.getProjectById(projectId);
    if (storedProject == null) {
      throw new EntityNotFoundException("Project with id " + projectId + " not found");
    }
    projectStorage.deleteProject(projectId);
  }

  /**
   * Retrieves the list of Activities with offset, limit and a keyword that can be
   * empty
   *
   * @return List of {@link org.exoplatform.timetracker.dto.Project} that contains the list of Activities
   */
  public List<Project> getProjectsList() {
    return projectStorage.getProjects();
  }

}
