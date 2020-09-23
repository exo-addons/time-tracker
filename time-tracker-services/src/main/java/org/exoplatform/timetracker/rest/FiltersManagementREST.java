/*
 * This file is part of the Meeds filter (https://meeds.io/).
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
import org.exoplatform.timetracker.dto.Filter;
import org.exoplatform.timetracker.dto.FilterModel;
import org.exoplatform.timetracker.service.FilterService;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * <p>FiltersManagementREST class.</p>
 *
 * @author medamine
 * @version $Id: $Id
 */
@Path("timetracker/filtersmgn")
@RolesAllowed("users")
@Api(value = "/timetracker", description = "Manage and access Filters") // NOSONAR
public class FiltersManagementREST implements ResourceContainer {

  private static final Log      LOG                 = ExoLogger.getLogger(FiltersManagementREST.class);

  private final String          portalContainerName = "portal";

  private final FilterService filterService;

  /**
   * <p>Constructor for FiltersManagementREST.</p>
   *
   * @param filterService a {@link org.exoplatform.timetracker.service.FilterService} object.
   * @param container a {@link org.exoplatform.container.PortalContainer} object.
   */
  public FiltersManagementREST(FilterService filterService, PortalContainer container) {
    this.filterService = filterService;
  }

  /**
   * <p>getFilters.</p>
   *
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @GET
  @Path("filter")
  @RolesAllowed("users")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Retrieves all available subresources of current endpoint", httpMethod = "GET", response = Response.class, produces = "application/json")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.OK, message = "Request fulfilled"),
          @ApiResponse(code = 500, message = "Internal server error") })
  public Response getFilters() {
    try {
      Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
      if (sourceIdentity == null) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
      }
      return Response.ok(filterService.getFiltersList(getCurrentUserName()).toString()).build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while getting Filters", e);
      return Response.serverError().build();
    }
  }

  /**
   * <p>createFilter.</p>
   *
   * @param filter a {@link org.exoplatform.timetracker.dto.FilterModel} object.
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @POST
  @Path("filter")
  @RolesAllowed("administrators")
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Creates a new Filter", httpMethod = "POST", response = Response.class, notes = "empty response")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
          @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
          @ApiResponse(code = 500, message = "Internal server error") })
  public Response createFilter(@ApiParam(value = "Filter to save", required = true) FilterModel filter) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      filterService.createFilter(filter,getCurrentUserName());
    } catch (EntityExistsException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while creating Filter", e);
      return Response.serverError().build();
    }
    return Response.noContent().build();
  }



  /**
   * <p>deleteFilter.</p>
   *
   * @param filterId a {@link java.lang.Long} object.
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @DELETE
  @Path("filter/{filterId}")
  @RolesAllowed("administrators")
  @ApiOperation(value = "Deletes an existing Filter identified by its id", httpMethod = "DELETE", response = Response.class, notes = "empty response")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
          @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
          @ApiResponse(code = 500, message = "Internal server error") })
  public Response deleteFilter(@ApiParam(value = "Filter technical id to delete", required = true) @PathParam("filterId") Long filterId) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      filterService.deleteFilter(filterId);
    } catch (IllegalAccessException e) {
      LOG.warn(e);
      return Response.status(HTTPStatus.UNAUTHORIZED).build();
    } catch (EntityNotFoundException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while deleting Filter", e);
      return Response.serverError().build();
    }
    return Response.noContent().build();
  }

  private String getCurrentUserName() {
    ConversationState state = ConversationState.getCurrent();
    return state == null || state.getIdentity() == null ? null : state.getIdentity().getUserId();
  }

}
