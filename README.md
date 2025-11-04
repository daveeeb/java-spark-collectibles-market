Java Spark for web apps
# Collectibles Market Web Application

## Project Overview
**Collectibles Market** is a 3-sprint Java web project built with **Spark Java**, **PostgreSQL**, and **WebSockets**, designed to sell and manage collectible items online.

It implements a complete MVC-based architecture with RESTful endpoints, PostgreSQL persistence, dynamic Mustache templates, and real-time updates using WebSockets.

## Project Context
After graduating in Systems Engineering, **Rafael** decided to apply his skills by helping his friend **Ramón**, a collector and event organizer, to sell his collectibles online.  
The project evolved through **three sprints**, each introducing new technologies and functionalities toward a production-ready web solution.

## Sprint Overview

| **Sprint** | **Objective** | **Key Deliverables** |
|-------------|----------------|----------------------|
| **Sprint 1** | Set up the API and model structure. | REST API, `Item` and `Offer` models, local JSON data source. |
| **Sprint 2** | Connect to a real PostgreSQL database. | `Database` connection, DAO layer, data persistence. |
| **Sprint 3** | Implement filtering and real-time price updates. | `PriceWebSocketHandler`, filters, Mustache templates. |

## Project Architecture


## User Management (API Endpoints)

| Method | Route | Function |
| :---: | :---: | :--- |
| **GET** | `/users` | Returns all users |
| **GET** | `/users/:id` | Returns a specific user |
| **POST** | `/users/:id` | Adds a new user |
| **PUT** | `/users/:id` | Updates an existing user |
| **OPTIONS** | `/users/:id` | Verifies if the user exists |
| **DELETE** | `/users/:id` | Deletes a user |

## Project structure:
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
│ └── org/example/
│ └── AppTest.java
│
├── pom.xml
└── README.md
```

## Technologies Used

| Category | Tools & Libraries |
|-----------|------------------|
| **Backend** | Java 17, Spark Java Framework |
| **Database** | PostgreSQL, JDBC |
| **Templating** | Mustache |
| **Frontend** | HTML, CSS, JavaScript |
| **Real-time** | WebSocket API |
| **Data Format** | JSON (Gson) |
| **Build Tool** | Maven |
| **Logging** | Logback |

## Main Classes Overview

### `App.java`
- Entry point of the application.  
- Configures routes, templates, and WebSocket endpoints.  
- Initializes the database connection and DAOs.

## Running the Application
Start PostgreSQL and create the collectiblesdb database.
Configure credentials in Database.java.
Run the app:

```
mvn clean compile exec:java
```

Access:
In one page try:
~~~
Web App: http://localhost:4567
~~~~
In the other one try:
~~~
API Endpoint: http://localhost:4567/items
~~~
