package neu.edu.webapp.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Date;

@Component
@Entity
@Table(name="Submission")
public class Submission {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false, insertable = false)
    private String id;

//    @ManyToOne
//    @JoinColumn(name = "assignment_Id", referencedColumnName = "id", nullable = false)
//    @JsonIgnore
    @Column(name="assignment_id", nullable = false)
    private String assignment_id;

//    @Transient
//    private String assignment_id;

    @Column(name = "email", nullable = false)
    @JsonIgnore
    private String account_email;

    @Column(name="submission_url",nullable = false)
    private String submission_url;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date submission_date;
    @CreationTimestamp
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date submission_updated;

//    @JsonIgnore
//    private Date submission_Date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

//    public Assignment getAssignment() {
//        return assignment;
//    }

//    public String getAssignment_id() {
//        return assignment_id;
//    }
//
//    public void setAssignment_id(String assignment_id) {
//        this.assignment_id = assignment_id;
//    }

//    public void setAssignment(Assignment assignment) {
//        this.assignment = assignment;
//    }

    public String getSubmission_url() {
        return submission_url;
    }

    public void setSubmission_url(String submission_url) {
        this.submission_url = submission_url;
    }

    public Date getSubmission_date() {
        return submission_date;
    }

    public void setSubmission_date(Date submission_date) {
        this.submission_date = submission_date;
    }

    public Date getSubmission_updated() {
        return submission_updated;
    }

    public void setSubmission_updated(Date submission_updated) {
        this.submission_updated = submission_updated;
    }

    public String getAccount_email() {
        return account_email;
    }

    public void setAccount_email(String account_email) {
        this.account_email = account_email;
    }
//    public Date getSubmission_Date() {
//        return submission_Date;
//    }
//
//    public void setSubmission_Date(Date submission_Date) {
//        this.submission_Date = submission_Date;
//    }


    public String getAssignment_id() {
        return assignment_id;
    }

    public void setAssignment_id(String assignment_id) {
        this.assignment_id = assignment_id;
    }
}
