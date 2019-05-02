package aspedrosa.weatherforecast.services;

import aspedrosa.weatherforecast.domain.Forecast;

/**
 * Base structure for a api service used to get forecast data
 */
public abstract class ForecastAPIService extends APIService {

    /**
     * Returns a constant of how many days of forecast the api can give
     *
     * @return constant
     */
    public abstract int MAX_DAYS_COUNT();

    /**
     * Makes a call to an external api to get forecast data
     *
     * @param latitude of the location
     * @param longitude of the location
     *
     * @return forecast results
     */
    abstract Forecast forecast(double latitude, double longitude);
}
