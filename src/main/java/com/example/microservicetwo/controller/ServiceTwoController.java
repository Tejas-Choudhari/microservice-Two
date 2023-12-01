package com.example.microservicetwo.controller;
import com.example.microservicetwo.entity.ServiceTwoEntity;
import com.example.microservicetwo.intercepter.ServiceTwoIntercepter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;


@Slf4j
@RestController
public class ServiceTwoController {

    private static final Logger logger = LoggerFactory.getLogger(ServiceTwoController.class);

    private WebClient webClient;

    private String data;

//    public ServiceTwoController(WebClient.Builder webClientBuilder) {
//        this.webClient = webClientBuilder.baseUrl("http://localhost:7000").build();
//    }

    @GetMapping("/food1")
    public String getFood(@RequestHeader Map<String ,String> header ,@RequestParam String name  ){
        logger.info(" inside the /food API ");
        return "API-1 of miceroservices Two called ";
    }

    @GetMapping("/festiv")
    public String getPost(@RequestHeader Map<String ,String>header){
        logger.info(" inside the /festiv API ");
        return "API-2 Of Miceroservices Two Called";
    }

    @PostMapping("/errorTrace")
    public String display(){
        Double.parseDouble("cwjhd");
        return "hi hi hi ";
    }
    @PostMapping("/met")
    public String postMethod(@RequestHeader Map<String ,String>header){
        logger.info("inside the /met API");
        return "Post Method from microservice two is called ";
    }

    @DeleteMapping("/delete")
    public String delMethod(@RequestHeader Map<String ,String>header){
        logger.info("inside the /delete API");
        return "Delete method is called from microservices two ";
    }
}
