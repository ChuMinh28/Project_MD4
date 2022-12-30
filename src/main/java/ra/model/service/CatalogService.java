package ra.model.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.model.entity.Catalog;

import java.util.List;

public interface CatalogService {
    List<Catalog> findAll();
    Catalog findById(int catalogID);
    Catalog saveOrUpdate(Catalog catalog);
    List<Catalog> searchByName(String catalogName);
    Page<Catalog> getPaging(Pageable pageable);
}
