package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Hibernate entity mapped to the {@code dbo.OnlineShopOrders} table in the
 * {@code turingdb} database. Each instance represents a single order record.
 *
 * <p>Lombok generates the getters, setters, no-args constructor and
 * {@code toString()} so the class stays free of boilerplate.</p>
 */
@Entity(name = "ShopOrder") // entity name avoids the JPQL reserved word ORDER
@Table(name = "OnlineShopOrders", schema = "dbo")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Order {

    @Id
    @Column(name = "OrderID")
    private int orderId;

    @Column(name = "CustomerName")
    private String customerName;

    @Column(name = "ProductCategory")
    private String productCategory;

    @Column(name = "ProductName")
    private String productName;

    @Column(name = "OrderDate")
    private LocalDate orderDate;

    @Column(name = "Quantity")
    private Integer quantity;

    @Column(name = "UnitPrice")
    private BigDecimal unitPrice;

    @Column(name = "PaymentMethod")
    private String paymentMethod;

    @Column(name = "ShippingCountry")
    private String shippingCountry;

    @Column(name = "OrderStatus")
    private String orderStatus;

    /**
     * @return revenue for this single order line: {@code quantity * unitPrice}.
     */
    public BigDecimal getRevenue() {
        if (quantity == null || unitPrice == null) {
            return BigDecimal.ZERO;
        }
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
