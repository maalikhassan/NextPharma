# NextPharma - Enterprise Pharmacy Management System

NextPharma is a robust, desktop-based Pharmacy Management System built to streamline inventory tracking, supplier management, and point-of-sale (POS) billing. Developed with enterprise-grade engineering principles, it ensures high reliability, data integrity, and a seamless user experience.

## 🏛️ Layered Architecture
This project strictly adheres to a **Layered Architecture**, completely decoupling the presentation, business logic, and database operations. (Note: A generic `model` package was specifically avoided in favor of a clean, strict layered pattern spanning DTOs, Entities, Services, and Repositories).
* **Presentation Layer (Controllers)**: Handles JavaFX UI events, bindings, and immediate user input validations.
* **Business Logic Layer (Services)**: `ServiceImpl` classes execute application rules, map DTOs to Entities, and define transaction boundaries.
* **Data Access Layer (Repositories)**: `RepositoryImpl` classes handle direct JDBC operations and SQL queries, entirely isolated from the rest of the application.
* **Data Transfer Objects (DTO) & Entities**: Segregates database structures (`Entity`) from the data sent to the UI (`DTO`).

## ✨ Key Features
* **Medicine & Supplier CRUD**: Complete management of inventory and medical suppliers with instant, real-time search functionalities.
* **Smart POS Billing System**: Cart-based order processing with automated calculations and stock checks.
* **Automated Invoicing**: Generates printable PDF receipts immediately upon successful order completion using **JasperReports**.

## 🛡️ Stability & Enterprise Practices

### 1. Robust ACID Transactions
Order processing utilizes manual JDBC transaction management to prevent data corruption and maintain database consistency. 
When an order is created, the system must write to the `order` table, write multiple items to `order_detail`, and reduce item quantities in the `medicine` table. By strategically toggling `connection.setAutoCommit(false)` and using `connection.rollback()`, the system guarantees that if *any* single step fails, the entire transaction is cancelled. Your database will never suffer from partial orders or incorrect stock deductions.

### 2. Bulletproof Validations
* **Input Safety**: Form controllers employ rigorous manual checks to prevent empty submissions and catch `NumberFormatException` errors (e.g., preventing strings in numeric fields).
* **Live Stock Validation**: The billing system intelligently evaluates requested quantities against available stock in real-time. It completely prevents users from attempting to checkout with more items than are physically present.

## 🛠️ Tech Stack
* **Language**: Java
* **UI Framework**: JavaFX
* **Database**: MySQL
* **Data Access**: JDBC
* **Reporting Tool**: JasperReports
* **Utilities**: Lombok (for reducing boilerplate)
* **Build Tool**: Maven

## 🚀 Getting Started
1. Clone this repository.
2. Configure your MySQL connection details inside the `DBConnection.java` class.
3. Import your `.sql` database dump.
4. Build the project using Maven: `mvn clean install`
5. Launch the application via `Main.java` or `Starter.java`.
