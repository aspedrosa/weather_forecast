package aspedrosa.weatherforecast;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class WeatherforecastApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherforecastApplication.class, args);
    }

    @Bean
    public RestTemplate rest_template() {
        return new RestTemplate();
    }
}
