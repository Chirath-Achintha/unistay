# UniStay Project — Member 01
# Module: User Management

**Project**: UniStay — Student Accommodation Platform
**Tech Stack**: HTML / CSS / JavaScript (Frontend) · Spring Boot Java (Backend) · MySQL (Database)

---

## Module Responsibilities

- Student registration
- Boarding owner registration
- Student and owner login / logout
- Profile management and updates

---

---

## SECTION 1 — FRONTEND FILES

---

### 1.1 HTML Pages

| # | File Name | File Path | Purpose |
|---|-----------|-----------|---------|
| 1 | `index.html` | `frontend/index.html` | Landing page with navigation links to login and register |
| 2 | `login.html` | `frontend/pages/login.html` | Login form for both students and boarding owners |
| 3 | `register.html` | `frontend/pages/register.html` | Role selector page (Student or Owner) |
| 4 | `register-student.html` | `frontend/pages/register-student.html` | Full registration form for students |
| 5 | `register-owner.html` | `frontend/pages/register-owner.html` | Full registration form for boarding owners |
| 6 | `profile.html` | `frontend/pages/profile.html` | View and edit profile for students and owners |

---

### 1.2 JavaScript Files

| # | File Name | File Path | Purpose |
|---|-----------|-----------|---------|
| 1 | `register.js` | `frontend/js/register.js` | Handles registration form submission and input validation |
| 2 | `login.js` | `frontend/js/login.js` | Handles login form submission, saves session token and user role |
| 3 | `profile.js` | `frontend/js/profile.js` | Loads user profile data and submits profile update requests |
| 4 | `common.js` | `frontend/js/common.js` | Shared utility: auth check, logout function, navigation rendering |
| 5 | `api.js` | `frontend/js/api.js` | Base API URL and shared fetch wrapper used across all JS files |

---

### 1.3 CSS Files

| # | File Name | File Path | Purpose |
|---|-----------|-----------|---------|
| 1 | `global.css` | `frontend/css/global.css` | Global design tokens, typography, layout utilities |
| 2 | `components.css` | `frontend/css/components.css` | Buttons, form inputs, cards, shared UI components |
| 3 | `auth.css` | `frontend/css/auth.css` | Styles specific to login, register and profile pages |

---

---

## SECTION 2 — BACKEND FILES

---

### 2.1 Controller (REST API Endpoints)

| # | File Name | File Path | Purpose |
|---|-----------|-----------|---------|
| 1 | `UserController.java` | `controller/UserController.java` | Main controller: register, login, logout, get/update profile |
| 2 | `AuthController.java` | `controller/AuthController.java` | Dedicated endpoints for login, logout and session validation |

