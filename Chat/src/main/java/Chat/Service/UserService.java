package Chat.Service;

import Chat.Domain.User;
import Chat.Repository.UserRepository;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public User save(User user) {

        return this.userRepository.save(user);
    }
}
