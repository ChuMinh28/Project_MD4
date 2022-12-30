package ra.model.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.model.entity.OrderDetails;
import ra.model.repository.OrderDetailsRepo;
import ra.model.service.OrderDetailsService;

import java.util.List;
@Service
public class OrderDetailsServiceImp implements OrderDetailsService {
    @Autowired
    OrderDetailsRepo orderDetailsRepo;
    @Override
    public List<OrderDetails> getAllOrderDetails(int orderID) {
        return orderDetailsRepo.findByOrder_OrderID(orderID);
    }

    @Override
    public OrderDetails save(OrderDetails orderDetails) {
        return orderDetailsRepo.save(orderDetails);
    }
}
