package com.example.microservicetwo.dto;


import lombok.Data;

@Data
public class ServiceTwoDto {

    private String serviceName="microservice-two";
    private long id;

    //extra fields
    private String requestTime;
    private String responseTime;
    private int statusCode;
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
    private String clientId;
}
