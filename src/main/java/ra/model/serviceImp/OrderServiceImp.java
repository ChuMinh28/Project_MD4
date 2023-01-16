package ra.model.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.model.entity.Order;
import ra.model.repository.OrderRepository;
import ra.model.service.OrderService;

import java.util.List;
@Service
public class OrderServiceImp implements OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Override
    public List<Order> getAllOrder() {
        return orderRepository.findAll();
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order findByID(int orderID) {
        return orderRepository.findById(orderID).get();
    }

    @Override
    public Order findByUser(int userId) {
        return orderRepository.searchOrderByUsers_UserId(userId);
    }
}
