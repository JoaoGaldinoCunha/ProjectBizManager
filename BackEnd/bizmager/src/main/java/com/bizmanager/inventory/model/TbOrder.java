package com.bizmanager.inventory.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class TbOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsible_id", referencedColumnName = "id")
    private TbEmployees responsible;

    @Column(name = "client_cnpj", nullable = false, length = 20)
    private String clientCnpj;

    @Column(nullable = false, length = 255)
    private String destination;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false, insertable = false)
    private LocalDateTime createdAt;

    @Column (name = "status",nullable = false )
    private String status;



    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TbEmployees getResponsible() {
        return responsible;
    }

    public void setResponsible(TbEmployees responsible) {
        this.responsible = responsible;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {return status;}

    public void setStatus(String status) {this.status = status;}
}