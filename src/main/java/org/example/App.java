package org.example;

import com.google.gson.Gson;
import org.example.models.Item;
import org.example.models.Offer;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.*;
import java.util.stream.Collectors;

import static spark.Spark.*;

public class App {
    private static final List<Item> items = new ArrayList<>();
    private static final List<Offer> offers = new ArrayList<>();
    private static final Gson gson = new Gson();
    private static final MustacheTemplateEngine mustache = new MustacheTemplateEngine();

    public static void main(String[] args) {
        port(4567);
        staticFiles.location("/public"); // para script.js y styles.css

        // Cargar ítems desde items.json (hardcoded)
        loadItems();

        // === MANEJO DE EXCEPCIONES ===
        notFound((req, res) -> {
            res.type("application/json");
            return gson.toJson(Map.of("error", "404 - Not Found"));
        });

        internalServerError((req, res) -> {
            res.type("application/json");
            return gson.toJson(Map.of("error", "500 - Internal Server Error"));
        });

        exception(Exception.class, (exception, req, res) -> {
            exception.printStackTrace();
            res.status(500);
            res.type("application/json");
            res.body(gson.toJson(Map.of("error", "Server inesperado")));
        });

        // === RUTAS API (JSON) ===
        path("/api", () -> {
            // Lista de ofertas
            get("/offers", (req, res) -> {
                res.type("application/json");
                return gson.toJson(offers);
            });

            // Recibir oferta
            post("/offer", (req, res) -> {
                res.type("application/json");
                Offer offer = gson.fromJson(req.body(), Offer.class);
                offers.add(offer);
                res.status(201);
                return gson.toJson(Map.of("message", "Oferta recibida"));
            });
        });

        // === RUTAS WEB (Mustache) ===
        // Lista de ítems
        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("items", items.stream()
                    .map(i -> Map.of(
                            "id", i.getId(),
                            "name", i.getName(),
                            "price", i.getPrice()
                    ))
                    .collect(Collectors.toList()));
            return mustache.render(new ModelAndView(model, "items.mustache"));
        });

        // Detalle de ítem
        get("/items/:id", (req, res) -> {
            String id = req.params(":id");
            Optional<Item> itemOpt = items.stream()
                    .filter(i -> i.getId().equals(id))
                    .findFirst();

            if (itemOpt.isPresent()) {
                Item item = itemOpt.get();
                Map<String, Object> model = new HashMap<>();
                model.put("id", item.getId());
                model.put("name", item.getName());
                model.put("price", item.getPrice());
                model.put("description", item.getDescription());
                return mustache.render(new ModelAndView(model, "item-detail.mustache"));
            } else {
                res.status(404);
                return "Item not found";
            }
        });
    }

    private static void loadItems() {
        items.add(new Item("item1", "Gorra autografiada por Peso Pluma", "Una gorra autografiada por el famoso Peso Pluma.", "$621.34 USD"));
        items.add(new Item("item2", "Casco autografiado por Rosalía", "Un casco autografiado por la famosa cantante Rosalía, una verdadera MOTOMAMI!", "$734.57 USD"));
        items.add(new Item("item3", "Chamarra de Bad Bunny", "Una chamarra de la marca favorita de Bad Bunny, autografiada por el propio artista.", "$521.89 USD"));
        items.add(new Item("item4", "Guitarra de Fernando Delgadillo", "Una guitarra acústica de alta calidad utilizada por el famoso cantautor Fernando Delgadillo.", "$823.12 USD"));
        items.add(new Item("item5", "Jersey firmado por Snoop Dogg", "Un jersey autografiado por el legendario rapero Snoop Dogg.", "$355.67 USD"));
        items.add(new Item("item6", "Prenda de Cardi B autografiada", "Un crop-top usado y autografiado por la famosa rapera Cardi B. en su última visita a México", "$674.23 USD"));
        items.add(new Item("item7", "Guitarra autografiada por Coldplay", "Una guitarra eléctrica autografiada por la popular banda británica Coldplay, un día antes de su concierto en Monterrey en 2022.", "$458.91 USD"));
    }
}