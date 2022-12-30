package ra.model.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.model.entity.Cart;
import ra.model.repository.CartRepository;
import ra.model.service.CartService;

import java.util.List;

@Service
public class CartServiceImp implements CartService {
    @Autowired
    CartRepository cartRepository;
    @Override
    public Cart insertCart(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public List<Cart> findAllUserCartById(int userId) {
        return cartRepository.findByUsers_UserId(userId);
    }

    @Override
    public void delete(int cartID) {
        cartRepository.deleteById(cartID);
    }

    @Override
    public Cart findCartByID(int cartID) {
        return cartRepository.findById(cartID).get();
    }
}
