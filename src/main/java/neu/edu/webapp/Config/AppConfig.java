package neu.edu.webapp.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@Component
public class AppConfig {

    @Bean
    public SnsClient snsClient(){
        return SnsClient.builder().region(Region.US_EAST_1).build();
    }
}
