package aspedrosa.weatherforecast.services;

import aspedrosa.weatherforecast.repositories.CurrentWeatherCache;
import aspedrosa.weatherforecast.repositories.DailyForecastCache;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import static org.junit.Assert.*;

public class ForecastServiceTest {

    @TestConfiguration
    static class ForecastServiceTestContextConfiguration {
        @Bean
        public ForecastService search_service() {
            return new ForecastService();
        }
    }

    @Autowired
    ForecastService forecast_service;

    @MockBean
    DarkSkyAPIService backup_api;

    @MockBean
    ApixuAPIService primary_api;

    @MockBean
    CurrentWeatherCache current_cache;

    @MockBean
    DailyForecastCache daily_cache;

    @BeforeClass
    public static void init() {
        //change timezone so later tests can manipulate time
    }

    /**
     * If the primary api fails, use the backup one
     */
    @Test
    public void use_backup() {
        //mockito.verify
    }

    /**
     * Two consecutive queries to the cache
     *  should lead to the usage of cache on the second query
     *  and storage on the first query
     */
    @Test
    public void cache_usage() {

    }

    /**
     * Two consecutive queries to the cache should lead
     *  to the usage of cache on the second, whoever if on the
     *  second query a higher number of days is requested cached
     *  fails to give necessary results -> consider a hit that needs to
     *  be corrected
     */
    @Test
    public void cache_not_enough() {

    }

    /**
     * Two consecutive queries to the cache should lead
     *  to the usage of cache on the second, whoever after
     *  fifteen minutes only daily data is available (not expired).
     * Another call to the apis should happen.
     */
    @Test
    public void only_current_not_cached() {

    }
}