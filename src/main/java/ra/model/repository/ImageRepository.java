package ra.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.entity.ProductImage;
@Repository
public interface ImageRepository extends JpaRepository<ProductImage,Integer> {
}
