package ra.payload.request;

import lombok.Data;
@Data
public class CartRequest {
    private int quantity;
    private int productID;
}
