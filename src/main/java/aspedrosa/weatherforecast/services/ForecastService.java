package aspedrosa.weatherforecast.services;

import aspedrosa.weatherforecast.domain.Coordinates;
import aspedrosa.weatherforecast.domain.CurrentWeather;
import aspedrosa.weatherforecast.domain.DailyForecast;
import aspedrosa.weatherforecast.domain.Forecast;
import aspedrosa.weatherforecast.repositories.CurrentWeatherCache;
import aspedrosa.weatherforecast.repositories.DailyForecastCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.util.List;

/**
 * Handles the request of forecast data
 */
@Service
public class ForecastService {

    /**
     * Cache repository for current results
     */
    @Autowired
    CurrentWeatherCache current_cache;

    /**
     * Cache repository for daily results
     */
    @Autowired
    DailyForecastCache daily_cache;

    /**
     * Api service with higher priority of use
     */
    @Autowired
    ApixuAPIService primary_api_service;

    /**
     * Api service used if the primary one fails
     */
    @Autowired
    DarkSkyAPIService backup_api_service;

    private Forecast retrieve_from_apis(double latitude, double longitude, int days_count) {
        Forecast forecast;
        try {
            forecast = primary_api_service.forecast(latitude, longitude);
        } catch (RestClientException e) {
            forecast = backup_api_service.forecast(latitude, longitude);
        }

        current_cache.cache_data(new Coordinates(latitude, longitude), forecast.getCurrent_weather());
        daily_cache.cache_data(new Coordinates(latitude, longitude), forecast.getDaily_forecast());

        return new Forecast(
            forecast.getDaily_forecast().subList(0, days_count),
            forecast.getCurrent_weather());
    }

    /**
     * Decides the data source. First uses the cache then the api services,
     *  given priority to the primary, using the backup no in case the primary
     *  fails
     *
     * @param latitude of the location
     * @param longitude of the location
     * @param days_count data of how much days
     *
     * @return forecast results
     */
    public Forecast forecast(double latitude, double longitude, int days_count) {
        Coordinates coords = new Coordinates(latitude, longitude);
        //if i have to retrieve data anyways because of days_count count as miss
        List<DailyForecast> daily_forecasts;

        daily_forecasts = daily_cache.get_cached_data(coords);
        if (daily_forecasts == null) {
            daily_cache.correct_stats();
            current_cache.correct_stats(); // assume if there is no data on daily, there is no data on current

            return retrieve_from_apis(latitude, longitude, days_count);
        }

        CurrentWeather current_weather;
        if (daily_forecasts.size() >= days_count) {
            current_weather = current_cache.get_cached_data(coords);

            if (current_weather == null) {
                current_cache.correct_stats();

                return retrieve_from_apis(latitude, longitude, days_count);
            }

            return new Forecast(daily_forecasts.subList(0, days_count), current_weather);
        }

        daily_cache.correct_stats();
        current_cache.correct_stats();

        return retrieve_from_apis(latitude, longitude, days_count);
    }
}
