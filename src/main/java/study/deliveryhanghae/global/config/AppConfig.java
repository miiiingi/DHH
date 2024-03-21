package study.deliveryhanghae.global.config;

import com.siot.IamportRestClient.IamportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    String apiKey = "4377287105131222";
    String secretKey="7GyCjB1boE54qNUte8aT26jepBxbvY12tsh8IgWMxz8XgUWL5AQ69cWQ5ZqVPH1XuQ4su7QHLbVHFfdM";

    @Bean
    public IamportClient iamportClient() {
        return new IamportClient(apiKey, secretKey);
    }
}