package com.geekbrains.yourweather;

import androidx.appcompat.app.AppCompatActivity;
import android.content.res.Configuration;
import android.os.Bundle;
import com.geekbrains.yourweather.fragments.CoatOfArmsFragment;

public class CoatOfArmsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coatofarms);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }

        if (savedInstanceState == null) {
            CoatOfArmsFragment details = new CoatOfArmsFragment();
            details.setArguments(getIntent().getExtras());
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, details)
                    .commit();
        }
    }
}