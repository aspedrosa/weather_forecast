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

    /**
     * Test if assumes days_count as 1 if no value is received
     *
     * @throws Exception by MockMvc
     */
    @Test
    public void forecast_no_day_count() throws Exception {
        double latitude = 40.641203, longitude = -8.655614;

        Mockito.when(rest_template.getForEntity(
            forecast_service.primary_api_service.build_api_url(new Object[] {latitude, longitude}),
            String.class
        )).thenReturn(new ResponseEntity<>(
            "{ \"location\": { \"name\": \"Aveiro\", \"region\": \"Aveiro\", \"country\": \"Portugal\", \"lat\": 40.64, \"lon\": -8.66, \"tz_id\": \"Europe/Lisbon\", \"localtime_epoch\": 1556632361, \"localtime\": \"2019-04-30 14:52\" }, \"current\": { \"last_updated_epoch\": 1556631914, \"last_updated\": \"2019-04-30 14:45\", \"temp_c\": 16.0, \"temp_f\": 60.8, \"is_day\": 1, \"condition\": { \"text\": \"Partly cloudy\", \"icon\": \"//cdn.apixu.com/weather/64x64/day/116.png\", \"code\": 1003 }, \"wind_mph\": 16.1, \"wind_kph\": 25.9, \"wind_degree\": 340, \"wind_dir\": \"NNW\", \"pressure_mb\": 1017.0, \"pressure_in\": 30.5, \"precip_mm\": 0.0, \"precip_in\": 0.0, \"humidity\": 77, \"cloud\": 25, \"feelslike_c\": 16.0, \"feelslike_f\": 60.8, \"vis_km\": 10.0, \"vis_miles\": 6.0, \"uv\": 5.0, \"gust_mph\": 17.2, \"gust_kph\": 27.7 }, \"forecast\": { \"forecastday\": [ { \"date\": \"2019-04-30\", \"date_epoch\": 1556582400, \"day\": { \"maxtemp_c\": 19.5, \"maxtemp_f\": 67.1, \"mintemp_c\": 11.3, \"mintemp_f\": 52.3, \"avgtemp_c\": 15.0, \"avgtemp_f\": 59.0, \"maxwind_mph\": 12.5, \"maxwind_kph\": 20.2, \"totalprecip_mm\": 0.0, \"totalprecip_in\": 0.0, \"avgvis_km\": 18.4, \"avgvis_miles\": 11.0, \"avghumidity\": 77.0, \"condition\": { \"text\": \"Partly cloudy\", \"icon\": \"//cdn.apixu.com/weather/64x64/day/116.png\", \"code\": 1003 }, \"uv\": 8.0 }, \"astro\": { \"sunrise\": \"06:35 AM\", \"sunset\": \"08:29 PM\", \"moonrise\": \"04:48 AM\", \"moonset\": \"04:05 PM\" } }, { \"date\": \"2019-05-01\", \"date_epoch\": 1556668800, \"day\": { \"maxtemp_c\": 20.4, \"maxtemp_f\": 68.7, \"mintemp_c\": 12.6, \"mintemp_f\": 54.7, \"avgtemp_c\": 16.2, \"avgtemp_f\": 61.1, \"maxwind_mph\": 14.5, \"maxwind_kph\": 23.4, \"totalprecip_mm\": 0.0, \"totalprecip_in\": 0.0, \"avgvis_km\": 19.3, \"avgvis_miles\": 11.0, \"avghumidity\": 73.0, \"condition\": { \"text\": \"Partly cloudy\", \"icon\": \"//cdn.apixu.com/weather/64x64/day/116.png\", \"code\": 1003 }, \"uv\": 7.8 }, \"astro\": { \"sunrise\": \"06:34 AM\", \"sunset\": \"08:30 PM\", \"moonrise\": \"05:15 AM\", \"moonset\": \"05:04 PM\" } }, { \"date\": \"2019-05-02\", \"date_epoch\": 1556755200, \"day\": { \"maxtemp_c\": 23.9, \"maxtemp_f\": 75.0, \"mintemp_c\": 13.7, \"mintemp_f\": 56.7, \"avgtemp_c\": 18.6, \"avgtemp_f\": 65.5, \"maxwind_mph\": 10.5, \"maxwind_kph\": 16.9, \"totalprecip_mm\": 0.0, \"totalprecip_in\": 0.0, \"avgvis_km\": 20.0, \"avgvis_miles\": 12.0, \"avghumidity\": 62.0, \"condition\": { \"text\": \"Partly cloudy\", \"icon\": \"//cdn.apixu.com/weather/64x64/day/116.png\", \"code\": 1003 }, \"uv\": 8.1 }, \"astro\": { \"sunrise\": \"06:32 AM\", \"sunset\": \"08:31 PM\", \"moonrise\": \"05:41 AM\", \"moonset\": \"06:03 PM\" } }, { \"date\": \"2019-05-03\", \"date_epoch\": 1556841600, \"day\": { \"maxtemp_c\": 21.7, \"maxtemp_f\": 71.1, \"mintemp_c\": 14.1, \"mintemp_f\": 57.4, \"avgtemp_c\": 17.6, \"avgtemp_f\": 63.6, \"maxwind_mph\": 19.0, \"maxwind_kph\": 30.6, \"totalprecip_mm\": 0.0, \"totalprecip_in\": 0.0, \"avgvis_km\": 20.0, \"avgvis_miles\": 12.0, \"avghumidity\": 58.0, \"condition\": { \"text\": \"Partly cloudy\", \"icon\": \"//cdn.apixu.com/weather/64x64/day/116.png\", \"code\": 1003 }, \"uv\": 7.8 }, \"astro\": { \"sunrise\": \"06:31 AM\", \"sunset\": \"08:32 PM\", \"moonrise\": \"06:07 AM\", \"moonset\": \"07:05 PM\" } }, { \"date\": \"2019-05-04\", \"date_epoch\": 1556928000, \"day\": { \"maxtemp_c\": 22.1, \"maxtemp_f\": 71.8, \"mintemp_c\": 12.4, \"mintemp_f\": 54.3, \"avgtemp_c\": 16.6, \"avgtemp_f\": 61.9, \"maxwind_mph\": 12.1, \"maxwind_kph\": 19.4, \"totalprecip_mm\": 0.0, \"totalprecip_in\": 0.0, \"avgvis_km\": 20.0, \"avgvis_miles\": 12.0, \"avghumidity\": 58.0, \"condition\": { \"text\": \"Partly cloudy\", \"icon\": \"//cdn.apixu.com/weather/64x64/day/116.png\", \"code\": 1003 }, \"uv\": 8.0 }, \"astro\": { \"sunrise\": \"06:30 AM\", \"sunset\": \"08:33 PM\", \"moonrise\": \"06:35 AM\", \"moonset\": \"08:08 PM\" } }, { \"date\": \"2019-05-05\", \"date_epoch\": 1557014400, \"day\": { \"maxtemp_c\": 24.7, \"maxtemp_f\": 76.5, \"mintemp_c\": 13.3, \"mintemp_f\": 55.9, \"avgtemp_c\": 18.3, \"avgtemp_f\": 64.9, \"maxwind_mph\": 10.3, \"maxwind_kph\": 16.6, \"totalprecip_mm\": 0.0, \"totalprecip_in\": 0.0, \"avgvis_km\": 20.0, \"avgvis_miles\": 12.0, \"avghumidity\": 56.0, \"condition\": { \"text\": \"Partly cloudy\", \"icon\": \"//cdn.apixu.com/weather/64x64/day/116.png\", \"code\": 1003 }, \"uv\": 5.0 }, \"astro\": { \"sunrise\": \"06:29 AM\", \"sunset\": \"08:34 PM\", \"moonrise\": \"07:06 AM\", \"moonset\": \"09:13 PM\" } }, { \"date\": \"2019-05-06\", \"date_epoch\": 1557100800, \"day\": { \"maxtemp_c\": 26.3, \"maxtemp_f\": 79.3, \"mintemp_c\": 12.6, \"mintemp_f\": 54.7, \"avgtemp_c\": 19.2, \"avgtemp_f\": 66.6, \"maxwind_mph\": 9.2, \"maxwind_kph\": 14.8, \"totalprecip_mm\": 0.0, \"totalprecip_in\": 0.0, \"avgvis_km\": 20.0, \"avgvis_miles\": 12.0, \"avghumidity\": 51.0, \"condition\": { \"text\": \"Partly cloudy\", \"icon\": \"//cdn.apixu.com/weather/64x64/day/116.png\", \"code\": 1003 }, \"uv\": 5.0 }, \"astro\": { \"sunrise\": \"06:28 AM\", \"sunset\": \"08:35 PM\", \"moonrise\": \"07:41 AM\", \"moonset\": \"10:18 PM\" } } ] } }\n",
            HttpStatus.ACCEPTED
        ));

        mock_mvc.perform(
            MockMvcRequestBuilders
                .get("/api/forecast")
                .param("latitude", latitude + "")
                .param("longitude", longitude + "")
        ).andExpect(MockMvcResultMatchers.status().isAccepted())
         .andExpect(MockMvcResultMatchers.content().json("{\"daily_forecast\":[{\"_min_temperature\":11.3,\"_humidity\":77.0,\"_icon\":\"http://cdn.apixu.com/weather/64x64/day/116.png\",\"_summary\":\"Partly cloudy\",\"_uv\":8.0,\"_max_temperature\":19.5}],\"current_weather\":{\"_wind_speed\":7.194444444444445,\"_pressure\":101700.0,\"_temperature\":16.0,\"_humidity\":77.0,\"_icon\":\"http://cdn.apixu.com/weather/64x64/day/116.png\",\"_summary\":\"Partly cloudy\",\"_uv\":5.0}}"));
    }

    /**
     * Test if rests the number of daily forecasts equal to the number
     *  received on the request parameter
     *
     * @throws Exception by MockMvc
     */
    @Test
    public void forecast_day_count() throws Exception {
        double latitude = 40.641203, longitude = -8.655614;

        Mockito.when(rest_template.getForEntity(
            forecast_service.primary_api_service.build_api_url(new Object[] {latitude, longitude}),
            String.class
        )).thenReturn(new ResponseEntity<>(
            "{ \"location\": { \"name\": \"Aveiro\", \"region\": \"Aveiro\", \"country\": \"Portugal\", \"lat\": 40.64, \"lon\": -8.66, \"tz_id\": \"Europe/Lisbon\", \"localtime_epoch\": 1556632361, \"localtime\": \"2019-04-30 14:52\" }, \"current\": { \"last_updated_epoch\": 1556631914, \"last_updated\": \"2019-04-30 14:45\", \"temp_c\": 16.0, \"temp_f\": 60.8, \"is_day\": 1, \"condition\": { \"text\": \"Partly cloudy\", \"icon\": \"//cdn.apixu.com/weather/64x64/day/116.png\", \"code\": 1003 }, \"wind_mph\": 16.1, \"wind_kph\": 25.9, \"wind_degree\": 340, \"wind_dir\": \"NNW\", \"pressure_mb\": 1017.0, \"pressure_in\": 30.5, \"precip_mm\": 0.0, \"precip_in\": 0.0, \"humidity\": 77, \"cloud\": 25, \"feelslike_c\": 16.0, \"feelslike_f\": 60.8, \"vis_km\": 10.0, \"vis_miles\": 6.0, \"uv\": 5.0, \"gust_mph\": 17.2, \"gust_kph\": 27.7 }, \"forecast\": { \"forecastday\": [ { \"date\": \"2019-04-30\", \"date_epoch\": 1556582400, \"day\": { \"maxtemp_c\": 19.5, \"maxtemp_f\": 67.1, \"mintemp_c\": 11.3, \"mintemp_f\": 52.3, \"avgtemp_c\": 15.0, \"avgtemp_f\": 59.0, \"maxwind_mph\": 12.5, \"maxwind_kph\": 20.2, \"totalprecip_mm\": 0.0, \"totalprecip_in\": 0.0, \"avgvis_km\": 18.4, \"avgvis_miles\": 11.0, \"avghumidity\": 77.0, \"condition\": { \"text\": \"Partly cloudy\", \"icon\": \"//cdn.apixu.com/weather/64x64/day/116.png\", \"code\": 1003 }, \"uv\": 8.0 }, \"astro\": { \"sunrise\": \"06:35 AM\", \"sunset\": \"08:29 PM\", \"moonrise\": \"04:48 AM\", \"moonset\": \"04:05 PM\" } }, { \"date\": \"2019-05-01\", \"date_epoch\": 1556668800, \"day\": { \"maxtemp_c\": 20.4, \"maxtemp_f\": 68.7, \"mintemp_c\": 12.6, \"mintemp_f\": 54.7, \"avgtemp_c\": 16.2, \"avgtemp_f\": 61.1, \"maxwind_mph\": 14.5, \"maxwind_kph\": 23.4, \"totalprecip_mm\": 0.0, \"totalprecip_in\": 0.0, \"avgvis_km\": 19.3, \"avgvis_miles\": 11.0, \"avghumidity\": 73.0, \"condition\": { \"text\": \"Partly cloudy\", \"icon\": \"//cdn.apixu.com/weather/64x64/day/116.png\", \"code\": 1003 }, \"uv\": 7.8 }, \"astro\": { \"sunrise\": \"06:34 AM\", \"sunset\": \"08:30 PM\", \"moonrise\": \"05:15 AM\", \"moonset\": \"05:04 PM\" } }, { \"date\": \"2019-05-02\", \"date_epoch\": 1556755200, \"day\": { \"maxtemp_c\": 23.9, \"maxtemp_f\": 75.0, \"mintemp_c\": 13.7, \"mintemp_f\": 56.7, \"avgtemp_c\": 18.6, \"avgtemp_f\": 65.5, \"maxwind_mph\": 10.5, \"maxwind_kph\": 16.9, \"totalprecip_mm\": 0.0, \"totalprecip_in\": 0.0, \"avgvis_km\": 20.0, \"avgvis_miles\": 12.0, \"avghumidity\": 62.0, \"condition\": { \"text\": \"Partly cloudy\", \"icon\": \"//cdn.apixu.com/weather/64x64/day/116.png\", \"code\": 1003 }, \"uv\": 8.1 }, \"astro\": { \"sunrise\": \"06:32 AM\", \"sunset\": \"08:31 PM\", \"moonrise\": \"05:41 AM\", \"moonset\": \"06:03 PM\" } }, { \"date\": \"2019-05-03\", \"date_epoch\": 1556841600, \"day\": { \"maxtemp_c\": 21.7, \"maxtemp_f\": 71.1, \"mintemp_c\": 14.1, \"mintemp_f\": 57.4, \"avgtemp_c\": 17.6, \"avgtemp_f\": 63.6, \"maxwind_mph\": 19.0, \"maxwind_kph\": 30.6, \"totalprecip_mm\": 0.0, \"totalprecip_in\": 0.0, \"avgvis_km\": 20.0, \"avgvis_miles\": 12.0, \"avghumidity\": 58.0, \"condition\": { \"text\": \"Partly cloudy\", \"icon\": \"//cdn.apixu.com/weather/64x64/day/116.png\", \"code\": 1003 }, \"uv\": 7.8 }, \"astro\": { \"sunrise\": \"06:31 AM\", \"sunset\": \"08:32 PM\", \"moonrise\": \"06:07 AM\", \"moonset\": \"07:05 PM\" } }, { \"date\": \"2019-05-04\", \"date_epoch\": 1556928000, \"day\": { \"maxtemp_c\": 22.1, \"maxtemp_f\": 71.8, \"mintemp_c\": 12.4, \"mintemp_f\": 54.3, \"avgtemp_c\": 16.6, \"avgtemp_f\": 61.9, \"maxwind_mph\": 12.1, \"maxwind_kph\": 19.4, \"totalprecip_mm\": 0.0, \"totalprecip_in\": 0.0, \"avgvis_km\": 20.0, \"avgvis_miles\": 12.0, \"avghumidity\": 58.0, \"condition\": { \"text\": \"Partly cloudy\", \"icon\": \"//cdn.apixu.com/weather/64x64/day/116.png\", \"code\": 1003 }, \"uv\": 8.0 }, \"astro\": { \"sunrise\": \"06:30 AM\", \"sunset\": \"08:33 PM\", \"moonrise\": \"06:35 AM\", \"moonset\": \"08:08 PM\" } }, { \"date\": \"2019-05-05\", \"date_epoch\": 1557014400, \"day\": { \"maxtemp_c\": 24.7, \"maxtemp_f\": 76.5, \"mintemp_c\": 13.3, \"mintemp_f\": 55.9, \"avgtemp_c\": 18.3, \"avgtemp_f\": 64.9, \"maxwind_mph\": 10.3, \"maxwind_kph\": 16.6, \"totalprecip_mm\": 0.0, \"totalprecip_in\": 0.0, \"avgvis_km\": 20.0, \"avgvis_miles\": 12.0, \"avghumidity\": 56.0, \"condition\": { \"text\": \"Partly cloudy\", \"icon\": \"//cdn.apixu.com/weather/64x64/day/116.png\", \"code\": 1003 }, \"uv\": 5.0 }, \"astro\": { \"sunrise\": \"06:29 AM\", \"sunset\": \"08:34 PM\", \"moonrise\": \"07:06 AM\", \"moonset\": \"09:13 PM\" } }, { \"date\": \"2019-05-06\", \"date_epoch\": 1557100800, \"day\": { \"maxtemp_c\": 26.3, \"maxtemp_f\": 79.3, \"mintemp_c\": 12.6, \"mintemp_f\": 54.7, \"avgtemp_c\": 19.2, \"avgtemp_f\": 66.6, \"maxwind_mph\": 9.2, \"maxwind_kph\": 14.8, \"totalprecip_mm\": 0.0, \"totalprecip_in\": 0.0, \"avgvis_km\": 20.0, \"avgvis_miles\": 12.0, \"avghumidity\": 51.0, \"condition\": { \"text\": \"Partly cloudy\", \"icon\": \"//cdn.apixu.com/weather/64x64/day/116.png\", \"code\": 1003 }, \"uv\": 5.0 }, \"astro\": { \"sunrise\": \"06:28 AM\", \"sunset\": \"08:35 PM\", \"moonrise\": \"07:41 AM\", \"moonset\": \"10:18 PM\" } } ] } }\n",
            HttpStatus.ACCEPTED
        ));

        mock_mvc.perform(
            MockMvcRequestBuilders
                .get("/api/forecast")
                .param("latitude", "40.641203")
                .param("longitude", "-8.655614")
                .param("days_count", "3")
        ).andExpect(MockMvcResultMatchers.status().isAccepted())
         .andExpect(MockMvcResultMatchers.content().json("{\"daily_forecast\":[{\"_humidity\":77.0,\"_icon\":\"http://cdn.apixu.com/weather/64x64/day/116.png\",\"_summary\":\"Partly cloudy\",\"_uv\":8.0,\"_max_temperature\":19.5,\"_min_temperature\":11.3},{\"_humidity\":73.0,\"_icon\":\"http://cdn.apixu.com/weather/64x64/day/116.png\",\"_summary\":\"Partly cloudy\",\"_uv\":7.8,\"_max_temperature\":20.4,\"_min_temperature\":12.6},{\"_humidity\":62.0,\"_icon\":\"http://cdn.apixu.com/weather/64x64/day/116.png\",\"_summary\":\"Partly cloudy\",\"_uv\":8.1,\"_max_temperature\":23.9,\"_min_temperature\":13.7}],\"current_weather\":{\"_wind_speed\":7.194444444444445,\"_pressure\":101700.0,\"_temperature\":16.0,\"_humidity\":77.0,\"_icon\":\"http://cdn.apixu.com/weather/64x64/day/116.png\",\"_summary\":\"Partly cloudy\",\"_uv\":5.0}}"));
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