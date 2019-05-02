package aspedrosa.weatherforecast.controllers;

import aspedrosa.weatherforecast.domain.Forecast;
import aspedrosa.weatherforecast.domain.SearchResult;
import aspedrosa.weatherforecast.services.ForecastService;
import aspedrosa.weatherforecast.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * Where the endpoints of this application are located
 */
@RestController
@RequestMapping ("/api")
public class WeatherController {

    /**
     * Service to request forecast results
     */
    @Autowired
    ForecastService forecast_service;

    /**
     * Service to request search results related to locations
     */
    @Autowired
    SearchService search_service;

    /**
     * Gives the weather results for certain coordinates within a time (daily) interval
     *
     * @param longitude of the location
     * @param latitude of the location
     * @return HTTP response with forecast results on the data field
     */
    @GetMapping("/forecast")
    public ResponseEntity<Forecast> forecast(@RequestParam double latitude,
                                             @RequestParam double longitude,
                                             @RequestParam (required = false, defaultValue="1") Integer days_count) {
        Forecast forecast = forecast_service.forecast(latitude, longitude, days_count);

        return new ResponseEntity<>(
            forecast,
            HttpStatus.ACCEPTED
        );
    }

    /**
     * Used to has a search and conversion mechanism from a location to global coordinates
     *
     * @param location input from the user of a location to search for
     * @return HTTP response with search results on the data field
     */
    @GetMapping("/search")
    public ResponseEntity<List<SearchResult>> search(@RequestParam String location) {
        location = location.trim().toLowerCase();

        if (location.equals("")) {
            return new ResponseEntity<>(
                Collections.emptyList(),
                HttpStatus.BAD_REQUEST
            );
        }

        List<SearchResult> data = search_service.search(location);

        HttpStatus status;

        if (data.isEmpty())
            status = HttpStatus.NOT_FOUND;
        else
            status = HttpStatus.ACCEPTED;

        return new ResponseEntity<>(
            data,
            status
        );
    }
}
