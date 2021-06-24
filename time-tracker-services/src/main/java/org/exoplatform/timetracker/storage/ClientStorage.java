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
package org.exoplatform.timetracker.storage;

import java.util.List;
import java.util.stream.Collectors;

import org.exoplatform.commons.utils.CommonsUtils;
import org.gatein.api.EntityNotFoundException;

import org.exoplatform.timetracker.dao.ClientDAO;
import org.exoplatform.timetracker.dto.Client;
import org.exoplatform.timetracker.entity.ClientEntity;

/**
 * Storage service to access / load and save Clients. This service will be
 * used , as well, to convert from JPA entity to DTO.
 *
 * @author medamine
 * @version $Id: $Id
 */
public class ClientStorage {

  private final ClientDAO clientDAO;

  /**
   * <p>Constructor for ClientStorage.</p>
   *
   * @param clientDAO a {@link org.exoplatform.timetracker.dao.ClientDAO} object.
   */
  public ClientStorage(ClientDAO clientDAO) {
    this.clientDAO = clientDAO;
  }

  /**
   * <p>createClient.</p>
   *
   * @param client a {@link org.exoplatform.timetracker.dto.Client} object.
   * @return a {@link org.exoplatform.timetracker.dto.Client} object.
   * @throws java.lang.Exception if any.
   */
  public Client createClient(Client client) throws Exception {
    if (client == null) {
      throw new IllegalArgumentException("Client is mandatory");
    }
    ClientEntity clientEntity = toEntity(client);
    client.setId(null);
    clientEntity = clientDAO.create(clientEntity);
    return toDTO(clientEntity);
  }

  /**
   * <p>updateClient.</p>
   *
   * @param client a {@link org.exoplatform.timetracker.dto.Client} object.
   * @return a {@link org.exoplatform.timetracker.dto.Client} object.
   * @throws java.lang.Exception if any.
   */
  public Client updateClient(Client client) throws Exception {
    if (client == null) {
      throw new IllegalArgumentException("Client is mandatory");
    }
    Long clientId = client.getId();
    ClientEntity clientEntity = clientDAO.find(client.getId());
    if (clientEntity == null) {
      throw new EntityNotFoundException("Client with id " + clientId + " wasn't found");
    }

    clientEntity = toEntity(client);
    clientEntity = clientDAO.update(clientEntity);

    return toDTO(clientEntity);
  }

  /**
   * <p>deleteClient.</p>
   *
   * @param clientId a long.
   * @throws org.gatein.api.EntityNotFoundException if any.
   */
  public void deleteClient(long clientId) throws EntityNotFoundException {
    if (clientId <= 0) {
      throw new IllegalArgumentException("ClientId must be a positive integer");
    }
    ClientEntity clientEntity = clientDAO.find(clientId);
    if (clientEntity == null) {
      throw new EntityNotFoundException("Client with id " + clientId + " not found");
    }
    clientDAO.delete(clientEntity);
  }

  /**
   * <p>getClientById.</p>
   *
   * @param ClientId a long.
   * @return a {@link org.exoplatform.timetracker.dto.Client} object.
   */
  public Client getClientById(long ClientId) {
    if (ClientId <= 0) {
      throw new IllegalArgumentException("ClientId must be a positive integer");
    }
    ClientEntity ClientEntity = clientDAO.find(ClientId);
    return toDTO(ClientEntity);
  }

  /**
   * <p>getClients.</p>
   *
   * @return a {@link java.util.List} object.
   */
  public List<Client> getClients() {
    List<ClientEntity> applicatiions = clientDAO.findAll();
    return applicatiions.stream().map(this::toDTO).collect(Collectors.toList());
  }

  /**
   * <p>countClients.</p>
   *
   * @return a long.
   */
  public long countClients() {
    return clientDAO.count();
  }

  /**
   * <p>toDTO.</p>
   *
   * @param clientEntity a {@link org.exoplatform.timetracker.entity.ClientEntity} object.
   * @return a {@link org.exoplatform.timetracker.dto.Client} object.
   */
  public Client toDTO(ClientEntity clientEntity) {
    if (clientEntity == null) {
      return null;
    }
    SalesOrderStorage salesOrderStorage = CommonsUtils.getService(SalesOrderStorage.class);

    return new Client(clientEntity.getId(), clientEntity.getCode(), clientEntity.getLabel(),salesOrderStorage.getSalesOrderByClienId(clientEntity.getId()));
  }

  /**
   * <p>toEntity.</p>
   *
   * @param client a {@link org.exoplatform.timetracker.dto.Client} object.
   * @return a {@link org.exoplatform.timetracker.entity.ClientEntity} object.
   */
  public ClientEntity toEntity(Client client) {
    if (client == null) {
      return null;
    }
    return new ClientEntity(client.getId(),
                              client.getCode(),
                              client.getLabel());
  }

}
