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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.common.http.HTTPStatus;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.rest.resource.ResourceContainer;
import org.exoplatform.services.security.ConversationState;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.service.rest.Util;
import org.exoplatform.timetracker.dto.ActivityRecord;
import org.exoplatform.timetracker.service.ActivityRecordService;

import io.swagger.annotations.*;

/**
 * <p>ActivityRecordsManagementREST class.</p>
 *
 * @author medamine
 * @version $Id: $Id
 */
@Path("timetracker/activityRecordrecordsmgn")
@RolesAllowed("users")
@Api(value = "/timetracker", description = "Manage and access ActivityRecord center ActivityRecords") // NOSONAR
public class ActivityRecordsManagementREST implements ResourceContainer {

  private static final Log      LOG                 = ExoLogger.getLogger(ActivityRecordsManagementREST.class);

  private final String          portalContainerName = "portal";

  private final ActivityRecordService activityRecordService;

  /**
   * <p>Constructor for ActivityRecordsManagementREST.</p>
   *
   * @param activityRecordService a {@link org.exoplatform.timetracker.service.ActivityRecordService} object.
   * @param container a {@link org.exoplatform.container.PortalContainer} object.
   */
  public ActivityRecordsManagementREST(ActivityRecordService activityRecordService, PortalContainer container) {
    this.activityRecordService = activityRecordService;
  }

