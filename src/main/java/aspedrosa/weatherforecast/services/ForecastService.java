package aspedrosa.weatherforecast.services;

import aspedrosa.weatherforecast.domain.ForecastResult;
import aspedrosa.weatherforecast.repositories.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Handles the request of forecast data
 */
@Service
public class ForecastService {

    /**
     * Cache repository
     */
    @Autowired
    Cache cache;

    /**
     * Api service with higher priority of use
     */
    @Autowired
    DarkSkyAPIService primary_api_service;

    /**
     * Api service used if the primary one fails
     */
    @Autowired
    OpenWeatherAPIService backup_api_service;

    /**
     * Decides the data source. First uses the cache then the api services,
     *  given priority to the primary, using the backup no in case the primary
     *  fails
     *
     * @param latitude of the location
     * @param longitude of the location
     * @param days_count data of how much days
     * @param days_offset how many days after todays
     *
     * @return forecast results
     */
    public ForecastResult forecast(double latitude, double longitude, int days_count, int days_offset) {
        return null;
    }
}
