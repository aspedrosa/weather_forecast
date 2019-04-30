package aspedrosa.weatherforecast.services;

import aspedrosa.weatherforecast.domain.Coordinates;
import aspedrosa.weatherforecast.domain.CurrentWeather;
import aspedrosa.weatherforecast.domain.DailyForecast;
import aspedrosa.weatherforecast.domain.Forecast;
import aspedrosa.weatherforecast.repositories.CurrentWeatherCache;
import aspedrosa.weatherforecast.repositories.DailyForecastCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
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

    final int LATITUDE = 40;
    final int LONGITUDE = -8;
    Coordinates coords;
    List<DailyForecast> daily_forecasts;
    CurrentWeather current_weather;
    Forecast forecast;

    public ForecastServiceTest() {
        coords = new Coordinates(LATITUDE, LONGITUDE);

        daily_forecasts = new ArrayList<>();
        for (int i = 0; i < 2; i++)
            daily_forecasts.add(new DailyForecast());

        current_weather = new CurrentWeather();

        forecast = new Forecast(daily_forecasts, current_weather);
    }

    /**
     * If the primary api fails, use the backup one
     */
    @Test
    public void use_backup() {
        Mockito.when(primary_api.forecast(LATITUDE, LONGITUDE)).thenThrow(RestClientException.class);
        Mockito.when(daily_cache.get_cached_data(coords)).thenReturn(null);
        Mockito.when(backup_api.forecast(LATITUDE, LONGITUDE)).thenReturn(forecast);

        forecast_service.forecast(LATITUDE, LONGITUDE, 1);

        Mockito.verify(primary_api, Mockito.times(1)).forecast(LATITUDE, LONGITUDE);
        Mockito.verify(backup_api, Mockito.times(1)).forecast(LATITUDE, LONGITUDE);
        Mockito.verify(daily_cache, Mockito.times(1)).get_cached_data(coords);
    }

    /**
     * If the primary api works fine, backup api shouldn't be called
     */
    @Test
    public void dont_use_backup() {
        Mockito.when(daily_cache.get_cached_data(coords)).thenReturn(null);
        Mockito.when(primary_api.forecast(LATITUDE, LONGITUDE)).thenReturn(forecast);

        forecast_service.forecast(LATITUDE, LONGITUDE, 1);

        Mockito.verify(primary_api, Mockito.times(1)).forecast(LATITUDE, LONGITUDE);
        Mockito.verify(backup_api, Mockito.times(0)).forecast(LATITUDE, LONGITUDE);
        Mockito.verify(daily_cache, Mockito.times(1)).get_cached_data(coords);
    }

    /**
     * Two consecutive queries to the cache
     *  should lead to the usage of cache on the second query
     *  and storage on the first query
     */
    @Test
    public void cache_usage() {
        Mockito.when(primary_api.forecast(LATITUDE, LONGITUDE)).thenReturn(forecast);
        Mockito.when(daily_cache.get_cached_data(coords))
            .thenReturn(null)
            .thenReturn(daily_forecasts);
        Mockito.when(current_cache.get_cached_data(coords))
            .thenReturn(current_weather);

        forecast_service.forecast(LATITUDE, LONGITUDE, 1);
        forecast_service.forecast(LATITUDE, LONGITUDE, 1);

        Mockito.verify(primary_api, Mockito.times(1))
            .forecast(LATITUDE, LONGITUDE);

        Mockito.verify(daily_cache, Mockito.times(2))
            .get_cached_data(coords);
        Mockito.verify(daily_cache, Mockito.times(1))
            .cache_data(coords, daily_forecasts);
        //The first time there is no data cached on daily, doesn't call get_cached_data on current_cache
        Mockito.verify(current_cache, Mockito.times(1))
            .get_cached_data(coords);
        Mockito.verify(current_cache, Mockito.times(1))
            .cache_data(coords, current_weather);
    }

    /**
     * Two consecutive queries to the cache should lead
     *  to the usage of cache on the second, whoever if on the
     *  second query a higher number of days is requested, cache
     *  fails to give necessary results -> it considers a hit -> needs to
     *  be corrected
     */
    @Test
    public void cache_not_enough() {
        List<DailyForecast> forecasts_longer = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            forecasts_longer.add(new DailyForecast());

        Mockito.when(primary_api.forecast(LATITUDE, LONGITUDE))
            .thenReturn(forecast)
            .thenReturn(new Forecast(forecasts_longer, current_weather));
        Mockito.when(daily_cache.get_cached_data(coords))
            .thenReturn(null)
            .thenReturn(daily_forecasts);

        forecast_service.forecast(LATITUDE, LONGITUDE, 2);
        forecast_service.forecast(LATITUDE, LONGITUDE, 4);

        Mockito.verify(primary_api, Mockito.times(2))
            .forecast(LATITUDE, LONGITUDE);

        Mockito.verify(daily_cache, Mockito.times(2))
            .get_cached_data(coords);
        Mockito.verify(daily_cache, Mockito.times(1))
            .cache_data(coords, daily_forecasts);
        Mockito.verify(daily_cache, Mockito.times(1))
            .cache_data(coords, forecasts_longer);

        Mockito.verify(current_cache, Mockito.times(0))
            .get_cached_data(coords);
        Mockito.verify(current_cache, Mockito.times(2))
            .cache_data(coords, current_weather);
    }

    /**
     * Two consecutive queries to the cache should lead
     *  to the usage of cache on the second, whoever after
     *  fifteen minutes only daily data is available (not expired).
     * Another call to the apis should happen.
     */
    @Test
    public void only_current_not_cached() {
        Mockito.when(primary_api.forecast(LATITUDE, LONGITUDE))
            .thenReturn(forecast)
            .thenReturn(forecast);
        Mockito.when(daily_cache.get_cached_data(coords))
            .thenReturn(null)
            .thenReturn(daily_forecasts);
        Mockito.when(current_cache.get_cached_data(coords))
            .thenReturn(null)
            .thenReturn(null); //after fifteen minutes get_cached_data returns null

        forecast_service.forecast(LATITUDE, LONGITUDE, 2);
        forecast_service.forecast(LATITUDE, LONGITUDE, 2);

        Mockito.verify(primary_api, Mockito.times(2))
            .forecast(LATITUDE, LONGITUDE);

        Mockito.verify(daily_cache, Mockito.times(2))
            .get_cached_data(coords);
        Mockito.verify(daily_cache, Mockito.times(2))
            .cache_data(coords, daily_forecasts);

        Mockito.verify(current_cache, Mockito.times(1))
            .get_cached_data(coords);
        Mockito.verify(current_cache, Mockito.times(2))
            .cache_data(coords, current_weather);
    }
}