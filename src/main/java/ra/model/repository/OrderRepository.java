package ra.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.entity.Order;
@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {
}
