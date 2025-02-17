package com.bizmanager.inventory.controller;

import com.bizmanager.inventory.model.TbProduct;
import com.bizmanager.inventory.model.dto.ProductQuantityDTO;
import com.bizmanager.inventory.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @PreAuthorize("hasAnyAuthority('SCOPE_Company', 'SCOPE_Admin', 'SCOPE_Employees')")
    @PostMapping("/create")
    public ResponseEntity<?> createStock(@RequestBody TbProduct product) {
        return productService.createProduct(product) ;
    }


    @PreAuthorize("hasAnyAuthority('SCOPE_Company', 'SCOPE_Admin', 'SCOPE_Employees')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteStock(@PathVariable Long id){
        return productService.deleteProduct(id);
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_Company', 'SCOPE_Admin', 'SCOPE_Employees')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody TbProduct product){
        return productService.updateProduct(id,product);
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_Company', 'SCOPE_Admin', 'SCOPE_Employees')")
    @PutMapping("/update/quantity")
    public ResponseEntity<?> updateProductQuantity( @RequestBody ProductQuantityDTO productQuantityDTO){
        return productService.updateProductQuantity(productQuantityDTO);
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_Company', 'SCOPE_Admin', 'SCOPE_Employees')")
    @GetMapping("/searchAllProductsByStockId/{id}")
    public ResponseEntity<?> searchAllProductsByCompanyId(@PathVariable Long id) {
        return productService.searchAllProductsByStockId(id);
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_Company', 'SCOPE_Admin', 'SCOPE_Employees')")
    @GetMapping("/searchByProductName/{stockId}/{name}")
    public ResponseEntity<?> searchProductsByName(@PathVariable String name,@PathVariable Long stockId) {
        return productService.searchProductsByName(name,stockId);
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_Company', 'SCOPE_Admin', 'SCOPE_Employees')")
    @GetMapping("/searchProductsMostOrdered/{id}")
    public ResponseEntity<?> searchProductsMostOrdered(@PathVariable Long id) {
        return productService.searchProductsMostOrdered(id);
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_Company', 'SCOPE_Admin', 'SCOPE_Employees')")
    @GetMapping("/searchProductsWithLowQuantity/{id}")
    public ResponseEntity<?> searchProductsWithLowQuantity(@PathVariable Long id) {
        return productService.searchProductsWithLowQuantity(id);
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_Company', 'SCOPE_Admin', 'SCOPE_Employees')")
    @GetMapping("/searchProductById/{id}")
    public ResponseEntity<?>  searchProductsById(@PathVariable Long id){
        return  productService.searchProductById(id);
    }
    @PreAuthorize("hasAnyAuthority('SCOPE_Company', 'SCOPE_Admin', 'SCOPE_Employees')")
    @GetMapping("/searchAllProductsByCompanyId/{id}")
    public ResponseEntity<?>  searchAllProductsByComapnyId(@PathVariable Long id){
        return  productService.searchAllProductsByComapnyId(id);
    }


}
