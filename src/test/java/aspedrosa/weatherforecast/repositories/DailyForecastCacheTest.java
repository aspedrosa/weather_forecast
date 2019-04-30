package aspedrosa.weatherforecast.repositories;

import aspedrosa.weatherforecast.domain.Coordinates;
import aspedrosa.weatherforecast.domain.DailyForecast;
import org.joda.time.DateTimeUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import static org.junit.Assert.*;

/**
 * The cache for daily forecasts overwrites some default methods of the
 *  class cache
 * For that some tests to those methods are done
 */
public class DailyForecastCacheTest {

    private DailyForecastCache daily_cache;

    private Coordinates coords;

    private List<DailyForecast> values;

    /**
     * Because these tests are time dependent,
     *  here the time returned for System.currentMilliseconds is manipulated
     *  making it return always the same time and time zone
     */
    @BeforeClass
    public static void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC")); //all timestamps on this test use utc timezone
        DateTimeUtils.setCurrentMillisFixed(1556548741000L); // 29/04/2019 @ 2:39pm (UTC)
    }

    @Before
    public void setUp() {
        coords = new Coordinates(40, -8);

        daily_cache = new DailyForecastCache();

        values = new ArrayList<>();
        values.add(new DailyForecast("a", 0, 0, 0, 0, null));
        values.add(new DailyForecast("b", 0, 0, 0, 0, null));
        values.add(new DailyForecast("c", 0, 0, 0, 0, null));
        values.add(new DailyForecast("d", 0, 0, 0, 0, null));
    }

    /**
     * Verifies if tells correctly if a value has expired
     */
    @Test
    public void has_value_expired() {
        assertTrue(daily_cache.has_value_expired(1556495400000L)); // 28/04/2019 @ 11:50pm (UTC)
        assertFalse(daily_cache.has_value_expired(1556499600000L)); // 29/04/2019 @ 1:00am (UTC)
    }

    /**
     * Test if whenever x values are stored and y days had pass, x > y,
     *  that x - y values are removed
     */
    @Test
    public void handle_expired_value_true() {
        daily_cache.cache_data(coords, values);
        daily_cache.data.get(coords).write_date = 1556495400000L; // 28/04/2019 @ 11:50pm (UTC)

        assertTrue(daily_cache.handle_expired_value(coords));

        List<DailyForecast> after_handle = daily_cache.data.get(coords).data;
        assertEquals(3, after_handle.size());
        String[] summaries = {"b", "c", "d"};
        for (int i = 0; i < 3; i++)
            assertEquals(summaries[i], after_handle.get(i).get_summary());
    }

    /**
     * Test if whenever x values are stored and y days had pass, y > x,
     *  that removes all data associated with those coordinates
     */
    @Test
    public void handle_expired_value_false() {
        daily_cache.cache_data(coords, values);
        daily_cache.data.get(coords).write_date = 1555588800000L; // 18/04/2019 @ 12:00pm (UTC)

        assertFalse(daily_cache.handle_expired_value(coords));

        assertFalse(daily_cache.data.containsKey(coords));
    }

    /**
     * Test if whenever the update_value method is called and the
     *  new data has less values than the ones cached overwrites
     *  the oldest (the ones with lower index)
     */
    @Test
    public void update_value() {
        daily_cache.cache_data(coords, values);

        List<DailyForecast> new_data = new ArrayList<>();
        new_data.add(new DailyForecast("e", 0, 0, 0, 0, null));
        new_data.add(new DailyForecast("f", 0, 0, 0, 0, null));

        daily_cache.update_value(daily_cache.data.get(coords), new_data);

        List<DailyForecast> after_update = daily_cache.data.get(coords).data;
        assertEquals(4, after_update.size());
        String[] summaries = {"e", "f", "c", "d"};
        for (int i = 0; i < 4; i++)
            assertEquals(summaries[i], after_update.get(i).get_summary());
    }

    /**
     * Test if whenever the update_value method is called and the
     *  new data has more values than the ones cached replaces the
     *  entire cached data
     */
    @Test
    public void update_value_over() {
        daily_cache.cache_data(coords, values);

        List<DailyForecast> new_data = new ArrayList<>();
        new_data.add(new DailyForecast("e", 0, 0, 0, 0, null));
        new_data.add(new DailyForecast("f", 0, 0, 0, 0, null));
        new_data.add(new DailyForecast("g", 0, 0, 0, 0, null));
        new_data.add(new DailyForecast("h", 0, 0, 0, 0, null));
        new_data.add(new DailyForecast("i", 0, 0, 0, 0, null));

        daily_cache.update_value(daily_cache.data.get(coords), new_data);

        List<DailyForecast> after_update = daily_cache.data.get(coords).data;
        assertEquals(5, after_update.size());
        String[] summaries = {"e", "f", "g", "h", "i"};
        for (int i = 0; i < 5; i++)
            assertEquals(summaries[i], after_update.get(i).get_summary());
    }

    /**
     * Make the query of time not fixed
     */
    @AfterClass
    public static void destroy() {
        DateTimeUtils.setCurrentMillisSystem();
    }
}