package org.example;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Handles WebSocket connections for broadcasting real-time price updates.
 * <p>
 * Clients can connect to "/ws" and receive price changes instantly when new offers are made.
 */
@WebSocket
public class PriceWebSocketHandler {
    private static final Set<Session> sessions = new HashSet<>();
    private static final Gson gson = new Gson();

    /**
     * Triggered when a new client connects via WebSocket.
     *
     * @param session the session associated with the new client
     */
    @OnWebSocketConnect
    public void onConnect(Session session) {
        sessions.add(session);
    }

    /**
     * Triggered when a WebSocket connection is closed.
     *
     * @param session    the client session that was closed
     * @param statusCode the close status code
     * @param reason     the reason for closure
     */
    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        sessions.remove(session);
    }

    /**
     * Triggered when a message is received from a connected client.
     *
     * @param session the session from which the message originated
     * @param message the received message content
     */
    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        // If messages from clients need to be processed, handle them here.
    }

    /**
     * Broadcasts a price update and bidder name to all connected WebSocket sessions.
     *
     * @param itemId the ID of the item whose price was updated
     * @param priceAmount the new numeric amount of the item
     * @param priceString the formatted price string (e.g., "$120.00 USD")
     * @param bidderName the name of the bidder who made the offer
     */
    public static void broadcastPriceUpdate(String itemId, double priceAmount, String priceString, String bidderName) {
        String message = gson.toJson(Map.of(
                "type", "updatePrice",
                "itemId", itemId,
                "priceAmount", priceAmount,
                "price", priceString,
                "bidder", bidderName
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
