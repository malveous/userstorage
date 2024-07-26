package com.globallogic.userstorage.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "phone")
@NoArgsConstructor
@Data
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "number", length = 15)
    private String number;

    @Column(name = "city_code", length = 5)
    private String cityCode;

    @Column(name = "country_code", length = 5)
    private String countryCode;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    public Phone(String number, String cityCode, String countryCode, User user) {
        this.number = number;
        this.cityCode = cityCode;
        this.countryCode = countryCode;
        this.user = user;
    }

}
