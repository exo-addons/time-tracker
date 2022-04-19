/*
 * Copyright (C) 2020 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
*/
package org.exoplatform.timetracker.storage;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.exoplatform.timetracker.dto.Client;
import org.exoplatform.timetracker.entity.ClientEntity;
import org.exoplatform.timetracker.service.BaseTimeTrackerTest;
import org.gatein.api.EntityNotFoundException;
import org.junit.Test;

import java.util.List;

public class ClientStorageTest extends BaseTimeTrackerTest {

  @Test
  public void testCreateClient() throws Exception {

    Client client = new Client(null, "test", "test");
    Client clientNew = null;
    // When
    clientNew = clientStorage.createClient(client);
    // Then
    assertNotNull(clientNew);
    // Throw
    assertThrows(IllegalArgumentException.class, () -> clientStorage.createClient(null));
  }

  @Test
  public void testUpdateClient() throws Exception {
    Client client = new Client(null, "test", "test");
    Client clientNew = clientStorage.createClient(client);
    clientNew.setCode("testUpdate");
    Client clientNotFound = new Client(5000l, "testCode", "testLabel");
    clientStorage.updateClient(clientNew);

    // Throw
    assertThrows(IllegalArgumentException.class, () -> clientStorage.updateClient(null));
    assertThrows(EntityNotFoundException.class, () -> clientStorage.updateClient(clientNotFound));
  }

  @Test
  public void testDeleteClient() throws Exception {
    Client client = new Client(null, "test", "test");
    Client clientNew = clientStorage.createClient(client);
    Long clientId = clientNew.getId();
    Long clientIdNull = 0l;
    Long clientIdNotFound = 5000l;

    // When
    clientStorage.deleteClient(clientId);

    // Throw
    assertThrows(IllegalArgumentException.class, () -> clientStorage.deleteClient(clientIdNull));
    assertThrows(EntityNotFoundException.class, () -> clientStorage.deleteClient(clientIdNotFound));

  }

  @Test
  public void testGetClientById() throws Exception {
    Client client = new Client(null, "test", "test");
    Client clientNew = clientStorage.createClient(client);
    Long clientId = clientNew.getId();
    Long clientIdNull = 0l;
    Client newClientUpdated = null;

    // When
    newClientUpdated = clientStorage.getClientById(clientId);

    // Throw
    assertNotNull(newClientUpdated);
    assertThrows(IllegalArgumentException.class, () -> clientStorage.getClientById(clientIdNull));

  }

  @Test
  public void testGetClients() {
    List<Client> clients = clientStorage.getClients();
    assertNotNull(clients);
    assertTrue(!clients.isEmpty());
  }

  @Test
  public void testCountClients() throws Exception {
    Client client = new Client(null, "test", "test");
    clientStorage.createClient(client);
    Long countClient = clientStorage.countClients();
    assertNotNull(countClient);
  }

  @Test
  public void toDTO() {
    ClientEntity clientEntityNull = null;
    Client clientDTO = clientStorage.toDTO(clientEntityNull);
    assertNull(clientDTO);
  }

  @Test
  public void toEntity() {
    Client clientNull = null;
    ClientEntity clientEntity = clientStorage.toEntity(clientNull);
    assertNull(clientEntity);
  }

}
