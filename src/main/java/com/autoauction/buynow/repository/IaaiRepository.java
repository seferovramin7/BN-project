package com.autoauction.buynow.repository;

import com.autoauction.buynow.model.CopartTransportation;
import com.autoauction.buynow.model.IaaiTransportation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IaaiRepository extends JpaRepository<IaaiTransportation, Long> {

    List<IaaiTransportation> findAll();

    IaaiTransportation findByOriginAndLocationIgnoreCase(String loc, String origin);

    IaaiTransportation findByLocationIgnoreCase(String loc);

    IaaiTransportation findByOriginIgnoreCase(String origin);


}
