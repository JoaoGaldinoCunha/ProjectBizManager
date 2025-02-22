package com.bizmanager.inventory.controller;

import com.bizmanager.inventory.model.TbEmployees;
import com.bizmanager.inventory.model.dto.UpdateEmployeeByAdminOrCompanyDTO;
import com.bizmanager.inventory.model.dto.UpdateEmployeeDTO;
import com.bizmanager.inventory.service.EmployeesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employees")
public class EmployeesController {

    @Autowired
    private EmployeesService employeesService;


    @PreAuthorize("hasAnyAuthority('SCOPE_Company', 'SCOPE_Admin' )")
    @GetMapping("/allCompanyEmployees/{id}")
    public ResponseEntity<?> searchAllCompanyEmployees(@PathVariable Long id) {
        return employeesService.searchAllEmployeesByCompanyId(id);
    }


    @PreAuthorize("hasAnyAuthority('SCOPE_Company', 'SCOPE_Admin','SCOPE_Employees')")
    @PostMapping("/create")
    public ResponseEntity<?> createEmployee(@RequestBody TbEmployees employee) {
        return employeesService.createEmployee(employee);
    }


    @PreAuthorize("hasAnyAuthority('SCOPE_Company', 'SCOPE_Admin')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id){
        return employeesService.deleteEmployee(id);
    }


    @PreAuthorize("hasAnyAuthority('SCOPE_Company', 'SCOPE_Admin', 'SCOPE_Employees')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable Long id, @RequestBody UpdateEmployeeDTO employee){
        return employeesService.updateEmployee(id,employee);
    }


    @PreAuthorize("hasAnyAuthority('SCOPE_Company', 'SCOPE_Admin', 'SCOPE_Employees')")
    @PutMapping("/updateByAdminOrCompany/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable Long id, @RequestBody UpdateEmployeeByAdminOrCompanyDTO employee){
        return employeesService.updateAdminOrCompany(id,employee);
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_Company', 'SCOPE_Admin', 'SCOPE_Employees')")
    @GetMapping("/{id}")
    public ResponseEntity<?> searchEmployeeById(@PathVariable Long id){
        return employeesService.searchEmployeeById(id);
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_Company', 'SCOPE_Admin', 'SCOPE_Employees')")
    @GetMapping("searchByEmployeeName/{name}/{companyId}")
    public ResponseEntity<?> searchEmployeesByName(@PathVariable String name,Long companyId){
        return employeesService.searchEmployeesByName(name,companyId);
    }
}