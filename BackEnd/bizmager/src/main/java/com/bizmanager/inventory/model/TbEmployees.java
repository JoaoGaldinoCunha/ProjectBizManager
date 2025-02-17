package com.bizmanager.inventory.model;
import com.bizmanager.inventory.model.request.LoginRequest;
import com.bizmanager.inventory.model.request.VerifyPassword;
import jakarta.persistence.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

@Entity
@Table (name = "employees")
public class TbEmployees {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(name = "birth_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date birthDate;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false,length = 11)
    private String cpf;
    @ManyToOne()
    @JoinColumn(name = "company_id")
    private TbCompany company;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private TbRoles role;


    // Getters and Setters
    public Long getId() {return id;}

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public TbCompany getCompany() {
        return company;
    }

    public String getCpf() {return cpf;}

    public void setCpf(String cpf) {this.cpf = cpf;}

    public void setCompany(TbCompany company) {
        this.company = company;
    }

    public TbRoles getRole() {return role;}

    public void setRole(TbRoles role) {this.role = role;}

    public boolean isLoginCorrect(LoginRequest loginRequest, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(loginRequest.password(),this.password);
    }

    public boolean isPasswordCorrect(VerifyPassword verifyPassword, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(verifyPassword.password(),this.password);
    }

    public boolean isAdmin(TbEmployees employee) {
        return employee.getRole().getName().equalsIgnoreCase("ADMIN");
    }

    public boolean isCompany() {
        return this.getRole().getName().equalsIgnoreCase("COMPANY");
    }
}
