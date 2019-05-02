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
    public ApixuAPIService primary_api_service;

    /**
     * Api service used if the primary one fails
     */
    @Autowired
    public DarkSkyAPIService backup_api_service;

    /**
     * Maximum number of days of forecast data
     *  that this service can provide, accordingly to the
     *  external api used
     *
     * @return see method description
     */
    private int max_days_count() {
        if (primary_api_service.MAX_DAYS_COUNT() > backup_api_service.MAX_DAYS_COUNT())
            return primary_api_service.MAX_DAYS_COUNT();
        return backup_api_service.MAX_DAYS_COUNT();
    }

    /**
     * Consults the cache before using an external api.
     * If the cache has the requested data, them just send that data
     *  otherwise makes a call to an external api
     *
     * @param latitude of the location
     * @param longitude of the location
     * @param days_count number of forecast data to send
     * @return data to send
     */
    private Forecast retrieve_from_apis(double latitude, double longitude, int days_count) {
        Forecast forecast;

        /*
         * if the number of requested days is higher than the ones
         *  the primary api can give and the backup api can
         *  give more days, use the backup api
        */
        if (days_count > primary_api_service.MAX_DAYS_COUNT()
            &&
            primary_api_service.MAX_DAYS_COUNT() < backup_api_service.MAX_DAYS_COUNT())
            try {
                forecast = backup_api_service.forecast(latitude, longitude);
            } catch (RestClientException e) {
                forecast = primary_api_service.forecast(latitude, longitude);
            }
        //Otherwise use the primary api
        else
            try {
                forecast = primary_api_service.forecast(latitude, longitude);
            } catch (RestClientException e) {
                forecast = backup_api_service.forecast(latitude, longitude);
            }

        current_cache.cache_data(new Coordinates(latitude, longitude), forecast.getCurrent_weather());
        daily_cache.cache_data(new Coordinates(latitude, longitude), forecast.getDaily_forecast());

        if (days_count > forecast.getDaily_forecast().size())
            return forecast;
        return new Forecast(
            forecast.getDaily_forecast().subList(0, days_count),
            forecast.getCurrent_weather());
    }

    /**
     * Decides the data source. First uses the cache then the api services,
     *  given priority to the primary, using the backup no in case the primary
     *  fails
     * A call to an api retrieves both current and forecast data
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
        if (daily_forecasts == null)
            return retrieve_from_apis(latitude, longitude, days_count);

        CurrentWeather current_weather;
        if (daily_forecasts.size() >= days_count || (daily_forecasts.size() == max_days_count())) {
            current_weather = current_cache.get_cached_data(coords);

            if (current_weather == null)
                return retrieve_from_apis(latitude, longitude, days_count);

            if (days_count > daily_forecasts.size())
                return new Forecast(daily_forecasts, current_weather);
            return new Forecast(daily_forecasts.subList(0, days_count), current_weather);
        }

        daily_cache.correct_stats();

        return retrieve_from_apis(latitude, longitude, days_count);
    }
}
