# Bill Calculation Service - Build Instructions

## Prerequisites

- Docker
- Docker Compose

## Tech Stack

- Java 17
- Maven
- API Key for Open Exchange Rates (Open Exchange Rates API)

## Project Setup

### Clone the Repository

```bash
git clone https://github.com/AMurtaza95/Bill-Calculation-Service.git
cd Bill-Calculation-Service
```

### Running the Application

```bash
docker-compose up -d
```
This command will:

* Build the Docker image
* Launch the Bill Calculation Service application

### Running Tests
```bash
# Run all unit tests
mvn test

# Run specific test class
mvn test -Dtest=TestClassName

# Run a specific test method
mvn test -Dtest=TestClassName#testMethodName
```

### Generate Code Coverage Report
```bash
# Generate JaCoCo code coverage report
mvn jacoco:report

# The report will be available at:
# target/site/jacoco/index.html
```

### Static Code Analysis
```bash
# Run code style checks
mvn checkstyle:check
```

## Swagger Documentation

### Accessing Swagger UI
- After starting the application, navigate to:
  - `http://localhost:8080/swagger-ui/index.html`
  - `http://localhost:8080/v3/api-docs` (OpenAPI JSON format)

### Key Swagger Features
- Interactive API documentation
- Ability to test API endpoints directly from the UI
- Displays all available endpoints, request/response models
- Supports authentication testing


### UML Class Diagram

![Bill Calculation Service UML Class Diagram](https://github.com/AMurtaza95/Bill-Calculation-Service/blob/main/Bill%20Calculation%20Service%20Class%20Diagram.png)
