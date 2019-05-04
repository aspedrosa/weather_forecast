package aspedrosa.weatherforecast.controllers;

import aspedrosa.weatherforecast.domain.CurrentWeather;
import aspedrosa.weatherforecast.domain.DailyForecast;
import aspedrosa.weatherforecast.domain.Forecast;
import aspedrosa.weatherforecast.domain.SearchResult;
import aspedrosa.weatherforecast.services.ForecastService;
import aspedrosa.weatherforecast.services.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Unit tests for the web layer
 *  mocking services dependencies
 *
 * Because all the queries to forecast
 *  just simply call a forecast, both the integration
 *  and unit test will be the same, for that only integration
 *  tests were done
 */
@RunWith(SpringRunner.class)
@WebMvcTest(WeatherController.class)
public class WeatherControllerTest {

    @Autowired
    MockMvc mock_mvc;

    @MockBean
    SearchService search_service;

    @MockBean
    ForecastService forecast_service;

    private final double LATITUDE = 40.641203;
    private final double LONGITUDE = -8.655614;
    CurrentWeather current_weather;
    List<DailyForecast> daily_forecasts;

    public WeatherControllerTest() {
        current_weather = new CurrentWeather("Partly cloudy", 16,77,101700,5, 7.194444444444445,"http://cdn.apixu.com/weather/64x64/day/116.png");

        daily_forecasts = new ArrayList<>();
        daily_forecasts.add(new DailyForecast("Partly cloudy", 77, 19.5, 11.3, 8, "http://cdn.apixu.com/weather/64x64/day/116.png"));
        daily_forecasts.add(new DailyForecast("Partly cloudy", 73, 20.4, 12.6, 7.8, "http://cdn.apixu.com/weather/64x64/day/116.png"));
        daily_forecasts.add(new DailyForecast("Partly cloudy", 62, 23.9, 13.7, 8.1, "http://cdn.apixu.com/weather/64x64/day/116.png"));
    }

    @Test
    public void invalid_search_field() throws Exception {
        mock_mvc.perform(
            MockMvcRequestBuilders
                .get("/api/search")
                .param("location", "  "))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().json("[]"));
    }

    /**
     * Test a regular search result
     *
     * @throws Exception by MockMvc
     */
    @Test
    public void search_accepted() throws Exception {
        List<SearchResult> ilhavo = new ArrayList<>();
        ilhavo.add(new SearchResult("Ílhavo, Aveiro, Baixo Vouga, Centro, Portugal",
            40.60552025,
            -8.68594848811954));
        Mockito.when(search_service.search("ilhavo")).thenReturn(ilhavo);

        mock_mvc.perform(
            MockMvcRequestBuilders
                .get("/api/search")
                .param("location", "ilhavo"))
        .andExpect(MockMvcResultMatchers.status().isAccepted())
        .andExpect(MockMvcResultMatchers.content().json("[{\"_display_name\":\"Ílhavo, Aveiro, Baixo Vouga, Centro, Portugal\",\"_latitude\":40.60552025,\"_longitude\":-8.68594848811954}]"));
    }

    /**
     * Test a not found search result
     *
     * @throws Exception by MockMvc
     */
    @Test
    public void search_not_found() throws Exception {
        Mockito.when(search_service.search("asdfsdfh")).thenReturn(Collections.emptyList());

        mock_mvc.perform(
            MockMvcRequestBuilders
                .get("/api/search")
                .param("location", "asdfsdfh"))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(MockMvcResultMatchers.content().json("[]"));
    }

    /**
     * Test if assumes days_count as 1 if argument "days_count" is
     *  not set
     *
     * @throws Exception by MockMvc
     */
    @Test
    public void forecast_no_day_count() throws Exception {
        Mockito
            .when(forecast_service.forecast(LATITUDE, LONGITUDE, 1))
            .thenReturn(new Forecast(daily_forecasts.subList(0, 1), current_weather));

        mock_mvc.perform(
            MockMvcRequestBuilders
                .get("/api/forecast")
                .param("latitude", LATITUDE + "")
                .param("longitude", LONGITUDE + "")
        ).andExpect(MockMvcResultMatchers.status().isAccepted())
            .andExpect(MockMvcResultMatchers.content().json("{\"daily_forecast\":[{\"_min_temperature\":11.3,\"_humidity\":77.0,\"_icon\":\"http://cdn.apixu.com/weather/64x64/day/116.png\",\"_summary\":\"Partly cloudy\",\"_uv\":8.0,\"_max_temperature\":19.5}],\"current_weather\":{\"_wind_speed\":7.194444444444445,\"_pressure\":101700.0,\"_temperature\":16.0,\"_humidity\":77.0,\"_icon\":\"http://cdn.apixu.com/weather/64x64/day/116.png\",\"_summary\":\"Partly cloudy\",\"_uv\":5.0}}"));
    }

    /**
     * Test if gets the number of daily forecasts equal to the number
     *  received on the request parameter
     *
     * @throws Exception by MockMvc
     */
    @Test
    public void forecast_day_count() throws Exception {
        int days_count = 3;

        Mockito
            .when(forecast_service.forecast(LATITUDE, LONGITUDE, days_count))
            .thenReturn(new Forecast(daily_forecasts, current_weather));

        mock_mvc.perform(
            MockMvcRequestBuilders
                .get("/api/forecast")
                .param("latitude", LATITUDE + "")
                .param("longitude", LONGITUDE + "")
                .param("days_count", days_count + "")
        ).andExpect(MockMvcResultMatchers.status().isAccepted())
            .andExpect(MockMvcResultMatchers.content().json("{\"daily_forecast\":[{\"_humidity\":77.0,\"_icon\":\"http://cdn.apixu.com/weather/64x64/day/116.png\",\"_summary\":\"Partly cloudy\",\"_uv\":8.0,\"_max_temperature\":19.5,\"_min_temperature\":11.3},{\"_humidity\":73.0,\"_icon\":\"http://cdn.apixu.com/weather/64x64/day/116.png\",\"_summary\":\"Partly cloudy\",\"_uv\":7.8,\"_max_temperature\":20.4,\"_min_temperature\":12.6},{\"_humidity\":62.0,\"_icon\":\"http://cdn.apixu.com/weather/64x64/day/116.png\",\"_summary\":\"Partly cloudy\",\"_uv\":8.1,\"_max_temperature\":23.9,\"_min_temperature\":13.7}],\"current_weather\":{\"_wind_speed\":7.194444444444445,\"_pressure\":101700.0,\"_temperature\":16.0,\"_humidity\":77.0,\"_icon\":\"http://cdn.apixu.com/weather/64x64/day/116.png\",\"_summary\":\"Partly cloudy\",\"_uv\":5.0}}"));
    }

    /**
     * Test that a BAD_REQUEST HTTP response is received after
     *  sending parameters with invalid format
     * @throws Exception by MockMvc
     */
    @Test
    public void invalid_parameters() throws Exception {
        mock_mvc.perform(
            MockMvcRequestBuilders
                .get("/api/forecast")
                .param("latitude", "asdf")
                .param("longitude", "-8")
        ).andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string("Bad number format for parameters"));

        mock_mvc.perform(
            MockMvcRequestBuilders
                .get("/api/forecast")
                .param("latitude", "40")
                .param("longitude", "-8")
                .param("days_count", "3.5")
        ).andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string("Bad number format for parameters"));
    }
}