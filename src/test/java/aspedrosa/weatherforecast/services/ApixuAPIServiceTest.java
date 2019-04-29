package aspedrosa.weatherforecast.services;

import aspedrosa.weatherforecast.domain.CurrentWeather;
import aspedrosa.weatherforecast.domain.DailyForecast;
import aspedrosa.weatherforecast.domain.Forecast;
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
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Test the service dedicated to the apixu api
 */
@RunWith(SpringRunner.class)
public class ApixuAPIServiceTest {

    @TestConfiguration
    static class ApixuAPIServiceTestContextConfiguration {
        @Bean
        public ApixuAPIService search_api_service() {
            return new ApixuAPIService();
        }
    }

    @Autowired
    ApixuAPIService api_service;

    @MockBean
    RestTemplate rest_template;

    /**
     * Test if the conversion between units are done right
     *         number of days is the same as received
     */
    @Test
    public void forecast() {
        double latitude = 40.641340, longitude = -8.653667;

        Mockito.when(
            rest_template.getForEntity(
                api_service.build_api_url(new Object[] {latitude, longitude}),
                String.class
            )
        ).thenReturn(
            new ResponseEntity<>(
                "{ \"location\": { \"name\": \"Aveiro\", \"region\": \"Aveiro\", \"country\": \"Portugal\", \"lat\": 40.64, \"lon\": -8.65, \"tz_id\": \"Europe/Lisbon\", \"localtime_epoch\": 1556560076, \"localtime\": \"2019-04-29 18:47\" }, \"current\": { \"last_updated_epoch\": 1556559915, \"last_updated\": \"2019-04-29 18:45\", \"temp_c\": 16.0, \"temp_f\": 60.8, \"is_day\": 1, \"condition\": { \"text\": \"Partly cloudy\", \"icon\": \"//cdn.apixu.com/weather/64x64/day/116.png\", \"code\": 1003 }, \"wind_mph\": 15.0, \"wind_kph\": 24.1, \"wind_degree\": 360, \"wind_dir\": \"N\", \"pressure_mb\": 1017.0, \"pressure_in\": 30.5, \"precip_mm\": 0.0, \"precip_in\": 0.0, \"humidity\": 82, \"cloud\": 50, \"feelslike_c\": 16.0, \"feelslike_f\": 60.8, \"vis_km\": 10.0, \"vis_miles\": 6.0, \"uv\": 5.0, \"gust_mph\": 16.1, \"gust_kph\": 25.9 }, \"forecast\": { \"forecastday\": [ { \"date\": \"2019-04-29\", \"date_epoch\": 1556496000, \"day\": { \"maxtemp_c\": 20.1, \"maxtemp_f\": 68.2, \"mintemp_c\": 12.1, \"mintemp_f\": 53.8, \"avgtemp_c\": 16.3, \"avgtemp_f\": 61.3, \"maxwind_mph\": 12.3, \"maxwind_kph\": 19.8, \"totalprecip_mm\": 0.0, \"totalprecip_in\": 0.0, \"avgvis_km\": 18.8, \"avgvis_miles\": 11.0, \"avghumidity\": 71.0, \"condition\": { \"text\": \"Partly cloudy\", \"icon\": \"//cdn.apixu.com/weather/64x64/day/116.png\", \"code\": 1003 }, \"uv\": 8.6 }, \"astro\": { \"sunrise\": \"06:36 AM\", \"sunset\": \"08:28 PM\", \"moonrise\": \"04:19 AM\", \"moonset\": \"03:06 PM\" } }, { \"date\": \"2019-04-30\", \"date_epoch\": 1556582400, \"day\": { \"maxtemp_c\": 19.3, \"maxtemp_f\": 66.7, \"mintemp_c\": 11.1, \"mintemp_f\": 52.0, \"avgtemp_c\": 14.8, \"avgtemp_f\": 58.7, \"maxwind_mph\": 12.1, \"maxwind_kph\": 19.4, \"totalprecip_mm\": 0.0, \"totalprecip_in\": 0.0, \"avgvis_km\": 19.5, \"avgvis_miles\": 12.0, \"avghumidity\": 78.0, \"condition\": { \"text\": \"Partly cloudy\", \"icon\": \"//cdn.apixu.com/weather/64x64/day/116.png\", \"code\": 1003 }, \"uv\": 8.0 }, \"astro\": { \"sunrise\": \"06:35 AM\", \"sunset\": \"08:29 PM\", \"moonrise\": \"04:48 AM\", \"moonset\": \"04:05 PM\" } }, { \"date\": \"2019-05-01\", \"date_epoch\": 1556668800, \"day\": { \"maxtemp_c\": 20.5, \"maxtemp_f\": 68.9, \"mintemp_c\": 12.6, \"mintemp_f\": 54.7, \"avgtemp_c\": 16.4, \"avgtemp_f\": 61.6, \"maxwind_mph\": 12.8, \"maxwind_kph\": 20.5, \"totalprecip_mm\": 0.0, \"totalprecip_in\": 0.0, \"avgvis_km\": 18.8, \"avgvis_miles\": 11.0, \"avghumidity\": 75.0, \"condition\": { \"text\": \"Partly cloudy\", \"icon\": \"//cdn.apixu.com/weather/64x64/day/116.png\", \"code\": 1003 }, \"uv\": 7.9 }, \"astro\": { \"sunrise\": \"06:34 AM\", \"sunset\": \"08:30 PM\", \"moonrise\": \"05:15 AM\", \"moonset\": \"05:04 PM\" } }, { \"date\": \"2019-05-02\", \"date_epoch\": 1556755200, \"day\": { \"maxtemp_c\": 25.2, \"maxtemp_f\": 77.4, \"mintemp_c\": 15.0, \"mintemp_f\": 59.0, \"avgtemp_c\": 19.0, \"avgtemp_f\": 66.1, \"maxwind_mph\": 11.6, \"maxwind_kph\": 18.7, \"totalprecip_mm\": 0.0, \"totalprecip_in\": 0.0, \"avgvis_km\": 19.9, \"avgvis_miles\": 12.0, \"avghumidity\": 66.0, \"condition\": { \"text\": \"Partly cloudy\", \"icon\": \"//cdn.apixu.com/weather/64x64/day/116.png\", \"code\": 1003 }, \"uv\": 8.0 }, \"astro\": { \"sunrise\": \"06:32 AM\", \"sunset\": \"08:31 PM\", \"moonrise\": \"05:41 AM\", \"moonset\": \"06:03 PM\" } }, { \"date\": \"2019-05-03\", \"date_epoch\": 1556841600, \"day\": { \"maxtemp_c\": 22.9, \"maxtemp_f\": 73.2, \"mintemp_c\": 15.4, \"mintemp_f\": 59.7, \"avgtemp_c\": 18.9, \"avgtemp_f\": 66.1, \"maxwind_mph\": 13.9, \"maxwind_kph\": 22.3, \"totalprecip_mm\": 0.0, \"totalprecip_in\": 0.0, \"avgvis_km\": 20.0, \"avgvis_miles\": 12.0, \"avghumidity\": 61.0, \"condition\": { \"text\": \"Partly cloudy\", \"icon\": \"//cdn.apixu.com/weather/64x64/day/116.png\", \"code\": 1003 }, \"uv\": 7.9 }, \"astro\": { \"sunrise\": \"06:31 AM\", \"sunset\": \"08:32 PM\", \"moonrise\": \"06:07 AM\", \"moonset\": \"07:05 PM\" } }, { \"date\": \"2019-05-04\", \"date_epoch\": 1556928000, \"day\": { \"maxtemp_c\": 21.3, \"maxtemp_f\": 70.3, \"mintemp_c\": 13.5, \"mintemp_f\": 56.3, \"avgtemp_c\": 17.0, \"avgtemp_f\": 62.6, \"maxwind_mph\": 13.4, \"maxwind_kph\": 21.6, \"totalprecip_mm\": 0.0, \"totalprecip_in\": 0.0, \"avgvis_km\": 20.0, \"avgvis_miles\": 12.0, \"avghumidity\": 64.0, \"condition\": { \"text\": \"Partly cloudy\", \"icon\": \"//cdn.apixu.com/weather/64x64/day/116.png\", \"code\": 1003 }, \"uv\": 5.0 }, \"astro\": { \"sunrise\": \"06:30 AM\", \"sunset\": \"08:33 PM\", \"moonrise\": \"06:35 AM\", \"moonset\": \"08:08 PM\" } }, { \"date\": \"2019-05-05\", \"date_epoch\": 1557014400, \"day\": { \"maxtemp_c\": 25.3, \"maxtemp_f\": 77.5, \"mintemp_c\": 13.9, \"mintemp_f\": 57.0, \"avgtemp_c\": 18.3, \"avgtemp_f\": 64.9, \"maxwind_mph\": 10.1, \"maxwind_kph\": 16.2, \"totalprecip_mm\": 0.0, \"totalprecip_in\": 0.0, \"avgvis_km\": 20.0, \"avgvis_miles\": 12.0, \"avghumidity\": 60.0, \"condition\": { \"text\": \"Partly cloudy\", \"icon\": \"//cdn.apixu.com/weather/64x64/day/116.png\", \"code\": 1003 }, \"uv\": 5.0 }, \"astro\": { \"sunrise\": \"06:29 AM\", \"sunset\": \"08:34 PM\", \"moonrise\": \"07:06 AM\", \"moonset\": \"09:13 PM\" } } ] } }\n",
                HttpStatus.ACCEPTED
            )
        );

        Forecast forecast = api_service.forecast(latitude, longitude);

        CurrentWeather current_weather = forecast.getCurrent_weather();
        assertEquals(82, current_weather.get_humidity(), 0);
        assertEquals("http://cdn.apixu.com/weather/64x64/day/116.png", current_weather.get_icon());
        assertEquals(101700, current_weather.get_pressure(), 0);
        assertEquals("Partly cloudy", current_weather.get_summary());
        assertEquals(5, current_weather.get_uv(), 0);
        assertEquals(6.694, current_weather.get_wind_speed(), 0.001);

        List<DailyForecast> daily_forecasts = forecast.getDaily_forecast();
        assertEquals(7, daily_forecasts.size());

        DailyForecast daily_forecast = daily_forecasts.get(0);
        assertEquals("Partly cloudy", daily_forecast.get_summary());
        assertEquals(71, daily_forecast.get_humidity(), 0);
        assertEquals("http://cdn.apixu.com/weather/64x64/day/116.png", daily_forecast.get_icon());
        assertEquals(20.1, daily_forecast.get_max_temperature(), 0);
        assertEquals(12.1, daily_forecast.get_min_temperature(), 0);
        assertEquals(8.6, daily_forecast.get_uv(), 0);
    }
}