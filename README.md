Java Spark for web apps

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
src/
 └── main/
     ├── java/org/example/
     │   ├── App.java
     │   └── models/
     │       ├── Item.java
     │       └── Offer.java
     └── resources/
         ├── public/
         │   ├── script.js
         │   └── styles.css
         └── templates/
             ├── items.mustache
             └── item-detail.mustache
```
