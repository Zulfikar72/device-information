package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class SensorActivity extends AppCompatActivity implements SensorEventListener  {
    private SensorManager sensorManager;
    private Sensor mGPSSensor;
    private Sensor mGyroscopeSensor;
    private Sensor mBarometerSensor;
    private Sensor mAccelerometerSensor;
    private Sensor mRotationVectorSensor;
    private Sensor mProximitySensor;
    private Sensor mAmbientLightSensor;
    private static final int REQUEST_CODE = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        // Initialize sensors
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mGyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mBarometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        mAccelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mRotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        mProximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mAmbientLightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

       // tvManufacturer.setText("Manufacturer: " + android.os.Build.MANUFACTURER);


    }
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, mGPSSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener( this, mGyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, mBarometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, mRotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, mProximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, mAmbientLightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                TextView accelerometerDataTextView = findViewById(R.id.accelerometer_data);
                accelerometerDataTextView.setText("Accelerometer Data: " +"X"+ x + ", "+"Y" + y + ", "+"Z" + z);
                break;
            case Sensor.TYPE_GYROSCOPE:
                float a = event.values[0];
                float b = event.values[1];
                float c = event.values[2];
                TextView gyroscopeDataTextView = findViewById(R.id.gyroscope_data);
                gyroscopeDataTextView.setText("Gyroscope Data: " +"A"+ a + ", " +"B"+ b + ", "+"C" + c);
                break;
            case Sensor.TYPE_PRESSURE:
                float pressure = event.values[1];
                TextView barometerDataTextView = findViewById(R.id.barometer_data);
                barometerDataTextView.setText("Barometer Data: " + pressure);
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
                float d = event.values[0];
                float e = event.values[1];
                float f = event.values[2];
                TextView rotationVectorDataTextView = findViewById(R.id.rotation_vector_data);
                rotationVectorDataTextView.setText("Rotation Vector Data: " +"A"+ d + ", "+"B" + e + ", "+"C" + f);
                break;
            case Sensor.TYPE_PROXIMITY:
                float distance = event.values[0];
                TextView proximityDataTextView = findViewById(R.id.proximity_data);
                proximityDataTextView.setText("Proximity Sensor Data: " + distance);
                break;
            case Sensor.TYPE_LIGHT:
                float light = event.values[0];
                TextView ambientLightDataTextView = findViewById(R.id.ambient_light_data);
                ambientLightDataTextView.setText("Ambient Light Data: " + light);
                break;
        }
//        sensorManager.unregisterListener(this);
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }




}