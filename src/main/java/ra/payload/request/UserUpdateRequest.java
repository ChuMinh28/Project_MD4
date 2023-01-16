package ra.payload.request;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String phone;
    private String email;
    private String address;
}
