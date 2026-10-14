package com.elevati.repository;

import com.elevati.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Database access for the orders table.
 * JpaRepository already gives us save, findAll, count, deleteAll etc.
 * findByShopifyId gets its SQL from the method name, search has its query written by hand.
 */
public interface OrderRepository extends JpaRepository<Order, Long>
{
    // used by the sync to check if a Shopify order is already saved
    Optional<Order> findByShopifyId(Long shopifyId);

    // used by ?search=, matches order name, customer name or email, e.g. "1003" or "alice"
    @Query("select o from Order o where lower(o.name) like lower(concat('%', :text, '%'))"
            + " or lower(o.customerName) like lower(concat('%', :text, '%'))"
            + " or lower(o.email) like lower(concat('%', :text, '%'))")
    List<Order> search(@Param("text") String text);
}
