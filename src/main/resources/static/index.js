let search_table_tbody = $($("#search_table").children()[1]);

let empty_forecast_divs = function () {
    $("#current_weather").empty();
    $("#daily_forecast").empty();
};

$("#search_btn").click(function () {
    $.get(
        "http://localhost:8080/api/search?location=" + $("#location_input").val(),
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
        alert("Error consulting the api!");
        console.log(error);
    });
});

$("#clear_btn").click(function () {
    $("#search_display").hide();
});

$("body").on("click", "#search_go", function () {
    let ind = $(this).val();

    let latitude = $(search_table_tbody.children()[ind].children[2]).text();
    let longitude = $(search_table_tbody.children()[ind].children[3]).text();

    let days_count = $("#days_count_select").val();

    $.get(
        "http://localhost:8080/api/forecast?latitude=" +
        latitude + "&longitude=" +
        longitude + "&days_count=" +
        days_count,
        function (data) {
            empty_forecast_divs();

            let card = '<div class="card text-center">' +
                            '<h2>Current</h2>';
            if (data.current_weather._icon != null)
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
            $("#current_weather").append(card);

            let daily_forecasts = data.daily_forecast;
            for (let i = 0; i < daily_forecasts.length; i++) {
                let daily_forecast = daily_forecasts[i];

                card = '<div class="card" style="text-align:center; margin: 5px">';
                if (i == 0)
                    card += '<h2>Today</h2>'
                else if (i == 1)
                    card += '<h2>Tomorrow</h2>'
                else {
                    let today = new Date();
                    today.setDate(today.getDate() + i)
                    let day = today.getDate();
                    let month = today.getMonth();
                    let year = today.getFullYear();
                    card += '<h2>' + day + '/' + month + '/' + year + '</h2>'
                }
                if (daily_forecast._icon != null)
                    card += '<img class="card-img-top" style="width:120px; height:120px; margin-left:auto; margin-right:auto" src="' + daily_forecast._icon + '">';

                card += '<div class="card-body">' +
                            '<h5 class="card-title">' + daily_forecast._summary + '</h5>' +
                            '<p><i class="fas fa-tint"></i> ' + daily_forecast._humidity + ' %</p>' +
                            '<p><i class="fas fa-thermometer-full"></i> ' + daily_forecast._max_temperature + ' °C</p>' +
                            '<p><i class="fas fa-thermometer-empty"></i> ' + daily_forecast._min_temperature + ' °C</p>' +
                            '<p><i class="fas fa-sun"></i> ' + data.current_weather._uv + ' uv</p>' +
                        '</div>'+
                    '</div>';

                $("#daily_forecast").append(card);
            }



            $("#forecast_display").show();
            $("#search_display").hide();
            search_table_tbody.empty();
        }
        /*
         *

         */
    ).fail(function (error) {
        alert("Error consulting the api!");
        console.log(error);
    });
});
