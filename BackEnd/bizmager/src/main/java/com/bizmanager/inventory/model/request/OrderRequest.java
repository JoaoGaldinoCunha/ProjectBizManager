package com.bizmanager.inventory.model.request;

import com.bizmanager.inventory.model.TbOrder;
import com.bizmanager.inventory.model.dto.ProductQuantityDTO;

import java.util.List;

public class OrderRequest {
    private TbOrder order;
    private List<ProductQuantityDTO> products;

    // Getters and Setters
    public TbOrder getOrder() {
        return order;
    }

    public void setOrder(TbOrder order) {
        this.order = order;
    }

    public List<ProductQuantityDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductQuantityDTO> products) {
        this.products = products;
    }
}