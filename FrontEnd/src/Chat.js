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
  const [onlineUsers, setOnlineUsers] = useState([]);
  const [userLeftMessages, setUserLeftMessages] = useState([]);

  // Show received messages
  function showMessage(temp) {
    setMessages((prev) => [temp.body, ...prev]);
    console.log("📩 Tin nhắn sau cập nhật:", messages);
  }

  // Show updated list of online users
  function showOnlineUsers(users) {
    setOnlineUsers(users);
  }

  useEffect(() => {
    const username = localStorage.getItem("username");

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

        // Subscribe to online users list
        client.subscribe("/topic/onlineUsers", (response) => {
          const users = JSON.parse(response.body);
          showOnlineUsers(users);
        });

        client.subscribe("/topic/userLeft", (response) => {
          const userLeft = JSON.parse(response.body);
          console.log("🚪 Người dùng rời nhóm:", userLeft);
          setUserLeftMessages((prev) => [...prev, userLeft]);
        });

        // Notify server about the new user joining
        client.publish({
          destination: "/app/chat.addUser",
          body: JSON.stringify({ sender: username }),
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

      {/* Sidebar for online users */}
      <div className="online-users">
        <h3>Online Users</h3>
        <ul>
          {Array.isArray(onlineUsers) && onlineUsers.length > 0 ? (
            onlineUsers.map((user, index) => <li key={index}>{user}</li>)
          ) : (
            <li>Không có ai online</li>
          )}
        </ul>
      </div>

      <div className="chat-messages">
        {/* 🛑 Hiển thị danh sách người rời nhóm */}
        {userLeftMessages.map((user, index) => (
          <div key={index} className="user-left-message">
            🚪 <b>{user.sender}</b> đã rời nhóm
          </div>
        ))}

        {/* 📨 Hiển thị tin nhắn chat */}
        {messages.map((msg, index) => {
          if (!msg || typeof msg !== "object") {
            console.warn("❗ Tin nhắn không hợp lệ:", msg);
            return null;
          }

          return (
            <div
              key={index}
              className={`message ${
                msg.sender === localStorage.getItem("username")
                  ? "message-sent"
                  : "message-received"
              }`}
            >
              <div className="message-info">
                <b>{msg.sender || "Ẩn danh"}</b> • {msg.time || "N/A"}
              </div>
              {msg.text && <div className="message-text">{msg.text}</div>}
            </div>
          );
        })}
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
