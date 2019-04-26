package aspedrosa.weatherforecast.repositories;

import aspedrosa.weatherforecast.domain.SearchResult;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SearchCacheTest {

    SearchCache search_cache;

    int TIME_TO_LIVE;

    @Before
    public void setUp() {
        search_cache = new SearchCache();
        TIME_TO_LIVE = Integer.parseInt(System.getenv("TIME_TO_LIVE"));
    }

    @Test
    public void no_data_after_time_to_live() {
        List<SearchResult> data = new ArrayList<>();
        data.add(new SearchResult("Ílhavo, Aveiro", 40.5, -8.6));
        data.add(new SearchResult("Praia da Barra, Aveiro", 40.6, -8.7));
        data.add(new SearchResult("Esgueira, Aveiro", 40.6, -8.6));
        search_cache.cache_data("aveiro", data);

        assertNotNull(search_cache.get_cached_data("aveiro"));

        try {
            Thread.sleep(TIME_TO_LIVE * 1000 + 1000);
        } catch (InterruptedException e) {}

        assertNull(search_cache.get_cached_data("aveiro"));
    }

    @Test
    public void data_after_insert() {
        List<SearchResult> data = new ArrayList<>();
        data.add(new SearchResult("Ílhavo, Aveiro", 40.5, -8.6));
        data.add(new SearchResult("Praia da Barra, Aveiro", 40.6, -8.7));
        data.add(new SearchResult("Esgueira, Aveiro", 40.6, -8.6));
        search_cache.cache_data("aveiro", data);

        assertNotNull(search_cache.get_cached_data("aveiro"));
    }
}