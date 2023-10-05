package neu.edu.webapp.DAO;

import neu.edu.webapp.Model.Account;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RegisterAccountDAO extends DAO {

    public void saveAccountToDB(Account acc) {
        try {
            begin();
            getSession().save(acc);
            commit();

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        } finally {
            close();
        }
    }


    public boolean checkEmailIfAlreadyExists(Account acc) {
        try {
            begin();
            String fName = acc.getFirst_name();
            String lName = acc.getLast_name();
            String email = acc.getEmail();

            Query query = getSession().createQuery("From Account where email=:accountEmailId");
            query.setParameter("accountEmailId", email);

            List result = query.list();
            commit();
            if (result.size() == 0) return false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            close();
        }
        return true;
    }


}
