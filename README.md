# LinkedAIS Backend - JWT Authentication System

## Beginner's Guide to This Project

This is a Spring Boot backend application with **email/password authentication** using **JWT (JSON Web Tokens)**.

### What Does This App Do?

1. **User Registration** - New users can create an account with email and password
2. **User Login** - Registered users can log in to get a JWT token
3. **Protected Endpoints** - Some endpoints require a valid JWT token to access

Think of JWT as a special "ticket" that proves you're logged in. After logging in, you get this ticket and show it with every request.

---

## How Authentication Works

### 1. **Registration Flow**
```
User → POST /api/auth/register → Server
         {email, password, name}
                ↓
         Server creates user in database
         Server hashes password (BCrypt)
         Server generates JWT token
                ↓
User ← JWT token + user info ← Server
```

### 2. **Login Flow**
```
User → POST /api/auth/login → Server
         {email, password}
                ↓
         Server checks email exists
         Server verifies password hash
         Server generates JWT token
                ↓
User ← JWT token + user info ← Server
```

### 3. **Accessing Protected Endpoints**
```
User → GET /api/user/me → Server
       Header: "Authorization: Bearer <token>"
                ↓
         JwtAuthenticationFilter validates token
         If valid → endpoint returns data
         If invalid → 401 Unauthorized
                ↓
User ← User data ← Server
```

---

## How to Run

### 1. **Prerequisites**
- Java 17 or higher
- Maven

### 2. **Build the Project**
```bash
mvn clean package -DskipTests
```

### 3. **Run the Application**
```bash
mvn spring-boot:run
```

Server starts on: `http://localhost:8080`

---

## API Endpoints

### Public Endpoints (No Authentication Required)

#### Register a New User
```http
POST /api/auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123",
  "name": "John Doe"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGci...",
  "email": "user@example.com",
  "name": "John Doe"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGci...",
  "email": "user@example.com",
  "name": "John Doe"
}
```

### Protected Endpoints (Require JWT Token)

#### Get Current User Info
```http
GET /api/user/me
Authorization: Bearer eyJhbGci...
```

**Response (200 OK):**
```json
{
  "email": "user@example.com",
  "authorities": [{"authority": "ROLE_USER"}]
}
```

---

### View Database
While app is running, visit: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:linkedais`
- Username: `sa`
- Password: *(leave empty)*

---

## Testing with Postman

1. **Register** → Copy the `token` from response
2. **Access protected endpoint** → Add header: `Authorization: Bearer <token>`

---