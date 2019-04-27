package aspedrosa.weatherforecast.services;

import aspedrosa.weatherforecast.domain.SearchResult;
import aspedrosa.weatherforecast.repositories.SearchCache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Handles the request of a location
 */
@Service
public class SearchService {

    /**
     * Search api service where the call to the external api is done
     */
    @Autowired
    public LocationIqAPIService search_api_service;

    @Autowired
    SearchCache search_cache;

    /**
     * Searches for locations on an external api that match the input received from the user
     *
     * @param user_input_location location received on the rest path of search
     * @return list of all search results that match the input of the user
     */
    public List<SearchResult> search(String user_input_location) {
        List<SearchResult> data = search_cache.get_cached_data(user_input_location);
        if (!data.isEmpty())
            return data;

        data = search_api_service.search(user_input_location);

        if (data.isEmpty())
            return Collections.emptyList();

        search_cache.cache_data(user_input_location, data);

        return data;
    }
}
