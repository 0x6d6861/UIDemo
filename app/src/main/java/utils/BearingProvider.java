package utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;

import static android.content.Context.SENSOR_SERVICE;

public class BearingProvider implements SensorEventListener {

    private Sensor accelerometer;
    private float[] lastAccelerometer = new float[3];
    private boolean lastAccelerometerSet = false;
    private float[] lastMagnetometer = new float[3];
    private boolean lastMagnetometerSet = false;
    private Sensor magnetometer;
    private float[] orientation = new float[3];
    private double previousBearing = -100.0;
    private long previousTimestamp;
    private float[] r = new float[9];
    private SensorManager sensorManager;
    private boolean subscribed;
    private Context context;


    public BearingProvider(Context context) {
        this.context = context;
    }


    private void debounce(double d2) {
        long l2 = SystemClock.uptimeMillis();
        if (l2 - this.previousTimestamp > 1000) {
            if (Math.abs(d2 - this.previousBearing) > 5.0) {
                this.previousBearing = d2;
            }
            this.previousTimestamp = l2;
        }
    }

   /* private void sendEvent(bqh bqh2, String string2, bqt bqt2) {
        bqh2.a(DeviceEventManagerModule$RCTDeviceEventEmitter.class).emit(string2, bqt2);
    }*/

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private void startMonitoring() {

        this.sensorManager = (SensorManager)this.context.getSystemService(SENSOR_SERVICE);
        this.accelerometer = this.sensorManager.getDefaultSensor(1);
        this.magnetometer = this.sensorManager.getDefaultSensor(2);
        if (this.sensorManager == null || this.accelerometer == null || this.magnetometer == null) return;
        this.sensorManager.registerListener(this, this.accelerometer, 3);
        this.sensorManager.registerListener(this, this.magnetometer, 3);
        this.subscribed = true;
    }

    private void stopMonitoring() {
        if (this.subscribed) {
            Sensor sensor;
            SensorManager sensorManager = this.sensorManager;
            if (sensorManager != null && (sensor = this.accelerometer) != null && this.magnetometer != null) {
                sensorManager.unregisterListener(this, sensor);
                this.sensorManager.unregisterListener(this, this.magnetometer);
            }
            this.subscribed = false;
        }
        this.sensorManager = null;
        this.accelerometer = null;
        this.magnetometer = null;
    }

    /*@bqm
    public void disableMonitoring() {
        this.enabled = false;
        this.stopMonitoring();
    }*/

    /*@bqm
    public void enableMonitoring(bqe bqe2) {
        this.enabled = true;
        this.startMonitoring(bqe2);
    }*/



    public void onAccuracyChanged(Sensor sensor, int n2) {
    }

    /*public void onHostDestroy() {
        this.stopMonitoring();
    }*/

   /* @Override
    public void onHostPause() {
        this.stopMonitoring();
    }

    @Override
    public void onHostResume() {
        this.startMonitoring(null);
    }*/

    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor == this.accelerometer) {
            System.arraycopy(sensorEvent.values, 0, this.lastAccelerometer, 0, sensorEvent.values.length);
            this.lastAccelerometerSet = true;
        } else if (sensorEvent.sensor == this.magnetometer) {
            System.arraycopy(sensorEvent.values, 0, this.lastMagnetometer, 0, sensorEvent.values.length);
            this.lastMagnetometerSet = true;
        }
        if (this.lastAccelerometerSet && this.lastMagnetometerSet) {
            SensorManager.getRotationMatrix((float[])this.r, (float[])null, (float[])this.lastAccelerometer, (float[])this.lastMagnetometer);
            SensorManager.getOrientation((float[])this.r, (float[])this.orientation);
            this.debounce((Math.toDegrees(this.orientation[0]) + 360.0) % 360.0);
        }
    }
}