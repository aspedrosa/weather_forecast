package aspedrosa.weatherforecast.repositories;

import org.joda.time.DateTimeUtils;

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
    protected int hits;

    /**
     * Number of retrieving data calls that didn't used cache
     */
    protected int misses;

    /**
     * Class to use as value for the internal map to store cache data.
     * Stores the expiration time for this value and the value itself.
     */
    protected final class Value {
        /**
         * Unix timestamp when this value was written
         */
        public long write_date;
        /**
         * Cached data
         */
        public V data;

        /**
         * Main constructor
         * @param data data to cache
         */
        public Value(V data) {
            this.data = data;
            this.write_date = DateTimeUtils.currentTimeMillis();
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
    }

    /**
     * Verifies if the value expired accordingly to the internal expiration time
     *
     * @param write_date when a specific value was written
     *
     * @return true if cached data has expired, false otherwise
     */
    protected abstract boolean has_value_expired(long write_date);

    /**
     * Function called whenever a certain value expired
     * By default just removes the value, but the method can be overwritten
     *  on subclasses
     *
     * @param key key of the value that has expired
     *
     * @return false if the value was just removed, true if it was updated
     */
    protected boolean handle_expired_value(K key) {
        data.remove(key);
        return false;
    }

    /**
     * Function called whenever a value is going to be written to the cache
     *  for a given key, but some other value is already stored.
     * By default overwrites the old data, but the method can be overwritten
     *  on subclasses
     *
     * @param val value to update
     * @param new_data new data received to cache
     */
    protected void update_value(Value val, V new_data) {
        val.data = new_data;
        val.write_date = DateTimeUtils.currentTimeMillis();
    }

    /**
     * Used to when a match for the request is not found on cache
     * Number of requests and misses are incremented
     */
    private void miss() {
        total_requests++;
        misses++;
    }

    /**
     * Used to when a match for the request is found on cache
     * Number of requests and hits are incremented
     */
    private void hit() {
        total_requests++;
        hits++;
    }

    /**
     * Used to send statistic data of this cache
     */
    public final class Statistics {
        private int total_requests;
        private int hits;
        private int misses;

        private Statistics(int total_requests, int hits, int misses) {
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
    public synchronized V get_cached_data(K key) {
        if (!data.containsKey(key)) {
            miss();
            return null;
        }

        Value val = data.get(key);

        //if it did not handle (just removed it) then consider a miss
        if (has_value_expired(val.write_date) && !handle_expired_value(key)) {
            miss();
            return null;
        }

        hit();
        return val.data;
    }

    /**
     * Used to store results retrieved from an external api
     * If data was already associated with the key
     *  received, call the update_value method and update
     *  the write_time for the stored value
     *
     * @param key key associated with data
     * @param value new cached data
     */
    public synchronized void cache_data(K key, V value) {
        if (!data.containsKey(key)) {
            Value val = new Value(value);

            data.put(key, val);
        }
        else {
            update_value(data.get(key), value);
            data.get(key).write_date = DateTimeUtils.currentTimeMillis();
        }
    }
}
