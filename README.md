# PS029: Digital Knowledge Platform & Content Access Management System

**Course Code:** 24SDCS03A - SOA Programming and Microservices  
**Architecture:** Service-Oriented Microservices Architecture (Spring Boot + Spring Cloud)

---

## 📌 Project Overview
The **Digital Knowledge Platform (DKP)** is a scalable, cloud-native microservice system built using **Spring Boot 3.4.1** and **Spring Cloud 2024.0.0**. It manages user authentication, digital content cataloging, subscription/grant permissions, and reading activity analytics through decoupled, independently deployable microservices linked via **Eureka Service Discovery**.

---

## 🏗️ System Architecture & Services

```
                          +-------------------------+
                          |   Eureka Server (8761)  |
                          +------------+------------+
                                       |
          +--------------------+-------+-------+--------------------+
          |                    |               |                    |
+---------v--------+  +--------v-------+  +----+-----------+  +-----v----------+
|   Auth Service   |  | Content Service|  | Access Service |  | Usage Service  |
|    (Port 8081)   |  |   (Port 8082)  |  |  (Port 8083)   |  |  (Port 8084)   |
+------------------+  +----------------+  +--------+-------+  +-------+--------+
                                                   |                  ^
                                                   +--- Sync Call ----+
                                                 (Access -> Content -> Usage)
```

| Microservice | Port | Description | Database (In-Memory) |
| :--- | :--- | :--- | :--- |
| **Eureka Server** | `8761` | Service Registry & Discovery Server | N/A |
| **Auth Service** | `8081` | Authentication, JWT Token Generation & User Management | H2 (`dkp_auth`) |
| **Content Service** | `8082` | Digital Books, Research Papers, & Content Catalog | H2 (`dkp_content`) |
| **Access Service** | `8083` | Access Grant Control & Permission Enforcement | H2 (`dkp_access`) |
| **Usage Service** | `8084` | User Reading Activity Tracking & Usage Analytics | H2 (`dkp_usage`) |

---

## 🛠️ Technology Stack
* **Language:** Java 21 LTS
* **Framework:** Spring Boot 3.4.1
* **Cloud Infrastructure:** Spring Cloud Netflix Eureka 2024.0.0
* **Security & Auth:** Spring Security, JJWT (JSON Web Token)
* **Database:** H2 In-Memory RDBMS (Production ready for PostgreSQL)
* **Build Tool:** Apache Maven 3.9.x
* **Inter-Service Communication:** Spring `RestTemplate` with Client-Side Load Balancing

---

## 📑 API Endpoints & Postman Documentation

### 1️⃣ Auth Service (`http://localhost:8081`)

#### Register New User
* **Method:** `POST`
* **URL:** `http://localhost:8081/api/auth/register`
* **Request Body:**
```json
{
  "username": "hasini",
  "email": "hasini@example.com",
  "password": "password123",
  "role": "STUDENT"
}
```
* **Response (`201 Created`):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "userId": 1,
  "username": "hasini",
  "email": "hasini@example.com",
  "role": "ROLE_STUDENT",
  "message": "User registered successfully"
}
```

#### User Login
* **Method:** `POST`
* **URL:** `http://localhost:8081/api/auth/login`
* **Request Body:**
```json
{
  "username": "hasini",
  "password": "password123"
}
```

#### Get All Users
* **Method:** `GET`
* **URL:** `http://localhost:8081/api/auth/users`

---

### 2️⃣ Content Service (`http://localhost:8082`)

#### Get All Content Resources
* **Method:** `GET`
* **URL:** `http://localhost:8082/api/content`

#### Create Content Resource
* **Method:** `POST`
* **URL:** `http://localhost:8082/api/content`
* **Request Body:**
```json
{
  "title": "SOA Microservices Engineering",
  "description": "Comprehensive guide to microservice architectures",
  "type": "BOOK",
  "category": "COMPUTER_SCIENCE",
  "author": "Hasini",
  "totalPages": 150,
  "contentBody": "Chapter 1: Microservice Foundations..."
}
```

#### Read Content Body
* **Method:** `GET`
* **URL:** `http://localhost:8082/api/content/1/read`

---

### 3️⃣ Access Service (`http://localhost:8083`)

#### Grant Access to User
* **Method:** `POST`
* **URL:** `http://localhost:8083/api/access/grant`
* **Request Body:**
```json
{
  "userId": 1,
  "contentId": 1,
  "accessType": "READ",
  "permissionLevel": "FULL"
}
```

#### Check User Access Permission
* **Method:** `GET`
* **URL:** `http://localhost:8083/api/access/check?userId=1&contentId=1`
* **Response (`200 OK`):**
```json
{
  "hasAccess": true,
  "accessId": 1,
  "status": "ACTIVE",
  "planType": "PREMIUM",
  "expiresAt": "2026-12-21T15:00:41.590674",
  "message": "Active access permission confirmed"
}
```

#### Get Access Grants for User
* **Method:** `GET`
* **URL:** `http://localhost:8083/api/access/user/1`

---

### 4️⃣ Usage Service (`http://localhost:8084`)

#### Log Reading Activity
* **Method:** `POST`
* **URL:** `http://localhost:8084/api/usage/log`
* **Request Body:**
```json
{
  "userId": 1,
  "contentId": 1,
  "pagesRead": 25,
  "timeSpentMinutes": 30
}
```

#### Get User Reading History
* **Method:** `GET`
* **URL:** `http://localhost:8084/api/usage/history/1`

#### Get Analytics Summary
* **Method:** `GET`
* **URL:** `http://localhost:8084/api/usage/analytics/summary`
* **Response (`200 OK`):**
```json
{
  "totalSessions": 4,
  "totalReadingHours": 1.58,
  "distinctActiveUsers": 2,
  "distinctReadResources": 2,
  "averageSessionMinutes": 23.75,
  "sessionsByResource": {
    "Content #1": 3,
    "Content #2": 1
  }
}
```

---

## 🚀 How to Run the Project

### Option 1: 1-Click Launch Script (Windows)
Double-click `run-all.bat` or execute in terminal:
```cmd
run-all.bat
```

### Option 2: Maven Command Line
1. **Clean & Build All Modules:**
   ```bash
   mvn clean install -DskipTests
   ```
2. **Start Microservices in Order:**
   * **Step 1:** `cd eureka-server && mvn spring-boot:run`
   * **Step 2:** `cd auth-service && mvn spring-boot:run`
   * **Step 3:** `cd content-service && mvn spring-boot:run`
   * **Step 4:** `cd access-service && mvn spring-boot:run`
   * **Step 5:** `cd usage-service && mvn spring-boot:run`

---

## 👩‍💻 Author & Project Info
* **Project:** PS029 - Digital Knowledge Platform
* **Course:** SOA Programming and Microservices (24SDCS03A)
* **GitHub Repository:** `https://github.com/kajjayamhasini091/soaproject`
