package aspedrosa.weatherforecast.repositories;

import aspedrosa.weatherforecast.domain.Coordinates;
import aspedrosa.weatherforecast.domain.DailyForecast;
import aspedrosa.weatherforecast.domain.DateValues;
import org.joda.time.DateTimeUtils;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.List;

/**
 * Cache to store daily forecast results
 * The forecasts for each day are stored in a single position
 *  on a array. Whenever a day passes the data needs to be shifted
 */
@Repository
public class DailyForecastCache extends Cache<Coordinates, List<DailyForecast>> {

    /**
     * Retrieves from a timestamp the year and day of the year associated
     *
     * @param timestamp unix timestamp in milliseconds
     * @return class with both year and day of year
     */
    private DateValues get_date_values(long timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);

        return new DateValues(cal.get(Calendar.YEAR), cal.get(Calendar.DAY_OF_YEAR));
    }

    /**
     * Expires when the DAY of write is different
     *
     * @see Cache
     */
    @Override
    protected boolean has_value_expired(long write_date) {
        DateValues then = get_date_values(write_date);
        DateValues now = get_date_values(DateTimeUtils.currentTimeMillis());

        return then.get_year() != now.get_year() ||
               then.get_day() != now.get_day();
    }


    /**
     * If more than MAX_DAY_COUNT had pass then all data is expired
     * If not, shift data on the array so that data for days that
     *  are on the past is deleted
     *
     * @see Cache
     */
    @Override
    protected boolean handle_expired_value(Coordinates key) {
        long write_date = data.get(key).get_write_date();
        List<DailyForecast> daily_forecasts = data.get(key).get_data();

        DateValues then = get_date_values(write_date);
        DateValues now = get_date_values(DateTimeUtils.currentTimeMillis());

        // if the last time it was retrieved was ten days ago
        //  all data is expired.
        // then just remove all data
        if (then.get_year() != now.get_year() ||
            then.get_day() + daily_forecasts.size() < now.get_day()) {
            data.remove(key);
            return false;
        }

        // else shift data on the array accordingly to how much days
        //  passed since last write
        int diff_days = now.get_day() - then.get_day();
        List<DailyForecast> forecast = data.get(key).get_data();

        for (int i = 0; i < forecast.size() - diff_days; i++)
            forecast.set(i, forecast.get(i + diff_days));

        data.get(key).set_data(forecast.subList(0, forecast.size() - diff_days));
        data.get(key).set_write_date(DateTimeUtils.currentTimeMillis());

        return true;
    }

    /**
     * Because the new data is more recent and can be more accurate
     *  override it for data that was cached
     *
     * @see Cache
     */
    @Override
    protected void update_value(Value val, List<DailyForecast> new_data) {
        if (val.data.size() > new_data.size())
            for (int i = 0; i < new_data.size(); i++)
                val.data.set(i, new_data.get(i));
        else
            val.data = new_data;
    }

    /**
     * Used by the forecast service.
     * When he retrieves data associated with a set of
     *  coordinates is considered as a hit, whoever the request
     *  may be requesting five day but only two are cached, leading
     *  to a call to the external api. In other words should
     *  not be considered as a hit
     */
    public void correct_stats() {
        hits--;
        misses++;
    }
}
