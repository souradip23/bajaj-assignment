# BFHL – Bajaj Finserv Health API

A Spring Boot REST API for the Bajaj Finserv Health qualifier round.

---

## ⚙️ Setup — Fill Your Details First!

Open `src/main/java/com/bajaj/bfhl/service/BfhlServiceImpl.java` and update these 4 constants:

```java
private static final String FULL_NAME   = "john_doe";      // your name in lowercase with underscore
private static final String DOB         = "17091999";      // your DOB as ddmmyyyy
private static final String EMAIL       = "john@xyz.com";  // your email
private static final String ROLL_NUMBER = "ABCD123";       // your college roll number
```

---

## 🚀 Running Locally

### Prerequisites
- Java 17+
- Maven 3.8+

### Steps
```bash
# 1. Clone / unzip the project
cd bfhl

# 2. Build
mvn clean install

# 3. Run
mvn spring-boot:run
```

The API will start at `http://localhost:8080/bfhl`

### Test it locally
```bash
curl -X POST http://localhost:8080/bfhl \
  -H "Content-Type: application/json" \
  -d '{"data": ["a","1","334","4","R","$"]}'
```

---

## 🧪 Running Tests

```bash
mvn test
```

---

## ☁️ Deploying to Render (Free)

1. Push this project to a **GitHub repository**
2. Go to [render.com](https://render.com) → New → Web Service
3. Connect your GitHub repo
4. Set these settings:
   - **Environment:** Docker
   - **Port:** 8080
5. Click **Deploy**
6. Your API URL will be: `https://your-app.onrender.com/bfhl`

## ☁️ Deploying to Railway (Free)

1. Push to GitHub
2. Go to [railway.app](https://railway.app) → New Project → Deploy from GitHub
3. Railway auto-detects the Dockerfile
4. Set environment variable `PORT=8080` if needed
5. Your URL: `https://your-app.up.railway.app/bfhl`

---

## 📋 API Reference

### POST /bfhl

**Request:**
```json
{
  "data": ["a", "1", "334", "4", "R", "$"]
}
```

**Response (200 OK):**
```json
{
  "is_success": true,
  "user_id": "john_doe_17091999",
  "email": "john@xyz.com",
  "roll_number": "ABCD123",
  "odd_numbers": ["1"],
  "even_numbers": ["334", "4"],
  "alphabets": ["A", "R"],
  "special_characters": ["$"],
  "sum": "339",
  "concat_string": "Ra"
}
```

---

## 🏗️ Project Structure

```
src/
├── main/java/com/bajaj/bfhl/
│   ├── BfhlApplication.java          # Entry point
│   ├── controller/
│   │   └── BfhlController.java       # POST /bfhl endpoint
│   ├── service/
│   │   ├── BfhlService.java          # Interface
│   │   └── BfhlServiceImpl.java      # Business logic
│   ├── dto/
│   │   ├── BfhlRequest.java          # Request DTO
│   │   ├── BfhlResponse.java         # Response DTO
│   │   └── ErrorResponse.java        # Error DTO
│   └── exception/
│       ├── BfhlException.java        # Custom exception
│       └── GlobalExceptionHandler.java # Handles all errors gracefully
└── test/java/com/bajaj/bfhl/
    └── BfhlApplicationTests.java     # Unit + Integration tests
```
