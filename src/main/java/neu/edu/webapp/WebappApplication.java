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
        registerAccountFromCSV.saveDataFromCSV("C:\\Users\\sangr\\OneDrive - Northeastern University\\Desktop\\opt\\users.csv");
        }catch(Exception e){
            System.out.println("unable to load data from CSV");
        }
    }
    public static void main(String[] args) {
        SpringApplication.run(WebappApplication.class, args);
    }

}
