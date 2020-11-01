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
import org.exoplatform.timetracker.dto.*;
import org.exoplatform.timetracker.service.CodesService;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * <p>CodesManagementREST class.</p>
 *
 * @author medamine
 * @version $Id: $Id
 */
@Path("timetracker/codesmgn")
@RolesAllowed("users")
@Api(value = "/timetracker", description = "Manage and access Codes") // NOSONAR
public class CodesManagementREST implements ResourceContainer {

  private static final Log      LOG                 = ExoLogger.getLogger(CodesManagementREST.class);

  private final String          portalContainerName = "portal";

  private final CodesService codesService;

  /**
   * <p>Constructor for CodesManagementREST.</p>
   *
   * @param codesService a {@link org.exoplatform.timetracker.service.CodesService} object.
   * @param container a {@link org.exoplatform.container.PortalContainer} object.
   */
  public CodesManagementREST(CodesService codesService, PortalContainer container) {
    this.codesService = codesService;
  }

  /**
   * <p>getActivityCodes.</p>
   *
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @GET
  @Path("activityCode")
  @RolesAllowed("users")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Retrieves all available subresources of current endpoint", httpMethod = "GET", response = Response.class, produces = "application/json")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.OK, message = "Request fulfilled"),
          @ApiResponse(code = 500, message = "Internal server error") })
  public Response getActivityCodes() {
    try {
      Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
      if (sourceIdentity == null) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
      }
      return Response.ok(codesService.getActivityCodesList()).build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while getting ActivityCodes", e);
      return Response.serverError().build();
    }
  }

  /**
   * <p>createActivityCode.</p>
   *
   * @param activityCode a {@link org.exoplatform.timetracker.dto.ActivityCode} object.
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @POST
  @Path("activityCode")
  @RolesAllowed("administrators")
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Creates a new ActivityCode", httpMethod = "POST", response = Response.class, notes = "empty response")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
          @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
          @ApiResponse(code = 500, message = "Internal server error") })
  public Response createActivityCode(@ApiParam(value = "ActivityCode to save", required = true) ActivityCode activityCode) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      codesService.createActivityCode(activityCode);
    } catch (EntityExistsException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while creating ActivityCode", e);
      return Response.serverError().build();
    }
    LOG.info("service=time-tracker operation=add-activity-code parameters=\"user_social_id:{}\"", sourceIdentity.getId());
    return Response.noContent().build();
  }

  /**
   * <p>updateActivityCode.</p>
   *
   * @param activityCode a {@link org.exoplatform.timetracker.dto.ActivityCode} object.
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @PUT
  @Path("activityCode")
  @RolesAllowed("administrators")
  @ApiOperation(value = "Updates an existing ActivityCode identified by its id", httpMethod = "PUT", response = Response.class, notes = "empty response")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
          @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
          @ApiResponse(code = 500, message = "Internal server error") })
  public Response updateActivityCode(@ApiParam(value = "ActivityCode to update", required = true) ActivityCode activityCode) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      codesService.updateActivityCode(activityCode, getCurrentUserName());
    } catch (IllegalAccessException e) {
      LOG.warn(e);
      return Response.status(HTTPStatus.UNAUTHORIZED).build();
    } catch (EntityExistsException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while updating ActivityCode", e);
      return Response.serverError().build();
    }
    LOG.info("service=time-tracker operation=update-activity-code parameters=\"user_social_id:{}\"", sourceIdentity.getId());
    return Response.noContent().build();
  }

  /**
   * <p>deleteActivityCode.</p>
   *
   * @param activityCodeId a {@link java.lang.Long} object.
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @DELETE
  @Path("activityCode/{activityCodeId}")
  @RolesAllowed("administrators")
  @ApiOperation(value = "Deletes an existing ActivityCode identified by its id", httpMethod = "DELETE", response = Response.class, notes = "empty response")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
          @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
          @ApiResponse(code = 500, message = "Internal server error") })
  public Response deleteActivityCode(@ApiParam(value = "ActivityCode technical id to delete", required = true) @PathParam("activityCodeId") Long activityCodeId) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      codesService.deleteActivityCode(activityCodeId, getCurrentUserName());
    } catch (IllegalAccessException e) {
      LOG.warn(e);
      return Response.status(HTTPStatus.UNAUTHORIZED).build();
    } catch (EntityNotFoundException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while deleting ActivityCode", e);
      return Response.serverError().build();
    }
    LOG.info("service=time-tracker operation=delete-activity-code parameters=\"user_social_id:{}\"", sourceIdentity.getId());
    return Response.noContent().build();
  }



  /**
   * <p>getSubActivityCodes.</p>
   *
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @GET
  @Path("subActivityCode")
  @RolesAllowed("users")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Retrieves all available subresources of current endpoint", httpMethod = "GET", response = Response.class, produces = "application/json")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.OK, message = "Request fulfilled"),
          @ApiResponse(code = 500, message = "Internal server error") })
  public Response getSubActivityCodes() {
    try {
      Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
      if (sourceIdentity == null) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
      }
      return Response.ok(codesService.getSubActivityCodesList()).build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while getting SubActivityCodes", e);
      return Response.serverError().build();
    }
  }

  /**
   * <p>createSubActivityCode.</p>
   *
   * @param subActivityCode a {@link org.exoplatform.timetracker.dto.SubActivityCode} object.
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @POST
  @Path("subActivityCode")
  @RolesAllowed("administrators")
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Creates a new SubActivityCode", httpMethod = "POST", response = Response.class, notes = "empty response")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
          @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
          @ApiResponse(code = 500, message = "Internal server error") })
  public Response createSubActivityCode(@ApiParam(value = "SubActivityCode to save", required = true) SubActivityCode subActivityCode) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      codesService.createSubActivityCode(subActivityCode);
    } catch (EntityExistsException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while creating SubActivityCode", e);
      return Response.serverError().build();
    }
    LOG.info("service=time-tracker operation=add-sub-activity-code parameters=\"user_social_id:{}\"", sourceIdentity.getId());
    return Response.noContent().build();
  }

  /**
   * <p>updateSubActivityCode.</p>
   *
   * @param subActivityCode a {@link org.exoplatform.timetracker.dto.SubActivityCode} object.
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @PUT
  @Path("subActivityCode")
  @RolesAllowed("administrators")
  @ApiOperation(value = "Updates an existing SubActivityCode identified by its id", httpMethod = "PUT", response = Response.class, notes = "empty response")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
          @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
          @ApiResponse(code = 500, message = "Internal server error") })
  public Response updateSubActivityCode(@ApiParam(value = "SubActivityCode to update", required = true) SubActivityCode subActivityCode) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      codesService.updateSubActivityCode(subActivityCode, getCurrentUserName());
    } catch (IllegalAccessException e) {
      LOG.warn(e);
      return Response.status(HTTPStatus.UNAUTHORIZED).build();
    } catch (EntityExistsException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while updating SubActivityCode", e);
      return Response.serverError().build();
    }
    LOG.info("service=time-tracker operation=edit-sub-activity-code parameters=\"user_social_id:{}\"", sourceIdentity.getId());
    return Response.noContent().build();
  }

  /**
   * <p>deleteSubActivityCode.</p>
   *
   * @param subActivityCodeId a {@link java.lang.Long} object.
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @DELETE
  @Path("subActivityCode/{subActivityCodeId}")
  @RolesAllowed("administrators")
  @ApiOperation(value = "Deletes an existing SubActivityCode identified by its id", httpMethod = "DELETE", response = Response.class, notes = "empty response")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
          @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
          @ApiResponse(code = 500, message = "Internal server error") })
  public Response deleteSubActivityCode(@ApiParam(value = "SubActivityCode technical id to delete", required = true) @PathParam("subActivityCodeId") Long subActivityCodeId) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      codesService.deleteSubActivityCode(subActivityCodeId, getCurrentUserName());
    } catch (IllegalAccessException e) {
      LOG.warn(e);
      return Response.status(HTTPStatus.UNAUTHORIZED).build();
    } catch (EntityNotFoundException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while deleting SubActivityCode", e);
      return Response.serverError().build();
    }
    LOG.info("service=time-tracker operation=delete-sub-activity-code parameters=\"user_social_id:{}\"", sourceIdentity.getId());
    return Response.noContent().build();
  }




  /**
   * <p>getTypes.</p>
   *
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @GET
  @Path("type")
  @RolesAllowed("users")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Retrieves all available subresources of current endpoint", httpMethod = "GET", response = Response.class, produces = "application/json")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.OK, message = "Request fulfilled"),
          @ApiResponse(code = 500, message = "Internal server error") })
  public Response getTypes() {
    try {
      Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
      if (sourceIdentity == null) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
      }
      return Response.ok(codesService.getTypesList()).build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while getting Types", e);
      return Response.serverError().build();
    }
  }

  /**
   * <p>createType.</p>
   *
   * @param type a {@link org.exoplatform.timetracker.dto.Type} object.
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @POST
  @Path("type")
  @RolesAllowed("administrators")
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Creates a new Type", httpMethod = "POST", response = Response.class, notes = "empty response")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
          @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
          @ApiResponse(code = 500, message = "Internal server error") })
  public Response createType(@ApiParam(value = "Type to save", required = true) Type type) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      codesService.createType(type);
    } catch (EntityExistsException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while creating Type", e);
      return Response.serverError().build();
    }
    LOG.info("service=time-tracker operation=add-type parameters=\"user_social_id:{}\"", sourceIdentity.getId());
    return Response.noContent().build();
  }

  /**
   * <p>updateType.</p>
   *
   * @param type a {@link org.exoplatform.timetracker.dto.Type} object.
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @PUT
  @Path("type")
  @RolesAllowed("administrators")
  @ApiOperation(value = "Updates an existing Type identified by its id", httpMethod = "PUT", response = Response.class, notes = "empty response")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
          @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
          @ApiResponse(code = 500, message = "Internal server error") })
  public Response updateType(@ApiParam(value = "Type to update", required = true) Type type) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      codesService.updateType(type, getCurrentUserName());
    } catch (IllegalAccessException e) {
      LOG.warn(e);
      return Response.status(HTTPStatus.UNAUTHORIZED).build();
    } catch (EntityExistsException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while updating Type", e);
      return Response.serverError().build();
    }
    LOG.info("service=time-tracker operation=edit-type parameters=\"user_social_id:{}\"", sourceIdentity.getId());
    return Response.noContent().build();
  }

  /**
   * <p>deleteType.</p>
   *
   * @param typeId a {@link java.lang.Long} object.
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @DELETE
  @Path("type/{typeId}")
  @RolesAllowed("administrators")
  @ApiOperation(value = "Deletes an existing Type identified by its id", httpMethod = "DELETE", response = Response.class, notes = "empty response")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
          @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
          @ApiResponse(code = 500, message = "Internal server error") })
  public Response deleteType(@ApiParam(value = "Type technical id to delete", required = true) @PathParam("typeId") Long typeId) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      codesService.deleteType(typeId, getCurrentUserName());
    } catch (IllegalAccessException e) {
      LOG.warn(e);
      return Response.status(HTTPStatus.UNAUTHORIZED).build();
    } catch (EntityNotFoundException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while deleting Type", e);
      return Response.serverError().build();
    }
    LOG.info("service=time-tracker operation=delete-type parameters=\"user_social_id:{}\"", sourceIdentity.getId());
    return Response.noContent().build();
  }



  /**
   * <p>getSubTypes.</p>
   *
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @GET
  @Path("subType")
  @RolesAllowed("users")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Retrieves all available subresources of current endpoint", httpMethod = "GET", response = Response.class, produces = "application/json")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.OK, message = "Request fulfilled"),
          @ApiResponse(code = 500, message = "Internal server error") })
  public Response getSubTypes() {
    try {
      Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
      if (sourceIdentity == null) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
      }
      return Response.ok(codesService.getSubTypesList()).build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while getting SubTypes", e);
      return Response.serverError().build();
    }
  }

  /**
   * <p>createSubType.</p>
   *
   * @param subType a {@link org.exoplatform.timetracker.dto.SubType} object.
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @POST
  @Path("subType")
  @RolesAllowed("administrators")
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Creates a new SubType", httpMethod = "POST", response = Response.class, notes = "empty response")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
          @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
          @ApiResponse(code = 500, message = "Internal server error") })
  public Response createSubType(@ApiParam(value = "SubType to save", required = true) SubType subType) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      codesService.createSubType(subType);
    } catch (EntityExistsException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while creating SubType", e);
      return Response.serverError().build();
    }
    LOG.info("service=time-tracker operation=add-sub-type parameters=\"user_social_id:{}\"", sourceIdentity.getId());

    return Response.noContent().build();
  }

  /**
   * <p>updateSubType.</p>
   *
   * @param subType a {@link org.exoplatform.timetracker.dto.SubType} object.
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @PUT
  @Path("subType")
  @RolesAllowed("administrators")
  @ApiOperation(value = "Updates an existing SubType identified by its id", httpMethod = "PUT", response = Response.class, notes = "empty response")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
          @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
          @ApiResponse(code = 500, message = "Internal server error") })
  public Response updateSubType(@ApiParam(value = "SubType to update", required = true) SubType subType) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      codesService.updateSubType(subType, getCurrentUserName());
    } catch (IllegalAccessException e) {
      LOG.warn(e);
      return Response.status(HTTPStatus.UNAUTHORIZED).build();
    } catch (EntityExistsException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while updating SubType", e);
      return Response.serverError().build();
    }
    LOG.info("service=time-tracker operation=update-sub-type parameters=\"user_social_id:{}\"", sourceIdentity.getId());
    return Response.noContent().build();
  }

  /**
   * <p>deleteSubType.</p>
   *
   * @param subTypeId a {@link java.lang.Long} object.
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @DELETE
  @Path("subType/{subTypeId}")
  @RolesAllowed("administrators")
  @ApiOperation(value = "Deletes an existing SubType identified by its id", httpMethod = "DELETE", response = Response.class, notes = "empty response")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
          @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
          @ApiResponse(code = 500, message = "Internal server error") })
  public Response deleteSubType(@ApiParam(value = "SubType technical id to delete", required = true) @PathParam("subTypeId") Long subTypeId) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      codesService.deleteSubType(subTypeId, getCurrentUserName());
    } catch (IllegalAccessException e) {
      LOG.warn(e);
      return Response.status(HTTPStatus.UNAUTHORIZED).build();
    } catch (EntityNotFoundException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while deleting SubType", e);
      return Response.serverError().build();
    }
    LOG.info("service=time-tracker operation=delete-sub-type parameters=\"user_social_id:{}\"", sourceIdentity.getId());
    return Response.noContent().build();
  }



  private String getCurrentUserName() {
    ConversationState state = ConversationState.getCurrent();
    return state == null || state.getIdentity() == null ? null : state.getIdentity().getUserId();
  }

}
