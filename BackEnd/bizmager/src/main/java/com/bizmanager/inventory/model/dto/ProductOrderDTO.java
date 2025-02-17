package com.bizmanager.inventory.model.dto;

import java.math.BigDecimal;

public class ProductOrderDTO {
    private Long productId;
    private String productName;
    private BigDecimal totalOrdered;
    private Integer quantityInStock;
    private String status;

    public ProductOrderDTO(Long productId, String productName, BigDecimal totalOrdered, Integer quantityInStock, String status) {
        this.productId = productId;
        this.productName = productName;
        this.totalOrdered = totalOrdered;
        this.quantityInStock = quantityInStock;
        this.status = status;
    }

    // Getters and Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getTotalOrdered() {
        return totalOrdered;
    }

    public void setTotalOrdered(BigDecimal totalOrdered) {
        this.totalOrdered = totalOrdered;
    }

    public Integer getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(Integer quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
