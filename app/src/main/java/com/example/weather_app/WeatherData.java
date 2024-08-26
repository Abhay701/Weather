package com.example.weather_app;

public class WeatherData {
    private int cloud;
    private double humidity;
    private double rainProbability;
    private double snowProbability;
    private double temperature;
    private double visibility;
    private double windSpeed;

    // Constructor
    public WeatherData(double humidity, double rainProbability, double snowProbability, double temperature, double visibility, double windSpeed,int cloud) {
        this.humidity = humidity;
        this.rainProbability = rainProbability;
        this.snowProbability = snowProbability;
        this.temperature = temperature;
        this.visibility = visibility;
        this.windSpeed = windSpeed;
        this.cloud=cloud;
    }

    // Getters

    public double getHumidity() {
        return humidity;
    }

    public double getRainProbability() {
        return rainProbability;
    }

    public double getSnowProbability() {
        return snowProbability;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getVisibility() {
        return visibility;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public int getCloudiness(){return cloud;}
}
