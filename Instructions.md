# 📘 Instructions.md – Popcorn Palace 🎬

## Overview

**Popcorn Palace** is a Java Spring Boot backend system for managing movies, showtimes, and ticket bookings. It provides RESTful APIs and uses **PostgreSQL** for data persistence, **H2** for testing, and includes a complete test suite (unit + end-to-end).

---

## 📦 Prerequisites

Before running the project, make sure you have the following installed:

- **Java 21 SDK**  
  [Download Java 21](https://www.oracle.com/java/technologies/downloads/#java21)
- **Maven (optional)**  
  You can use the bundled Maven Wrapper (`./mvnw`) instead.
- **Docker**  
  [Install Docker Desktop](https://www.docker.com/products/docker-desktop)
- **IDE** (e.g., IntelliJ IDEA)  
  [Download IntelliJ](https://www.jetbrains.com/idea/)

---

## 🐘 Setting Up the PostgreSQL Database

Use the provided `compose.yaml` file to spin up a local PostgreSQL instance:

```bash
docker compose up -d
```

This will start a PostgreSQL container with the following settings:

- **Port:** 5432
- **Database:** `popcorn-palace`
- **Username:** `popcorn-palace`
- **Password:** `popcorn-palace`

> ℹ️ To initialize schema/data locally, the app automatically runs the `schema.sql` and `data.sql` files on startup (configured via `application.yaml`).

---

## 🚀 Running the Application

### Option 1: With Maven Wrapper (recommended)
```bash
./mvnw spring-boot:run
```

### Option 2: With Installed Maven
```bash
mvn spring-boot:run
```

The application will be available at:
```
http://localhost:8080
```

---

## 🧪 Running Tests

### ✅ Run All Tests

You can run all unit and end-to-end tests using either Maven Wrapper or installed Maven:

#### Option 1: With Maven Wrapper (recommended)
```bash
./mvnw test
```

#### Option 2: With Installed Maven
```bash
mvn install && mvn test
```

### 📌 Test Details

- **Unit Tests**: Controller-level, using JUnit and Mockito
- **End-to-End Tests**: Use Spring `MockMvc`, run with H2 DB initialized via `schema-test.sql` and `data-test.sql`
- **Profile**: Tests use `application-test.yaml` and `ActiveProfiles("test")`

---

## ⚙️ Configuration Summary

### ➤ application.yaml (main config)

- Runs on `port: 8080`
- Points to Dockerized PostgreSQL DB

### ➤ application-test.yaml (for tests)

- Uses H2 (`jdbc:h2:mem:testdb`)
- Loads schema/data from `schema-test.sql` and `data-test.sql`
- Runs on `port: 8081`

---