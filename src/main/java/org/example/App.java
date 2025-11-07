package org.example;

import com.google.gson.Gson;
import org.example.models.Item;
import org.example.models.Offer;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.*;
import static spark.Spark.*;

/**
 * Main entry point of the Collectibles Market web application.
 * <p>
 * This class configures the Spark Java server, sets up HTTP routes, initializes
 * the WebSocket handler, defines REST API endpoints, and renders web views
 * using Mustache templates. It also handles in-memory collections of items and offers.
 */
public class App {
    /** In-memory list of collectible items available in the marketplace. */
    private static final List<Item> items = new ArrayList<>();

    /** In-memory list of offers made by users. */
    private static final List<Offer> offers = new ArrayList<>();

    /** Gson instance used for JSON serialization and deserialization. */
    private static final Gson gson = new Gson();

    /** Mustache template engine used to render HTML views. */
    private static final MustacheTemplateEngine mustache = new MustacheTemplateEngine();

    /**
     * Main method that starts the Spark web application.
     * <p>
     * This method sets the server port, static file location, WebSocket route,
     * API routes, and web routes for rendering views.
     *
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {
        port(4567);
        staticFiles.location("/public");
        loadItems();

        webSocket("/ws", PriceWebSocketHandler.class);
        init();

        // Error handlers
        notFound((req, res) -> {
            res.type("application/json");
            return gson.toJson(Map.of("error", "404"));
        });
        internalServerError((req, res) -> {
            res.type("application/json");
            return gson.toJson(Map.of("error", "500"));
        });

        /**
         * GET /items
         * <p>
         * Returns all items in JSON format.
         * Supports optional filtering by price range using query parameters
         * `min_price` and `max_price`.
         */
        get("/items", (req, res) -> {
            res.type("application/json");
            String minStr = req.queryParams("min_price");
            String maxStr = req.queryParams("max_price");

            List<Item> filtered = items;
            if (minStr != null || maxStr != null) {
                double min = 0;
                double max = Double.MAX_VALUE;
                try {
                    if (minStr != null && !minStr.isBlank()) min = Double.parseDouble(minStr);
                    if (maxStr != null && !maxStr.isBlank()) max = Double.parseDouble(maxStr);
                } catch (NumberFormatException e) {
                    // If parsing fails, return all items (or could respond with 400)
                }

                final double fmin = min;
                final double fmax = max;

                filtered = items.stream()
                        .filter(i -> {
                            double p = parsePrice(i.getPrice());
                            return p >= fmin && p <= fmax;
                        })
                        .toList();
            }

            // Build JSON payload
            List<Map<String, Object>> payload = new ArrayList<>();
            for (Item i : filtered) {
                Map<String, Object> m = new HashMap<>();
                m.put("id", i.getId());
                m.put("name", i.getName());
                m.put("price", i.getPrice()); // string
                m.put("priceAmount", parsePrice(i.getPrice())); // numeric
                payload.add(m);
            }

            return gson.toJson(payload);
        });

        /**
         * POST /api/offer
         * <p>
         * Receives an offer from a client as JSON.
         * If the offer is higher than the item's current price, updates the item price
         * and broadcasts the change to all WebSocket clients in real time.
         */
        post("/api/offer", (req, res) -> {
            res.type("application/json");
            Offer offer = gson.fromJson(req.body(), Offer.class);
            offers.add(offer);

            Item item = items.stream()
                    .filter(i -> i.getId().equals(offer.getId()))
                    .findFirst()
                    .orElse(null);

            if (item != null && offer.getAmount() > parsePrice(item.getPrice())) {
                double newAmount = offer.getAmount();
                String newPriceString = String.format("$%.2f USD", newAmount);
                item.setPrice(newPriceString);
                PriceWebSocketHandler.broadcastPriceUpdate(offer.getId(), newAmount, newPriceString, offer.getBidderName());
            }
            res.status(201);
            return gson.toJson(Map.of("message", "Oferta recibida"));
        });

        /**
         * GET /
         * <p>
         * Renders the home page showing the list of all items with their current prices.
         */
        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Map<String, Object>> viewItems = new ArrayList<>();
            for (Item i : items) {
                viewItems.add(Map.of(
                        "id", i.getId(),
                        "name", i.getName(),
                        "price", i.getPrice(),
                        "priceAmount", parsePrice(i.getPrice())
                ));
            }
            model.put("items", viewItems);
            return mustache.render(new ModelAndView(model, "items.mustache"));
        });

        /**
         * GET /items/:id
         * <p>
         * Displays the detail page for a specific item identified by ID.
         * If the item is not found, returns a 404 error.
         */
        get("/items/:id", (req, res) -> {
            String id = req.params(":id");
            return items.stream()
                    .filter(i -> i.getId().equals(id))
                    .findFirst()
                    .map(item -> {
                        Map<String, Object> model = new HashMap<>();
                        model.put("id", item.getId());
                        model.put("name", item.getName());
                        model.put("price", item.getPrice());
                        model.put("priceAmount", parsePrice(item.getPrice()));
                        model.put("description", item.getDescription());
                        return mustache.render(new ModelAndView(model, "item-detail.mustache"));
                    })
                    .orElseGet(() -> {
                        res.status(404);
                        return "Not found";
                    });
        });
    }

    /**
     * Parses a price string (e.g., "$120.00 USD") into a numeric double value.
     *
     * @param priceStr String representation of the price
     * @return Parsed double value, or 0 if parsing fails
     */
    private static double parsePrice(String priceStr) {
        if (priceStr == null) return 0;
        try {
            return Double.parseDouble(priceStr.replace("$", "").replace("USD", "").replace(" ", "").trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Loads the initial list of items into memory.
     * This method clears the current list and prepares it for initialization.
     * (Currently empty, should be populated with demo or database data.)
     */
    private static void loadItems() {
        items.clear();
        items.add(new Item("item1", "Gorra autografiada por Peso Pluma", "Una gorra autografiada por el famoso Peso Pluma.", "$621.34 USD"));
        items.add(new Item("item2", "Casco autografiado por Rosalía", "Un casco autografiado por la famosa cantante Rosalía.", "$734.57 USD"));
        items.add(new Item("item3", "Chamarra de Bad Bunny", "Una chamarra autografiada por Bad Bunny.", "$521.89 USD"));
        items.add(new Item("item4", "Guitarra de Fernando Delgadillo", "Guitarra acústica usada por Fernando Delgadillo.", "$823.12 USD"));
        items.add(new Item("item5", "Jersey firmado por Snoop Dogg", "Jersey autografiado por Snoop Dogg.", "$355.67 USD"));
        items.add(new Item("item6", "Prenda de Cardi B autografiada", "Crop-top autografiado por Cardi B.", "$674.23 USD"));
        items.add(new Item("item7", "Guitarra autografiada por Coldplay", "Guitarra eléctrica de Coldplay.", "$458.91 USD"));
    }
}