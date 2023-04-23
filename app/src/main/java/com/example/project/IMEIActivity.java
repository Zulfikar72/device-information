package com.example.project;


import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class IMEIActivity extends AppCompatActivity {
    TextView imeitxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imeiactivity);
       getSupportActionBar().hide();
        imeitxt = (TextView)findViewById(R.id.imeiTextView);
        imeitxt.setText("YOUR IMEI NUMBER :-  "+DeviceInfoUtils.getIMEI(getApplicationContext()));

    }}