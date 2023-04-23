# zulfiproject
This is an Android Java code for an app that retrieves various information about the device it's running on, such as the device's manufacturer,
model name, RAM, storage, battery level, Android version, camera megapixels and aperture, CPU information, GPU information, and IMEI number. 
The code uses various APIs provided by the Android SDK to retrieve this information, such as the android.os.Build class for manufacturer,
model name, and Android version, and the android.hardware.camera2 package for camera information.

The onCreate method sets up the app's user interface and calls various methods to retrieve the device information.
The checkAndRequestPermissions method checks if the app has the necessary permissions to retrieve the information and requests them if necessary. 
The getDeviceInfo method retrieves the device information using various methods such as getModelName, getRAM, getInternalStorage, getTotalInternalStorage,
getBatteryLevel, getCameraInfo, getCPUInfo, and getGPUInfo. The getAndroidVersion method retrieves the Android version of the device.

There is also an onClick method that sets up a button to launch a new activity that displays sensor information.

Note that the getIMEI method is not included in the code, but it appears to be a custom method defined in a separate class called DeviceInfoUtils.
