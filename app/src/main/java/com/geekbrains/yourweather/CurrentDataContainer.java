package com.geekbrains.yourweather;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CurrentDataContainer implements Serializable {
    public String currCityName = "";
    boolean[] switchSettingsArray;
    List<WeatherData> weekWeatherData = new ArrayList<>();
    ArrayList<String> citiesList = new ArrayList<>();
}