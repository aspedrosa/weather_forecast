package aspedrosa.weatherforecast.services;

import aspedrosa.weatherforecast.domain.CurrentWeather;
import aspedrosa.weatherforecast.domain.DailyForecast;
import aspedrosa.weatherforecast.domain.Forecast;
import aspedrosa.weatherforecast.repositories.CurrentWeatherCache;
import aspedrosa.weatherforecast.repositories.DailyForecastCache;
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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Test the service and repository layer related
 *  to forecast queries
 * Because the two tests on this class use the same ForecastService,
 *  querying to the same set of coordinates would lead to cached values
 *  on the second test, but the test are assuming that he cache is empty.
 *  For that a constant is added to the latitude value
 */
@RunWith(SpringRunner.class)
public class ForecastServiceIT {

    @TestConfiguration
    static class ForecastServiceITContextConfiguration {
        @Bean
        public ForecastService forecast_service() {
            return new ForecastService();
        }

        @Bean
        public DarkSkyAPIService backup_api() {
            return new DarkSkyAPIService();
        }

        @Bean
        public ApixuAPIService primary_api() {
            return new ApixuAPIService();
        }

        @Bean
        public DailyForecastCache daily_cache() {
            return new DailyForecastCache();
        }

        @Bean
        public CurrentWeatherCache current_cache() {
            return new CurrentWeatherCache();
        }
    }

    @MockBean
    RestTemplate rest_template;

    @Autowired
    ForecastService forecast_service;

    @Autowired
    DarkSkyAPIService backup_api;

    @Autowired
    ApixuAPIService primary_api;

    final double LATITUDE = 40.641340;
    final double LONGITUDE = -8.653667;

