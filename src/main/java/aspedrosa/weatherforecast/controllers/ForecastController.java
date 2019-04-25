package aspedrosa.weatherforecast.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author aspedrosa
 */
@RestController
@RequestMapping("/api")
public class ForecastController {

    /**
     * @param location_id
     * @param days_number
     * @param days_offset
     * @return
     */
    @GetMapping
    public ResponseEntity forecast(@RequestParam(required = true) long location_id,
                                   @RequestParam(required = false) Integer days_number,
                                   @RequestParam(required = false) Integer days_offset) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}
