package Chat.Controller;

import Chat.Domain.Message;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final ChatService chatService;
    private final Set<String> onlineUsers = ConcurrentHashMap.newKeySet(); // Set để lưu trữ người dùng online

    private final SimpMessagingTemplate messagingTemplate;

    public UserController(UserService userService, UserRepository userRepository, SimpMessagingTemplate messagingTemplate, ChatService chatService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        User existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser!=null) {
            User updatedUser = existingUser;
            updatedUser.setStatus(user.getStatus());
            userService.save(updatedUser);
            return ResponseEntity.ok(updatedUser);
        } else {
            User savedUser = userService.save(user);
            return ResponseEntity.ok(savedUser);
        }
    }
    @GetMapping("/getAllUser")
    public ResponseEntity<?> getAllUser(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);

        return ResponseEntity.ok(users);
    }
    @GetMapping("/getUser")
    public ResponseEntity<?> getUser(@RequestParam(name = "username") String username) {
        User user = userRepository.findByUsername(username);
        return ResponseEntity.ok(user.getId());
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ResponseEntity<?> sendMessage(@Payload Message message ) {
        System.out.println("📩 Received message: " + message);
        this.chatService.save(message);
        return ResponseEntity.ok().body(message);
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ResponseEntity<Message> addUser(@Payload Message chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());

        onlineUsers.add(chatMessage.getSender());

        // Gửi danh sách online users đến tất cả các client
        messagingTemplate.convertAndSend("/topic/onlineUsers", onlineUsers);


        return ResponseEntity.ok(chatMessage);
    }

}
