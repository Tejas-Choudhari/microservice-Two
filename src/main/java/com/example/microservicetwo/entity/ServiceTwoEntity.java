package com.example.microservicetwo.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "serviceAuditing")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ServiceTwoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String serviceName="microservice-two";

    //extra fields
    private String requestTime;
    private String responseTime;
    private int StatusCode;
    private String timeTaken;
    private String requestURI;
    private String requestMethod;
    private String requestHeaderName;
    private String contentType;
    private String queryParam;
    private String requestID;
    private String hostName;
    private String response;
    private String errorTrace;
    private String client_id;
}