    /**
     * Test a regular forecast query
     */
    @Test
    public void forecast_primary() {
        String url = primary_api.build_api_url(new Object[] {LATITUDE+1, LONGITUDE});

        Mockito.when(
            rest_template.getForEntity(
                url,
                String.class
            )
        ).thenReturn(
            new ResponseEntity<>(
                "{ \"location\": { \"name\": \"Aveiro\", \"region\": \"Aveiro\", \"country\": \"Portugal\", \"lat\": 40.64, \"lon\": -8.65, \"tz_id\": \"Europe/Lisbon\", \"localtime_epoch\": 1556560076, \"localtime\": \"2019-04-29 18:47\" }, \"current\": { \"last_updated_epoch\": 1556559915, \"last_updated\": \"2019-04-29 18:45\", \"temp_c\": 16.0, \"temp_f\": 60.8, \"is_day\": 1, \"condition\": { \"text\": \"Partly cloudy\", \"icon\": \"//cdn.apixu.com/weather/64x64/day/116.png\", \"code\": 1003 }, \"wind_mph\": 15.0, \"wind_kph\": 24.1, \"wind_degree\": 360, \"wind_dir\": \"N\", \"pressure_mb\": 1017.0, \"pressure_in\": 30.5, \"precip_mm\": 0.0, \"precip_in\": 0.0, \"humidity\": 82, \"cloud\": 50, \"feelslike_c\": 16.0, \"feelslike_f\": 60.8, \"vis_km\": 10.0, \"vis_miles\": 6.0, \"uv\": 5.0, \"gust_mph\": 16.1, \"gust_kph\": 25.9 }, \"forecast\": { \"forecastday\": [ { \"date\": \"2019-04-29\", \"date_epoch\": 1556496000, \"day\": { \"maxtemp_c\": 20.1, \"maxtemp_f\": 68.2, \"mintemp_c\": 12.1, \"mintemp_f\": 53.8, \"avgtemp_c\": 16.3, \"avgtemp_f\": 61.3, \"maxwind_mph\": 12.3, \"maxwind_kph\": 19.8, \"totalprecip_mm\": 0.0, \"totalprecip_in\": 0.0, \"avgvis_km\": 18.8, \"avgvis_miles\": 11.0, \"avghumidity\": 71.0, \"condition\": { \"text\": \"Partly cloudy\", \"icon\": \"//cdn.apixu.com/weather/64x64/day/116.png\", \"code\": 1003 }, \"uv\": 8.6 }, \"astro\": { \"sunrise\": \"06:36 AM\", \"sunset\": \"08:28 PM\", \"moonrise\": \"04:19 AM\", \"moonset\": \"03:06 PM\" } }, { \"date\": \"2019-04-30\", \"date_epoch\": 1556582400, \"day\": { \"maxtemp_c\": 19.3, \"maxtemp_f\": 66.7, \"mintemp_c\": 11.1, \"mintemp_f\": 52.0, \"avgtemp_c\": 14.8, \"avgtemp_f\": 58.7, \"maxwind_mph\": 12.1, \"maxwind_kph\": 19.4, \"totalprecip_mm\": 0.0, \"totalprecip_in\": 0.0, \"avgvis_km\": 19.5, \"avgvis_miles\": 12.0, \"avghumidity\": 78.0, \"condition\": { \"text\": \"Partly cloudy\", \"icon\": \"//cdn.apixu.com/weather/64x64/day/116.png\", \"code\": 1003 }, \"uv\": 8.0 }, \"astro\": { \"sunrise\": \"06:35 AM\", \"sunset\": \"08:29 PM\", \"moonrise\": \"04:48 AM\", \"moonset\": \"04:05 PM\" } }, { \"date\": \"2019-05-01\", \"date_epoch\": 1556668800, \"day\": { \"maxtemp_c\": 20.5, \"maxtemp_f\": 68.9, \"mintemp_c\": 12.6, \"mintemp_f\": 54.7, \"avgtemp_c\": 16.4, \"avgtemp_f\": 61.6, \"maxwind_mph\": 12.8, \"maxwind_kph\": 20.5, \"totalprecip_mm\": 0.0, \"totalprecip_in\": 0.0, \"avgvis_km\": 18.8, \"avgvis_miles\": 11.0, \"avghumidity\": 75.0, \"condition\": { \"text\": \"Partly cloudy\", \"icon\": \"//cdn.apixu.com/weather/64x64/day/116.png\", \"code\": 1003 }, \"uv\": 7.9 }, \"astro\": { \"sunrise\": \"06:34 AM\", \"sunset\": \"08:30 PM\", \"moonrise\": \"05:15 AM\", \"moonset\": \"05:04 PM\" } }, { \"date\": \"2019-05-02\", \"date_epoch\": 1556755200, \"day\": { \"maxtemp_c\": 25.2, \"maxtemp_f\": 77.4, \"mintemp_c\": 15.0, \"mintemp_f\": 59.0, \"avgtemp_c\": 19.0, \"avgtemp_f\": 66.1, \"maxwind_mph\": 11.6, \"maxwind_kph\": 18.7, \"totalprecip_mm\": 0.0, \"totalprecip_in\": 0.0, \"avgvis_km\": 19.9, \"avgvis_miles\": 12.0, \"avghumidity\": 66.0, \"condition\": { \"text\": \"Partly cloudy\", \"icon\": \"//cdn.apixu.com/weather/64x64/day/116.png\", \"code\": 1003 }, \"uv\": 8.0 }, \"astro\": { \"sunrise\": \"06:32 AM\", \"sunset\": \"08:31 PM\", \"moonrise\": \"05:41 AM\", \"moonset\": \"06:03 PM\" } }, { \"date\": \"2019-05-03\", \"date_epoch\": 1556841600, \"day\": { \"maxtemp_c\": 22.9, \"maxtemp_f\": 73.2, \"mintemp_c\": 15.4, \"mintemp_f\": 59.7, \"avgtemp_c\": 18.9, \"avgtemp_f\": 66.1, \"maxwind_mph\": 13.9, \"maxwind_kph\": 22.3, \"totalprecip_mm\": 0.0, \"totalprecip_in\": 0.0, \"avgvis_km\": 20.0, \"avgvis_miles\": 12.0, \"avghumidity\": 61.0, \"condition\": { \"text\": \"Partly cloudy\", \"icon\": \"//cdn.apixu.com/weather/64x64/day/116.png\", \"code\": 1003 }, \"uv\": 7.9 }, \"astro\": { \"sunrise\": \"06:31 AM\", \"sunset\": \"08:32 PM\", \"moonrise\": \"06:07 AM\", \"moonset\": \"07:05 PM\" } }, { \"date\": \"2019-05-04\", \"date_epoch\": 1556928000, \"day\": { \"maxtemp_c\": 21.3, \"maxtemp_f\": 70.3, \"mintemp_c\": 13.5, \"mintemp_f\": 56.3, \"avgtemp_c\": 17.0, \"avgtemp_f\": 62.6, \"maxwind_mph\": 13.4, \"maxwind_kph\": 21.6, \"totalprecip_mm\": 0.0, \"totalprecip_in\": 0.0, \"avgvis_km\": 20.0, \"avgvis_miles\": 12.0, \"avghumidity\": 64.0, \"condition\": { \"text\": \"Partly cloudy\", \"icon\": \"//cdn.apixu.com/weather/64x64/day/116.png\", \"code\": 1003 }, \"uv\": 5.0 }, \"astro\": { \"sunrise\": \"06:30 AM\", \"sunset\": \"08:33 PM\", \"moonrise\": \"06:35 AM\", \"moonset\": \"08:08 PM\" } }, { \"date\": \"2019-05-05\", \"date_epoch\": 1557014400, \"day\": { \"maxtemp_c\": 25.3, \"maxtemp_f\": 77.5, \"mintemp_c\": 13.9, \"mintemp_f\": 57.0, \"avgtemp_c\": 18.3, \"avgtemp_f\": 64.9, \"maxwind_mph\": 10.1, \"maxwind_kph\": 16.2, \"totalprecip_mm\": 0.0, \"totalprecip_in\": 0.0, \"avgvis_km\": 20.0, \"avgvis_miles\": 12.0, \"avghumidity\": 60.0, \"condition\": { \"text\": \"Partly cloudy\", \"icon\": \"//cdn.apixu.com/weather/64x64/day/116.png\", \"code\": 1003 }, \"uv\": 5.0 }, \"astro\": { \"sunrise\": \"06:29 AM\", \"sunset\": \"08:34 PM\", \"moonrise\": \"07:06 AM\", \"moonset\": \"09:13 PM\" } } ] } }\n",
                HttpStatus.ACCEPTED
            )
        );

        forecast_service.forecast(LATITUDE+1, LONGITUDE, 2);
        Forecast forecast = forecast_service.forecast(LATITUDE+1, LONGITUDE, 4);

        Mockito.verify(rest_template, Mockito.times(1))
            .getForEntity(
                url,
                String.class
            );

        CurrentWeather current_weather = forecast.getCurrent_weather();
        assertEquals(82, current_weather.get_humidity(), 0);
        assertEquals("http://cdn.apixu.com/weather/64x64/day/116.png", current_weather.get_icon());
        assertEquals(101700, current_weather.get_pressure(), 0);
        assertEquals("Partly cloudy", current_weather.get_summary());
        assertEquals(5, current_weather.get_uv(), 0);
        assertEquals(6.694, current_weather.get_wind_speed(), 0.001);

        List<DailyForecast> daily_forecasts = forecast.getDaily_forecast();
        assertEquals(4, daily_forecasts.size());

        DailyForecast daily_forecast = daily_forecasts.get(0);
        assertEquals("Partly cloudy", daily_forecast.get_summary());
        assertEquals(71, daily_forecast.get_humidity(), 0);
        assertEquals("http://cdn.apixu.com/weather/64x64/day/116.png", daily_forecast.get_icon());
        assertEquals(20.1, daily_forecast.get_max_temperature(), 0);
        assertEquals(12.1, daily_forecast.get_min_temperature(), 0);
        assertEquals(8.6, daily_forecast.get_uv(), 0);
    }

