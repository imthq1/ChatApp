package Chat.Repository;

import Chat.Domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.stereotype.Repository;



@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    User save(User user);
    User findByUsername(String username);
    Page<User> findAll(Pageable pageable);
    boolean existsByUsernameAndPassword(String username, String password);
}
