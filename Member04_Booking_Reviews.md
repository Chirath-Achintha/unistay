# UniStay Project — Member 04
# Module: Booking & Reviews

**Project**: UniStay — Student Accommodation Platform
**Tech Stack**: HTML / CSS / JavaScript (Frontend) · Spring Boot Java (Backend) · MySQL (Database)

---

## Module Responsibilities

- Students submit room booking requests
- Owners view incoming booking requests
- Owners accept or reject requests
- Manage booking / request status
- Students give ratings and reviews for boardings

---

---

## SECTION 1 — FRONTEND FILES

---

### 1.1 HTML Pages

| # | File Name | File Path | Purpose |
|---|-----------|-----------|---------|
| 1 | `boarding-details.html` | `frontend/pages/boarding-details.html` | Contains the booking request form and the review/rating section |
| 2 | `bookings.html` | `frontend/pages/bookings.html` | Student's booking history page showing all requests and their status |
| 3 | `owner-dashboard.html` | `frontend/pages/owner-dashboard.html` | Owner's dashboard which includes the incoming booking requests panel |

---

### 1.2 JavaScript Files

| # | File Name | File Path | Purpose |
|---|-----------|-----------|---------|
| 1 | `booking-request.js` | `frontend/js/booking-request.js` | Submits a room request from the boarding details page |
| 2 | `bookings.js` | `frontend/js/bookings.js` | Loads the student's booking history and displays status badges |
| 3 | `owner-bookings.js` | `frontend/js/owner-bookings.js` | Loads incoming requests for the owner and handles accept/reject actions |
| 4 | `review.js` | `frontend/js/review.js` | Submits a star rating and written review from the boarding details page |
| 5 | `api.js` | `frontend/js/api.js` | Shared base API URL and fetch wrapper |
| 6 | `common.js` | `frontend/js/common.js` | Auth check, logout, and navigation rendering |

---

### 1.3 CSS Files

| # | File Name | File Path | Purpose |
|---|-----------|-----------|---------|
| 1 | `global.css` | `frontend/css/global.css` | Global design tokens, typography, layout utilities |
| 2 | `components.css` | `frontend/css/components.css` | Buttons, cards, badges — shared UI components |
| 3 | `booking.css` | `frontend/css/booking.css` | Styles for booking request form, status badges, review form, star rating widget |

---

---

## SECTION 2 — BACKEND FILES

---

### 2.1 Controller (REST API Endpoints)

| # | File Name | File Path | Purpose |
|---|-----------|-----------|---------|
| 1 | `VisitController.java` | `controller/VisitController.java` | Main controller for booking request CRUD and status updates |
| 2 | `ReviewController.java` | `controller/ReviewController.java` | Endpoints for submitting and retrieving reviews |
| 3 | `BookingController.java` | `controller/BookingController.java` | Additional endpoints for booking status tracking and notification triggers |

