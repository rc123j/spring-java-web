package com.elevati.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * One order as Shopify sends it. Only the fields we save are mapped,
 * the rest (line_items, financial_status, ...) is ignored.
 * Kept separate from the Order entity so a change in Shopify's json doesn't touch the table.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShopifyOrder
{
    private Long id;

    private String name;

    private String email;

    private String currency;

    // Shopify uses snake_case names, @JsonProperty maps them to our fields
    @JsonProperty("total_price")
    private BigDecimal totalPrice;

    @JsonProperty("total_discounts")
    private BigDecimal totalDiscounts;

    @JsonProperty("created_at")
    private OffsetDateTime createdAt;

    @JsonProperty("processed_at")
    private OffsetDateTime processedAt;

    private Customer customer;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getCurrency()
    {
        return currency;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
    }

    public BigDecimal getTotalPrice()
    {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice)
    {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTotalDiscounts()
    {
        return totalDiscounts;
    }

    public void setTotalDiscounts(BigDecimal totalDiscounts)
    {
        this.totalDiscounts = totalDiscounts;
    }

    public OffsetDateTime getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getProcessedAt()
    {
        return processedAt;
    }

    public void setProcessedAt(OffsetDateTime processedAt)
    {
        this.processedAt = processedAt;
    }

    public Customer getCustomer()
    {
        return customer;
    }

    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }

    // the "customer": { ... } object inside an order, we only need the name
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Customer
    {
        @JsonProperty("first_name")
        private String firstName;

        @JsonProperty("last_name")
        private String lastName;

        public String getFirstName()
        {
            return firstName;
        }

        public void setFirstName(String firstName)
        {
            this.firstName = firstName;
        }

        public String getLastName()
        {
            return lastName;
        }

        public void setLastName(String lastName)
        {
            this.lastName = lastName;
        }
    }
}
