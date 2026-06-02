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
- **PDF Generation:** iTextPDF 5.5.13.5
- **Notifications:** Swing Toast Notifications
- **Date Picker:** Swing DateTime Picker 2.1.3
- **Modal Dialogs:** Modal Dialog 2.6.1

## Features

- **Authentication** — Login/logout with role-based access (admin/staff)
- **Dashboard** — Revenue overview, daily/monthly statistics, best-sellers, recent orders
- **Menu Management** — CRUD for products, grid display with category filtering, status tracking
- **Order Management** — Create and manage orders, order details with product selection
- **Invoice Management** — Invoice creation, payment status tracking, PDF export
- **Staff Management** — Add/edit staff, salary table with commission rates
- **Account Panel** — Staff profile view with order/salary summaries
- **Chat** — Real-time internal messaging between staff members (text & file sharing)
- **Revenue Analytics** — Charts and statistics with date range filtering
- **Salary Formatting** — Compact display (K/M/B/T suffixes)

## Project Structure

```
src/main/java/
├── App.java                    # Application entry point
├── Main.java                   # Main window, navigation & login/logout flow
├── hanabi/
│   ├── components/             # Reusable Swing components
│   │   ├── MainForm.java       # Navigation container
│   │   ├── Menu.java           # Abstract JFrame base for form windows
│   │   ├── PopUp.java          # Custom popup dialogs
│   │   ├── ProductTable.java   # Dynamic product table (columns from DB metadata)
│   │   ├── SalaryTablePanel.java
│   │   └── form/
│   │       ├── AddProductForm.java   # Add product dialog
│   │       └── EditProductForm.java  # Edit/delete product dialog
│   ├── dao/                    # Data access layer (Hibernate queries)
│   │   ├── BaseDAO.java        # Generic CRUD base
│   │   ├── ChatMessageDAO.java
│   │   ├── InvoiceDAO.java
│   │   ├── OrderDAO.java
│   │   ├── OrderDetailDAO.java
│   │   ├── ProductDAO.java
│   │   ├── SalaryDAO.java
│   │   ├── StaffDAO.java
│   │   └── UserDAO.java
│   ├── model/                  # JPA entities
│   │   ├── ChatMessage.java    # Internal messaging
│   │   ├── Invoice.java
│   │   ├── Order.java
│   │   ├── OrderDetail.java
│   │   ├── Product.java
│   │   ├── Salary.java
│   │   ├── Staff.java
│   │   └── User.java           # DTO (not an entity)
│   ├── service/                # Business logic layer
│   │   ├── AccountService.java # Staff management & salary data
│   │   ├── AuthService.java    # Login/logout
│   │   ├── CreateUser.java     # Staff creation form logic
│   │   ├── MenuService.java    # Products & orders
│   │   └── RevenueService.java
│   ├── util/                   # Utilities
│   │   ├── DataInitializer.java   # Seed data on first run
│   │   ├── DBInitializer.java     # Database creation
│   │   ├── FontLoader.java
│   │   ├── global.java
│   │   ├── HibernateUtil.java     # SessionFactory singleton
│   │   ├── PasswordUtil.java      # SHA-256 password hashing
│   │   ├── TableDBInitializer.java
│   │   └── UIUtils.java
│   └── view/                   # UI panels
│       ├── Auth/
│       │   ├── Banner.java
│       │   └── LoginPanel.java
│       └── ui/
│           ├── AccountPanel.java
│           ├── CategoryPanel.java   # Sidebar navigation
│           ├── ChatContactPanel.java
│           ├── ChatConversationPanel.java
│           ├── ChatPanel.java
│           ├── DashboardView.java
│           ├── MenuItemsPanel.java
│           ├── OrdersPanel.java
│           └── RevenuePanel.java
├── schemas/                    # Reference SQL DDL
│   ├── chat_messages.sql
│   ├── invoice.sql
│   ├── order_details.sql
│   ├── orders.sql
│   ├── products.sql
│   ├── salaries.sql
│   └── staff.sql
src/main/resources/
├── hanabi/
│   ├── assets/                 # Icons, images (SVG/PNG)
│   │   ├── icon/
│   │   ├── img/
│   │   └── Fonts/
│   ├── backend/
│   │   └── hibernate.cfg.xml   # Hibernate configuration
│   ├── config/
│   └── themes/
├── library/                    # Local JAR dependencies
│   ├── swing-toast-notifications-1.0.4.jar
│   └── swing-crazy-panel-1.0.0.jar
└── Fonts/
```

