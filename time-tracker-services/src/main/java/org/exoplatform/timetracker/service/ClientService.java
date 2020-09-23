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
package org.exoplatform.timetracker.service;

import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang.StringUtils;

import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.timetracker.dto.Client;
import org.exoplatform.timetracker.storage.ClientStorage;

/**
 * A Service to access and store Activities
 *
 * @author medamine
 * @version $Id: $Id
 */
public class ClientService {

  private static final Log      LOG = ExoLogger.getLogger(ClientService.class);

  private final ClientStorage clientStorage;

  /**
   * <p>Constructor for ClientService.</p>
   *
   * @param clientStorage a {@link org.exoplatform.timetracker.storage.ClientStorage} object.
   */
  public ClientService(ClientStorage clientStorage) {
    this.clientStorage = clientStorage;

  }

  /**
   * Create new Client that will be available for all users. If the Client
   * already exits an {@link javax.persistence.EntityExistsException} will be thrown.
   *
   * @param client Client to create
   * @return stored {@link org.exoplatform.timetracker.dto.Client} in datasource
   * @throws java.lang.Exception when Client already exists or an error occurs while
   *           creating Client or its attached image
   */
  public Client createClient(Client client) throws Exception {
    if (client == null) {
      throw new IllegalArgumentException("Client is mandatory");
    }
/*    Client existingClient = clientStorage.getClientById(client.getId());
    if (existingClient != null) {
      throw new EntityExistsException("An Client with same code exists");

    }*/
    return clientStorage.createClient(client);
  }

  /**
   * Update an existing Client on datasource. If the Client doesn't exit an
   * {@link javax.persistence.EntityNotFoundException} will be thrown.
   *
   * @param Client dto to update on store
   * @param username username storing Client
   * @return stored {@link org.exoplatform.timetracker.dto.Client} in datasource
   * @throws java.lang.Exception when {@link java.lang.Exception} is thrown or an error
   *           occurs while saving Client
   */
  public Client updateClient(Client Client, String username) throws Exception {
    if (Client == null) {
      throw new IllegalArgumentException("Client is mandatory");
    }
    if (StringUtils.isBlank(username)) {
      throw new IllegalArgumentException("username is mandatory");
    }
    Long ClientId = Client.getId();
    if (ClientId == null) {
      throw new EntityNotFoundException("Client with null id wasn't found");
    }
    Client storedClient = clientStorage.getClientById(ClientId);
    if (storedClient == null) {
      throw new EntityNotFoundException("Client with id " + ClientId + " wasn't found");
    }
    return clientStorage.updateClient(Client);
  }

  /**
   * Delete Client identified by its id and check if username has permission to
   * delete it.
   *
   * @param clientId technical identifier of Client
   * @param username user currently deleting Client
   * @throws javax.persistence.EntityNotFoundException if Client wasn't found
   * @throws java.lang.IllegalAccessException if user is not allowed to delete Client
   */
  public void deleteClient(Long clientId, String username) throws EntityNotFoundException, IllegalAccessException {
    if (clientId == null || clientId <= 0) {
      throw new IllegalArgumentException("ClientId must be a positive integer");
    }
    if (StringUtils.isBlank(username)) {
      throw new IllegalArgumentException("username is mandatory");
    }

    Client storedClient = clientStorage.getClientById(clientId);
    if (storedClient == null) {
      throw new EntityNotFoundException("Client with id " + clientId + " not found");
    }
    clientStorage.deleteClient(clientId);
  }

  /**
   * Retrieves the list of Activities with offset, limit and a keyword that can be
   * empty
   *
   * @return List of {@link org.exoplatform.timetracker.dto.Client} that contains the list of Activities
   */
  public List<Client> getClientsList() {
    return clientStorage.getClients();
  }

}
