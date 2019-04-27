package aspedrosa.weatherforecast.repositories;

import aspedrosa.weatherforecast.domain.Coordinates;
import aspedrosa.weatherforecast.domain.DailyForecast;
import org.springframework.stereotype.Repository;

/**
 * Cache to store daily forecast results
 */
@Repository
public class DailyForecastCache extends Cache<Coordinates, DailyForecast[]> {

    /**
     * Expires when the DAY of write is different
     *
     * @see Cache
     */
    @Override
    public boolean has_value_expired(long write_date) {
        return false; // TODO
    }
}
