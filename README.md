# Patient Management System

## Table of Contents
- [Architecture](#architecture)
- [Services](#services)
- [Technologies](#technologies)
- [API Reference](#api-reference)
- [Authentication](#authentication)
- [Event Flow](#event-flow)

## Architecture
The system consists of the following microservices:
- **API Gateway**: Entry point for all client requests
- **Auth Service**: Handles user authentication and authorization
- **Patient Service**: Core service for patient data management
- **Billing Service**: Manages billing accounts and transactions
- **Analytics Service**: Processes and analyzes patient and billing data

![Image](https://github.com/user-attachments/assets/f746b761-842f-496c-96b1-32005d461d10)

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
- **Database**: PostgreSQL
- **Build Tool**: Maven

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

#### `PUT /api/patients/{id}`
Updates an existing patient record.

#### `DELETE /api/patients/{id}`
Deletes an patient record.

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

### Billing Service (Internal gRPC)

The Billing Service exposes gRPC endpoints for internal communication:
- `CreateBillingAccount`: Creates a new billing account for a patient

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
