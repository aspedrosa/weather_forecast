package aspedrosa.weatherforecast.services;

import aspedrosa.weatherforecast.domain.ForecastResult;
import org.springframework.stereotype.Service;


/**
 * Forecast api Service that uses Dark Sky API
 */
@Service
public class DarkSkyAPIService extends ForecastAPIService {

    /**
     * @see APIService
     *
     * @param args (double latitude, double longitude) both of the location
     */
    @Override
    public String build_api_url(Object[] args) {
        return "https://api.darksky.net/forecast/" +
            System.getenv("DARK_SKY_APIKEY") +
            "/" +
            args[0] +
            "," +
            args[1] +
            "?units=si";
    }

    /**
     * @see ForecastAPIService
     */
    @Override
    public ForecastResult forecast(double latitude, double longitude, int days_count, int days_offset) {
        return null;
    }
}
