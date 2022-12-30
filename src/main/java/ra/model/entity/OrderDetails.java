package ra.model.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderDetailID;
    @ManyToOne
    @JoinColumn(name = "orderID")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "productID")
    private Product product;
    @Column(name = "price")
    private float price;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "totalPrice")
    private float total;
}
