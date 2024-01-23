/*
 * This file is part of the Meeds team (https://meeds.io/).
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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
import org.exoplatform.timetracker.dto.Team;
import org.exoplatform.timetracker.dto.TeamMember;
import org.exoplatform.timetracker.service.TeamService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

/**
 * <p>TeamManagementREST class.</p>
 *
 * @author medamine
 * @version $Id: $Id
 */
@Path("timetracker/teamsmgn")
@RolesAllowed("users")
@Tag(name = "/timetracker", description = "Manage and access Teams") // NOSONAR
public class TeamManagementREST implements ResourceContainer {

  private static final Log      LOG                 = ExoLogger.getLogger(TeamManagementREST.class);

  private final String          portalContainerName = "portal";

  private final TeamService teamService;

  /**
   * <p>Constructor for TeamManagementREST.</p>
   *
   * @param teamService a {@link org.exoplatform.timetracker.service.TeamService} object.
   * @param container a {@link org.exoplatform.container.PortalContainer} object.
   */
  public TeamManagementREST(TeamService teamService, PortalContainer container) {
    this.teamService = teamService;
  }

  /**
   * <p>getTeams.</p>
   *
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @GET
  @Path("team")
  @RolesAllowed("users")
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Retrieves all available subresources of current endpoint", method = "GET")
  @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Request fulfilled"),
          @ApiResponse(responseCode = "500", description = "Internal server error") })
  public Response getTeams() {
    try {
      Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
      if (sourceIdentity == null) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
      }
      return Response.ok(teamService.getTeamsList(getCurrentUserName())).build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while getting Teams", e);
      return Response.serverError().build();
    }
  }

  /**
   * <p>getAll.</p>
   *
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @GET
  @Path("team/all")
  @RolesAllowed("users")
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Retrieves all available subresources of current endpoint", method = "GET")
  @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Request fulfilled"),
          @ApiResponse(responseCode = "500", description = "Internal server error") })
  public Response getAll() {
    try {
      Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
      if (sourceIdentity == null) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
      }
      return Response.ok(teamService.getTeams()).build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while getting Teams", e);
      return Response.serverError().build();
    }
  }

  /**
   * <p>createTeam.</p>
   *
   * @param team a {@link org.exoplatform.timetracker.dto.Team} object.
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @POST
  @Path("team")
  @RolesAllowed("time-tracking-managers")
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(summary = "Creates a new Team", method = "POST", description = "empty response")
  @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Request fulfilled"),
          @ApiResponse(responseCode = "401", description = "Unauthorized operation"),
          @ApiResponse(responseCode = "500", description = "Internal server error") })
  public Response createTeam(@Parameter(description = "Team to save", required = true) Team team) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      teamService.createTeam(team);
    } catch (EntityExistsException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while creating Team", e);
      return Response.serverError().build();
    }
    LOG.info("service=time-tracker operation=add-team parameters=\"user_social_id:{}\"", sourceIdentity.getId());
    return Response.noContent().build();
  }

  /**
   * <p>updateTeam.</p>
   *
   * @param team a {@link org.exoplatform.timetracker.dto.Team} object.
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @PUT
  @Path("team")
  @RolesAllowed("time-tracking-managers")
  @Operation(summary = "Updates an existing Team identified by its id", method = "PUT", description = "empty response")
  @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Request fulfilled"),
          @ApiResponse(responseCode = "401", description = "Unauthorized operation"),
          @ApiResponse(responseCode = "500", description = "Internal server error") })
  public Response updateTeam(@Parameter(description = "Team to update", required = true) Team team) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      teamService.updateTeam(team);
    } catch (IllegalAccessException e) {
      LOG.warn(e);
      return Response.status(HTTPStatus.UNAUTHORIZED).build();
    } catch (EntityExistsException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while updating Team", e);
      return Response.serverError().build();
    }
    LOG.info("service=time-tracker operation=update-team parameters=\"user_social_id:{}\"", sourceIdentity.getId());
    return Response.noContent().build();
  }

  /**
   * <p>deleteTeam.</p>
   *
   * @param teamId a {@link java.lang.String} object.
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @DELETE
  @Path("team")
  @RolesAllowed("time-tracking-managers")
  @Operation(summary = "Deletes an existing Team identified by its id", method = "DELETE", description = "empty response")
  @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Request fulfilled"),
          @ApiResponse(responseCode = "401", description = "Unauthorized operation"),
          @ApiResponse(responseCode = "500", description = "Internal server error") })
  public Response deleteTeam(@Parameter(description = "Team technical id to delete", required = true) @QueryParam("teamId") String teamId) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      teamService.deleteTeam(teamId);
    } catch (IllegalAccessException e) {
      LOG.warn(e);
      return Response.status(HTTPStatus.UNAUTHORIZED).build();
    } catch (EntityNotFoundException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while deleting Team", e);
      return Response.serverError().build();
    }
    LOG.info("service=time-tracker operation=delete-team parameters=\"user_social_id:{}\"", sourceIdentity.getId());
    return Response.noContent().build();
  }


  /**
   * <p>getTeamMembers.</p>
   *
   * @param teamId a {@link java.lang.String} object.
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @GET
  @Path("teamMember")
  @RolesAllowed("users")
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Retrieves all available subresources of current endpoint", method = "GET")
  @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Request fulfilled"),
          @ApiResponse(responseCode = "500", description = "Internal server error") })
  public Response getTeamMembers(@Parameter(description = "Team technical id", required = true) @QueryParam("teamId") String teamId) {
    try {
      Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
      if (sourceIdentity == null) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
      }
      return Response.ok(teamService.getMembersList(teamId)).build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while getting TeamMembers", e);
      return Response.serverError().build();
    }
  }

  /**
   * <p>createTeamMember.</p>
   *
   * @param teamMember a {@link org.exoplatform.timetracker.dto.TeamMember} object.
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @POST
  @Path("teamMember")
  @RolesAllowed("time-tracking-managers")
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(summary = "Creates a new TeamMember", method = "POST", description = "empty response")
  @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Request fulfilled"),
          @ApiResponse(responseCode = "401", description = "Unauthorized operation"),
          @ApiResponse(responseCode = "500", description = "Internal server error") })
  public Response createTeamMember(@Parameter(description = "TeamMember to save", required = true) TeamMember teamMember) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      teamService.createTeamMember(teamMember);
    } catch (EntityExistsException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while creating TeamMember", e);
      return Response.serverError().build();
    }
    LOG.info("service=time-tracker operation=add-team-member parameters=\"user_social_id:{}\"", sourceIdentity.getId());
    return Response.noContent().build();
  }

  /**
   * <p>createAllTeamMember.</p>
   *
   * @param teamMembers a {@link java.util.List} object.
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @POST
  @Path("teamMember/all")
  @RolesAllowed("time-tracking-managers")
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(summary = "Creates a new TeamMember", method = "POST", description = "empty response")
  @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Request fulfilled"),
          @ApiResponse(responseCode = "401", description = "Unauthorized operation"),
          @ApiResponse(responseCode = "500", description = "Internal server error") })
  public Response createAllTeamMember(@Parameter(description = "TeamMember to save", required = true) List<TeamMember> teamMembers) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      for(TeamMember teamMember : teamMembers) {
        teamService.createTeamMember(teamMember);
        LOG.info("service=time-tracker operation=add-team-member parameters=\"user_social_id:{}\"", sourceIdentity.getId());
      }
    } catch (EntityExistsException e) {
      LOG.warn(e);
    } catch (Exception e) {
      LOG.error("Unknown error occurred while creating TeamMember", e);
      return Response.serverError().build();
    }
    return Response.noContent().build();
  }

  /**
   * <p>deleteTeamMember.</p>
   *
   * @param teamMemberId a {@link java.lang.String} object.
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @DELETE
  @Path("teamMember")
  @RolesAllowed("time-tracking-managers")
  @Operation(summary = "Deletes an existing TeamMember identified by its id", method = "DELETE", description = "empty response")
  @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Request fulfilled"),
          @ApiResponse(responseCode = "401", description = "Unauthorized operation"),
          @ApiResponse(responseCode = "500", description = "Internal server error") })
  public Response deleteTeamMember(@Parameter(description = "TeamMember technical id to delete", required = true) @QueryParam("teamMemberId") String teamMemberId) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      teamService.deleteTeamMember(teamMemberId);
    } catch (IllegalAccessException e) {
      LOG.warn(e);
      return Response.status(HTTPStatus.UNAUTHORIZED).build();
    } catch (EntityNotFoundException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while deleting TeamMember", e);
      return Response.serverError().build();
    }
    LOG.info("service=time-tracker operation=delete-team-member parameters=\"user_social_id:{}\"", sourceIdentity.getId());
    return Response.noContent().build();
  }


  /**
   * <p>getTeamMembers.</p>
   *
   * @param teamId a {@link java.lang.String} object.
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @GET
  @Path("employees")
  @RolesAllowed("users")
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Retrieves all employees related to current user", method = "GET")
  @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Request fulfilled"),
          @ApiResponse(responseCode = "500", description = "Internal server error") })
  public Response getEmployees(@Parameter(description = "Team technical id", required = true) @QueryParam("teamId") String teamId) {
    try {
      Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
      if (sourceIdentity == null) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
      }
      List<TeamMember> employees = new ArrayList<>();
      if(ConversationState.getCurrent().getIdentity().isMemberOf("/platform/time-tracking-managers")){
        employees = teamService.getEmployeesList(sourceIdentity.getRemoteId());
      }
      return Response.ok(employees).build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while getting TeamMembers", e);
      return Response.serverError().build();
    }
  }


  private String getCurrentUserName() {
    ConversationState state = ConversationState.getCurrent();
    return state == null || state.getIdentity() == null ? null : state.getIdentity().getUserId();
  }

}