    /**
     * Test a forecast query where the primary api
     *  fails
     */
    @Test
    public void forecast_backup() {
        String url_primary = primary_api.build_api_url(new Object[] {LATITUDE, LONGITUDE});
        String url_backup = backup_api.build_api_url(new Object[] {LATITUDE, LONGITUDE});

        Mockito.when(
            rest_template.getForEntity(
                url_primary,
                String.class
            )
        ).thenThrow(RestClientException.class);
        Mockito.when(
            rest_template.getForEntity(
                url_backup,
                String.class
            )
        ).thenReturn(
            new ResponseEntity<>(
                "{\"latitude\":40.64134,\"longitude\":-8.653667,\"timezone\":\"Europe/Lisbon\",\"currently\":{\"time\":1556559184,\"summary\":\"Partly Cloudy\",\"icon\":\"partly-cloudy-day\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":14.86,\"apparentTemperature\":14.86,\"dewPoint\":12.8,\"humidity\":0.87,\"pressure\":1017.16,\"windSpeed\":6.27,\"windGust\":8.26,\"windBearing\":347,\"cloudCover\":0.41,\"uvIndex\":1,\"visibility\":10.81,\"ozone\":306.56},\"daily\":{\"summary\":\"No precipitation throughout the week, with high temperatures rising to 27°C next Monday.\",\"icon\":\"clear-day\",\"data\":[{\"time\":1556492400,\"summary\":\"Foggy in the morning.\",\"icon\":\"fog\",\"sunriseTime\":1556516244,\"sunsetTime\":1556566136,\"moonPhase\":0.83,\"precipIntensity\":0.0025,\"precipIntensityMax\":0.0076,\"precipIntensityMaxTime\":1556550000,\"precipProbability\":0.01,\"precipType\":\"rain\",\"temperatureHigh\":17.02,\"temperatureHighTime\":1556546400,\"temperatureLow\":10.91,\"temperatureLowTime\":1556600400,\"apparentTemperatureHigh\":17.02,\"apparentTemperatureHighTime\":1556546400,\"apparentTemperatureLow\":10.91,\"apparentTemperatureLowTime\":1556600400,\"dewPoint\":12.47,\"humidity\":0.91,\"pressure\":1018.07,\"windSpeed\":3.29,\"windGust\":8.3,\"windGustTime\":1556560800,\"windBearing\":345,\"cloudCover\":0.57,\"uvIndex\":5,\"uvIndexTime\":1556535600,\"visibility\":9.83,\"ozone\":310.67,\"temperatureMin\":11.25,\"temperatureMinTime\":1556506800,\"temperatureMax\":17.02,\"temperatureMaxTime\":1556546400,\"apparentTemperatureMin\":11.25,\"apparentTemperatureMinTime\":1556506800,\"apparentTemperatureMax\":17.02,\"apparentTemperatureMaxTime\":1556546400},{\"time\":1556578800,\"summary\":\"Partly cloudy starting in the evening.\",\"icon\":\"partly-cloudy-night\",\"sunriseTime\":1556602566,\"sunsetTime\":1556652599,\"moonPhase\":0.86,\"precipIntensity\":0,\"precipIntensityMax\":0.0051,\"precipIntensityMaxTime\":1556582400,\"precipProbability\":0,\"temperatureHigh\":20.8,\"temperatureHighTime\":1556632800,\"temperatureLow\":11.85,\"temperatureLowTime\":1556686800,\"apparentTemperatureHigh\":20.8,\"apparentTemperatureHighTime\":1556632800,\"apparentTemperatureLow\":11.85,\"apparentTemperatureLowTime\":1556686800,\"dewPoint\":11.01,\"humidity\":0.76,\"pressure\":1016.7,\"windSpeed\":4.15,\"windGust\":8.03,\"windGustTime\":1556643600,\"windBearing\":341,\"cloudCover\":0.11,\"uvIndex\":8,\"uvIndexTime\":1556625600,\"visibility\":16.09,\"ozone\":328.3,\"temperatureMin\":10.91,\"temperatureMinTime\":1556600400,\"temperatureMax\":20.8,\"temperatureMaxTime\":1556632800,\"apparentTemperatureMin\":10.91,\"apparentTemperatureMinTime\":1556600400,\"apparentTemperatureMax\":20.8,\"apparentTemperatureMaxTime\":1556632800},{\"time\":1556665200,\"summary\":\"Clear throughout the day.\",\"icon\":\"clear-day\",\"sunriseTime\":1556688890,\"sunsetTime\":1556739063,\"moonPhase\":0.89,\"precipIntensity\":0.0025,\"precipIntensityMax\":0.0102,\"precipIntensityMaxTime\":1556690400,\"precipProbability\":0.02,\"precipType\":\"rain\",\"temperatureHigh\":22.38,\"temperatureHighTime\":1556719200,\"temperatureLow\":13.39,\"temperatureLowTime\":1556773200,\"apparentTemperatureHigh\":22.38,\"apparentTemperatureHighTime\":1556719200,\"apparentTemperatureLow\":13.39,\"apparentTemperatureLowTime\":1556773200,\"dewPoint\":11.64,\"humidity\":0.72,\"pressure\":1012.32,\"windSpeed\":3.78,\"windGust\":8.32,\"windGustTime\":1556730000,\"windBearing\":340,\"cloudCover\":0.06,\"uvIndex\":8,\"uvIndexTime\":1556712000,\"visibility\":16.09,\"ozone\":338.97,\"temperatureMin\":11.85,\"temperatureMinTime\":1556686800,\"temperatureMax\":22.38,\"temperatureMaxTime\":1556719200,\"apparentTemperatureMin\":11.85,\"apparentTemperatureMinTime\":1556686800,\"apparentTemperatureMax\":22.38,\"apparentTemperatureMaxTime\":1556719200},{\"time\":1556751600,\"summary\":\"Clear throughout the day.\",\"icon\":\"clear-day\",\"sunriseTime\":1556775214,\"sunsetTime\":1556825526,\"moonPhase\":0.92,\"precipIntensity\":0,\"precipIntensityMax\":0,\"precipProbability\":0,\"temperatureHigh\":25.73,\"temperatureHighTime\":1556802000,\"temperatureLow\":14.64,\"temperatureLowTime\":1556859600,\"apparentTemperatureHigh\":25.73,\"apparentTemperatureHighTime\":1556802000,\"apparentTemperatureLow\":14.64,\"apparentTemperatureLowTime\":1556859600,\"dewPoint\":11.4,\"humidity\":0.63,\"pressure\":1010.26,\"windSpeed\":2.83,\"windGust\":8,\"windGustTime\":1556820000,\"windBearing\":5,\"cloudCover\":0.01,\"uvIndex\":8,\"uvIndexTime\":1556798400,\"visibility\":16.09,\"ozone\":336.11,\"temperatureMin\":13.39,\"temperatureMinTime\":1556773200,\"temperatureMax\":25.73,\"temperatureMaxTime\":1556802000,\"apparentTemperatureMin\":13.39,\"apparentTemperatureMinTime\":1556773200,\"apparentTemperatureMax\":25.73,\"apparentTemperatureMaxTime\":1556802000},{\"time\":1556838000,\"summary\":\"Clear throughout the day.\",\"icon\":\"clear-day\",\"sunriseTime\":1556861540,\"sunsetTime\":1556911989,\"moonPhase\":0.95,\"precipIntensity\":0,\"precipIntensityMax\":0.0025,\"precipIntensityMaxTime\":1556874000,\"precipProbability\":0,\"temperatureHigh\":25.27,\"temperatureHighTime\":1556892000,\"temperatureLow\":13.38,\"temperatureLowTime\":1556946000,\"apparentTemperatureHigh\":25.27,\"apparentTemperatureHighTime\":1556892000,\"apparentTemperatureLow\":13.38,\"apparentTemperatureLowTime\":1556946000,\"dewPoint\":10.66,\"humidity\":0.57,\"pressure\":1010.44,\"windSpeed\":2.74,\"windGust\":11.56,\"windGustTime\":1556910000,\"windBearing\":14,\"cloudCover\":0,\"uvIndex\":8,\"uvIndexTime\":1556884800,\"visibility\":16.09,\"ozone\":339.62,\"temperatureMin\":14.64,\"temperatureMinTime\":1556859600,\"temperatureMax\":25.27,\"temperatureMaxTime\":1556892000,\"apparentTemperatureMin\":14.64,\"apparentTemperatureMinTime\":1556859600,\"apparentTemperatureMax\":25.27,\"apparentTemperatureMaxTime\":1556892000},{\"time\":1556924400,\"summary\":\"Clear throughout the day.\",\"icon\":\"clear-day\",\"sunriseTime\":1556947868,\"sunsetTime\":1556998451,\"moonPhase\":0.98,\"precipIntensity\":0,\"precipIntensityMax\":0,\"precipProbability\":0,\"temperatureHigh\":24.33,\"temperatureHighTime\":1556974800,\"temperatureLow\":13.23,\"temperatureLowTime\":1557032400,\"apparentTemperatureHigh\":24.33,\"apparentTemperatureHighTime\":1556974800,\"apparentTemperatureLow\":13.23,\"apparentTemperatureLowTime\":1557032400,\"dewPoint\":10.43,\"humidity\":0.6,\"pressure\":1014.36,\"windSpeed\":1.97,\"windGust\":9.07,\"windGustTime\":1556924400,\"windBearing\":348,\"cloudCover\":0.02,\"uvIndex\":8,\"uvIndexTime\":1556971200,\"visibility\":16.09,\"ozone\":336.06,\"temperatureMin\":13.38,\"temperatureMinTime\":1556946000,\"temperatureMax\":24.33,\"temperatureMaxTime\":1556974800,\"apparentTemperatureMin\":13.38,\"apparentTemperatureMinTime\":1556946000,\"apparentTemperatureMax\":24.33,\"apparentTemperatureMaxTime\":1556974800},{\"time\":1557010800,\"summary\":\"Clear throughout the day.\",\"icon\":\"clear-day\",\"sunriseTime\":1557034196,\"sunsetTime\":1557084914,\"moonPhase\":0.02,\"precipIntensity\":0,\"precipIntensityMax\":0,\"precipProbability\":0,\"temperatureHigh\":25.28,\"temperatureHighTime\":1557061200,\"temperatureLow\":14.2,\"temperatureLowTime\":1557118800,\"apparentTemperatureHigh\":25.28,\"apparentTemperatureHighTime\":1557061200,\"apparentTemperatureLow\":14.2,\"apparentTemperatureLowTime\":1557118800,\"dewPoint\":10.84,\"humidity\":0.61,\"pressure\":1017.92,\"windSpeed\":1.67,\"windGust\":6.29,\"windGustTime\":1557079200,\"windBearing\":347,\"cloudCover\":0.08,\"uvIndex\":8,\"uvIndexTime\":1557057600,\"visibility\":16.09,\"ozone\":330.98,\"temperatureMin\":13.23,\"temperatureMinTime\":1557032400,\"temperatureMax\":25.28,\"temperatureMaxTime\":1557061200,\"apparentTemperatureMin\":13.23,\"apparentTemperatureMinTime\":1557032400,\"apparentTemperatureMax\":25.28,\"apparentTemperatureMaxTime\":1557061200},{\"time\":1557097200,\"summary\":\"Partly cloudy until afternoon.\",\"icon\":\"partly-cloudy-day\",\"sunriseTime\":1557120526,\"sunsetTime\":1557171377,\"moonPhase\":0.05,\"precipIntensity\":0.0025,\"precipIntensityMax\":0.0051,\"precipIntensityMaxTime\":1557111600,\"precipProbability\":0.01,\"precipType\":\"rain\",\"temperatureHigh\":26.55,\"temperatureHighTime\":1557147600,\"temperatureLow\":14.54,\"temperatureLowTime\":1557205200,\"apparentTemperatureHigh\":26.55,\"apparentTemperatureHighTime\":1557147600,\"apparentTemperatureLow\":14.54,\"apparentTemperatureLowTime\":1557205200,\"dewPoint\":10.78,\"humidity\":0.57,\"pressure\":1019.95,\"windSpeed\":1.33,\"windGust\":4.29,\"windGustTime\":1557154800,\"windBearing\":354,\"cloudCover\":0.15,\"uvIndex\":7,\"uvIndexTime\":1557147600,\"visibility\":16.09,\"ozone\":331.93,\"temperatureMin\":14.2,\"temperatureMinTime\":1557118800,\"temperatureMax\":26.55,\"temperatureMaxTime\":1557147600,\"apparentTemperatureMin\":14.2,\"apparentTemperatureMinTime\":1557118800,\"apparentTemperatureMax\":26.55,\"apparentTemperatureMaxTime\":1557147600}]},\"offset\":1}",
                HttpStatus.ACCEPTED
            )
        );

        Forecast forecast = forecast_service.forecast(LATITUDE, LONGITUDE, 2);
        forecast_service.forecast(LATITUDE, LONGITUDE, 4);

        Mockito.verify(rest_template, Mockito.times(1))
            .getForEntity(
                url_primary,
                String.class
            );
        Mockito.verify(rest_template, Mockito.times(1))
            .getForEntity(
                url_backup,
                String.class
            );

        CurrentWeather current_weather = forecast.getCurrent_weather();
        assertEquals(87, current_weather.get_humidity(), 0);
        assertNull(current_weather.get_icon());
        assertEquals(101716, current_weather.get_pressure(), 0);
        assertEquals("Partly Cloudy", current_weather.get_summary());
        assertEquals(1, current_weather.get_uv(), 0);
        assertEquals(6.27, current_weather.get_wind_speed(), 0);

        List<DailyForecast> daily_forecasts = forecast.getDaily_forecast();
        assertEquals(2, daily_forecasts.size());

        DailyForecast daily_forecast = daily_forecasts.get(0);
        assertEquals("Foggy in the morning.", daily_forecast.get_summary());
        assertEquals(91, daily_forecast.get_humidity(), 0);
        assertNull(daily_forecast.get_icon());
        assertEquals(17.02, daily_forecast.get_max_temperature(), 0);
        assertEquals(11.25, daily_forecast.get_min_temperature(), 0);
        assertEquals(5, daily_forecast.get_uv(), 0);
    }
}