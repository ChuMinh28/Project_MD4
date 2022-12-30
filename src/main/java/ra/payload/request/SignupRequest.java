package ra.payload.request;

import lombok.Data;
import ra.model.entity.Cart;

import java.util.Date;

import java.util.List;
import java.util.Set;
@Data
public class SignupRequest {
    private String userName;
    private String password;
    private String email;
    private String phone;
    private String address;
    private Date created;
    private boolean userStatus;
    private Set<String> listRoles;
}