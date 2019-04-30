$(document).ready(function () {
  $("#clear_btn").hide();
});

$("#search_btn").click(function () {
  $.get(
    "http://localhost:8080/api/search?location=" + $("#location_input").val(),
    function (data) {
      $("#clear_btn").show();
      let html = "<table class=\"table\"><tr><th>Name</th><th>Latitude</th><th>Longitude<th></tr>";
      for (let i = 0; i < data.length; i++) {
        let result = data[i];
        html += "<tr><td>" + result._display_name + "</td>";
        html += "<td>" + result._latitude + "</td>";
        html += "<td>" + result._longitude + "</td>";
        html += "<td><button class=\"btn btn-success\">Go</button></td></tr>";
      }
      $("#location_input").val("");
      $("#search_results").append(html + "</table>");
    }
  ).fail(function () {
    alert("Error consulting the api!");
  });
});

$("#clear_btn").click(function () {
  $("#search_results").empty();
  $(this).hide();
});
