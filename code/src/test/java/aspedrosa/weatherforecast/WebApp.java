package aspedrosa.weatherforecast;

import aspedrosa.weatherforecast.controllers.WeatherController;
import aspedrosa.weatherforecast.domain.CurrentWeather;
import aspedrosa.weatherforecast.domain.DailyForecast;
import aspedrosa.weatherforecast.domain.Forecast;
import aspedrosa.weatherforecast.domain.SearchResult;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Test all flows on the website
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class WebApp {

    @LocalServerPort
    int port;

    static WebDriver driver;

    @MockBean
    WeatherController weather_controller;

    ResponseEntity<List<SearchResult>> search_response;
    CurrentWeather current_weather;

    @BeforeClass
    public static void setUp() throws Exception {
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    public WebApp() {
        List<SearchResult> search_results = new ArrayList<>();
        search_results.add(new SearchResult("1", 1, 1));
        search_results.add(new SearchResult("2", 2, 2));
        search_results.add(new SearchResult("3", 3, 3));
        search_results.add(new SearchResult("4", 4, 4));
        search_results.add(new SearchResult("5", 5, 5));
        search_response = new ResponseEntity<>(
            search_results,
            HttpStatus.ACCEPTED
        );

        current_weather = new CurrentWeather("0", 0, 0, 0, 0, 0, "http://cdn.apixu.com/weather/64x64/day/116.png");
    }

    /**
     * Because different tests consider different number of days
     *  this method generated the response for the api accordingly to the
     *  days count assumed
     *
     * @param days_count number of days of forecast data to retrive
     * @return HTTP response from the api
     */
    private ResponseEntity<Forecast> generate_results(int days_count) {

        List<DailyForecast> daily_forecasts = new ArrayList<DailyForecast>();
        for (int i = 1; i <= days_count; i++)
            daily_forecasts.add(new DailyForecast(i + "", i, i, i, i, "http://cdn.apixu.com/weather/64x64/day/116.png"));

        return new ResponseEntity<>(
            new Forecast(daily_forecasts, current_weather),
            HttpStatus.ACCEPTED
        );

    }

    /**
     * Test the "I'm felling lucky feature" where forecast data
     *  for the first result of the search query should be used
     *  without asking to choose a location
     * Also test that without choosing any days count it should
     *  consider three (3) as default
     */
    @Test
    public void feel_lucky() {

        Mockito.when(weather_controller.forecast(1, 1, 3)).thenReturn(generate_results(3));
        Mockito.when(weather_controller.search("number")).thenReturn(search_response);

        driver.get("http://localhost:" + port);
        driver.findElement(By.id("location_input")).click();
        driver.findElement(By.id("location_input")).clear();
        driver.findElement(By.id("location_input")).sendKeys("number");
        driver.findElement(By.id("search_btn_lucky")).click();

        new WebDriverWait(driver, 3).until(ExpectedConditions.visibilityOf(driver.findElement(By.id("forecast"))));

        assertEquals("1", driver.findElement(By.id("location_name")).getText());
        assertEquals(3 + 1, driver.findElements(By.id("card")).size());

        Mockito.verify(weather_controller, Mockito.times(1)).forecast(1, 1, 3);
        Mockito.verify(weather_controller, Mockito.times(1)).search("number");
    }

    /**
     * Test if whenever the user chooses a different days count other
     *  than the default, that number of days of forecast data is displayed
     *  to the user
     * Also it is tested after the user chooses one of the search result that
     *  the forecast data related to that location is displays/requested
     */
    @Test
    public void simple_workflow() {
        int forecast_data_count = 4;

        Mockito
            .when(weather_controller.forecast(forecast_data_count, forecast_data_count, forecast_data_count))
            .thenReturn(generate_results(forecast_data_count));
        Mockito.when(weather_controller.search("number")).thenReturn(search_response);

        driver.get("http://localhost:" + port);
        driver.findElement(By.id("location_input")).click();
        driver.findElement(By.id("location_input")).clear();
        driver.findElement(By.id("location_input")).sendKeys("number");
        driver.findElement(By.id("search_btn")).click();
        driver.findElement(By.id("days_count_select")).click();
        new Select(driver.findElement(By.id("days_count_select"))).selectByVisibleText(forecast_data_count + "");
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Days count'])[1]/following::option[4]")).click();
        driver.findElement(By.xpath("//table[@id='search_table']/tbody/tr[4]/td/button")).click();

        new WebDriverWait(driver, 3).until(ExpectedConditions.visibilityOf(driver.findElement(By.id("forecast"))));

        assertEquals(forecast_data_count + 1, driver.findElements(By.id("card")).size());
        assertEquals(forecast_data_count + "", driver.findElement(By.id("location_name")).getText());

        Mockito.verify(weather_controller, Mockito.times(1)).forecast(forecast_data_count,
                                                                                              forecast_data_count,
                                                                                              forecast_data_count);
        Mockito.verify(weather_controller, Mockito.times(1)).search("number");
    }

    /**
     * When the clients searches for a strange/unknown location
     *  an alert should appear telling that there is no search results
     *  for that location
     */
    @Test
    public void alert_on_unknown_location() {
        Mockito
            .when(weather_controller.search("aaskldjhlfrg"))
            .thenReturn(new ResponseEntity<>(
                Collections.emptyList(),
                HttpStatus.NOT_FOUND
            ));

        driver.get("http://localhost:8080/");
        driver.findElement(By.id("location_input")).click();
        driver.findElement(By.id("location_input")).clear();
        driver.findElement(By.id("location_input")).sendKeys("aaskldjhlfrg");
        driver.findElement(By.xpath("//button[@id='search_btn']/i")).click();

        new WebDriverWait(driver, 3).until(ExpectedConditions.alertIsPresent());

        Alert alert = driver.switchTo().alert();

        assertEquals("No search results for the given location!", alert.getText());
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
    }
}
