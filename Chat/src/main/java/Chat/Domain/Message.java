package Chat.Domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Table(name = "messages")
@Entity
@Getter
@Setter
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender;
    private String receiver;
    private String text;

    @Column(columnDefinition = "timestamp")
    private LocalDateTime time;

    @Column(name = "is_private", nullable = false)
    private boolean isPrivate = false;

    public Message() {}

    @JsonCreator
    public Message(
            @JsonProperty("sender") String sender,
            @JsonProperty("receiver") String receiver,
            @JsonProperty("text") String text,
            @JsonProperty("time") LocalDateTime time,
            @JsonProperty("isPrivate") boolean isPrivate) {
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        this.time = time;
        this.isPrivate = isPrivate;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", text='" + text + '\'' +
                ", time=" + time +
                ", isPrivate=" + isPrivate +
                '}';
    }
}
