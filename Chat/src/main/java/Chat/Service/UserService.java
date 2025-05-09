package Chat.Service;

import Chat.Domain.Request.UserDTO;
import Chat.Domain.User;
import Chat.Repository.UserRepository;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public User save(UserDTO userDTO) {
            User user = new User();
            user.setUsername(userDTO.getUsername());
            user.setPassword(userDTO.getPassword());
        return this.userRepository.save(user);
    }
}
