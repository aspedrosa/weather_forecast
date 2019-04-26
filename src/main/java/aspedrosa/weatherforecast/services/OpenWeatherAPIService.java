package aspedrosa.weatherforecast.services;

import aspedrosa.weatherforecast.domain.ForecastResult;
import org.springframework.stereotype.Service;

/**
 * Forecast API Service that uses the Open Weather API
 */
@Service
public class OpenWeatherAPIService extends ForecastAPIService {

    /**
     * @see APIService
     *
     * @param args (double latitude, double longitude) both from the location
     */
    @Override
    public String build_api_url(Object[] args) {
        return "http://api.openweathermap.org/data/2.5/forecast?" +
            "lat=" + args[0] +
            "&lon=" + args[1] +
            "&APPID=" + System.getenv("OPEN_WEATHER_APIKEY") +
            "&units=metric";
    }

    /**
     * @see ForecastAPIService
     */
    @Override
    public ForecastResult forecast(double latitude, double longitude, int days_count, int days_offset) {
        return null;
    }
}
