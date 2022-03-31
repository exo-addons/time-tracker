package org.exoplatform.timetracker.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.exoplatform.timetracker.dto.Client;
import org.exoplatform.timetracker.dto.SalesOrder;
import org.exoplatform.timetracker.storage.SalesOrderStorage;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import junit.framework.TestCase;

public class SalesOrderServiceTest extends TestCase {
	private SalesOrderService salesOrderService;

	private SalesOrderStorage salesOrderStorage;

	@Before
	public void setUp() throws Exception {
		salesOrderStorage = mock(SalesOrderStorage.class);
		salesOrderService = new SalesOrderService(salesOrderStorage);
	}

	@Test
	public void testCreateSalesOrder() throws Exception {
		// Given
		SalesOrder salesOrder = null;
		Client client = new Client(1l, "test", "test");
		SalesOrder newSalesOrder = new SalesOrder(1l, "testCode", "testLabel", client);
		when(salesOrderStorage.createSalesOrder(newSalesOrder)).thenReturn(newSalesOrder);

		// When
		salesOrder = salesOrderService.createSalesOrder(newSalesOrder);

		// Then
		verify(salesOrderStorage, times(1)).createSalesOrder(any());

		// Throw
		try {
			salesOrder = salesOrderService.createSalesOrder(null);
			fail("SalesOrder is mandatory");
		} catch (IllegalArgumentException e) {
			// Expected, SalesOrder is mandatory
		}
	}

	@Test
	public void testUpdateSalesOrder() throws Exception {
		// Given
		SalesOrder newSalesOrderUpdated = null;
		Client client = new Client(1l, "test", "test");
		SalesOrder salesOrder = new SalesOrder(1l, "testCode", "testLabel", client);
		SalesOrder salesOrderUpdated = new SalesOrder(1l, "testCodeUpdated", "testLabelUpdated", client);
		when(salesOrderStorage.getSalesOrderById(anyLong())).thenReturn(salesOrder);
		when(salesOrderStorage.updateSalesOrder(salesOrder)).thenReturn(salesOrderUpdated);

		// When
		newSalesOrderUpdated = salesOrderService.updateSalesOrder(salesOrder, "root");

		// Then
		assertEquals((long) newSalesOrderUpdated.getId(), 1l);
		verify(salesOrderStorage, times(1)).updateSalesOrder(any());
		verify(salesOrderStorage, times(1)).getSalesOrderById(anyLong());

		// Throw
		try {
			newSalesOrderUpdated = salesOrderService.updateSalesOrder(null, "root");
			fail("Activity is mandatory");
		} catch (IllegalArgumentException e) {
			// Expected, activity remoteId is mandatory
		}
		try {
			newSalesOrderUpdated = salesOrderService.updateSalesOrder(salesOrder, null);
			fail("Username is mandatory");
		} catch (IllegalArgumentException e) {
			// Expected, activity userName is mandatory
		}
		try {
			Long salesOrderId = 1l;
			when(salesOrderStorage.getSalesOrderById(salesOrderId)).thenReturn(null);
			salesOrder = salesOrderService.updateSalesOrder(salesOrder, "root");
			fail("Activity with id '1l' wasn't found");
		} catch (EntityNotFoundException e) {
			// Expected, activity remoteId is mandatory
		}
		try {
			newSalesOrderUpdated = salesOrder;
			newSalesOrderUpdated.setId(null);
			newSalesOrderUpdated = salesOrderService.updateSalesOrder(newSalesOrderUpdated, "root");
			fail("Activity with null id wasn't found");
		} catch (EntityNotFoundException e) {
			// Expected, activity remoteId is mandatory
		}

	}

	@Test
	public void testDeleteSalesOrder() throws Exception {
		// Given
		Client client = new Client(1l, "test", "test");
		SalesOrder salesOrder = new SalesOrder(1l, "testCode", "testLabel", client);
		doNothing().when(salesOrderStorage).deleteSalesOrder(salesOrder.getId());
		when(salesOrderStorage.getSalesOrderById(salesOrder.getId())).thenReturn(salesOrder);

		// When
		salesOrderService.deleteSalesOrder(salesOrder.getId(), "root");

		// Then
		verify(salesOrderStorage, times(1)).deleteSalesOrder(anyLong());
		verify(salesOrderStorage, times(1)).getSalesOrderById(anyLong());

		// Throw
		try {
			salesOrderService.deleteSalesOrder(salesOrder.getId(), null);
			fail("Username is mandatory");
		} catch (IllegalArgumentException e) {
			// Expected, SalesOrder remoteId is mandatory
		}
		try {
			Long salesOrderId = salesOrder.getId();
			when(salesOrderStorage.getSalesOrderById(salesOrderId)).thenReturn(null);
			salesOrderService.deleteSalesOrder(salesOrderId, "root");
			fail("Activity with id wasn't found");
		} catch (EntityNotFoundException e) {
			// Expected, SalesOrder remoteId is mandatory

		}
		try {
			salesOrderService.deleteSalesOrder(null, "root");
			fail("ActivityId must be a positive integer");
		} catch (IllegalArgumentException e) {
			// Expected, SalesOrder remoteId is mandatory
		}
	}

	@Test
	public void testGetSalesOrdersList() {
		// Given
		Client client = new Client(1l, "test", "test");
		SalesOrder salesOrder = new SalesOrder(1l, "testCode", "testLabel", client);
		SalesOrder salesOrder1 = new SalesOrder(2l, "testCode1", "testLabel1", client);
		SalesOrder salesOrder2 = new SalesOrder(3l, "testCode2", "testLabel2", client);
		SalesOrder salesOrder3 = new SalesOrder(4l, "testCode3", "testLabel3", client);
		List<SalesOrder> salesOrders = new ArrayList<>();
		salesOrders.add(salesOrder);
		salesOrders.add(salesOrder1);
		salesOrders.add(salesOrder2);
		salesOrders.add(salesOrder3);
		when(salesOrderStorage.getSalesOrders()).thenReturn(salesOrders);

		// When
		List<SalesOrder> salesOrdersList = salesOrderService.getSalesOrdersList();

		// Then
		assertEquals(4, salesOrdersList.size());
		verify(salesOrderStorage, times(1)).getSalesOrders();
	}

	@Test
	public void testGetSalesOrderByClienId() {
		Long clientId = 1l;
		Client client = new Client(1l, "test", "test");
		SalesOrder salesOrder = new SalesOrder(1l, "testCode", "testLabel", client);
		List<SalesOrder> salesOrders = new ArrayList<>();
		salesOrders.add(salesOrder);
		when(salesOrderStorage.getSalesOrderByClienId(clientId)).thenReturn(salesOrders);
		// When
		List<SalesOrder> salesOrdersList = salesOrderService.getSalesOrderByClienId(clientId);
		// Then
		assertNotNull(clientId);
		assertEquals(1, salesOrdersList.size());
		verify(salesOrderStorage, times(1)).getSalesOrderByClienId(anyLong());
	}
}
