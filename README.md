# Bill Calculation Service - Build Instructions

## Prerequisites
- Java 17 or later
- Maven
- API Key for Open Exchange Rates

## Project Setup

### Clone the Repository
```bash
git clone https://github.com/your-username/bill-calculation-service.git
cd bill-calculation-service
```

### Configure API Key
1. Open `src/main/resources/application.properties`
2. Add your Open Exchange Rates API key:
```properties
exchange.api.url=https://open.er-api.com/v6/latest
exchange.api.key=YOUR_API_KEY_HERE
```

## Running the Application

### Using Maven
```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

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

## Build Commands

### Full Build
```bash
# Clean, compile, test, and package the application
mvn clean install
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
