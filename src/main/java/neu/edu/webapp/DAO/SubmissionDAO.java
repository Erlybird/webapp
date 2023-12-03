package neu.edu.webapp.DAO;


import neu.edu.webapp.Model.Assignment;
import neu.edu.webapp.Model.Submission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.hibernate.query.Query;


@Component
public class SubmissionDAO extends DAO{

 @Autowired
 Submission submission;

 public boolean saveSubmissionToDB(Submission submission){
  try{
   begin();
   getSession().save(submission);
   commit();
  }
  catch(Exception e){
   System.out.println("Error in submissionDAO " + e.getMessage());
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
}
