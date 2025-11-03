package org.example;

import com.google.gson.Gson;
import org.example.dao.ItemDao;
import org.example.dao.OrderDao;
import org.example.dao.UserDao;
import org.example.models.Item;
import org.example.models.Order;

import java.util.List;
import java.util.UUID;

import static spark.Spark.*;

public class App {
    public static void main(String[] args) {
        port(4567);
        Gson gson = new Gson();
        UserDao userDao = new UserDao();
        ItemDao itemDao = new ItemDao();
        OrderDao orderDao = new OrderDao();

        // Inicializa DB con datos de items.json si vacío (decisión: seed inicial)
        if (itemDao.getAllItems().isEmpty()) {
            List<Item> initialItems = List.of(
                    new Item("item1", "Gorra autografiada por Peso Pluma", "Una gorra autografiada por el famoso Peso Pluma.", "$621.34 USD"),
                    // Añade los demás como en Sprint 1
                    new Item("item7", "Guitarra autografiada por Coldplay", "Una guitarra eléctrica autografiada por la popular banda británica Coldplay, un día antes de su concierto en Monterrey en 2022.", "$458.91 USD")
            );
            // Inserta en DB (añade método insertItem en ItemDao si quieres)
        }

        path("/users", () -> {
            get("", (req, res) -> gson.toJson(userDao.getAllUsers()));
            get("/:id", (req, res) -> {
                User user = userDao.getUserById(req.params(":id"));
                if (user != null) return gson.toJson(user);
                res.status(404);
                return "Not found";
            });
            post("/:id", (req, res) -> {
                User newUser = gson.fromJson(req.body(), User.class);
                newUser.setId(req.params(":id"));
                userDao.addUser(newUser);
                res.status(201);
                return gson.toJson(newUser);
            });
            put("/:id", (req, res) -> {
                User updated = gson.fromJson(req.body(), User.class);
                updated.setId(req.params(":id"));
                userDao.updateUser(updated);
                return gson.toJson(updated);
            });
            options("/:id", (req, res) -> {
                res.status(userDao.existsUser(req.params(":id")) ? 200 : 404);
                return "";
            });
            delete("/:id", (req, res) -> {
                userDao.deleteUser(req.params(":id"));
                res.status(204);
                return "";
            });
        });

        path("/items", () -> {
            // Lista con filtro opcional ?maxPrice=500
            get("", (req, res) -> {
                res.type("application/json");
                String maxPriceStr = req.queryParams("maxPrice");
                List<Item> itemList;
                if (maxPriceStr != null) {
                    double maxPrice = Double.parseDouble(maxPriceStr);
                    itemList = itemDao.getItemsByMaxPrice(maxPrice);
                } else {
                    itemList = itemDao.getAllItems();
                }
                // Solo id, name, price
                List<Object> response = itemList.stream()
                        .map(i -> new Object() { public String id = i.getId(); public String name = i.getName(); public String price = i.getPrice(); })
                        .toList();
                return gson.toJson(response);
            });

            get("/:id", (req, res) -> {
                Item item = itemDao.getItemById(req.params(":id"));
                if (item != null) return gson.toJson(item.getDescription());
                res.status(404);
                return "Not found";
            });

            // Nueva: POST /items/:id/bid — Pujar (actualiza precio si mayor)
            post("/:id/bid", (req, res) -> {
                String id = req.params(":id");
                Item item = itemDao.getItemById(id);
                if (item == null) {
                    res.status(404);
                    return "Item not found";
                }
                double newBid = Double.parseDouble(req.body());  // Asume body es el nuevo precio numérico
                double currentPrice = Double.parseDouble(item.getPrice().replace("$", "").replace(" USD", ""));
                if (newBid > currentPrice) {
                    String newPrice = "$" + newBid + " USD";
                    itemDao.updateItemPrice(id, newPrice);
                    // Opcional: crea order si es compra final, pero para ahora solo actualiza
                    return "Bid accepted: new price " + newPrice;
                } else {
                    res.status(400);
                    return "Bid too low";
                }
            });
        });

        // Rutas para orders (usa las tuyas, integra)
        post("/orders", (req, res) -> {
            Order newOrder = gson.fromJson(req.body(), Order.class);
            newOrder.setId(UUID.randomUUID().toString());
            orderDao.createOrder(newOrder);
            res.status(201);
            return gson.toJson(newOrder);
        });
        get("/orders", (req, res) -> gson.toJson(orderDao.getAllOrders()));

        System.out.println("API with DB running");
    }
}