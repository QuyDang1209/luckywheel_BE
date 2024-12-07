package burundi.ilucky.config;

import com.paypal.base.rest.APIContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaypalConfig {
    @Value( "${paypal.client.id}")
    private String clientId;

    @Value("EGrZGKF-LLW0kYxiXlSYDpoKMapr1pQdIgP_Bd-Lgn4WT5nbt7l5XsYSD0uGmpyZfyZdxhcHSKi9d4G_")
    private String clientSecret;

    @Value("${paypal.mode}")
    private String mode;

    @Bean
    public APIContext apiContext(){
        return new APIContext(clientId,clientSecret,mode);
    }

}
