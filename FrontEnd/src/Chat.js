import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import { useEffect, useRef, useState, useCallback } from "react";
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
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const [loading, setLoading] = useState(false);
  const observer = useRef();

  function showMessage(temp) {
    setMessages((prev) => [temp.body, ...prev]);
    console.log("📩 Tin nhắn sau cập nhật:", messages);
  }

  useEffect(() => {
    const username = localStorage.getItem("username");
    if (!username) {
      navigate("/");
      return;
    }

    if (stompClient) return;

    const socket = new SockJS("http://localhost:8080/ws");
    const client = new Client({
      webSocketFactory: () => socket,
      debug: (str) => console.log(str),
      reconnectDelay: 5000,
      onConnect: () => {
        client.subscribe("/topic/public", (response) => {
          const receivedMessage = JSON.parse(response.body);
          console.log("📩 Tin nhắn nhận được:", receivedMessage);
          showMessage(receivedMessage);
        });

        client.subscribe("/topic/onlineUsers", (response) => {
          setOnlineUsers(JSON.parse(response.body));
        });

        client.subscribe("/topic/userLeft", (response) => {
          setUserLeftMessages((prev) => [...prev, JSON.parse(response.body)]);
        });

        client.publish({
          destination: "/app/chat.addUser",
          body: JSON.stringify({ sender: username }),
        });
      },
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
    if (!username || !stompClient || !stompClient.connected) return;

    // Kiểm tra nếu tin nhắn rỗng (chỉ chứa dấu cách)
    if (!newMessage.trim()) {
      alert("Tin nhắn không được để trống!");
      return;
    }

    const result = await UserService.getId(username);
    if (result.error) return;

    const message = {
      senderId: result.user,
      sender: username,
      text: newMessage.trim(),
      time: new Date().toLocaleTimeString([], {
        hour: "2-digit",
        minute: "2-digit",
      }),
    };

    stompClient.publish({
      destination: "/app/chat.sendMessage",
      body: JSON.stringify(message),
    });

    setMessages((prevMessages) => [...prevMessages, message]);
    setNewMessage(""); // Xóa input sau khi gửi
  };

  const loadMessages = async () => {
    if (!hasMore || loading) return; // Chặn nếu đã tải hết hoặc đang tải
    setLoading(true);

    console.log("Đang tải tin nhắn...");
    const result = await UserService.getMessages(page);
    console.log("Result", result);
    if (result.error) {
      console.error(result.error);
      setLoading(false);
      return;
    }

    setMessages((prev) => [...prev, ...result.messages]);
    setPage((prev) => prev + 1);
    setHasMore(result.hasMore);
    setLoading(false);
  };

  const lastMessageRef = useCallback(
    (node) => {
      if (loading || !hasMore) return;
      if (observer.current) observer.current.disconnect();

      observer.current = new IntersectionObserver((entries) => {
        if (entries[0].isIntersecting) {
          loadMessages(); // Gọi API khi cuộn xuống
        }
      });

      if (node) observer.current.observe(node);
    },
    [loading, hasMore]
  );
  return (
    <div className="chat-container">
      <div className="chat-header">
        <h2>Chat Room</h2>
        <div className="online-status">
          <div className="online-dot"></div>
          <span>Online</span>
        </div>
      </div>

      <div className="online-users">
        <h3>Online Users</h3>
        <ul>
          {onlineUsers.length > 0 ? (
            onlineUsers.map((user, index) => <li key={index}>{user}</li>)
          ) : (
            <li>Không có ai online</li>
          )}
        </ul>
      </div>

      <div className="chat-messages">
        {userLeftMessages.map((user, index) => (
          <div key={index} className="user-left-message">
            🚪 <b>{user.sender}</b> đã rời nhóm
          </div>
        ))}
        {messages.map((msg, index) => (
          <div
            ref={index === messages.length - 1 ? lastMessageRef : null}
            key={index}
            className={`message ${
              msg.sender === localStorage.getItem("username")
                ? "message-sent"
                : "message-received"
            }`}
          >
            {msg.sender && msg.time && (
              <div className="message-info">
                <b>{msg.sender}</b> • {msg.time}
              </div>
            )}
            {msg.text && <div className="message-text">{msg.text}</div>}
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
          <button
            type="submit"
            className="send-button"
            disabled={!newMessage.trim()}
          >
            Send
          </button>
        </form>
      </div>
    </div>
  );
};

export default Chat;
