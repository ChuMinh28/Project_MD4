package ra.model.service;

import ra.model.entity.ProductImage;

public interface ImageService {
    ProductImage save(ProductImage productImage);
    void delete(int imageID);
}
