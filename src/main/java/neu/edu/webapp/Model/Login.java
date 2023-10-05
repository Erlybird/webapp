package neu.edu.webapp.Model;

import org.springframework.stereotype.Component;

@Component
public class Login {

    private String userName;
    private String password;

    public Login() {
    }

    public Login(String username, String password) {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Login{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