  /**
   * <p>getActivityRecords.</p>
   *
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @GET
  @Path("activityrecord")
  @RolesAllowed("users")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Retrieves all available subresources of current endpoint", httpMethod = "GET", response = Response.class, produces = "application/json")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.OK, message = "Request fulfilled"),
      @ApiResponse(code = 500, message = "Internal server error") })
  public Response getActivityRecords() {
    try {
      Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
      if (sourceIdentity == null) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
      }
      return Response.ok(activityRecordService.getActivityRecords()).build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while getting ActivityRecords", e);
      return Response.serverError().build();
    }
  }
  /**
   * <p>getActivityRecordsList.</p>
   *
   * @param uriInfo a {@link javax.ws.rs.core.UriInfo} object.
   * @param search a {@link java.lang.String} object.
   * @param activity a long.
   * @param type a long.
   * @param subType a long.
   * @param activityCode a long.
   * @param subActivityCode a long.
   * @param client a long.
   * @param project a long.
   * @param feature a long.
   * @param fromDate a {@link java.lang.String} object.
   * @param toDate a {@link java.lang.String} object.
   * @param userName a {@link java.lang.String} object.
   * @param location a {@link java.lang.String} object.
   * @param office a {@link java.lang.String} object.
   * @param sortBy a {@link java.lang.String} object.
   * @param sortDesc a {@link java.lang.Boolean} object.
   * @param page a int.
   * @param limit a int.
   * @param export a {@link java.lang.Boolean} object.
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @GET
  @Path("activityrecord/list")
  @RolesAllowed("users")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Retrieves all available subresources of current endpoint", httpMethod = "GET", response = Response.class, produces = "application/json")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.OK, message = "Request fulfilled"),
      @ApiResponse(code = 500, message = "Internal server error") })
  public Response getActivityRecordsList(@Context UriInfo uriInfo,
                                         @QueryParam("search") String search,
                                         @QueryParam("activity") long activity,
                                         @QueryParam("type") long type,
                                         @QueryParam("subType") long subType,
                                         @QueryParam("activityCode") long activityCode,
                                         @QueryParam("subActivityCode") long subActivityCode,
                                         @QueryParam("client") long client,
                                         @QueryParam("project") long project,
                                         @QueryParam("feature") long feature,
                                         @QueryParam("fromDate") String fromDate,
                                         @QueryParam("toDate") String toDate,
                                         @QueryParam("userName") String userName,
                                         @QueryParam("location") String location,
                                         @QueryParam("office") String office,
                                         @QueryParam("sortby") String sortBy,
                                         @QueryParam("sortdesc") Boolean sortDesc,
                                         @QueryParam("page") int page,
                                         @QueryParam("limit") int limit,
                                         @QueryParam("export") Boolean export) {
    try {
      Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
      if (sourceIdentity == null) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
      }
      if (StringUtils.isEmpty(userName)){
        userName=sourceIdentity.getRemoteId();
      }
      return Response.ok(activityRecordService.getActivityRecordsList(search, activity, type, subType, activityCode, subActivityCode, client, project, feature, fromDate, toDate, userName, location, office, page, limit, sortBy, sortDesc)).build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while getting ActivityRecords", e);
      return Response.serverError().build();
    }
  }

  /**
   * <p>getActivityRecords.</p>
   *
   * @param day a {@link java.lang.String} object.
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @GET
  @Path("activityrecord/{day}")
  @RolesAllowed("users")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Retrieves all available subresources of current endpoint", httpMethod = "GET", response = Response.class, produces = "application/json")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.OK, message = "Request fulfilled"),
      @ApiResponse(code = 500, message = "Internal server error") })
  public Response getActivityRecords(@PathParam("day") String day) {
    try {
      Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
      if (sourceIdentity == null) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
      }
      return Response.ok(activityRecordService.getUserActivityRecordsList(day,sourceIdentity.getRemoteId())).build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while getting ActivityRecords", e);
      return Response.serverError().build();
    }
  }

  /**
   * <p>createActivityRecord.</p>
   *
   * @param activityRecord a {@link org.exoplatform.timetracker.dto.ActivityRecord} object.
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @POST
  @Path("activityrecord")
  @RolesAllowed("users")
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Creates a new ActivityRecord", httpMethod = "POST", response = Response.class, notes = "empty response")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
      @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
      @ApiResponse(code = 500, message = "Internal server error") })
  public Response createActivityRecord(@ApiParam(value = "ActivityRecord to save", required = true) ActivityRecord activityRecord) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      if(activityRecord.getActivity().getId()==null){
        activityRecord.setActivity(null);
      }
      activityRecord.setUserName(sourceIdentity.getRemoteId());
      activityRecordService.createActivityRecord(activityRecord);
    } catch (EntityExistsException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while creating ActivityRecord", e);
      return Response.serverError().build();
    }
    LOG.info("service=time-tracker operation=add-record parameters=\"user_social_id:{}\"", sourceIdentity.getId());
    return Response.noContent().build();
  }

  /**
   * <p>updateActivityRecord.</p>
   *
   * @param activityRecord a {@link org.exoplatform.timetracker.dto.ActivityRecord} object.
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @PUT
  @Path("activityrecord")
  @RolesAllowed("users")
  @ApiOperation(value = "Updates an existing ActivityRecord identified by its id", httpMethod = "PUT", response = Response.class, notes = "empty response")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
      @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
      @ApiResponse(code = 500, message = "Internal server error") })
  public Response updateActivityRecord(@ApiParam(value = "ActivityRecord to update", required = true) ActivityRecord activityRecord) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      activityRecordService.updateActivityRecord(activityRecord, getCurrentUserName());
    } catch (IllegalAccessException e) {
      LOG.warn(e);
      return Response.status(HTTPStatus.UNAUTHORIZED).build();
    } catch (EntityExistsException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while updating ActivityRecord", e);
      return Response.serverError().build();
    }
    LOG.info("service=time-tracker operation=update-record parameters=\"user_social_id:{}\"", sourceIdentity.getId());
    return Response.noContent().build();
  }

  /**
   * <p>deleteActivityRecord.</p>
   *
   * @param activityRecordId a {@link java.lang.Long} object.
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @DELETE
  @Path("activityrecord/{activityrecordId}")
  @RolesAllowed("users")
  @ApiOperation(value = "Deletes an existing ActivityRecord identified by its id", httpMethod = "DELETE", response = Response.class, notes = "empty response")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
      @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
      @ApiResponse(code = 500, message = "Internal server error") })
  public Response deleteActivityRecord(@ApiParam(value = "ActivityRecord technical id to delete", required = true) @PathParam("activityrecordId") Long activityRecordId) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      activityRecordService.deleteActivityRecord(activityRecordId, getCurrentUserName());
    } catch (IllegalAccessException e) {
      LOG.warn(e);
      return Response.status(HTTPStatus.UNAUTHORIZED).build();
    } catch (EntityNotFoundException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while deleting ActivityRecord", e);
      return Response.serverError().build();
    }
    LOG.info("service=time-tracker operation=remove-record parameters=\"user_social_id:{}\"", sourceIdentity.getId());

    return Response.noContent().build();
  }

  private String getCurrentUserName() {
    ConversationState state = ConversationState.getCurrent();
    return state == null || state.getIdentity() == null ? null : state.getIdentity().getUserId();
  }

}
