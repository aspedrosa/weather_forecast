package aspedrosa.weatherforecast.repositories;

import org.awaitility.Duration;
import org.junit.Before;
import org.junit.Test;

import org.awaitility.Awaitility;

import static org.junit.Assert.*;

public class CacheTest {

    class CacheImpl extends Cache<String, Object>{

        /**
         * values expires in three second
         */
        @Override
        public boolean has_value_expired(long write_date) {
            return System.currentTimeMillis() > write_date + 3000L;
        }
    }

    CacheImpl cache;

    @Before
    public void setUp() {
        cache = new CacheImpl();
    }


    /**
     * Test that data is expired after TIME_TO_LIVE seconds
     * Also test statistics for cache (total requests, hits, misses)
     */
    @Test
    public void no_data_after_expired() {
        cache.cache_data("key", "value");

        long then = System.currentTimeMillis();

        Awaitility.await().atMost(Duration.FIVE_SECONDS).until(
            () -> System.currentTimeMillis() > then + 3000
        );

        assertNull(cache.get_cached_data("key"));

        Cache.Statistics stats = cache.get_stats();
        assertEquals(1, stats.get_total_requests());
        assertEquals(0, stats.get_hits());
        assertEquals(1, stats.get_misses());
    }

    /**
     * Test that after caching data for a specific key
     *  that data is return on the next get_cached_data
     */
    @Test
    public void data_after_insert() {
        cache.cache_data("key", "value");

        assertNotNull(cache.get_cached_data("key"));

        Cache.Statistics stats = cache.get_stats();
        assertEquals(1, stats.get_total_requests());
        assertEquals(1, stats.get_hits());
        assertEquals(0, stats.get_misses());
    }

    /**
     * Test null with no data
     */
    @Test
    public void null_for_no_data() {
        assertNull(cache.get_cached_data("key"));
    }
}