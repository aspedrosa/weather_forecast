package aspedrosa.weatherforecast.services;

import aspedrosa.weatherforecast.domain.SearchResult;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Search api service that uses the LocationIQ API
 */
@Service
public class LocationIqAPIService extends SearchAPIService {

    @Autowired
    RestTemplate rest_template;

    /**
     * @see APIService
     *
     * @param args (String user_location_input) location inserted by the user
     */
    @Override
    public String build_api_url(Object[] args) {
        return "https://us1.locationiq.com/v1/search.php?key=" +
            System.getenv("LOCATION_IQ_APIKEY") +
            "&q=" +
            args[0] +
            "&format=json";
    }

    /**
     * @see SearchAPIService
     */
    @Override
    public List<SearchResult> search(String user_location_input) {
        //Initialize data
        List<SearchResult> data = new ArrayList<>();

        ResponseEntity<String> response_entity;
        //REST call
        try {
            response_entity = rest_template.getForEntity(build_api_url(
                new Object[] {user_location_input}),
                String.class);
        } catch (HttpClientErrorException e) {
            return data;
        }

        //Convert data into json object
        JSONArray response = new JSONArray(response_entity.getBody());

        //Keep track of number of times I have seen the same display_name
        Map<String, Integer> location_count = new HashMap<>();

        //For every location on the response
        for (int i = 0; i < response.length(); i++) {
            JSONObject location = response.getJSONObject(i);

            String display_name = location.getString("display_name");

            Integer count;
            if ((count = location_count.get(display_name)) == null) { //If I have never seen this display_name
                //Create a new one and increment count
                SearchResult tmp_search_result = new SearchResult();

                tmp_search_result.set_display_name(display_name);
                tmp_search_result.set_latitude(location.getDouble("lat"));
                tmp_search_result.set_longitude(location.getDouble("lon"));

                location_count.put(display_name, 1);
                data.add(tmp_search_result);
            }
            else {
                //Else go to the one with the same display_name and do a mean of the coordinates
                // and increment the count
                for (SearchResult read_result : data) {
                    if (read_result.get_display_name().equals(display_name)) {
                        read_result.set_latitude((read_result.get_latitude() * count + location.getDouble("lat")) / (count + 1));
                        read_result.set_longitude((read_result.get_longitude() * count + location.getDouble("lon")) / (count + 1));

                        location_count.put(display_name, count + 1);
                    }
                }
            }
        }

        return data;
    }
}
