package ra.payload.response;

import lombok.Data;
@Data
public class OrderDetailsResponse {
    private String productName;
    private float price;
    private int quantity;
    private float total;
}
