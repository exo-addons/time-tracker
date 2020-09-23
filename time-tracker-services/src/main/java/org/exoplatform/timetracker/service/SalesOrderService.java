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
package org.exoplatform.timetracker.service;

import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang.StringUtils;

import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.timetracker.dto.SalesOrder;
import org.exoplatform.timetracker.storage.SalesOrderStorage;

/**
 * A Service to access and store Activities
 *
 * @author medamine
 * @version $Id: $Id
 */
public class SalesOrderService {

  private static final Log      LOG = ExoLogger.getLogger(SalesOrderService.class);

  private final SalesOrderStorage salesOrderStorage;

  /**
   * <p>Constructor for SalesOrderService.</p>
   *
   * @param salesOrderStorage a {@link org.exoplatform.timetracker.storage.SalesOrderStorage} object.
   */
  public SalesOrderService(SalesOrderStorage salesOrderStorage) {
    this.salesOrderStorage = salesOrderStorage;

  }

  /**
   * Create new SalesOrder that will be available for all users. If the SalesOrder
   * already exits an {@link javax.persistence.EntityExistsException} will be thrown.
   *
   * @param salesOrder SalesOrder to create
   * @return stored {@link org.exoplatform.timetracker.dto.SalesOrder} in datasource
   * @throws java.lang.Exception when SalesOrder already exists or an error occurs while
   *           creating SalesOrder or its attached image
   */
  public SalesOrder createSalesOrder(SalesOrder salesOrder) throws Exception {
    if (salesOrder == null) {
      throw new IllegalArgumentException("SalesOrder is mandatory");
    }
/*    SalesOrder existingSalesOrder = salesOrderStorage.getSalesOrderById(salesOrder.getId());
    if (existingSalesOrder != null) {
      throw new EntityExistsException("An SalesOrder with same code exists");

    }*/
    return salesOrderStorage.createSalesOrder(salesOrder);
  }

  /**
   * Update an existing SalesOrder on datasource. If the SalesOrder doesn't exit an
   * {@link javax.persistence.EntityNotFoundException} will be thrown.
   *
   * @param SalesOrder dto to update on store
   * @param username username storing SalesOrder
   * @return stored {@link org.exoplatform.timetracker.dto.SalesOrder} in datasource
   * @throws java.lang.Exception when {@link java.lang.Exception} is thrown or an error
   *           occurs while saving SalesOrder
   */
  public SalesOrder updateSalesOrder(SalesOrder SalesOrder, String username) throws Exception {
    if (SalesOrder == null) {
      throw new IllegalArgumentException("SalesOrder is mandatory");
    }
    if (StringUtils.isBlank(username)) {
      throw new IllegalArgumentException("username is mandatory");
    }
    Long SalesOrderId = SalesOrder.getId();
    if (SalesOrderId == null) {
      throw new EntityNotFoundException("SalesOrder with null id wasn't found");
    }
    SalesOrder storedSalesOrder = salesOrderStorage.getSalesOrderById(SalesOrderId);
    if (storedSalesOrder == null) {
      throw new EntityNotFoundException("SalesOrder with id " + SalesOrderId + " wasn't found");
    }
    return salesOrderStorage.updateSalesOrder(SalesOrder);
  }

  /**
   * Delete SalesOrder identified by its id and check if username has permission to
   * delete it.
   *
   * @param salesOrderId technical identifier of SalesOrder
   * @param username user currently deleting SalesOrder
   * @throws javax.persistence.EntityNotFoundException if SalesOrder wasn't found
   * @throws java.lang.IllegalAccessException if user is not allowed to delete SalesOrder
   */
  public void deleteSalesOrder(Long salesOrderId, String username) throws EntityNotFoundException, IllegalAccessException {
    if (salesOrderId == null || salesOrderId <= 0) {
      throw new IllegalArgumentException("SalesOrderId must be a positive integer");
    }
    if (StringUtils.isBlank(username)) {
      throw new IllegalArgumentException("username is mandatory");
    }

    SalesOrder storedSalesOrder = salesOrderStorage.getSalesOrderById(salesOrderId);
    if (storedSalesOrder == null) {
      throw new EntityNotFoundException("SalesOrder with id " + salesOrderId + " not found");
    }
    salesOrderStorage.deleteSalesOrder(salesOrderId);
  }

  /**
   * Retrieves the list of Activities with offset, limit and a keyword that can be
   * empty
   *
   * @return List of {@link org.exoplatform.timetracker.dto.SalesOrder} that contains the list of Activities
   */
  public List<SalesOrder> getSalesOrdersList() {
    return salesOrderStorage.getSalesOrders();
  }

}
