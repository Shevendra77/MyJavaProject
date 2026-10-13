# DSA Tracker — Java Placement Prep

A personal DSA checklist built with Java 17, Spring Boot, Spring Data JPA, MySQL, HTML, CSS and JavaScript. Clicking a checkbox immediately sends a PATCH request to Spring Boot and persists the solved status in MySQL.

## Features
- 150 topic-wise checklist entries from the placement sheet
- One-click solved / unsolved toggle with automatic database saving
- Dashboard: total, completed, remaining and completion percentage
- Topic navigation, text search, topic filter and status filter
- Responsive interface with refined dashboard styling and topic progress bars
- Export all question statuses to CSV for backup or spreadsheet tracking
- Reset search and filters in one click
- Accessible focus states and reduced-motion support

## Requirements
- JDK 17 or newer
- MySQL 8.x
- Maven 3.6+ (or use the Maven wrapper from your IDE if you add one)

## Run locally

1. Start MySQL Server.
2. Create the database (optional because `createDatabaseIfNotExist=true` is enabled):
   ```sql
   CREATE DATABASE dsa_tracker;
   ```
3. Set your database credentials. Default username is `root` and default password is blank. For a password, set environment variable `DB_PASSWORD` before starting the app. You can also edit `src/main/resources/application.properties`.
4. From this folder, run:
   ```bash
   mvn spring-boot:run
   ```
5. Open **http://localhost:8080**.

Spring Boot creates the table and seeds the topic-wise questions on the first run. The seeder avoids duplicate topic/title pairs on later restarts. Solved status remains in MySQL after restarting the app.

## REST API
- `GET /api/questions` — list all questions
- `PATCH /api/questions/{id}/status` — body: `{"solved": true}` or `false`
- `GET /api/dashboard` — overall and topic-wise progress

## Notes
- This starter version is intended for one local user; it does not include login or multi-user accounts.
- Keep database credentials private. Do not commit real passwords to GitHub.
