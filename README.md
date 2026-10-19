# 🏦 Banking Loan Form Management and Document Processing System

A secure, full-stack enterprise web application built using **Spring Boot 3 (Java 17)** and **React (Frontend)** designed to digitize and automate the banking loan application workflow, validate customer information, persist application data, and dynamically generate official, formatted PDF documents for banking operations.

---

## 🧩 Key Capabilities & Improvements

- **End-to-End Loan Form Management**: Accepts, validates, and persists applicant and financial loan application details.
- **Authoritative Server-Side Validation**: Robust Bean Validation on all fields (email format, 10-digit phone, 6-digit PIN, mandatory fields, message lengths).
- **Decoupled Architecture**: Strict separation of concerns between Controllers, Services, DTOs (`FormRequestDTO`, `FormResponseDTO`), and JPA Persistence Entities (`FormEntity`).
- **Dynamic PDF Document Generation**: Generates official application letters / loan documents using OpenPDF with metadata, headers, footers, and null safety.
- **Production-Ready Exception Handling**: Centralized `@RestControllerAdvice` returning structured `ApiError` payloads with field-level validation maps and clean HTTP status codes (201, 200, 400, 404, 405, 500).
- **Externalized Configuration**: Database credentials, connection pools (HikariCP), server ports, and CORS origins configured via environment variables and `environment variables`.
- **Database Performance & Integrity**: Dedicated indexing on `email` and `created_at`, lifecycle timestamps (`@PrePersist`, `@PreUpdate`), and DDL schema migration script (`schema.sql`).
- **Automated Testing Suite**: Full test coverage across Controller layer (`MockMvc`), Service business logic (`Mockito`), PDF generation magic bytes, JPA Repository (`DataJpaTest`), and in-memory H2 test profile (`application-test.properties`).
- **OpenAPI / Swagger 3 Documentation**: Interactive API documentation accessible at `/swagger-ui.html`.
- **Modern Responsive React Frontend**: Field grouping, real-time client-side checks, loading indicators, duplicate submission prevention, synchronized state, inline PDF preview, and direct PDF download.

---

## 🏗️ Project Architecture

```
FormApplication/
│
├── df-frontend/                         # React 19 Client Application
│   ├── src/
│   │   ├── components/
│   │   │   ├── Form.jsx                 # Loan application form with validation & states
│   │   │   └── FormList.jsx             # Real-time list of submitted applications
│   │   ├── services/
│   │   │   └── api.js                   # Axios HTTP client with configurable base URL
│   │   ├── App.js                       # Main application shell with navbar & state sync
│   │   └── setupTests.js                # Jest DOM configuration
│   ├── package.json
│   └── tailwind.config.js
│
├── dynamic-form-pdf/                    # Spring Boot 3 Backend
│   ├── src/main/java/com/example/dynamicformpdf/
│   │   ├── config/                      # WebMvc & CORS configuration
│   │   ├── controller/                  # REST API endpoints & OpenAPI annotations
│   │   ├── dto/                         # FormRequestDTO and FormResponseDTO contracts
│   │   ├── entity/                      # FormEntity JPA persistence model
│   │   ├── exception/                   # GlobalExceptionHandler & structured ApiError
│   │   ├── repository/                  # FormRepository with pagination & custom queries
│   │   └── service/                     # FormService & PdfService
│   ├── src/main/resources/
│   │   ├── application.properties       # Externalized production properties
│   │   └── schema.sql                   # MySQL DDL migration script
│   ├── src/test/java/                   # Full JUnit 5 & Mockito test suite
│   ├── src/test/resources/              # Isolated H2 test profile properties
│   └── pom.xml                          # Maven build file with Lombok & H2 dependencies
│
├── environment variables                         # Environment configuration template
└── README.md
```

---

## 🛠️ Technology Stack

| Layer | Technology | Version | Purpose |
|---|---|---|---|
| **Backend** | Java | 17 | Core platform |
| | Spring Boot | 3.2.5 | Enterprise application framework |
| | Spring Web & REST | 3.2.5 | HTTP REST API endpoints |
| | Spring Data JPA | 3.2.5 | ORM and database operations |
| | Hibernate | 6.4.4 | Persistence provider |
| | Jakarta Bean Validation | 3.0 | Authoritative input validation |
| | OpenPDF | 1.3.30 | Dynamic PDF document creation |
| | SpringDoc OpenAPI | 2.3.0 | Swagger UI API documentation |
| | Lombok | 1.18.30 | Boilerplate reduction |
| | JUnit 5 & Mockito | 5.x / 5.x | Automated unit and integration testing |
| **Frontend** | React | 19.2.3 | Component-based UI library |
| | Axios | 1.13.2 | HTTP client with timeout & interceptors |
| | Tailwind CSS | 3.4.17 | Modern utility-first styling |
| | React Testing Library | 16.3.1 | Automated UI component testing |
| **Database** | MySQL | 8.0+ | Relational persistence for production |
| | H2 Database | 2.x | In-memory isolated database for tests |

