# UniStay Project — Member 03
# Module: Search & Recommendation

**Project**: UniStay — Student Accommodation Platform
**Tech Stack**: HTML / CSS / JavaScript (Frontend) · Spring Boot Java (Backend) · MySQL (Database)

---

## Module Responsibilities

- Search for boarding places
- Filter by location, price, room type, facilities, and availability
- Compare accommodation options side by side
- Recommend accommodations based on student preferences (budget, distance, university, room type, facilities)

---

---

## SECTION 1 — FRONTEND FILES

---

### 1.1 HTML Pages

| # | File Name | File Path | Purpose |
|---|-----------|-----------|---------|
| 1 | `index.html` | `frontend/index.html` | Landing page with a quick search bar for location and room type |
| 2 | `search.html` | `frontend/pages/search.html` | Full search results page with filter sidebar and listing cards |
| 3 | `compare.html` | `frontend/pages/compare.html` | Side-by-side comparison page for multiple selected boardings |
| 4 | `recommendations.html` | `frontend/pages/recommendations.html` | Personalized recommended boardings for a logged-in student |

---

### 1.2 JavaScript Files

| # | File Name | File Path | Purpose |
|---|-----------|-----------|---------|
| 1 | `search.js` | `frontend/js/search.js` | Sends search/filter queries to API, renders result cards, handles pagination |
| 2 | `compare.js` | `frontend/js/compare.js` | Loads selected boarding IDs from session and renders comparison table |
| 3 | `recommendations.js` | `frontend/js/recommendations.js` | Fetches and displays personalised recommendations with match scores |
| 4 | `api.js` | `frontend/js/api.js` | Shared base API URL and fetch wrapper |
| 5 | `common.js` | `frontend/js/common.js` | Auth check, logout, and navigation rendering |

---

### 1.3 CSS Files

| # | File Name | File Path | Purpose |
|---|-----------|-----------|---------|
| 1 | `global.css` | `frontend/css/global.css` | Global design tokens, typography, layout utilities |
| 2 | `components.css` | `frontend/css/components.css` | Buttons, cards, badges, filter chips — shared UI components |
| 3 | `search.css` | `frontend/css/search.css` | Styles for search results, filter sidebar, compare table, recommendation cards |

---

---

## SECTION 2 — BACKEND FILES

---

### 2.1 Controller (REST API Endpoints)

| # | File Name | File Path | Purpose |
|---|-----------|-----------|---------|
| 1 | `SearchController.java` | `controller/SearchController.java` | Handles search and filter requests with dynamic query parameters |
| 2 | `RecommendationController.java` | `controller/RecommendationController.java` | Returns personalised boarding recommendations for a student |

