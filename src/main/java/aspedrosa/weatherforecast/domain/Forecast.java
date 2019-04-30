package aspedrosa.weatherforecast.domain;

import java.util.List;

/**
 * Used to send from a ForecastAPIService both the
 *  current and daily forecast
 */
public class Forecast {

    /**
     * All daily forecasts for the several future days
     */
    private List<DailyForecast> daily_forecast;

    /**
     * Current weather forecast
     */
    private CurrentWeather current_weather;

    /**
     * Empty constructor. Supposed to use setters
     */
    public Forecast() {}

    /**
     * Main constructor
     *
     * @param daily_forecast
     * @param current_weather
     */
    public Forecast(List<DailyForecast> daily_forecast, CurrentWeather current_weather) {
        this.daily_forecast = daily_forecast;
        this.current_weather = current_weather;
    }

    public CurrentWeather getCurrent_weather() {
        return current_weather;
    }

    public void setCurrent_weather(CurrentWeather current_weather) {
        this.current_weather = current_weather;
    }

    public List<DailyForecast> getDaily_forecast() {
        return daily_forecast;
    }

    public void setDaily_forecast(List<DailyForecast> daily_forecast) {
        this.daily_forecast = daily_forecast;
    }
}
