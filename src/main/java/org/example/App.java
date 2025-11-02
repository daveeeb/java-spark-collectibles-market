package org.example;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class App {
    public static void main(String[] args) {

        port (4567);
        Gson gson = new Gson();

        //SIMULATION OF USERS DATABASE
        Map<String,String> users = new HashMap<>();
        users.put("1", "Javier");
        users.put("2", "David");
        users.put("3", "Henry");

        //USER ROUTES
        //GET all the users
        get("/users", (req, res) -> {
            res.type("application/json");
            return gson.toJson(users);
        });

        //getting user by id
        get("/users/:id",(req,res) -> {
            String id = req.params(":id");
            res.type("application/json");
            if(users.containsKey(id)){
                return gson.toJson(Map.of("id", id,"name",users.get(id)));
            }else{
                res.status(404);
                return gson.toJson(Map.of("Error","User not found"));
            }
        });

        //POST adding the user by id
        post("/users/:id",(request, response) -> {
            String id = request.params(":id");
            String name = request.body();
            users.put(id,name);
            response.status(201);
            return gson.toJson(Map.of("message", "User created","id",id));
        });

        //Put /users/:id edited/updated the user
        put("/users/:id", (req, res) -> {
            String id = req.params(":id");
            if (users.containsKey(id)) {
                String newName = req.body();
                users.put(id, newName);
                return gson.toJson(Map.of("message", "User updated"));
            } else {
                res.status(404);
                return gson.toJson(Map.of("error", "User not found"));
            }
        });

        //OPTIONS VERIFY EXISTENCE
        options("/users/:id", (req, res) -> {
            String id = req.params(":id");
            boolean exists = users.containsKey(id);
            return gson.toJson(Map.of("exists", exists));
        });

        // DELETE /users/:id DELATE USER
        delete("/users/:id", (req, res) -> {
            String id = req.params(":id");
            if (users.containsKey(id)) {
                users.remove(id);
                return gson.toJson(Map.of("message", "User deleted"));
            } else {
                res.status(404);
                return gson.toJson(Map.of("error", "User not found"));
            }
        });
        System.out.println("Spark Server running on http://localhost:4567/users");
    }
}