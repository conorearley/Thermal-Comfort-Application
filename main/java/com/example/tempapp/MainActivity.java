package com.example.tempapp;
import com.example.tempapp.R;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity  implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor tempSensor;
    private Sensor humidSensor;
    private Sensor lightSensor;
    private Sensor gyroSensor;
    private Button darkButton,purpleButton ,whiteButton;
    private TextView tempView,humidView;
    private ImageView gaugeIcon;
    private View layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        tempView = findViewById(R.id.temp_tv);
        humidView = findViewById(R.id.humid_tv);
        gaugeIcon = findViewById(R.id.gauge_icon);
        layout = findViewById(R.id.main_layout);



        tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        humidSensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        Button darkButton = findViewById(R.id.dark_button);
        darkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setBackgroundColor(Color.parseColor("#013F4B"));
                Toast.makeText(MainActivity.this,"Dark", Toast.LENGTH_SHORT).show();

            }
        });

        Button purpleButton = findViewById(R.id.purple_button);
        purpleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setBackgroundColor(Color.parseColor("#681575"));
                Toast.makeText(MainActivity.this,"Purple", Toast.LENGTH_SHORT).show();
            }
        });

        Button whiteButton = findViewById(R.id.white_button);
        whiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setBackgroundColor(Color.parseColor("#DFDFDF"));
                Toast.makeText(MainActivity.this,"White", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(
                this,
                tempSensor,
                SensorManager.SENSOR_DELAY_NORMAL);

        sensorManager.registerListener(
                this,
                humidSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            float temp = event.values[0];
            tempView.setText(String.valueOf(temp));
        } else if (event.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
            float hum = event.values[0];
            humidView.setText(String.valueOf(hum) + " %");
        }

        if (tempSensor != null && humidSensor != null) {
            float temp = tempSensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE ? event.values[0] : -1;
            float hum = humidSensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY ? event.values[0] : -1;


            gaugeIcon.setColorFilter(isTemperatureInComfortZone(temp, hum) ? Color.GREEN : Color.RED);
        }
    }
//    public void onSensorChanged(SensorEvent event) {
//
//        if(event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE){
//            float temp = event.values[0];
//            tempView.setText(""+temp);
//
//        }
//        if(event.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY){
//            float hum = event.values[0];
//            humidView.setText(""+hum+" %");
//
//        }
//        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE || event.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
//            float temp = event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE ? event.values[0] : -1;
//            float hum = event.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY ? event.values[0] : -1;
//
////            tempView.setText(String.valueOf(temp));
////            humidView.setText(String.valueOf(hum));
//
//            gaugeIcon.setColorFilter(isTemperatureInComfortZone(temp, hum) ? Color.GREEN : Color.RED);
//        }




    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    private boolean isTemperatureInComfortZone(float temperature, float humidity) {

        float[][] comfortRanges = {
                {21, 27},   // 100%
                {21.5f, 27.5f}, // 90%
                {22, 28},   // 80%
                {22, 28},   // 70%
                {22.5f, 28.5f}, // 60%
                {23, 29},   // 50%
                {23.5f, 29.5f}, // 40%
                {24, 30},   // 30%
                {24, 30},   // 20%
                {24.5f, 30.5f}, // 10%
                {25, 31}    // 0%
        };

        int humidityIndex = (int) (humidity / 10);
        if (humidityIndex < 0) humidityIndex = 0;
        if (humidityIndex >= comfortRanges.length) humidityIndex = comfortRanges.length - 1;
        float[] range = comfortRanges[humidityIndex];
        return temperature >= range[0] && temperature <= range[1];
    }
}