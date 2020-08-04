package com.geekbrains.yourweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    private TextView temperatureView;
    private Button okBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        setOnClickBehaviourBtn();
        String instanceState;
        if (savedInstanceState == null){
            instanceState = "Добро пожаловать!";
        }
        else{
            instanceState = "Вы вернулись!";
        }
        Toast.makeText(getApplicationContext(), instanceState + " ", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(getApplicationContext(), "Начали", Toast.LENGTH_SHORT).show();
        Log.d("onStart", "App Started");
    }

    @Override
    protected void onRestoreInstanceState(Bundle saveInstanceState){
        super.onRestoreInstanceState(saveInstanceState);
        Toast.makeText(getApplicationContext(), "Повторный запуск!!", Toast.LENGTH_SHORT).show();
        Log.d("onRestoreInstanceStat", "Instance State Restored");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(getApplicationContext(), "Вы вернулись", Toast.LENGTH_SHORT).show();
        Log.d("onResume", "Resumed");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(getApplicationContext(), "Пауза", Toast.LENGTH_SHORT).show();
        Log.d("onPause", "Paused");
    }

    @Override
    protected void onSaveInstanceState(Bundle saveInstanceState){
        super.onSaveInstanceState(saveInstanceState);
        Toast.makeText(getApplicationContext(), "Сохраняем", Toast.LENGTH_SHORT).show();
        Log.d("onSaveInstanceState", "Instance State Saved");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(getApplicationContext(), "Стоп", Toast.LENGTH_SHORT).show();
        Log.d("onStop", "Stopped");
    }

        @Override
        protected void onRestart() {
            super.onRestart();
            Toast.makeText(getApplicationContext(), "Перезапуск", Toast.LENGTH_SHORT).show();
            Log.d("onRestart", "Restarted");
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
            Toast.makeText(getApplicationContext(), "Конец", Toast.LENGTH_SHORT).show();
            Log.d("onDestroy", "Destroyed");
        }



    private void findViews(){
        temperatureView = findViewById(R.id.view_temperature);
        okBtn = findViewById(R.id.ok_button);
    }
    private void setOnClickBehaviourBtn() {
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Toast.makeText(getApplicationContext(), "Вангую", Toast.LENGTH_SHORT).show();
                    Random randInt = new Random();
                    int randomTemp = randInt.nextInt(60);
                    String getTemp = Integer.toString(randomTemp);
                    int getTempEquals = !getTemp.equals(" ")?Integer.parseInt(getTemp) : 0;
                    int parseTemp = Integer.parseInt(getTemp);
                    String newRandom = String.valueOf(++randomTemp);
                    temperatureView.setText(newRandom);
            }

        });
    }

}