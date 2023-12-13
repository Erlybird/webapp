package neu.edu.webapp.Controller;

import com.timgroup.statsd.StatsDClientErrorHandler;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
import neu.edu.webapp.Model.Account;
import neu.edu.webapp.Model.Assignment;
import neu.edu.webapp.Model.Login;
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

import java.util.List;

@RestController
public class AssignmentController {

    @Autowired
    AssignmentService assignmentService;

//    @GetMapping("/v1/assignments")
//    public ResponseEntity<List<Assignment>> getAllAssignmentsByID()

    @Autowired
    LoginService loginService;

    @Autowired
    SubmissionService submissionService;


    Logger logger = LoggerFactory.getLogger(AssignmentController.class);
    private final StatsDClient statsd = new NonBlockingStatsDClient("metric", "localhost", 8125 );

    @PostMapping(value = "/v1/assignments")
    public ResponseEntity<?> addAssignments(@RequestHeader("Authorization") String authHeader, @RequestBody Assignment ass) {
        statsd.incrementCounter("PostAssignments.executed");
        Login login = loginService.getLoginDetails(authHeader);
        boolean isLoginSuccess = loginService.checkValidUser(login);

        if (isLoginSuccess) {
            if (ass == null) {
                //no content
                logger.warn("No assignment, please check the body");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).cacheControl(CacheControl.noCache()).build();
            }

            //wrong content
            if (assignmentService.isInvalidPayload(ass)) {
                logger.warn("Assignment is Invalid, please give proper payLoad");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).cacheControl(CacheControl.noCache()).build();
            }

