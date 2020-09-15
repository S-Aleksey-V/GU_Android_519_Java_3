package server;

public class DBAuthServise implements AuthService {

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        return Database.getNickByLoginAndPass(login, password);
    }

    @Override
    public boolean registration(String login, String password, String nickname) {
        return Database.registration(login, password, nickname);
    }

    @Override
    public boolean ChangeNick(String oldNickname, String newNickAme) {
        return Database.changeNick(oldNickname,newNickAme);
    }
}
