package com.bizmanager.inventory.model.dto;

public class OrdersDetailsDTO {

    private Long orderId;
    private String status;
    private String clientCnpj;
    private String productName;
    private int productQuantity;
    private String productCategory;

    // Construtores, getters e setters
    public OrdersDetailsDTO(Long orderId, String status, String clientCnpj, String productName, int productQuantity, String productCategory) {
        this.orderId = orderId;
        this.status = status;
        this.clientCnpj = clientCnpj;
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.productCategory = productCategory;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClientCnpj() {
        return clientCnpj;
    }

    public void setClientCnpj(String clientCnpj) {
        this.clientCnpj = clientCnpj;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }
}
