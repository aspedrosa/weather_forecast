package aspedrosa.weatherforecast.repositories;

import aspedrosa.weatherforecast.domain.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class SearchCache extends Cache<String, List<SearchResult>> {

    private static Logger logger = LoggerFactory.getLogger(SearchCache.class);

    @Override
    public synchronized List<SearchResult> get_cached_data(String key) {
        if (!data.containsKey(key)) {
            miss();
            return Collections.emptyList();
        }

        Value val = data.get(key);

        if (val.has_expired()) {
            data.remove(key);
            miss();
            return Collections.emptyList();
        }

        val.reset_expiration_time();
        hit();

        return val.get_data();
    }

    @Override
    public synchronized void cache_data(String key, List<SearchResult> value) {
        if (!data.containsKey(key)) {
            Value val = new Value(value);

            data.put(key, val);
        }
        else {
            logger.warn("Caching already cached value");
            Value val = data.get(key);
            val.update_data(value);
        }
    }
}
