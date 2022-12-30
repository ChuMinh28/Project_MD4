package ra.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.entity.OrderDetails;

import java.util.List;

@Repository
public interface OrderDetailsRepo extends JpaRepository<OrderDetails,Integer> {
    List<OrderDetails> findByOrder_OrderID(int orderID);
}
