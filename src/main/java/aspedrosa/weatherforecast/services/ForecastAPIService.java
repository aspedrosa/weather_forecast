package aspedrosa.weatherforecast.services;

import aspedrosa.weatherforecast.domain.ForecastResult;

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
     * @param days_offset used to now which days form today the user wants
     *
     * @return forecast results
     */
    abstract ForecastResult forecast(double latitude, double longitude, int days_count, int days_offset);
}
