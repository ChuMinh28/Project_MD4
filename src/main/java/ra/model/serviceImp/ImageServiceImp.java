package ra.model.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.model.entity.ProductImage;
import ra.model.repository.ImageRepository;
import ra.model.service.ImageService;
@Service
public class ImageServiceImp implements ImageService {
    @Autowired
    ImageRepository imageRepository;
    @Override
    public ProductImage save(ProductImage productImage) {
        return imageRepository.save(productImage);
    }

    @Override
    public void delete(int imageID) {
        imageRepository.deleteById(imageID);
    }
}
