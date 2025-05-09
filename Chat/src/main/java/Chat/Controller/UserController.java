package Chat.Controller;

import Chat.Domain.Message;
import Chat.Domain.Request.UserDTO;
import Chat.Domain.User;
import Chat.Repository.UserRepository;
import Chat.Service.ChatService;
import Chat.Service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final ChatService chatService;
    private final Set<String> onlineUsers = ConcurrentHashMap.newKeySet();

    private final SimpMessagingTemplate messagingTemplate;

    public UserController(UserService userService, UserRepository userRepository, SimpMessagingTemplate messagingTemplate, ChatService chatService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserDTO user) {
            if(this.userRepository.findByUsername(user.getUsername()) != null) {
                return ResponseEntity.badRequest().body("Username already exists");
            }

            User savedUser = userService.save(user);
            return ResponseEntity.ok(savedUser);

    }

    @GetMapping("/getAllUser")
    public ResponseEntity<?> getAllUser(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);

        return ResponseEntity.ok(users);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO user) {
        if(this.userRepository.existsByUsernameAndPassword(user.getUsername(), user.getPassword())) {
            return ResponseEntity.ok(user);
        }
        else{
            return ResponseEntity.badRequest().body("Username or password is incorrect");
        }
    }
    @GetMapping("/getUser")
    public ResponseEntity<?> getUser(@RequestParam(name = "username") String username) {
        User user = userRepository.findByUsername(username);
        return ResponseEntity.ok(user.getId());
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload Message message) {
        System.out.println("📩 Received message: " + message);
        chatService.save(message);

        if (message.isPrivate()) {
            if (message.getReceiver() != null) {
                System.out.println("📤 Sending private message to receiver: /user/" + message.getReceiver() + "/queue/messages");
                messagingTemplate.convertAndSend("/user/" + message.getReceiver() + "/queue/messages", message);
            } else {
                System.out.println("⚠️ Receiver is null for private message");
            }
            if (message.getSender() != null) {
                System.out.println("📤 Sending private message to sender: /user/" + message.getSender() + "/queue/messages");
                messagingTemplate.convertAndSend("/user/" + message.getSender() + "/queue/messages", message);
            } else {
                System.out.println("⚠️ Sender is null for private message");
            }
        } else {
            System.out.println("📤 Sending public message to /topic/public");
            messagingTemplate.convertAndSend("/topic/public", message);
        }
    }


    @MessageMapping("/chat.addUser")
    public void addUser(@Payload Map<String, String> payload, SimpMessageHeaderAccessor headerAccessor) {
        String username = payload.get("username");
        System.out.println("📥 Received addUser payload: " + payload);
        if (username != null) {
            headerAccessor.getSessionAttributes().put("username", username);
            onlineUsers.add(username);

            System.out.println("📤 Sending onlineUsers: " + onlineUsers);
            messagingTemplate.convertAndSend("/topic/onlineUsers", onlineUsers);

            Message systemMessage = new Message();
            systemMessage.setSender("System");
            systemMessage.setText(username + " has joined the chat");
            systemMessage.setTime(LocalDateTime.now());
            systemMessage.setPrivate(false);

            System.out.println("📤 Sending system message: " + systemMessage);
            messagingTemplate.convertAndSend("/topic/public", systemMessage);
        } else {
            System.out.println("⚠️ Username is null in addUser payload");
        }
    }
    @GetMapping("/messages/private")
    public ResponseEntity<?> getPrivateMessages(
            @RequestParam String user1,
            @RequestParam String user2
    ) {
        List<Message> messages = chatService.getPrivateMessages(user1, user2);
        return ResponseEntity.ok(messages);
    }


}
