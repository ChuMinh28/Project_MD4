package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ra.model.entity.Catalog;
import ra.model.entity.Product;
import ra.model.entity.ProductImage;
import ra.model.entity.Users;
import ra.model.service.CatalogService;
import ra.model.service.ImageService;
import ra.model.service.ProductService;
import ra.model.service.UserService;
import ra.payload.request.ProductRequest;
import ra.payload.response.MessageResponse;
import ra.payload.response.ProductDTO;
import ra.payload.response.ProductShort;
import ra.security.CustomUserDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/v1/product")
public class ProductController {
    @Autowired
    ProductService productService;
    @Autowired
    CatalogService catalogService;
    @Autowired
    ImageService imageService;

    @GetMapping("/delete/{productID}")
    public ResponseEntity<?> delete(@PathVariable("productID") int productID){
        Product product = productService.findById(productID);
        product.setProductStatus(false);
        productService.saveOrUpdate(product);
        return ResponseEntity.ok(new MessageResponse("Delete product successfully"));
    }
    @GetMapping
    public List<ProductShort> getAll() {
        List<ProductShort> listProductShort = new ArrayList<>();
        List<Product> listPro = productService.findAll();
        for (Product product : listPro) {
            ProductShort productShort = new ProductShort();
            productShort.setProductID(product.getProductID());
            productShort.setProductName(product.getProductName());
            productShort.setProductTitle(product.getProductTitle());
            productShort.setImage(product.getImage());
            productShort.setPrice(product.getPrice());
            productShort.setCatalog(product.getCatalog().getCatalogName());
            listProductShort.add(productShort);
        }
        return listProductShort;
    }

    @GetMapping("/detail/{productID}")
    public ProductDTO findById(@PathVariable("productID") int productID) {
        Product product = productService.findById(productID);
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductName(product.getProductName());
        productDTO.setProductTitle(product.getProductTitle());
        productDTO.setDescriptions(product.getDescriptions());
        productDTO.setPrice(product.getPrice());
        productDTO.setQuantity(product.getQuantity());
        productDTO.setImage(product.getImage());
        productDTO.setProductStatus(product.isProductStatus());
        productDTO.setCatalog(product.getCatalog().getCatalogName());
        productDTO.getListImageLink().addAll(product.getListImageLink());
        return productDTO;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ProductRequest product) {
        try {
            Catalog catalog = catalogService.findById(product.getCatalogID());
            Product proNew = new Product();
            proNew.setCatalog(catalog);
            proNew.setProductName(product.getProductName());
            proNew.setProductTitle(product.getProductTitle());
            proNew.setPrice(product.getPrice());
            proNew.setQuantity(product.getQuantity());
            proNew.setImage(product.getImage());
            proNew.setDescriptions(product.getDescriptions());
            proNew.setProductStatus(true);
            productService.saveOrUpdate(proNew);
            for (String str : product.getListImageLink()) {
                ProductImage image = new ProductImage();
                image.setUrlImage(str);
                image.setProduct(proNew);
                imageService.save(image);
            }
            return ResponseEntity.ok(new MessageResponse("Create product successfully"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse("Có lỗi trong quá trình xử lý vui lòng thử lại!"));
        }

    }

    @PutMapping("/{productID}")
    public ResponseEntity<?> update(@PathVariable("productID") int productID, @RequestBody ProductRequest product) {
        try {
            Catalog catalog = catalogService.findById(product.getCatalogID());
            Product productUpdate = productService.findById(productID);
            productUpdate.setProductName(product.getProductName());
            productUpdate.setProductTitle(product.getProductTitle());
            productUpdate.setPrice(product.getPrice());
            productUpdate.setQuantity(product.getQuantity());
            productUpdate.setImage(product.getImage());
            productUpdate.setDescriptions(product.getDescriptions());
            productUpdate.setProductStatus(product.isProductStatus());
            productUpdate.setCatalog(catalog);
            for (ProductImage image : productUpdate.getListImageLink()) {
                imageService.delete(image.getIdImage());
            }
            for (String str : product.getListImageLink()) {
                ProductImage image = new ProductImage();
                image.setUrlImage(str);
                image.setProduct(productUpdate);
                imageService.save(image);
            }
            return ResponseEntity.ok(new MessageResponse("Update product successfully"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse("Có lỗi trong quá trình xử lý vui lòng thử lại!"));
        }
    }
    @GetMapping("/{productName}")
    public List<Product> searchByName(@PathVariable("productName") String productName){
        return productService.searchByName(productName);
    }

    @GetMapping("/paging")
    public ResponseEntity<?> getPaging(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productService.getPaging(pageable);
        Map<String, Object> data = new HashMap<>();
        data.put("products", products.getContent());
        data.put("total", products.getSize());
        data.put("totalItems", products.getTotalElements());
        data.put("totalPages", products.getTotalPages());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
