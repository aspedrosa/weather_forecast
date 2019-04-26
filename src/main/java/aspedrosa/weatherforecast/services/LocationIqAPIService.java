package aspedrosa.weatherforecast.services;

import aspedrosa.weatherforecast.domain.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Search api service that uses the LocationIQ API
 */
@Service
public class LocationIqAPIService implements SearchAPIService {

    /**
     * Used to retrieve the apikey
     */
    @Autowired
    Environment environment;

    /**
     * @see APIService
     */
    @Override
    public String get_api_key() {
        return environment.getProperty("LOCATION_IQ_APIKEY");
    }

    /**
     * @see SearchAPIService
     */
    @Override
    public List<SearchResult> search(String user_location_input) {
        return null;
    }
}
