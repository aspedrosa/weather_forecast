package aspedrosa.weatherforecast.services;

import aspedrosa.weatherforecast.domain.SearchResult;
import aspedrosa.weatherforecast.repositories.SearchCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Test the service and repository layer related
 *  to location search
 */
@RunWith(SpringRunner.class)
public class SearchServiceIT {

    @TestConfiguration
    static class SearchServiceITContextConfiguration {
        @Bean
        public SearchService search_service() {
            return new SearchService();
        }

        @Bean
        public LocationIqAPIService search_api_service() {
            return new LocationIqAPIService();
        }

        @Bean
        public SearchCache search_cache() {
            return new SearchCache();
        }
    }
    @Autowired
    SearchService search_service;

    @MockBean
    RestTemplate rest_template;

    /**
     * Test not found response
     */
    @Test
    public void search_no_result() {
        Mockito.when(rest_template.getForEntity(
            search_service.search_api_service.build_api_url(
                new Object[] {"asdfsdfh"}),
            String.class)
        ).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        List<SearchResult> response = search_service.search("asdfsdfh");

        assertTrue(response.isEmpty());
    }

    /**
     * Test regular response
     */
    @Test
    public void search() {
        Mockito.when(rest_template.getForEntity(
            search_service.search_api_service.build_api_url(
                new Object[] {"ilhavo"}),
            String.class)
        ).thenReturn(new ResponseEntity<>(
            "[{ \"place_id\": 214919473, \"licence\": \"https://locationiq.com/attribution\", \"osm_type\": \"relation\", \"osm_id\": 5325848, \"boundingbox\": [\"40.562442\", \"40.6598551\", \"-8.7660889\", \"-8.6180954\"], \"lat\": \"40.6109125\", \"lon\": \"-8.70525067623908\", \"display_name\": \"Ílhavo, Aveiro, Baixo Vouga, Centro, Portugal\", \"class\": \"boundary\", \"type\": \"administrative\", \"importance\": 0.4088805262029, \"icon\": \"https://locationiq.org/static/images/mapicons/poi_boundary_administrative.p.20.png\" }, { \"place_id\": 215022613, \"licence\": \"https://locationiq.com/attribution\", \"osm_type\": \"relation\", \"osm_id\": 6021276, \"boundingbox\": [\"40.562442\", \"40.6310255\", \"-8.7270573\", \"-8.6180954\"], \"lat\": \"40.600128\", \"lon\": \"-8.6666463\", \"display_name\": \"Ílhavo, Aveiro, Baixo Vouga, Centro, Portugal\", \"class\": \"boundary\", \"type\": \"administrative\", \"importance\": 0.35, \"icon\": \"https://locationiq.org/static/images/mapicons/poi_boundary_administrative.p.20.png\" }]",
            HttpStatus.ACCEPTED
        ));

        List<SearchResult> response = search_service.search("ilhavo");

        assertFalse(response.isEmpty());

        assertEquals(response.size(), 1);

        assertEquals("Ílhavo, Aveiro, Baixo Vouga, Centro, Portugal", response.get(0).get_display_name());
        assertEquals(40.60552025, response.get(0).get_latitude(), 0);
        assertEquals(-8.68594848811954, response.get(0).get_longitude(), 0);
    }
}