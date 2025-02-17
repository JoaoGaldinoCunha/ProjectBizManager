package com.bizmanager.inventory.controller;

import com.bizmanager.inventory.model.TbStock;
import com.bizmanager.inventory.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stock")
public class StockController {

    @Autowired
    StockService stockService;

    @PreAuthorize("hasAnyAuthority('SCOPE_Company', 'SCOPE_Admin', 'SCOPE_Employees')")
    @PostMapping("/create")
    public ResponseEntity<?> createStock(@RequestBody TbStock stock) {
        return stockService.createStock(stock) ;
    }

        @PreAuthorize("hasAnyAuthority( 'SCOPE_Company')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteStock(@PathVariable Long id){
        return stockService.deleteStock(id);
    }

    @PreAuthorize("hasAnyAuthority( 'SCOPE_Company')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateStock(@PathVariable Long id, @RequestBody TbStock stock){
        return stockService.updateStock(id,stock);
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_Company', 'SCOPE_Admin', 'SCOPE_Employees')")
    @GetMapping("/allCompanyStocks/{id}")
    public ResponseEntity<?> searchAllCompanyStocks(@PathVariable Long id) {
        return stockService.searchAllStocksByCompanyId(id);
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_Company')")
    @GetMapping("/searchStockById/{id}")
    public ResponseEntity<?> searchStockById(@PathVariable Long id) {
        return stockService.searchStockById(id);
    }



}
