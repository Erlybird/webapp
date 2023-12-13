package neu.edu.webapp.DAO;


import neu.edu.webapp.Controller.AssignmentController;
import neu.edu.webapp.Model.Assignment;
import neu.edu.webapp.Model.Submission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.hibernate.query.Query;


@Component
public class SubmissionDAO extends DAO{

 @Autowired
 Submission submission;

 Logger logger = LoggerFactory.getLogger(SubmissionDAO.class);

 public boolean saveSubmissionToDB(Submission submission){
  try{
   begin();
   getSession().save(submission);
   commit();
  }
  catch(Exception e){
   System.out.println("Error in submissionDAO " + e.getMessage());
   logger.error("Exception in saving the submission");

   return false;
  }finally{
   close();
  }
  return true;
 }
//
// public int noOfSubmissionsAlreadyMade(String username, Assignment ass_id){
//  int result = 0;
//  try{
//   begin();
//   Query query = getSession().createQuery("SELECT count(*) from Submission  where assignment=:assignmentId AND account_email=:emailId ");
//   query.setParameter("assignmentId",ass_id.getId());
//   query.setParameter("emailId",username);
//   result = (Integer)query.uniqueResult();
//
//   // to be commented
//   System.out.println(result + " delete in SubmissionDAO");
//
//  }catch(Exception e){
//   System.out.println(e.getMessage());
//  }
//  return result;
// }

 public long noOfSubmissionsAlreadyMade(String username, String ass_id){

   long result =0;
   try{
    begin();
    Query query = getSession().createQuery("SELECT COUNT(*) FROM Submission where assignment_id=:assignmentID AND account_email=:emailId ");
    query.setParameter("assignmentID", ass_id);
    query.setParameter("emailId", username);
    result = (Long) query.uniqueResult();
    commit();

   }catch(Exception e){
    System.out.println(e.getMessage());
    logger.error("Exception in counting the no. of Submissions already made");
   }finally{
    close();
   }
   return result;
 }


 public long totalSubmissionsMadeOfAssignment( String ass_id){

  long result =0;
  try{
   begin();
   Query query = getSession().createQuery("SELECT COUNT(*) FROM Submission where assignment_id=:assignmentID ");
   query.setParameter("assignmentID", ass_id);
   result = (Long) query.uniqueResult();
   commit();

  }catch(Exception e){
   System.out.println(e.getMessage());
   logger.error("Exception in counting the total no. of Submissions of an Assignment with id :" + ass_id);
  }finally{
   close();
  }
  return result;
 }
}
