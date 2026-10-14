package com.elevati.service;

import com.elevati.model.Order;
import com.elevati.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Runs against an in-memory H2 database (see src/test/resources/application.properties)
 * and reads the orders from sample-orders.json, which has 7 orders.
 */
@SpringBootTest
class OrderServiceTest
{
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp()
    {
        orderRepository.deleteAll();
    }

    @Test
    void syncSavesOrders()
    {
        assertEquals(7, orderService.sync());
        assertEquals(7, orderRepository.count());

        Order order = orderRepository.findByShopifyId(450789469L).orElse(null);
        assertNotNull(order);
        assertEquals("#1001", order.getName());
        assertEquals("Bob Norman", order.getCustomerName());
        assertEquals(0, new BigDecimal("99.00").compareTo(order.getTotalPrice()));
    }

    @Test
    void syncTwiceDoesNotDuplicate()
    {
        orderService.sync();
        orderService.sync();

        assertEquals(7, orderRepository.count());
    }

    @Test
    void searchByOrderCustomerOrEmail()
    {
        orderService.sync();

        List<Order> byName = orderService.getOrders("1003");
        assertEquals(1, byName.size());
        assertEquals("#1003", byName.get(0).getName());

        List<Order> byCustomer = orderService.getOrders("ALICE");
        assertEquals(1, byCustomer.size());
        assertEquals("#1002", byCustomer.get(0).getName());

        List<Order> byEmail = orderService.getOrders("charlie.brown@");
        assertEquals(1, byEmail.size());
        assertEquals("#1003", byEmail.get(0).getName());

        // empty search shows everything
        assertEquals(7, orderService.getOrders("  ").size());
    }

    @Test
    void exportCsv()
    {
        orderService.sync();

        String[] lines = orderService.exportCsv(null).split("\n");
        assertEquals(8, lines.length);  // header + 7 orders
        assertEquals("Order,Customer,Email,Currency,Total Price,Discount,Created At,Processed At", lines[0]);
        assertTrue(lines[1].startsWith("#1001,Bob Norman,bob.norman@hostmail.com,USD,99.00,10.00,"));

        // export with a search only has the matching order
        assertEquals(2, orderService.exportCsv("alice").split("\n").length);
    }
}
