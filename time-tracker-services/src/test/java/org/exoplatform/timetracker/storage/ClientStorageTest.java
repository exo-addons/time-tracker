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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.component.RequestLifeCycle;
import org.exoplatform.timetracker.dao.ClientDAO;
import org.exoplatform.timetracker.dto.Client;
import org.exoplatform.timetracker.entity.ClientEntity;

public class ClientStorageTest {

  private ClientDAO     clientDAO;

  private ClientStorage clientStorage;

  @Before
  public void setUp() {
    clientDAO = mock(ClientDAO.class);
    clientStorage = new ClientStorage(clientDAO);
    PortalContainer container = PortalContainer.getInstance();
    ExoContainerContext.setCurrentContainer(container);
    RequestLifeCycle.begin(container);
  }

  @After
  public void tearDown() {
    RequestLifeCycle.end();
  }

  @Test
  public void testGetClientById() {
    ClientEntity clientEntity = new ClientEntity(2l, "code", "label");
    when(clientDAO.find(eq(2l))).thenReturn(clientEntity);
    Client notExistingClient = clientStorage.getClientById(1l);
    assertNull(notExistingClient);
    verify(clientDAO, times(1)).find(anyLong());
    Client client = clientStorage.getClientById(2l);
    assertNotNull(client);
    verify(clientDAO, times(2)).find(anyLong());
  }

  @Test
  public void testGetClients() {
    ClientEntity clientEntity = new ClientEntity(2l, "code", "label");
    ClientEntity clientEntity1 = new ClientEntity(2l, "code", "label");
    ClientEntity clientEntity2 = new ClientEntity(2l, "code", "label");
    ClientEntity clientEntity3 = new ClientEntity(2l, "code", "label");
    List<ClientEntity> clients = new ArrayList<>();
    clients.add(clientEntity);
    clients.add(clientEntity1);
    clients.add(clientEntity2);
    clients.add(clientEntity3);
    when(clientDAO.findAll()).thenReturn(clients);
    List<Client> clientsList = clientStorage.getClients();
    assertEquals(4,clientsList.size());
    verify(clientDAO, times(1)).findAll();
  }

}
