package com.bizmanager.inventory.model.dto;

import java.util.Date;

public class OrdersDTO {
    private Long orderId;
    private Date createdAt;
    private String clientCnpj;
    private String destination;
    private Long productQuantityItems;
    private String status;

    public OrdersDTO(Long orderId, Date createdAt, String clientCnpj, String destination, Long productQuantityItems, String status) {
        this.orderId = orderId;
        this.createdAt = createdAt;
        this.clientCnpj = clientCnpj;
        this.destination = destination;
        this.productQuantityItems = productQuantityItems;
        this.status = status;
    }

    // Getters and Setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getClientCnpj() {
        return clientCnpj;
    }

    public void setClientCnpj(String clientCnpj) {
        this.clientCnpj = clientCnpj;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Long getProductQuantityItems() {
        return productQuantityItems;
    }

    public void setProductQuantityItems(Long productQuantityItems) {
        this.productQuantityItems = productQuantityItems;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
