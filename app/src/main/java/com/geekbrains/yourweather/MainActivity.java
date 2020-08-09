package com.geekbrains.yourweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;




public class MainActivity extends AppCompatActivity {
    final static String dataKey = "dataKey";
    private Button okBtn;
    private EditText EditCityName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        okBtnClickedToForecastAct();
    }

    private void findViews() {
        okBtn = findViewById(R.id.ok_button);
        EditCityName = findViewById(R.id.edit_city_name);

    }

            private void okBtnClickedToForecastAct () {
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, weatherForecast.class);
                        intent.putExtra("dataKey", EditCityName.getText().toString());
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Вангую", Toast.LENGTH_SHORT).show();
                    }

                });
            }

        }




