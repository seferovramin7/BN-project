package com.autoauction.buynow.repository;

import com.autoauction.buynow.model.CarTypeModel;
import com.autoauction.buynow.model.CopartTransportation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarTypeRepository extends JpaRepository<CarTypeModel, Long> {

}
