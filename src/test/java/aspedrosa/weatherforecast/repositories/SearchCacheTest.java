package aspedrosa.weatherforecast.repositories;

import aspedrosa.weatherforecast.domain.SearchResult;
import org.awaitility.Duration;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.awaitility.Awaitility.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class SearchCacheTest {

    SearchCache search_cache;

    static int TIME_TO_LIVE;

    @BeforeClass
    public static void init() {
        TIME_TO_LIVE = Integer.parseInt(System.getenv("TIME_TO_LIVE"));
    }

    @Before
    public void setUp() {
        search_cache = new SearchCache();
    }

    @Test
    public void no_data_after_time_to_live() {
        List<SearchResult> data = new ArrayList<>();
        data.add(new SearchResult("Ílhavo, Aveiro", 40.5, -8.6));
        data.add(new SearchResult("Praia da Barra, Aveiro", 40.6, -8.7));
        data.add(new SearchResult("Esgueira, Aveiro", 40.6, -8.6));
        search_cache.cache_data("aveiro", data);

        assertNotEquals(Collections.emptyList(), search_cache.get_cached_data("aveiro"));

        long then = System.currentTimeMillis();

        await().atMost(Duration.FIVE_SECONDS).until(
            () -> System.currentTimeMillis() > then + TIME_TO_LIVE * 1000 + 1000
        );

        assertEquals(Collections.emptyList(), search_cache.get_cached_data("aveiro"));

        Cache.Statistics stats = search_cache.get_stats();
        assertEquals(2, stats.get_total_requests());
        assertEquals(1, stats.get_hits());
        assertEquals(1, stats.get_misses());
    }

    @Test
    public void data_after_insert() {
        List<SearchResult> data = new ArrayList<>();
        data.add(new SearchResult("Ílhavo, Aveiro", 40.5, -8.6));
        data.add(new SearchResult("Praia da Barra, Aveiro", 40.6, -8.7));
        data.add(new SearchResult("Esgueira, Aveiro", 40.6, -8.6));
        search_cache.cache_data("aveiro", data);

        assertNotEquals(Collections.emptyList(), search_cache.get_cached_data("aveiro"));

        Cache.Statistics stats = search_cache.get_stats();
        assertEquals(1, stats.get_total_requests());
        assertEquals(1, stats.get_hits());
        assertEquals(0, stats.get_misses());
    }
}