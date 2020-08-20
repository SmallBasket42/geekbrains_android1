package com.geekbrains.yourweather;

import android.util.Log;
import android.widget.ArrayAdapter;
import androidx.fragment.app.FragmentTransaction;
import java.util.List;

public final class ChooseCityActivityPresenter {
    final String myLog = "myLog";
    private static ChooseCityActivityPresenter instance = null;
    private static final Object syncObj = new Object();
    private static String[] citiesArray;
    private static ArrayAdapter<String> citiesListAdapter;
    private static List<String> citiesList;
    private ChooseCityActivityPresenter(){}
    public static ChooseCityActivityPresenter getInstance(){
        synchronized (syncObj) {
            if (instance == null) {
                instance = new ChooseCityActivityPresenter();
            }
            return instance;
        }
    }

    public void updateWeatherInLandscape(CurrentDataContainer container,
                                         androidx.fragment.app.FragmentManager fragmentManager) {
        WeatherMainFragment weatherMainFragment = (WeatherMainFragment) fragmentManager.findFragmentById(R.id.weatherMain);
        if(weatherMainFragment == null || !weatherMainFragment.getCityName().equals(ChooseCityFragment.currentCity)) {
            Log.d(myLog, "inside updateWeatherInLandscape");
            weatherMainFragment = WeatherMainFragment.create(container);
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.weatherMain, weatherMainFragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.addToBackStack(null);
            ft.addToBackStack("Some_Key");
            ft.commit();
        }
    }
}

