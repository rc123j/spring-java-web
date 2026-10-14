package com.elevati.api.controller;

import com.elevati.model.Order;
import com.elevati.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

/**
 * Same orders as the /order page, returned as json.
 */
@RestController
@RequestMapping(path = "/api/v1/orders")
@RequestScope
public class OrderApiController
{
    private final OrderService orderService;

    public OrderApiController(OrderService orderService)
    {
        this.orderService = orderService;
    }

    // GET /api/v1/orders or /api/v1/orders?search=bob
    @GetMapping
    public Iterable<Order> getAll(@RequestParam(required = false) String search)
    {
        return orderService.getOrders(search);
    }

}
