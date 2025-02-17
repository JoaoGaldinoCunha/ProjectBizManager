package com.bizmanager.inventory.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "stock")
public class TbStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @ManyToOne
    @JoinColumn(name = "responsible_id")
    private TbEmployees responsible;

    @Column(name = "max_capacity", nullable = false)
    private Integer maxCapacity;

    @Column(name = "stock_type", nullable = false, length = 255)
    private String stockType;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private TbCompany company;




    // Getters and Setters
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

    public TbEmployees getResponsible() {
        return responsible;
    }

    public void setResponsible(TbEmployees responsible) {
        this.responsible = responsible;
    }

    public Integer getMaxCapacity() {return maxCapacity;}

    public void setMaxCapacity(Integer maxCapacity) {this.maxCapacity = maxCapacity;}

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

    public TbCompany getCompany() {return company;}

    public void setCompany(TbCompany company) {this.company = company;}

    public enum Category{
        Frigorifico,
        Geral,
        Seco,
        Liquido;


        String stockType;

        public String getStockType() {
            return stockType;
        }

        public void setStockType(String stockType) {
            this.stockType = stockType;
        }
    };
}