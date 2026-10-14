package com.elevati.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * One row in the orders table. Hibernate creates the table from this class
 * (spring.jpa.hibernate.ddl-auto=update).
 */
@Entity
@Table(name = "orders")  // "order" is a reserved word in MySQL
public class Order
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // id of the order in Shopify, unique so the same order can't be saved twice
    @Column(unique = true, nullable = false)
    private Long shopifyId;

    // Shopify order name, e.g. "#1001"
    private String name;

    // first + last name of the Shopify customer
    private String customerName;

    private String email;

    private String currency;

    // BigDecimal for money, double can give rounding errors
    @Column(precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalDiscounts;

    // Shopify dates come with a timezone, e.g. 2023-10-12T10:15:30-04:00
    private OffsetDateTime createdAt;

    private OffsetDateTime processedAt;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getShopifyId()
    {
        return shopifyId;
    }

    public void setShopifyId(Long shopifyId)
    {
        this.shopifyId = shopifyId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getCustomerName()
    {
        return customerName;
    }

    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
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
}
