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
import java.util.List;

/**
 * Forecast API Service that uses the Open Weather API
 */
@Service
public class ApixuAPIService implements ForecastAPIService {

    @Autowired
    RestTemplate rest_template;

    /**
     * @see APIService
     *
     * @param args (double latitude, double longitude) both from the location
     */
    @Override
    public String build_api_url(Object[] args) {
        return "http://api.apixu.com/v1/forecast.json?key=" + System.getenv("APIXU_APIKEY") +
            "&q=" + args[0] + "," + args[1] +
            "&days=" + MAX_DAYS_COUNT();
    }

    /**
     * @see ForecastAPIService
     */
    @Override
    public int MAX_DAYS_COUNT() {
        return 7;
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

        JSONObject json_current = json_response.getJSONObject("current");
        CurrentWeather current_weather = new CurrentWeather();
        current_weather.set_humidity(json_current.getInt("humidity"));
        current_weather.set_icon("http:" + json_current.getJSONObject("condition").getString("icon"));
        current_weather.set_pressure(100 * json_current.getDouble("pressure_mb"));
        current_weather.set_summary(json_current.getJSONObject("condition").getString("text"));
        current_weather.set_temperature(json_current.getDouble("temp_c"));
        current_weather.set_uv(json_current.getDouble("uv"));
        current_weather.set_wind_speed(((double) 5 / (double) 18) * json_current.getDouble("wind_kph"));

        JSONArray json_forecast = json_response.getJSONObject("forecast").getJSONArray("forecastday");
        List<DailyForecast> daily_forecast = new ArrayList<>();
        for (int i = 0; i < json_forecast.length(); i++) {
            JSONObject json_day_forecast = json_forecast.getJSONObject(i).getJSONObject("day");
            DailyForecast day_forecast = new DailyForecast();
            day_forecast.set_humidity(json_day_forecast.getInt("avghumidity"));
            day_forecast.set_icon("http:" + json_day_forecast.getJSONObject("condition").getString("icon"));
            day_forecast.set_max_temperature(json_day_forecast.getDouble("maxtemp_c"));
            day_forecast.set_min_temperature(json_day_forecast.getDouble("mintemp_c"));
            day_forecast.set_summary(json_day_forecast.getJSONObject("condition").getString("text"));
            day_forecast.set_uv(json_day_forecast.getDouble("uv"));

            daily_forecast.add(day_forecast);
        }

        return new Forecast(daily_forecast, current_weather);
    }
}
