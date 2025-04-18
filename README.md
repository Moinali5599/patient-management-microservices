# Patient Management System

## Overview
A comprehensive microservice-based solution for healthcare organizations to manage patient data, billing information, and analytics. This system provides a secure, scalable architecture that handles patient records, automates billing account creation, and generates valuable analytics.

## Table of Contents
- [Architecture](#architecture)
- [Services](#services)
- [Technologies](#technologies)
- [Installation](#installation)
- [Configuration](#configuration)
- [API Reference](#api-reference)
- [Authentication](#authentication)
- [Event Flow](#event-flow)
- [Contributing](#contributing)
- [License](#license)

## Architecture
The system consists of the following microservices:
- **API Gateway**: Entry point for all client requests
- **Auth Service**: Handles user authentication and authorization
- **Patient Service**: Core service for patient data management
- **Billing Service**: Manages billing accounts and transactions
- **Analytics Service**: Processes and analyzes patient and billing data

![Architecture Diagram]([https://placeholder-for-architecture-diagram.png](https://github.com/user-attachments/assets/f746b761-842f-496c-96b1-32005d461d10))

## Services

### API Gateway
- Routes requests to appropriate microservices
- Handles request/response transformations
- Implements cross-cutting concerns (logging, monitoring)

### Auth Service
- User registration and authentication
- JWT token generation and validation
- Role-based access control

### Patient Service
- Patient CRUD operations
- Triggers billing account creation via gRPC
- Publishes patient events to Kafka

### Billing Service
- Creates and manages billing accounts
- Processes billing transactions
- Exposes gRPC interface for internal communication

### Analytics Service
- Consumes patient events from Kafka
- Generates reports and insights
- Provides data visualization endpoints

## Technologies
- **Framework**: Spring Boot
- **Communication**: REST API, gRPC, Kafka
- **Authentication**: JWT
- **Database**: [Your DB choice - e.g., PostgreSQL]
- **Build Tool**: Maven/Gradle

## Installation

### Prerequisites
- Java 17+
- Docker and Docker Compose
- Kafka
- [Your DB choice]

### Clone the repository
```bash
git clone https://github.com/yourusername/patient-management-system.git
cd patient-management-system
```

### Build and Run with Docker Compose
```bash
# Build all services
./mvnw clean package -DskipTests

# Start infrastructure (Kafka, databases)
docker-compose -f docker-compose-infra.yml up -d

# Start all services
docker-compose up -d
```

### Build and Run Individually
```bash
# For each service (example for patient-service)
cd patient-service
./mvnw spring-boot:run
```

## Configuration

### API Gateway Configuration
```yaml
server:
  port: 8080

spring:
  cloud:
    gateway:
      routes:
        - id: patient-service
          uri: lb://patient-service
          predicates:
            - Path=/api/patients/**
        # More route configurations
```

### Auth Service Configuration
```yaml
jwt:
  secret: your-secret-key
  expiration: 86400000  # 24 hours in milliseconds
```

### Service Connection Configuration
Each service has its own configuration for connecting to databases, Kafka, and other services.

## API Reference

### Patient Service

#### `GET /api/patients`
Returns all patients the user has access to.

#### `GET /api/patients/{id}`
Returns a specific patient by ID.

#### `POST /api/patients`
Creates a new patient record. Also triggers:
- gRPC call to Billing Service to create billing account
- Kafka event to Analytics Service

Request body:
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "dateOfBirth": "1980-01-01",
  "address": {
    "street": "123 Main St",
    "city": "Anytown",
    "state": "CA",
    "zipCode": "12345"
  },
  "phoneNumber": "555-123-4567",
  "email": "john.doe@example.com",
}
```

#### `PUT /api/patients/{id}`
Updates an existing patient record.

### Billing Service (Internal gRPC)

The Billing Service exposes gRPC endpoints for internal communication:
- `CreateBillingAccount`: Creates a new billing account for a patient
- `GetBillingAccount`: Retrieves billing account information

### Auth Service

#### `POST /api/auth/signup`
Registers a new user.

#### `POST /api/auth/login`
Authenticates a user and returns a JWT.

#### `POST /api/auth/logout`
Invalidates the current user's JWT.

## Authentication

The system uses JWT (JSON Web Tokens) for authentication:

1. User logs in through the Auth Service
2. Auth Service validates credentials and issues a JWT
3. Client includes JWT in the `Authorization` header for subsequent requests
4. API Gateway validates the token before routing requests
5. Microservices can also validate tokens for additional security

Example JWT header:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

## Event Flow

### Patient Registration Flow
1. Client sends POST request to create a patient
2. Patient Service stores patient data
3. Patient Service calls Billing Service via gRPC to create a billing account
4. Patient Service publishes a `PatientCreated` event to Kafka
5. Analytics Service consumes the event and updates its data store

## Contributing
1. Fork the repository
2. Create a feature branch: `git checkout -b feature-name`
3. Commit your changes: `git commit -m 'Add some feature'`
4. Push to the branch: `git push origin feature-name`
5. Open a pull request

## License
This project is licensed under the MIT License - see the LICENSE file for details.
