package Chat.Service;

import Chat.Domain.Message;
import Chat.Repository.ChatRespository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatService {
    private final ChatRespository chatRespository;
    public ChatService(ChatRespository chatRespository) {
        this.chatRespository = chatRespository;
    }
    public Message save(Message message) {
        return chatRespository.save(message);
    }
    public Map<String, Object> findAll(int page) {
        int pageSize = 20;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("time").descending());

        List<Message> messages = chatRespository.findAll(pageable).getContent();

        boolean hasMore = messages.size() == pageSize;

        Map<String, Object> response = new HashMap<>();
        response.put("messages", messages);
        response.put("hasMore", hasMore);

        return response;
    }
    public List<Message> getPrivateMessages(String user1, String user2) {
        return this.chatRespository.findBySenderAndReceiverOrReceiverAndSender(user1, user2, user1, user2);
    }

}