            //adding the assignment
            Account account = assignmentService.fetchUserAccount(login.getUserName());
            ass.setAccountId(account);
            if (assignmentService.addAssignmentService(ass)) {
                logger.info("Assignment added to DB");
                System.out.println("Assignment added to DB");
            }

        } else {
            //login credentials wrong
            logger.warn("Wrong credentials. Unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).cacheControl(CacheControl.noCache()).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).cacheControl(CacheControl.noCache()).body(ass);
    }

    @GetMapping(value = "v1/assignments")
    public ResponseEntity<List<Assignment>> getAllAssignmentsbyId(@RequestHeader("Authorization") String authHeader) {
        statsd.incrementCounter("GetAllAssignments.executed");

        List<Assignment> allAssignments = null;

        try {
//            if(authHeader == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).cacheControl(CacheControl.noCache()).build();
//            System.out.println(authHeader);
            Login login = loginService.getLoginDetails(authHeader);
            if(login == null) {
                logger.warn("Login credentials are not given. Please authorize correctly");

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).cacheControl(CacheControl.noCache()).build();
            }
            if (loginService.checkValidUser(login)) {
                Account account = assignmentService.fetchUserAccount(login.getUserName());
//                String accountId = account.getId();

                allAssignments = assignmentService.getAllAssignmentsById(account);
            } else {
                logger.warn(" Not a valid user. Give correct credentials");

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).cacheControl(CacheControl.noCache()).build();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        logger.info("All assignments have been outputed.");
        return ResponseEntity.status(HttpStatus.OK).cacheControl(CacheControl.noCache()).body(allAssignments);

    }


    @GetMapping(value = "v1/assignments/{id}")
    public ResponseEntity<Assignment> getAssignmentById(@RequestHeader("Authorization") String authHeader, @PathVariable(value = "id") String assignmentId) {
        statsd.incrementCounter("GetAssignment.executed");

        Assignment assignment = null;

        try {
            Login login = loginService.getLoginDetails(authHeader);

            if (loginService.checkValidUser(login)) {
                Account account = assignmentService.fetchUserAccount(login.getUserName());
                String accountId = account.getId();

                //if the assignment doesn't belong to username

                //get account details from assignmentId => assignment.getAccountId();
                Assignment assignment3 = assignmentService.getAssignmentFromIdWithoutAccountID(assignmentId);
                String usernameOfAssignment = assignment3.getAccountId().getEmail();
                if(!usernameOfAssignment.equals(login.getUserName())){
                    logger.warn("you are not allowed to access this assignment.");

//                    System.out.println(usernameOfAssignment + "   :   "+ login.getUserName());
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).cacheControl(CacheControl.noCache()).build();
                }
                assignment = assignmentService.getAssignmentbyId(assignmentId, account);

                if (assignment == null) {
                    logger.warn("Assignment is not found.");
                    //assignment not found
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).cacheControl(CacheControl.noCache()).build();
                }

            } else {
                //return unauthorized
                logger.warn(" Login credentials are incorrect or unauthorized.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).cacheControl(CacheControl.noCache()).build();
            }
        } catch (Exception e) {
            System.out.println("Exception in getAssignmentById Method " + e.getMessage());
        }
        logger.info("Assignment is outputed");
        return ResponseEntity.status(HttpStatus.OK).cacheControl(CacheControl.noCache()).body(assignment);
    }

    @DeleteMapping(value = "v1/assignments/{id}")
    public ResponseEntity<Void> deleteAssignmentById(@RequestHeader("Authorization") String authHeader, @PathVariable(value = "id") String assignmentId) {
        statsd.incrementCounter("DeleteAssignment.executed");

        boolean assignmentbyId = false;
        try {
            Login login = loginService.getLoginDetails(authHeader);

            if (loginService.checkValidUser(login)) {
                Account account = assignmentService.fetchUserAccount(login.getUserName());

                assignmentbyId = assignmentService.deleteAssignmentbyId(assignmentId, account);
                //if the assignment doesn't belong to username
                if (!assignmentbyId) {
                    logger.warn("Assignment is not present, so couldn't be deleted");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).cacheControl(CacheControl.noCache()).build();
                }
                //get account details from assignmentId => assignment.getAccountId();
                Assignment assignment3 = assignmentService.getAssignmentFromIdWithoutAccountID(assignmentId);
                String usernameOfAssignment = assignment3.getAccountId().getEmail();
                if(!usernameOfAssignment.equals(login.getUserName())){
//                    System.out.println(usernameOfAssignment + "   :   "+ login.getUserName());
                    logger.warn("User Doesn't have access to the assignment");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).cacheControl(CacheControl.noCache()).build();
                }

                if(submissionService.totalSubmissionsMadeOfAssignment(assignmentId) >= 0){
                    logger.error("Assignment couldn't be deleted because there are submissions for this assignment");
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).cacheControl(CacheControl.noCache()).build();

                }

            } else {
                //invalid username and password
                logger.warn("Credentials are not correct ");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).cacheControl(CacheControl.noCache()).build();

            }
        } catch (Exception e) {
            System.out.println("Exception in Logging In" + e.getMessage());
        }
        logger.info("Assignment has been deleted with the id : " + assignmentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).cacheControl(CacheControl.noCache()).build();

    }

    @PutMapping(value = "/v1/assignments/{id}")
    public ResponseEntity<?> updateAssignmentByID(@RequestHeader("Authorization") String authHeader, @RequestBody Assignment assignment,
                                                     @PathVariable(value = "id") String assignmentId) {
        statsd.incrementCounter("UpdateAssignment.executed");

        try {
            Login login = loginService.getLoginDetails(authHeader);

            if (loginService.checkValidUser(login)) {
                Account account = assignmentService.fetchUserAccount(login.getUserName());

                //if the assignment doesn't belong to username

                //get account details from assignmentId => assignment.getAccountId();
                Assignment assignment3 = assignmentService.getAssignmentFromIdWithoutAccountID(assignmentId);
                String usernameOfAssignment = assignment3.getAccountId().getEmail();
                if(!usernameOfAssignment.equals(login.getUserName())){
                    logger.warn("User doesn't have access to the assignment");
//                    System.out.println(usernameOfAssignment + "   :   "+ login.getUserName());
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).cacheControl(CacheControl.noCache()).build();
                }
                if (assignmentService.isInvalidPayload(assignment)) {
                    logger.error("Assignment is invalid , please provide assignment with proper payload");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).cacheControl(CacheControl.noCache()).build();
                }
                if (assignmentService.updateAssignment(assignmentId, assignment, account)) {
                    logger.info("assignment has been updated successfully");
                    System.out.println("Update success");
                } else {
                    logger.warn("Assignment is not found");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).cacheControl(CacheControl.noCache()).build();
                }

            } else {
                //invalid username and password
                logger.warn("Credentials are not correct");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).cacheControl(CacheControl.noCache()).build();

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).cacheControl(CacheControl.noCache()).body(assignment);

    }
}
