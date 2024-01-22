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

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
import org.exoplatform.timetracker.dto.FilterModel;
import org.exoplatform.timetracker.service.FilterService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

/**
 * <p>FiltersManagementREST class.</p>
 *
 * @author medamine
 * @version $Id: $Id
 */
@Path("timetracker/filtersmgn")
@RolesAllowed("users")
@Tag(name = "/timetracker", description = "Manage and access Filters") // NOSONAR
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
  @Operation(summary = "Retrieves all available subresources of current endpoint", method = "GET")
  @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Request fulfilled"),
          @ApiResponse(responseCode = "500", description = "Internal server error") })
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
  @RolesAllowed("users")
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(summary = "Creates a new Filter", method = "POST", description = "empty response")
  @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Request fulfilled"),
          @ApiResponse(responseCode = "401", description = "Unauthorized operation"),
          @ApiResponse(responseCode = "500", description = "Internal server error") })
  public Response createFilter(@Parameter(description = "Filter to save", required = true) FilterModel filter) {
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
    LOG.info("service=time-tracker operation=add-personal-filter parameters=\"user_social_id:{}\"", sourceIdentity.getId());
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
  @RolesAllowed("users")
  @Operation(summary = "Deletes an existing Filter identified by its id", method = "DELETE", description = "empty response")
  @ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Request fulfilled"),
          @ApiResponse(responseCode = "401", description = "Unauthorized operation"),
          @ApiResponse(responseCode = "500", description = "Internal server error") })
  public Response deleteFilter(@Parameter(description = "Filter technical id to delete", required = true) @PathParam("filterId") Long filterId) {
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
    LOG.info("service=time-tracker operation=delete-personal-filter parameters=\"user_social_id:{}\"", sourceIdentity.getId());
    return Response.noContent().build();
  }

  private String getCurrentUserName() {
    ConversationState state = ConversationState.getCurrent();
    return state == null || state.getIdentity() == null ? null : state.getIdentity().getUserId();
  }

}
