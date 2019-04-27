package aspedrosa.weatherforecast.repositories;

import aspedrosa.weatherforecast.domain.SearchResult;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Cache to store location search results
 */
@Repository
public class SearchCache extends Cache<String, List<SearchResult>> {

    public SearchCache() {
        super();
    }

    /**
     * Expires after one month
     *
     * @see Cache
     */
    @Override
    public boolean has_value_expired(long write_date) {
        return System.currentTimeMillis() > write_date + 2678400000L;
    }

}
