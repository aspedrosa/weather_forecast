package aspedrosa.weatherforecast.repositories;

import aspedrosa.weatherforecast.domain.Coordinates;
import aspedrosa.weatherforecast.domain.CurrentWeather;
import org.springframework.stereotype.Repository;

/**
 * Cache to store current weather results
 */
@Repository
public class CurrentWeatherCache extends Cache<Coordinates, CurrentWeather> {

    /**
     * Expire after 15 min
     *
     * @see Cache
     */
    @Override
    public boolean has_value_expired(long write_date) {
        return System.currentTimeMillis() > write_date + 900000L;
    }

}
