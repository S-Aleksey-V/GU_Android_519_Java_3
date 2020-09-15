package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.util.logging.*;

public class ClientHandler {
    Server server;
    Socket socket = null;
    DataInputStream in;
    DataOutputStream out;

    private String nick;
    private String login;
    private static final Logger logger = Logger.getLogger(ClientHandler.class.getName());


    public ClientHandler(Server server, Socket socket) {

        try {
            Handler fileHandler = new FileHandler("ClientHandler.log",true);
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.FINE);
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false);

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
                            String[] token = str.split("\\s");
                            if (token.length < 3) {
                                continue;
                            }

                            String newNick = server.getAuthService().getNicknameByLoginAndPassword(token[1], token[2]);
                            login = token[1];
                            if (newNick != null) {
                                if (!server.isLoginAuthorized(login)){
                                    sendMsg("/authok " + newNick);
                                    nick = newNick;
                                    server.subscribe(this);
                                    logger.log(Level.SEVERE,nick + " Клиент подключился \n");
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
                            String[] token = str.split("\\s");
                            if (token.length < 4) {
                                continue;
                            }
                            boolean b = server.getAuthService().registration(token[1],token[2],token[3]);
                            if(b){
                                sendMsg("/regresult ok");
                            }else{
                                sendMsg("/regresult failed");
                            }
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
                                logger.log(Level.INFO,this.nick + token[1]+ token[2]);
                            }

                        } else {
                            server.broadcastMsg(this, str);
                        }

                        if(str.startsWith("/rename ")){
                            String[] token = str.split(" ",2);
                            if(token.length < 2){
                                continue;
                            }
                            if(token[1].contains(" ")){
                                sendMsg("Ник не может содержать пробелы");
                                continue;
                            }
                            if(server.getAuthService().ChangeNick(this.nick,token[1])){
                                sendMsg("/yournickis " + token[1]);
                                sendMsg("Ваш ник изменён на " + token[1]);
                                logger.log(Level.INFO,this.nick + " Поменял ник на " + token[1]);
                                this.nick = token[1];
                                server.broadcastClientList();
                            }else{
                                sendMsg("не удалось изменить ник. Ник " + token[1] + " уже существует");
                            }

                        }

                    }
                }catch (SocketTimeoutException e){
                    sendMsg("/end");
                }catch (IOException e) {
                    e.printStackTrace();
                    logger.log(Level.SEVERE,ClientHandler.class.getName(),e);
                }  finally {
                    logger.log(Level.SEVERE, nick + " Клиент отключился" );
                    server.unsubscribe(this);
                    try {
                        in.close();
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        logger.log(Level.SEVERE,ClientHandler.class.getName(),e);
                    }
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        logger.log(Level.SEVERE,ClientHandler.class.getName(),e);
                    }
                }
            }).start();


        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE,ClientHandler.class.getName(),e);
        }


    }

    void sendMsg(String str) {
        try {
            out.writeUTF(str);
            logger.log(Level.INFO,str);
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE,ClientHandler.class.getName(),e);
        }
    }

    public String getNick() {
        return nick;
    }

    public String getLogin() {
        return nick;
    }
}
