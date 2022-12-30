package ra.payload.response;

import lombok.Data;
import ra.model.entity.ProductImage;

import java.util.ArrayList;
import java.util.List;
@Data
public class ProductDTO {
    private String productName;
    private float price;
    private int quantity;
    private String productTitle;
    private String descriptions;
    private String image;
    private String catalog;
    private List<ProductImage> listImageLink = new ArrayList<>();
    private boolean productStatus;
}
