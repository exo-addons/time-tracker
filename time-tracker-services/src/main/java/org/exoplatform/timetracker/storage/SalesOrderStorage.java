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
package org.exoplatform.timetracker.storage;

import java.util.List;
import java.util.stream.Collectors;

import org.gatein.api.EntityNotFoundException;

import org.exoplatform.timetracker.dao.SalesOrderDAO;
import org.exoplatform.timetracker.dto.SalesOrder;
import org.exoplatform.timetracker.entity.SalesOrderEntity;

/**
 * Storage service to access / load and save SalesOrders. This service will be
 * used , as well, to convert from JPA entity to DTO.
 */
public class SalesOrderStorage {

  private final SalesOrderDAO salesOrderDAO;
  private final ClientStorage clientStorage;

  public SalesOrderStorage(SalesOrderDAO salesOrderDAO, ClientStorage clientStorage) {
    this.salesOrderDAO = salesOrderDAO;
    this.clientStorage = clientStorage;
  }

  public SalesOrder createSalesOrder(SalesOrder salesOrder) throws Exception {
    if (salesOrder == null) {
      throw new IllegalArgumentException("SalesOrder is mandatory");
    }
    SalesOrderEntity salesOrderEntity = toEntity(salesOrder);
    salesOrder.setId(null);
    salesOrderEntity = salesOrderDAO.create(salesOrderEntity);
    return toDTO(salesOrderEntity);
  }

  public SalesOrder updateSalesOrder(SalesOrder salesOrder) throws Exception {
    if (salesOrder == null) {
      throw new IllegalArgumentException("SalesOrder is mandatory");
    }
    Long salesOrderId = salesOrder.getId();
    SalesOrderEntity salesOrderEntity = salesOrderDAO.find(salesOrder.getId());
    if (salesOrderEntity == null) {
      throw new EntityNotFoundException("SalesOrder with id " + salesOrderId + " wasn't found");
    }

    salesOrderEntity = toEntity(salesOrder);
    salesOrderEntity = salesOrderDAO.update(salesOrderEntity);

    return toDTO(salesOrderEntity);
  }

  public void deleteSalesOrder(long salesOrderId) throws EntityNotFoundException {
    if (salesOrderId <= 0) {
      throw new IllegalArgumentException("SalesOrderId must be a positive integer");
    }
    SalesOrderEntity salesOrderEntity = salesOrderDAO.find(salesOrderId);
    if (salesOrderEntity == null) {
      throw new EntityNotFoundException("SalesOrder with id " + salesOrderId + " not found");
    }
    salesOrderDAO.delete(salesOrderEntity);
  }

  public SalesOrder getSalesOrderById(long SalesOrderId) {
    if (SalesOrderId <= 0) {
      throw new IllegalArgumentException("SalesOrderId must be a positive integer");
    }
    SalesOrderEntity SalesOrderEntity = salesOrderDAO.find(SalesOrderId);
    return toDTO(SalesOrderEntity);
  }

  public List<SalesOrder> getSalesOrders() {
    List<SalesOrderEntity> applicatiions = salesOrderDAO.findAll();
    return applicatiions.stream().map(this::toDTO).collect(Collectors.toList());
  }

  public long countSalesOrders() {
    return salesOrderDAO.count();
  }

  public SalesOrder toDTO(SalesOrderEntity salesOrderEntity) {
    if (salesOrderEntity == null) {
      return null;
    }
    return new SalesOrder(salesOrderEntity.getId(),
                        salesOrderEntity.getName(),
                        salesOrderEntity.getDescription(),
                        clientStorage.toDTO(salesOrderEntity.getClientEntity()));
  }

  public SalesOrderEntity toEntity(SalesOrder salesOrder) {
    if (salesOrder == null) {
      return null;
    }
    return new SalesOrderEntity(salesOrder.getId(),
                              salesOrder.getName(),
                              salesOrder.getDescription(),
                              clientStorage.toEntity(salesOrder.getClient()));
  }

}
