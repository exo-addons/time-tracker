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
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.exoplatform.timetracker.dto.Client;
import org.exoplatform.timetracker.storage.ClientStorage;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

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
	
	@Test
	public void testCreateClient() throws Exception {
		// Given
		Client client;
		Client newClient = new Client(1l, "testCode", "testLabel");
		when(clientStorage.createClient(newClient)).thenReturn(newClient);

		// When
		client = clientService.createClient(newClient);

		// Then
		verify(clientStorage, times(1)).createClient(any());

		// Throw
		try {
			client = clientService.createClient(null);
			fail("Client is mandatory");
		} catch (IllegalArgumentException e) {
			// Expected, Client is mandatory
		}
	}
	
	@Test
	public void testUpdateClient() throws Exception {
		//Given
		Client newClientUpdated;
		Client client = new Client(1l, "testCode", "testLabel");
		Client clientUpdated = new Client(1l, "testCodeUpdate", "testLabelUpdate");
		when(clientStorage.getClientById(anyLong())).thenReturn(client);
		when(clientStorage.updateClient(client)).thenReturn(clientUpdated);

		// When
		newClientUpdated = clientService.updateClient(client, "root");

		// Then
		verify(clientStorage, times(1)).updateClient(any());
		verify(clientStorage, times(1)).getClientById(anyLong());

		// Throw
		try {
			newClientUpdated = clientService.updateClient(null, "root");
			fail("Activity is mandatory");
		} catch (IllegalArgumentException e) {
			// Expected, activity remoteId is mandatory
		}
		try {
			newClientUpdated = clientService.updateClient(client, null);
			fail("Username is mandatory");
		} catch (IllegalArgumentException e) {
			// Expected, activity userName is mandatory
		}
		try {
			Long clientId = 1l;
			when(clientStorage.getClientById(clientId)).thenReturn(null);
			client = clientService.updateClient(client, "root");
			fail("Activity with id '1l' wasn't found");
		} catch (EntityNotFoundException e) {
			// Expected, activity remoteId is mandatory
		}
		try {
			newClientUpdated = client;
			newClientUpdated.setId(null);
			newClientUpdated = clientService.updateClient(newClientUpdated, "root");
			fail("Activity with null id wasn't found");
		} catch (EntityNotFoundException e) {
			// Expected, activity remoteId is mandatory
		}

	}
	@Test
	public void testDeleteclient() throws Exception {
		// Given
		Client client = new Client(1l, "testCode", "testLabel", null);
		doNothing().when(clientStorage).deleteClient(client.getId());
		when(clientStorage.getClientById(client.getId())).thenReturn(client);

		// When
		clientService.deleteClient(client.getId(), "root");

		// Then
		verify(clientStorage, times(1)).deleteClient(anyLong());
		verify(clientStorage, times(1)).getClientById(anyLong());

		// Throw
		try {
			clientService.deleteClient(client.getId(), null);
			fail("Username is mandatory");
		} catch (IllegalArgumentException e) {
			// Expected, client remoteId is mandatory
		}
		try {
			Long clientId = client.getId();
			when(clientStorage.getClientById(clientId)).thenReturn(null);
			clientService.deleteClient(clientId, "root");
			fail("Activity with id wasn't found");
		} catch (EntityNotFoundException e) {
			// Expected, client remoteId is mandatory

		}
		try {
			clientService.deleteClient(null, "root");
			fail("ActivityId must be a positive integer");
		} catch (IllegalArgumentException e) {
			// Expected, Client remoteId is mandatory
		}
	}

}
