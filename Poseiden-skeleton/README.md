# Poseidon - Trading Management Application

Spring Boot web application (MVC + Thymeleaf) to manage trading entities:
- `BidList`
- `Trade`
- `CurvePoint`
- `Rating`
- `RuleName`
- `User`

The application is secured with Spring Security using form login and role-based access (`USER` / `ADMIN`).

## Tech Stack

- Java 17
- Spring Boot 3.5.14
- Spring MVC + Thymeleaf
- Spring Data JPA
- Spring Security
- MySQL (local/prod runtime)
- H2 (tests)
- Maven
- Bootstrap 4.3.1

## Prerequisites

- JDK 17
- Maven 3.9+
- MySQL 8+ (or compatible)

## Run Locally

1. Create the MySQL database:

```sql
CREATE DATABASE demo CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. Initialize schema and seed data:

```bash
# from the project root
mysql -u root -p demo < doc/data.sql
```

3. Configure database connection in `src/main/resources/application.properties`:
- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`

4. Start the application:

```bash
mvn spring-boot:run
```

5. Open:
- `http://localhost:8080/app/login`

## Default Accounts and Roles

The script `doc/data.sql` creates 2 users:
- `admin` with role `ADMIN`
- `user` with role `USER`

Passwords are stored as BCrypt hashes in the script.

## Security Rules

Configuration: `src/main/java/com/nnk/springboot/configuration/SecurityConfig.java`

- Public: `/app/login`, `/app/error`, `/css/**`, `/js/**`, `/images/**`, `/webjars/**`
- Admin only: `/user/**`
- All other routes: authenticated user required
- Access denied: redirect to `/app/error` (403 template)

## Main Routes

- `/bidList/list`
- `/trade/list`
- `/curvePoint/list`
- `/rating/list`
- `/ruleName/list`
- `/user/list` (ADMIN only)

`/` redirects to `/bidList/list`.

## Production Profile

The `prod` profile reads these environment variables:
- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`

File: `src/main/resources/application-prod.properties`

Launch example:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

## Tests

Run all tests:

```bash
mvn test
```

The `test` profile uses in-memory H2 (see `src/test/resources/application-test.properties`).

## Database Note

The `doc/data.sql` script uses `INT AUTO_INCREMENT` primary keys.

For production, it is recommended to use a migration tool (Flyway or Liquibase) instead of manually re-running SQL scripts.
