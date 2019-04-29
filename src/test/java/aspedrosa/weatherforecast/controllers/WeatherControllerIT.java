package aspedrosa.weatherforecast.controllers;

import aspedrosa.weatherforecast.services.ForecastService;
import aspedrosa.weatherforecast.services.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Test all 3 application layers (controllers, services and controllers),
 *  mocking response from external apis
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WeatherControllerIT {

    @Autowired
    MockMvc mock_mvc;

    @MockBean
    RestTemplate rest_template;

    @Autowired
    SearchService search_service;

    @Autowired
    ForecastService forecast_service;

    public void forecast() {
    }

    /**
     * Test regular search result
     *
     * @throws Exception by MockMvc
     */
    @Test
    public void search_accepted() throws Exception {
        Mockito.when(rest_template.getForEntity(
            search_service.search_api_service.build_api_url(
                new Object[] {"ilhavo"}),
            String.class)
        ).thenReturn(new ResponseEntity<>(
            "[{ \"place_id\": 214919473, \"licence\": \"https://locationiq.com/attribution\", \"osm_type\": \"relation\", \"osm_id\": 5325848, \"boundingbox\": [\"40.562442\", \"40.6598551\", \"-8.7660889\", \"-8.6180954\"], \"lat\": \"40.6109125\", \"lon\": \"-8.70525067623908\", \"display_name\": \"Ílhavo, Aveiro, Baixo Vouga, Centro, Portugal\", \"class\": \"boundary\", \"type\": \"administrative\", \"importance\": 0.4088805262029, \"icon\": \"https://locationiq.org/static/images/mapicons/poi_boundary_administrative.p.20.png\" }, { \"place_id\": 215022613, \"licence\": \"https://locationiq.com/attribution\", \"osm_type\": \"relation\", \"osm_id\": 6021276, \"boundingbox\": [\"40.562442\", \"40.6310255\", \"-8.7270573\", \"-8.6180954\"], \"lat\": \"40.600128\", \"lon\": \"-8.6666463\", \"display_name\": \"Ílhavo, Aveiro, Baixo Vouga, Centro, Portugal\", \"class\": \"boundary\", \"type\": \"administrative\", \"importance\": 0.35, \"icon\": \"https://locationiq.org/static/images/mapicons/poi_boundary_administrative.p.20.png\" }]",
            HttpStatus.ACCEPTED
        ));

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
        Mockito.when(rest_template.getForEntity(
            search_service.search_api_service.build_api_url(
                new Object[] {"asdfsdfh"}),
            String.class)
        ).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        mock_mvc.perform(
            MockMvcRequestBuilders
                .get("/api/search")
                .param("location", "asdfsdfh"))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andExpect(MockMvcResultMatchers.content().json("[]"));
    }
}