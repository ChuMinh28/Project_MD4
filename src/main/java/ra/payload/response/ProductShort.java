package ra.payload.response;

import lombok.Data;

@Data
public class ProductShort {
    private int productID;
    private String productName;
    private float price;
    private String productTitle;
    private String catalog;
    private String image;
}
