package com.bizmanager.inventory.model.dto;

import java.math.BigDecimal;
import java.util.Date;

public class ProductByIdDTO {

        private Long productId;
        private String productName;
        private String description;
        private String category;
        private Date expirationDate;
        private BigDecimal purchasePrice;
        private Integer quantity;
        private Long stockId;
        private String stockName;

        public ProductByIdDTO(Long productId, String productName, String description, String category, Date expirationDate, BigDecimal purchasePrice, Integer quantity, Long stockId, String stockName) {
            this.productId = productId;
            this.productName = productName;
            this.description = description;
            this.category = category;
            this.expirationDate = expirationDate;
            this.purchasePrice = purchasePrice;
            this.quantity = quantity;
            this.stockId = stockId;
            this.stockName = stockName;
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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public Date getExpirationDate() {
            return expirationDate;
        }

        public void setExpirationDate(Date expirationDate) {
            this.expirationDate = expirationDate;
        }

        public BigDecimal getPurchasePrice() {
            return purchasePrice;
        }

        public void setPurchasePrice(BigDecimal purchasePrice) {
            this.purchasePrice = purchasePrice;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public Long getStockId() {
            return stockId;
        }

        public void setStockId(Long stockId) {
            this.stockId = stockId;
        }

        public String getStockName() {
            return stockName;
        }

        public void setStockName(String stockName) {
            this.stockName = stockName;
        }

}
