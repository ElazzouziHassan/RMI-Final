# Simple Chat Application with Java RMI

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Architecture](#architecture)
- [Installation](#installation)
- [Usage](#usage)
- [Technical Challenges](#technical-challenges)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## Introduction

Welcome to our Simple Chat Application developed using Java RMI. This project demonstrates a basic real-time chat system where users can interact through a centralized server. The application leverages Java RMI for efficient client-server communication, offering both public and private messaging functionalities.

![Class Diagram](chat/public/assets/3-chats.png)

## Features

- **Real-time Messaging**: Chat with other users in real time.
- **Public and Private Messages**: Send messages to all users or privately to a specific user.
- **User Nicknames**: Choose a nickname upon connection.
- **Simple User Interface**: Easy-to-use interface for quick interaction.
- **Join/Leave Chat**: Connect or disconnect from the chat server at any time.

## Architecture

The application follows a client-server architecture using Java RMI. Here's a brief overview:

- **ChatServer**: Manages messaging logic and client connections.
- **ChatClient**: Connects to the server and allows user interaction via a graphical interface.
- **ChatUI**: Provides the graphical user interface for users.
- **ConnectedClient**: Manages connected clients and facilitates private messaging.

### UML Diagram
![Class Diagram](chat/public/assets/classdiagramme.png)
### RMI Architecture
![RMI Architecture](chat/public/assets/diagrammedescasdutilisation.png)


## Installation

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Apache Ant (optional, for building the project)

### Steps

1. **Clone the repository**

```bash 
  git clone git@github.com:ElazzouziHassan/RMI-Final.git
```
```bash
  cd RMI-Final
```

2. **Compile the project**
```bash
javac -d bin src/**/*.java
```
3. **Start the RMI registry**
```bash
rmiregistry
```
4. **Run the server** 
```bash
java -cp bin server.ChatServer
``` 
4. **Run the client**  
```bash
java -cp bin client.ChatClient
```

**Note!**: After clonig the project, then from you IDE do the following this steps:
1. Run `ChatServer.java` 
2. Run `ChatUI.java`

## Usage
1. **Start the server**: Ensure the server is running and RMI registry is active.
2. **Launch the client**: Open multiple clients to simulate multiple users.
3. **Enter Nickname**: Enter a unique nickname for each client.
4. **Send Messages**: Use the input field to send public or private messages.
5. **Private Messaging**: select a specific user from the list , write your message and click the button "PRV MSG"

## Echnical Challenges
### Communication Issues
 - **Challenge**: Ensuring reliable communication between clients and the server.
 - **Solution**: Implementing Java RMI for direct and real-time communication.

## Contributing
We welcome contributions to enhance this project! Please follow these steps:

1. **Fork the repository**
2. **Create a new branch**
```bash 
git checkout -b feature/your-feature-name
```
3. **Make your changes**
4. **Commit your changes**
```bash
  git commit -m "Add your feature"
```
5. **Push to your branch**
```bash
 git push origin feature/your-feature-name
```
6. **Create a Pull Request**

## License
This project has been developed for educational purposes and it is open source so anyone is free to use as he wish.

## Contact
For any questions or feedback, please reach out to us at:


- El Azzouzi Hassan
  - Contact : [Email](mailto:ezhassan.info@gmail.com)
  - LinkedIn : [Profil LinkedIn](https://www.linkedin.com/in/elazzouzihassan/)

- Yahia Ezzahri
  - Contact : [Email](mailto:Yahiaezzahri@gmail.com)
  - LinkedIn : [Profil LinkedIn](https://www.linkedin.com/in/yahia-ezzahri-3708b51b4?utm_source=share&utm_campaign=share_via&utm_content=profile&utm_medium=android_app)

- Ettalbi Kabira
  - Contact : [Email](mailto:kabiraettalbi@gmail.com)
  - LinkedIn : [Profil LinkedIn](https://www.linkedin.com/in/kabira-ettalbi/)


