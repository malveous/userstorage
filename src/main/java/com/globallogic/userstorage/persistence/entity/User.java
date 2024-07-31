package com.globallogic.userstorage.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "\"user\"") // Given that user is a reserved keyword, using double quotes will help us
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "user_id", length = 36)
    private String userId;

    @Column(name = "name", length = 100, nullable = true)
    private String name;

    @Column(name = "email", unique = true, length = 70)
    private String email;

    @Column(name = "password", length = 60)
    private String password;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "updated_date")
    private Instant updatedDate;

    @Column(name = "last_login_date")
    private Instant lastLoginDate;

    @Column(name = "token")
    private String token;

    @Column(name = "active")
    private boolean active;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private List<Phone> phoneList;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public User(String name, String email, String password, Instant createdDate, Instant updatedDate,
            Instant lastLoginDate, boolean active, List<Phone> phoneList) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.lastLoginDate = lastLoginDate;
        this.active = active;
        this.phoneList = phoneList;
    }
}
