package ra.payload.response;

import lombok.Data;

@Data
public class CartDTO {
    private int cartID;
    private String productName;
    private int quantity;
    private float price;
    private float totalPrice;
}
