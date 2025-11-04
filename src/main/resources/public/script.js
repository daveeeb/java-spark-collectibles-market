$(document).ready(function() {
    // Inicial fetch de items para la página principal
    if ($("#item-list").length) {
        $.get("/items", function(data) {
            renderItems(data);
        });
    }

    // Filtrado desde el formulario
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

    // Clear filter
    $(document).on("click", "#clear-filter", function() {
        $("#min-price").val("");
        $("#max-price").val("");
        $.get("/items", function(data) { renderItems(data); });
    });

    // Oferta (formulario de item-detail)
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
                $("#offer-success").removeClass("hidden");
                form.find("input[type=text], input[type=number]").val("");
                setTimeout(function() { $("#offer-success").addClass("hidden"); }, 3000);
            },
            error: function(jqXHR) {
                $("#offer-error").text(jqXHR.responseText).removeClass("hidden");
                setTimeout(function() { $("#offer-error").addClass("hidden"); }, 3000);
            }
        });
    });

    // Renderizar items desde la API
    function renderItems(items) {
        var template = $("#item-template").html();
        var html = "";
        $.each(items, function(index, item) {
            // item.price puede venir como "$.. USD"
            var view = {
                id: item.id,
                name: item.name,
                price: item.price
            };
            html += Mustache.render(template, view);
        });
        $("#item-list").html(html);
    }

    // WebSocket: recibe actualizaciones de precio
    try {
        var wsProtocol = location.protocol === "https:" ? "wss://" : "ws://";
        var socket = new WebSocket(wsProtocol + location.host + "/ws");
        socket.onmessage = function(event) {
            var message = JSON.parse(event.data);
            if (message.type === "updatePrice") {
                // message.price es string formateado; message.priceAmount es number
                var id = message.itemId;
                var priceString = message.price;
                // Actualiza elemento si existe
                var el = $("#price-" + id);
                if (el.length) {
                    el.text(priceString);
                }
            }
        };

        socket.onopen = function() {
            console.log("WebSocket conectado");
        };
        socket.onerror = function(e) {
            console.warn("WebSocket error", e);
        };
    } catch (e) {
        console.warn("WebSocket no disponible:", e);
    }
});