**API Endpoints handled by this module:**

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/visits` | Student submits a new booking/room request |
| GET | `/api/visits/student/{studentId}` | Get all booking requests made by a student |
| GET | `/api/visits/boarding/{boardingId}` | Owner views all requests for a boarding |
| PUT | `/api/visits/{id}/status` | Owner accepts or rejects a request |
| DELETE | `/api/visits/{id}` | Student cancels a booking request |
| POST | `/api/reviews` | Student submits a review and rating |
| GET | `/api/reviews/boarding/{boardingId}` | Get all reviews for a boarding house |
| GET | `/api/reviews/student/{studentId}` | Get all reviews written by a student |
| DELETE | `/api/reviews/{id}` | Delete a review (owner or admin) |
| GET | `/api/notifications/{userId}` | Get all notifications for a user |
| PUT | `/api/notifications/{id}/read` | Mark a notification as read |

---

### 2.2 Service (Business Logic)

| # | File Name | File Path | Purpose |
|---|-----------|-----------|---------|
| 1 | `VisitService.java` | `service/VisitService.java` | Business logic for creating requests and updating status (PENDING → ACCEPTED / REJECTED) |
| 2 | `ReviewService.java` | `service/ReviewService.java` | Business logic for submitting reviews and calculating average ratings |
| 3 | `NotificationService.java` | `service/NotificationService.java` | Creates and sends notifications when booking status changes or a new review is posted |

---

### 2.3 Repository (Database Access)

| # | File Name | File Path | Purpose |
|---|-----------|-----------|---------|
| 1 | `VisitRepository.java` | `repository/VisitRepository.java` | JPA repository for querying the `visits` table |
| 2 | `ReviewRepository.java` | `repository/ReviewRepository.java` | JPA repository for querying the `reviews` table |
| 3 | `NotificationRepository.java` | `repository/NotificationRepository.java` | JPA repository for querying the `notifications` table |

---

### 2.4 Entity (Database Models)

| # | File Name | File Path | Purpose |
|---|-----------|-----------|---------|
| 1 | `Visit.java` | `entity/Visit.java` | JPA entity mapped to the `visits` table (booking requests) |
| 2 | `VisitStatus.java` | `entity/VisitStatus.java` | Enum for booking status: PENDING, ACCEPTED, REJECTED, CANCELLED |
| 3 | `Review.java` | `entity/Review.java` | JPA entity mapped to the `reviews` table |
| 4 | `Notification.java` | `entity/Notification.java` | JPA entity mapped to the `notifications` table |

---

### 2.5 DTO (Data Transfer Objects)

| # | File Name | File Path | Purpose |
|---|-----------|-----------|---------|
| 1 | `VisitRequestDTO.java` | `dto/VisitRequestDTO.java` | Request body when a student submits a booking request |
| 2 | `VisitResponseDTO.java` | `dto/VisitResponseDTO.java` | API response object containing booking request data |
| 3 | `VisitStatusUpdateDTO.java` | `dto/VisitStatusUpdateDTO.java` | Request body for owner to accept or reject a request |
| 4 | `ReviewRequestDTO.java` | `dto/ReviewRequestDTO.java` | Request body when a student submits a review |
| 5 | `ReviewResponseDTO.java` | `dto/ReviewResponseDTO.java` | API response object containing review data |
| 6 | `BoardingReviewsDTO.java` | `dto/BoardingReviewsDTO.java` | Aggregated reviews for a boarding (list + average rating) |
| 7 | `NotificationDTO.java` | `dto/NotificationDTO.java` | API response object containing notification data |

---

---

## SECTION 3 — DATABASE TABLES

---

### Table 1: `visits`
> Stores all room booking requests submitted by students

```sql
CREATE TABLE visits (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id      BIGINT          NOT NULL,
    boarding_id     BIGINT          NOT NULL,
    room_id         BIGINT,
    status          ENUM('PENDING', 'ACCEPTED', 'REJECTED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    message         TEXT,
    requested_date  DATE,
    response_note   TEXT,
    created_at      TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (boarding_id) REFERENCES boardings(id) ON DELETE CASCADE,
    FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE SET NULL
);
```

| Column | Type | Description |
|--------|------|-------------|
| `id` | BIGINT | Primary key, auto-incremented |
| `student_id` | BIGINT | Foreign key — the student making the request |
| `boarding_id` | BIGINT | Foreign key — the boarding house being requested |
| `room_id` | BIGINT | Foreign key — the specific room requested (optional) |
| `status` | ENUM | Current status: PENDING, ACCEPTED, REJECTED, CANCELLED |
| `message` | TEXT | Student's message to the owner |
| `requested_date` | DATE | Student's preferred move-in date |
| `response_note` | TEXT | Owner's reply message when accepting or rejecting |
| `created_at` | TIMESTAMP | When the request was submitted |
| `updated_at` | TIMESTAMP | When the status was last changed |

---

### Table 2: `reviews`
> Stores star ratings and written reviews submitted by students

```sql
CREATE TABLE reviews (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id      BIGINT          NOT NULL,
    boarding_id     BIGINT          NOT NULL,
    rating          TINYINT         NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment         TEXT,
    is_anonymous    BOOLEAN         DEFAULT FALSE,
    created_at      TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_student_boarding (student_id, boarding_id),
    FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (boarding_id) REFERENCES boardings(id) ON DELETE CASCADE
);
```

| Column | Type | Description |
|--------|------|-------------|
| `id` | BIGINT | Primary key |
| `student_id` | BIGINT | Foreign key — the student who wrote the review |
| `boarding_id` | BIGINT | Foreign key — the boarding house being reviewed |
| `rating` | TINYINT | Star rating from 1 to 5 |
| `comment` | TEXT | Written review text |
| `is_anonymous` | BOOLEAN | If TRUE, the student's name is hidden on the public display |
| `created_at` | TIMESTAMP | When the review was submitted |
| *(unique constraint)* | | Each student can only submit one review per boarding house |

---

### Table 3: `notifications`
> Stores in-app notifications sent to users when booking or review events occur

```sql
CREATE TABLE notifications (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT          NOT NULL,
    title           VARCHAR(150)    NOT NULL,
    message         TEXT            NOT NULL,
    type            ENUM('BOOKING_REQUEST', 'BOOKING_ACCEPTED', 'BOOKING_REJECTED', 'NEW_REVIEW') NOT NULL,
    reference_id    BIGINT,
    is_read         BOOLEAN         DEFAULT FALSE,
    created_at      TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

| Column | Type | Description |
|--------|------|-------------|
| `id` | BIGINT | Primary key |
| `user_id` | BIGINT | Foreign key — the user receiving this notification |
| `title` | VARCHAR(150) | Short notification title (e.g. "Booking Accepted!") |
| `message` | TEXT | Full notification message body |
| `type` | ENUM | Notification type: BOOKING_REQUEST, BOOKING_ACCEPTED, BOOKING_REJECTED, NEW_REVIEW |
| `reference_id` | BIGINT | ID of the related visit or review record |
| `is_read` | BOOLEAN | Whether the user has read this notification |
| `created_at` | TIMESTAMP | When the notification was created |

---

### Relationships

```
users      (1) ──── (Many)  visits          [student_id]
boardings  (1) ──── (Many)  visits          [boarding_id]
rooms      (1) ──── (Many)  visits          [room_id]

users      (1) ──── (Many)  reviews         [student_id]
boardings  (1) ──── (Many)  reviews         [boarding_id]

users      (1) ──── (Many)  notifications   [user_id]
```

---

## Booking Workflow

```
Student views boarding details page
        │
        ▼
Student fills and submits booking request form
        │
        ▼
New visit record created  →  Status: PENDING
        │
        ▼
Owner receives notification on dashboard
        │
   ┌────┴────┐
   │         │
ACCEPT     REJECT
   │         │
   ▼         ▼
Status:   Status:
ACCEPTED  REJECTED
   │         │
   └────┬────┘
        │
        ▼
Student receives notification with owner's response
```

---

*Document prepared for UniStay Project — Member 04: Booking & Reviews*
