# Real-Time Chat Application

A modern, real-time chat application built with **Spring Boot** and **React**, leveraging **WebSocket** for instant messaging. This application supports group and private chats, online/offline user status, and persistent message storage, making it ideal for real-time communication needs.

## Features

- **Real-Time Messaging**: Send and receive messages instantly using WebSocket with STOMP protocol.
- **Group and Private Chats**: Engage in public group chats or private one-on-one conversations.
- **Paginated Chat History**: Load chat history efficiently with infinite scrolling.
- **Persistent Storage**: Messages are stored in a PostgreSQL database for reliable retrieval.
- **Online/Offline Status**: View which users are currently online in the chat interface.
- **Responsive UI**: Clean and intuitive interface optimized for both desktop and mobile devices.

## Technology Stack

### Backend
- **Framework**: Spring Boot
- **Database**: PostgreSQL
- **Real-Time Communication**: WebSocket with STOMP protocol
- **Dependencies**: Spring Data JPA, Spring Security (optional for authentication)

### Frontend
- **Framework**: React.js
- **State Management**: React Hooks and Context API
- **WebSocket Client**: SockJS and STOMP.js
- **Styling**: CSS with responsive design


## Getting Started

### Backend Setup

1. Clone the repository
```bash
git clone https://github.com/imthq1/ChatApp
```

2. Configure PostgreSQL
```properties
# src/main/resources/application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/chatapp
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. Run the Spring Boot application
```bash
./mvnw spring-boot:run
```

### Frontend Setup

1. Navigate to the frontend directory
```bash
cd frontend
```

2. Install dependencies
```bash
npm install
```

3. Start the development server
```bash
npm start
```


##  Screenshots
## Login Page
![image](https://github.com/user-attachments/assets/2c511ed7-0cf0-4000-8b91-d270caec3b29)
```
Enter your credentials or sign up to access the chat application.
```

## Sign-Up Page
![image](https://github.com/user-attachments/assets/22478369-5c28-41fc-bc10-1a47551f5f01)

## Chat Page
### Group Chat
![image](https://github.com/user-attachments/assets/f43edc02-c3fe-4b8c-ad72-ddde9d01a291)
```
Participate in real-time group discussions with all online users.
```
### Private Chat
![image](https://github.com/user-attachments/assets/904e16c2-d9e0-4798-8887-36b744d4c47a)
```
Engage in one-on-one conversations with other users.
```
