package aspedrosa.weatherforecast.services;

import aspedrosa.weatherforecast.domain.ForecastResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;


/**
 * Forecast api Service that uses Dark Sky API
 */
@Service
public class DarkSkyAPIService implements ForecastAPIService {

    /**
     * Used to retrieve the apikey
     */
    @Autowired
    Environment environment;


    /**
     * @see APIService
     */
    @Override
    public String get_api_key() {
        return environment.getProperty("DARK_SKY_APIKEY");
    }

    /**
     * @see ForecastAPIService
     */
    @Override
    public ForecastResult forecast(double latitude, double longitude, int days_count, int days_offset) {
        return null;
    }
}
