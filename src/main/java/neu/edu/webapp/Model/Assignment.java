package neu.edu.webapp.Model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Date;

@Component
@Entity
@Table(name = "Assignment")
public class Assignment {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false, insertable = false)
    private String id;

    @Column(nullable=false)
    private String name;
    @Min(value = 1)
    @Max(value = 100)
    @Column(nullable = false)
    private int points;
    @Min(value = 1)
    @Max(value = 100)
    @Column(nullable = false)
    private int num_of_attemps;
//    @CreationTimestamp
    @Column(nullable = false)
    private Date deadline;
    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Date assignment_created;
    @CreationTimestamp
    @Column(nullable = false)
    private Date assignment_updated;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "accountId", referencedColumnName = "id")
    private Account accountId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getNum_of_attemps() {
        return num_of_attemps;
    }

    public void setNum_of_attemps(int num_of_attemps) {
        this.num_of_attemps = num_of_attemps;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Date getAssignment_created() {
        return assignment_created;
    }

    public void setAssignment_created(Date assignment_created) {
        this.assignment_created = assignment_created;
    }

    public Date getAssignment_updated() {
        return assignment_updated;
    }

    public void setAssignment_updated(Date assignment_updated) {
        this.assignment_updated = assignment_updated;
    }

    public Account getAccountId() {
        return accountId;
    }

    public void setAccountId(Account accountId) {
        this.accountId = accountId;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", points=" + points +
                ", num_of_attemps=" + num_of_attemps +
                ", deadline=" + deadline +
                ", assignment_created=" + assignment_created +
                ", assignment_updated=" + assignment_updated +
                '}';
    }
}
