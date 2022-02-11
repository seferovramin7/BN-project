package com.autoauction.buynow.controller;

import com.autoauction.buynow.model.CarTypeModel;
import com.autoauction.buynow.repository.CarTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

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
    public String getAlive() throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        System.setProperty("https.proxyHost", "10.0.3.18");
        System.setProperty("https.proxyPort", "3128");
        String baseUrl = "https://api.telegram.org/bot5297651309:AAGpziE4Q27UrK77k51FwDkDZ_h7kdbheg0/sendMessage?chat_id=-1001733208631&text=" + "yaşıyıram relax";
        URI uri = new URI(baseUrl);
        ResponseEntity<String> result = restTemplate.postForEntity(uri, null, String.class);
        return "Alive";
    }


}
