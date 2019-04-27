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
    private int uv;

    /**
     * internet location
     */
    private String icon;

    public DailyForecast(String summary, double humidity, double max_temperature, double min_temperature, int uv, String icon) {
        this.summary = summary;
        this.humidity = humidity;
        this.max_temperature = max_temperature;
        this.min_temperature = min_temperature;
        this.uv = uv;
        this.icon = icon;
    }

    public DailyForecast() {
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getMax_temperature() {
        return max_temperature;
    }

    public void setMax_temperature(double max_temperature) {
        this.max_temperature = max_temperature;
    }

    public double getMin_temperature() {
        return min_temperature;
    }

    public void setMin_temperature(double min_temperature) {
        this.min_temperature = min_temperature;
    }

    public int getUv() {
        return uv;
    }

    public void setUv(int uv) {
        this.uv = uv;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
