package neu.edu.webapp.Service;

import neu.edu.webapp.DAO.SubmissionDAO;
import neu.edu.webapp.Model.Assignment;
import neu.edu.webapp.Model.Submission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SubmissionService {

    @Autowired
    SubmissionDAO submissionDAO;

    public boolean addSubmissionService(Submission submission){
        try{
            submissionDAO.saveSubmissionToDB(submission);
        }catch(Exception e){
            System.out.println("Exception in Submission Service  :" + e.getMessage() );
            return false;
        }
        return true;
    }
//
//    public int noOfSubmissionsAlreadyMade(String username, Assignment ass_id){
//
//           int result = submissionDAO.noOfSubmissionsAlreadyMade(username,ass_id);
//        return result;
//    }
}
