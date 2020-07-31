package com.geekbrains.yourweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private TextView temperatureView;
    private Button okBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findTemp();
        setOnClickBehaviourBtn();

    }

    private void findTemp(){
        temperatureView = findViewById(R.id.view_temperature);
        okBtn = findViewById(R.id.ok_button);
    }
    private void setOnClickBehaviourBtn() {
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random randInt = new Random();
                int randomTemp = randInt.nextInt(60);

                int getTemp = Integer.parseInt(temperatureView.getText().toString());
                String newRandom = String.valueOf(++randomTemp);

                temperatureView.setText(newRandom);

            }
        });
    }

}