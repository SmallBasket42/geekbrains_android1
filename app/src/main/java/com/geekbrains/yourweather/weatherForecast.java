package com.geekbrains.yourweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Objects;


public class weatherForecast extends AppCompatActivity {
    private TextView cityName;
    private Button infoBtn;
    final static String dataKey = "dataKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        showBackBtn();
        findViews();
        showCityNameFromMainAct();
        showMoreInfoBtnOnClick();
    }
    private void findViews() {
        cityName = findViewById(R.id.city_name);
        infoBtn = findViewById(R.id.info_button);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            String text = cityName.getText().toString();
            Intent dataIntent = new Intent();
            dataIntent.putExtra(dataKey, text);
            setResult(RESULT_OK, dataIntent);
            finish();
        }
        return true;
    }
    private void showBackBtn() {
        try {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException exc) {
            exc.printStackTrace();
        }
    }
    private void showCityNameFromMainAct() {
        String data = getIntent().getStringExtra(MainActivity.dataKey);
        cityName.setText(data);
    }

    private void showMoreInfoBtnOnClick() {
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.calend.ru/events/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }
}
