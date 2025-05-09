package Chat.Repository;

import Chat.Domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRespository extends JpaRepository<Message, Integer> {
    Message save(Message message);
    @EntityGraph(attributePaths = {"sender"})
    @Query("SELECT m FROM Message m ORDER BY m.time asc ")
    Page<Message> findAll(Pageable pageable);
    List<Message> findBySenderAndReceiverOrReceiverAndSender(String sender1, String receiver1, String sender2, String receiver2);
}
