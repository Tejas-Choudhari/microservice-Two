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

        // Send audit data to http://localhost:7000/api/data
//        ServiceTwoEntity serviceTwoEntity = new ServiceTwoEntity(); // Create audit data object
//        webClient.post()
//                .uri("/api/data")
//                .bodyValue(serviceTwoEntity)
//                .retrieve()
//                .bodyToMono(String.class)
//                .block(); // Send POST request

        return "Menu-> ladu , chivda, shankarpalya " ;
    }

    @GetMapping("/festiv")
    public String getPost(){
        return "Festival -> Diwali , Dasara";
    }
}
