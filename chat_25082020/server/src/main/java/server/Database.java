package server;

import java.sql.*;

public class Database {
    private static Connection connection;
    private static PreparedStatement psGetNickname;
    private static PreparedStatement psRegistration;
    private static PreparedStatement psChangeNick;


    private static PreparedStatement psAddMessage;
    private static PreparedStatement psGetMessageForNick;

    public static boolean connect(){
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:bd.27082020.db");
            prepareAllStatements();
            return true;
        } catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
            return false;
        }
    }

    private static void prepareAllStatements() throws SQLException{
        psGetNickname = connection.prepareStatement("SELECT nickname FROM users WHERE login = ? AND password = ?;");
        psRegistration = connection.prepareStatement("INSERT INTO users(login, password, nickname) VALUES (? , ? , ?);");
       psChangeNick = connection.prepareStatement("UPDATE users SET nickname = ? WHERE nickname = ?;");



    }

    public static void disconnect() {

        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    public static boolean registration(String login, String pass, String nick) {
    try {


        psRegistration.setString(1, login);
        psRegistration.setString(2, pass);
        psRegistration.setString(3, nick);
        psRegistration.executeUpdate();
        return true;
    } catch (SQLException e){
        e.printStackTrace();
        return false;
}
    }

    public static String getNickByLoginAndPass(String login, String pass) {

        String nick = null;
        try {
            psGetNickname.setString(1, login);
            psGetNickname.setString(2, pass);
            ResultSet rs = psGetNickname.executeQuery();
            if(rs.next()){
                nick = rs.getString(1);
            }
            rs.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return nick;

    }

    public static boolean changeNick(String oldNickName,String newNickName) {

        try {
            psChangeNick.setString(1, newNickName);
            psChangeNick.setString(2, oldNickName);
            psChangeNick.executeUpdate();
            return true;
        }catch (SQLException e){
            return false;
        }


    }


}
