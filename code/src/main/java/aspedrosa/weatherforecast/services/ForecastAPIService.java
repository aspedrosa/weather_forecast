package aspedrosa.weatherforecast.services;

import aspedrosa.weatherforecast.domain.Forecast;

/**
 * Base structure for a api service used to get forecast data
 */
public interface ForecastAPIService extends APIService {

    /**
     * Returns a constant of how many days of forecast the api can give
     *
     * @return constant
     */
    int MAX_DAYS_COUNT();

    /**
     * Makes a call to an external api to get forecast data
     *
     * @param latitude of the location
     * @param longitude of the location
     *
     * @return forecast results
     */
    Forecast forecast(double latitude, double longitude);
}
