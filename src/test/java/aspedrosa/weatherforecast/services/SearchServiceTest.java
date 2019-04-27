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
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
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

    @MockBean
    SearchCache search_cache;

    /**
     * Test not found response.
     * Verify that checked cache but didn't store
     */
    @Test
    public void search_no_result() {
        Mockito.when(search_api_service.search("asdfsdfh")).thenReturn(Collections.emptyList());
        Mockito.when(search_cache.get_cached_data("asdfsdfh")).thenReturn(null).thenReturn(null);

        List<SearchResult> response = search_service.search("asdfsdfh");

        assertTrue(response.isEmpty());

        Mockito.verify(search_cache, Mockito.times(1)).get_cached_data("asdfsdfh");
        Mockito.verify(search_cache, Mockito.times(0)).cache_data("asdfsdfh", Collections.emptyList());
    }

    /**
     * Test two regular searches
     * Verify that checked cached for both times but
     *  only stored that one time
     */
    @Test
    public void search() {
        List<SearchResult> ilhavo = new ArrayList<>();
        ilhavo.add(new SearchResult("Ílhavo, Aveiro, Baixo Vouga, Centro, Portugal",
            40.60552025,
            -8.68594848811954));
        Mockito.when(search_api_service.search("ilhavo")).thenReturn(ilhavo);
        Mockito.when(search_cache.get_cached_data("ilhavo")).thenReturn(null).thenReturn(ilhavo);

        List<SearchResult> response = search_service.search("ilhavo");

        assertFalse(response.isEmpty());

        assertEquals(response.size(), 1);

        assertEquals("Ílhavo, Aveiro, Baixo Vouga, Centro, Portugal", response.get(0).get_display_name());
        assertEquals(40.60552025, response.get(0).get_latitude(), 0);
        assertEquals(-8.68594848811954, response.get(0).get_longitude(), 0);

        search_service.search("ilhavo");

        Mockito.verify(search_cache, Mockito.times(2)).get_cached_data("ilhavo");
        Mockito.verify(search_cache, Mockito.times(1)).cache_data("ilhavo", ilhavo);
    }
}