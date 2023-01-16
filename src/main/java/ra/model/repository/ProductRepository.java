package ra.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.model.entity.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    List<Product> searchByProductNameContaining(String productName);
    @Query(value = "select p.productID,p.productName,p.price,p.productTitle,p.image,p.catalogID" +
            "from product p join wishlist w on p.productID = w.productID where w.userId = :uID",nativeQuery = true)
    List<Product> getAllWishList(@Param("uID") int userId);
}
