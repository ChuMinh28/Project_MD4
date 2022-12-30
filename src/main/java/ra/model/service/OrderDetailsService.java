package ra.model.service;

import ra.model.entity.OrderDetails;

import java.util.List;

public interface OrderDetailsService {
    List<OrderDetails> getAllOrderDetails(int orderID);
    OrderDetails save(OrderDetails orderDetails);
}
