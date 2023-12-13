package neu.edu.webapp.Controller;

import neu.edu.webapp.Model.Assignment;
import neu.edu.webapp.Model.Login;
import neu.edu.webapp.Model.Submission;
import neu.edu.webapp.Model.Submission_Request;
import neu.edu.webapp.Service.AssignmentService;
import neu.edu.webapp.Service.LoginService;
import neu.edu.webapp.Service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class SubmissionController {

    @Autowired
    LoginService loginService;

    @Autowired
    AssignmentService assignmentService;
    @Autowired
    Submission submission;

    @Autowired
    SubmissionService submissionService;

    @PostMapping(value = "/v1/assignments/{id}/submission")
    public ResponseEntity<?> addSubmission(@RequestHeader("Authorization") String authHeader,
                                           @RequestBody Submission_Request submission_req_url,
                                           @PathVariable(value = "id") String Id_Assignment){
        Login login = loginService.getLoginDetails(authHeader);
        boolean isLoginSuccess = loginService.checkValidUser(login);

        if(isLoginSuccess){

            //write the case for if assignment doesn't exist with the id
            Assignment assignment = assignmentService.getAssignmentFromIdWithoutAccountID(Id_Assignment);
            Date deadline = assignment.getDeadline();
//            submission.setAssignment(assignment);
//            submission.setAssignment_id(assignment.getId());
            submission.setAssignment_id(Id_Assignment);
            submission.setSubmission_url(submission_req_url.getSubmission_url());
            submission.setAccount_email(login.getUserName());

            // if attempts are greater than limit dont' submit the assignment
//            int result = submissionService.noOfSubmissionsAlreadyMade(login.getUserName(), assignment);
            if(submissionService.addSubmissionService(submission)){
                System.out.println("submission added to DB");
                submissionService.sendSMS(assignment,submission,login);
                return ResponseEntity.status(HttpStatus.CREATED).cacheControl(CacheControl.noCache()).body(submission);
            }

        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).cacheControl(CacheControl.noCache()).build();

        }

        //needs to be updated
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).cacheControl(CacheControl.noCache()).build();

    }
}
