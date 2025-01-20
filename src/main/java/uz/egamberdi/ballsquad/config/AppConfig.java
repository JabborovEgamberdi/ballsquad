package uz.egamberdi.ballsquad.config;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Level;
import java.util.logging.Logger;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public OkHttpClient okHttpClient() {
        Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);
        return new OkHttpClient.Builder().build();
    }

}