package com.ecommerce.demo.ecommerce.backend.model;

import jakarta.persistence.*;
import lombok.*;

import javax.print.attribute.standard.MediaSize;

@Entity
@Table(name = "product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name = "name",nullable = false,unique = true)
    private String name;

    @Column(name = "short_description",nullable = false)
    private String shortDescription;

    @Column(name = "long_description")
    private String longDescription;

    @Column(name = "price",nullable = false)
    private Double price;

    @OneToOne(mappedBy = "product",cascade = CascadeType.REMOVE,optional = false,orphanRemoval = true)
    private Inventory inventory;
}
