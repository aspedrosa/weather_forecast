package aspedrosa.weatherforecast.services;

import aspedrosa.weatherforecast.domain.SearchResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class SearchServiceTest {

    @TestConfiguration
    static class SearchServiceTestContextConfiguration {
        @Bean
        public SearchService search_service() {
            return new SearchService();
        }
    }

    @Autowired
    SearchService search_service;

    @MockBean
    LocationIqAPIService search_api_service;

    @Before
    public void setUp() {
        List<SearchResult> ilhavo = new ArrayList<>();
        ilhavo.add(new SearchResult("Ílhavo, Aveiro, Baixo Vouga, Centro, Portugal",
                                    40.60552025,
                                    -8.68594848811954));

        List<SearchResult> empty = new ArrayList<>();

        Mockito.when(search_api_service.search("ilhavo")).thenReturn(ilhavo);
        Mockito.when(search_api_service.search("asdfsdfh")).thenReturn(empty);
    }

    @Test
    public void search_no_result() {
        List<SearchResult> response = search_api_service.search("asdfsdfh");

        assertTrue(response.isEmpty());
    }

    @Test
    public void search() {

        List<SearchResult> response = search_service.search("ilhavo");

        assertFalse(response.isEmpty());

        assertEquals(response.size(), 1);

        assertEquals(response.get(0).get_display_name(), "Ílhavo, Aveiro, Baixo Vouga, Centro, Portugal");
        assertEquals(response.get(0).get_latitude(), 40.60552025, 0);
        assertEquals(response.get(0).get_longitude(), -8.68594848811954, 0);
    }
}