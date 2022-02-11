package com.autoauction.buynow.controller;

import com.autoauction.buynow.model.CarTypeModel;
import com.autoauction.buynow.repository.CarTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class InfoController {


    @Autowired
    CarTypeRepository carTypeRepository;

    @GetMapping(value = "/alive/{id}")
    public String getAliveId(@PathVariable Long id) {
        CarTypeModel byId = carTypeRepository.getById(id);
        return byId.getCarType();
    }

    @GetMapping(value = "/alive")
    public String getAlive() {
        return "Alive";
    }

}
