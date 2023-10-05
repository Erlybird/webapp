package neu.edu.webapp.Service;

import neu.edu.webapp.DAO.AssignmentDAO;
import neu.edu.webapp.DAO.LoginDAO;
import neu.edu.webapp.Model.Account;
import neu.edu.webapp.Model.Assignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AssignmentService {
    @Autowired
    AssignmentDAO assignmentDAO;

    @Autowired
    LoginDAO loginDAO;

    public boolean addAssignmentService(Assignment ass) {

        try {
            assignmentDAO.saveAssignmentToDB(ass);
        } catch (Exception e) {
            System.out.print("Exception in adding assignment method: " + e.getMessage());
            return false;
        }
        return true;
    }

    //    public boolean
    public boolean isInvalidPayload(Assignment ass) {
        String name = ass.getName();
        int points = ass.getPoints();
        int attempts = ass.getNum_of_attemps();
        if (!name.isEmpty() && points >= 1 && points <= 100 && attempts >= 1 && attempts <= 100) {
            return false;
        }
        return true;
    }

    public List<Assignment> getAllAssignmentsById(Account acc) {

        return assignmentDAO.retriveAllAssignmentsUsingAccID(acc);


    }

    public Assignment getAssignmentbyId(String id, Account acc) {

        return assignmentDAO.retriveAssignmentById(id, acc);
    }

    public boolean deleteAssignmentbyId(String id, Account account) {

        return assignmentDAO.deleteAssignmentById(id, account);
    }

    public boolean updateAssignment(String id, Assignment assignment, Account account) {
        return assignmentDAO.updateAssignmentById(id, assignment, account);
    }

    public Account fetchUserAccount(String userName) {

        Account account = loginDAO.retriveAccountfromDB(userName);

        return account;

    }
}
