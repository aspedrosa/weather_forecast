package aspedrosa.weatherforecast.services;

import aspedrosa.weatherforecast.domain.DailyForecast;

/**
 * Base structure for a api service used to get forecast data
 */
public abstract class ForecastAPIService extends APIService {
    /**
     * Makes a call to an external api to get forecast data
     *
     * @param latitude of the location
     * @param longitude of the location
     * @param days_count get data of how much days
     *
     * @return forecast results
     */
    abstract DailyForecast[] forecast(double latitude, double longitude, int days_count);
}
