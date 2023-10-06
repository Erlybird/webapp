package neu.edu.webapp.Controller;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import neu.edu.webapp.Model.Account;
import neu.edu.webapp.Model.Assignment;
import neu.edu.webapp.Model.Login;
import neu.edu.webapp.Service.AssignmentService;
import neu.edu.webapp.Service.LoginService;
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

    @PostMapping(value = "/v1/assignments")
    public ResponseEntity<Void> addAssignments(@RequestHeader("Authorization") String authHeader, @RequestBody Assignment ass) {

        Login login = loginService.getLoginDetails(authHeader);
        boolean isLoginSuccess = loginService.checkValidUser(login);

        if (isLoginSuccess) {
            if (ass == null) {
                //no content
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).cacheControl(CacheControl.noCache()).build();
            }

            //wrong content
            if (assignmentService.isInvalidPayload(ass)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).cacheControl(CacheControl.noCache()).build();
            }

            //adding the assignment
            Account account = assignmentService.fetchUserAccount(login.getUserName());
            ass.setAccountId(account);
            if (assignmentService.addAssignmentService(ass)) {
                System.out.println("Assignment added to DB");
            }

        } else {
            //login credentials wrong
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).cacheControl(CacheControl.noCache()).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).cacheControl(CacheControl.noCache()).body(null);
    }

    @GetMapping(value = "v1/assignments")
    public ResponseEntity<List<Assignment>> getAllAssignmentsbyId(@RequestHeader("Authorization") String authHeader) {
        List<Assignment> allAssignments = null;

        try {
//            if(authHeader == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).cacheControl(CacheControl.noCache()).build();
//            System.out.println(authHeader);
            Login login = loginService.getLoginDetails(authHeader);
            if(login == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).cacheControl(CacheControl.noCache()).build();
            if (loginService.checkValidUser(login)) {
                Account account = assignmentService.fetchUserAccount(login.getUserName());
//                String accountId = account.getId();

                allAssignments = assignmentService.getAllAssignmentsById(account);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).cacheControl(CacheControl.noCache()).build();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).cacheControl(CacheControl.noCache()).body(allAssignments);

    }


    @GetMapping(value = "v1/assignments/{id}")
    public ResponseEntity<Assignment> getAssignmentById(@RequestHeader("Authorization") String authHeader, @PathVariable(value = "id") String assignmentId) {
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
//                    System.out.println(usernameOfAssignment + "   :   "+ login.getUserName());
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).cacheControl(CacheControl.noCache()).build();
                }
                assignment = assignmentService.getAssignmentbyId(assignmentId, account);

                if (assignment == null) {
                    //assignment not found
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).cacheControl(CacheControl.noCache()).build();
                }

            } else {
                //return unauthorized
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).cacheControl(CacheControl.noCache()).build();
            }
        } catch (Exception e) {
            System.out.println("Exception in getAssignmentById Method " + e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).cacheControl(CacheControl.noCache()).body(assignment);
    }

    @DeleteMapping(value = "v1/assignments/{id}")
    public ResponseEntity<Void> deleteAssignmentById(@RequestHeader("Authorization") String authHeader, @PathVariable(value = "id") String assignmentId) {
        boolean assignmentbyId = false;
        try {
            Login login = loginService.getLoginDetails(authHeader);

            if (loginService.checkValidUser(login)) {
                Account account = assignmentService.fetchUserAccount(login.getUserName());

                assignmentbyId = assignmentService.deleteAssignmentbyId(assignmentId, account);
                //if the assignment doesn't belong to username

                //get account details from assignmentId => assignment.getAccountId();
                Assignment assignment3 = assignmentService.getAssignmentFromIdWithoutAccountID(assignmentId);
                String usernameOfAssignment = assignment3.getAccountId().getEmail();
                if(!usernameOfAssignment.equals(login.getUserName())){
//                    System.out.println(usernameOfAssignment + "   :   "+ login.getUserName());
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).cacheControl(CacheControl.noCache()).build();
                }
                if (!assignmentbyId)
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).cacheControl(CacheControl.noCache()).build();

            } else {
                //invalid username and password
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).cacheControl(CacheControl.noCache()).build();

            }
        } catch (Exception e) {
            System.out.println("Exception in Logging In" + e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).cacheControl(CacheControl.noCache()).build();

    }

    @PutMapping(value = "/v1/assignments/{id}")
    public ResponseEntity<Void> updateAssignmentByID(@RequestHeader("Authorization") String authHeader, @RequestBody Assignment assignment,
                                                     @PathVariable(value = "id") String assignmentId) {
        try {
            Login login = loginService.getLoginDetails(authHeader);

            if (loginService.checkValidUser(login)) {
                Account account = assignmentService.fetchUserAccount(login.getUserName());

                //if the assignment doesn't belong to username

                //get account details from assignmentId => assignment.getAccountId();
                Assignment assignment3 = assignmentService.getAssignmentFromIdWithoutAccountID(assignmentId);
                String usernameOfAssignment = assignment3.getAccountId().getEmail();
                if(!usernameOfAssignment.equals(login.getUserName())){
//                    System.out.println(usernameOfAssignment + "   :   "+ login.getUserName());
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).cacheControl(CacheControl.noCache()).build();
                }
                if (assignmentService.updateAssignment(assignmentId, assignment, account)) {
                    System.out.println("Update success");
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).cacheControl(CacheControl.noCache()).build();
                }

            } else {
                //invalid username and password
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).cacheControl(CacheControl.noCache()).build();

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).cacheControl(CacheControl.noCache()).build();

    }
}
