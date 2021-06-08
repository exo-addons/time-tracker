/*
 * This file is part of the Meeds salesOrder (https://meeds.io/).
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
import org.exoplatform.timetracker.dto.SalesOrder;
import org.exoplatform.timetracker.service.SalesOrderService;

import io.swagger.annotations.*;

/**
 * <p>SalesOrdersManagementREST class.</p>
 *
 * @author medamine
 * @version $Id: $Id
 */
@Path("timetracker/salesOrdersmgn")
@RolesAllowed("users")
@Api(value = "/timetracker", description = "Manage and access SalesOrders") // NOSONAR
public class SalesOrdersManagementREST implements ResourceContainer {

  private static final Log      LOG                 = ExoLogger.getLogger(SalesOrdersManagementREST.class);

  private final String          portalContainerName = "portal";

  private final SalesOrderService salesOrderService;

  /**
   * <p>Constructor for SalesOrdersManagementREST.</p>
   *
   * @param salesOrderService a {@link org.exoplatform.timetracker.service.SalesOrderService} object.
   * @param container a {@link org.exoplatform.container.PortalContainer} object.
   */
  public SalesOrdersManagementREST(SalesOrderService salesOrderService, PortalContainer container) {
    this.salesOrderService = salesOrderService;
  }

  /**
   * <p>getSalesOrders.</p>
   *
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @GET
  @Path("salesOrder")
  @RolesAllowed("users")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Retrieves all available subresources of current endpoint", httpMethod = "GET", response = Response.class, produces = "application/json")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.OK, message = "Request fulfilled"),
      @ApiResponse(code = 500, message = "Internal server error") })
  public Response getSalesOrders() {
    try {
      Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
      if (sourceIdentity == null) {
        return Response.status(Response.Status.UNAUTHORIZED).build();
      }
      return Response.ok(salesOrderService.getSalesOrdersList()).build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while getting SalesOrders", e);
      return Response.serverError().build();
    }
  }

  /**
   * <p>createSalesOrder.</p>
   *
   * @param salesOrder a {@link org.exoplatform.timetracker.dto.SalesOrder} object.
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @POST
  @Path("salesOrder")
  @RolesAllowed("time-tracking-managers")
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Creates a new SalesOrder", httpMethod = "POST", response = Response.class, notes = "empty response")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
      @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
      @ApiResponse(code = 500, message = "Internal server error") })
  public Response createSalesOrder(@ApiParam(value = "SalesOrder to save", required = true) SalesOrder salesOrder) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      salesOrderService.createSalesOrder(salesOrder);
    } catch (EntityExistsException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while creating SalesOrder", e);
      return Response.serverError().build();
    }
    LOG.info("service=time-tracker operation=add-sales-order parameters=\"user_social_id:{}\"", sourceIdentity.getId());
    return Response.noContent().build();
  }

  /**
   * <p>updateSalesOrder.</p>
   *
   * @param salesOrder a {@link org.exoplatform.timetracker.dto.SalesOrder} object.
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @PUT
  @Path("salesOrder")
  @RolesAllowed("time-tracking-managers")
  @ApiOperation(value = "Updates an existing SalesOrder identified by its id", httpMethod = "PUT", response = Response.class, notes = "empty response")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
      @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
      @ApiResponse(code = 500, message = "Internal server error") })
  public Response updateSalesOrder(@ApiParam(value = "SalesOrder to update", required = true) SalesOrder salesOrder) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      salesOrderService.updateSalesOrder(salesOrder, getCurrentUserName());
    } catch (IllegalAccessException e) {
      LOG.warn(e);
      return Response.status(HTTPStatus.UNAUTHORIZED).build();
    } catch (EntityExistsException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while updating SalesOrder", e);
      return Response.serverError().build();
    }
    LOG.info("service=time-tracker operation=update-sales-order parameters=\"user_social_id:{}\"", sourceIdentity.getId());
    return Response.noContent().build();
  }

  /**
   * <p>deleteSalesOrder.</p>
   *
   * @param salesOrderId a {@link java.lang.Long} object.
   * @return a {@link javax.ws.rs.core.Response} object.
   */
  @DELETE
  @Path("salesOrder/{salesOrderId}")
  @RolesAllowed("time-tracking-managers")
  @ApiOperation(value = "Deletes an existing SalesOrder identified by its id", httpMethod = "DELETE", response = Response.class, notes = "empty response")
  @ApiResponses(value = { @ApiResponse(code = HTTPStatus.NO_CONTENT, message = "Request fulfilled"),
      @ApiResponse(code = HTTPStatus.UNAUTHORIZED, message = "Unauthorized operation"),
      @ApiResponse(code = 500, message = "Internal server error") })
  public Response deleteSalesOrder(@ApiParam(value = "SalesOrder technical id to delete", required = true) @PathParam("salesOrderId") Long salesOrderId) {
    Identity sourceIdentity = Util.getAuthenticatedUserIdentity(portalContainerName);
    if (sourceIdentity == null) {
      return Response.status(Response.Status.UNAUTHORIZED).build();
    }
    try {
      salesOrderService.deleteSalesOrder(salesOrderId, getCurrentUserName());
    } catch (IllegalAccessException e) {
      LOG.warn(e);
      return Response.status(HTTPStatus.UNAUTHORIZED).build();
    } catch (EntityNotFoundException e) {
      LOG.warn(e);
      return Response.serverError().build();
    } catch (Exception e) {
      LOG.error("Unknown error occurred while deleting SalesOrder", e);
      return Response.serverError().build();
    }
    LOG.info("service=time-tracker operation=delete-sales-order parameters=\"user_social_id:{}\"", sourceIdentity.getId());
    return Response.noContent().build();
  }

  private String getCurrentUserName() {
    ConversationState state = ConversationState.getCurrent();
    return state == null || state.getIdentity() == null ? null : state.getIdentity().getUserId();
  }

}
