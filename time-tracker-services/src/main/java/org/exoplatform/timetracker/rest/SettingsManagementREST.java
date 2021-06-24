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

import io.swagger.jaxrs.PATCH;
import org.exoplatform.common.http.HTTPStatus;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.rest.resource.ResourceContainer;
import org.exoplatform.services.security.ConversationState;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.service.rest.Util;
import org.exoplatform.timetracker.dto.Location;
import org.exoplatform.timetracker.dto.Office;
import org.exoplatform.timetracker.dto.TimeTrackerSetting;
import org.exoplatform.timetracker.dto.WorkTime;
import org.exoplatform.timetracker.service.TimeTrackerSettingsService;

import io.swagger.annotations.*;

/**
 * <p>
 * SettingsManagementREST class.
 * </p>
 *
 * @author medamine
 * @version $Id: $Id
 */
@Path("timetracker/settings")
@RolesAllowed("users")
@Api(value = "/timetracker", description = "Manage and access Codes") // NOSONAR
public class SettingsManagementREST implements ResourceContainer {

  private static final Log      LOG                 = ExoLogger.getLogger(SettingsManagementREST.class);

  private final String          portalContainerName = "portal";

  private final TimeTrackerSettingsService timeTrackerSettingsService;

  /**
   * <p>
   * Constructor for SettingsManagementREST.
   * </p>
   *
   * @param timeTrackerSettingsService a {@link TimeTrackerSettingsService} object.
   * @param container a {@link PortalContainer} object.
   */
  public SettingsManagementREST(TimeTrackerSettingsService timeTrackerSettingsService, PortalContainer container) {
    this.timeTrackerSettingsService = timeTrackerSettingsService;
  }

