package com.autoauction.buynow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="CarTypeModel")
public class CarTypeModel {

    @javax.persistence.Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="CAR_TYPE")
    private String carType;

    @Column(name="AUCTION_TYPE")
    private String auctionType;

//    @Lob
    @Column(name="HANDLER", length=2048)
    private String handler;

    @Column(name="MOTOR")
    private String motor;

}
