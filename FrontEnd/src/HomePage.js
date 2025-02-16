import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import UserService from "./API/UserApi";
import "./Style/HomePage.css";
function Login() {
  const [username, setUsername] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    setError("");
    const result = await UserService.createUser(username);

    if (result.error) {
      setError(result.error);
    } else {
      localStorage.setItem("username", result.user.username);
      navigate("/chat");
    }
  };

  return (
    <div className="login-container">
      <div className="login-card">
        <h1 className="login-title">Welcome to ChatApp</h1>

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="username" className="form-label">
              Username
            </label>
            <input
              type="text"
              id="username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              className="form-input"
              placeholder="Enter your username"
              required
            />
          </div>
          {error && <p className="error-message">{error}</p>}{" "}
          {/* Hiển thị lỗi nếu có */}
          <button type="submit" className="submit-button">
            Join Chat
          </button>
        </form>
      </div>
    </div>
  );
}

export default Login;
