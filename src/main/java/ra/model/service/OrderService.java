package ra.model.service;

import ra.model.entity.Order;

import java.util.List;

public interface OrderService {
    List<Order> getAllOrder();
    Order save(Order order);
    Order findByID(int orderID);
    Order findByUser(int userId);
}
