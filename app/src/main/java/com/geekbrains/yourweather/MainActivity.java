package com.geekbrains.yourweather;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int display_mode = getResources().getConfiguration().orientation;
        Log.d("myLog", "SET THEME FROM ACTIVITY: orientation = " + display_mode);
        if (display_mode == Configuration.ORIENTATION_LANDSCAPE) {
           if (WeatherMainFragment.isNightModeOn) {
               setTheme(R.style.NoToolbarDarkTheme);
           } else {
            setTheme(R.style.NoToolbarTheme);
           }
        } else {
           if (WeatherMainFragment.isNightModeOn) {
               setTheme(R.style.AppThemeDark);
           } else {
              setTheme(R.style.AppTheme);
            }
        }
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            WeatherMainFragment wmf = new WeatherMainFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.weatherMain, wmf);
            fragmentTransaction.commit();
            Log.d("myLog", "MainActivity: onCreate; savedInstanceState == null");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.about) {
            Intent intent = new Intent(this, InfoActivity.class);
            startActivity(intent);
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        int countOfFragmentInManager = getSupportFragmentManager().getBackStackEntryCount();
        if(countOfFragmentInManager > 0) {
            getSupportFragmentManager().popBackStack();
            getSupportFragmentManager().popBackStack("Some_Key",
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }
}