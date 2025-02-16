package Chat.Controller;

import Chat.Domain.Message;
import Chat.Domain.User;
import Chat.Repository.UserRepository;
import Chat.Service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        User savedUser = userService.save(user);
        return ResponseEntity.ok(savedUser);
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
        return ResponseEntity.ok().body(message);
    }

}
