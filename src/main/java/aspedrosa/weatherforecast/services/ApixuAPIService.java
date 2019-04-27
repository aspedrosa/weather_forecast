package aspedrosa.weatherforecast.services;

import aspedrosa.weatherforecast.domain.DailyForecast;
import org.springframework.stereotype.Service;

/**
 * Forecast API Service that uses the Open Weather API
 */
@Service
public class ApixuAPIService extends ForecastAPIService {

    /**
     * @see APIService
     *
     * @param args (double latitude, double longitude) both from the location
     */
    @Override
    public String build_api_url(Object[] args) {
        return "http://api.apixu.com/v1/forecast.json?key=" + System.getenv("APIXU_APIKEY") +
            "q=" + args[0] + "," + args[1] +
            "&days=" + args[2];
    }

    /**
     * @see ForecastAPIService
     */
    @Override
    public DailyForecast[] forecast(double latitude, double longitude, int days_count) {
        return null; // TODO
    }
}
