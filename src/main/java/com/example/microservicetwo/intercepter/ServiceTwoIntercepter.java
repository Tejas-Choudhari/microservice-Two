package com.example.microservicetwo.intercepter;

import com.example.microservicetwo.entity.ServiceTwoEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.apache.commons.io.IOUtils;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class ServiceTwoIntercepter implements HandlerInterceptor {


    private static final Logger logger = LoggerFactory.getLogger(ServiceTwoIntercepter.class);

    String error =" Error occured";
    Date mainRequestTime = new Date(); // Capture the current date and time
    private long startTime;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("prehandling started");
        startTime = System.currentTimeMillis();
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

        //for response
        ContentCachingResponseWrapper wrapper;
        logger.info("content caching filter applied");
        if (response instanceof ContentCachingResponseWrapper) { //response wrapper
            wrapper = (ContentCachingResponseWrapper) response;
        } else {
            wrapper = new ContentCachingResponseWrapper(response);
        }
        String responseContent = getResponse(wrapper);


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
        serviceTwoEntity.setErrorTrace(errorStackTreeThread(ex));
        serviceTwoEntity.setQueryParam(request.getQueryString());

        //for client ID
        String clientId=request.getHeader("client_id");
        serviceTwoEntity.setClientId(clientId);


        //for sending the data
        try {
            WebClient webClient = WebClient.create();
            String uri = "http://localhost:7000/api/data";
            logger.info("Inside the Web Client ");
            webClient.post()
                    .uri(uri)
                    .header("Authentication", "")
                    .body(BodyInserters.fromValue(serviceTwoEntity))
                    .retrieve()
                    .bodyToMono(String.class)
                    .toFuture();
            logger.info(" data send succesfully");
        }  catch (WebClientResponseException clientException) {
            logger.error("Error while sending data using WebClient. HTTP Status: {}", clientException.getStatusCode());
            logger.error("Response body: {}", clientException.getResponseBodyAsString());
        } catch (Exception webClientException) {
            logger.error("Error while sending data using WebClient", webClientException);
        }

    }

    private String getRequestHeaderNames(HttpServletRequest request) {

        CompletableFuture <String> headerNameThread= CompletableFuture.supplyAsync(() -> {
            try {
                logger.info(" inside the Thread of getting header response");
                Enumeration<String> headerNames = request.getHeaderNames();
                StringBuilder headersStr = new StringBuilder();

                while (headerNames.hasMoreElements()) {
                    String headerName = headerNames.nextElement();
                    headersStr.append(headerName).append(": ");

                    Enumeration<String> headerValues = request.getHeaders(headerName);
                    while (headerValues.hasMoreElements()) {
                        String headerValue = headerValues.nextElement();
                        headersStr.append(headerValue).append(", ");
                    }
                    headersStr.delete(headersStr.length() - 2, headersStr.length());
                    headersStr.append(", ");
                }
                return headersStr.toString();
            }catch (Exception e) {
                logger.error("Error getting header name asynchronously", e);
                return error;
            }
        });
        logger.info(" header name Thread executed");
        return headerNameThread.join();
    }

    public String getResponse(ContentCachingResponseWrapper contentCachingResponseWrapper) {
        logger.info("Getting response asynchronously");

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                logger.info("Getting response");
                return IOUtils.toString(contentCachingResponseWrapper.getContentAsByteArray(),
                        contentCachingResponseWrapper.getCharacterEncoding());

            } catch (Exception e) {
                logger.error("Error getting response asynchronously", e);
                return error;
            }
        });
        logger.info("thread executed");
        return future.join();
    }

    //for request id
    public static String generateRequestId() {
        logger.info(" generating alphanumaric request ID ");
        UUID uuid = UUID.randomUUID();
        String string = uuid.toString().replace("-", ""); // Remove hyphens
        StringBuilder alphanumericCharacters = new StringBuilder(string.replaceAll("[^A-Za-z0-9]", "")); // Remove non-alphanumeric characters
        while (alphanumericCharacters.length() < 10) {
            alphanumericCharacters.append(generateRandomAlphanumeric());
        }

        return alphanumericCharacters.substring(0, 10);
    }

    private static String generateRandomAlphanumeric() {

        logger.info(" inside the generateRandomAlphanumeric method");
        CompletableFuture <String> aplha= CompletableFuture.supplyAsync(() -> {
            logger.info(" generateRandomAlphanumeric thread started");
            try {
                String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
                int randomIndex = (int) (Math.random() * characters.length());
                return characters.substring(randomIndex, randomIndex + 1);
            } catch (Exception e) {
                logger.error("Error getting alpha numaric ID asynchronously", e);
                return "Error occurred";
            }
        });
        logger.info(" generateRandomAlphanumeric thread completed");
        return aplha.join();
    }
    public String errorStackTreeThread(Exception ex){
        logger.info("inside the errorStackThread");
        CompletableFuture <String> errorThread = CompletableFuture.supplyAsync(()-> {

            try {
                String errorStackTrace = null;
                logger.info("error trace ");
                if (ex != null) {
                    // Capture the exception stack trace in a variable
                    StringWriter sw = new StringWriter();
                    ex.printStackTrace(new PrintWriter(sw));
                    errorStackTrace = sw.toString();
                }
                return errorStackTrace;
            }catch (Exception e) {
                logger.error("Error getting errorStackTrace asynchronously", e);
                return "Error occurred";
            }
        });
        logger.info("Thread executed");
        return errorThread.join();
    }

}
