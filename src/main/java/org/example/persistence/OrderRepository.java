package org.example.persistence;

import org.example.entity.Order;
import org.hibernate.Session;

import java.util.List;

/**
 * Read-only data access for {@link Order} records via Hibernate.
 */
public class OrderRepository {

    /**
     * Loads every row of {@code dbo.OnlineShopOrders} as {@link Order} objects.
     *
     * @return all orders, ordered by their id
     */
    public List<Order> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session
                    .createQuery("from ShopOrder o order by o.orderId", Order.class)
                    .getResultList();
        }
    }
}
