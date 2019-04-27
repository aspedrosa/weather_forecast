package aspedrosa.weatherforecast.domain;

import java.util.Objects;

/**
 * Holds both latitude and longitude.
 * Used as key for cache on both current weather and daily forecast
 */
public class Coordinates {

    /**
     * Latitude of the location
     */
    private double latitude;

    /**
     * Longitude of the location
     */
    private double longitude;

    /**
     * Main constructor
     *
     * @param latitude of the location
     * @param longitude of the location
     */
    public Coordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return Double.compare(that.latitude, latitude) == 0 &&
            Double.compare(that.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }
}
