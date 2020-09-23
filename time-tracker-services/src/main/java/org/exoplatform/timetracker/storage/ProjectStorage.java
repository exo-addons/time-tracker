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

import org.exoplatform.timetracker.dao.ProjectDAO;
import org.exoplatform.timetracker.dto.Project;
import org.exoplatform.timetracker.entity.ProjectEntity;

/**
 * Storage service to access / load and save Projects. This service will be
 * used , as well, to convert from JPA entity to DTO.
 *
 * @author medamine
 * @version $Id: $Id
 */
public class ProjectStorage {

  private final ProjectDAO projectDAO;
  private final ClientStorage clientStorage;

  /**
   * <p>Constructor for ProjectStorage.</p>
   *
   * @param projectDAO a {@link org.exoplatform.timetracker.dao.ProjectDAO} object.
   * @param clientStorage a {@link org.exoplatform.timetracker.storage.ClientStorage} object.
   */
  public ProjectStorage(ProjectDAO projectDAO, ClientStorage clientStorage) {
    this.projectDAO = projectDAO;
    this.clientStorage = clientStorage;
  }

  /**
   * <p>createProject.</p>
   *
   * @param project a {@link org.exoplatform.timetracker.dto.Project} object.
   * @return a {@link org.exoplatform.timetracker.dto.Project} object.
   * @throws java.lang.Exception if any.
   */
  public Project createProject(Project project) throws Exception {
    if (project == null) {
      throw new IllegalArgumentException("Project is mandatory");
    }
    ProjectEntity projectEntity = toEntity(project);
    project.setId(null);
    projectEntity = projectDAO.create(projectEntity);
    return toDTO(projectEntity);
  }

  /**
   * <p>updateProject.</p>
   *
   * @param project a {@link org.exoplatform.timetracker.dto.Project} object.
   * @return a {@link org.exoplatform.timetracker.dto.Project} object.
   * @throws java.lang.Exception if any.
   */
  public Project updateProject(Project project) throws Exception {
    if (project == null) {
      throw new IllegalArgumentException("Project is mandatory");
    }
    Long projectId = project.getId();
    ProjectEntity projectEntity = projectDAO.find(project.getId());
    if (projectEntity == null) {
      throw new EntityNotFoundException("Project with id " + projectId + " wasn't found");
    }

    projectEntity = toEntity(project);
    projectEntity = projectDAO.update(projectEntity);

    return toDTO(projectEntity);
  }

  /**
   * <p>deleteProject.</p>
   *
   * @param projectId a long.
   * @throws org.gatein.api.EntityNotFoundException if any.
   */
  public void deleteProject(long projectId) throws EntityNotFoundException {
    if (projectId <= 0) {
      throw new IllegalArgumentException("ProjectId must be a positive integer");
    }
    ProjectEntity projectEntity = projectDAO.find(projectId);
    if (projectEntity == null) {
      throw new EntityNotFoundException("Project with id " + projectId + " not found");
    }
    projectDAO.delete(projectEntity);
  }

  /**
   * <p>getProjectById.</p>
   *
   * @param ProjectId a long.
   * @return a {@link org.exoplatform.timetracker.dto.Project} object.
   */
  public Project getProjectById(long ProjectId) {
    if (ProjectId <= 0) {
      throw new IllegalArgumentException("ProjectId must be a positive integer");
    }
    ProjectEntity ProjectEntity = projectDAO.find(ProjectId);
    return toDTO(ProjectEntity);
  }

  /**
   * <p>getProjects.</p>
   *
   * @return a {@link java.util.List} object.
   */
  public List<Project> getProjects() {
    List<ProjectEntity> applicatiions = projectDAO.findAll();
    return applicatiions.stream().map(this::toDTO).collect(Collectors.toList());
  }

  /**
   * <p>countProjects.</p>
   *
   * @return a long.
   */
  public long countProjects() {
    return projectDAO.count();
  }

  /**
   * <p>toDTO.</p>
   *
   * @param projectEntity a {@link org.exoplatform.timetracker.entity.ProjectEntity} object.
   * @return a {@link org.exoplatform.timetracker.dto.Project} object.
   */
  public Project toDTO(ProjectEntity projectEntity) {
    if (projectEntity == null) {
      return null;
    }
    return new Project(projectEntity.getId(),
                        projectEntity.getCode(),
                        projectEntity.getLabel(),
                        clientStorage.toDTO(projectEntity.getClientEntity()));
  }

  /**
   * <p>toEntity.</p>
   *
   * @param project a {@link org.exoplatform.timetracker.dto.Project} object.
   * @return a {@link org.exoplatform.timetracker.entity.ProjectEntity} object.
   */
  public ProjectEntity toEntity(Project project) {
    if (project == null) {
      return null;
    }
    return new ProjectEntity(project.getId(),
                              project.getCode(),
                              project.getLabel(),
                              clientStorage.toEntity(project.getClient()));
  }

}
