package com.elevati.web.controller;

import com.elevati.service.OrderService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/order")
public class OrderController
{
    private final OrderService orderService;

    public OrderController(OrderService orderService)
    {
        this.orderService = orderService;
    }

    // all orders are sent to the page, the search box filters them in the browser
    @GetMapping
    public String index(Model model)
    {
        model.addAttribute("orders", orderService.getOrders());
        return "order";  // order.html view file
    }

    // "Export" button, downloads orders.csv (only the searched orders if a search is typed)
    @GetMapping(path = "/export")
    public ResponseEntity<String> export(@RequestParam(required = false) String search)
    {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=orders.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(orderService.exportCsv(search));
    }

    // called by the "Sync Orders" button on the dashboard
    @GetMapping(path = "/sync")
    public ModelAndView sync()
    {
        orderService.sync();

        return new ModelAndView("redirect:/order");
    }
}
