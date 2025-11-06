# Java Spark Collectibles Market

Challengue 6: Spring and Spring Boot in Java for Web Applications

## Project Overview

**Java Spark Collectibles Market** is a full-stack web application developed with **Java Spark Framework**.  
It allows users to view, filter, and purchase collectible items.  
The system integrates with a **PostgreSQL database** and features **real-time price updates** using **WebSockets**.

This project was developed in **three sprints**, each focusing on a different part of the system’s functionality.

## Sprints Summary

### **Sprint 1 — Develop Order API**
**Objective:** Create a Java Spark application connected to PostgreSQL that manages collectible orders.

**Key Deliverables:**
- Java Spark project setup.
- `/orders` endpoint to create and list orders.
- PostgreSQL database connection.
- DAO (Data Access Object) implementation for orders.

### **Sprint 2 — Frontend & Integration**
**Objective:** Integrate the API with a web interface using Mustache templates.

**Key Deliverables:**
- HTML templates for items and orders.
- `/items` endpoint rendering data from `items.json`.
- Mustache rendering with routes for viewing item details.
- Initial database integration for item data.

### **Sprint 3 — Filters & Real-Time Updates**
**Objective:** Implement filtering and WebSocket-based price updates.

**Key Deliverables:**
- Price range filters for items (`min_price` and `max_price`).
- Real-time price update notifications using WebSockets.
- Frontend updates with `script.js` and `styles.css` integration.

## Project Structure:
```
java-spark-collectibles-market/
│
├── src/
│ ├── main/
│ │ ├── java/org/example/
│ │ │ ├── App.java
│ │ │ ├── Database.java
│ │ │ ├── models/
│ │ │ │ ├── Item.java
│ │ │ │ ├── Offer.java
│ │ │ │ ├── Order.java
│ │ │ │ └── DAO/
│ │ │ │ ├── ItemDAO.java
│ │ │ │ ├── OfferDAO.java
│ │ │ │ └── OrderDAO.java
│ │ │ ├── PriceWebSocketHandler.java
│ └── resources/
│ │ ├── templates/
│ │ │ ├── item.mustache
│ │ │ ├── item-detail.mustache
│ │ ├── public/
│ │ │ ├── script.js
│ │ │ └── styles.css
│ │ └── items.json
│ │
│ └── test/
|   | org/example/
|   └── AppTest.java
│
├── pom.xml
└── README.md
```

## Technologies Used

| Category | Tool |
|-----------|------|
| Backend Framework | Java Spark 3 |
| Template Engine | Mustache |
| Database | PostgreSQL |
| Real-Time Communication | WebSockets |
| JSON Parsing | Gson |
| Build Tool | Maven |
| Frontend | HTML5, CSS3, JS (Mustache + jQuery) |

## Installation Guide

### 1. Clone the Repository
```bash
git clone https://github.com/your-username/java-spark-collectibles-market.git
cd java-spark-collectibles-market
```

### 2. Configure PostgreSQL
Create a database named `collectiblesdb` and update your credentials in `Database.java`:
```java
private static final String URL = "jdbc:postgresql://localhost:5432/collectiblesdb";
private static final String USER = "postgres";
private static final String PASSWORD = "yourpassword";
```

### 3. Install Dependencies
```bash
mvn clean compile
```

### 4. Run the Application
Make sure you’re in the project root (where `pom.xml` is located):
```bash
mvn exec:java
```

### 5. Access in Browser
```
http://localhost:4567/
```
## API Endpoints

| Method | Endpoint | Description |
|--------|-----------|-------------|
| `GET` | `/items` | Returns all collectible items |
| `GET` | `/items/:id` | Returns an item by ID |
| `GET` | `/items?min_price=X&max_price=Y` | Returns items within the price range |
| `POST` | `/orders` | Creates a new order |
| `WS` | `/ws` | WebSocket endpoint for real-time price updates |

## Main Classes Overview

### **App.java**
Main entry point that initializes Spark, sets routes, and starts the web server on port `4567`.

### **Item.java**
Model representing a collectible item with `id`, `name`, `description`, and `price`.

### **Order.java**
Represents an order entity, including `id`, `itemId`, and `customer info`.

### **Database.java**
Handles PostgreSQL connections and query execution.

### **PriceWebSocketHandler.java**
Implements WebSocket communication for updating prices in real time.

### **item-detail.mustache**
Template rendering detailed information for each collectible item.

## Project Roadmap

| Sprint | Focus | Duration | Deliverables |
|--------|--------|-----------|---------------|
| **Sprint 1** | Backend setup + Orders API | Week 1 | API connected to DB |
| **Sprint 2** | Web UI integration | Week 2 | Mustache templates and item display |
| **Sprint 3** | Filtering + Real-time | Week 3 | WebSocket and dynamic price updates |

---

## Quality Checklist

- Project builds and runs successfully  
- All endpoints respond with valid JSON  
- PostgreSQL connection established  
- Mustache templates render correctly  
- WebSocket updates are real-time  
- API documented and tested in Postman  
- Code organized into models and views  
- README file complete  

## Final Notes
This project demonstrates the integration of **Java Spark**, **PostgreSQL**, and **real-time WebSockets** in a clean, modular architecture suitable for small e-commerce applications.

Javier David Barraza Ureña | NAOID: 3303 
