package com.autoauction.buynow.service;

import com.autoauction.buynow.configuration.Initializer;
import com.autoauction.buynow.model.CarTypeModel;
import com.autoauction.buynow.repository.CarTypeRepository;
import com.autoauction.buynow.repository.IaaiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

@Service
public class CarTypeInitializer {
    @Autowired
    SeleniumService seleniumService;
    @Autowired
    CarTypeRepository carTypeRepository;
    @Autowired
    IaaiRepository iaaiRepository;
    @Autowired
    private DataSource dataSource;

    @Scheduled(fixedRate = 999999999)
    public void initData() throws Exception {
        Initializer.loadData(dataSource);
    }

//    @Scheduled(fixedRate = 900000)
    public void initCar() {
        List<CarTypeModel> all = carTypeRepository.findAll();
        all.forEach(carTypeModel -> {
            try {
                String auctionType = carTypeModel.getAuctionType();
                switch (auctionType) {
                    case "copart":
                        System.out.println("Case COPART active");
                        seleniumService.goToCopart(carTypeModel.getHandler(), carTypeModel.getCarType(), carTypeModel.getAuctionType());
                        break;
                    case "iaai":
                        System.out.println("Case IAAI active");
                        seleniumService.goToiaai(carTypeModel.getMotor(),carTypeModel.getHandler(), carTypeModel.getCarType(), carTypeModel.getAuctionType());
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
