# Hotel Management System

A distributed microservices-based hotel management system consisting of two Spring Boot applications:
- **TravelGig** - Frontend gateway service (Port 8282)
- **HotelMicroservice** - Backend hotel data service (Port 8383)

## System Architecture

This system is built with a microservices architecture to handle hotel management operations:

1. **TravelGig (Port 8282)**
   - Acts as the API gateway and frontend service
   - Provides a web interface for hotel data entry
   - Routes requests to the appropriate microservice
   - Enhances responses with additional information (UniqueId, dateOfCreation)

2. **HotelMicroservice (Port 8383)**
   - Manages hotel data persistence
   - Provides RESTful API endpoints for hotel operations
   - Handles database CRUD operations

## Project Structure

### TravelGig Service
```
com.travelgig
├── TravelGigApplication.java
├── controller
│   └── GatewayController.java
└── model
    ├── Hotel.java
    └── JsonHotel.java
```

### HotelMicroservice
```
com.hotelmicroservice
├── HotelMicroserviceApplication.java
├── controller
│   └── HotelController.java
├── entity
│   └── HotelEntity.java
├── repository
│   └── HotelRepository.java
└── service
    └── HotelService.java
```

## Key Flows

### Adding a New Hotel
1. User fills out the hotel form in the TravelGig web interface
2. Form data is submitted to the GatewayController as a JsonHotel object
3. GatewayController converts the data to a Hotel object and forwards it to HotelMicroservice
4. HotelMicroservice persists the data and returns the saved HotelEntity
5. GatewayController enhances the response with a UniqueId and dateOfCreation
6. Response is returned to the web interface with confirmation message

## API Endpoints

### TravelGig
- **POST /hotels** - Submit hotel data to be processed and stored

### HotelMicroservice
- **POST /api/hotels** - Create a new hotel
- **GET /api/hotels/{id}** - Retrieve a hotel by ID

## Setup and Running

### Prerequisites
- Java 8 or higher
- Maven or Gradle

### Running TravelGig
```bash
cd travel-gig
mvn spring-boot:run
```

### Running HotelMicroservice
```bash
cd hotel-microservice
mvn spring-boot:run
```

### Accessing the Application
- Hotel submission form: http://localhost:8282/hotel.html
- Hotel microservice API: http://localhost:8383/api/hotels

## Database

HotelMicroservice uses an H2 in-memory database for development/testing:
- Console: http://localhost:8383/h2-console
- JDBC URL: jdbc:h2:mem:hoteldb
- Username: sa
- Password: (empty)

## Dependencies

- Spring Boot
- Spring Web
- Spring Data JPA
- H2 Database
- Thymeleaf (optional, for templates)

## Notes

- For production deployment, consider replacing the H2 database with a persistent database like PostgreSQL or MySQL
- Add authentication and authorization for secure API access
- Implement service discovery and circuit breakers for a more robust microservice architecture

