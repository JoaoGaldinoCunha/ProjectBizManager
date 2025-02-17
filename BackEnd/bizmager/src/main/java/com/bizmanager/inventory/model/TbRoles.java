package com.bizmanager.inventory.model;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class TbRoles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 255, name = "name",unique = true,nullable = false)
    private String name;

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

    public enum Values{
        admin(1L),
        company(2L),
        employees(3L);

        long id;

        Values(long id) {
            this.id = id;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }
    }

}