## Database

- **Name:** `StoreManagement`
- **Host:** `localhost:3306`
- **Engine:** MySQL 8+
- **ORM:** Hibernate `hbm2ddl.auto = none` — tables are created by `TableDBInitializer` on startup.
- All primary/foreign keys use `BINARY(16)` for UUID storage.

### Entity-Relationship Diagram

```
erDiagram
    staff ||--o| salaries : "1:1"
    staff ||--o{ invoices : "1:N"
    staff ||--o{ orders : "1:N"
    staff ||--o{ chat_messages : "1:N" (as sender)
    staff ||--o{ chat_messages : "1:N" (as receiver)
    invoices ||--o{ orders : "1:N"
    orders ||--o{ orders_details : "1:N"
    products ||--o{ orders_details : "1:N"
```

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

Index: `idx_invoices_staff` on `staff_id`

#### `orders`
| Column | Type | Constraints |
|--------|------|-------------|
| order_id | BINARY(16) | PK |
| invoice_id | BINARY(16) | NOT NULL, FK → invoices(invoice_id) |
| staff_id | BINARY(16) | NOT NULL, FK → staff(staff_id) |
| status | TINYINT(1) | DEFAULT 1 (1=active, 0=cancelled) |
| order_date | DATE | |
| total | INT | |

Indexes: `idx_orders_staff` on `staff_id`, `idx_orders_invoice` on `invoice_id`

#### `orders_details`
| Column | Type | Constraints |
|--------|------|-------------|
| order_id | BINARY(16) | PK, FK → orders(order_id) |
| product_id | BINARY(16) | PK, FK → products(product_id) |
| quantity | INT | |

Index: `idx_od_product` on `product_id`

#### `chat_messages`
| Column | Type | Constraints |
|--------|------|-------------|
| message_id | BINARY(16) | PK |
| sender_id | BINARY(16) | NOT NULL, FK → staff(staff_id) |
| receiver_id | BINARY(16) | NOT NULL, FK → staff(staff_id) |
| content | TEXT | NOT NULL |
| message_type | VARCHAR(10) | DEFAULT 'TEXT' |
| file_path | VARCHAR(500) | |
| created_at | DATETIME | NOT NULL |

Indexes: `idx_chat_sender` on `sender_id`, `idx_chat_receiver` on `receiver_id`

### Relationship Summary

| # | From | To | Type | Constraint |
|---|------|----|------|------------|
| 1 | `staff` | `salaries` | One-to-One | Shared primary key (`staff_id`) |
| 2 | `staff` | `invoices` | One-to-Many | `invoices.staff_id` → `staff.staff_id` |
| 3 | `staff` | `orders` | One-to-Many | `orders.staff_id` → `staff.staff_id` |
| 4 | `staff` | `chat_messages` | One-to-Many | `chat_messages.sender_id` → `staff.staff_id` |
| 5 | `staff` | `chat_messages` | One-to-Many | `chat_messages.receiver_id` → `staff.staff_id` |
| 6 | `invoices` | `orders` | One-to-Many | `orders.invoice_id` → `invoices.invoice_id` |
| 7 | `orders` | `orders_details` | One-to-Many | `orders_details.order_id` → `orders.order_id` |
| 8 | `products` | `orders_details` | One-to-Many | `orders_details.product_id` → `products.product_id` |

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
