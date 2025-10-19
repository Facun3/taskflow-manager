# TaskFlow API

A Task Management REST API built with Spring Boot - A Java Backend Learning Project

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Running the Application

1. **Clone and navigate to the project:**
   ```bash
   cd taskflow-project
   ```

2. **Build the project:**
   ```bash
   mvn clean compile
   ```

3. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

4. **Test the endpoints:**
   ```bash
   # Basic hello endpoint
   curl http://localhost:8080/api/v1/hello
   
   # Personalized hello endpoint
   curl "http://localhost:8080/api/v1/hello/personalized?name=YourName"
   
   # Health check endpoint
   curl http://localhost:8080/api/v1/hello/health
   ```

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/taskflow/
│   │   ├── controller/          # REST Controllers
│   │   ├── service/            # Business Logic Services
│   │   ├── repository/         # Data Access Layer (Future)
│   │   ├── entity/             # JPA Entities (Future)
│   │   ├── dto/                # Data Transfer Objects (Future)
│   │   ├── config/             # Configuration Classes
│   │   └── TaskFlowApplication.java
│   └── resources/
│       ├── application.yml     # Main configuration
│       └── application-dev.yml # Development configuration
└── test/
    ├── java/com/taskflow/      # Test classes
    └── resources/
        └── application-test.yml # Test configuration
```

## 🧩 Phase 1 - Current Implementation

### ✅ Completed Features

- **Spring Boot Setup**: Complete project structure with Maven
- **Dependency Injection**: Demonstrates @Autowired and constructor injection
- **REST Endpoints**: Basic hello world endpoints
- **Configuration**: Environment-specific configuration files
- **Logging**: SLF4J with Logback integration (without Lombok)
- **Testing**: Basic integration tests

### 🔧 Key Concepts Demonstrated

1. **Dependency Injection Pattern**
   - Constructor injection in `HelloController`
   - Service layer separation

2. **Layered Architecture**
   - Controller → Service → (Future: Repository)
   - Clear separation of concerns

3. **Configuration Management**
   - Environment-specific properties
   - Profile-based configuration

4. **REST API Design**
   - RESTful endpoint structure
   - Proper HTTP status codes
   - JSON response format

## 🧪 Testing

Run the tests with:
```bash
mvn test
```

## 📝 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/hello` | Basic greeting message |
| GET | `/api/v1/hello/personalized?name={name}` | Personalized greeting |
| GET | `/api/v1/hello/health` | Application health check |

## 🔄 Next Steps

This completes **Phase 1** of the TaskFlow project. The next phase will include:
- Database integration with JPA/Hibernate
- User, Project, and Task entities
- CRUD operations
- Data validation

See `PROJECT_PLAN.md` for the complete roadmap.

## 🛠️ Technologies Used

- **Java 17+**
- **Spring Boot 3.2.0**
- **Maven 3.9.11**
- **SLF4J + Logback**
- **JUnit 5**

---

**Happy Learning! 🚀**
