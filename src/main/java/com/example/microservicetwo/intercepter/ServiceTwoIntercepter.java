package com.example.microservicetwo.intercepter;

import com.example.microservicetwo.entity.ServiceTwoEntity;
//import com.example.microservicetwo.service.ServiceTwoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.apache.commons.io.IOUtils;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class ServiceTwoIntercepter implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(ServiceTwoIntercepter.class);

    private WebClient.Builder builder;
    Date requestTime = new Date(); // Capture the current date and time
    private long startTime;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("prehandling started");
        startTime = System.currentTimeMillis();
        Date requestTime = new Date(); // Capture the current date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("Request Time: " + dateFormat.format(requestTime));
        request.setAttribute("startTime", startTime);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.info("afterCompletion started");
        ServiceTwoEntity serviceTwoEntity = new ServiceTwoEntity();

        long endTime = System.currentTimeMillis();
        long timeTaken = endTime - startTime;
        Date responseTime = new Date(); // Capture the current date and time for response
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //for error trace
        String errorStackTrace = null;
        logger.info("error tracking method executed");
        if (ex != null) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            errorStackTrace = sw.toString();
            System.out.println(" error trace : " + errorStackTrace);
        }


        //for response
        ContentCachingResponseWrapper wrapper;
        logger.info("content caching filter applied");
        if (response instanceof ContentCachingResponseWrapper) {
            wrapper = (ContentCachingResponseWrapper) response;
        } else {
            wrapper = new ContentCachingResponseWrapper(response);
        }
        String responseContent = getResponse(wrapper);


        //for query parameter
        Map<String, String[]> queryParams = request.getParameterMap();

        for (Map.Entry<String, String[]> entry : queryParams.entrySet()) {
            String paramName = entry.getKey();
            String[] paramValues = entry.getValue();
            String paramValue = (paramValues != null && paramValues.length > 0) ? paramValues[0] : null;

            logger.info("Query Parameter: {} = {}", paramName, paramValue);

            serviceTwoEntity.setQueryParam(paramValue);

            // You can store or process the query parameter as needed
        }

        //for storing into database
        serviceTwoEntity.setRequestTime(dateFormat.format(startTime));
        serviceTwoEntity.setResponseTime(dateFormat.format(responseTime));
        serviceTwoEntity.setStatusCode(response.getStatus());
        serviceTwoEntity.setTimeTaken(String.valueOf(timeTaken) +" ms");
        serviceTwoEntity.setRequestURI(request.getRequestURI());
        serviceTwoEntity.setRequestMethod(request.getMethod());
        serviceTwoEntity.setRequestHeaderName(getRequestHeaderNames(request));
        serviceTwoEntity.setContentType(request.getContentType());

        serviceTwoEntity.setRequestID(generateRequestId());
        serviceTwoEntity.setHostName(request.getServerName());
        serviceTwoEntity.setResponse(responseContent);
        serviceTwoEntity.setErrorTrace(errorStackTrace);


        WebClient webClient = WebClient.create();
        logger.info("Inside the Web Client ");
        webClient.post()
                .uri("http://localhost:7000/api/data")
                .body(BodyInserters.fromValue(serviceTwoEntity))
                .retrieve()
                .bodyToMono(String.class)
                .block();


    }


    private String getRequestHeaderNames(HttpServletRequest request) {
        logger.info("header Method is called");
        Enumeration<String> headerNames = request.getHeaderNames();
        StringBuilder headerNamesStr = new StringBuilder();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headerNamesStr.append(headerName).append(", ");
        }
        return headerNamesStr.toString();
    }

    private String getResponse(ContentCachingResponseWrapper contentCachingResponseWrapper) {
        logger.info("response method is called ");
        String response = IOUtils.toString(contentCachingResponseWrapper.getContentAsByteArray(), contentCachingResponseWrapper.getCharacterEncoding());
        return response;
    }

    //for request id
    public static String generateRequestId() {
        logger.info(" generating alphanumaric request ID ");
        UUID uuid = UUID.randomUUID();
        String string = uuid.toString().replaceAll("-", ""); // Remove hyphens
        String alphanumericCharacters = string.replaceAll("[^A-Za-z0-9]", ""); // Remove non-alphanumeric characters
//        int randomIndex = (int) (Math.random() * alphanumericCharacters.length());

        while (alphanumericCharacters.length() < 10) {
            alphanumericCharacters += generateRandomAlphanumeric();
        }

        return alphanumericCharacters.substring(0, 10);
    }

    private static String generateRandomAlphanumeric() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int randomIndex = (int) (Math.random() * characters.length());
        return characters.substring(randomIndex, randomIndex + 1);
    }

}
