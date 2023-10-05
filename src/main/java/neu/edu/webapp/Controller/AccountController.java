package neu.edu.webapp.Controller;

import neu.edu.webapp.Model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    @Autowired
    private Account acc;
//
//    @GetMapping("/userdetails/{id}")
//    public ResponseEntity<Account> getUserDetails(@PathVariable(value="id") String id){
//    return ResponseEntity<acc> ;
//    }
}
