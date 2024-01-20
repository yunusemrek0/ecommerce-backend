package com.ecommerce.demo.ecommerce.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "address")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "address_line_1",nullable = false,length = 512)
    private String addressLine1;

    @Column(name = "address_line_2",length = 512)
    private String addressLine2;

    @Column(name = "city",nullable = false)
    private String city;

    @Column(name = "country",nullable = false,length = 75)
    private String country;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id",nullable = false)
    private LocalUser user;
}
