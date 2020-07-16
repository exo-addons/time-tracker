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
 */
public class ProjectStorage {

  private final ProjectDAO projectDAO;
  private final ClientStorage clientStorage;

  public ProjectStorage(ProjectDAO projectDAO, ClientStorage clientStorage) {
    this.projectDAO = projectDAO;
    this.clientStorage = clientStorage;
  }

  public Project createProject(Project project) throws Exception {
    if (project == null) {
      throw new IllegalArgumentException("Project is mandatory");
    }
    ProjectEntity projectEntity = toEntity(project);
    project.setId(null);
    projectEntity = projectDAO.create(projectEntity);
    return toDTO(projectEntity);
  }

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

  public Project getProjectById(long ProjectId) {
    if (ProjectId <= 0) {
      throw new IllegalArgumentException("ProjectId must be a positive integer");
    }
    ProjectEntity ProjectEntity = projectDAO.find(ProjectId);
    return toDTO(ProjectEntity);
  }

  public List<Project> getProjects() {
    List<ProjectEntity> applicatiions = projectDAO.findAll();
    return applicatiions.stream().map(this::toDTO).collect(Collectors.toList());
  }

  public long countProjects() {
    return projectDAO.count();
  }

  public Project toDTO(ProjectEntity projectEntity) {
    if (projectEntity == null) {
      return null;
    }
    return new Project(projectEntity.getId(),
                        projectEntity.getCode(),
                        projectEntity.getLabel(),
                        clientStorage.toDTO(projectEntity.getClientEntity()));
  }

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
