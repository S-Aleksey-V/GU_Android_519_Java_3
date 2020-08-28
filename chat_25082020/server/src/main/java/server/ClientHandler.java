package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.SQLException;

public class ClientHandler {
    Server server;
    Socket socket = null;
    DataInputStream in;
    DataOutputStream out;

    private String nick;
    private String login;

    public ClientHandler(Server server, Socket socket) {
        try {
            this.server = server;
            this.socket = socket;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                try {
                    socket.setSoTimeout(120000);

                    //цикл аутентификации
                    while (true) {
                        String str = in.readUTF();

                        if (str.startsWith("/auth ")) {
                            String[] token = str.split(" ");

                            String newNick = Database.getNickByLoginAndPass(token[1], token[2]);

                            if (newNick != null) {
                                if (!server.isLoginAuthorized(newNick)){
                                    sendMsg("/authok " + newNick);
                                    nick = newNick;
                                    server.subscribe(this);
                                    System.out.printf("Клиент %s подключился \n", nick);
                                    socket.setSoTimeout(0);
                                    break;
                                } else {
                                    sendMsg("С этим логином уже авторизовались");
                                }
                            } else {
                                sendMsg("Неверный логин / пароль");
                            }
                        }

                        if (str.startsWith("/reg ")) {
                            String[] token = str.split(" ");
//                            if (token.length < 4) {
//                                continue;
//                            }
                            Database.setNewUsers(token[1],token[2],token[3]);
//                            if()){
//                                sendMsg("/regresult ok");
//                            }else{
//                                sendMsg("/regresult failed");
//                            }
                        }


                    }

                    //цикл работы
                    while (true) {
                        String str = in.readUTF();

                        if (str.startsWith("/")) {
                            if (str.equals("/end")) {
                                out.writeUTF("/end");
                                break;
                            }

                            if (str.startsWith("/w ")) {
                                String[] token = str.split("\\s", 3);
                                if (token.length < 3) {
                                    continue;
                                }

                                server.privateMsg(this, token[1], token[2]);
                            }

                        } else {
                            server.broadcastMsg(this, str);
                        }

                        if(str.startsWith("/rename ")){
                            String[] token = str.split(" ");
//
                            Database.changeName(token[1],token[2],token[3]);
                            server.unsubscribe(this);
                            server.subscribe(this);
                        }
                    }
                }catch (SocketTimeoutException e){
                    sendMsg("/end");
                }catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("Клиент отключился");
                    server.unsubscribe(this);
                    try {
                        in.close();
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    void sendMsg(String str) {
        try {
            out.writeUTF(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNick() {
        return nick;
    }

    public String getLogin() {
        return nick;
    }
}