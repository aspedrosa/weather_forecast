package aspedrosa.weatherforecast.repositories;

import aspedrosa.weatherforecast.domain.ForecastResult;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Used to implement a cache mechanism for forecast calls.
 * Has implemented a time-to-live mechanism.
 * Keeps track of the total requests done to the cache and the
 *  number of hits and misses.
 */
@Repository
public class Cache {

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
     * Used to ask for cache data
     *
     * @param latitude of the location
     * @param longitude of the location
     * @param time TODO
     * @return TODO
     */
    public Object get_cached_forecast(double latitude, double longitude) {
        /*
        if (found()) {
            hit();
            return new Object();
        }

        miss();
        return null;
         */
        return null;
    }

    /**
     * Used to store results retrieved from an external api
     *
     * @param forecast_result the data retrieved
     */
    public void cache_forecast_results(ForecastResult forecast_result) {

    }
}
