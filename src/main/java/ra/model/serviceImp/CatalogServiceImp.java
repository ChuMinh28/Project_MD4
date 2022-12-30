package ra.model.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ra.model.entity.Catalog;
import ra.model.repository.CatalogRepository;
import ra.model.service.CatalogService;

import java.sql.SQLException;
import java.util.List;
@Service
@Transactional(rollbackFor = SQLException.class)
public class CatalogServiceImp implements CatalogService {
    @Autowired
    CatalogRepository catalogRepository;
    @Override
    public List<Catalog> findAll() {
        return catalogRepository.findAll();
    }

    @Override
    public Catalog findById(int catalogID) {
        return catalogRepository.findById(catalogID).get();
    }

    @Override
    public Catalog saveOrUpdate(Catalog catalog) {
        return catalogRepository.save(catalog);
    }

    @Override
    public List<Catalog> searchByName(String catalogName) {
        return catalogRepository.searchCatalogByCatalogNameContains(catalogName);
    }

    @Override
    public Page<Catalog> getPaging(Pageable pageable) {
        return catalogRepository.findAll(pageable);
    }
}
