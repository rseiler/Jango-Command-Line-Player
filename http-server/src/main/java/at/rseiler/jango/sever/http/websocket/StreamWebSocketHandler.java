package at.rseiler.jango.sever.http.websocket;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@SuppressWarnings("PMD.SignatureDeclareThrowsException")
class StreamWebSocketHandler extends TextWebSocketHandler {
    private final WebSocketManager webSocketManager;

    StreamWebSocketHandler(WebSocketManager webSocketManager) {
        this.webSocketManager = webSocketManager;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        webSocketManager.addSession(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        webSocketManager.removeSession(session);
    }
}
