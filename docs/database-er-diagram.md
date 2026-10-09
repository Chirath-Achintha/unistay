# UniStay Database ER Diagram

This diagram documents the database tables mapped by the backend JPA entities.
It includes persisted columns, primary and foreign keys, and the relationships
defined by the entity mappings.

```mermaid
erDiagram
    USERS {
        bigint id PK "Generated identity"
        varchar full_name "NOT NULL"
        varchar email UK "NOT NULL"
        varchar phone "NOT NULL"
        varchar password "NOT NULL"
        varchar role "NOT NULL: STUDENT, OWNER, ADMIN"
        varchar university "Nullable; student profile field"
        varchar gender "Nullable; student profile field"
        varchar nic "Nullable; owner profile field"
        varchar address "Nullable; owner profile field"
        timestamp created_at "NOT NULL"
    }

    BOARDINGS {
        bigint id PK "Generated identity"
        bigint owner_id FK "NOT NULL"
        varchar name "NOT NULL"
        text description "Nullable"
        varchar address "NOT NULL"
        varchar location "NOT NULL"
        varchar google_maps_link "Nullable"
        varchar university "Nullable"
        double price_per_month "NOT NULL"
        integer total_rooms "NOT NULL"
        integer available_rooms "NOT NULL"
        varchar room_type "NOT NULL"
        integer students_per_room "NOT NULL"
        varchar suitable_gender "NOT NULL"
        boolean has_beds "Nullable; defaults false in Java"
        boolean has_hot_water "Nullable; defaults false in Java"
        boolean has_kitchen "Nullable; defaults false in Java"
        boolean has_laundry "Nullable; defaults false in Java"
        boolean has_ac "Nullable; defaults false in Java"
        boolean has_attached_bathroom "Nullable; defaults false in Java"
        boolean has_cctv "Nullable; defaults false in Java"
        boolean has_parking "Nullable; defaults false in Java"
        boolean has_wifi "Nullable; defaults false in Java"
        boolean has_main_road_access "Nullable; defaults false in Java"
        double distance_from_university "Nullable"
        varchar contact_number "Nullable"
        timestamp created_at "NOT NULL"
    }

    BOARDING_IMAGES {
        bigint id PK "Generated identity"
        bigint boarding_id FK "NOT NULL"
        varchar image_url "NOT NULL"
        boolean is_primary "Nullable; defaults false in Java"
    }

    VISITS {
        bigint id PK "Generated identity"
        bigint student_id FK "NOT NULL"
        bigint boarding_id FK "NOT NULL"
        date requested_date "NOT NULL"
        date proposed_date "Nullable"
        varchar status "NOT NULL: PENDING, ACCEPTED, ALTERNATIVE_DATE_PROPOSED, DECLINED"
        timestamp created_at "NOT NULL"
    }

    REVIEWS {
        bigint id PK "Generated identity"
        bigint student_id FK "NOT NULL"
        bigint boarding_id FK "NOT NULL"
        integer rating "NOT NULL"
        text comment "Nullable"
        timestamp created_at "NOT NULL"
        timestamp updated_at "Nullable"
    }

    USERS ||--o{ BOARDINGS : owns
    BOARDINGS ||--o{ BOARDING_IMAGES : has
    USERS ||--o{ VISITS : requests
    BOARDINGS ||--o{ VISITS : receives
    USERS ||--o{ REVIEWS : writes
    BOARDINGS ||--o{ REVIEWS : receives
```

## Relationship and attribute notes

- `USERS` stores students, boarding owners, and admins in one table. The
  `role` column distinguishes these account types; student and owner profile
  columns are nullable because they are role-specific.
- Each boarding belongs to one owner (`boardings.owner_id`).
- Each boarding can have zero or more images, visits, and reviews. Each image,
  visit, and review references exactly one boarding.
- A visit and a review reference a user through `student_id`. The foreign key
  itself does not constrain that user's `role`; student-only behavior is
  enforced by the application.
- `UK` marks the unique email constraint. `PK` and `FK` mark primary and
  foreign keys. Enum values are persisted as strings.
- Types and nullability are derived from the JPA entity mappings and default
  naming/type conventions. Verify against the deployed database if its schema
  has been changed independently.
