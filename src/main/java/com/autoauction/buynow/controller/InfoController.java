package com.autoauction.buynow.controller;

import com.autoauction.buynow.model.CarTypeModel;
import com.autoauction.buynow.repository.CarTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {

    @Autowired
    CarTypeRepository carTypeRepository;

    @GetMapping("/alive{id}")
    public String getDBAlive(@PathVariable Long id){
        CarTypeModel byId = carTypeRepository.getById(id);
        String carType = byId.getCarType();
        System.out.println(carType);
        return carType;
    }

}
