package com.bizmanager.inventory.model.dto;
public class LowQuantityProductDTO {
        private Long productId;
        private String productCategory;
        private String productName;
        private Integer quantity;
        private Long stockId;
        private Long companyId;
        private String stockVolume;

        // Constructor
        public LowQuantityProductDTO(Long productId, String productCategory, String productName, Integer quantity, Long stockId, Long companyId, String stockVolume) {
            this.productId = productId;
            this.productCategory = productCategory;
            this.productName = productName;
            this.quantity = quantity;
            this.stockId = stockId;
            this.companyId = companyId;
            this.stockVolume = stockVolume;
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

        public String getProductCategory() {
            return productCategory;
        }

        public void setProductCategory(String productCategory) {
            this.productCategory = productCategory;
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

        public Long getCompanyId() {
            return companyId;
        }

        public void setCompanyId(Long companyId) {
            this.companyId = companyId;
        }

        public String getStockVolume() {
            return stockVolume;
        }

        public void setStockVolume(String stockVolume) {
            this.stockVolume = stockVolume;
        }
    }