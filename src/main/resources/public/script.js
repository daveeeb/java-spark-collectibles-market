/**
 * Handles all client-side logic for the collectibles market.
 * Includes: fetching items, filtering, submitting offers, and
 * listening to WebSocket updates for real-time price changes.
 */
$(document).ready(function() {

    if ($("#item-list").length) {
        $.get("/items", function(data) {
            renderItems(data);
        });
    }

    $(document).on("submit", "#filter-form", function(event) {
        event.preventDefault();
        var minPrice = $("#min-price").val();
        var maxPrice = $("#max-price").val();
        var qs = "?";
        if (minPrice) qs += "min_price=" + minPrice;
        if (maxPrice) {
            if (qs.length > 1) qs += "&";
            qs += "max_price=" + maxPrice;
        }
        $.get("/items" + qs, function(data) {
            renderItems(data);
        });
    });

    $(document).on("click", "#clear-filter", function() {
        $("#min-price").val("");
        $("#max-price").val("");
        $.get("/items", function(data) {
            renderItems(data);
        });
    });

    $(document).on("submit", "#offer-form", function(event) {
        event.preventDefault();
        var form = $(this);
        var url = form.attr("action");
        var formData = {
            id: form.find("input[name=id]").val(),
            buyer: form.find("input[name=buyer]").val(),
            amount: parseFloat(form.find("input[name=amount]").val())
        };

        $.ajax({
            type: "POST",
            url: url,
            data: JSON.stringify(formData),
            contentType: "application/json",
            success: function(response) {
                $("#offer-success").removeClass("hidden").text("Your offer was submitted successfully!");
                form.find("input[type=text], input[type=number]").val("");
                setTimeout(function() { $("#offer-success").addClass("hidden"); }, 3000);
            },
            error: function(jqXHR) {
                $("#offer-error").text("Error: " + jqXHR.responseText).removeClass("hidden");
                setTimeout(function() { $("#offer-error").addClass("hidden"); }, 3000);
            }
        });
    });

    function renderItems(items) {
        var template = $("#item-template").html();
        var html = "";
        $.each(items, function(index, item) {
            var view = {
                id: item.id,
                name: item.name,
                price: item.price,
                buyer: item.buyer || "No bids yet" // default text if no bidder
            };
            html += Mustache.render(template, view);
        });
        $("#item-list").html(html);
    }

    try {
        var wsProtocol = location.protocol === "https:" ? "wss://" : "ws://";
        var socket = new WebSocket(wsProtocol + location.host + "/ws");

        /**
         * Handles messages from the server.
         * Expected message format:
         * {
         *   "type": "updatePrice",
         *   "itemId": "123",
         *   "price": "$250.00 USD",
         *   "buyer": "John Doe"
         * }
         */
        socket.onmessage = function(event) {
            var message = JSON.parse(event.data);

            if (message.type === "updatePrice") {
                var id = message.itemId;
                var priceString = message.price;
                var buyer = message.buyer;

                // Update price
                var priceEl = $("#price-" + id);
                if (priceEl.length) {
                    priceEl.text(priceString);
                }

                // Update best bidder
                var buyerEl = $("#buyer-" + id);
                if (buyerEl.length) {
                    buyerEl.text(buyer ? buyer : "No bids yet");
                }
            }
        };

        socket.onopen = function() {
            console.log("WebSocket connected successfully.");
        };

        socket.onerror = function(e) {
            console.warn("️WebSocket error:", e);
        };

    } catch (e) {
        console.warn("WebSocket not available:", e);
    }
});
