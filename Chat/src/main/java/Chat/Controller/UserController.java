package Chat.Controller;

import Chat.Domain.User;
import Chat.Repository.UserRepository;
import Chat.Service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        if (userRepository.findByUsername(user.getUsername())!=null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("User already exists!");
        }

        User savedUser = userService.save(user);
        return ResponseEntity.ok(savedUser);
    }


}
