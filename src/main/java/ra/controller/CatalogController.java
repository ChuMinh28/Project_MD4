package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ra.model.entity.Catalog;
import ra.model.service.CatalogService;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/v1/catalog")
@PreAuthorize("hasRole('ADMIN')")
public class CatalogController {
    @Autowired
    CatalogService catalogService;
    @GetMapping
    public List<Catalog> getAll(){
        return catalogService.findAll();
    }
    @GetMapping("/{catalogID}")
    public Catalog findById(@PathVariable("catalogID") int catalogID){
        return catalogService.findById(catalogID);
    }
    @PostMapping
    public Catalog create(@RequestBody Catalog catalog){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dateNow = new Date();
        String strNow = sdf.format(dateNow);
        try {
            catalog.setCreated(sdf.parse(strNow));
        }catch (Exception ex){
            ex.printStackTrace();
        }
        catalog.setCatalogStatus(true);
        return catalogService.saveOrUpdate(catalog);
    }
    @PutMapping("/{catalogID}")
    public Catalog updateCatalog(@PathVariable("catalogID") int catalogID,@RequestBody Catalog catalog){
        Catalog catalogUpdate = catalogService.findById(catalogID);
        catalogUpdate.setCatalogName(catalog.getCatalogName());
        catalogUpdate.setCatalogTitle(catalog.getCatalogTitle());
        catalogUpdate.setCatalogStatus(catalog.isCatalogStatus());
        catalogUpdate.setCreated(catalogUpdate.getCreated());
        return catalogService.saveOrUpdate(catalogUpdate);
    }
    @GetMapping ("/delete/{catalogID}")
    public void delete(@PathVariable("catalogID") int catalogID){
        Catalog catalog = catalogService.findById(catalogID);
        catalog.setCatalogStatus(false);
        catalogService.saveOrUpdate(catalog);
    }
    @GetMapping("/search")
    public List<Catalog> searchByName(@RequestParam("catalogName") String catalogName){
        return catalogService.searchByName(catalogName);
    }
    @GetMapping("/paging")
    public ResponseEntity<?> getPagingAndSortByName(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size){
        Sort.Order order;
        order=new Sort.Order(Sort.Direction.DESC,"created");
        Pageable pageable = PageRequest.of(page,size,Sort.by(order));
        Page<Catalog> catalogs = catalogService.getPaging(pageable);
        Map<String,Object> data = new HashMap<>();
        data.put("catalog",catalogs.getContent());
        data.put("total",catalogs.getSize());
        data.put("totalItems",catalogs.getTotalElements());
        data.put("totalPages",catalogs.getTotalPages());
        return  new ResponseEntity<>(data, HttpStatus.OK);
    }
}
