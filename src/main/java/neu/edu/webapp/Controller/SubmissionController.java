package neu.edu.webapp.Controller;

import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
import neu.edu.webapp.Model.Assignment;
import neu.edu.webapp.Model.Login;
import neu.edu.webapp.Model.Submission;
import neu.edu.webapp.Model.Submission_Request;
import neu.edu.webapp.Service.AssignmentService;
import neu.edu.webapp.Service.LoginService;
import neu.edu.webapp.Service.SubmissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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


    Logger logger = LoggerFactory.getLogger(SubmissionController.class);
    private final StatsDClient statsd = new NonBlockingStatsDClient("metric", "localhost", 8125 );


    @PostMapping(value = "/v1/assignments/{id}/submission")
    public ResponseEntity<?> addSubmission(@RequestHeader("Authorization") String authHeader,
                                           @RequestBody Submission_Request submission_req_url,
                                           @PathVariable(value = "id") String Id_Assignment){
        statsd.incrementCounter("PostSubmission.executed");
        Login login = loginService.getLoginDetails(authHeader);
        boolean isLoginSuccess = loginService.checkValidUser(login);


        if(isLoginSuccess){

            if(assignmentService.isAssignmentPresent(Id_Assignment)){
                Assignment assignment = assignmentService.getAssignmentFromIdWithoutAccountID(Id_Assignment);
//                Date deadline = assignment.getDeadline();
                long noOfAttempts = assignment.getNum_of_attemps();

                if(!assignmentService.isAssignmentOpen(assignment)){
                    logger.error("Assignment is not open, can't submit");
                    System.out.println("Assignment is not open, can't submit");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Assignment is not open to submissions");
                }
                if(submissionService.noOfSubmissionsAlreadyMade(login.getUserName(), assignment.getId()) >= noOfAttempts){
                    System.out.println("Max Submissions reached, can't submit the submission");
                    logger.error("Max no. of attempts have been crossed, can't make submissions");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Max no. of attempts have been crossed, can't make submissions");

                }
                submission.setAssignment_id(Id_Assignment);
                submission.setSubmission_url(submission_req_url.getSubmission_url());
                submission.setAccount_email(login.getUserName());
                if(submissionService.addSubmissionService(submission)){
                    System.out.println("submission added to DB");
                    submissionService.sendSMS(assignment,submission,login);
                    logger.info("Submission has been created");
                    return ResponseEntity.status(HttpStatus.CREATED).cacheControl(CacheControl.noCache()).body(submission);
                }

            }else {
                System.out.println("Assignment is not present");
                logger.warn("Assignment is not present");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).cacheControl(CacheControl.noCache()).body("Assignmnet is not present");
            }


        }else{
            logger.error("Credentials are not correct");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).cacheControl(CacheControl.noCache()).build();

        }

        //needs to be updated
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).cacheControl(CacheControl.noCache()).build();

    }

    @GetMapping(value = "/v1/assignments/{id}/submission")
    public ResponseEntity<?> getSubmission(@RequestHeader("Authorization") String authHeader,
                                           @RequestBody Submission_Request submission_req_url,
                                           @PathVariable(value = "id") String Id_Assignment) {
        logger.error("Method not allowed");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).cacheControl(CacheControl.noCache()).build();

    }


    @PutMapping(value = "/v1/assignments/{id}/submission")
    public ResponseEntity<?> putSubmission(@RequestHeader("Authorization") String authHeader,
                                           @RequestBody Submission_Request submission_req_url,
                                           @PathVariable(value = "id") String Id_Assignment) {
        logger.error("Method not allowed");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).cacheControl(CacheControl.noCache()).build();

    }

    @DeleteMapping(value = "/v1/assignments/{id}/submission")
    public ResponseEntity<?> delSubmission(@RequestHeader("Authorization") String authHeader,
                                           @RequestBody Submission_Request submission_req_url,
                                           @PathVariable(value = "id") String Id_Assignment) {
        logger.error("Method not allowed");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).cacheControl(CacheControl.noCache()).build();

    }
    }
