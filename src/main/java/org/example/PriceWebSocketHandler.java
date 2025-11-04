package org.example;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@WebSocket
public class PriceWebSocketHandler {
    private static final Set<Session> sessions = new HashSet<>();
    private static final Gson gson = new Gson();

    @OnWebSocketConnect
    public void onConnect(Session session) {
        sessions.add(session);
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        sessions.remove(session);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        // Si necesitas recibir mensajes del cliente, procesarlos aquí
    }

    public static void broadcastPriceUpdate(String itemId, double priceAmount, String priceString) {
        String message = gson.toJson(Map.of(
                "type", "updatePrice",
                "itemId", itemId,
                "priceAmount", priceAmount,
                "price", priceString
        ));

        sessions.removeIf(s -> !s.isOpen());
        for (Session s : sessions) {
            try {
                s.getRemote().sendString(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
