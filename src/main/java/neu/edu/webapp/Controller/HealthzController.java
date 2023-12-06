package neu.edu.webapp.Controller;

import neu.edu.webapp.Service.ConnectionCheckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class HealthzController {
    @Autowired
    private ConnectionCheckService connectionCheckService;

    Logger logger = LoggerFactory.getLogger(HealthzController.class);

    @GetMapping("/healthz")
    public ResponseEntity<Void> checkDatabaseConnection(@RequestParam(name = "inputParam", required = false) String inputParam, @RequestBody(required = false) String inputData) {
        boolean isDbUp = connectionCheckService.isDatabaseConnected();
        logger.warn("Hello World");
//        System.out.println("I'm in IN");
        if (isDbUp) {
            if (inputParam != null && !inputParam.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).cacheControl(CacheControl.noCache()).build(); // returns 400 Bad Request
            }
            if (inputData != null && !inputData.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).cacheControl(CacheControl.noCache()).build(); // returns 400 Bad Request
            } else {
                return ResponseEntity.ok().cacheControl(CacheControl.noCache()).build(); // returns 200 OK
            }

        } else {
            if (inputData != null && !inputData.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).cacheControl(CacheControl.noCache()).build(); // returns 400 Bad Request
            } else {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).cacheControl(CacheControl.noCache()).build(); // return 503
            }
        }
    }

    @RequestMapping(value = "/healthz", method = { RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE })
    public ResponseEntity<Void> otherMappingRequest(@RequestBody(required = false) String inputData) {

        boolean isDbUp = connectionCheckService.isDatabaseConnected();
        if (isDbUp) {
            System.out.println("into this method1");
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).cacheControl(CacheControl.noCache()) //return 405
                    .build();

        } else {
            System.out.println("into this method2");
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).cacheControl(CacheControl.noCache()).build(); // return 405
        }

    }
//
//    @RequestMapping(value = "/**", method = { RequestMethod.GET })
//    public ResponseEntity<Void> requestNotFound() {
//        // For all other requests, return a 404 Not Found without any response body.
//
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).cacheControl(CacheControl.noCache()).build(); //returns 404
//
//
//    }

}
