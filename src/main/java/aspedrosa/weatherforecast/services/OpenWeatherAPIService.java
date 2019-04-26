package aspedrosa.weatherforecast.services;

import aspedrosa.weatherforecast.domain.ForecastResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * Forecast API Service that uses the Open Weather API
 */
@Service
public class OpenWeatherAPIService implements ForecastAPIService {

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
        return environment.getProperty("OPEN_WEATHER_APIKEY");
    }

    /**
     * @see ForecastAPIService
     */
    @Override
    public ForecastResult forecast(double latitude, double longitude, int days_count, int days_offset) {
        return null;
    }
}
