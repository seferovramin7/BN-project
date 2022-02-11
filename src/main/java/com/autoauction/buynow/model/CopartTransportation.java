package com.autoauction.buynow.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="CopartTransportation")
public class CopartTransportation {

    @javax.persistence.Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price")
    private String price;

    @Column(name = "location")
    private String location;

    @Column(name = "origin")
    private String origin;
}
