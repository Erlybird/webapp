package neu.edu.webapp;

import jakarta.annotation.PostConstruct;
import neu.edu.webapp.Service.RegisterAccountFromCSV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;

@SpringBootApplication
public class WebappApplication {
    @Autowired
    RegisterAccountFromCSV registerAccountFromCSV;
    @PostConstruct
    public void init() throws IOException {
        try{
        registerAccountFromCSV.saveDataFromCSV("/opt/csye6225/users.csv");
        }catch(Exception e){
            System.out.println("unable to load data from CSV");
        }
    }
    public static void main(String[] args) {
//        System.out.println("------------------------");
//        System.out.println(System.getenv("DB") + "   !!!!!!!!came in!!!!!!");
//        System.out.println("------------------------");
        SpringApplication.run(WebappApplication.class, args);
    }

}
