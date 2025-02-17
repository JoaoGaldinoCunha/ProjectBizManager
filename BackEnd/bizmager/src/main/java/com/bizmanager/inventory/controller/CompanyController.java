package com.bizmanager.inventory.controller;

import com.bizmanager.inventory.model.TbCompany;
import com.bizmanager.inventory.model.dto.UpdateCompanyDTO;
import com.bizmanager.inventory.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    private CompanyService companyService;


    @PostMapping("/create")
    public ResponseEntity<?> createCompany(@RequestBody TbCompany company) {
        return companyService.createCompany(company);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('SCOPE_Company')")
    public ResponseEntity<?> deleteCompany(@PathVariable Long id) {
        return companyService.deleteCompany(id);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('SCOPE_Company')")
    public ResponseEntity<?> updateCompany(@PathVariable Long id, @RequestBody UpdateCompanyDTO company){
        return companyService.updateCompany(id,company);
    }

    @GetMapping("/searchById/{id}")
    @PreAuthorize("hasAuthority('SCOPE_Company')")
    public ResponseEntity<?> searchComapnyById(@PathVariable Long id){
        return companyService.searchCompanyById(id);
    }
}
