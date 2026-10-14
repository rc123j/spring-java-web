package com.elevati.service;

import com.elevati.dto.ShopifyOrder;
import com.elevati.dto.ShopifyOrdersResponse;
import com.elevati.model.Order;
import com.elevati.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * All the order logic lives here, both controllers just call this class.
 *
 * Sync flow: Shopify api (or sample-orders.json) -> ShopifyOrder dto -> Order entity -> MySQL
 */
@Service
public class OrderService
{
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private static final DateTimeFormatter CSV_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    // set in application.properties, empty in the tests so they don't call the internet
    @Value("${shopify.orders.url:}")
    private String ordersUrl;

    public OrderService(OrderRepository orderRepository, RestTemplateBuilder restTemplateBuilder, ObjectMapper objectMapper)
    {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplateBuilder.build();
        this.objectMapper = objectMapper;
    }

    public List<Order> getOrders()
    {
        return orderRepository.findAll();
    }

    /**
     * Returns all orders, or only the ones whose order name, customer name or email contains the search text.
     */
    public List<Order> getOrders(String search)
    {
        if (search == null || search.trim().isEmpty())
        {
            return getOrders();
        }

        String text = search.trim();
        return orderRepository.search(text);
    }

    /**
     * Builds a csv file (opens in Excel) from the orders, used by the Export button.
     */
    public String exportCsv(String search)
    {
        StringBuilder csv = new StringBuilder("Order,Customer,Email,Currency,Total Price,Discount,Created At,Processed At\n");

        for (Order order : getOrders(search))
        {
            csv.append(csvValue(order.getName())).append(',')
                    .append(csvValue(order.getCustomerName())).append(',')
                    .append(csvValue(order.getEmail())).append(',')
                    .append(csvValue(order.getCurrency())).append(',')
                    .append(csvValue(order.getTotalPrice())).append(',')
                    .append(csvValue(order.getTotalDiscounts())).append(',')
                    .append(csvValue(order.getCreatedAt())).append(',')
                    .append(csvValue(order.getProcessedAt())).append('\n');
        }

        return csv.toString();
    }

    // wraps a value in quotes if it has a comma or quote in it, so it doesn't break the columns
    private String csvValue(Object value)
    {
        if (value == null)
        {
            return "";
        }

        String text = value instanceof OffsetDateTime ? CSV_DATE.format((OffsetDateTime) value) : value.toString();
        if (text.contains(",") || text.contains("\"") || text.contains("\n"))
        {
            return "\"" + text.replace("\"", "\"\"") + "\"";
        }
        return text;
    }

    /**
     * Fetches the orders and saves them. Safe to run again and again,
     * an order that already exists is updated instead of added twice.
     *
     * @return number of orders received from Shopify
     */
    @Transactional
    public int sync()
    {
        List<ShopifyOrder> shopifyOrders = fetchOrders();

        for (ShopifyOrder shopifyOrder : shopifyOrders)
        {
            // update the existing row if the order was synced before
            Order order = orderRepository.findByShopifyId(shopifyOrder.getId()).orElseGet(Order::new);
            order.setShopifyId(shopifyOrder.getId());
            order.setName(shopifyOrder.getName());
            order.setCustomerName(customerName(shopifyOrder.getCustomer()));
            order.setEmail(shopifyOrder.getEmail());
            order.setCurrency(shopifyOrder.getCurrency());
            order.setTotalPrice(shopifyOrder.getTotalPrice());
            order.setTotalDiscounts(shopifyOrder.getTotalDiscounts());
            order.setCreatedAt(shopifyOrder.getCreatedAt());
            order.setProcessedAt(shopifyOrder.getProcessedAt());
            orderRepository.save(order);
        }

        log.info("Synced {} orders", shopifyOrders.size());
        return shopifyOrders.size();
    }

    // "Alice" + "Smith" -> "Alice Smith", null if the order has no customer
    private String customerName(ShopifyOrder.Customer customer)
    {
        if (customer == null)
        {
            return null;
        }

        String first = customer.getFirstName() != null ? customer.getFirstName() : "";
        String last = customer.getLastName() != null ? customer.getLastName() : "";
        return (first + " " + last).trim();
    }

    private List<ShopifyOrder> fetchOrders()
    {
        if (!ordersUrl.isEmpty())
        {
            try
            {
                // Jackson converts the json response into ShopifyOrdersResponse for us
                ShopifyOrdersResponse response = restTemplate.getForObject(ordersUrl, ShopifyOrdersResponse.class);
                if (response != null)
                {
                    return response.getOrders();
                }
            }
            catch (RestClientException e)
            {
                log.warn("Could not fetch orders from {} ({}), using sample-orders.json", ordersUrl, e.getMessage());
            }
        }

        // the test endpoint is not available, so fall back to the sample Shopify response
        try (InputStream in = new ClassPathResource("sample-orders.json").getInputStream())
        {
            return objectMapper.readValue(in, ShopifyOrdersResponse.class).getOrders();
        }
        catch (IOException e)
        {
            throw new UncheckedIOException("Could not read sample-orders.json", e);
        }
    }
}
