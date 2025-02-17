package com.bizmanager.inventory.model.dto;

import com.bizmanager.inventory.model.TbCompany;
import com.bizmanager.inventory.model.TbRoles;

import java.util.Date;

public class UpdateEmployeeByAdminOrCompanyDTO {
    private String name;
    private Date birthDate;
    private String email;
    private String cpf;
    private TbCompany company;
    private TbRoles role;
    private String password;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Date getBirthDate() { return birthDate; }
    public void setBirthDate(Date birthDate) { this.birthDate = birthDate; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public TbCompany getCompany() { return company; }
    public void setCompany(TbCompany company) { this.company = company; }

    public TbRoles getRole() { return role; }
    public void setRole(TbRoles role) { this.role = role; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

}