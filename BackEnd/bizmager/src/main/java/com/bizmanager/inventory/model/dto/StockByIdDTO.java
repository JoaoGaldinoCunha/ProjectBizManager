package com.bizmanager.inventory.model.dto;

import java.util.Date;

public class StockByIdDTO {
    private Long id;
    private String name;
    private Long companyId;
    private Long responsibleId;
    private String responsibleName;
    private int maxCapacity;
    private String stockType;
    private Date createdAt;

    public StockByIdDTO(Long id, String name, Long companyId, Long responsibleId, String responsibleName, int maxCapacity, String stockType, Date createdAt) {
        this.id = id;
        this.name = name;
        this.companyId = companyId;
        this.responsibleId = responsibleId;
        this.responsibleName = responsibleName;
        this.maxCapacity = maxCapacity;
        this.stockType = stockType;
        this.createdAt = createdAt;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getResponsibleId() {
        return responsibleId;
    }

    public void setResponsibleId(Long responsibleId) {
        this.responsibleId = responsibleId;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public String getStockType() {
        return stockType;
    }

    public void setStockType(String stockType) {
        this.stockType = stockType;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getResponsibleName() {
        return responsibleName;
    }

    public void setResponsibleName(String responsibleName) {
        this.responsibleName = responsibleName;
    }
}
