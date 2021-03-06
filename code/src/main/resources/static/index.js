// jQuery object of the tbody of the search resutls table
let search_table_tbody = $($("#search_table").children()[1]);

/**
 * Trims and transforms into lower case the location
 *  inserted by the user
 *
 * @returns {string|null} null if the location has less
 *  then 3 characters
 */
let parse_location = function() {
    let location = $("#location_input").val();
    location = location.trim().toLowerCase();

    if (location.length < 3)
        return null;

    return location;
};

/**
 * Whenever the search button is clicked
 *  1. retrieve and parse the location inserted
 *  2. retrieve search data from the api
 *  3. clear the search table body
 *  4. build the search table
 *  5. display the search table
 */
$("#search_btn").click(function () {
    let location = parse_location();

    if (location === null) {
        alert("Invalid location!");
        return;
    }

    $.get(
        "http://localhost:8080/api/search?location=" + location,
        function (data) {
            search_table_tbody.empty();

            let html = "";
            for (let i = 0; i < data.length; i++) {
                let result = data[i];
                html += "<tr><td><button value='" + i + "' id='search_go' class='btn btn-success'>Go</button></td>";
                html += "<td>" + result._display_name + "</td>";
                html += "<td>" + result._latitude + "</td>";
                html += "<td>" + result._longitude + "</td></tr>";
            }
            $("#location_input").val("");
            search_table_tbody.append(html);

            $("#search_display").show();
        }
    ).fail(function (error) {
        alert("No search results for the given location!");
        console.log(error);
    });
});

/**
 * Hide the search table when the clear results button
 *  is clicked
 */
$("#clear_btn").click(function () {
    $("#search_display").hide();
});

/**
 * Abbreviation of the months of the year
 * @type {string[]}
 */
let monts = ["Jan", "Mar", "Fev", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];

/**
 * Display forecast data
 *
 * 1. get the number of days chosen on the select
 * 2. get forecast data from the api
 * 3. clear the forecast div
 * 4. build the html to display forecast data
 * 5. update the location name
 * 6. display forecast data
 * 7. hide search resutls
 *
 * @param location_name location chosen by the user from the search table
 * @param latitude from the search table
 * @param longitude from the search table
 */
let display_data = function(location_name, latitude, longitude) {
    let days_count = $("#days_count_select").val();

    $.get(
        "http://localhost:8080/api/forecast?latitude=" +
        latitude + "&longitude=" +
        longitude + "&days_count=" +
        days_count,
        function (data) {
            $("#forecast").empty();

            let card = '<div id="card" class="my_card">' +
                            '<h2>Current</h2>';
            if (data.current_weather._icon !== null)
                card += '<img class="card-img-top" style="width:120px; height:120px; margin-left:auto; margin-right: auto" src="' + data.current_weather._icon + '">';
            card += '<div class="card-body">' +
                        '<h5 class="card-title">' + data.current_weather._summary + '</h5>' +
                        '<p><i class="fas fa-tint"></i> ' + data.current_weather._humidity + ' %</p>' +
                        '<p><i class="fas fa-weight-hanging"></i> ' + data.current_weather._pressure + ' pascal</p>' +
                        '<p><i class="fas fa-thermometer-half"></i> ' + data.current_weather._temperature + ' °C</p>' +
                        '<p><i class="fas fa-sun"></i> ' + data.current_weather._uv + ' uv</p>' +
                        '<p><i class="fas fa-wind"></i> ' + data.current_weather._wind_speed.toFixed(3) + ' m/s</p>' +
                    '</div>'+
                '</div>';
            $("#forecast").append(card);

            let daily_forecasts = data.daily_forecast;
            for (let i = 0, today = new Date(), last_day = today.getDate();
                 i < daily_forecasts.length;
                 i++, today.setDate(today.getDate() + 1)) {
                let daily_forecast = daily_forecasts[i];

                card = '<div id="card" class="my_card" >';
                let day = today.getDate();
                if (Math.abs(day - last_day) !== 1)
                    today.setMonth(today.getMonth() + 1);
                last_day = day;
                let month = today.getMonth();

                card += '<h2>' + day + ' ' + monts[month - 1] + '</h2>';

                if (i === 0)
                    card += '<h2>Today</h2>';
                else if (i === 1)
                    card += '<h2>Tomorrow</h2>';
                if (daily_forecast._icon !== null)
                    card += '<img class="card-img-top card_img" src="' + daily_forecast._icon + '">';

                card += '<div class="card-body">' +
                            '<h5 class="card-title">' + daily_forecast._summary + '</h5>' +
                            '<p><i class="fas fa-tint"></i> ' + daily_forecast._humidity + ' %</p>' +
                            '<p><i class="fas fa-thermometer-full"></i> ' + daily_forecast._max_temperature + ' °C</p>' +
                            '<p><i class="fas fa-thermometer-empty"></i> ' + daily_forecast._min_temperature + ' °C</p>' +
                            '<p><i class="fas fa-sun"></i> ' + data.current_weather._uv + ' uv</p>' +
                        '</div>'+
                    '</div>';

                $("#forecast").append(card);
            }

            $("#location_name").html('<i class="fas fa-map-marker-alt"></i> ' + location_name);

            $("#forecast_display").show();
            $("#search_display").hide();
        }
    ).fail(function (error) {
        alert("Error consulting the api!");
        console.log(error);
    });
};

/**
 * Handle the click on one of the button present
 *  on the search results table
 *
 * 1. retrieve location name, latitude, longitude accordingly
 *      which button was pressed
 * 2. display data
 */
$("body").on("click", "#search_go", function () {
    let ind = $(this).val();

    let location_name = $(search_table_tbody.children()[ind].children[1]).text();
    let latitude = $(search_table_tbody.children()[ind].children[2]).text();
    let longitude = $(search_table_tbody.children()[ind].children[3]).text();

    display_data(location_name, latitude, longitude);
});

/**
 * "I'm Felling Lucky" feature
 *
 * 1. parses the location inserted
 * 2. retrieve data from the search api
 * 3. display forecast data of the first
 *      result of thesearch request
 */
$("#search_btn_lucky").click(function () {
    let location = parse_location();

    if (location === null) {
        alert("Invalid location!");
        return;
    }

   $.get(
        "http://localhost:8080/api/search?location=" + location,
        function (data) {
            let result = data[0];

            display_data(result._display_name,
                         result._latitude,
                         result._longitude);

            $("#location_input").val("");
        }
    ).fail(function (error) {
        alert("No search results for the given location!");
        console.log(error);
    });
});
