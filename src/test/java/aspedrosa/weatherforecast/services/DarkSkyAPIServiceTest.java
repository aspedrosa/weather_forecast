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
 * Test the service dedicated to the dark sky api
 */
@RunWith(SpringRunner.class)
public class DarkSkyAPIServiceTest {

    @TestConfiguration
    static class DarkSkyAPIServiceTestContextConfiguration {
        @Bean
        public DarkSkyAPIService search_api_service() {
            return new DarkSkyAPIService();
        }
    }

    @Autowired
    DarkSkyAPIService api_service;

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
                "{\"latitude\":40.64134,\"longitude\":-8.653667,\"timezone\":\"Europe/Lisbon\",\"currently\":{\"time\":1556559184,\"summary\":\"Partly Cloudy\",\"icon\":\"partly-cloudy-day\",\"precipIntensity\":0,\"precipProbability\":0,\"temperature\":14.86,\"apparentTemperature\":14.86,\"dewPoint\":12.8,\"humidity\":0.87,\"pressure\":1017.16,\"windSpeed\":6.27,\"windGust\":8.26,\"windBearing\":347,\"cloudCover\":0.41,\"uvIndex\":1,\"visibility\":10.81,\"ozone\":306.56},\"daily\":{\"summary\":\"No precipitation throughout the week, with high temperatures rising to 27°C next Monday.\",\"icon\":\"clear-day\",\"data\":[{\"time\":1556492400,\"summary\":\"Foggy in the morning.\",\"icon\":\"fog\",\"sunriseTime\":1556516244,\"sunsetTime\":1556566136,\"moonPhase\":0.83,\"precipIntensity\":0.0025,\"precipIntensityMax\":0.0076,\"precipIntensityMaxTime\":1556550000,\"precipProbability\":0.01,\"precipType\":\"rain\",\"temperatureHigh\":17.02,\"temperatureHighTime\":1556546400,\"temperatureLow\":10.91,\"temperatureLowTime\":1556600400,\"apparentTemperatureHigh\":17.02,\"apparentTemperatureHighTime\":1556546400,\"apparentTemperatureLow\":10.91,\"apparentTemperatureLowTime\":1556600400,\"dewPoint\":12.47,\"humidity\":0.91,\"pressure\":1018.07,\"windSpeed\":3.29,\"windGust\":8.3,\"windGustTime\":1556560800,\"windBearing\":345,\"cloudCover\":0.57,\"uvIndex\":5,\"uvIndexTime\":1556535600,\"visibility\":9.83,\"ozone\":310.67,\"temperatureMin\":11.25,\"temperatureMinTime\":1556506800,\"temperatureMax\":17.02,\"temperatureMaxTime\":1556546400,\"apparentTemperatureMin\":11.25,\"apparentTemperatureMinTime\":1556506800,\"apparentTemperatureMax\":17.02,\"apparentTemperatureMaxTime\":1556546400},{\"time\":1556578800,\"summary\":\"Partly cloudy starting in the evening.\",\"icon\":\"partly-cloudy-night\",\"sunriseTime\":1556602566,\"sunsetTime\":1556652599,\"moonPhase\":0.86,\"precipIntensity\":0,\"precipIntensityMax\":0.0051,\"precipIntensityMaxTime\":1556582400,\"precipProbability\":0,\"temperatureHigh\":20.8,\"temperatureHighTime\":1556632800,\"temperatureLow\":11.85,\"temperatureLowTime\":1556686800,\"apparentTemperatureHigh\":20.8,\"apparentTemperatureHighTime\":1556632800,\"apparentTemperatureLow\":11.85,\"apparentTemperatureLowTime\":1556686800,\"dewPoint\":11.01,\"humidity\":0.76,\"pressure\":1016.7,\"windSpeed\":4.15,\"windGust\":8.03,\"windGustTime\":1556643600,\"windBearing\":341,\"cloudCover\":0.11,\"uvIndex\":8,\"uvIndexTime\":1556625600,\"visibility\":16.09,\"ozone\":328.3,\"temperatureMin\":10.91,\"temperatureMinTime\":1556600400,\"temperatureMax\":20.8,\"temperatureMaxTime\":1556632800,\"apparentTemperatureMin\":10.91,\"apparentTemperatureMinTime\":1556600400,\"apparentTemperatureMax\":20.8,\"apparentTemperatureMaxTime\":1556632800},{\"time\":1556665200,\"summary\":\"Clear throughout the day.\",\"icon\":\"clear-day\",\"sunriseTime\":1556688890,\"sunsetTime\":1556739063,\"moonPhase\":0.89,\"precipIntensity\":0.0025,\"precipIntensityMax\":0.0102,\"precipIntensityMaxTime\":1556690400,\"precipProbability\":0.02,\"precipType\":\"rain\",\"temperatureHigh\":22.38,\"temperatureHighTime\":1556719200,\"temperatureLow\":13.39,\"temperatureLowTime\":1556773200,\"apparentTemperatureHigh\":22.38,\"apparentTemperatureHighTime\":1556719200,\"apparentTemperatureLow\":13.39,\"apparentTemperatureLowTime\":1556773200,\"dewPoint\":11.64,\"humidity\":0.72,\"pressure\":1012.32,\"windSpeed\":3.78,\"windGust\":8.32,\"windGustTime\":1556730000,\"windBearing\":340,\"cloudCover\":0.06,\"uvIndex\":8,\"uvIndexTime\":1556712000,\"visibility\":16.09,\"ozone\":338.97,\"temperatureMin\":11.85,\"temperatureMinTime\":1556686800,\"temperatureMax\":22.38,\"temperatureMaxTime\":1556719200,\"apparentTemperatureMin\":11.85,\"apparentTemperatureMinTime\":1556686800,\"apparentTemperatureMax\":22.38,\"apparentTemperatureMaxTime\":1556719200},{\"time\":1556751600,\"summary\":\"Clear throughout the day.\",\"icon\":\"clear-day\",\"sunriseTime\":1556775214,\"sunsetTime\":1556825526,\"moonPhase\":0.92,\"precipIntensity\":0,\"precipIntensityMax\":0,\"precipProbability\":0,\"temperatureHigh\":25.73,\"temperatureHighTime\":1556802000,\"temperatureLow\":14.64,\"temperatureLowTime\":1556859600,\"apparentTemperatureHigh\":25.73,\"apparentTemperatureHighTime\":1556802000,\"apparentTemperatureLow\":14.64,\"apparentTemperatureLowTime\":1556859600,\"dewPoint\":11.4,\"humidity\":0.63,\"pressure\":1010.26,\"windSpeed\":2.83,\"windGust\":8,\"windGustTime\":1556820000,\"windBearing\":5,\"cloudCover\":0.01,\"uvIndex\":8,\"uvIndexTime\":1556798400,\"visibility\":16.09,\"ozone\":336.11,\"temperatureMin\":13.39,\"temperatureMinTime\":1556773200,\"temperatureMax\":25.73,\"temperatureMaxTime\":1556802000,\"apparentTemperatureMin\":13.39,\"apparentTemperatureMinTime\":1556773200,\"apparentTemperatureMax\":25.73,\"apparentTemperatureMaxTime\":1556802000},{\"time\":1556838000,\"summary\":\"Clear throughout the day.\",\"icon\":\"clear-day\",\"sunriseTime\":1556861540,\"sunsetTime\":1556911989,\"moonPhase\":0.95,\"precipIntensity\":0,\"precipIntensityMax\":0.0025,\"precipIntensityMaxTime\":1556874000,\"precipProbability\":0,\"temperatureHigh\":25.27,\"temperatureHighTime\":1556892000,\"temperatureLow\":13.38,\"temperatureLowTime\":1556946000,\"apparentTemperatureHigh\":25.27,\"apparentTemperatureHighTime\":1556892000,\"apparentTemperatureLow\":13.38,\"apparentTemperatureLowTime\":1556946000,\"dewPoint\":10.66,\"humidity\":0.57,\"pressure\":1010.44,\"windSpeed\":2.74,\"windGust\":11.56,\"windGustTime\":1556910000,\"windBearing\":14,\"cloudCover\":0,\"uvIndex\":8,\"uvIndexTime\":1556884800,\"visibility\":16.09,\"ozone\":339.62,\"temperatureMin\":14.64,\"temperatureMinTime\":1556859600,\"temperatureMax\":25.27,\"temperatureMaxTime\":1556892000,\"apparentTemperatureMin\":14.64,\"apparentTemperatureMinTime\":1556859600,\"apparentTemperatureMax\":25.27,\"apparentTemperatureMaxTime\":1556892000},{\"time\":1556924400,\"summary\":\"Clear throughout the day.\",\"icon\":\"clear-day\",\"sunriseTime\":1556947868,\"sunsetTime\":1556998451,\"moonPhase\":0.98,\"precipIntensity\":0,\"precipIntensityMax\":0,\"precipProbability\":0,\"temperatureHigh\":24.33,\"temperatureHighTime\":1556974800,\"temperatureLow\":13.23,\"temperatureLowTime\":1557032400,\"apparentTemperatureHigh\":24.33,\"apparentTemperatureHighTime\":1556974800,\"apparentTemperatureLow\":13.23,\"apparentTemperatureLowTime\":1557032400,\"dewPoint\":10.43,\"humidity\":0.6,\"pressure\":1014.36,\"windSpeed\":1.97,\"windGust\":9.07,\"windGustTime\":1556924400,\"windBearing\":348,\"cloudCover\":0.02,\"uvIndex\":8,\"uvIndexTime\":1556971200,\"visibility\":16.09,\"ozone\":336.06,\"temperatureMin\":13.38,\"temperatureMinTime\":1556946000,\"temperatureMax\":24.33,\"temperatureMaxTime\":1556974800,\"apparentTemperatureMin\":13.38,\"apparentTemperatureMinTime\":1556946000,\"apparentTemperatureMax\":24.33,\"apparentTemperatureMaxTime\":1556974800},{\"time\":1557010800,\"summary\":\"Clear throughout the day.\",\"icon\":\"clear-day\",\"sunriseTime\":1557034196,\"sunsetTime\":1557084914,\"moonPhase\":0.02,\"precipIntensity\":0,\"precipIntensityMax\":0,\"precipProbability\":0,\"temperatureHigh\":25.28,\"temperatureHighTime\":1557061200,\"temperatureLow\":14.2,\"temperatureLowTime\":1557118800,\"apparentTemperatureHigh\":25.28,\"apparentTemperatureHighTime\":1557061200,\"apparentTemperatureLow\":14.2,\"apparentTemperatureLowTime\":1557118800,\"dewPoint\":10.84,\"humidity\":0.61,\"pressure\":1017.92,\"windSpeed\":1.67,\"windGust\":6.29,\"windGustTime\":1557079200,\"windBearing\":347,\"cloudCover\":0.08,\"uvIndex\":8,\"uvIndexTime\":1557057600,\"visibility\":16.09,\"ozone\":330.98,\"temperatureMin\":13.23,\"temperatureMinTime\":1557032400,\"temperatureMax\":25.28,\"temperatureMaxTime\":1557061200,\"apparentTemperatureMin\":13.23,\"apparentTemperatureMinTime\":1557032400,\"apparentTemperatureMax\":25.28,\"apparentTemperatureMaxTime\":1557061200},{\"time\":1557097200,\"summary\":\"Partly cloudy until afternoon.\",\"icon\":\"partly-cloudy-day\",\"sunriseTime\":1557120526,\"sunsetTime\":1557171377,\"moonPhase\":0.05,\"precipIntensity\":0.0025,\"precipIntensityMax\":0.0051,\"precipIntensityMaxTime\":1557111600,\"precipProbability\":0.01,\"precipType\":\"rain\",\"temperatureHigh\":26.55,\"temperatureHighTime\":1557147600,\"temperatureLow\":14.54,\"temperatureLowTime\":1557205200,\"apparentTemperatureHigh\":26.55,\"apparentTemperatureHighTime\":1557147600,\"apparentTemperatureLow\":14.54,\"apparentTemperatureLowTime\":1557205200,\"dewPoint\":10.78,\"humidity\":0.57,\"pressure\":1019.95,\"windSpeed\":1.33,\"windGust\":4.29,\"windGustTime\":1557154800,\"windBearing\":354,\"cloudCover\":0.15,\"uvIndex\":7,\"uvIndexTime\":1557147600,\"visibility\":16.09,\"ozone\":331.93,\"temperatureMin\":14.2,\"temperatureMinTime\":1557118800,\"temperatureMax\":26.55,\"temperatureMaxTime\":1557147600,\"apparentTemperatureMin\":14.2,\"apparentTemperatureMinTime\":1557118800,\"apparentTemperatureMax\":26.55,\"apparentTemperatureMaxTime\":1557147600}]},\"offset\":1}",
                HttpStatus.ACCEPTED
            )
        );

        Forecast forecast = api_service.forecast(latitude, longitude);

        CurrentWeather current_weather = forecast.getCurrent_weather();
        assertEquals(87, current_weather.get_humidity(), 0);
        assertNull(current_weather.get_icon());
        assertEquals(101716, current_weather.get_pressure(), 0);
        assertEquals("Partly Cloudy", current_weather.get_summary());
        assertEquals(1, current_weather.get_uv(), 0);
        assertEquals(6.27, current_weather.get_wind_speed(), 0);

        List<DailyForecast> daily_forecasts = forecast.getDaily_forecast();
        assertEquals(8, daily_forecasts.size());

        DailyForecast daily_forecast = daily_forecasts.get(0);
        assertEquals("Foggy in the morning.", daily_forecast.get_summary());
        assertEquals(91, daily_forecast.get_humidity(), 0);
        assertNull(daily_forecast.get_icon());
        assertEquals(17.02, daily_forecast.get_max_temperature(), 0);
        assertEquals(11.25, daily_forecast.get_min_temperature(), 0);
        assertEquals(5, daily_forecast.get_uv(), 0);
    }
}