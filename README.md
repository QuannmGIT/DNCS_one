# HANABI CAFE - Coffee Shop Management System

A desktop application for managing coffee shop operations built with Java Swing and Hibernate ORM.

## Tech Stack

- **Language:** Java 21
- **UI Framework:** Swing with FlatLaf 3.7.1 (modern Look & Feel)
- **ORM:** Hibernate 7.3.5.Final (Jakarta Persistence)
- **Database:** MySQL 8+ (via Connector/J 9.7.0)
- **Build Tool:** Maven
- **Layout:** MigLayout 11.4.3
- **SVG Rendering:** JSVG 2.1.0

## Features

- **Authentication** — Login/logout with role-based access (admin/staff)
- **Dashboard** — Revenue overview, daily/monthly statistics, best-sellers, recent orders
- **Menu Management** — CRUD for products, grid display with category filtering, status tracking
- **Order Management** — Create and manage orders, order details with product selection
- **Invoice Management** — Invoice creation, payment status tracking
- **Staff Management** — Add/edit staff, salary table with commission rates
- **Account Panel** — Staff profile view with order/salary summaries
- **Salary Formatting** — Compact display (K/M/B/T suffixes)

## Project Structure

```
src/main/java/
├── App.java                    # Application entry point
├── Main.java                   # Main window, login/logout flow
├── hanabi/
│   ├── components/             # Reusable Swing components
│   │   ├── ButtonLink.java
│   │   ├── MainForm.java       # Navigation container
│   │   ├── PopUp.java          # Custom popup dialogs
│   │   └── SalaryTablePanel.java
│   ├── dao/                    # Data access layer (Hibernate queries)
│   │   ├── BaseDAO.java
│   │   ├── InvoiceDAO.java
│   │   ├── OrderDAO.java
│   │   ├── OrderDetailDAO.java
│   │   ├── ProductDAO.java
│   │   ├── SalaryDAO.java
│   │   ├── StaffDAO.java
│   │   └── UserDAO.java
│   ├── model/                  # JPA entities
│   │   ├── Invoice.java
│   │   ├── Order.java
│   │   ├── OrderDetail.java
│   │   ├── Product.java
│   │   ├── Salary.java
│   │   ├── Staff.java
│   │   └── User.java           # DTO (not an entity)
│   ├── service/                # Business logic layer
│   │   ├── AccountService.java
│   │   ├── AuthService.java
│   │   ├── CreateUser.java     # Staff creation form logic
│   │   ├── MenuService.java
│   │   └── RevenueService.java
│   ├── util/                   # Utilities
│   │   ├── DataInitializer.java
│   │   ├── DBInitializer.java
│   │   ├── FontLoader.java
│   │   ├── global.java
│   │   ├── HibernateUtil.java  # SessionFactory singleton
│   │   ├── PasswordUtil.java   # Password hashing
│   │   ├── TableDBInitializer.java
│   │   └── UIUtils.java
│   └── view/                   # UI panels
│       ├── Category/
│       │   ├── AccountPanel.java
│       │   ├── CategoryPanel.java
│       │   ├── DashboardView.java
│       │   ├── MenuItemsPanel.java
│       │   ├── RevenuePanel.java
│       │   └── SalaryPanel.java
│       ├── Field/
│       │   ├── AddProductForm.java
│       │   └── LoginView.java
│       └── Login/
│           ├── Banner.java
│           └── LoginPanel.java
├── schemas/                    # Reference SQL DDL
│   ├── invoice.sql
│   ├── order_details.sql
│   ├── orders.sql
│   ├── products.sql
│   ├── salaries.sql
│   └── staff.sql
src/main/resources/
├── hanabi/
│   ├── assets/                 # Icons, images (SVG)
│   ├── backend/
│   │   └── hibernate.cfg.xml   # Hibernate configuration
│   └── config/
└── Fonts/
```

## Database

- **Name:** `StoreManagement`
- **Host:** `localhost:3306`
- **Engine:** MySQL 8+
- **ORM:** Hibernate `hbm2ddl.auto = update` — tables are auto-created/altered on startup.
- All primary/foreign keys use `BINARY(16)` for UUID storage.

### Table Structure

#### `staff`
| Column | Type | Constraints |
|--------|------|-------------|
| staff_id | BINARY(16) | PK |
| staff_name | VARCHAR(50) | NOT NULL, UNIQUE |
| email | VARCHAR(100) | |
| password | VARCHAR(255) | NOT NULL |
| full_name | VARCHAR(100) | |
| role | ENUM('admin','staff') | DEFAULT 'staff' |
| status | TINYINT(1) | DEFAULT 1 (1=active, 0=layoff) |

#### `salaries`
| Column | Type | Constraints |
|--------|------|-------------|
| staff_id | BINARY(16) | PK, FK → staff(staff_id) |
| baseSalary | DECIMAL | |
| commissionRate | DECIMAL | |

