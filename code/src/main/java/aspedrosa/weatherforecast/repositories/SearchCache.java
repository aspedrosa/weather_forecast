package aspedrosa.weatherforecast.repositories;

import aspedrosa.weatherforecast.domain.SearchResult;
import org.joda.time.DateTimeUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Cache to store location search results
 */
@Repository
public class SearchCache extends Cache<String, List<SearchResult>> {

    /**
     * Expires after one month
     *
     * @see Cache
     */
    @Override
    public boolean has_value_expired(long write_date) {
        return DateTimeUtils.currentTimeMillis() > write_date + 2678400000L;
    }

}
