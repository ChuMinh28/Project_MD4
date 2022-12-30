package ra.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
@Entity
@Table(name = "Catalog")
@Data
public class Catalog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CatalogID")
    private int catalogID;
    @Column(name = "CatalogName", nullable = false, unique = true)
    private String catalogName;
    @Column(name = "CatalogTitle", columnDefinition = "text")
    private String catalogTitle;
    @Column(name = "Created")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date created;
    @Column(name = "CatalogStatus")
    private boolean catalogStatus;
}