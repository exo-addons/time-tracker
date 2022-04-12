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
import static org.junit.Assert.fail;

import org.exoplatform.timetracker.dao.ClientDAO;
import org.exoplatform.timetracker.dto.Client;
import org.exoplatform.timetracker.service.BaseTimeTrackerTest;
import org.junit.Test;

public class ClientStorageTest extends BaseTimeTrackerTest {

	private ClientDAO clientDAO;

	// private ClientStorage clientStorage;

	/*
	 * @Before public void setUp() { clientDAO = mock(ClientDAO.class);
	 * clientStorage = new ClientStorage(clientDAO); PortalContainer container =
	 * PortalContainer.getInstance();
	 * ExoContainerContext.setCurrentContainer(container);
	 * RequestLifeCycle.begin(container); }
	 * 
	 * @After public void tearDown() { RequestLifeCycle.end(); }
	 * 
	 * @Test public void testGetClientById() { ClientEntity clientEntity = new
	 * ClientEntity(2l, "code", "label");
	 * when(clientDAO.find(eq(2l))).thenReturn(clientEntity); Client
	 * notExistingClient = clientStorage.getClientById(1l);
	 * assertNull(notExistingClient); verify(clientDAO, times(1)).find(anyLong());
	 * Client client = clientStorage.getClientById(2l); assertNotNull(client);
	 * verify(clientDAO, times(2)).find(anyLong()); }
	 * 
	 * @Test public void testGetClients() { ClientEntity clientEntity = new
	 * ClientEntity(2l, "code", "label"); ClientEntity clientEntity1 = new
	 * ClientEntity(2l, "code", "label"); ClientEntity clientEntity2 = new
	 * ClientEntity(2l, "code", "label"); ClientEntity clientEntity3 = new
	 * ClientEntity(2l, "code", "label"); List<ClientEntity> clients = new
	 * ArrayList<>(); clients.add(clientEntity); clients.add(clientEntity1);
	 * clients.add(clientEntity2); clients.add(clientEntity3);
	 * when(clientDAO.findAll()).thenReturn(clients); List<Client> clientsList =
	 * clientStorage.getClients(); assertEquals(4,clientsList.size());
	 * verify(clientDAO, times(1)).findAll(); }
	 */

	@Test
	public void testcreateClient() throws Exception {
		Client client = new Client(null, null, null);
		Client clientTest = null;
		Client clientNew = null;
		try {
			clientTest = new Client(null, "test", "test");
			assertNotNull(clientTest);
			clientNew = clientStorage.createClient(clientTest);
		} catch (IllegalArgumentException e) {
			fail("Client is mandatory");
			//Exception
		}
		
		try {
			clientNew = clientStorage.createClient(clientTest);
			assertNotNull(clientNew);
		} catch (Exception e) {
			fail("Client is mandatory");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Client clientNewTest = null;
		try {
			clientNewTest = clientStorage.createClient(clientTest);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(clientTest.getCode(), clientNewTest.getCode());
		
		try {
			clientStorage.updateClient(client);
		}catch(IllegalArgumentException e){
			fail("aaaaaaaaaaa");
		}

	}

	@Test
	public void testUpdateClient() throws Exception {
		Client client = new Client(null, "test", "test");

		Client clientCreated = null;

			clientCreated = clientStorage.createClient(client);
		clientCreated.setCode("testUpdated");

		try {
			assertNotNull(clientCreated.getId());
		} catch (IllegalArgumentException e) {
			// Exception
			fail("Client is mandatory");
		}
		Client clientEntity = clientStorage.getClientById(clientCreated.getId());
		
		
		try {
			Client clientNew = clientStorage.updateClient(clientEntity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
