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
import org.exoplatform.timetracker.dto.Client;
import org.exoplatform.timetracker.dto.SalesOrder;
import org.exoplatform.timetracker.entity.ClientEntity;
import org.exoplatform.timetracker.entity.SalesOrderEntity;

/**
 * Storage service to access / load and save SalesOrders. This service will be
 * used , as well, to convert from JPA entity to DTO.
 *
 * @author medamine
 * @version $Id: $Id
 */
public class SalesOrderStorage {

  private final SalesOrderDAO salesOrderDAO;
  private final ClientStorage clientStorage;

  /**
   * <p>Constructor for SalesOrderStorage.</p>
   *
   * @param salesOrderDAO a {@link org.exoplatform.timetracker.dao.SalesOrderDAO} object.
   * @param clientStorage a {@link org.exoplatform.timetracker.storage.ClientStorage} object.
   */
  public SalesOrderStorage(SalesOrderDAO salesOrderDAO, ClientStorage clientStorage) {
    this.salesOrderDAO = salesOrderDAO;
    this.clientStorage = clientStorage;
  }

  /**
   * <p>createSalesOrder.</p>
   *
   * @param salesOrder a {@link org.exoplatform.timetracker.dto.SalesOrder} object.
   * @return a {@link org.exoplatform.timetracker.dto.SalesOrder} object.
   * @throws java.lang.Exception if any.
   */
  public SalesOrder createSalesOrder(SalesOrder salesOrder) throws Exception {
    if (salesOrder == null) {
      throw new IllegalArgumentException("SalesOrder is mandatory");
    }
    salesOrder.setId(null);
    SalesOrderEntity salesOrderEntity = toEntity(salesOrder);
    salesOrderEntity = salesOrderDAO.create(salesOrderEntity);
    return toDTO(salesOrderEntity);
  }

  /**
   * <p>updateSalesOrder.</p>
   *
   * @param salesOrder a {@link org.exoplatform.timetracker.dto.SalesOrder} object.
   * @return a {@link org.exoplatform.timetracker.dto.SalesOrder} object.
   * @throws java.lang.Exception if any.
   */
  public SalesOrder updateSalesOrder(SalesOrder salesOrder) throws Exception {
    if (salesOrder == null) {
      throw new IllegalArgumentException("SalesOrder is mandatory");
    }
    Long salesOrderId = salesOrder.getId();
    SalesOrderEntity salesOrderEntity = salesOrderDAO.find(salesOrderId);
    if (salesOrderEntity == null) {
      throw new EntityNotFoundException("SalesOrder with id " + salesOrderId + " wasn't found");
    }
    salesOrderEntity = toEntity(salesOrder);
    SalesOrder salesOrderDTO= toDTO(salesOrderEntity);
    Client client= clientStorage.getClientById(salesOrder.getClient().getId());
    salesOrderDTO.setClient(client);
    return salesOrderDTO;
  }

  /**
   * <p>deleteSalesOrder.</p>
   *
   * @param salesOrderId a long.
   * @throws org.gatein.api.EntityNotFoundException if any.
   */
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

  /**
   * <p>getSalesOrderById.</p>
   *
   * @param SalesOrderId a long.
   * @return a {@link org.exoplatform.timetracker.dto.SalesOrder} object.
   */
  public SalesOrder getSalesOrderById(long SalesOrderId) {
    if (SalesOrderId <= 0) {
      throw new IllegalArgumentException("SalesOrderId must be a positive integer");
    }
    SalesOrderEntity SalesOrderEntity = salesOrderDAO.find(SalesOrderId);
    return toDTO(SalesOrderEntity);
  }
  /**
   * <p>getSalesOrderById.</p>
   *
   * @param clientId a long.
   * @return a {@link org.exoplatform.timetracker.dto.SalesOrder} object.
   */
  public List<SalesOrder> getSalesOrderByClienId(long clientId) {
    if (clientId <= 0) {
      throw new IllegalArgumentException("SalesOrderId must be a positive integer");
    }
    List<SalesOrderEntity> salesOrderEntities = salesOrderDAO.getSalesOrderByClienId(clientId);
    return salesOrderEntities.stream().map(this::toDTOWoClient).collect(Collectors.toList());
  }

  /**
   * <p>getSalesOrders.</p>
   *
   * @return a {@link java.util.List} object.
   */
  public List<SalesOrder> getSalesOrders() {
    List<SalesOrderEntity> applicatiions = salesOrderDAO.findAll();
    return applicatiions.stream().map(this::toDTO).collect(Collectors.toList());
  }

  /**
   * <p>countSalesOrders.</p>
   *
   * @return a long.
   */
  public long countSalesOrders() {
    return salesOrderDAO.count();
  }

  /**
   * <p>toDTO.</p>
   *
   * @param salesOrderEntity a {@link org.exoplatform.timetracker.entity.SalesOrderEntity} object.
   * @return a {@link org.exoplatform.timetracker.dto.SalesOrder} object.
   */
  public SalesOrder toDTO(SalesOrderEntity salesOrderEntity) {
    if (salesOrderEntity == null) {
      return null;
    }
    return new SalesOrder(salesOrderEntity.getId(),
                        salesOrderEntity.getName(),
                        salesOrderEntity.getDescription(),
                        clientStorage.toDTO(salesOrderEntity.getClientEntity()));
  }
  public SalesOrder toDTOWoClient(SalesOrderEntity salesOrderEntity) {
    if (salesOrderEntity == null) {
      return null;
    }
    return new SalesOrder(salesOrderEntity.getId(),
                        salesOrderEntity.getName(),
                        salesOrderEntity.getDescription(),
                        salesOrderEntity.getClientEntity().getId());
  }

  /**
   * <p>toEntity.</p>
   *
   * @param salesOrder a {@link org.exoplatform.timetracker.dto.SalesOrder} object.
   * @return a {@link org.exoplatform.timetracker.entity.SalesOrderEntity} object.
   */
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
