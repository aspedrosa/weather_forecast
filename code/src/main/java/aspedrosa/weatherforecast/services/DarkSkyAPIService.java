package aspedrosa.weatherforecast.services;

import aspedrosa.weatherforecast.domain.DailyForecast;
import aspedrosa.weatherforecast.domain.CurrentWeather;
import aspedrosa.weatherforecast.domain.Forecast;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Forecast api Service that uses Dark Sky API
 */
@Service
public class DarkSkyAPIService extends ForecastAPIService {

    private static Map<String, String> icons;

    static {
        icons = new HashMap<>();
        icons.put("clear-day", "images/Sun.svg");
        icons.put("clear-night", "images/Moon.svg");
        icons.put("rain", "images/Cloud-Rain.svg");
        icons.put("snow", "images/Cloud-Snow.svg");
        icons.put("sleet", "images/Cloud-Hail.svg");
        icons.put("wind", "images/Cloud-Wind.svg");
        icons.put("fog", "images/Cloud-Fog.svg");
        icons.put("cloudy", "images/Cloud.svg");
        icons.put("partly-cloudy-day", "images/Cloud-Sun.svg");
        icons.put("partly-cloudy-night", "images/Cloud-Moon.svg");

    }

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
    public int MAX_DAYS_COUNT() {
        return 8;
    }

    /**
     * @see ForecastAPIService
     */
    @Override
    public Forecast forecast(double latitude, double longitude) {
        ResponseEntity<String> response_entity = rest_template.getForEntity(
            build_api_url(new Object[] {latitude, longitude}),
            String.class);

        JSONObject json_response = new JSONObject(response_entity.getBody());

        JSONObject json_current = json_response.getJSONObject("currently");
        CurrentWeather current_weather = new CurrentWeather();
        current_weather.set_icon(icons.get(json_current.getString("icon")));
        current_weather.set_humidity(100 * json_current.getDouble("humidity"));
        current_weather.set_pressure(100 * json_current.getDouble("pressure"));
        current_weather.set_summary(json_current.getString("summary"));
        current_weather.set_temperature(json_current.getDouble("temperature"));
        current_weather.set_uv(json_current.getDouble("uvIndex"));
        current_weather.set_wind_speed(json_current.getDouble("windSpeed"));

        JSONArray json_forecast = json_response.getJSONObject("daily").getJSONArray("data");
        List<DailyForecast> daily_forecast = new ArrayList<>();
        for (int i = 0; i < MAX_DAYS_COUNT(); i++) {
            JSONObject json_day_forecast = json_forecast.getJSONObject(i);
            DailyForecast day_forecast = new DailyForecast();
            day_forecast.set_icon(icons.get(json_day_forecast.getString("icon")));
            day_forecast.set_humidity(100 * json_day_forecast.getDouble("humidity"));
            day_forecast.set_max_temperature(json_day_forecast.getDouble("temperatureMax"));
            day_forecast.set_min_temperature(json_day_forecast.getDouble("temperatureMin"));
            day_forecast.set_summary(json_day_forecast.getString("summary"));
            day_forecast.set_uv(json_day_forecast.getDouble("uvIndex"));

            daily_forecast.add(day_forecast);
        }

        return new Forecast(daily_forecast, current_weather);
    }
}
