package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ra.jwt.JwtTokenProvider;
import ra.model.entity.ERole;
import ra.model.entity.Roles;
import ra.model.entity.Users;
import ra.model.service.ProductService;
import ra.model.service.RoleService;
import ra.model.service.UserService;
import ra.payload.request.ChangePasswordRequest;
import ra.payload.request.LoginRequest;
import ra.payload.request.SignupRequest;
import ra.payload.request.UserUpdateRequest;
import ra.payload.response.JwtResponse;
import ra.payload.response.MessageResponse;
import ra.payload.response.UserResponse;
import ra.security.CustomUserDetails;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    ProductService productService;
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Users> getAll(){
        return userService.getAll();
    }
    @GetMapping("/lock/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> lock(@PathVariable("userId") int userId){
        Users users = userService.getUserByID(userId);
        users.setUserStatus(false);
        userService.saveOrUpdate(users);
        return ResponseEntity.ok(new MessageResponse("Lock User successfully"));
    }

    @GetMapping("/unlock/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> unlock(@PathVariable("userId") int userId){
        Users users = userService.getUserByID(userId);
        users.setUserStatus(true);
        userService.saveOrUpdate(users);
        return ResponseEntity.ok(new MessageResponse("Unlock User successfully"));
    }
    @GetMapping("/search/userName")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Users> searchByName(@RequestParam("userName") String userName){
        return userService.searchUserByName(userName);
    }
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        if (userService.existsByUserName(signupRequest.getUserName())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already"));
        }
        if (userService.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already"));
        }
        Users user = new Users();
        user.setUserName(signupRequest.getUserName());
        user.setPassword(encoder.encode(signupRequest.getPassword()));
        user.setEmail(signupRequest.getEmail());
        user.setPhone(signupRequest.getPhone());
        user.setAddress(signupRequest.getAddress());
        user.setUserStatus(true);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dateNow = new Date();
        String strNow = sdf.format(dateNow);
        try {
            user.setCreated(sdf.parse(strNow));
        }catch (Exception ex){
            ex.printStackTrace();
        }
        Set<String> strRoles = signupRequest.getListRoles();
        Set<Roles> listRoles = new HashSet<>();
        if (strRoles==null){
            //User quyen mac dinh
            Roles userRole = roleService.findByRoleName(ERole.ROLE_USER).orElseThrow(()->new RuntimeException("Error: Role is not found"));
            listRoles.add(userRole);
        }else {
            strRoles.forEach(role->{
                if (role.equals("admin")) {
                    Roles adminRole = roleService.findByRoleName(ERole.ROLE_ADMIN)
                            .orElseThrow(()->new RuntimeException("Error: Role is not found"));
                    listRoles.add(adminRole);
                }
            });
        }
        user.setListRoles(listRoles);
        userService.saveOrUpdate(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }
    @PostMapping("/signIn")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest){
        Users users = userService.findByUserName(loginRequest.getUserName());
        if (users.isUserStatus()){
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUserName(),loginRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            CustomUserDetails customUserDetail = (CustomUserDetails) authentication.getPrincipal();
            //Sinh JWT tra ve client
            String jwt = tokenProvider.generateToken(customUserDetail);
            //Lay cac quyen cua user
            List<String> listRoles = customUserDetail.getAuthorities().stream()
                    .map(item->item.getAuthority()).collect(Collectors.toList());
            return ResponseEntity.ok(new JwtResponse(jwt,customUserDetail.getUsername(),customUserDetail.getEmail(),
                    customUserDetail.getPhone(), customUserDetail.getAddress(), listRoles));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Account has locked"));
        }
    }

    @PutMapping("/updateUserInfo")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateUserInfo(@RequestBody UserUpdateRequest userUpdateRequest){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            Users users = userService.getUserByID(userDetails.getUserId());
            users.setEmail(userUpdateRequest.getEmail());
            users.setAddress(userUpdateRequest.getAddress());
            users.setPhone(userUpdateRequest.getPhone());
            userService.saveOrUpdate(users);
            return ResponseEntity.ok(new MessageResponse("Update successfully"));
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new MessageResponse("Có lỗi trong quá trình xử lý vui lòng thử lại!"));
        }
    }

    @PutMapping("/changePassword")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userService.getUserByID(userDetails.getUserId());
        String pass = encoder.encode(changePasswordRequest.getOldPassword());
        System.out.println(pass);
        if (users.getPassword().equals(encoder.encode(changePasswordRequest.getOldPassword()))) {
            if (changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmNewPassword())) {
                users.setPassword(encoder.encode(changePasswordRequest.getNewPassword()));
                userService.saveOrUpdate(users);
            }else {
                return ResponseEntity.badRequest().body(new MessageResponse("Mật khẩu không trùng khớp!"));
            }
        } return ResponseEntity.badRequest().body(new MessageResponse("???"));
    }

    @GetMapping("/myAccount")
    @PreAuthorize("hasRole('USER')")
    public UserResponse getUser(){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = userService.getUserByID(userDetails.getUserId());
        UserResponse userResponse = new UserResponse();
        userResponse.setUserId(users.getUserId());
        userResponse.setUserName(users.getUserName());
        userResponse.setCreated(users.getCreated());
        userResponse.setPhone(users.getPhone());
        userResponse.setAddress(users.getAddress());
        userResponse.setEmail(users.getEmail());
        return userResponse;
    }

//    @GetMapping("/addToWishList/{productID}")
//    public ResponseEntity<?> addToWishList(@PathVariable("productID") int productID){
//        try {
//            Product product = productService.findById(productID);
//            CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            Users users = userService.getUserByID(userDetails.getUserId());
//            users.getListProduct().add(product);
//            userService.saveOrUpdate(users);
//            return ResponseEntity.ok(new MessageResponse("Add product to wishlist successfully"));
//        }catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.badRequest().body(new MessageResponse("Có lỗi trong quá trình xử lý vui lòng thử lại!"));
//        }
//    }
//
//    @GetMapping("/getWishlist")
//    public List<ProductShort> getWishList(){
//        List<ProductShort> listProduct = new ArrayList<>();
//        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        List<Product> list = productService.getWishlist(userDetails.getUserId());
//        for (Product product:list) {
//            ProductShort productShort = new ProductShort();
//            productShort.setProductID(product.getProductID());
//            productShort.setProductName(product.getProductName());
//            productShort.setProductTitle(product.getProductTitle());
//            productShort.setImage(product.getImage());
//            productShort.setPrice(product.getPrice());
//            productShort.setCatalog(product.getCatalog().getCatalogName());
//            listProduct.add(productShort);
//        }
//        return listProduct;
//    }
}