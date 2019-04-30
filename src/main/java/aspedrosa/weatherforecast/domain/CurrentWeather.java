package aspedrosa.weatherforecast.domain;

/**
 * Data sent in response from a current weather call
 * This data is build on a forecastAPIService
 */
public class CurrentWeather {

    /**
     * Little summary of weather condition
     */
    private String summary;

    /**
     * Degrees Celsius
     */
    private double temperature;

    /**
     * Percentage
     */
    private double humidity;

    /**
     * pascal
     */
    private double pressure;

    /**
     * index
     */
    private double uv;

    /**
     * meters per second
     */
    private double wind_speed;

    /**
     * internet location
     */
    private String icon;

    /**
     * Main constructor
     */
    public CurrentWeather(String summary, double temperature, double humidity, double pressure, double uv, double wind_speed, String icon) {
        this.summary = summary;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.uv = uv;
        this.wind_speed = wind_speed;
        this.icon = icon;
    }

    /**
     * Empty constructor. Supposed to use setters
     */
    public CurrentWeather() {}

    public String get_summary() {
        return summary;
    }

    public void set_summary(String summary) {
        this.summary = summary;
    }

    public double get_temperature() {
        return temperature;
    }

    public void set_temperature(double temperature) {
        this.temperature = temperature;
    }

    public double get_humidity() {
        return humidity;
    }

    public void set_humidity(double humidity) {
        this.humidity = humidity;
    }

    public double get_pressure() {
        return pressure;
    }

    public void set_pressure(double pressure) {
        this.pressure = pressure;
    }

    public double get_uv() {
        return uv;
    }

    public void set_uv(double uv) {
        this.uv = uv;
    }

    public double get_wind_speed() {
        return wind_speed;
    }

    public void set_wind_speed(double wind_speed) {
        this.wind_speed = wind_speed;
    }

    public String get_icon() {
        return icon;
    }

    public void set_icon(String icon) {
        this.icon = icon;
    }
}
