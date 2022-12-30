package ra.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Data
@Table(name = "Product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProductID")
    private int productID;
    @Column(name = "ProductName", unique = true, nullable = false)
    private String productName;
    @Column(name = "Price")
    private float price;
    @Column(name = "Quantity", nullable = false)
    private int quantity;
    @Column(name = "ProductTitle", columnDefinition = "text")
    private String productTitle;
    @Column(name = "Descriptions", columnDefinition = "text")
    private String descriptions;
    @Column(name = "Image")
    private String image;
    @ManyToOne
    @JoinColumn(name = "CatalogID", nullable = false)
    private Catalog catalog;
    private boolean productStatus;
    @OneToMany(mappedBy = "product")
    private List<ProductImage> listImageLink = new ArrayList<>();
}
