package com.autoauction.buynow.repository;

import com.autoauction.buynow.model.CopartTransportation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CopartRepository extends JpaRepository<CopartTransportation, Long> {

    List<CopartTransportation> findAll();

    CopartTransportation findByOriginAndLocationIgnoreCase(String loc, String origin);

    CopartTransportation findByLocationIgnoreCase(String loc);

    CopartTransportation findByOriginIgnoreCase(String origin);


}
