package aspedrosa.weatherforecast.services;

import aspedrosa.weatherforecast.repositories.CurrentWeatherCache;
import aspedrosa.weatherforecast.repositories.DailyForecastCache;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.*;

public class ForecastServiceIT {

    @TestConfiguration
    static class ForecastServiceITContextConfiguration {
        @Bean
        public ForecastService search_service() {
            return new ForecastService();
        }

        @Bean
        public DarkSkyAPIService backup_api() {
            return new DarkSkyAPIService();
        }

        @Bean
        public ApixuAPIService primary_api() {
            return new ApixuAPIService();
        }

        @Bean
        public DailyForecastCache daily_cache() {
            return new DailyForecastCache();
        }

        @Bean
        public CurrentWeatherCache current_cache() {
            return new CurrentWeatherCache();
        }
    }

    @MockBean
    RestTemplate rest_template;

    @Autowired
    ForecastService forecast_service;

    @Autowired
    DarkSkyAPIService backup_api;

    @Autowired
    ApixuAPIService primary_api;

    @Autowired
    CurrentWeatherCache current_cache;

    @Autowired
    DailyForecastCache daily_cache;

    @Test
    public void forecast() {
    }
}