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
import org.exoplatform.timetracker.dto.Feature;
import org.exoplatform.timetracker.service.FeatureService;

import io.swagger.annotations.*;

/**
 * <p>FeaturesManagementREST class.</p>
 *
 * @author medamine
 * @version $Id: $Id
 */
@Path("timetracker/featuresmgn")
@RolesAllowed("users")
@Api(value = "/timetracker", description = "Manage and access Features") // NOSONAR
public class FeaturesManagementREST implements ResourceContainer {

  private static final Log      LOG                 = ExoLogger.getLogger(FeaturesManagementREST.class);

  private final String          portalContainerName = "portal";

  private final FeatureService featureService;

  /**
   * <p>Constructor for FeaturesManagementREST.</p>
   *
   * @param featureService a {@link org.exoplatform.timetracker.service.FeatureService} object.
   * @param container a {@link org.exoplatform.container.PortalContainer} object.
   */
  public FeaturesManagementREST(FeatureService featureService, PortalContainer container) {
    this.featureService = featureService;
  }

  /**
   * <p>getFeatures.</p>
   *
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @GET
  @Path("feature")
  @RolesAllowed("users")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Retrieves all available subresources of current endpoint", httpMethod = "GET", response = Response.class, produces = "application/json")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.OK, message = "Request fulfilled"),
      @ApiResponse(code = 500, message = "Internal server error") })
  public Response getFeatures() {
    try {
      Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
      if (sourceIdentity == null) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
      }
      return Response.ok(featureService.getFeaturesList()).build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while getting Features", e);
      return Response.serverError().build();
    }
  }

  /**
   * <p>createFeature.</p>
   *
   * @param feature a {@link org.exoplatform.timetracker.dto.Feature} object.
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @POST
  @Path("feature")
  @RolesAllowed("administrators")
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Creates a new Feature", httpMethod = "POST", response = Response.class, notes = "empty response")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
      @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
      @ApiResponse(code = 500, message = "Internal server error") })
  public Response createFeature(@ApiParam(value = "Feature to save", required = true) Feature feature) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      featureService.createFeature(feature);
    } catch (EntityExistsException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while creating Feature", e);
      return Response.serverError().build();
    }
    return Response.noContent().build();
  }

  /**
   * <p>updateFeature.</p>
   *
   * @param feature a {@link org.exoplatform.timetracker.dto.Feature} object.
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @PUT
  @Path("feature")
  @RolesAllowed("administrators")
  @ApiOperation(value = "Updates an existing Feature identified by its id", httpMethod = "PUT", response = Response.class, notes = "empty response")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
      @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
      @ApiResponse(code = 500, message = "Internal server error") })
  public Response updateFeature(@ApiParam(value = "Feature to update", required = true) Feature feature) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      featureService.updateFeature(feature, getCurrentUserName());
    } catch (IllegalAccessException e) {
      LOG.warn(e);
      return Response.status(HTTPStatus.UNAUTHORIZED).build();
    } catch (EntityExistsException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while updating Feature", e);
      return Response.serverError().build();
    }
    return Response.noContent().build();
  }

  /**
   * <p>deleteFeature.</p>
   *
   * @param featureId a {@link java.lang.Long} object.
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @DELETE
  @Path("feature/{featureId}")
  @RolesAllowed("administrators")
  @ApiOperation(value = "Deletes an existing Feature identified by its id", httpMethod = "DELETE", response = Response.class, notes = "empty response")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
      @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
      @ApiResponse(code = 500, message = "Internal server error") })
  public Response deleteFeature(@ApiParam(value = "Feature technical id to delete", required = true) @PathParam("featureId") Long featureId) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      featureService.deleteFeature(featureId, getCurrentUserName());
    } catch (IllegalAccessException e) {
      LOG.warn(e);
      return Response.status(HTTPStatus.UNAUTHORIZED).build();
    } catch (EntityNotFoundException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while deleting Feature", e);
      return Response.serverError().build();
    }
    return Response.noContent().build();
  }

  private String getCurrentUserName() {
    ConversationState state = ConversationState.getCurrent();
    return state == null || state.getIdentity() == null ? null : state.getIdentity().getUserId();
  }

}
