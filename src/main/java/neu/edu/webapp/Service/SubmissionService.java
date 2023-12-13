package neu.edu.webapp.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import neu.edu.webapp.Controller.AssignmentController;
import neu.edu.webapp.DAO.LoginDAO;
import neu.edu.webapp.DAO.SubmissionDAO;
import neu.edu.webapp.Model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import com.fasterxml.jackson.core.JsonProcessingException;


@Component
public class SubmissionService {

    @Autowired
    SubmissionDAO submissionDAO;

    @Autowired
    Submission submission;

    @Autowired
    Assignment assignment;

    @Autowired
    LoginDAO loginDAO;

    @Autowired
    Login login;

    @Autowired
    SnsClient snsClient;

    String topicarn = System.getenv("TopicARN");
    Logger logger = LoggerFactory.getLogger(SubmissionService.class);


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
    public long noOfSubmissionsAlreadyMade(String username, String ass_id){

           long result = submissionDAO.noOfSubmissionsAlreadyMade(username,ass_id);
        return result;
    }

    public void sendSMS(Assignment assignment,Submission submission, Login login){
        SNSData snsData = new SNSData();
        snsData.setSubmissionId(submission.getId());
        snsData.setEmailId(submission.getAccount_email());
        snsData.setAssignmentId(submission.getAssignment_id());
        snsData.setAssignmentName(assignment.getName());
        Account account = loginDAO.retriveAccountfromDB(login.getUserName());
        snsData.setFirstName(account.getFirst_name());
        snsData.setSubmissionDate(submission.getSubmission_date().toString());
        snsData.setSubmissionUrl(submission.getSubmission_url());

        ObjectMapper objectMapper = new ObjectMapper();

        String message= "";
        try {
            message = objectMapper.writeValueAsString(snsData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        PublishRequest publishRequest= PublishRequest.builder()
                .topicArn(topicarn)
                .message(message)
                .build();
        snsClient.publish(publishRequest);
        logger.info("SNS message has been published");

    }

    public long totalSubmissionsMadeOfAssignment( String ass_id){
        return submissionDAO.totalSubmissionsMadeOfAssignment(ass_id);
    }
}