**API Endpoints handled by this controller:**

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/users/register/student` | Register a new student |
| POST | `/api/users/register/owner` | Register a new boarding owner |
| POST | `/api/users/login` | Login (returns session token + role) |
| POST | `/api/users/logout` | Logout (invalidates session) |
| GET | `/api/users/profile/{id}` | Get a user's profile data |
| PUT | `/api/users/profile/{id}` | Update a user's profile |

---

### 2.2 Service (Business Logic)

| # | File Name | File Path | Purpose |
|---|-----------|-----------|---------|
| 1 | `UserService.java` | `service/UserService.java` | Business logic: registration, login validation, profile updates |

---

### 2.3 Repository (Database Access)

| # | File Name | File Path | Purpose |
|---|-----------|-----------|---------|
| 1 | `UserRepository.java` | `repository/UserRepository.java` | JPA repository for querying the users table |

---

### 2.4 Entity (Database Models)

| # | File Name | File Path | Purpose |
|---|-----------|-----------|---------|
| 1 | `User.java` | `entity/User.java` | JPA entity mapped to the `users` database table |
| 2 | `UserRole.java` | `entity/UserRole.java` | Enum defining roles: STUDENT, OWNER, ADMIN |

---

### 2.5 DTO (Data Transfer Objects)

| # | File Name | File Path | Purpose |
|---|-----------|-----------|---------|
| 1 | `StudentRegistrationDTO.java` | `dto/StudentRegistrationDTO.java` | Request body for student registration |
| 2 | `OwnerRegistrationDTO.java` | `dto/OwnerRegistrationDTO.java` | Request body for boarding owner registration |
| 3 | `StudentUpdateDTO.java` | `dto/StudentUpdateDTO.java` | Request body for updating student profile |
| 4 | `UserResponseDTO.java` | `dto/UserResponseDTO.java` | API response object containing user data |
| 5 | `LoginRequestDTO.java` | `dto/LoginRequestDTO.java` | Request body for login (email + password) |
| 6 | `LoginResponseDTO.java` | `dto/LoginResponseDTO.java` | Login response containing token, role, and user ID |

---

### 2.6 Utility / Config

| # | File Name | File Path | Purpose |
|---|-----------|-----------|---------|
| 1 | `PasswordUtil.java` | `util/PasswordUtil.java` | Password hashing and verification (BCrypt) |
| 2 | `SessionUtil.java` | `util/SessionUtil.java` | Session token generation and validation |
| 3 | `AdminTokenStore.java` | `util/AdminTokenStore.java` | In-memory token store for admin authentication |
| 4 | `AdminAuthInterceptor.java` | `config/AdminAuthInterceptor.java` | Interceptor that guards admin-only routes |

---

---

## SECTION 3 — DATABASE TABLES

---

### Table 1: `users`
> Stores all registered users (students, owners, admins)

```sql
CREATE TABLE users (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name       VARCHAR(100)    NOT NULL,
    email           VARCHAR(150)    NOT NULL UNIQUE,
    password_hash   VARCHAR(255)    NOT NULL,
    phone           VARCHAR(20),
    role            ENUM('STUDENT', 'OWNER', 'ADMIN') NOT NULL DEFAULT 'STUDENT',
    profile_image   VARCHAR(500),
    created_at      TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active       BOOLEAN         DEFAULT TRUE
);
```

| Column | Type | Description |
|--------|------|-------------|
| `id` | BIGINT | Primary key, auto-incremented |
| `full_name` | VARCHAR(100) | User's full name |
| `email` | VARCHAR(150) | Unique login email address |
| `password_hash` | VARCHAR(255) | BCrypt hashed password |
| `phone` | VARCHAR(20) | Contact phone number |
| `role` | ENUM | User role: STUDENT, OWNER, or ADMIN |
| `profile_image` | VARCHAR(500) | Cloudinary URL for profile photo |
| `created_at` | TIMESTAMP | Account creation time |
| `updated_at` | TIMESTAMP | Last profile update time |
| `is_active` | BOOLEAN | Soft delete / account status flag |

---

### Table 2: `student_profiles`
> Extended profile data for students only

```sql
CREATE TABLE student_profiles (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT NOT NULL UNIQUE,
    university      VARCHAR(150),
    student_id_no   VARCHAR(50),
    year_of_study   INT,
    budget_min      DECIMAL(10, 2),
    budget_max      DECIMAL(10, 2),
    preferred_area  VARCHAR(150),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

| Column | Type | Description |
|--------|------|-------------|
| `id` | BIGINT | Primary key |
| `user_id` | BIGINT | Foreign key to `users.id` (one-to-one) |
| `university` | VARCHAR(150) | Name of the student's university |
| `student_id_no` | VARCHAR(50) | University-issued student ID number |
| `year_of_study` | INT | Current academic year (e.g. 1, 2, 3) |
| `budget_min` | DECIMAL | Minimum monthly budget (LKR) |
| `budget_max` | DECIMAL | Maximum monthly budget (LKR) |
| `preferred_area` | VARCHAR(150) | Preferred city or area for boarding |

---

### Table 3: `owner_profiles`
> Extended profile data for boarding owners only

```sql
CREATE TABLE owner_profiles (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT NOT NULL UNIQUE,
    nic_number      VARCHAR(20),
    address         VARCHAR(255),
    bank_details    VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

| Column | Type | Description |
|--------|------|-------------|
| `id` | BIGINT | Primary key |
| `user_id` | BIGINT | Foreign key to `users.id` (one-to-one) |
| `nic_number` | VARCHAR(20) | National Identity Card number |
| `address` | VARCHAR(255) | Owner's residential address |
| `bank_details` | VARCHAR(255) | Bank account info for payments |

---

### Relationships

```
users  (1) ──── (1)  student_profiles
users  (1) ──── (1)  owner_profiles
```

---

*Document prepared for UniStay Project — Member 01: User Management*
