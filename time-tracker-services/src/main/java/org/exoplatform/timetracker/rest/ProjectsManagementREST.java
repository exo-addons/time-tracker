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
package org.exoplatform.timetracker.rest;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.exoplatform.common.http.HTTPStatus;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.rest.resource.ResourceContainer;
import org.exoplatform.services.security.ConversationState;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.service.rest.Util;
import org.exoplatform.timetracker.dto.Project;
import org.exoplatform.timetracker.service.ProjectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * <p>ProjectsManagementREST class.</p>
 *
 * @author medamine
 * @version $Id: $Id
 */
@Path("timetracker/projectsmgn")
@RolesAllowed("users")
@Tag(name = "/timetracker", description = "Manage and access Projects") // NOSONAR
public class ProjectsManagementREST implements ResourceContainer {

  private static final Log      LOG                 = ExoLogger.getLogger(ProjectsManagementREST.class);

  private final String          portalContainerName = "portal";

  private final ProjectService projectService;

  /**
   * <p>Constructor for ProjectsManagementREST.</p>
   *
   * @param projectService a {@link org.exoplatform.timetracker.service.ProjectService} object.
   * @param container a {@link org.exoplatform.container.PortalContainer} object.
   */
  public ProjectsManagementREST(ProjectService projectService, PortalContainer container) {
    this.projectService = projectService;
  }

  /**
   * <p>getProjects.</p>
   *
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @GET
  @Path("project")
  @RolesAllowed("users")
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Retrieves all available subresources of current endpoint", method = "GET")
  @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Request fulfilled"),
      @ApiResponse(responseCode = "500", description = "Internal server error") })
  public Response getProjects() {
    try {
      Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
      if (sourceIdentity == null) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
      }
      return Response.ok(projectService.getProjectsList()).build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while getting Projects", e);
      return Response.serverError().build();
    }
  }

  /**
   * <p>createProject.</p>
   *
   * @param project a {@link org.exoplatform.timetracker.dto.Project} object.
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @POST
  @Path("project")
  @RolesAllowed("time-tracking-managers")
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(summary = "Creates a new Project", method = "POST", description = "empty response")
  @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Request fulfilled"),
      @ApiResponse(responseCode = "401", description = "Unauthorized operation"),
      @ApiResponse(responseCode = "500", description = "Internal server error") })
  public Response createProject(@Parameter(description = "Project to save", required = true) Project project) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      projectService.createProject(project);
    } catch (EntityExistsException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while creating Project", e);
      return Response.serverError().build();
    }
    LOG.info("service=time-tracker operation=add-project parameters=\"user_social_id:{}\"", sourceIdentity.getId());
    return Response.noContent().build();
  }

  /**
   * <p>updateProject.</p>
   *
   * @param project a {@link org.exoplatform.timetracker.dto.Project} object.
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @PUT
  @Path("project")
  @RolesAllowed("time-tracking-managers")
  @Operation(summary = "Updates an existing Project identified by its id", method = "PUT", description = "empty response")
  @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Request fulfilled"),
      @ApiResponse(responseCode = "401", description = "Unauthorized operation"),
      @ApiResponse(responseCode = "500", description = "Internal server error") })
  public Response updateProject(@Parameter(description = "Project to update", required = true) Project project) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      projectService.updateProject(project, getCurrentUserName());
    } catch (IllegalAccessException e) {
      LOG.warn(e);
      return Response.status(HTTPStatus.UNAUTHORIZED).build();
    } catch (EntityExistsException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while updating Project", e);
      return Response.serverError().build();
    }
    LOG.info("service=time-tracker operation=update-project parameters=\"user_social_id:{}\"", sourceIdentity.getId());
    return Response.noContent().build();
  }

  /**
   * <p>deleteProject.</p>
   *
   * @param projectId a {@link java.lang.Long} object.
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @DELETE
  @Path("project/{projectId}")
  @RolesAllowed("time-tracking-managers")
  @Operation(summary = "Deletes an existing Project identified by its id", method = "DELETE", description = "empty response")
  @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Request fulfilled"),
      @ApiResponse(responseCode = "401", description = "Unauthorized operation"),
      @ApiResponse(responseCode = "500", description = "Internal server error") })
  public Response deleteProject(@Parameter(description = "Project technical id to delete", required = true) @PathParam("projectId") Long projectId) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      projectService.deleteProject(projectId, getCurrentUserName());
    } catch (IllegalAccessException e) {
      LOG.warn(e);
      return Response.status(HTTPStatus.UNAUTHORIZED).build();
    } catch (EntityNotFoundException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while deleting Project", e);
      return Response.serverError().build();
    }
    LOG.info("service=time-tracker operation=delete-project parameters=\"user_social_id:{}\"", sourceIdentity.getId());
    return Response.noContent().build();
  }

  private String getCurrentUserName() {
    ConversationState state = ConversationState.getCurrent();
    return state == null || state.getIdentity() == null ? null : state.getIdentity().getUserId();
  }

}
