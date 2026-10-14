package com.elevati.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * The whole Shopify response, it looks like { "orders": [ ... ] }
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShopifyOrdersResponse
{
    private List<ShopifyOrder> orders = new ArrayList<>();

    public List<ShopifyOrder> getOrders()
    {
        return orders;
    }

    public void setOrders(List<ShopifyOrder> orders)
    {
        this.orders = orders;
    }
}