#### `products`
| Column | Type | Constraints |
|--------|------|-------------|
| product_id | BINARY(16) | PK |
| product_name | VARCHAR(50) | NOT NULL, UNIQUE |
| category | VARCHAR(100) | |
| price | DECIMAL | |
| cost | DECIMAL | |
| image | VARCHAR(255) | |
| status | TINYINT(1) | DEFAULT 1 (1=available, 0=out of stock) |

#### `invoices`
| Column | Type | Constraints |
|--------|------|-------------|
| invoice_id | BINARY(16) | PK |
| staff_id | BINARY(16) | NOT NULL, FK → staff(staff_id) |
| invoice_date | DATE | |
| total | INT | |
| status | TINYINT(1) | DEFAULT 1 (1=paid, 0=unpaid) |

#### `orders`
| Column | Type | Constraints |
|--------|------|-------------|
| order_id | BINARY(16) | PK |
| invoice_id | BINARY(16) | NOT NULL, FK → invoices(invoice_id) |
| staff_id | BINARY(16) | NOT NULL, FK → staff(staff_id) |
| order_date | DATE | |
| total | INT | |

#### `orders_details`
| Column | Type | Constraints |
|--------|------|-------------|
| order_id | BINARY(16) | PK, FK → orders(order_id) |
| product_id | BINARY(16) | PK, FK → products(product_id) |
| quantity | INT | |

### Entity-Relationship Diagram (ERD)

```mermaid
**erDiagram
    staff ||--o| salaries : "1:1"
    staff ||--o{ invoices : "1:N"
    staff ||--o{ orders : "1:N"
    invoices ||--o{ orders : "1:N"
    orders ||--o{ orders_details : "1:N"
    products ||--o{ orders_details : "1:N"

    staff {
        binary16 staff_id PK
        varchar50 staff_name UK "NOT NULL"
        varchar100 email
        varchar255 password "NOT NULL"
        varchar100 full_name
        enum role "admin|staff"
        tinyint1 status "1=active, 0=layoff"
    }

    salaries {
        binary16 staff_id PK,FK
        decimal baseSalary
        decimal commissionRate
    }

    products {
        binary16 product_id PK
        varchar50 product_name UK "NOT NULL"
        varchar100 category
        decimal price
        decimal cost
        varchar255 image
        tinyint1 status "1=available, 0=out of stock"
    }

    invoices {
        binary16 invoice_id PK
        binary16 staff_id FK "NOT NULL"
        date invoice_date
        int total
        tinyint1 status "1=paid, 0=unpaid"
    }

    orders {
        binary16 order_id PK
        binary16 invoice_id FK "NOT NULL"
        binary16 staff_id FK "NOT NULL"
        date order_date
        int total
    }

    orders_details {
        binary16 order_id PK,FK
        binary16 product_id PK,FK
        int quantity
   ** }
```

#### Relationship Notation

| Symbol | Meaning |
|--------|---------|
| `||--o|` | One-to-One (mandatory → optional) |
| `||--o{` | One-to-Many (mandatory → optional) |
| `PK` | Primary Key |
| `FK` | Foreign Key |
| `UK` | Unique Key |

### Relationship Summary

| # | From | To | Type | Constraint |
|---|------|----|------|------------|
| 1 | `staff` | `salaries` | One-to-One | Shared primary key (`staff_id`) |
| 2 | `staff` | `invoices` | One-to-Many | `invoices.staff_id` → `staff.staff_id` |
| 4 | `staff` | `orders` | One-to-Many | `orders.staff_id` → `staff.staff_id` |
| 5 | `invoices` | `orders` | One-to-Many | `orders.invoice_id` → `invoices.invoice_id` |
| 6 | `orders` | `orders_details` | One-to-Many | `orders_details.order_id` → `orders.order_id` |
| 7 | `products` | `orders_details` | One-to-Many | `orders_details.product_id` → `products.product_id` |

### Referential Integrity

- Deleting a `staff` cascades to `salaries`, `invoices`, and `orders` via Hibernate cascade settings.
- Deleting an `invoice` cascades to its related `orders`.
- Deleting an `order` cascades to its `orders_details`.
- Deleting a `product` is restricted if referenced in `orders_details`.

## Setup & Running

1. **Prerequisites:** Java 21, Maven, MySQL 8+
2. **Database:** Create a MySQL database named `StoreManagement`
3. **Configure:** Edit `src/main/resources/hanabi/backend/hibernate.cfg.xml` if your MySQL credentials differ (default: root with no password)
4. **Build:** `mvn clean install`
5. **Run:** `mvn exec:java` or run `App.java` from your IDE

## Build Artifact

The Maven POM is configured to produce an executable JAR with `App` as the main class:

```bash
mvn clean package
java -jar target/DACN-1-1.0-SNAPSHOT.jar
```
