package aspedrosa.weatherforecast.controllers;

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
}