**API Endpoints handled by this module:**

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/search` | Search with filters: `?city=&minPrice=&maxPrice=&type=&facilities=&gender=` |
| GET | `/api/search/compare?ids=1,2,3` | Get details for multiple boardings to compare |
| GET | `/api/recommendations/{studentId}` | Get personalised boarding recommendations for a student |
| POST | `/api/preferences/{studentId}` | Save or update a student's search preferences |
| GET | `/api/preferences/{studentId}` | Get saved preferences for a student |

---

### 2.2 Service (Business Logic)

| # | File Name | File Path | Purpose |
|---|-----------|-----------|---------|
| 1 | `SearchService.java` | `service/SearchService.java` | Applies filters, sorts results, and handles pagination |
| 2 | `RecommendationService.java` | `service/RecommendationService.java` | Scores each boarding against the student's preferences and returns ranked results |

---

### 2.3 Repository (Database Access)

| # | File Name | File Path | Purpose |
|---|-----------|-----------|---------|
| 1 | `BoardingRepository.java` | `repository/BoardingRepository.java` | JPA repository — extended with JPQL filter queries |
| 2 | `BoardingSpecification.java` | `repository/BoardingSpecification.java` | JPA Specification class for building dynamic filter queries |
| 3 | `StudentPreferenceRepository.java` | `repository/StudentPreferenceRepository.java` | JPA repository for querying the `student_preferences` table |

---

### 2.4 Entity (Database Models)

| # | File Name | File Path | Purpose |
|---|-----------|-----------|---------|
| 1 | `Boarding.java` | `entity/Boarding.java` | JPA entity for the `boardings` table (shared with Member 02) |
| 2 | `StudentPreference.java` | `entity/StudentPreference.java` | JPA entity for the `student_preferences` table |

---

### 2.5 DTO (Data Transfer Objects)

| # | File Name | File Path | Purpose |
|---|-----------|-----------|---------|
| 1 | `BoardingResponseDTO.java` | `dto/BoardingResponseDTO.java` | Standard boarding response object used in search results |
| 2 | `SearchFilterDTO.java` | `dto/SearchFilterDTO.java` | Encapsulates all filter parameters: city, price range, room type, facilities |
| 3 | `RecommendationDTO.java` | `dto/RecommendationDTO.java` | Boarding response extended with a `matchScore` field (0–100) |

---

---

## SECTION 3 — DATABASE TABLES

---

### Table 1: `student_preferences`
> Stores each student's accommodation preferences, used to generate recommendations

```sql
CREATE TABLE student_preferences (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id              BIGINT NOT NULL UNIQUE,
    preferred_university    VARCHAR(150),
    preferred_city          VARCHAR(100),
    min_budget              DECIMAL(10, 2),
    max_budget              DECIMAL(10, 2),
    max_distance_km         DECIMAL(5, 2),
    preferred_room_type     ENUM('SINGLE', 'DOUBLE', 'TRIPLE', 'DORMITORY'),
    preferred_gender_type   ENUM('MALE', 'FEMALE', 'MIXED'),
    required_facilities     JSON,
    updated_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE
);
```

| Column | Type | Description |
|--------|------|-------------|
| `id` | BIGINT | Primary key |
| `student_id` | BIGINT | Foreign key — the student who owns these preferences (unique) |
| `preferred_university` | VARCHAR(150) | University the student attends |
| `preferred_city` | VARCHAR(100) | Preferred city for boarding |
| `min_budget` | DECIMAL | Minimum monthly budget (LKR) |
| `max_budget` | DECIMAL | Maximum monthly budget (LKR) |
| `max_distance_km` | DECIMAL | Maximum acceptable distance to university |
| `preferred_room_type` | ENUM | Preferred room type: SINGLE, DOUBLE, TRIPLE, DORMITORY |
| `preferred_gender_type` | ENUM | Preferred boarding gender type |
| `required_facilities` | JSON | Array of required facility IDs e.g. `[1, 3, 5]` |
| `updated_at` | TIMESTAMP | Last time preferences were updated |

---

### Table 2: `saved_searches`
> Allows students to save a set of search filters for later reuse

```sql
CREATE TABLE saved_searches (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id      BIGINT NOT NULL,
    search_name     VARCHAR(100),
    filter_json     JSON NOT NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE
);
```

| Column | Type | Description |
|--------|------|-------------|
| `id` | BIGINT | Primary key |
| `student_id` | BIGINT | Foreign key — the student who saved this search |
| `search_name` | VARCHAR(100) | User-defined name for the saved search (e.g. "Near SLIIT") |
| `filter_json` | JSON | Serialised filter parameters (city, price, type, etc.) |
| `created_at` | TIMESTAMP | When the search was saved |

---

### Table 3: `boarding_views`
> Tracks which boardings students have viewed — used to improve recommendation accuracy

```sql
CREATE TABLE boarding_views (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    boarding_id     BIGINT NOT NULL,
    student_id      BIGINT,
    viewed_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (boarding_id) REFERENCES boardings(id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE SET NULL
);
```

| Column | Type | Description |
|--------|------|-------------|
| `id` | BIGINT | Primary key |
| `boarding_id` | BIGINT | Foreign key — the boarding that was viewed |
| `student_id` | BIGINT | Foreign key — the student who viewed it (NULL if not logged in) |
| `viewed_at` | TIMESTAMP | Date and time of the view |

---

### Relationships

```
users              (1) ──── (1)     student_preferences   [student_id]
users              (1) ──── (Many)  saved_searches        [student_id]
users              (1) ──── (Many)  boarding_views        [student_id]
boardings          (1) ──── (Many)  boarding_views        [boarding_id]
```

---

## Recommendation Scoring Algorithm

The `RecommendationService.java` scores each available boarding from **0 to 100** based on how well it matches the student's saved preferences:

| Factor | Points | Logic |
|--------|--------|-------|
| **Budget match** | 30 pts | Price is within student's min–max budget range (±10% tolerance) |
| **Distance to university** | 25 pts | Closer boardings score higher; 0 km = 25 pts, > 10 km = 0 pts |
| **Room type match** | 20 pts | Exact room type match scores full 20 pts |
| **Facilities match** | 15 pts | Score = (matched facilities / required facilities) × 15 |
| **Gender type match** | 10 pts | Exact gender type match scores full 10 pts |
| **Total** | **100 pts** | Results are sorted by score descending |

---

*Document prepared for UniStay Project — Member 03: Search & Recommendation*
