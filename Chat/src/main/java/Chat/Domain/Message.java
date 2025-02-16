package Chat.Domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {
    private Long senderId;
    private String sender;
    private String text;
    private String time;

    // ✅ Constructor mặc định
    public Message() {}

    // ✅ Constructor có annotation giúp Jackson parse JSON đúng
    @JsonCreator
    public Message(@JsonProperty("senderId") Long senderId,
                   @JsonProperty("sender") String sender,
                   @JsonProperty("text") String text,
                   @JsonProperty("time") String time) {
        this.senderId = senderId;
        this.sender = sender;
        this.text = text;
        this.time = time;
    }

    // ✅ Getter & Setter đầy đủ
    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    @Override
    public String toString() {
        return "Message{" +
                "senderId=" + senderId +
                ", sender='" + sender + '\'' +
                ", text='" + text + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
