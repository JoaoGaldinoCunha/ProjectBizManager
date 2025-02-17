package com.bizmanager.inventory.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "products")
public class TbProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String category;

    @Column(name = "expiration_date")
    private Date expirationDate;

    @Column(name = "purchase_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal purchasePrice;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "stock_id", nullable = false)
    private TbStock stock;






    // Getters and Setters
    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    public String getCategory() {return category;}

    public void setCategory(String category) {this.category = category;}

    public Date getExpirationDate() {return expirationDate;}

    public void setExpirationDate(Date expirationDate) {this.expirationDate = expirationDate;}

    public BigDecimal getPurchasePrice() {return purchasePrice;}

    public void setPurchasePrice(BigDecimal purchasePrice) {this.purchasePrice = purchasePrice;}

    public Integer getQuantity() {return quantity;}

    public void setQuantity(Integer quantity) {this.quantity = quantity;}

    public TbStock getStock() {return stock;}

    public void setStock(TbStock stock) {this.stock = stock;}
}