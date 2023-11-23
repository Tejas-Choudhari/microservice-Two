package com.example.microservicetwo.controller;
import com.example.microservicetwo.entity.ServiceTwoEntity;
import com.example.microservicetwo.intercepter.ServiceTwoIntercepter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;


@Slf4j
@RestController
public class ServiceTwoController {

    private static final Logger logger = LoggerFactory.getLogger(ServiceTwoController.class);

    private WebClient webClient;

    public ServiceTwoController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:7000").build();
    }

    @GetMapping("/food")
    public String getName() throws Exception {
        logger.info("inside the /food API ");
        return "API-1 of the Microservices Two is Called ." ;
    }

    @GetMapping("/festiv")
    public String getPost(){
        logger.info(" inside the /festiv API ");
        return "API-2 Of Miceroservices Two Called";
    }

    @PostMapping("/met")
    public String postMethod(){
        logger.info("inside the /met API");
        return "Post Method from microservice two is called ";
    }

    @DeleteMapping("/delete")
    public String delMethod(){
        logger.info("inside the /delete API");
        return "Delete method is called from microservices two ";
    }
}
