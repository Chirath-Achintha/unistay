# UniStay Project — Member 02
# Module: Boarding / Accommodation Management

**Project**: UniStay — Student Accommodation Platform
**Tech Stack**: HTML / CSS / JavaScript (Frontend) · Spring Boot Java (Backend) · MySQL (Database)

---

## Module Responsibilities

- Add boarding house details
- Update and delete boarding listings
- Manage room details and availability
- Manage prices, facilities, locations, and photos

---

---

## SECTION 1 — FRONTEND FILES

---

### 1.1 HTML Pages

| # | File Name | File Path | Purpose |
|---|-----------|-----------|---------|
| 1 | `owner-dashboard.html` | `frontend/pages/owner-dashboard.html` | Owner's main dashboard showing all their boarding listings |
| 2 | `add-boarding.html` | `frontend/pages/add-boarding.html` | Form to create a new boarding house listing |
| 3 | `edit-boarding.html` | `frontend/pages/edit-boarding.html` | Form to update details of an existing boarding listing |
| 4 | `boarding-details.html` | `frontend/pages/boarding-details.html` | Public-facing detailed view of a boarding house (photos, rooms, map) |

---

### 1.2 JavaScript Files

| # | File Name | File Path | Purpose |
|---|-----------|-----------|---------|
| 1 | `owner-dashboard.js` | `frontend/js/owner-dashboard.js` | Loads the owner's listings, handles delete actions and dashboard stats |
| 2 | `add-boarding.js` | `frontend/js/add-boarding.js` | Submits the add boarding form, handles photo uploads to Cloudinary |
| 3 | `edit-boarding.js` | `frontend/js/edit-boarding.js` | Pre-fills the edit form with existing data and submits updates |
| 4 | `boarding-details.js` | `frontend/js/boarding-details.js` | Loads and renders boarding details, photo gallery, room info |
| 5 | `api.js` | `frontend/js/api.js` | Shared base API URL and fetch wrapper |
| 6 | `common.js` | `frontend/js/common.js` | Auth check, logout, and navigation rendering |

---

### 1.3 CSS Files

| # | File Name | File Path | Purpose |
|---|-----------|-----------|---------|
| 1 | `global.css` | `frontend/css/global.css` | Global design tokens, typography, layout utilities |
| 2 | `components.css` | `frontend/css/components.css` | Buttons, cards, forms, badges — shared UI components |
| 3 | `boarding.css` | `frontend/css/boarding.css` | Styles for boarding forms, dashboard cards, photo gallery |

---

---

## SECTION 2 — BACKEND FILES

---

### 2.1 Controller (REST API Endpoints)

| # | File Name | File Path | Purpose |
|---|-----------|-----------|---------|
| 1 | `BoardingController.java` | `controller/BoardingController.java` | Main controller: create, read, update, delete boarding listings |
| 2 | `RoomController.java` | `controller/RoomController.java` | CRUD endpoints for individual rooms within a boarding house |
| 3 | `FacilityController.java` | `controller/FacilityController.java` | Manage facility tags (WiFi, Parking, etc.) and attach them to boardings |

