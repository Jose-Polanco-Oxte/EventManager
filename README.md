<!-- Badges -->
<p align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.0-green?logo=springboot" alt="Spring Boot"/>
  <img src="https://img.shields.io/badge/Java-21-blue?logo=java" alt="Java"/>
  <img src="https://img.shields.io/badge/Gradle-8.0-green?logo=gradle" alt="Gradle"/>
  <img src="https://img.shields.io/badge/MySQL-8.0-blue?logo=mysql" alt="MySQL"/>
  <img src="https://img.shields.io/badge/MongoDB-6.0-green?logo=mongodb" alt="MongoDB"/>
  <img src="https://img.shields.io/badge/JUnit-5.0-green?logo=junit5" alt="JUnit 5"/>
  <img src="https://img.shields.io/badge/Mockito-4.0-yellow?logo=mockito" alt="Mockito"/>
  <img src="https://img.shields.io/badge/Lombok-1.18-red?logo=lombok" alt="Lombok"/>
  <img src="https://img.shields.io/badge/JWT-Auth-blue?logo=jsonwebtokens" alt="JWT"/>
</p>

# Event Manager App

This repository is for a backend application that manages events. It provides a RESTful API to create, read, update,
and delete events. The application is built using Spring Boot, SQL and MongoDB.

## Installation 💻

1. 📝 Clone the repository:
   ```bash
   git clone
   ```
2. 📂 Navigate to the project directory:
   ```bash
    cd event-manager
   ```
3. 🗄️ Install SQL and MongoDB servers

## Configuration ⚙️

1. Edit the `application.properties` file to set up your database connection details and JWT secret key. Go to
   `application-core/src/main/resources/application.properties` and update the following properties (default port is
   8080):

   ```properties
   # MySQL Configuration
   spring.datasource.url={your_database_url}
   spring.datasource.username={your_username}
   spring.datasource.password={your_password}

   # MongoDB Configuration
   spring.data.mongodb.uri={your_mongodb_uri}

   # JWT Configuration
   jwt.secret={your_jwt_secret_key}
   jwt.expiration.access={jwt_token_expiration_time_in_milliseconds}
   jwt.expiration.refresh={refresh_token_expiration_time_in_milliseconds}
   
   # QR Code Configuration
   QRPATH={path_to_store_qr_codes}
   ```
2. Make sure your MySQL and MongoDB servers are running and accessible.
3. The application will automatically create the necessary tables in MySQL and collections in MongoDB upon startup, but
   you must switch `spring.jpa.hibernate.ddl-auto` to `update` or `create` in the `application.properties` file if you
   want
   the tables to be created automatically.
4. (Optional) Edit the `QRPATH` property to specify where QR codes should be stored.
5. (Optional) Edit the `jwt.expiration.access` and `jwt.expiration.refresh` properties to set the expiration times for
   access and refresh tokens.
6. (Recommended) Change the `jwt.secret` property to a strong, unique value for security reasons.
7. (Optional) Change the server port by adding `server.port={your_port}` in the `application.properties` file.
8. Clean and build the project using Gradle:
   ```bash
   ./gradlew clean build
   ```
9. Run the application:
   ```bash
   ./gradlew bootRun
   ```
10. The application will be accessible at `http://localhost:8080` (or your specified port).
11. Use tools like Postman or curl to interact with the API endpoints.

## Project Structure 📁

Currently, the project only contains two modules:

- `application-core`: Contains the main application code, including models, repositories, services, controllers.
- `domain-model`: Contains the entity classes for SQL and metamodels.

Soon, `application-core` will be renamed to `accounts` and a new module `events` will be created to handle event-related
functionalities.

### Application Core

This module contains two main packages:

* `applicationcore.user`: Contains all classes related to user management, including models, repositories, services,
  controllers, and security configurations.
* `applicationcore.audit`: Contains classes related to auditing, including models, repositories, services, and
  controllers.

## License 📜

This project is licensed under the MIT License. See the `LICENSE` file for details.
