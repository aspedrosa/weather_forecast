package aspedrosa.weatherforecast.services;

import aspedrosa.weatherforecast.domain.DailyForecast;
import aspedrosa.weatherforecast.repositories.CurrentWeatherCache;
import aspedrosa.weatherforecast.repositories.DailyForecastCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public DailyForecast[] forecast(double latitude, double longitude, int days_count) {
        return null;
    }
}
