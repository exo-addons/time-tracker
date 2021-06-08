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

import io.swagger.annotations.*;
import org.exoplatform.common.http.HTTPStatus;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.rest.resource.ResourceContainer;
import org.exoplatform.services.security.ConversationState;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.service.rest.Util;
import org.exoplatform.timetracker.dto.Activity;
import org.exoplatform.timetracker.dto.Team;
import org.exoplatform.timetracker.dto.TeamMember;
import org.exoplatform.timetracker.service.ActivityService;
import org.exoplatform.timetracker.service.TeamService;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>ActivitiesManagementREST class.</p>
 *
 * @author medamine
 * @version $Id: $Id
 */
@Path("timetracker/activitymgn")
@RolesAllowed("users")
@Api(value = "/timetracker", description = "Manage and access Activity center Activitys") // NOSONAR
public class ActivitiesManagementREST implements ResourceContainer {

    private static final Log LOG = ExoLogger.getLogger(ActivitiesManagementREST.class);

    private final String portalContainerName = "portal";

    private final ActivityService activityService;
    private final TeamService teamService;

    /**
     * <p>Constructor for ActivitiesManagementREST.</p>
     *
     * @param activityService a {@link org.exoplatform.timetracker.service.ActivityService} object.
     * @param teamService a {@link org.exoplatform.timetracker.service.TeamService} object.
     * @param container a {@link org.exoplatform.container.PortalContainer} object.
     */
    public ActivitiesManagementREST(ActivityService activityService, TeamService teamService, PortalContainer container) {
        this.activityService = activityService;
        this.teamService = teamService;
    }

    /**
     * <p>getAllActivities.</p>
     *
     * @return a {@link javax.ws.rs.core.Response} object.
     */
    @GET
    @Path("activity/all")
    @RolesAllowed("users")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Retrieves all available subresources of current endpoint", httpMethod = "GET", response = Response.class, produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = HTTPStatus.OK, message = "Request fulfilled"),
            @ApiResponse(code = 500, message = "Internal server error")})
    public Response getAllActivities() {
        try {
            Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
            if (sourceIdentity == null) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            return Response.ok(activityService.getActivitiesList()).build();
        } catch (Exception e) {
            LOG.error("Unknown error occurred while getting Activities", e);
            return Response.serverError().build();
        }
    }

    /**
     * <p>getActivities.</p>
     *
     * @return a {@link javax.ws.rs.core.Response} object.
     */
    @GET
    @Path("activity")
    @RolesAllowed("users")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Retrieves all available subresources of current endpoint", httpMethod = "GET", response = Response.class, produces = "application/json")
    @ApiResponses(value = {@ApiResponse(code = HTTPStatus.OK, message = "Request fulfilled"),
            @ApiResponse(code = 500, message = "Internal server error")})
    public Response getActivities() {
        try {
            Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
            if (sourceIdentity == null) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            List<Team> teams = teamService.getTeamsList(getCurrentUserName());
            //List<Team> teams = teamsMmebers.stream().map(TeamMember::getTeam).collect(Collectors.toList());
            List<Activity> activities = activityService.getActivitiesforUser(teams.stream().map(Team::getId).collect(Collectors.toList()));
            return Response.ok(activities).build();
        } catch (Exception e) {
            LOG.error("Unknown error occurred while getting Activities", e);
            return Response.serverError().build();
        }
    }

    /**
     * <p>createActivity.</p>
     *
     * @param activity a {@link org.exoplatform.timetracker.dto.Activity} object.
     * @return a {@link javax.ws.rs.core.Response} object.
     */
    @POST
    @Path("activity")
    @RolesAllowed("time-tracking-managers")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Creates a new Activity", httpMethod = "POST", response = Response.class, notes = "empty response")
    @ApiResponses(value = {@ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
            @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
            @ApiResponse(code = 500, message = "Internal server error")})
    public Response createActivity(@ApiParam(value = "Activity to save", required = true) Activity activity) {
        Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
        if (sourceIdentity == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        try {
            activityService.createActivity(activity);
        } catch (EntityExistsException e) {
            LOG.warn(e);
            return Response.serverError().build();
        } catch (Exception e) {
            LOG.error("Unknown error occurred while creating Activity", e);
            return Response.serverError().build();
        }
        LOG.info("service=time-tracker operation=add-activity parameters=\"user_social_id:{}\"", sourceIdentity.getId());
        return Response.noContent().build();
    }

    /**
     * <p>updateActivity.</p>
     *
     * @param activity a {@link org.exoplatform.timetracker.dto.Activity} object.
     * @return a {@link javax.ws.rs.core.Response} object.
     */
    @PUT
    @Path("activity")
    @RolesAllowed("time-tracking-managers")
    @ApiOperation(value = "Updates an existing Activity identified by its id", httpMethod = "PUT", response = Response.class, notes = "empty response")
    @ApiResponses(value = {@ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
            @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
            @ApiResponse(code = 500, message = "Internal server error")})
    public Response updateActivity(@ApiParam(value = "Activity to update", required = true) Activity activity) {
        Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
        if (sourceIdentity == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        try {
            activityService.updateActivity(activity, getCurrentUserName());
        } catch (IllegalAccessException e) {
            LOG.warn(e);
            return Response.status(HTTPStatus.UNAUTHORIZED).build();
        } catch (EntityExistsException e) {
            LOG.warn(e);
            return Response.serverError().build();
        } catch (Exception e) {
            LOG.error("Unknown error occurred while updating Activity", e);
            return Response.serverError().build();
        }
        LOG.info("service=time-tracker operation=update-activity parameters=\"user_social_id:{}\"", sourceIdentity.getId());
        return Response.noContent().build();
    }

    /**
     * <p>deleteActivity.</p>
     *
     * @param activityId a {@link java.lang.Long} object.
     * @return a {@link javax.ws.rs.core.Response} object.
     */
    @DELETE
    @Path("activity/{activityId}")
    @RolesAllowed("time-tracking-managers")
    @ApiOperation(value = "Deletes an existing Activity identified by its id", httpMethod = "DELETE", response = Response.class, notes = "empty response")
    @ApiResponses(value = {@ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
            @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
            @ApiResponse(code = 500, message = "Internal server error")})
    public Response deleteActivity(@ApiParam(value = "Activity technical id to delete", required = true) @PathParam("activityId") Long activityId) {
        Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
        if (sourceIdentity == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        try {
            activityService.deleteActivity(activityId, getCurrentUserName());
        } catch (IllegalAccessException e) {
            LOG.warn(e);
            return Response.status(HTTPStatus.UNAUTHORIZED).build();
        } catch (EntityNotFoundException e) {
            LOG.warn(e);
            return Response.serverError().build();
        } catch (Exception e) {
            LOG.error("Unknown error occurred while deleting Activity", e);
            return Response.serverError().build();
        }
        LOG.info("service=time-tracker operation=remove-activity parameters=\"user_social_id:{}\"", sourceIdentity.getId());
        return Response.noContent().build();
    }

    private String getCurrentUserName() {
        ConversationState state = ConversationState.getCurrent();
        return state == null || state.getIdentity() == null ? null : state.getIdentity().getUserId();
    }

}
