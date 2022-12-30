package ra.model.service;

import ra.model.entity.Cart;

import java.util.List;

public interface CartService {
    Cart insertCart(Cart cart);
    List<Cart> findAllUserCartById(int userId);
    void delete(int cartID);
    Cart findCartByID(int cartID);
}
