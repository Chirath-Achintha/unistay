# UniStay – University Student Accommodation Platform

A full-stack university accommodation platform built with **Java 21 + Spring Boot** (backend) and **HTML5 + CSS3 + Vanilla JavaScript** (frontend).

---

## Project Structure

```
UniStay/
├── backend/        ← Spring Boot REST API (Java 21 + Maven)
└── frontend/       ← Static website (HTML5 + CSS3 + Vanilla JS)
```

---

## Prerequisites

Before running the project, make sure you have the following installed:

| Tool | Version | Download |
|------|---------|----------|
| Java JDK | 21+ | https://adoptium.net |
| MySQL | 8.0+ | https://dev.mysql.com/downloads/ |
| Maven | (bundled via `mvnw`) | Not required separately |

---

## Step 1 – Set Up the Database

1. Open **MySQL Workbench** or your MySQL terminal
2. Create the database (Spring Boot will auto-create tables):

```sql
CREATE DATABASE IF NOT EXISTS unistay_db;
```

3. Open `backend/src/main/resources/application.properties` and update your MySQL credentials:

```properties
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD_HERE
```

---

## Step 2 – Run the Backend (Spring Boot)

Open a terminal and navigate into the **`backend/`** folder:

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

Or using Maven if installed globally:

```bash
cd backend
mvn spring-boot:run
```

✅ The backend starts at: **http://localhost:8080**

### Available REST API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET`  | `/api/health` | System health check |
| `POST` | `/api/users/register/student` | Register a student account |
| `POST` | `/api/users/register/owner` | Register a boarding owner account |
| `GET`  | `/api/users/check-email?email=` | Check if email is taken |

---

## Step 3 – Run the Frontend

The frontend is **pure HTML/CSS/JavaScript** — no build tools or Node.js required.

### Option A: VS Code Live Server *(Recommended)*

1. Open the `frontend/` folder in VS Code
2. Right-click `frontend/index.html`
3. Select **"Open with Live Server"**
4. Browser opens at: **http://127.0.0.1:5500**

### Option B: Python Local Server

```bash
cd frontend
python -m http.server 5000
```

Open: **http://localhost:5000**

### Option C: Open HTML File Directly

Double-click `frontend/index.html` to open directly in your browser.

> ⚠️ The backend must be running first for registration forms to work. The frontend automatically targets `http://localhost:8080/api` for all REST calls.

---

## Development Tips

- **Backend only change?** Restart just the backend; the frontend needs no restart.
- **Frontend only change?** Simply refresh the browser.
- **Database schema changes?** `spring.jpa.hibernate.ddl-auto=update` automatically applies entity changes on backend restart.

---

## Technology Stack

| Layer | Technology |
|-------|-----------|
| Backend Language | Java 21 |
| Backend Framework | Spring Boot |
| Build Tool | Maven |
| Database | MySQL 8 |
| ORM | Spring Data JPA / Hibernate |
| Frontend | HTML5 |
| Styling | CSS3 (Vanilla) |
| Frontend Scripts | Vanilla JavaScript |

---

## Modules Implemented

- [x] Project Foundation & Global Design System
- [x] User Registration (Student & Boarding Owner)
- [ ] Authentication / Login
- [ ] Accommodation Listings Management
- [ ] Search & Filtering
- [ ] Recommendation System
- [ ] Room Requests
- [ ] Reviews & Ratings
- [ ] Admin Dashboard
