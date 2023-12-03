package neu.edu.webapp.Model;

import org.springframework.stereotype.Component;

@Component
public class Submission_Request {

    private String submission_url;

    public String getSubmission_url() {
        return submission_url;
    }

    public void setSubmission_url(String submission_url) {
        this.submission_url = submission_url;
    }
}
