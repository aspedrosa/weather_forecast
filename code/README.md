## How to run

This project uses external apis to get meteorologic data which need api keys.

Currently the projects uses the following apis:
- LocationIQ: for forward geocoding
- Apixu: as primary api for weather data
- Dark Sky: as backup api for weather data

To get the api keys the project uses environment variables, so before running the project
define the following environment variables each one with the respective api key:
- LOCATION_IQ_APIKEY
- APIXU_APIKEY
- DARK_SKY_APIKEY
