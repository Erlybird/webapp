package neu.edu.webapp.DAO;

import neu.edu.webapp.Model.Account;
import neu.edu.webapp.Model.Login;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginDAO extends DAO{
    @Autowired
    Login login;

    @Autowired
    Account account;

    public Account retriveAccountfromDB(String Username){
        Account retrivedAccount = null;
        try{
            begin();
            Query query = getSession().createQuery("from Account where email=:email_Id");
            query.setParameter("email_Id",Username);

            retrivedAccount = (Account)query.uniqueResult();
            commit();
        }catch(Exception e){
            System.out.println("Exception in Retreiving Account: " + e.getMessage());
        }finally{
            close();
        }
        return retrivedAccount;
    }


}
