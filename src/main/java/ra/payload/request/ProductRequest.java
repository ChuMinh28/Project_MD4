package ra.payload.request;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;
@Data
public class ProductRequest {
    private int productID;
    private String productName;
    private float price;
    private int quantity;
    private String productTitle;
    private String descriptions;
    private String image;
    private int catalogID;
    private List<String> listImageLink = new ArrayList<>();
    private boolean productStatus;
}
