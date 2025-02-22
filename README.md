# 💬 Real-time Chat Application

A modern, real-time chat application built with Spring Boot and React, featuring WebSocket communication for instant messaging.

## ✨ Features

- Real-time messaging using WebSocket protocol
- Paginated chat history
- Persistent message storage with PostgreSQL
- Online/offline user status


## 🛠 Technology Stack

### Backend
- **Framework:** Spring Boot
- **Database:** PostgreSQL
- **Real-time Communication:** WebSocket (STOMP protocol)

### Frontend
- **Framework:** React.js
- **State Management:** Redux/Context API
- **WebSocket Client:** SockJS/STOMP.js

## 📋 Prerequisites
- Java 17 
- PostgreSQL
- Maven/Gradle

## 🚀 Getting Started

### Backend Setup

1. Clone the repository
```bash
git clone https://github.com/yourusername/chatapp.git
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
npm run dev
```


## 📸 Screenshots

![image](https://github.com/user-attachments/assets/1588087c-15a9-4200-9aa2-906b0d372d92)

![image](https://github.com/user-attachments/assets/b3c6a496-688f-4791-a74a-7a84b3e19096)