**API Endpoints handled by this module:**

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/boardings` | Get all boarding listings |
| POST | `/api/boardings` | Create a new boarding listing |
| GET | `/api/boardings/{id}` | Get full details of a boarding house |
| PUT | `/api/boardings/{id}` | Update boarding house details |
| DELETE | `/api/boardings/{id}` | Delete a boarding listing |
| POST | `/api/boardings/{id}/images` | Upload photos for a boarding house |
| DELETE | `/api/boardings/{id}/images/{imageId}` | Remove a specific photo |
| GET | `/api/boardings/owner/{ownerId}` | Get all listings by a specific owner |
| POST | `/api/rooms` | Add a new room to a boarding house |
| GET | `/api/rooms/boarding/{boardingId}` | Get all rooms for a boarding house |
| PUT | `/api/rooms/{id}` | Update room details or availability |
| DELETE | `/api/rooms/{id}` | Delete a room |
| GET | `/api/facilities` | Get list of all available facilities |

---

### 2.2 Service (Business Logic)

| # | File Name | File Path | Purpose |
|---|-----------|-----------|---------|
| 1 | `BoardingService.java` | `service/BoardingService.java` | Business logic for creating, updating, and deleting boarding listings |
| 2 | `RoomService.java` | `service/RoomService.java` | Room availability tracking and pricing logic |
| 3 | `CloudinaryService.java` | `service/CloudinaryService.java` | Handles photo upload and deletion via the Cloudinary API |

---

### 2.3 Repository (Database Access)

| # | File Name | File Path | Purpose |
|---|-----------|-----------|---------|
| 1 | `BoardingRepository.java` | `repository/BoardingRepository.java` | JPA repository for querying the `boardings` table |
| 2 | `BoardingImageRepository.java` | `repository/BoardingImageRepository.java` | JPA repository for querying the `boarding_images` table |
| 3 | `RoomRepository.java` | `repository/RoomRepository.java` | JPA repository for querying the `rooms` table |

---

### 2.4 Entity (Database Models)

| # | File Name | File Path | Purpose |
|---|-----------|-----------|---------|
| 1 | `Boarding.java` | `entity/Boarding.java` | JPA entity mapped to the `boardings` table |
| 2 | `BoardingImage.java` | `entity/BoardingImage.java` | JPA entity mapped to the `boarding_images` table |
| 3 | `Room.java` | `entity/Room.java` | JPA entity mapped to the `rooms` table |
| 4 | `Facility.java` | `entity/Facility.java` | JPA entity mapped to the `facilities` table |

---

### 2.5 DTO (Data Transfer Objects)

| # | File Name | File Path | Purpose |
|---|-----------|-----------|---------|
| 1 | `BoardingRequestDTO.java` | `dto/BoardingRequestDTO.java` | Request body for creating or updating a boarding listing |
| 2 | `BoardingResponseDTO.java` | `dto/BoardingResponseDTO.java` | API response object containing boarding house data |
| 3 | `RoomRequestDTO.java` | `dto/RoomRequestDTO.java` | Request body for creating or updating a room |
| 4 | `RoomResponseDTO.java` | `dto/RoomResponseDTO.java` | API response object containing room data |

---

### 2.6 Configuration

| # | File Name | File Path | Purpose |
|---|-----------|-----------|---------|
| 1 | `CloudinaryConfig.java` | `config/CloudinaryConfig.java` | Configures the Cloudinary SDK with API credentials |

---

---

## SECTION 3 — DATABASE TABLES

---

### Table 1: `boardings`
> Stores all boarding house listings added by owners

```sql
CREATE TABLE boardings (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    owner_id            BIGINT          NOT NULL,
    name                VARCHAR(150)    NOT NULL,
    description         TEXT,
    address             VARCHAR(255)    NOT NULL,
    city                VARCHAR(100)    NOT NULL,
    district            VARCHAR(100),
    latitude            DECIMAL(10, 7),
    longitude           DECIMAL(10, 7),
    nearby_university   VARCHAR(150),
    distance_km         DECIMAL(5, 2),
    gender_type         ENUM('MALE', 'FEMALE', 'MIXED') NOT NULL,
    is_active           BOOLEAN         DEFAULT TRUE,
    created_at          TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE
);
```

| Column | Type | Description |
|--------|------|-------------|
| `id` | BIGINT | Primary key, auto-incremented |
| `owner_id` | BIGINT | Foreign key — the owner who created this listing |
| `name` | VARCHAR(150) | Name of the boarding house |
| `description` | TEXT | Detailed description of the boarding |
| `address` | VARCHAR(255) | Full street address |
| `city` | VARCHAR(100) | City where the boarding is located |
| `district` | VARCHAR(100) | District/region |
| `latitude` | DECIMAL | GPS latitude for map display |
| `longitude` | DECIMAL | GPS longitude for map display |
| `nearby_university` | VARCHAR(150) | Closest university name |
| `distance_km` | DECIMAL | Distance to nearby university in km |
| `gender_type` | ENUM | Accepted gender: MALE, FEMALE, or MIXED |
| `is_active` | BOOLEAN | Whether the listing is visible to students |
| `created_at` | TIMESTAMP | Listing creation date |
| `updated_at` | TIMESTAMP | Last modification date |

---

### Table 2: `rooms`
> Stores individual room types within a boarding house

```sql
CREATE TABLE rooms (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    boarding_id         BIGINT          NOT NULL,
    room_type           ENUM('SINGLE', 'DOUBLE', 'TRIPLE', 'DORMITORY') NOT NULL,
    price_per_month     DECIMAL(10, 2)  NOT NULL,
    total_count         INT             NOT NULL DEFAULT 1,
    available_count     INT             NOT NULL DEFAULT 1,
    size_sqft           DECIMAL(8, 2),
    floor_number        INT,
    has_attached_bath   BOOLEAN         DEFAULT FALSE,
    description         TEXT,
    FOREIGN KEY (boarding_id) REFERENCES boardings(id) ON DELETE CASCADE
);
```

| Column | Type | Description |
|--------|------|-------------|
| `id` | BIGINT | Primary key |
| `boarding_id` | BIGINT | Foreign key — the boarding house this room belongs to |
| `room_type` | ENUM | Room type: SINGLE, DOUBLE, TRIPLE, DORMITORY |
| `price_per_month` | DECIMAL | Monthly rent in LKR |
| `total_count` | INT | Total number of rooms of this type |
| `available_count` | INT | Currently available rooms of this type |
| `size_sqft` | DECIMAL | Room size in square feet |
| `floor_number` | INT | Floor the room is on |
| `has_attached_bath` | BOOLEAN | Whether an attached bathroom is included |
| `description` | TEXT | Additional room description |

---

### Table 3: `boarding_images`
> Stores Cloudinary photo URLs for each boarding house

```sql
CREATE TABLE boarding_images (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    boarding_id     BIGINT          NOT NULL,
    image_url       VARCHAR(500)    NOT NULL,
    cloudinary_id   VARCHAR(200),
    is_primary      BOOLEAN         DEFAULT FALSE,
    display_order   INT             DEFAULT 0,
    FOREIGN KEY (boarding_id) REFERENCES boardings(id) ON DELETE CASCADE
);
```

| Column | Type | Description |
|--------|------|-------------|
| `id` | BIGINT | Primary key |
| `boarding_id` | BIGINT | Foreign key — the boarding house this photo belongs to |
| `image_url` | VARCHAR(500) | Public Cloudinary URL of the image |
| `cloudinary_id` | VARCHAR(200) | Cloudinary public ID used for deletion |
| `is_primary` | BOOLEAN | Whether this is the main cover photo |
| `display_order` | INT | Sort order for photo gallery display |

---

### Table 4: `facilities`
> Master list of available facilities (e.g. WiFi, Parking, Laundry)

```sql
CREATE TABLE facilities (
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(100) UNIQUE NOT NULL
);
```

| Column | Type | Description |
|--------|------|-------------|
| `id` | BIGINT | Primary key |
| `name` | VARCHAR(100) | Unique facility name (e.g. "WiFi", "AC", "Parking") |

---

### Table 5: `boarding_facilities`
> Many-to-many join table linking boardings with facilities

```sql
CREATE TABLE boarding_facilities (
    boarding_id     BIGINT NOT NULL,
    facility_id     BIGINT NOT NULL,
    PRIMARY KEY (boarding_id, facility_id),
    FOREIGN KEY (boarding_id) REFERENCES boardings(id) ON DELETE CASCADE,
    FOREIGN KEY (facility_id) REFERENCES facilities(id) ON DELETE CASCADE
);
```

| Column | Type | Description |
|--------|------|-------------|
| `boarding_id` | BIGINT | Foreign key — the boarding house |
| `facility_id` | BIGINT | Foreign key — the facility |

---

### Relationships

```
users      (1) ──── (Many)  boardings         [owner_id]
boardings  (1) ──── (Many)  rooms             [boarding_id]
boardings  (1) ──── (Many)  boarding_images   [boarding_id]
boardings  (M) ──── (Many)  facilities        [via boarding_facilities]
```

---

*Document prepared for UniStay Project — Member 02: Boarding / Accommodation Management*
