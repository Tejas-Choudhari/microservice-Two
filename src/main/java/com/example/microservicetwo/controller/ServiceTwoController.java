package com.example.microservicetwo.controller;
import com.example.microservicetwo.entity.ServiceTwoEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class ServiceTwoController {

    private WebClient webClient;

    public ServiceTwoController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:7000").build();
    }

    @GetMapping("/food")
    public String getName() throws Exception {
        return "API-1 of the Microservices Two is Called ." ;
    }

    @GetMapping("/festiv")
    public String getPost(){
        return "API-2 Of Miceroservices Two Called";
    }
}
