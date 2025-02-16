import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router";
import "./Style/Chat.css";
import UserService from "./API/UserApi";
const Chat = () => {
  const navigate = useNavigate();
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState("");
  const [stompClient, setStompClient] = useState(null);
  const [name, setName] = useState(null);
  function showMessage(temp) {
    setMessages((prev) => [temp.body, ...prev]);
  }

  useEffect(() => {
    const username = localStorage.getItem("username");
    setName(username);
    if (!username) {
      navigate("/");
      return;
    }

    const socket = new SockJS("http://localhost:8080/ws");
    const client = new Client({
      webSocketFactory: () => socket,
      debug: (str) => console.log(str),
      reconnectDelay: 5000,
      onConnect: (frame) => {
        client.subscribe("/topic/public", (response) => {
          const receivedMessage = JSON.parse(response.body);
          console.log("📩 Tin nhắn nhận được:", receivedMessage);
          showMessage(receivedMessage);
        });
      },
      onStompError: (frame) => {},
    });

    client.activate();
    setStompClient(client);

    return () => {
      client.deactivate();
    };
  }, [navigate]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    const username = localStorage.getItem("username");
    if (!username) {
      console.error("Username is required.");
      return;
    }

    // Lấy ID người dùng
    const result = await UserService.getId(username);
    if (result.error) {
      console.error(result.error);
      return;
    }

    if (newMessage.trim() && stompClient && stompClient.connected && result) {
      const message = {
        senderId: result.user,
        sender: username,
        text: newMessage,
        time: new Date().toLocaleTimeString([], {
          hour: "2-digit",
          minute: "2-digit",
        }),
      };

      console.log("🚀 JSON gửi lên:", JSON.stringify(message));

      stompClient.publish({
        destination: "/app/chat.sendMessage",
        body: JSON.stringify(message),
      });

      setNewMessage("");
    } else {
      console.error("❌ STOMP connection is not established.");
    }
  };

  return (
    <div className="chat-container">
      <div className="chat-header">
        <h2>Chat Room</h2>
        <div className="online-status">
          <div className="online-dot"></div>
          <span>Online</span>
        </div>
      </div>

      <div className="chat-messages">
        {messages.map((msg, index) => (
          <div
            key={index}
            className={`message ${
              msg.sender === localStorage.getItem("username")
                ? "message-sent"
                : "message-received"
            }`}
          >
            <div className="message-info">
              <b>{msg.sender}</b> • {msg.time || "N/A"}
            </div>
            <div className="message-text">{msg.text}</div>
          </div>
        ))}
      </div>

      <div className="chat-input-container">
        <form onSubmit={handleSubmit} className="chat-input-form">
          <input
            type="text"
            className="chat-input"
            value={newMessage}
            onChange={(e) => setNewMessage(e.target.value)}
            placeholder="Type a message..."
          />
          <button type="submit" className="send-button">
            Send
          </button>
        </form>
      </div>
    </div>
  );
};

export default Chat;
