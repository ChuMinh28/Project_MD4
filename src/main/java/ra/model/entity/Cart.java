package ra.model.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CartID")
    private int cartID;
    private int quantity;
    private float price;
    private float totalPrice;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "UserId")
    private Users users;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "productID")
    private Product product;
}
