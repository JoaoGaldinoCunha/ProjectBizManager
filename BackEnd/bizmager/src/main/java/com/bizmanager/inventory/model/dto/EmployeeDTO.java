package com.bizmanager.inventory.model.dto;

import com.bizmanager.inventory.model.TbCompany;
import com.bizmanager.inventory.model.TbRoles;

import java.util.Date;

public class EmployeeDTO {
    private Long id;
    private String name;
    private Date birthDate;
    private String email;
    private String cpf;
    private Long companyId;
    private TbRoles role;



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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public TbRoles getRole() {
        return role;
    }

    public void setRole(TbRoles role) {
        this.role = role;
    }
}