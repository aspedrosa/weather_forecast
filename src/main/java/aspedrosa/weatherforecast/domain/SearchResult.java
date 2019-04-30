package aspedrosa.weatherforecast.domain;

/**
 * Data sent in response from a search result
 * This data is build on a SearchAPIService
 */
public class SearchResult {

    /**
     * Name to display for this search results
     */
    private String display_name;

    /**
     * Latitude of this search result
     */
    private double latitude;

    /**
     * Latitude of this search result
     */
    private  double longitude;

    /**
     * Empty constructor. Supposed to use setters
     */
    public SearchResult() {}

    /**
     * Main contructor
     *
     * @param display_name name of the location displayed
     * @param latitude of the location
     * @param longitude of the location
     */
    public SearchResult(String display_name, double latitude, double longitude) {
        this.display_name = display_name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String get_display_name() {
        return display_name;
    }

    public double get_latitude() {
        return latitude;
    }

    public double get_longitude() {
        return longitude;
    }

    public void set_display_name(String display_name) {
        this.display_name = display_name;
    }

    public void set_latitude(double latitude) {
        this.latitude = latitude;
    }

    public void set_longitude(double longitude) {
        this.longitude = longitude;
    }
}
