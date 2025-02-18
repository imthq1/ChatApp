package Chat.Config;

import Chat.Domain.Message;

import Chat.Domain.User;
import Chat.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.sql.Time;
import java.time.LocalTime;


@Component
public class WebSocketEventListener {
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    public WebSocketEventListener(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        User user = userRepository.findByUsername(username);
        if(username != null) {
            logger.info("User Disconnected : " + username);

            Message chatMessage = new Message();
            chatMessage.setSender(user.getUsername());
            chatMessage.setText(user.getUsername() + " đã rời khỏi phòng chat.");
            chatMessage.setTime(LocalTime.now().toString());
            messagingTemplate.convertAndSend("/topic/userLeft", chatMessage);

        }
    }
}
