package ra.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderID;
    private float totalAmount;
    private String orderStatus;
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "createdDate")
    private Date created;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "UserId")
    @JsonIgnore
    private Users users;
    @OneToMany(mappedBy = "order")
    private List<OrderDetails> listOrderDetails = new ArrayList<>();
}
