package ra.model.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.model.entity.Product;
import ra.payload.response.ProductShort;

import java.util.List;

public interface ProductService {
    List<Product> findAll();
    Product findById(int productID);
    Product saveOrUpdate(Product product);
    List<Product> searchByName(String productName);
    Page<Product> getPaging(Pageable pageable);
    void delete(int productID);
    void deleteFromWishList(int productID);
    List<Product> getWishlist(int userId);
}
