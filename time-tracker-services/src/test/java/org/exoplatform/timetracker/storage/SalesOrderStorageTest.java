package org.exoplatform.timetracker.storage;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.exoplatform.timetracker.dto.Client;
import org.exoplatform.timetracker.dto.SalesOrder;
import org.exoplatform.timetracker.entity.SalesOrderEntity;
import org.exoplatform.timetracker.service.BaseTimeTrackerTest;
import org.gatein.api.EntityNotFoundException;
import org.junit.Test;

import java.util.List;

public class SalesOrderStorageTest extends BaseTimeTrackerTest {

  @Test
  public void testCreateSalesOrder() throws Exception {
    Client client = new Client(null, "test", "test");
    Client clientNew = createClient(client);
    SalesOrder salesOrder = new SalesOrder(null, "test", "test", clientNew);
    assertNotNull(salesOrder);
    SalesOrder newSalesOrder = createSalesOrder(salesOrder);
    assertNotNull(newSalesOrder);
    // Throw
    assertThrows(IllegalArgumentException.class, () -> salesOrderStorage.createSalesOrder(null));
  }

  @Test
  public void testUpdateSalesOrder() throws Exception {
    Client client = new Client(null, "test", "test");
    Client clientNew = createClient(client);
    SalesOrder salesOrder = new SalesOrder(null, "test", "test", clientNew);
    SalesOrder newSalesOrder = createSalesOrder(salesOrder);
    newSalesOrder.setDescription("testUpdate5");
    SalesOrder salesOrderNotFound = salesOrderStorage.getSalesOrders().get(0);
    salesOrderNotFound.setId(50000l);
    SalesOrder newSalesOrderUpdated = salesOrderStorage.updateSalesOrder(newSalesOrder);
    // Throw
    assertNotNull(newSalesOrderUpdated);
    assertThrows(IllegalArgumentException.class, () -> salesOrderStorage.updateSalesOrder(null));
    assertThrows(EntityNotFoundException.class, () -> salesOrderStorage.updateSalesOrder(salesOrderNotFound));
  }

  @Test
  public void testDeleteSalesOrder() throws Exception {
    Client client = new Client(null, "test", "test");
    Client clientNew = createClient(client);
    SalesOrder salesOrder = new SalesOrder(null, "test", "test", clientNew);
    SalesOrder newSalesOrder = createSalesOrder(salesOrder);
    Long salesOrderId = newSalesOrder.getId();
    Long salesOrderIdNull = 0l;
    Long salesOrderIdNotFound = 500000l;
    cleanupClients.remove(clientNew);
    cleanupSalesOrders.remove(newSalesOrder);
    salesOrderStorage.deleteSalesOrder(salesOrderId);

    // Throw
    assertThrows(IllegalArgumentException.class, () -> salesOrderStorage.deleteSalesOrder(salesOrderIdNull));
    assertThrows(EntityNotFoundException.class, () -> salesOrderStorage.deleteSalesOrder(salesOrderIdNotFound));

  }
  
  @Test
  public void testGetSalesOrderById() throws Exception {
    Client client = new Client(null, "test", "test");
    Client clientNew = createClient(client);
    SalesOrder salesOrder = new SalesOrder(null, "test", "test", clientNew);
    SalesOrder salesOrderNew = createSalesOrder(salesOrder);
    Long salesOrderId = salesOrderNew.getId();
    Long salesOrderIdNull = 0l;

    SalesOrder newSalesOrder = salesOrderStorage.getSalesOrderById(salesOrderId);

    // Throw
    assertNotNull(newSalesOrder);
    assertThrows(IllegalArgumentException.class, () -> salesOrderStorage.getSalesOrderById(salesOrderIdNull));

  }
  
  @Test
  public void getSalesOrderByClienId() throws Exception {
    Client client = new Client(null, "test", "test");
    Client clientNew = createClient(client);
    SalesOrder salesOrder = new SalesOrder(null, "test", "test", clientNew);
    SalesOrder salesOrderNew = createSalesOrder(salesOrder);
    Long salesOrderId = salesOrderNew.getId();
    Long salesOrderIdNull = 0l;
    List<SalesOrder> newSalesOrder = salesOrderStorage.getSalesOrderByClienId(salesOrderId);
    // Throw
    assertNotNull(newSalesOrder);
    assertThrows(IllegalArgumentException.class, () -> salesOrderStorage.getSalesOrderByClienId(salesOrderIdNull));
  }

  @Test
  public void testGetSalesOrders() throws Exception {
    Client client = new Client(null, "test", "test");
    Client clientNew = createClient(client);
    SalesOrder salesOrder = new SalesOrder(null, "test", "test", clientNew);
    createSalesOrder(salesOrder);
    List<SalesOrder> salesOrders = salesOrderStorage.getSalesOrders();
    assertNotNull(salesOrders);
    assertTrue(!salesOrders.isEmpty());

  }

  @Test
  public void testCountSalesOrders() throws Exception {
    Client client = new Client(null, "test", "test");
    Client clientNew = createClient(client);
    SalesOrder salesOrder = new SalesOrder(null, "test", "test", clientNew);
    createSalesOrder(salesOrder);
    Long countSalesOrder = salesOrderStorage.countSalesOrders();
    assertNotNull(countSalesOrder);
  }

  @Test
  public void toDTO() {
    SalesOrderEntity salesOrderEntityNull = null;
    SalesOrder salesOrderDTO = salesOrderStorage.toDTO(salesOrderEntityNull);
    assertNull(salesOrderDTO);
  }

  @Test
  public void toEntity() {
    SalesOrder salesOrderNull = null;
    SalesOrderEntity salesOrderEntity = salesOrderStorage.toEntity(salesOrderNull);
    assertNull(salesOrderEntity);
  }

  private SalesOrder createSalesOrder(SalesOrder salesOrder) throws Exception {
    List<SalesOrder> salesOrders = salesOrderStorage.getSalesOrders();
    assertNotNull(salesOrders);
    SalesOrder storedSalesOrder = salesOrderStorage.createSalesOrder(salesOrder);
    assertNotNull(storedSalesOrder);
    cleanupSalesOrders.add(storedSalesOrder);
    return storedSalesOrder;
  }

  private Client createClient(Client client) throws Exception {
    List<Client> clients = clientStorage.getClients();
    assertNotNull(clients);
    Client storedClient = clientStorage.createClient(client);
    assertNotNull(storedClient);
    cleanupClients.add(storedClient);
    return storedClient;
  }

}
