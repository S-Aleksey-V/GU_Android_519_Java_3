package server;

import java.sql.*;

public class Database {
    private static Connection connection;
    private static Statement statement;

    public static void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:bd.27082020.db");
        statement = connection.createStatement();
    }

    public static void disconnect() {
        try {
            statement.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static Object setNewUsers(String login, String pass, String nick) throws SQLException, ClassNotFoundException {
        connect();
        String sql = String.format("INSERT INTO users (login, password, nickname) VALUES ('%s', '%s', '%s')", login, pass, nick);

        boolean rs = statement.execute(sql);


        return rs;
    }

    public static String getNickByLoginAndPass(String login, String pass) {
        String sql = String.format("SELECT nickname FROM users where login = '%s' and password = '%s'", login, pass);

        try {
            ResultSet rs = statement.executeQuery(sql);

            if (rs.next()) {

                return rs.getString(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String changeName(String login,String password,String newName) {
        String sql = String.format("UPDATE users SET nickname = '%s' WHERE (login = '%s' AND password = '%s') ",newName,login,password);
        try {
            ResultSet rs = statement.executeQuery(sql);

            if (rs.next()) {
//
                return rs.getString(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }


}
