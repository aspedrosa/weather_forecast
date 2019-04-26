package aspedrosa.weatherforecast.services;

/**
 * Base structure for a service that communicates to an api.
 * With this if another api only new class, that follows the same structure
 *  needs to be implements, leaving the higher level services untouched.
 */
public interface APIService {
    /**
     * Gets the api key/token for this api
     * All apis use some kind of key or token
     *
     * @return the api key/token
     */
    String get_api_key();
}
