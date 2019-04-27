package aspedrosa.weatherforecast.services;

import aspedrosa.weatherforecast.domain.DailyForecast;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


/**
 * Forecast api Service that uses Dark Sky API
 */
@Service
public class DarkSkyAPIService extends ForecastAPIService {

    @Autowired
    RestTemplate rest_template;

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
            "?units=si&exclude=minutely,hourly,alerts,flags";
    }

    /**
     * @see ForecastAPIService
     */
    @Override
    public DailyForecast[] forecast(double latitude, double longitude, int days_count) {
        return null; // TODO
    }
}
