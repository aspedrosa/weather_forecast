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
}
