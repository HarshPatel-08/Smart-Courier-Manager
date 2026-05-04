<div align="center">

<br/>

```
 ██████╗ ██████╗ ██╗   ██╗██████╗ ██╗███████╗██████╗
██╔════╝██╔═══██╗██║   ██║██╔══██╗██║██╔════╝██╔══██╗
██║     ██║   ██║██║   ██║██████╔╝██║█████╗  ██████╔╝
██║     ██║   ██║██║   ██║██╔══██╗██║██╔══╝  ██╔══██╗
╚██████╗╚██████╔╝╚██████╔╝██║  ██║██║███████╗██║  ██║
 ╚═════╝ ╚═════╝  ╚═════╝ ╚═╝  ╚═╝╚═╝╚══════╝╚═╝  ╚═╝
███╗   ███╗ █████╗ ███╗   ██╗ █████╗  ██████╗ ███████╗██████╗
████╗ ████║██╔══██╗████╗  ██║██╔══██╗██╔════╝ ██╔════╝██╔══██╗
██╔████╔██║███████║██╔██╗ ██║███████║██║  ███╗█████╗  ██████╔╝
██║╚██╔╝██║██╔══██║██║╚██╗██║██╔══██║██║   ██║██╔══╝  ██╔══██╗
██║ ╚═╝ ██║██║  ██║██║ ╚████║██║  ██║╚██████╔╝███████╗██║  ██║
╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝ ╚═════╝ ╚══════╝╚═╝  ╚═╝
```

<br/>

**Enterprise-Grade Courier & Delivery Management REST API**

<br/>

![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.13-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-JWT-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)

<br/>

