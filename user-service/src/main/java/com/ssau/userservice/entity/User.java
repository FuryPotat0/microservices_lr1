package com.ssau.userservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String login;

    @JsonIgnore
    private String password;

    private boolean enabled;

    private Long companyId;

    public User(Long id, String name, String email, String login, Long companyId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.login = login;
        this.companyId = companyId;
    }
}
