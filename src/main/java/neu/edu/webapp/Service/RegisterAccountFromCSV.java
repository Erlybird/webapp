package neu.edu.webapp.Service;

import neu.edu.webapp.DAO.RegisterAccountDAO;
import neu.edu.webapp.Model.Account;
import neu.edu.webapp.Validater.EmailValidator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Component
public class RegisterAccountFromCSV {
    @Autowired

    Account acc;
    @Autowired
    RegisterAccountDAO registerAccountDAO;

    @Autowired
    LoginService loginService;

    @Autowired
    EmailValidator emailValidator;




    public void saveDataFromCSV(String csvFilePath) throws IOException {
        try (BufferedReader line_reader = new BufferedReader(new FileReader(csvFilePath))) {
            try (CSVParser records = CSVParser.parse(line_reader, CSVFormat.EXCEL.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

                records.forEach(record -> {
                    String passwordFromCSV = record.get("password");

                    String encryptPassword = loginService.PasswordHashing(passwordFromCSV);


                    //getting details of each record and saving it in Acc object
                acc.setFirst_name(record.get("first_name"));
                acc.setLast_name(record.get("last_name"));
                acc.setEmail(record.get("email"));

                acc.setPassword(encryptPassword);

                //check if the email already exists in
                    String email_Id = acc.getEmail();

                    if(!registerAccountDAO.checkEmailIfAlreadyExists(acc) && emailValidator.validate(email_Id)){

                        registerAccountDAO.saveAccountToDB(acc);
                    }



                });
            }catch(Exception e){
                System.out.println("Could not parse CSV file");
            }
        }catch(Exception e){
            System.out.println("Could not read the CSV file");
        }
    }

}