  /**
   * <p>
   * getWorkTimes.
   * </p>
   *
   * @return a {@link Response} object.
   */
  @GET
  @Path("worktime")
  @RolesAllowed("users")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Retrieves all available subresources of current endpoint", httpMethod = "GET", response = Response.class, produces = "application/json")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.OK, message = "Request fulfilled"),
      @ApiResponse(code = 500, message = "Internal server error") })
  public Response getWorkTimes() {
    try {
      Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
      if (sourceIdentity == null) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
      }
      return Response.ok(timeTrackerSettingsService.getWorkTimesList()).build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while getting WorkTimes", e);
      return Response.serverError().build();
    }
  }

  /**
   * <p>
   * createWorkTime.
   * </p>
   *
   * @param workTime a {@link WorkTime} object.
   * @return a {@link Response} object.
   */
  @POST
  @Path("worktime")
  @RolesAllowed("time-tracking-managers")
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Creates a new WorkTime", httpMethod = "POST", response = Response.class, notes = "empty response")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
      @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
      @ApiResponse(code = 500, message = "Internal server error") })
  public Response createWorkTime(@ApiParam(value = "WorkTime to save", required = true) WorkTime workTime) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      timeTrackerSettingsService.createWorkTime(workTime);
    } catch (EntityExistsException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while creating WorkTime", e);
      return Response.serverError().build();
    }
    LOG.info("service=time-tracker operation=add-activity-code parameters=\"user_id:{}\"", sourceIdentity.getId());
    return Response.noContent().build();
  }

  /**
   * <p>
   * updateWorkTime.
   * </p>
   *
   * @param workTime a {@link WorkTime} object.
   * @return a {@link Response} object.
   */
  @PUT
  @Path("worktime")
  @RolesAllowed("time-tracking-managers")
  @ApiOperation(value = "Updates an existing WorkTime identified by its id", httpMethod = "PUT", response = Response.class, notes = "empty response")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
      @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
      @ApiResponse(code = 500, message = "Internal server error") })
  public Response updateWorkTime(@ApiParam(value = "WorkTime to update", required = true) WorkTime workTime) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      timeTrackerSettingsService.updateWorkTime(workTime, getCurrentUserName());
    } catch (IllegalAccessException e) {
      LOG.warn(e);
      return Response.status(HTTPStatus.UNAUTHORIZED).build();
    } catch (EntityExistsException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while updating WorkTime", e);
      return Response.serverError().build();
    }
    LOG.info("service=time-tracker operation=update-activity-code parameters=\"user_id:{}\"", sourceIdentity.getId());
    return Response.noContent().build();
  }

  /**
   * <p>
   * deleteWorkTime.
   * </p>
   *
   * @param workTimeId a {@link Long} object.
   * @return a {@link Response} object.
   */
  @DELETE
  @Path("workTime/{workTimeId}")
  @RolesAllowed("time-tracking-managers")
  @ApiOperation(value = "Deletes an existing WorkTime identified by its id", httpMethod = "DELETE", response = Response.class, notes = "empty response")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
      @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
      @ApiResponse(code = 500, message = "Internal server error") })
  public Response deleteWorkTime(@ApiParam(value = "WorkTime technical id to delete", required = true) @PathParam("workTimeId") Long workTimeId) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      timeTrackerSettingsService.deleteWorkTime(workTimeId, getCurrentUserName());
    } catch (IllegalAccessException e) {
      LOG.warn(e);
      return Response.status(HTTPStatus.UNAUTHORIZED).build();
    } catch (EntityNotFoundException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while deleting WorkTime", e);
      return Response.serverError().build();
    }
    LOG.info("service=time-tracker operation=delete-activity-code parameters=\"user_id:{}\"", sourceIdentity.getId());
    return Response.noContent().build();
  }

  /**
   * <p>
   * getLocations.
   * </p>
   *
   * @return a {@link Response} object.
   */
  @GET
  @Path("location")
  @RolesAllowed("users")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Retrieves all available subresources of current endpoint", httpMethod = "GET", response = Response.class, produces = "application/json")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.OK, message = "Request fulfilled"),
      @ApiResponse(code = 500, message = "Internal server error") })
  public Response getLocations() {
    try {
      Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
      if (sourceIdentity == null) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
      }
      return Response.ok(timeTrackerSettingsService.getLocationsList()).build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while getting Locations", e);
      return Response.serverError().build();
    }
  }

  /**
   * <p>
   * createLocation.
   * </p>
   *
   * @param location a {@link Location} object.
   * @return a {@link Response} object.
   */
  @POST
  @Path("location")
  @RolesAllowed("time-tracking-managers")
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Creates a new Location", httpMethod = "POST", response = Response.class, notes = "empty response")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
      @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
      @ApiResponse(code = 500, message = "Internal server error") })
  public Response createLocation(@ApiParam(value = "Location to save", required = true) Location location) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      timeTrackerSettingsService.createLocation(location);
    } catch (EntityExistsException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while creating Location", e);
      return Response.serverError().build();
    }
    LOG.info("service=time-tracker operation=add-sub-activity-code parameters=\"user_id:{}\"", sourceIdentity.getId());
    return Response.noContent().build();
  }

  /**
   * <p>
   * updateLocation.
   * </p>
   *
   * @param location a {@link Location} object.
   * @return a {@link Response} object.
   */
  @PUT
  @Path("location")
  @RolesAllowed("time-tracking-managers")
  @ApiOperation(value = "Updates an existing Location identified by its id", httpMethod = "PUT", response = Response.class, notes = "empty response")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
      @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
      @ApiResponse(code = 500, message = "Internal server error") })
  public Response updateLocation(@ApiParam(value = "Location to update", required = true) Location location) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      timeTrackerSettingsService.updateLocation(location, getCurrentUserName());
    } catch (IllegalAccessException e) {
      LOG.warn(e);
      return Response.status(HTTPStatus.UNAUTHORIZED).build();
    } catch (EntityExistsException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while updating Location", e);
      return Response.serverError().build();
    }
    LOG.info("service=time-tracker operation=edit-sub-activity-code parameters=\"user_id:{}\"", sourceIdentity.getId());
    return Response.noContent().build();
  }

  /**
   * <p>
   * deleteLocation.
   * </p>
   *
   * @param code a {@link String} object.
   * @return a {@link Response} object.
   */
  @DELETE
  @Path("location/{code}")
  @RolesAllowed("time-tracking-managers")
  @ApiOperation(value = "Deletes an existing Location identified by its id", httpMethod = "DELETE", response = Response.class, notes = "empty response")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
      @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
      @ApiResponse(code = 500, message = "Internal server error") })
  public Response deleteLocation(@ApiParam(value = "Location technical id to delete", required = true) @PathParam("code") String code) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      timeTrackerSettingsService.deleteLocation(code, getCurrentUserName());
    } catch (IllegalAccessException e) {
      LOG.warn(e);
      return Response.status(HTTPStatus.UNAUTHORIZED).build();
    } catch (EntityNotFoundException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while deleting Location", e);
      return Response.serverError().build();
    }
    LOG.info("service=time-tracker operation=delete-sub-activity-code parameters=\"user_id:{}\"", sourceIdentity.getId());
    return Response.noContent().build();
  }

  /**
   * <p>
   * getOffices.
   * </p>
   *
   * @return a {@link Response} object.
   */
  @GET
  @Path("office")
  @RolesAllowed("users")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Retrieves all available subresources of current endpoint", httpMethod = "GET", response = Response.class, produces = "application/json")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.OK, message = "Request fulfilled"),
      @ApiResponse(code = 500, message = "Internal server error") })
  public Response getOffices() {
    try {
      Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
      if (sourceIdentity == null) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
      }
      return Response.ok(timeTrackerSettingsService.getOfficesList()).build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while getting Offices", e);
      return Response.serverError().build();
    }
  }

  /**
   * <p>
   * createOffice.
   * </p>
   *
   * @param office a {@link Office} object.
   * @return a {@link Response} object.
   */
  @POST
  @Path("office")
  @RolesAllowed("time-tracking-managers")
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Creates a new Office", httpMethod = "POST", response = Response.class, notes = "empty response")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
      @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
      @ApiResponse(code = 500, message = "Internal server error") })
  public Response createOffice(@ApiParam(value = "Office to save", required = true) Office office) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      timeTrackerSettingsService.createOffice(office);
    } catch (EntityExistsException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while creating Office", e);
      return Response.serverError().build();
    }
    LOG.info("service=time-tracker operation=add-office parameters=\"user_id:{}\"", sourceIdentity.getId());
    return Response.noContent().build();
  }

  /**
   * <p>
   * updateOffice.
   * </p>
   *
   * @param office a {@link Office} object.
   * @return a {@link Response} object.
   */
  @PUT
  @Path("office")
  @RolesAllowed("time-tracking-managers")
  @ApiOperation(value = "Updates an existing Office identified by its id", httpMethod = "PUT", response = Response.class, notes = "empty response")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
      @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
      @ApiResponse(code = 500, message = "Internal server error") })
  public Response updateOffice(@ApiParam(value = "Office to update", required = true) Office office) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      timeTrackerSettingsService.updateOffice(office, getCurrentUserName());
    } catch (IllegalAccessException e) {
      LOG.warn(e);
      return Response.status(HTTPStatus.UNAUTHORIZED).build();
    } catch (EntityExistsException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while updating Office", e);
      return Response.serverError().build();
    }
    LOG.info("service=time-tracker operation=edit-office parameters=\"user_id:{}\"", sourceIdentity.getId());
    return Response.noContent().build();
  }

  /**
   * <p>
   * deleteOffice.
   * </p>
   *
   * @param code a {@link String} object.
   * @return a {@link Response} object.
   */
  @DELETE
  @Path("office/{code}")
  @RolesAllowed("time-tracking-managers")
  @ApiOperation(value = "Deletes an existing Office identified by its id", httpMethod = "DELETE", response = Response.class, notes = "empty response")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
      @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
      @ApiResponse(code = 500, message = "Internal server error") })
  public Response deleteOffice(@ApiParam(value = "Office technical id to delete", required = true) @PathParam("code") String code) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      timeTrackerSettingsService.deleteOffice(code, getCurrentUserName());
    } catch (IllegalAccessException e) {
      LOG.warn(e);
      return Response.status(HTTPStatus.UNAUTHORIZED).build();
    } catch (EntityNotFoundException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while deleting Office", e);
      return Response.serverError().build();
    }
    LOG.info("service=time-tracker operation=delete-office parameters=\"user_id:{}\"", sourceIdentity.getId());
    return Response.noContent().build();
  }

  /**
   * <p>
   * get Other Settings.
   * </p>
   *
   * @return a {@link Response} object.
   */
  @GET
  @Path("other")
  @RolesAllowed("users")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Retrieves all available resources of current endpoint", httpMethod = "GET", response = Response.class, produces = "application/json")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.OK, message = "Request fulfilled"),
          @ApiResponse(code = 500, message = "Internal server error") })
  public Response getOtherSettings() {
    try {
      Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
      if (sourceIdentity == null) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
      }
      return Response.ok(timeTrackerSettingsService.getSettings()).build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while getting WorkTimes", e);
      return Response.serverError().build();
    }
  }

  /**
   * <p>
   * Update settings.
   * </p>
   *
   * @param ttSettings a {@link TimeTrackerSetting} object.
   * @return a {@link Response} object.
   */
  @PATCH
  @Path("others")
  @RolesAllowed("time-tracking-managers")
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "update settings", httpMethod = "POST", response = Response.class, notes = "empty response")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
          @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
          @ApiResponse(code = 500, message = "Internal server error") })
  public Response saveSettings(@ApiParam(value = "WorkTime to save", required = true) TimeTrackerSetting ttSettings) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      timeTrackerSettingsService.saveSettings(ttSettings);
    } catch (EntityExistsException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while creating WorkTime", e);
      return Response.serverError().build();
    }
    LOG.info("service=time-tracker operation=add-activity-code parameters=\"user_id:{}\"", sourceIdentity.getId());
    return Response.noContent().build();
  }


  private String getCurrentUserName() {
    ConversationState state = ConversationState.getCurrent();
    return state == null || state.getIdentity() == null ? null : state.getIdentity().getUserId();
  }

}
