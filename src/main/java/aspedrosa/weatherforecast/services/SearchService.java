package aspedrosa.weatherforecast.services;

import aspedrosa.weatherforecast.domain.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    LocationIqAPIService search_api_service;

    /**
     * Searches for locations on an external api that match the input received from the user
     *
     * @param user_input_location location received on the rest path of search
     * @return list of all search results that match the input of the user
     */
    public List<SearchResult> search(String user_input_location) {
        return null;
    }
}
