package ra.model.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.model.entity.Product;
import ra.model.repository.ProductRepository;
import ra.model.service.ProductService;
import ra.payload.response.ProductShort;

import java.util.List;
@Service
public class ProductServiceImp implements ProductService {
    @Autowired
    ProductRepository productRepository;
    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(int productID) {
        return productRepository.findById(productID).get();
    }

    @Override
    public Product saveOrUpdate(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> searchByName(String productName) {
        return productRepository.searchByProductNameContaining(productName);
    }

    @Override
    public Page<Product> getPaging(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public void delete(int productID) {
        productRepository.deleteById(productID);
    }

    @Override
    public void deleteFromWishList(int productID) {

    }

    @Override
    public List<Product> getWishlist(int userId) {
        return productRepository.getAllWishList(userId);
    }
}
