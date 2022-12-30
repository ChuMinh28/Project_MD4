package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ra.model.entity.Cart;
import ra.model.entity.Order;
import ra.model.entity.Product;
import ra.model.entity.Users;
import ra.model.service.CartService;
import ra.model.service.OrderService;
import ra.model.service.ProductService;
import ra.model.service.UserService;
import ra.payload.request.CartRequest;
import ra.payload.response.CartDTO;
import ra.payload.response.MessageResponse;
import ra.security.CustomUserDetails;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/v1/cart")
public class CartController {
    @Autowired
    CartService cartService;
    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;
    @Autowired
    OrderService orderService;
    @GetMapping("/getCart")
    public ResponseEntity<?> getCartByUSerID() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Cart> listCart = cartService.findAllUserCartById(userDetails.getUserId());
        List<CartDTO> listCartDTO = new ArrayList<>();
        for (Cart cart : listCart) {
            CartDTO cartDTO = new CartDTO();
            cartDTO.setCartID(cart.getCartID());
            cartDTO.setQuantity(cart.getQuantity());
            cartDTO.setPrice(cart.getProduct().getPrice());
            cartDTO.setTotalPrice(cart.getTotalPrice());
            cartDTO.setProductName(cart.getProduct().getProductName());
            listCartDTO.add(cartDTO);
        }
        return ResponseEntity.ok(listCartDTO);
    }

    @PostMapping
    public ResponseEntity<?> insertCart(@RequestBody CartRequest cartRequest) {
        try {
            boolean check = false;
            Cart cart = new Cart();
            CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<Cart> listCart = cartService.findAllUserCartById(userDetails.getUserId());
            Product product = productService.findById(cartRequest.getProductID());
            Users users = userService.getUserByID(userDetails.getUserId());
            for (Cart cartExist : listCart) {
                if (cartExist.getProduct().getProductID() == product.getProductID()) {
                    cart = cartExist;
                    check = true;
                    break;
                }
            }
            if (check) {
                cart.setQuantity(cartRequest.getQuantity() + cart.getQuantity());
                cart.setTotalPrice(cart.getProduct().getPrice() * cart.getQuantity());
                cartService.insertCart(cart);
            } else {
                Cart cartNew = new Cart();
                cartNew.setQuantity(cartRequest.getQuantity());
                cartNew.setUsers(users);
                cartNew.setProduct(product);
                cartNew.setPrice(product.getPrice());
                cartNew.setTotalPrice(cartNew.getProduct().getPrice() * cartNew.getQuantity());
                cartService.insertCart(cartNew);
            }
            return ResponseEntity.ok(new MessageResponse("Add product to cart successfully"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse("Có lỗi trong quá trình xử lý vui lòng thử lại!"));
        }
    }

    @DeleteMapping("/delete/{cartID}")
    public ResponseEntity<?> deleteProductFromCart(@PathVariable("cartID") int cartID) {
        try {
            cartService.delete(cartID);
            return ResponseEntity.ok(new MessageResponse("Product has remove from cart"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse("Có lỗi trong quá trình xử lý vui lòng thử lại!"));
        }
    }

    @PutMapping("/{cartID}")
    public ResponseEntity<?> updateCart(@RequestParam("quantity") int quantity, @PathVariable("cartID") int cartID){
        try {
            Cart cart = cartService.findCartByID(cartID);
            if (quantity > 0) {
                cart.setQuantity(quantity);
                cart.setTotalPrice(cart.getProduct().getPrice() * cart.getQuantity());
                cartService.insertCart(cart);
            }else {
                cartService.delete(cartID);
            }
            return ResponseEntity.ok(new MessageResponse("Update successfully"));
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse("Có lỗi trong quá trình xử lý vui lòng thử lại!"));
        }
    }
}