---

## 🔌 API Specification

### Base Path: `/api/forms` (with `/api/form/...` aliases for backward compatibility)

| Method | Endpoint | Description | Status Codes |
|---|---|---|---|
| `POST` | `/api/forms` or `/api/form/submit` | Submit and validate loan application form | `201 Created`, `400 Bad Request` |
| `GET` | `/api/forms` | List all applications (supports `?page=0&size=10`) | `200 OK` |
| `GET` | `/api/forms/{id}` | Retrieve specific application details by ID | `200 OK`, `404 Not Found` |
| `GET` | `/api/forms/pdf/preview/{id}` | Preview generated PDF in browser (`inline`) | `200 OK`, `404 Not Found` |
| `GET` | `/api/forms/pdf/download/{id}` or `/api/form/pdf?id={id}` | Download generated PDF (`attachment`) | `200 OK`, `404 Not Found` |

### Interactive API Documentation (Swagger)
Once the backend is running, open:
```
http://localhost:8080/swagger-ui.html
```

---

## 🚀 Setup and Execution Instructions

### 1. Prerequisites
- **Java 17+** (JDK 17 verified)
- **Apache Maven 3.9+**
- **Node.js 18+** & **npm 10+** (Node v22.18.0 verified)
- **MySQL 8.0+** (for local development/production persistence)

### 2. Database Setup (MySQL)
1. Ensure MySQL is running on port `3306`.
2. Create the database:
   ```sql
   CREATE DATABASE IF NOT EXISTS dynamic_form_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```
3. (Optional) Run the schema migration script located at `dynamic-form-pdf/src/main/resources/schema.sql`.

### 3. Backend Setup

You can run the backend in two ways:

#### Option A: Zero-Setup Local Development (In-Memory H2, No MySQL required)
Ideal for testing immediately without needing a running MySQL instance or configuring passwords:
```bash
cd dynamic-form-pdf
mvn spring-boot:run "-Dspring-boot.run.profiles=local"
```
*(Note for PowerShell: keep the double quotes around `"-Dspring-boot.run.profiles=local"`).*

#### Option B: MySQL Database Mode
1. Set your database credentials:
   - **PowerShell (Windows)**:
     ```powershell
     $env:DB_PASSWORD="your_mysql_password"
     mvn spring-boot:run
     ```
   - **Bash / Linux / macOS**:
     ```bash
     export DB_PASSWORD="your_mysql_password"
     mvn spring-boot:run
     ```
   - Or as an inline parameter:
     ```bash
     mvn spring-boot:run "-Dspring-boot.run.arguments=--spring.datasource.password=your_mysql_password"
     ```

Backend starts on: `http://localhost:8080`
- **Root URL / Swagger UI**: [http://localhost:8080/](http://localhost:8080/) (automatically redirects to `/swagger-ui.html`)
- **API Endpoints**: [http://localhost:8080/api/forms](http://localhost:8080/api/forms)
- **H2 Web Console** (when using `local` profile): [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (JDBC URL: `jdbc:h2:mem:dynamic_form_db`, user: `sa`, no password)

### 4. Frontend Setup
1. Open a new terminal in `df-frontend`:
   ```bash
   cd df-frontend
   npm install
   ```
2. Start the React development server:
   ```bash
   npm start
   ```
   Frontend starts on: [http://localhost:3000](http://localhost:3000)

---

## 🧪 Automated Testing

### Backend Tests (JUnit 5 + Mockito + H2 In-Memory DB)
```bash
cd dynamic-form-pdf
mvn test
```
*Executes 26 tests across Controller, Service, PDF Generation, and JPA Repository layers.*

### Frontend Tests (React Testing Library + Jest)
```bash
cd df-frontend
npm test -- --watchAll=false
```

---

## 🔒 Security and Operational Controls

- **No Hardcoded Secrets**: Datasource credentials, passwords, and connection URLs are externalized with environment variables.
- **Authoritative Validation**: Validation is enforced at the controller and JPA layer, rejecting invalid input regardless of frontend bypassing.
- **Structured Error Handling**: Stack traces, SQL exception messages, and internal package details are never exposed to clients.
- **Resource Cleanup**: Stream resources and PDF document contexts are closed in `finally` blocks, preventing memory leaks and file descriptor exhaustion.
- **Database Optimization**: Indexed on `email` and `created_at` for high-frequency queries and sorting.