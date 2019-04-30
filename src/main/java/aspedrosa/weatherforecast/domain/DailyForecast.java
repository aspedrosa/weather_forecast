package aspedrosa.weatherforecast.domain;

/**
 * Structure sent on a forecast associated to a day
 * This is built by a ForecastAPIService
 */
public class DailyForecast {

    /**
     * Little summary of weather condition
     */
    private String summary;

    /**
     * Percentage
     */
    private double humidity;

    /**
     * Degrees Celsius
     */
    private double max_temperature;

    /**
     * Degrees Celsius
     */
    private double min_temperature;

    /**
     * index
     */
    private double uv;

    /**
     * internet location
     */
    private String icon;

    public DailyForecast(String summary, double humidity, double max_temperature, double min_temperature, double uv, String icon) {
        this.summary = summary;
        this.humidity = humidity;
        this.max_temperature = max_temperature;
        this.min_temperature = min_temperature;
        this.uv = uv;
        this.icon = icon;
    }

    public DailyForecast() {
    }

    public String get_summary() {
        return summary;
    }

    public void set_summary(String summary) {
        this.summary = summary;
    }

    public double get_humidity() {
        return humidity;
    }

    public void set_humidity(double humidity) {
        this.humidity = humidity;
    }

    public double get_max_temperature() {
        return max_temperature;
    }

    public void set_max_temperature(double max_temperature) {
        this.max_temperature = max_temperature;
    }

    public double get_min_temperature() {
        return min_temperature;
    }

    public void set_min_temperature(double min_temperature) {
        this.min_temperature = min_temperature;
    }

    public double get_uv() {
        return uv;
    }

    public void set_uv(double uv) {
        this.uv = uv;
    }

    public String get_icon() {
        return icon;
    }

    public void set_icon(String icon) {
        this.icon = icon;
    }
}
