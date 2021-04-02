package org.exoplatform.timetracker.service;

import junit.framework.TestCase;
import org.exoplatform.commons.utils.CommonsUtils;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.container.component.RequestLifeCycle;
import org.exoplatform.timetracker.dto.Client;
import org.exoplatform.timetracker.dto.Project;
import org.exoplatform.timetracker.dto.SalesOrder;
import org.junit.Test;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class SalesOrderServiceTest extends TestCase {
    SalesOrderService salesOrderService ;
    ClientService clientService;
    private PortalContainer   container;
    List<SalesOrder> tearDownOrders = new ArrayList<>();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        container = PortalContainer.getInstance();
        salesOrderService = CommonsUtils.getService(SalesOrderService.class);
        clientService = CommonsUtils.getService(ClientService.class);
        begin();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        for(SalesOrder order : tearDownOrders) {
            salesOrderService.deleteSalesOrder(order.getId(), "root");
        }
        end();
    }

    private void begin() {
        RequestLifeCycle.begin(container);
    }

    private void end() {
        RequestLifeCycle.end();
    }


    @Test
    public void testCreateSalesOrder() throws Exception {
        SalesOrder salesOrder = null;
        try {
            try {
                salesOrder = salesOrderService.createSalesOrder(salesOrder);
            } catch (IllegalArgumentException illegalArgumentException) {
                // Expected
            }
            assertNull(salesOrder);
            salesOrder = createSalesOrders(1).get(0);
            assertNotNull(salesOrder);
            assertEquals("name0", salesOrder.getName());
        } catch (Exception e) {
            fail("Failed to create a salesOrder");
        }

    }
    @Test
    public void testUpdateSalesOrder() throws Exception {
        SalesOrder salesOrder = null;
        try {
            try {
                salesOrder = salesOrderService.createSalesOrder(salesOrder);
            } catch (IllegalArgumentException illegalArgumentException) {
                // Expected
            }
            salesOrder = new SalesOrder(null, "name", "not saved yet", null);
            try {
                salesOrder = salesOrderService.updateSalesOrder(salesOrder,"root");
            } catch (EntityNotFoundException e) {
                // Expected
            }
            salesOrder = new SalesOrder(100L, "name", "not saved yet", null);
            try {
                salesOrder = salesOrderService.updateSalesOrder(salesOrder,"root");
            } catch (EntityNotFoundException e) {
                // Expected
            }
            salesOrder = createSalesOrders(1).get(0);
            assertNotNull(salesOrder);
            salesOrder.setDescription("description updated");
            try {
                salesOrder = salesOrderService.updateSalesOrder(salesOrder,null);
            } catch (IllegalArgumentException illegalArgumentException) {
                //Expected username should not be null
            }
            salesOrder = salesOrderService.updateSalesOrder(salesOrder,"root");
            assertEquals("description updated",salesOrder.getDescription());

        } catch (Exception e) {
            fail("Problem running testUpdateSalesOrder : " + e.getMessage());       }

    }

    @Test
    public void testDeleteSalesOrder() {
        try {
            try {
                salesOrderService.deleteSalesOrder(-1L, "root");
            } catch (IllegalArgumentException e) {
                // Expected
            }
            try {
                salesOrderService.deleteSalesOrder(null, "root");
            } catch (IllegalArgumentException e) {
                // Expected
            }
            try {
                salesOrderService.deleteSalesOrder(1L, null);
            } catch (IllegalArgumentException e) {
                // Expected
            }
            try {
                salesOrderService.deleteSalesOrder(10L, "root");
            } catch (EntityNotFoundException e) {
                // Expected project should exist in DB
            }
            SalesOrder order = createSalesOrders(1).get(0);
            assertEquals(1, salesOrderService.getSalesOrdersList().size());
            tearDownOrders.remove(order);
            salesOrderService.deleteSalesOrder(order.getId(), "root");
            assertEquals(0,salesOrderService.getSalesOrdersList().size());
        } catch (Exception e) {
            fail("Problem running testDeleteSalesOrder : " + e.getMessage());
        }
    }

    @Test
    public void testgetSalesOrdersList() throws Exception {
        List<SalesOrder> orders= createSalesOrders(5);
        assertEquals(5, salesOrderService.getSalesOrdersList().size());
        tearDownOrders.remove(orders.get(0));
        salesOrderService.deleteSalesOrder(orders.get(0).getId(), "root");
        assertEquals(4,salesOrderService.getSalesOrdersList().size());
    }


    private List<SalesOrder> createSalesOrders(long index) throws Exception{
        List<SalesOrder> orders = new ArrayList<>();
        Client client = new Client(null, "code", "label");
        client = clientService.createClient(client);
        for(int i = 0; i < index; i++) {
            SalesOrder order = new SalesOrder(null, "name" + i, "description" + i, client);
            order = salesOrderService.createSalesOrder(order);
            orders.add(order);
            tearDownOrders.add(order);
        }
        return orders;
    }

}
