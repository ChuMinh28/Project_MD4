package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ra.model.entity.Cart;
import ra.model.entity.Order;
import ra.model.entity.OrderDetails;
import ra.model.service.CartService;
import ra.model.service.OrderDetailsService;
import ra.model.service.OrderService;
import ra.model.service.UserService;
import ra.payload.response.CartDTO;
import ra.payload.response.MessageResponse;
import ra.payload.response.OrderDetailsResponse;
import ra.payload.response.OrderResponse;
import ra.security.CustomUserDetails;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/v1/order")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    CartService cartService;
    @Autowired
    UserService userService;
    @Autowired
    OrderDetailsService orderDetailsService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admit/{orderID}")
    public ResponseEntity<?> updateOrderStatusToAdmit(@PathVariable("orderID") int orderID) {
        try {
            Order order = orderService.findByID(orderID);
            order.setOrderStatus("Accepted");
            orderService.save(order);
            return ResponseEntity.ok(new MessageResponse("Order has been accepted"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse("Có lỗi trong quá trình xử lý vui lòng thử lại!"));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/delivery/{orderID}")
    public ResponseEntity<?> updateOrderStatusToDelivery(@PathVariable("orderID") int orderID) {
        try {
            Order order = orderService.findByID(orderID);
            order.setOrderStatus("Delivery");
            orderService.save(order);
            return ResponseEntity.ok(new MessageResponse("Order is being delivery"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse("Có lỗi trong quá trình xử lý vui lòng thử lại!"));
        }
    }

    @GetMapping("/finish/{orderID}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateOrderStatusToFinish(@PathVariable("orderID") int orderID) {
        try {
            Order order = orderService.findByID(orderID);
            order.setOrderStatus("OK");
            orderService.save(order);
            return ResponseEntity.ok(new MessageResponse("Order has been paid"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse("Có lỗi trong quá trình xử lý vui lòng thử lại!"));
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderResponse> getAllOrder() {
        List<OrderResponse> listOrder = new ArrayList<>();
        List<Order> list = orderService.getAllOrder();
        for (Order order : list) {
            if (!Objects.equals(order.getOrderStatus(), "OK")) {
                OrderResponse orderResponse = new OrderResponse();
                orderResponse.setOrderID(order.getOrderID());
                orderResponse.setOrderStatus(order.getOrderStatus());
                orderResponse.setTotalAmount(order.getTotalAmount());
                orderResponse.setCreated(order.getCreated());
                orderResponse.setUsersId(order.getOrderID());
                listOrder.add(orderResponse);
            }
        }
        return listOrder;
    }

    @GetMapping("/userOrder")
    @PreAuthorize("hasRole('USER')")
    public OrderResponse getOrderByUser() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Order order = orderService.findByUser(userDetails.getUserId());
        OrderResponse orderResponse = new OrderResponse();
        if (!Objects.equals(order.getOrderStatus(), "OK")) {
            orderResponse.setOrderID(order.getOrderID());
            orderResponse.setOrderStatus(order.getOrderStatus());
            orderResponse.setTotalAmount(order.getTotalAmount());
            orderResponse.setCreated(order.getCreated());
            orderResponse.setUsersId(order.getOrderID());
        }
        return orderResponse;
    }

    @PostMapping
    public ResponseEntity<?> saveOrder() {
        try {
            Order order = new Order();
            CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<Cart> listCart = cartService.findAllUserCartById(userDetails.getUserId());
            List<OrderDetails> listOrderDetails = new ArrayList<>();
            float totalAmount = 0f;
            for (Cart cart : listCart) {
                totalAmount += cart.getTotalPrice();
            }
            order.setOrderStatus("Pending");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dateNow = new Date();
            String strNow = sdf.format(dateNow);
            try {
                order.setCreated(sdf.parse(strNow));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            order.setUsers(userService.getUserByID(userDetails.getUserId()));
            order.setTotalAmount(totalAmount);
            orderService.save(order);
            for (Cart cart : listCart) {
                OrderDetails orderDetails = new OrderDetails();
                orderDetails.setOrder(order);
                orderDetails.setQuantity(cart.getQuantity());
                orderDetails.setProduct(cart.getProduct());
                orderDetails.setPrice(cart.getPrice());
                orderDetails.setTotal(cart.getProduct().getPrice() * cart.getQuantity());
                orderDetailsService.save(orderDetails);
                listOrderDetails.add(orderDetails);
            }
            for (Cart cart : listCart) {
                cartService.delete(cart.getCartID());
            }
            return ResponseEntity.ok(new MessageResponse("Order successfully"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse("Có lỗi trong quá trình xử lý vui lòng thử lại!"));
        }
    }

    @GetMapping("/{orderID}")
    public ResponseEntity<?> getAllOrderDetails(@PathVariable("orderID") int orderID) {
        List<OrderDetails> listOrderDetails = orderDetailsService.getAllOrderDetails(orderID);
        List<OrderDetailsResponse> list = new ArrayList<>();
        for (OrderDetails orderDetails : listOrderDetails) {
            OrderDetailsResponse orderDetailsResponse = new OrderDetailsResponse();
            orderDetailsResponse.setQuantity(orderDetails.getQuantity());
            orderDetailsResponse.setPrice(orderDetails.getProduct().getPrice());
            orderDetailsResponse.setTotal(orderDetails.getTotal());
            orderDetailsResponse.setProductName(orderDetails.getProduct().getProductName());
            list.add(orderDetailsResponse);
        }
        return ResponseEntity.ok(list);
    }
}
