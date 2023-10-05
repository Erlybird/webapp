package neu.edu.webapp.Service;

import neu.edu.webapp.DAO.LoginDAO;
import neu.edu.webapp.Model.Account;
import neu.edu.webapp.Model.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class LoginService {
    @Autowired
    Login login;


    @Autowired
    LoginDAO loginDAO;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public Login getLoginDetails(String header) {


        String userNameFromUser = null;
        String passFromUser = null;

        Login logindetails = new Login();
//        if (header != null && header.startsWith("Basic")) {


            // get the encoded string
            String encodedHeader = header.substring("Basic ".length()).trim();

            //decode the string into username and password
            String decodeString = new String(Base64.getDecoder().decode(encodedHeader), StandardCharsets.UTF_8);
            //set username and password after decryption
            String[] splitDecodeString = decodeString.split(":");

//            System.out.println(decodeString);
            String userName = splitDecodeString[0];
            String passwordFrom = splitDecodeString[1];
//            logindetails.setUserName(userName);
//            logindetails.setPassword(passwordFrom);
//            System.out.println(logindetails.getUserName());
//            System.out.println(logindetails.getPassword());

            String[] splitCredentials = decodeString.split(":", 2);
            userNameFromUser = splitCredentials[0];
            passFromUser = splitCredentials[1];
//            System.out.println("UserName in Loginservice: " + userNameFromUser);
//            System.out.println("Password in Loginservice: " + passFromUser);

            logindetails.setUserName(userNameFromUser);
            logindetails.setPassword(passFromUser);

//        }

        return logindetails;
    }

    public String PasswordHashing(String password) {
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        return encodedPassword;
    }

    public boolean validateCredentials(String username, String password) {
        Account account = loginDAO.retriveAccountfromDB(username);

        if (bCryptPasswordEncoder.matches(password, account.getPassword())) return true;

        return false;

    }

    public boolean checkValidUser(Login login) {
        String userNameFromUser = login.getUserName();
        String passFromUser = login.getPassword();

        if (validateCredentials(userNameFromUser, passFromUser)) {
            return true;
        }
        return false;
    }


}
