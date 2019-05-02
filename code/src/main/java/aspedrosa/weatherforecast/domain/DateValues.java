package aspedrosa.weatherforecast.domain;

/**
 * Used to return both year and day for the method DailyForecastCache::get_date_values
 */
public class DateValues {

    /**
     * year of date
     */
    private int year;

    /**
     * day of the year of date (1-365)
     */
    private int day;

    /**
     * Main constructor
     *
     * @param year of date
     * @param day of date
     */
    public DateValues(int year, int day) {
        this.year = year;
        this.day = day;
    }

    public int get_year() {
        return year;
    }

    public int get_day() {
        return day;
    }

}
