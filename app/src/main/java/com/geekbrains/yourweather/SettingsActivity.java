package com.geekbrains.yourweather;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
    private SwitchCompat nightModeSwitch;
    private SwitchCompat pressureSwitch;
    private SwitchCompat feelsLikeSwitch;
    SettingsActivityPresenter settingsActivityPresenter = SettingsActivityPresenter.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (settingsActivityPresenter.getIsNightModeSwitchOn()) {
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        initViews();
        setCurrentSwitchState();
        setOnNightModeSwitchClickListener();
        setOnFeelsLikeSwitchClickListener();
        setOnPressureSwitchClickListener();
        showBackBtn();
    }

    private void initViews() {
        nightModeSwitch = findViewById(R.id.night_mode_switch);
        pressureSwitch = findViewById(R.id.pressure_switch);
        feelsLikeSwitch = findViewById(R.id.feelsLikeSwitch);
    }

    private void setOnNightModeSwitchClickListener(){
        nightModeSwitch.setOnClickListener(view -> {
            settingsActivityPresenter.changeNightModeSwitchStatus();
            recreate();
        });
    }

    private void setOnPressureSwitchClickListener(){
        pressureSwitch.setOnClickListener(view -> settingsActivityPresenter.changePressureSwitchStatus());
    }

    private void setOnFeelsLikeSwitchClickListener(){
        feelsLikeSwitch.setOnClickListener(view -> settingsActivityPresenter.changeFeelsLikeSwitchStatus());
    }

    private void showBackBtn() {
        try {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException exc) {
            exc.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this,MainActivity.class);
            intent.putExtra("currCity", getCurrentDataContainer());
            startActivity(intent);
            finish();
        }
        return true;
    }

    private CurrentDataContainer getCurrentDataContainer(){
        CurrentDataContainer newCdc = new CurrentDataContainer();
        CurrentDataContainer cdc = (CurrentDataContainer) getIntent().getSerializableExtra("currCity");
        newCdc.switchSettingsArray = settingsActivityPresenter.createSettingsSwitchArray();
        assert cdc != null;
        if (cdc.weekWeatherData != null) newCdc.weekWeatherData = cdc.weekWeatherData;
        if (cdc.citiesList.size() > 0) newCdc.citiesList = cdc.citiesList;
        newCdc.currCityName = cdc.currCityName;
        return newCdc;
    }

    public void setCurrentSwitchState(){
        boolean[] switchArr =  settingsActivityPresenter.getSettingsArray();
        if(switchArr != null){
            nightModeSwitch.setChecked(switchArr[0]);
            feelsLikeSwitch.setChecked(switchArr[1]);
            pressureSwitch.setChecked(switchArr[2]);
        }
    }
}