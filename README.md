# 📄 FormApplication – Full Stack Form to PDF Generator

A **full-stack web application** built using **Spring Boot (Backend)** and **React (Frontend)** that allows users to submit an **official application form**, process the data via REST APIs, and **download the submitted data as a well-formatted PDF document**.

---

## 🧩 Features Overview

- Submit application form via **React UI** or **Postman**
- Backend processes and validates data securely
- Dynamically generates a **professionally formatted PDF**
- Download PDF after successful submission
- REST APIs with proper validation and error handling

---

## 🏗️ Project Structure

```
FormApplication/
│
├── df-frontend/                # React Frontend
│   ├── src/
│   │   ├── components/
│   │   ├── services/
│   │   └── App.js
│   ├── package.json
│   └── tailwind.config.js
│
├── dynamic-form-pdf/           # Spring Boot Backend
│   ├── src/main/java
│   ├── src/main/resources
│   ├── pom.xml
│
├── .gitignore
└── README.md
```

---

## 🛠️ Tech Stack

### Frontend
- React
- Axios
- HTML5, CSS3
- Tailwind CSS / Bootstrap

### Backend
- Spring Boot
- Spring Web
- Spring Validation
- Lombok
- iText / OpenPDF (PDF generation)
- Maven

### Tools
- Postman
- Git
- VS Code / IntelliJ IDEA

---

## 🧑‍💻 Functional Requirements

### ✨ Frontend (React)

- Responsive **Official Application Form**
- Fields:
  - Name, Email, Contact Number
  - Address (City, State, PIN Code)
  - Recipient Details
  - Date (DD/MM/YYYY)
  - Subject
  - Detailed Message
  - Signature Details
- Client-side validation:
  - Mandatory fields
  - Minimum character validation for Message
- Submit form data using Axios
- Display success and error messages
- Enable **Download PDF** button after submission

---

### 🔧 Backend (Spring Boot)

- REST API to:
  - Accept form data using `POST`
  - Generate and return PDF using `GET`
- DTO-based request/response handling
- Bean Validation for input validation
- Dynamic PDF generation using submitted data
- Global exception handling using `@ControllerAdvice`
- Logging using SLF4J
- CORS configuration for frontend-backend communication

---

## 🔌 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST   | `/api/form/submit` | Submit form data |
| GET    | `/api/form/pdf`    | Download generated PDF |

---

## 📄 Sample PDF Output Structure

```
APPLICATION FORM / OFFICIAL APPLICATION LETTER

From:
Name:
Email:
Contact Number:
Full Address:
City, State, PIN Code

To:
Designation:
Organization/Department:
Address:

Date:
Subject:

Message Body

Signature:
Name:
Contact Number:
Email:
```

All placeholders are **automatically filled** using submitted form data.

---

## 🚀 How to Run the Project

---

### ✅ Backend Setup (Spring Boot)

#### Prerequisites
- Java 17+
- Maven

#### Steps
```bash
cd dynamic-form-pdf
mvn clean install
mvn spring-boot:run
```

Backend runs on:
```
http://localhost:8080
```

---

### ✅ Frontend Setup (React)

#### Prerequisites
- Node.js (v18+)
- npm

#### Steps
```bash
cd df-frontend
npm install
npm start
```

Frontend runs on:
```
http://localhost:3000
```

---

## 🧪 API Testing Using Postman

### 🔹 Submit Form Data
**POST**
```
http://localhost:8080/api/form/submit
```

**Request Body (JSON):**
```json
{
  "fromName": "John Doe",
  "fromEmail": "john@example.com",
  "fromContact": "9876543210",
  "city": "Delhi",
  "state": "Delhi",
  "pin": "110001",
  "toDesignation": "Manager",
  "organization": "ABC Corporation",
  "subject": "Application for Leave",
  "message": "This is a detailed application message."
}
```

---

### 🔹 Download PDF
**GET**
```
http://localhost:8080/api/form/pdf
```

Response: **PDF file download**

---

## 🔐 Additional Features

- CORS configuration
- Global exception handling
- Input validation using Bean Validation
- Logging using SLF4J
- Proper HTTP status codes
- Secure input handling

---

## 📌 Git & Version Control

```bash
git init
git add .
git commit -m "Initial commit: Full stack form to PDF application"
git branch -M main
git push -u origin main
```

---

## 🧠 Future Enhancements

- Swagger / OpenAPI documentation
- Database persistence (MySQL / PostgreSQL)
- User authentication
- Docker support
- Cloud deployment

---

## 👨‍💻 Author

**Abhishek Kumar**  
Full Stack Developer  
Java | Spring Boot | React