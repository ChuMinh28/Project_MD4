package ra.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class UserResponse {
    private int userId;
    private String userName;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date created;
    private String email;
    private String phone;
    private String address;
}