![License](https://img.shields.io/badge/License-MIT-blue?style=flat-square)
![Status](https://img.shields.io/badge/Status-Active-success?style=flat-square)
![Version](https://img.shields.io/badge/Version-0.0.1--SNAPSHOT-orange?style=flat-square)
![PRs Welcome](https://img.shields.io/badge/PRs-Welcome-brightgreen?style=flat-square)

<br/>

[📖 Documentation](#-api-documentation) · [🚀 Quick Start](#-quick-start) · [🏗️ Architecture](#️-architecture) · [🤝 Contributing](#-contributing)

</div>

---

## 📌 Overview

**Smart Courier Manager** is a production-ready, scalable backend system built with **Spring Boot 3** for managing the full lifecycle of courier operations — from user registration and order creation to intelligent delivery assignment, real-time location tracking, and automated email notifications.

Designed with a clean layered architecture, JWT security, and extensible design patterns, this system is built to handle real-world courier logistics at scale.

---

## ⚡ Core Features

| Feature | Description |
|---|---|
| 🔐 **JWT Authentication** | Stateless token-based auth with role-based access control |
| 👥 **Multi-Role System** | Customer, Delivery Agent, and Admin roles |
| 📦 **Order Management** | Full CRUD with real-time status tracking |
| 🚚 **Smart Assignment** | Single & bulk delivery assignment to agents |
| 📍 **Location Tracking** | Live location updates for in-transit deliveries |
| 📧 **Email Notifications** | Automated Gmail SMTP email alerts |
| ⏰ **Scheduled Updates** | Cron-based automatic delivery status updates |
| 🛡️ **Global Error Handling** | Centralized exception management with structured error responses |
| ✅ **Input Validation** | Jakarta Bean Validation on all request payloads |

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        CLIENT / POSTMAN                         │
└──────────────────────────────┬──────────────────────────────────┘
                               │ HTTP Request
                               ▼
┌─────────────────────────────────────────────────────────────────┐
│                    SECURITY LAYER (JWT Filter)                   │
│           JwtAuthenticationFilter → JwtTokenProvider            │
└──────────────────────────────┬──────────────────────────────────┘
                               │ Authenticated Request
                               ▼
┌─────────────────────────────────────────────────────────────────┐
│                     CONTROLLER LAYER                            │
│   AuthController │ UserController │ OrderController │           │
│   DeliveryAssignmentController                                  │
└──────────────────────────────┬──────────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────────┐
│                      SERVICE LAYER                              │
│   AuthService │ OrderService │ DeliveryAssignmentService │       │
│   UserService │ EmailService │ ScheduledDeliveryUpdateService   │
└──────────────────────────────┬──────────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────────┐
│                    REPOSITORY LAYER (JPA)                       │
│   UserRepository │ OrderRepository │ ParcelRepository │          │
│   DeliveryAssignmentRepository │ LocationRepository             │
└──────────────────────────────┬──────────────────────────────────┘
                               │
                               ▼
┌─────────────────────────────────────────────────────────────────┐
│                    MySQL DATABASE                               │
│                 courierManagement_db                            │
└─────────────────────────────────────────────────────────────────┘
```

---

## 🗂️ Project Structure

```
Smart-Courier-Manager/
│
├── 📁 src/main/java/com/Harsh/Smart/Courier/Manager/
│   │
│   ├── 📁 Controller/
│   │   ├── AuthController.java               # Login endpoint
│   │   ├── UserController.java               # Registration
│   │   ├── OrderController.java              # Order management
│   │   └── DeliveryAssignmentController.java # Delivery operations
│   │
│   ├── 📁 Service/
│   │   ├── AuthService.java
│   │   ├── OrderService.java
│   │   ├── DeliveryAssignmentService.java
│   │   ├── UserService.java
│   │   ├── EmailService.java
│   │   ├── RegisterUserUseCase.java
│   │   └── ScheduledDeliveryUpdateService.java
│   │
│   ├── 📁 Security/
│   │   ├── SecurityConfig.java               # Spring Security config
│   │   ├── JwtTokenProvider.java             # Token generation & validation
│   │   └── JwtAuthenticationFilter.java      # Request filter
│   │
│   ├── 📁 Model/
│   │   ├── Users.java
│   │   ├── Order.java
│   │   ├── Parcel.java
│   │   ├── DeliveryAssignment.java
│   │   ├── Location.java
│   │   ├── UserRole.java         (enum)
│   │   ├── OrderStatus.java      (enum)
│   │   ├── DeliveryStatus.java   (enum)
│   │   └── PackageStatus.java    (enum)
│   │
│   ├── 📁 Repository/
│   │   ├── UserRepository.java
│   │   ├── OrderRepository.java
│   │   ├── ParcelRepository.java
│   │   ├── DeliveryAssignmentRepository.java
│   │   └── LocationRepository.java
│   │
│   ├── 📁 Dto/
│   │   ├── RegisterRequest / RegisterResponse
│   │   ├── LoginRequest / LoginResponse
│   │   ├── OrdersRequest / OrderResponse
│   │   ├── DeliveryAssignmentRequest / Response
│   │   ├── BulkAssignmentRequest / Response
│   │   ├── ApiResponse.java
│   │   └── ErrorResponse.java
│   │
│   └── 📁 Exception/
│       ├── GlobalExceptionHandler.java
│       ├── InvalidCredentialsException.java
│       ├── UserAlreadyExistsException.java
│       ├── OrderNotFoundException.java
│       ├── InvalidRoleException.java
│       ├── PasswordMismatchException.java
│       ├── UnauthorizedException.java
│       └── LocationNotFound.java
│
├── 📁 src/main/resources/
│   ├── application.properties               # ⚠️ Gitignored (contains secrets)
│   └── application.properties.example      # ✅ Safe template to commit
│
├── pom.xml
├── mvnw / mvnw.cmd
└── README.md
```

---

## 🚀 Quick Start

### Prerequisites

```bash
✔ Java 17+
✔ MySQL 8.0+
✔ Maven 3.8+
✔ Git
```

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/HarshPatel-08/Smart-Courier-Manager.git
cd Smart-Courier-Manager
```

### 2️⃣ Setup Database

```sql
CREATE DATABASE courierManagement_db;
```

### 3️⃣ Configure Application Properties

```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

Edit `application.properties`:

```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/courierManagement_db?allowPublicKeyRetrieval=true&useSSL=false
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD

# JWT
jwt.secret=your-super-secret-key-at-least-32-characters-long
jwt.expiration=86400000

# Gmail SMTP (use App Password, not your real password)
spring.mail.username=your_email@gmail.com
spring.mail.password=your_gmail_app_password
```

> 💡 **Gmail App Password**: Go to `Google Account → Security → 2-Step Verification → App Passwords`

### 4️⃣ Run the Application

```bash
# Using Maven Wrapper (recommended)
./mvnw spring-boot:run

# Or on Windows
mvnw.cmd spring-boot:run
```

```
Server starts at → http://localhost:8080
```

---

## 📡 API Documentation

### Base URL
```
http://localhost:8080
```

### 🔐 Authentication

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "yourpassword"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9..."
  },
  "status": 200
}
```

---

### 👤 User

#### Register
```http
POST /user/register
Content-Type: application/json

{
  "name": "Harsh Patel",
  "email": "harsh@example.com",
  "password": "securepassword",
  "role": "CUSTOMER"
}
```

---

### 📦 Orders

> All order endpoints require: `Authorization: Bearer <token>`

| Method | Endpoint | Description | Role |
|--------|----------|-------------|------|
| `POST` | `/order/create` | Create a new order | Customer |
| `GET` | `/order/{orderId}` | Get order by ID | Any |
| `GET` | `/order/my-orders` | Get current user's orders | Customer |
| `GET` | `/order/all` | Get all orders | Admin |
| `PUT` | `/order/{orderId}/status?status=DELIVERED` | Update order status | Admin |

---

### 🚚 Delivery Assignments

> All assignment endpoints require: `Authorization: Bearer <token>`

| Method | Endpoint | Description | Role |
|--------|----------|-------------|------|
| `POST` | `/api/assignments/assign` | Assign delivery to agent | Admin |
| `POST` | `/api/assignments/bulk-assign` | Bulk assign multiple deliveries | Admin |
| `GET` | `/api/assignments/{id}` | Get assignment by ID | Any |
| `GET` | `/api/assignments/my-assignments` | Get agent's own assignments | Agent |
| `GET` | `/api/assignments/all` | Get all assignments | Admin |
| `GET` | `/api/assignments/in-transit` | View in-transit deliveries | Any |
| `PUT` | `/api/assignments/{id}/status?status=DELIVERED` | Update assignment status | Agent/Admin |
| `PUT` | `/api/assignments/{id}/location/{locationId}` | Update current location | Agent |

---

### 📬 Unified Response Format

All endpoints return a consistent response structure:

```json
{
  "success": true,
  "message": "Operation message here",
  "data": { ... },
  "status": 200
}
```

Error responses:
```json
{
  "success": false,
  "message": "Error description",
  "data": null,
  "status": 400
}
```

---

## 🔐 Security

- **JWT (JSON Web Token)** — stateless authentication via `io.jsonwebtoken` (JJWT 0.12.3)
- All protected routes validated via `JwtAuthenticationFilter` on every request
- Passwords stored with **BCrypt** hashing
- Role-based access control using Spring Security's `SecurityConfig`

**Using the token in requests:**
```http
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM...
```

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| **Language** | Java 17 |
| **Framework** | Spring Boot 3.5.13 |
| **Security** | Spring Security + JWT (JJWT 0.12.3) |
| **Persistence** | Spring Data JPA + Hibernate |
| **Database** | MySQL 8.0 |
| **Email** | Spring Mail (Gmail SMTP) |
| **Validation** | Jakarta Bean Validation |
| **Build Tool** | Apache Maven |

---

## 🧪 Testing

```bash
# Run all tests
./mvnw test

# Skip tests during build
./mvnw spring-boot:run -DskipTests
```

---

## 🤝 Contributing

Contributions are welcome!

```bash
# 1. Fork the project
# 2. Create your feature branch
git checkout -b feature/AmazingFeature

# 3. Commit your changes
git commit -m 'Add some AmazingFeature'

# 4. Push to the branch
git push origin feature/AmazingFeature

# 5. Open a Pull Request
```

---

## 📄 License

Distributed under the MIT License. See `LICENSE` for more information.

---

<div align="center">

**Built with ❤️ by [Harsh Patel](https://github.com/HarshPatel-08)**

⭐ **Star this repo if you found it helpful!**

</div>
