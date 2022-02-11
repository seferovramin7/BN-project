package com.autoauction.buynow.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="BnArchive")
public class BnArchive {

    @javax.persistence.Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String auctionType;
    private String lot;
    private Integer buyNow;

}
