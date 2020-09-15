package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import java.util.logging.*;

public class Server {
    private List<ClientHandler> clients;
    private AuthService authService;
    private static final Logger logger = Logger.getLogger(Server.class.getName());


    public AuthService getAuthService() {
        return authService;
    }

    public Server() {
        try {
            Handler fileHandler = new FileHandler("Server.log",true);
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.FINE);
            logger.addHandler(fileHandler);
            logger.setLevel(Level.SEVERE);

        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.setUseParentHandlers(false);


        clients = new Vector<>();


        if(!Database.connect()){
            throw new RuntimeException("Не у далось подключиться к базе данных");
        }
        authService = new DBAuthServise();

        ServerSocket server = null;
        Socket socket;

        final int PORT = 8189;

        try {
            server = new ServerSocket(PORT);
            logger.log(Level.SEVERE,"Сервер запущен!");



            while (true) {
                socket = server.accept();
                logger.log(Level.INFO,"Клиент подключился");
                logger.log(Level.INFO,"socket.getRemoteSocketAddress(): " + socket.getRemoteSocketAddress());
                logger.log(Level.INFO,"socket.getLocalSocketAddress() " + socket.getLocalSocketAddress());

                new ClientHandler(this, socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }  finally {
            Database.disconnect();
            try {
                server.close();
                Database.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                logger.log(Level.SEVERE,Server.class.getName(),e);
            }
        }
    }

    void broadcastMsg(ClientHandler sender, String msg) {
        String message = String.format("%s : %s", sender.getNick(), msg);

        for (ClientHandler client : clients) {
            client.sendMsg(message);
        }
    }

    void privateMsg(ClientHandler sender, String receiver, String msg) {
        String message = String.format("[%s] private [%s] : %s", sender.getNick(), receiver, msg);

        for (ClientHandler c : clients) {
            if(c.getNick().equals(receiver)){
                c.sendMsg(message);
                sender.sendMsg(message);
                return;
            }
        }
        sender.sendMsg(String.format("Client %s not found", receiver));
    }


    public void subscribe(ClientHandler clientHandler) {
        clients.add(clientHandler);
        broadcastClientList();
    }

    public void unsubscribe(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        broadcastClientList();
    }

    public boolean isLoginAuthorized(String login){
        for (ClientHandler c : clients) {
            if(c.getLogin().equals(login)){
                return true;
            }
        }
        return false;
    }

    void broadcastClientList() {
        StringBuilder sb = new StringBuilder("/clientlist ");

        for (ClientHandler c : clients) {
            sb.append(c.getNick()).append(" ");
        }

        String msg = sb.toString();

        for (ClientHandler c : clients) {
            c.sendMsg(msg);
        }
    }


}
