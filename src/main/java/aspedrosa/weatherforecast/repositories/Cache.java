package aspedrosa.weatherforecast.repositories;

import java.util.HashMap;
import java.util.Map;

/**
 * Used to implement a cache mechanism for forecast calls.
 * Has implemented a time-to-live mechanism.
 * Keeps track of the total requests done to the cache and the
 *  number of hits and misses.
 * A key value was used to implement this caching mechanism
 *
 * @param <K> Type for key
 * @param <V> Value to cache
 */
public abstract class Cache<K,V> {

    /**
     * Total number of requests done to the cache
     */
    private int total_requests;

    /**
     * Number of retrieving data calls that used cache
     */
    private int hits;

    /**
     * Number of retrieving data calls that didn't used cache
     */
    private int misses;

    /**
     * Expiration time for the data held by this cache
     */
    private final int TIME_TO_LIVE;

    /**
     * Class to use as value for the internal map to store cache data.
     * Stores the expiration time for this value and the value itself.
     */
    protected class Value {
        /**
         * Unix timestamp when this value expires
         */
        private long expiration_time;
        /**
         * Cached data
         */
        private V data;

        /**
         * Main constructor
         * @param data data to cache
         */
        public Value(V data) {
            this.data = data;
            this.expiration_time = System.currentTimeMillis() + TIME_TO_LIVE * 1000;
        }

        /**
         * Verifies if the value expired accordingly to the internal expiration time
         * @return true if cached data has expired, false otherwise
         */
        public boolean has_expired() {
            return System.currentTimeMillis() > this.expiration_time;
        }

        /**
         * Used on
         */
        public void reset_expiration_time() {
            this.expiration_time = System.currentTimeMillis() + TIME_TO_LIVE * 1000;
        }

        /**
         * Gets the cached data
         * @return cached data
         */
        public V get_data() {
            return data;
        }

        /**
         * Stores new data and updates the expiration time
         * @param data new cached data
         */
        public void update_data(V data) {
            this.data = data;
            this.expiration_time = System.currentTimeMillis() + TIME_TO_LIVE * 1000;
        }

    }

    /**
     * Where the cache data will be stored
     */
    protected Map<K,Value> data;

    /**
     * Main constructor
     */
    public Cache() {
        hits = misses = total_requests = 0;
        data = new HashMap<>();

        TIME_TO_LIVE = Integer.parseInt(System.getenv("TIME_TO_LIVE"));
    }

    /**
     * Used to when a match for the request is not found on cache
     * Number of requests and misses are incremented
     */
    protected synchronized void miss() {
        total_requests++;
        misses++;
    }

    /**
     * Used to when a match for the request is found on cache
     * Number of requests and hits are incremented
     */
    protected synchronized void hit() {
        total_requests++;
        hits++;
    }

    /**
     * Used to send statistic data of this cache
     */
    public class Statistics {
        private int total_requests;
        private int hits;
        private int misses;

        public Statistics(int total_requests, int hits, int misses) {
            this.total_requests = total_requests;
            this.hits = hits;
            this.misses = misses;
        }

        public int get_hits() {
            return hits;
        }

        public int get_total_requests() {
            return total_requests;
        }

        public int get_misses() {
            return misses;
        }
    }

    /**
     * Obtains statistic values for this cache class
     * @return internal class with all the values
     */
    public Statistics get_stats() {
        return new Statistics(total_requests, hits, misses);
    }

    /**
     * Used to ask for cache data
     *
     * @param key key associated with data
     * @return cached data
     */
    public abstract V get_cached_data(K key);

    /**
     * Used to store results retrieved from an external api
     *
     * @param key key associated with data
     * @param value new cached data
     */
    public abstract void cache_data(K key, V value);
}
