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

import io.swagger.annotations.*;
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

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * <p>TeamManagementREST class.</p>
 *
 * @author medamine
 * @version $Id: $Id
 */
@Path("timetracker/teamsmgn")
@RolesAllowed("users")
@Api(value = "/timetracker", description = "Manage and access Teams") // NOSONAR
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
  @ApiOperation(value = "Retrieves all available subresources of current endpoint", httpMethod = "GET", response = Response.class, produces = "application/json")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.OK, message = "Request fulfilled"),
          @ApiResponse(code = 500, message = "Internal server error") })
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
  @ApiOperation(value = "Retrieves all available subresources of current endpoint", httpMethod = "GET", response = Response.class, produces = "application/json")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.OK, message = "Request fulfilled"),
          @ApiResponse(code = 500, message = "Internal server error") })
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
  @RolesAllowed("administrators")
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Creates a new Team", httpMethod = "POST", response = Response.class, notes = "empty response")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
          @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
          @ApiResponse(code = 500, message = "Internal server error") })
  public Response createTeam(@ApiParam(value = "Team to save", required = true) Team team) {
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
  @RolesAllowed("administrators")
  @ApiOperation(value = "Updates an existing Team identified by its id", httpMethod = "PUT", response = Response.class, notes = "empty response")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
          @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
          @ApiResponse(code = 500, message = "Internal server error") })
  public Response updateTeam(@ApiParam(value = "Team to update", required = true) Team team) {
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
  @RolesAllowed("administrators")
  @ApiOperation(value = "Deletes an existing Team identified by its id", httpMethod = "DELETE", response = Response.class, notes = "empty response")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
          @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
          @ApiResponse(code = 500, message = "Internal server error") })
  public Response deleteTeam(@ApiParam(value = "Team technical id to delete", required = true) @QueryParam("teamId") String teamId) {
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
  @ApiOperation(value = "Retrieves all available subresources of current endpoint", httpMethod = "GET", response = Response.class, produces = "application/json")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.OK, message = "Request fulfilled"),
          @ApiResponse(code = 500, message = "Internal server error") })
  public Response getTeamMembers(@ApiParam(value = "Team technical id", required = true) @QueryParam("teamId") String teamId) {
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
  @RolesAllowed("administrators")
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Creates a new TeamMember", httpMethod = "POST", response = Response.class, notes = "empty response")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
          @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
          @ApiResponse(code = 500, message = "Internal server error") })
  public Response createTeamMember(@ApiParam(value = "TeamMember to save", required = true) TeamMember teamMember) {
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
  @RolesAllowed("administrators")
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Creates a new TeamMember", httpMethod = "POST", response = Response.class, notes = "empty response")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
          @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
          @ApiResponse(code = 500, message = "Internal server error") })
  public Response createAllTeamMember(@ApiParam(value = "TeamMember to save", required = true) List<TeamMember> teamMembers) {
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
  @RolesAllowed("administrators")
  @ApiOperation(value = "Deletes an existing TeamMember identified by its id", httpMethod = "DELETE", response = Response.class, notes = "empty response")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
          @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
          @ApiResponse(code = 500, message = "Internal server error") })
  public Response deleteTeamMember(@ApiParam(value = "TeamMember technical id to delete", required = true) @QueryParam("teamMemberId") String teamMemberId) {
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


  private String getCurrentUserName() {
    ConversationState state = ConversationState.getCurrent();
    return state == null || state.getIdentity() == null ? null : state.getIdentity().getUserId();
  }

}
