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
package org.exoplatform.timetracker.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import org.exoplatform.timetracker.dto.Client;
import org.exoplatform.timetracker.storage.ClientStorage;

public class ClientServiceTest {

  private ClientService clientService;

  private ClientStorage clientStorage;

  @Before
  public void setUp() throws Exception { // NOSONAR
    clientStorage = mock(ClientStorage.class);
    clientService = new ClientService(clientStorage);

  }

  @Test
  public void testGetClientsList() {
    Client clientDto = new Client(1l, "code", "label");
    Client clientDto1 = new Client(2l, "code1", "label1");
    Client clientDto2 = new Client(3l, "code2", "label2");
    List<Client> clients = new ArrayList<>();
    clients.add(clientDto);
    clients.add(clientDto1);
    clients.add(clientDto2);
    when(clientStorage.getClients()).thenReturn(clients);
    List<Client> clientsList = clientService.getClientsList();
    assertEquals(3, clientsList.size());
    verify(clientStorage, times(1)).getClients();
  }

}
