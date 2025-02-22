package com.bizmanager.inventory.controller;

import com.bizmanager.inventory.model.request.OrderRequest;
import com.bizmanager.inventory.model.request.StatusRequest;
import com.bizmanager.inventory.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PreAuthorize("hasAnyAuthority('SCOPE_Company', 'SCOPE_Admin', 'SCOPE_Employees')")
    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.createOrder(orderRequest.getOrder(), orderRequest.getProducts());
    }


    @PreAuthorize("hasAnyAuthority('SCOPE_Company', 'SCOPE_Admin', 'SCOPE_Employees')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> editStausOrder(@PathVariable Long id,@RequestBody StatusRequest statusRequest) {
        String status=statusRequest.getStatus();
        return orderService.updateOrderById(id,status);
    }


    @PreAuthorize("hasAnyAuthority('SCOPE_Company', 'SCOPE_Admin', 'SCOPE_Employees')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteOrderById (@PathVariable Long id) {
        return orderService.deleteOrder(id);
    }


    @PreAuthorize("hasAnyAuthority('SCOPE_Company', 'SCOPE_Admin', 'SCOPE_Employees')")
    @GetMapping("/allOrders/{id}")
    public  ResponseEntity<?> searchAllOrders(@PathVariable Long id){return orderService.searchAllOrders(id);}


    @PreAuthorize("hasAnyAuthority('SCOPE_Company', 'SCOPE_Admin', 'SCOPE_Employees')")
    @GetMapping("/odersById/{orderId}/{companyId}")
    public  ResponseEntity<?> searchOdersById(@PathVariable Long orderId,@PathVariable Long companyId){return orderService.searchOdersById(orderId,companyId);}


}
