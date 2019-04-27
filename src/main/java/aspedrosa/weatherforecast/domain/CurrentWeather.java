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
    private int uv;

    /**
     * meters per second
     */
    private int wind_speed;

    /**
     * internet location
     */
    private String icon = "";

    /**
     * Main constructor
     */
    public CurrentWeather(String summary, double temperature, double humidity, double pressure, int uv, int wind_speed, String icon) {
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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public int getUv() {
        return uv;
    }

    public void setUv(int uv) {
        this.uv = uv;
    }

    public int getWind_speed() {
        return wind_speed;
    }

    public void setWind_speed(int wind_speed) {
        this.wind_speed = wind_speed;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
