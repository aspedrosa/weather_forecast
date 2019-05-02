package aspedrosa.weatherforecast.services;

/**
 * Base structure for a service that communicates to an api.
 * With this if another api only new class, that follows the same structure
 *  needs to be implements, leaving the higher level services untouched.
 */
public abstract class APIService {

    /**
     * Gets the urls to query the external api
     *
     * @param args to build the url
     * @return the formatted url
     */
    abstract String build_api_url(Object[] args);
}
