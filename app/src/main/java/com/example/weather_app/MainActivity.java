package com.example.weather_app;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.*;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {


    private ImageView weatherIcon;

    private ImageView backgroundImage,stable;
    private ScrollView scrollView;
    private EditText cityName;
    Button search;
    TextView show;
    TextView temp, feel, pressure, humidity, minTemp, maxTemp,getCity, idDegree,mainId, description,idTime, sLelevel,gLevel, rFeel,moisture,vision,viss,wSpeed,idGust,idCountry,idSunset,idSunrise,idCloudCov,idCloud,idPrecip;

    TextView pollen,uv,oil,car,workout,traffic,trip,mosquito,camp;
    String url;
    ProgressBar progressBar;

    RelativeLayout bgImg;


    class getWeather extends AsyncTask<String, Void, String>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE); // Show the progress bar
        }

        @Override
        protected String doInBackground(String... urls){
            StringBuilder result = new StringBuilder();
            try{
                URL url= new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                String line="";
                while((line = reader.readLine()) != null){
                    result.append(line).append("\n");
                }

                // Check for error message in response
                if (result.toString().contains("cod") && result.toString().contains("message")) {
                    // Extract error message
                    JSONObject jsonObject = new JSONObject(result.toString());
                    String message = jsonObject.getString("message");
                    return "error:" + message; // Prefix error messages
                }

                return result.toString();
            }catch(Exception e){
                e.printStackTrace();
                return "error:Unable to fetch data"; // Handle exceptions
            }
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE); // Hide the progress bar


            if (result.startsWith("error:")) {
                // Show toast with error message
                Toast.makeText(MainActivity.this, result.substring(6), Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject main = jsonObject.getJSONObject("main");
                    JSONObject wind = jsonObject.getJSONObject("wind"); // Added to retrieve wind data
                    JSONObject sys = jsonObject.getJSONObject("sys"); // Added to retrieve country, sunrise, and sunset data
                    JSONObject clouds = jsonObject.getJSONObject("clouds"); // Get the clouds object
                    // The weather field is an array, so we need to get the first element
                    JSONArray weatherArray = jsonObject.getJSONArray("weather");
                    JSONObject weather = weatherArray.getJSONObject(0);

                    // Fetch rain details
//                    JSONObject rain = jsonObject.optJSONObject("rain");
//                    String precipitation;
//                    if (rain != null) {
//                        double rainVolume = rain.optDouble("1h", 0.0);
//                        // Convert rain volume to percentage (assuming 1 mm = 1%)
//                        precipitation = rainVolume*100 + "%";
//                    } else {
//                        // Set to "0%" if no rain data is available
//                        precipitation = "0%";
//                    }
//
//                    // Update the TextView with the precipitation percentage
//                    idPrecip.setText(precipitation);


                    // Fetch rain details
                    JSONObject rain = jsonObject.optJSONObject("rain");
                    String precipitation;
                    double rainProbability;
                    if (rain != null) {
                        double rainVolume = rain.optDouble("1h", 0.0);
                        double maxRainVolume = 30.0; // Assume 30 mm of rain represents 100% precipitation

                        // Calculate the precipitation percentage
                        double precipitationPercentage = (rainVolume / maxRainVolume) * 100;
                        precipitationPercentage =precipitationPercentage+10;

                        // Cap the percentage at 100% if it exceeds
                        if (precipitationPercentage > 100) {
                            precipitationPercentage = 100;
                        }

                        // Format the precipitation percentage without decimal places
                        precipitation = String.format(Locale.ENGLISH, "%.0f%%", precipitationPercentage);
                        rainProbability=precipitationPercentage;
                    } else {
                        // Set to "0%" if no rain data is available
                        precipitation = "0%";
                        rainProbability=0;
                    }

// Update the TextView with the precipitation percentage
                    idPrecip.setText(precipitation);

//                    Toast.makeText(MainActivity.this, "Rain "+rainProbability, Toast.LENGTH_SHORT).show();



                    // Get the icon code from the weather object
                    String iconCode = weather.getString("icon");
//                    Toast.makeText(MainActivity.this, iconCode, Toast.LENGTH_SHORT).show();
//
// Check the icon code and load the corresponding image from the drawable folder
                    if (iconCode.equals("01d")) {
                        Picasso.get().load(R.drawable.w01d).into(weatherIcon);
                        Picasso.get().load(R.drawable.d1).into(backgroundImage);
                        Picasso.get().load(R.drawable.d1).into(stable);
                        getCity.setTextColor(getResources().getColor(R.color.black));
                        cityName.setTextColor(getResources().getColor(R.color.black));
                    } else if (iconCode.equals("01n")) {
                        Picasso.get().load(R.drawable.w01n).into(weatherIcon);
                        Picasso.get().load(R.drawable.n1).into(backgroundImage);
                        Picasso.get().load(R.drawable.n1).into(stable);
                        getCity.setTextColor(getResources().getColor(R.color.white));
                        cityName.setTextColor(getResources().getColor(R.color.white));
                        mainId.setTextColor(getResources().getColor(R.color.white));
                        description.setTextColor(getResources().getColor(R.color.white));
                        idTime.setTextColor(getResources().getColor(R.color.white));
                        idDegree.setTextColor(getResources().getColor(R.color.white));
                    } else if (iconCode.equals("02d")) {
                        Picasso.get().load(R.drawable.w02d).into(weatherIcon);
                        Picasso.get().load(R.drawable.d2).into(backgroundImage);
                        Picasso.get().load(R.drawable.d2).into(stable);
                        getCity.setTextColor(getResources().getColor(R.color.white));
                        cityName.setTextColor(getResources().getColor(R.color.white));
                    } else if (iconCode.equals("02n")) {
                        Picasso.get().load(R.drawable.w02n).into(weatherIcon);
                        Picasso.get().load(R.drawable.n2).into(backgroundImage);
                        Picasso.get().load(R.drawable.n2).into(stable);
                        getCity.setTextColor(getResources().getColor(R.color.white));
                        cityName.setTextColor(getResources().getColor(R.color.white));
                        mainId.setTextColor(getResources().getColor(R.color.white));
                        description.setTextColor(getResources().getColor(R.color.white));
                        idTime.setTextColor(getResources().getColor(R.color.white));
                    } else if (iconCode.equals("03d")) {
                        Picasso.get().load(R.drawable.w03d).into(weatherIcon);
                        Picasso.get().load(R.drawable.d3).into(backgroundImage);
                        Picasso.get().load(R.drawable.d3).into(stable);
                        getCity.setTextColor(getResources().getColor(R.color.white));
                        cityName.setTextColor(getResources().getColor(R.color.white));
                    } else if (iconCode.equals("03n")) {
                        Picasso.get().load(R.drawable.w03n).into(weatherIcon);
                        Picasso.get().load(R.drawable.n3).into(backgroundImage);
                        Picasso.get().load(R.drawable.n3).into(stable);
                        getCity.setTextColor(getResources().getColor(R.color.white));
                        cityName.setTextColor(getResources().getColor(R.color.white));
                        mainId.setTextColor(getResources().getColor(R.color.white));
                        description.setTextColor(getResources().getColor(R.color.white));
                        idTime.setTextColor(getResources().getColor(R.color.white));
                    } else if (iconCode.equals("04d")) {
                        Picasso.get().load(R.drawable.w04d).into(weatherIcon);
                        Picasso.get().load(R.drawable.d4).into(backgroundImage);
                        Picasso.get().load(R.drawable.d4).into(stable);
                        getCity.setTextColor(getResources().getColor(R.color.white));
                        cityName.setTextColor(getResources().getColor(R.color.white));
                    } else if (iconCode.equals("04n")) {
                        Picasso.get().load(R.drawable.w04n).into(weatherIcon);
                        Picasso.get().load(R.drawable.n4).into(backgroundImage);
                        Picasso.get().load(R.drawable.n4).into(stable);
                        getCity.setTextColor(getResources().getColor(R.color.white));
                        cityName.setTextColor(getResources().getColor(R.color.white));
                        mainId.setTextColor(getResources().getColor(R.color.white));
                        description.setTextColor(getResources().getColor(R.color.white));
                        idTime.setTextColor(getResources().getColor(R.color.white));
                    } else if (iconCode.equals("09d")) {
                        Picasso.get().load(R.drawable.w09d).into(weatherIcon);
                        Picasso.get().load(R.drawable.d5).into(backgroundImage);
                        Picasso.get().load(R.drawable.d5).into(stable);
                    } else if (iconCode.equals("09n")) {
                        Picasso.get().load(R.drawable.w09n).into(weatherIcon);
                        Picasso.get().load(R.drawable.n5).into(backgroundImage);
                        Picasso.get().load(R.drawable.n5).into(stable);
                        getCity.setTextColor(getResources().getColor(R.color.white));
                        cityName.setTextColor(getResources().getColor(R.color.white));
                        mainId.setTextColor(getResources().getColor(R.color.white));
                        description.setTextColor(getResources().getColor(R.color.white));
                        idTime.setTextColor(getResources().getColor(R.color.white));
                    } else if (iconCode.equals("10d")) {
                        Picasso.get().load(R.drawable.w10d).into(weatherIcon);
                        Picasso.get().load(R.drawable.d6).into(backgroundImage);
                        Picasso.get().load(R.drawable.d6).into(stable);
                    } else if (iconCode.equals("10n")) {
                        Picasso.get().load(R.drawable.w10n).into(weatherIcon);
                        Picasso.get().load(R.drawable.n5).into(backgroundImage);
                        Picasso.get().load(R.drawable.n5).into(stable);
                        getCity.setTextColor(getResources().getColor(R.color.white));
                        cityName.setTextColor(getResources().getColor(R.color.white));
                        mainId.setTextColor(getResources().getColor(R.color.white));
                        description.setTextColor(getResources().getColor(R.color.white));
                        idTime.setTextColor(getResources().getColor(R.color.white));
                    } else if (iconCode.equals("11d")) {
                        Picasso.get().load(R.drawable.w11d).into(weatherIcon);
                        Picasso.get().load(R.drawable.d7).into(backgroundImage);
                        Picasso.get().load(R.drawable.d7).into(stable);
                        getCity.setTextColor(getResources().getColor(R.color.white));
                        cityName.setTextColor(getResources().getColor(R.color.white));
                    } else if (iconCode.equals("11n")) {
                        Picasso.get().load(R.drawable.w11d).into(weatherIcon);
                        Picasso.get().load(R.drawable.n6).into(backgroundImage);
                        Picasso.get().load(R.drawable.n6).into(stable);
                        getCity.setTextColor(getResources().getColor(R.color.white));
                        cityName.setTextColor(getResources().getColor(R.color.white));
                        mainId.setTextColor(getResources().getColor(R.color.white));
                        description.setTextColor(getResources().getColor(R.color.white));
                        idTime.setTextColor(getResources().getColor(R.color.white));
                    } else if (iconCode.equals("13d")) {
                        Picasso.get().load(R.drawable.w13d).into(weatherIcon);
                        Picasso.get().load(R.drawable.d8).into(backgroundImage);
                        Picasso.get().load(R.drawable.d8).into(stable);
                        cityName.setTextColor(getResources().getColor(R.color.black));
                        getCity.setTextColor(getResources().getColor(R.color.black));
                    } else if (iconCode.equals("13n")) {
                        Picasso.get().load(R.drawable.w13n).into(weatherIcon);
                        Picasso.get().load(R.drawable.n7).into(backgroundImage);
                        Picasso.get().load(R.drawable.n7).into(stable);
                        getCity.setTextColor(getResources().getColor(R.color.white));
                        cityName.setTextColor(getResources().getColor(R.color.white));
                        mainId.setTextColor(getResources().getColor(R.color.white));
                        description.setTextColor(getResources().getColor(R.color.white));
                        idTime.setTextColor(getResources().getColor(R.color.white));
                    } else if (iconCode.equals("50d")) {
                        Picasso.get().load(R.drawable.w50d).into(weatherIcon);
                        Picasso.get().load(R.drawable.d9).into(backgroundImage);
                        Picasso.get().load(R.drawable.d9).into(stable);
                        cityName.setTextColor(getResources().getColor(R.color.black));
                        getCity.setTextColor(getResources().getColor(R.color.black));
                    } else if (iconCode.equals("50n")) {
                        Picasso.get().load(R.drawable.w50n).into(weatherIcon);
                        Picasso.get().load(R.drawable.n8).into(backgroundImage);
                        Picasso.get().load(R.drawable.n8).into(stable);
                        getCity.setTextColor(getResources().getColor(R.color.white));
                        cityName.setTextColor(getResources().getColor(R.color.white));
                        mainId.setTextColor(getResources().getColor(R.color.white));
                        description.setTextColor(getResources().getColor(R.color.white));
                        idTime.setTextColor(getResources().getColor(R.color.white));
                        idDegree.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        // Set a default icon if no match is found
                        Picasso.get().load(R.drawable.w13n).into(weatherIcon);
                        Picasso.get().load(R.drawable.n8).into(backgroundImage);
                        Picasso.get().load(R.drawable.n8).into(stable);
                    }



                    // Build the icon URL
//                    String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";

                    // Set the weather icon using Picasso
//                    Picasso.get().load(iconUrl).into(weatherIcon);

                    // Get the city name from the JSON object
                    String CityName = jsonObject.getString("name");

                    // Get the country from the sys object
                    String country = sys.optString("country","N/A");

                    // Get the sunrise and sunset timestamps
                    long sunriseTimestamp = sys.getLong("sunrise");
                    long sunsetTimestamp = sys.getLong("sunset");

                    // Convert the timestamps to Date objects
                    Date sunriseDate = new Date(sunriseTimestamp * 1000L);
                    Date sunsetDate = new Date(sunsetTimestamp * 1000L);

                    // Format the times to "hh:mm a"
                    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                    String sunriseTime = timeFormat.format(sunriseDate);
                    String sunsetTime = timeFormat.format(sunsetDate);


// Create a DecimalFormat instance to format the values to one decimal place
                    DecimalFormat decimalFormat = new DecimalFormat("#.#");

// Assuming 'main' is your JSON object
                    double temperatureValue = main.getDouble("temp");
                    double feelsLikeValue = main.getDouble("feels_like");
                    double pressureValue = main.getDouble("pressure");
                    double humidityValue = main.getDouble("humidity");
                    double tempMaxValue = main.getDouble("temp_max");
                    double tempMinValue = main.getDouble("temp_min");
                    double visibilityValue = jsonObject.getDouble("visibility")/1000;
                    double seaLevelValue=main.getDouble(("sea_level"));
                    double groundLevelValue=main.getDouble(("grnd_level"));
                    double windSpeedValue = wind.getDouble("speed");
                    double windGustValue = wind.optDouble("gust", 0.0); // Gust might not be present
                    // Get the cloud coverage percentage
                    int cloudCoverage = clouds.getInt("all");

// Format the values
                    String temperature = decimalFormat.format(temperatureValue) + "°C";
                    String feeling = decimalFormat.format(feelsLikeValue) + "°C";
                    String pressur = decimalFormat.format(pressureValue); // Pressure usually has no decimal places
                    String humid = decimalFormat.format(humidityValue) + "%";
                    String tempMax = decimalFormat.format(tempMaxValue) + "°C";
                    String tempMin = decimalFormat.format(tempMinValue) + "°C";
                    String seaLevel=decimalFormat.format(seaLevelValue) + " hPa";
                    String groundLevel=decimalFormat.format(groundLevelValue) + " hPa";
                    String realF = decimalFormat.format(feelsLikeValue) + "°C";
                    String mois = decimalFormat.format(humidityValue) + "%";
                    String visibility = decimalFormat.format(visibilityValue) + " km"; // Convert visibility to kilometers
                    String windSpeed = decimalFormat.format(windSpeedValue) + " m/s";
                    String windGust = decimalFormat.format(windGustValue) + " m/s";


                    // Round off temperature
                    String degree = String.valueOf(Math.round(main.getDouble("temp"))) + "°";

                    String climate = weather.getString("main");
                    String climateDetail = weather.getString("description");

                    // Get the timestamp (dt) from the JSON object
                    long dt = jsonObject.getLong("dt");

                    // Convert the timestamp to a Date object
                    Date date = new Date(dt * 1000L); // Convert seconds to milliseconds

                    // Format the date to "DAY, MONTH DATE"
                    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd", Locale.ENGLISH);
                    String formattedDate = dateFormat.format(date);

                    // Set the formatted date to the TextView
                    TextView idTime = findViewById(R.id.id_time);
                    idTime.setText(formattedDate);


// Set the text for each TextView
                    getCity.setText(CityName);
                    temp.setText(temperature);
                    feel.setText(feeling);
                    pressure.setText(pressur);
                    humidity.setText(humid);
                    minTemp.setText(tempMin);
                    maxTemp.setText(tempMax);
                    idDegree.setText(degree);
                    mainId.setText(climate);
                    description.setText(climateDetail);
                    sLelevel.setText(seaLevel);
                    gLevel.setText(groundLevel);
                    rFeel.setText(realF);
                    moisture.setText(mois);
                    vision.setText(visibility);
                    viss.setText("Visibility throughout "+visibility);
                    wSpeed.setText(windSpeed);
                    idGust.setText(windGust);
                    idCountry.setText(country);
                    idSunset.setText(sunsetTime);
                    idSunrise.setText(sunriseTime);
                    // Set the cloud coverage to the TextView
                    idCloudCov.setText(cloudCoverage + "%");
                    idCloud.setText(cloudCoverage+"%");



                    // Fetch pollen count, UV index, rain probability, snow probability
                    int pollenCount = 0; // Placeholder value, replace with actual data if available
                    int uvIndex = 0; // Placeholder value, replace with actual data if available
                    double snowProbability;

                    if (temperatureValue<0&&temperatureValue>=-10){
                        snowProbability=45;
                    } else if (temperatureValue<-10&&temperatureValue>=-20) {
                        snowProbability=60;
                    } else if (temperatureValue<-20&&temperatureValue>=-40) {
                    snowProbability=75;
                } else if (temperatureValue<-40&&temperatureValue>=-60) {
                        snowProbability=85;
                    } else if (temperatureValue<-60&temperatureValue>=-80) {
                        snowProbability=95;
                    } else if (temperatureValue<-80) {
                        snowProbability=100;
                    }
                    else {
                        snowProbability=0;
                    }




//                    Toast.makeText(MainActivity.this, "Snow "+snowProbability, Toast.LENGTH_SHORT).show();


                    // Example WeatherData object (replace with actual data)
                    WeatherData weatherData = new WeatherData(humidityValue, rainProbability, snowProbability, temperatureValue, visibilityValue, windSpeedValue, cloudCoverage);

                    // Update weather recommendations
                    updateWeatherRecommendations(weatherData);




                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "An error occurred while processing data.", Toast.LENGTH_LONG).show();
                }
            }

        }
    }
    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityName = findViewById(R.id.cityName);
        search = findViewById(R.id.search);
        temp=findViewById(R.id.temp);
        feel=findViewById(R.id.feel);
        pressure=findViewById(R.id.pressure);
        humidity=findViewById(R.id.humidity);
        minTemp=findViewById(R.id.tmin);
        maxTemp=findViewById(R.id.tmax);
        getCity=findViewById(R.id.cityChosen);
        idDegree=findViewById(R.id.id_degree);
        mainId=findViewById(R.id.main);
        description=findViewById(R.id.description);
        backgroundImage = findViewById(R.id.backgroundImage);
        scrollView = findViewById(R.id.scrollView);
        progressBar = findViewById(R.id.progressBar);
        idTime = findViewById(R.id.id_time);
        sLelevel=findViewById(R.id.sea_levl);
        gLevel=findViewById(R.id.id_grnd_lvl);
        rFeel=findViewById(R.id.id_realfeel);
        moisture=findViewById(R.id.moist);
        vision = findViewById(R.id.vision);
        viss = findViewById(R.id.viss);
        wSpeed = findViewById(R.id.wSpeed);
        idGust = findViewById(R.id.id_gust);
        idCountry = findViewById(R.id.id_country);
        idSunset = findViewById(R.id.id_sunset);
        idSunrise = findViewById(R.id.id_sunrise);
        idCloudCov = findViewById(R.id.id_cloud_cov);
        weatherIcon = findViewById(R.id.img);
        idCloud=findViewById(R.id.id_cloud);
        idPrecip=findViewById(R.id.id_rain);
        stable=findViewById(R.id.stableImg);
        pollen=findViewById(R.id.pollen);
        uv=findViewById(R.id.uv);
        oil=findViewById(R.id.oil);
        car=findViewById(R.id.car);
        workout=findViewById(R.id.exercise);
        traffic=findViewById(R.id.traffic);
        trip=findViewById(R.id.trip);
        mosquito=findViewById(R.id.machhar);
        camp=findViewById(R.id.camp);







        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // Update the background image's position
                backgroundImage.setTranslationY(-scrollY * 0.1f); // Adjust the multiplier for effect
            }
        });




        final String[] temp={""};


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clean up unnecessary spaces before searching
                String city = cityName.getText().toString().trim().replaceAll(" +", " ");

                if (city != null && !city.isEmpty()) {
                    url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&appid=9cb03a651888e2aa7ad88cfa9400112f";
                    getWeather task = new getWeather();
                    task.execute(url);
                } else {
                    Toast.makeText(MainActivity.this, "Enter City", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }


    public void updateWeatherRecommendations(WeatherData weatherData) {

// Pollen count based on humidity, temperature, and rain probability
        if (weatherData.getRainProbability() < 20 && weatherData.getTemperature() > 15 && weatherData.getHumidity() < 60) {
            pollen.setText("High pollen count");
        } else {
            pollen.setText("Low pollen count");
        }

// UV index based on temperature, rain probability, and cloudiness
        if (weatherData.getRainProbability() < 20 && weatherData.getCloudiness() < 30 && weatherData.getTemperature() > 20) {
            uv.setText("High UV index");
        } else {
            uv.setText("Low UV index");
        }


        // Oil products usage
        if ((weatherData.getHumidity() > 30 && weatherData.getHumidity() <70)  || weatherData.getRainProbability() <60) {
            oil.setText("Can use oil based products");
        } else {
            oil.setText("Avoid oil based products");
        }

        // Car washing
        if (weatherData.getRainProbability() > 15 || weatherData.getSnowProbability() > 45) {
            car.setText("Not suitable for car washing");
        } else {
            car.setText("Suitable for car washing");
        }

        // Indoor workout
        if (weatherData.getTemperature() > 30 || weatherData.getRainProbability() > 10 || weatherData.getSnowProbability() > 0) {
            workout.setText("Suitable for indoor workouts");
        } else {
            workout.setText("Suitable for outdoor workouts");
        }

        // Traffic conditions
        if (weatherData.getVisibility() < 5 || weatherData.getWindSpeed() > 20) {
            traffic.setText("Bad traffic condition");
        } else {
            traffic.setText("Good traffic conditions");
        }

        // Trip suitability
        if (weatherData.getTemperature() < -2 || weatherData.getSnowProbability() >= 50) {
            trip.setText("Not suitable for a trip");
        } else {
            trip.setText("Suitable for a trip");
        }

        // Mosquito activity
        if (weatherData.getHumidity() > 80 && weatherData.getTemperature() > 25) {
            mosquito.setText("High mosquito activity");
        } else if ((weatherData.getHumidity() > 60 && weatherData.getHumidity() <= 80) || (weatherData.getTemperature() > 20 && weatherData.getTemperature() <= 25)) {
            mosquito.setText("Some mosquitoes");
        } else {
            mosquito.setText("Few mosquitoes");
        }

        // Camping suitability
        if (weatherData.getRainProbability() > 60 || weatherData.getTemperature() < -10) {
            camp.setText("Not suitable for camping");
        } else {
            camp.setText("Suitable for camping");
        }
    }



}