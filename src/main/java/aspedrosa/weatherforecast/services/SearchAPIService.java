package aspedrosa.weatherforecast.services;

import aspedrosa.weatherforecast.domain.SearchResult;

import java.util.List;

/**
 * Base structure for a api service used to get forecast data
 */
public abstract class SearchAPIService extends APIService {
    /**
     * Make a call to an external api to get search results
     *
     * @param user_location_input location to search for
     *
     * @return search results
     */
    abstract List<SearchResult> search(String user_location_input);
}
