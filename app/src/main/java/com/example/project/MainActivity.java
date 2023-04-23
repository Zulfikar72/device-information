package com.example.project;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ConfigurationInfo;
import android.graphics.ImageFormat;
import android.hardware.SensorEvent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSION = 123;
    private TextView tvManufacturer;
    private TextView tvModelName;
    private TextView tvModelNumber;
    private TextView tvRam;
    private TextView tvStorage;
    private TextView tvBatteryLevel;
    private TextView tvAndroidVersion;
    private TextView tvCameraMegapixels;
    private TextView tvCameraAperture;
    private TextView tvProcessor;
    private TextView tvGPU;

    private TextView imeitxt;

    private Button btn12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tvManufacturer = findViewById(R.id.tvManufacturer);
        tvModelName = findViewById(R.id.tvModelName);
        tvModelNumber = findViewById(R.id.tvModelNumber);
        tvRam = findViewById(R.id.tvRAM);
        tvStorage = findViewById(R.id.tvStorage);
        tvBatteryLevel = findViewById(R.id.tvBatteryLevel);
        tvAndroidVersion = findViewById(R.id.tvAndroidVersion);
        tvCameraMegapixels = findViewById(R.id.tvCameraMegaPixel);
        tvCameraAperture = findViewById(R.id.tvCameraAperture);
        tvProcessor = findViewById(R.id.tvCPUInfo);
        tvGPU = findViewById(R.id.tvGPUInfo);



        btn12 = findViewById(R.id.btn2);
        btn12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SensorActivity.class);
                startActivity(i);
            }
        });
        // Check and request permissions
        checkAndRequestPermissions();
        getAndroidVersion();
        // Get device information
        getDeviceInfo();



        tvManufacturer.setText("Manufacturer: " + android.os.Build.MANUFACTURER);

        imeitxt = (TextView)findViewById(R.id.imeiTextView);
        imeitxt.setText("IMEI NUMBER :-  "+DeviceInfoUtils.getIMEI(getApplicationContext()));

    }

    private void checkAndRequestPermissions() {
        String[] permissions = new String[]{
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        boolean allPermissionsGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
                break;
            }
        }

        if (!allPermissionsGranted) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_PERMISSION);
        }
    }


    private void getDeviceInfo() {
        // Get manufacturer
        String manufacturer = Build.MANUFACTURER;
        tvManufacturer.setText("Brand :- "+manufacturer);

        // Get model name and number
        String model = getModelName();
        tvModelName.setText(model);
        String modelNumber = Build.PRODUCT;
        tvModelNumber.setText("Board :- "+modelNumber);

        // Get RAM
        String ram = getRAM();
        tvRam.setText("Ram :- "+ram);

        // Get storage
        String storage = getInternalStorage() + " / " + getTotalInternalStorage();
        tvStorage.setText("Storage :- "+storage);

        // Get battery level
        String batteryLevel = getBatteryLevel() + "%";
        tvBatteryLevel.setText("batteryLevel :- " + batteryLevel);

        // Get Android version
        String androidVersion = Build.VERSION.RELEASE;
        tvAndroidVersion.setText("androidVersion :- " + androidVersion);

        // Get camera megapixels and aperture
//        String cameraMegapixels = getCameraMegapixels() + " MP";
//        tvCameraMegapixels.setText("cameraMegapixels :- " + cameraMegapixels);
        //get camera info
        String camera = getCameraInfo();
        tvCameraAperture.setText(camera);

        // Get processor information
        String processor = getCPUInfo();
        tvProcessor.setText(processor);

        // Get GPU information
        String gpu = getGPUInfo();
        tvGPU.setText( gpu);

    }

    private String getRAM() {
        String ram = "";
        try {
            FileReader fileReader = new FileReader("/proc/meminfo");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("MemTotal")) {
                    String[] parts = line.split("\\s+");

                    ram = parts[1] + " " + parts[2];
                    break;
                }
            }
            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ram;
    }

    private String getInternalStorage() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        long sizeInBytes = blockSize * availableBlocks;
        long sizeInMB = sizeInBytes / (1024 * 1024);
        return Long.toString(sizeInMB) + " MB";
    }

    private String getTotalInternalStorage() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        long sizeInBytes = blockSize * totalBlocks;
        long sizeInGB = sizeInBytes / (1024 * 1024 * 1024);
        return Long.toString(sizeInGB) + " GB";
    }

    private int getBatteryLevel() {
        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        return (int) Math.floor(level * 100 / (float) scale);
    }

    private String getCameraMegapixels() {
        Camera camera = Camera.open();
        Camera.Parameters params = camera.getParameters();
        List<Camera.Size> sizes = params.getSupportedPictureSizes();
        Camera.Size size = sizes.get(sizes.size() - 1);
        double megapixels = (size.width * size.height) / 1000000.0;
        camera.release();
        return String.format("%.2f", megapixels);
    }

    private String getCameraAperture() {
        Camera camera = null;
        try {
            camera = Camera.open();
            Camera.Parameters params = camera.getParameters();
            String aperture = params.get("aperture");
            if (aperture == null) {
                Log.d("Camera", "Aperture is not supported");
            }
            return aperture;
        } catch (Exception e) {
            Log.e("Camera", "Failed to get camera parameters", e);
            return null;
        } finally {
            if (camera != null) {
                camera.release();
            }
        }
    }


    private String getCPUInfo() {
        String[] command = {"cat", "/proc/cpuinfo"};
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        try {
            Process process = processBuilder.start();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            StringBuilder output = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("Hardware") || line.contains("Processor")) {
                    output.append(line).append("\n");
                }
            }
            bufferedReader.close();
            return output.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private String getGPUInfo() {

        String gpuInfo = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
            gpuInfo = configurationInfo.getGlEsVersion();
        } else {
            gpuInfo = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)).getDeviceConfigurationInfo().getGlEsVersion();
        }
        return gpuInfo;
    }

    private String getAndroidVersion() {
        return Build.VERSION.RELEASE;
    }

    private String getManufacturer() {
        return Build.MANUFACTURER;
    }

    private String getModelName() {
        return Build.MODEL;
    }


    private String getModelNumber() {
        String modelNumber = "";
        try {
            Class<?>[] partypes = new Class<?>[]{String.class, int.class};
            Method method = Build.class.getDeclaredMethod("getString", partypes);
            method.setAccessible(true);
            modelNumber = (String) method.invoke(null, "ro.product.model", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modelNumber;
    }


    private String getCameraInfo() {
        StringBuilder cameraInfo = new StringBuilder();
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        if (cameraManager != null) {
            try {
                String[] cameraIds = cameraManager.getCameraIdList();
                for (String cameraId : cameraIds) {
                    CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
                    cameraInfo.append("Camera ID: ").append(cameraId).append("\n");
                    cameraInfo.append("MegaPixels: ").append(getCameraMegaPixels(characteristics)).append("\n");
                    cameraInfo.append("Aperture: ").append(getCameraAperture(characteristics)).append("\n");
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
        return cameraInfo.toString();
    }

    private float getCameraMegaPixels(CameraCharacteristics characteristics) {
        float totalPixels = 0;
        float sensorSize = 0;
        float pixelSize = 20.0f;
        float megaPixels = 0;
        Size[] outputSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
        if (outputSizes != null && outputSizes.length > 0) {
            Size largest = outputSizes[0];
            for (Size size : outputSizes) {
                float pixels = size.getWidth() * size.getHeight();
                if (pixels > totalPixels) {
                    totalPixels = pixels;
                    largest = size;
                }
            }
            sensorSize = (float) Math.sqrt((double) (largest.getWidth() * largest.getWidth() + largest.getHeight() * largest.getHeight()));
            // Assume cameraId is already defined
            pixelSize = (float) (sensorSize / Math.sqrt(1 + Math.tan(Math.toRadians(characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION))) * Math.tan(Math.toRadians(characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)))));
           // System.out.println("Pixel size: " + pixelSize + " mm");
            //pixelSize = (float) (sensorSize / Math.sqrt(1 + characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)));
            megaPixels = (float) (totalPixels / (1000000.0));
        }
        return megaPixels;
    }

    private float getCameraAperture(CameraCharacteristics characteristics) {
        float aperture = 0;
        float[] apertures = characteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_APERTURES);
        if (apertures != null && apertures.length > 0) {
            aperture = apertures[0];
        }
        return aperture;
    }

    private String getProcessorInfo() {
        StringBuilder processorInfo = new StringBuilder();
        processorInfo.append("CPU: ").append(getCPUInfo()).append("\n");
        processorInfo.append("GPU: ").append(getGPUInfo()).append("\n");
        return processorInfo.toString();



    }
}


//}