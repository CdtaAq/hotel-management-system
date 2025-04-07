// travelbig-service/src/main/java/com/travelbig/TravelBigApplication.java
package com.travelbig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class TravelBigApplication {
    public static void main(String[] args) {
        SpringApplication.run(TravelBigApplication.class, args);
    }
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

// travelbig-service/src/main/java/com/travelbig/model/HotelResponse.java
package com.travelbig.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class HotelResponse {
    private Long id;
    private String hotelName;
    private String address;
    private String contactDetails;
    private String email;
    private LocalDateTime creationDate;
    private String uniqueId;
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getHotelName() {
        return hotelName;
    }
    
    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getContactDetails() {
        return contactDetails;
    }
    
    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public LocalDateTime getCreationDate() {
        return creationDate;
    }
    
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
    
    public String getUniqueId() {
        return uniqueId;
    }
    
    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}

// travelbig-service/src/main/java/com/travelbig/client/HotelClient.java
package com.travelbig.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HotelClient {
    
    private final RestTemplate restTemplate;
    
    @Value("${hotel.service.url}")
    private String hotelServiceUrl;
    
    @Autowired
    public HotelClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    public Object createHotel(Object hotelRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<Object> requestEntity = new HttpEntity<>(hotelRequest, headers);
        
        return restTemplate.postForObject(
                hotelServiceUrl + "/api/hotels",
                requestEntity,
                Object.class
        );
    }
}

// travelbig-service/src/main/java/com/travelbig/controller/GatewayController.java
package com.travelbig.controller;

import com.travelbig.client.HotelClient;
import com.travelbig.model.HotelResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/gateway")
public class GatewayController {
    
    private final HotelClient hotelClient;
    
    @Autowired
    public GatewayController(HotelClient hotelClient) {
        this.hotelClient = hotelClient;
    }
    
    @PostMapping("/hotels")
    public ResponseEntity<?> createHotel(@RequestBody Map<String, Object> hotelRequest) {
        // Forward request to Hotel Microservice
        LinkedHashMap<String, Object> hotelResponse = 
                (LinkedHashMap<String, Object>) hotelClient.createHotel(hotelRequest);
        
        // Generate uniqueId by combining id and creationDate
        String uniqueId = hotelResponse.get("id") + "_" + hotelResponse.get("creationDate");
        hotelResponse.put("uniqueId", uniqueId);
        
        // Create response object
        Map<String, String> response = Map.of(
                "message", "Hotel with UniqueId " + uniqueId + " is stored"
        );
        
        return ResponseEntity.ok(response);
    }
}

// travelbig-service/src/main/resources/application.properties
server.port=8282
spring.application.name=travelbig-service

# Hotel Microservice URL
hotel.service.url=http://localhost:8383
