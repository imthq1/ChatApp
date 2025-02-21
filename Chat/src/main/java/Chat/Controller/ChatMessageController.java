package Chat.Controller;

import Chat.Service.ChatService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ChatMessageController {
    private final ChatService chatService;
    public ChatMessageController(ChatService chatService) {
        this.chatService = chatService;
    }
    @GetMapping("/messages")
    public ResponseEntity<?> getAllMessages(@RequestParam(defaultValue = "0") int page ) {
        return ResponseEntity.ok(this.chatService.findAll(page));
    }
}
