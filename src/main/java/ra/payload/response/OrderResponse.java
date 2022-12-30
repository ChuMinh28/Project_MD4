package ra.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

@Data
public class OrderResponse {
    private int orderID;
    private float totalAmount;
    private int orderStatus;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date created;
    private int usersId;
}
