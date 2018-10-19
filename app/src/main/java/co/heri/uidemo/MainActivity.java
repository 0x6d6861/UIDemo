package co.heri.uidemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.fragment.app.FragmentActivity;
import io.nlopez.smartlocation.OnActivityUpdatedListener;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider;



public class MainActivity extends FragmentActivity implements SensorEventListener, OnMapReadyCallback, OnLocationUpdatedListener {

    private GoogleMap mMap = null;
    private Marker driverMarker = null;

    private FusedLocationProviderClient mFusedLocationClient;

    private double currentPhoneOrientation = -100.0;
    private boolean deviceStationary = true;



    TextView myLocationTXT;
    private long previousTimestamp = 0;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private boolean lastAccelerometerSet = false;
    private float[] lastAccelerometer = new float[3];
    private float[] lastMagnetometer = new float[3];
    private boolean lastMagnetometerSet = false;
    private float[] orientation = new float[3];
    private float[] r = new float[9];




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        myLocationTXT = findViewById(R.id.myLocationTXT);



    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(MainActivity.this, R.raw.map));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }

        mMap.setMyLocationEnabled(true);


        /*mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location myLocation) {
                LatLng myLatLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                animateMarker(driverMarker, myLatLng, false);
                float myRotation = (float) bearingBetweenLocations(driverMarker.getPosition(), myLatLng);
                rotateMarker(driverMarker, myRotation);
                Toast.makeText(MainActivity.this, String.format("Location Chnaged with rotation: %s", String.valueOf(myRotation)), Toast.LENGTH_SHORT).show();
                //mMap.animateCamera(CameraUpdateFactory.newLatLng(myLatLng));
                myLocationTXT.setText(String.format("Lat: %s, Lng: %s Bearing: %s", myLocation.getLatitude(), myLocation.getLongitude(), myLocation.getBearing()));
            }
        });*/
    }

    private double bearingBetweenLocations(LatLng latLng1,LatLng latLng2) {

        //double PI = 3.14159;

        double a = latLng1.latitude * Math.PI / 180;
        double b = latLng1.longitude * Math.PI / 180;
        double c = latLng2.latitude * Math.PI / 180;
        double d = latLng2.longitude * Math.PI / 180;

        if (Math.cos(c) * Math.cos(d - b) == 0) {
            return (c > a) ? 0 : 180;
        }

        double angle = Math.atan2(Math.cos(c) * Math.sin(d - b), Math.sin(c) * Math.cos(a) - Math.sin(a)
                * Math.cos(c) * Math.cos(d - b));
        return (angle * 180 / Math.PI + 360) % 360;

    }

private boolean isMarkerRotating = false;
    private void rotateMarker(final Marker marker, final float toRotation) {
        if(!isMarkerRotating) {
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final float startRotation = marker.getRotation();
            final long duration = 1000;

            final Interpolator interpolator = new LinearInterpolator();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    isMarkerRotating = true;

                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed / duration);

                    float rot = t * toRotation + (1 - t) * startRotation;

                    marker.setRotation(-rot > 180 ? rot / 2 : rot);
                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    } else {
                        isMarkerRotating = false;
                    }
                }
            });
        }
    }


    private boolean isMarkerMoving = false;

    public void animateMarker(final Marker marker, final LatLng toPosition, final boolean hideMarker) {

        //stopMonitoring();

        if(!isMarkerMoving){


            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            Projection proj = mMap.getProjection();
            Point startPoint = proj.toScreenLocation(marker.getPosition());
            final LatLng startLatLng = proj.fromScreenLocation(startPoint);
            final long duration = 5000;

            final Interpolator interpolator = new LinearInterpolator();

            handler.post(new Runnable() {
                @Override
                public void run() {

                    isMarkerMoving = true;

                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed
                            / duration);
                    double lng = t * toPosition.longitude + (1 - t)
                            * startLatLng.longitude;
                    double lat = t * toPosition.latitude + (1 - t)
                            * startLatLng.latitude;
                    marker.setPosition(new LatLng(lat, lng));

                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    } else {
                        if (hideMarker) {
                            marker.setVisible(false);
                        } else {
                            marker.setVisible(true);
                        }

                        isMarkerMoving = false;
                    }


                }
            });
        }
    }


    private String getNameFromType(DetectedActivity activityType) {
        switch (activityType.getType()) {
            case DetectedActivity.IN_VEHICLE:
                return "in_vehicle";
            case DetectedActivity.ON_BICYCLE:
                return "on_bicycle";
            case DetectedActivity.ON_FOOT:
                return "on_foot";
            case DetectedActivity.STILL:
                return "still";
            case DetectedActivity.TILTING:
                return "tilting";
            default:
                return "unknown";
        }
    }


    private LocationGooglePlayServicesProvider provider = new LocationGooglePlayServicesProvider();
    private void startLocationUpdate() {

        provider.setCheckLocationSettings(true);

//        long mLocTrackingInterval = 400 * 2; // 0.4 sec
//        float trackingDistance = 0;
        LocationAccuracy trackingAccuracy = LocationAccuracy.HIGH;

        LocationParams.Builder locationParams = new LocationParams.Builder()
                .setAccuracy(trackingAccuracy);
                //.setDistance(trackingDistance)
                //.setInterval(mLocTrackingInterval);

        SmartLocation smartLocation = new SmartLocation.Builder(this)
                .logging(false)
                .build();

        smartLocation.location(provider)
                .continuous()
                .config(locationParams.build())
                .start(MainActivity.this);

        smartLocation.activity().start(new OnActivityUpdatedListener() {
            @Override
            public void onActivityUpdated(DetectedActivity detectedActivity) {
                if (detectedActivity != null) {

                    if((detectedActivity.getType() != DetectedActivity.STILL) || (detectedActivity.getType() != DetectedActivity.TILTING)){
                        deviceStationary = false;
                    }else{
                        deviceStationary = true;
                        rotateMarker(driverMarker, (float) currentPhoneOrientation);
                    }

                    Toast.makeText(MainActivity.this,
                            String.format(getString(R.string.activity_detect),
                                    getNameFromType(detectedActivity),
                                    detectedActivity.getConfidence()), Toast.LENGTH_SHORT).show();

                } else {
                    deviceStationary = true;

                    Toast.makeText(MainActivity.this, R.string.no_activity_dectect, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void stopLocationUpdate() {

        // Location Stopped
        SmartLocation.with(this).location().stop();

        // Activity Recognition stopped!
        SmartLocation.with(this).activity().stop();

        // Geofencing stopped!
//        SmartLocation.with(this).geofencing().stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopMonitoring();
        stopLocationUpdate();
    }

    @Override
    protected void onResume() {
        super.onResume();

        startMonitoring();
        startLocationUpdate();

    }

    @Override
    protected void onPause() {
        super.onPause();
        // to stop the listener and save battery
        stopMonitoring();
        stopLocationUpdate();

    }

    // device sensor manager
    private SensorManager mSensorManager;
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {



        if (sensorEvent.sensor == this.accelerometer) {
            System.arraycopy(sensorEvent.values, 0, this.lastAccelerometer, 0, sensorEvent.values.length);
            this.lastAccelerometerSet = true;
        } else if (sensorEvent.sensor == this.magnetometer) {
            System.arraycopy(sensorEvent.values, 0, this.lastMagnetometer, 0, sensorEvent.values.length);
            this.lastMagnetometerSet = true;
        }

        if (this.lastAccelerometerSet && this.lastMagnetometerSet) {
            SensorManager.getRotationMatrix(this.r, null, this.lastAccelerometer, this.lastMagnetometer);
            SensorManager.getOrientation(this.r, this.orientation);

            // get the angle around the z-axis rotated
            double degree = (Math.toDegrees(this.orientation[0]) + 360.0) % 360.0;
            long l2 = SystemClock.uptimeMillis();
            if (l2 - this.previousTimestamp > 1000) {
                if (Math.abs(degree - this.currentPhoneOrientation) > 5.0) {
                    this.currentPhoneOrientation = degree;
                }
                this.previousTimestamp = l2;
                if(driverMarker != null) {
                    if(isMarkerMoving){
                        return;
                    }
                    rotateMarker(driverMarker, (float) degree);
                }
            }
        }
    }

    private void startMonitoring(){

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        {
            if(mSensorManager != null){
                this.accelerometer = this.mSensorManager.getDefaultSensor(1);
                this.magnetometer = this.mSensorManager.getDefaultSensor(2);
                if(this.accelerometer != null || this.magnetometer != null){
                    mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(1), 3);
                    mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(2), 3);
                }
            }

        }
    }

    private void stopMonitoring() {
        {
            Sensor sensor;
            SensorManager sensorManager = this.mSensorManager;
            if (sensorManager != null && (sensor = this.accelerometer) != null && this.magnetometer != null) {
                sensorManager.unregisterListener(this, sensor);
                this.mSensorManager.unregisterListener(this, this.magnetometer);
            }
        }
        this.mSensorManager = null;
        this.accelerometer = null;
        this.magnetometer = null;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onLocationUpdated(Location myLocation) {

        LatLng myLatLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());

        if (driverMarker == null){
            driverMarker = mMap.addMarker(new MarkerOptions()
                    .title(getString(R.string.my_location_title))
                    .position(myLatLng)
                    .flat(true)
                    .anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car)));
        }


        float myRotation = 0.0f;
        float distance = 0.0f;

        if(!(driverMarker == null)){

            Location driverLocation = new Location("");
            driverLocation.setLatitude(driverMarker.getPosition().latitude);
            driverLocation.setLongitude(driverMarker.getPosition().longitude);

            distance = driverLocation.distanceTo(myLocation);

            if(distance < 1.378f){
                deviceStationary = true;
                if(isMarkerMoving){
                    return;
                }
                //rotateMarker(driverMarker, (float) currentPhoneOrientation);
                driverMarker.setPosition(myLatLng);
                return;
            }else{
                myRotation = (float) bearingBetweenLocations(driverMarker.getPosition(), myLatLng);
                rotateMarker(driverMarker, myRotation);
                animateMarker(driverMarker, myLatLng, false);
            }


        }



        Toast.makeText(MainActivity.this, String.format(getString(R.string.on_location_change), String.valueOf(distance)), Toast.LENGTH_SHORT).show();
        //mMap.animateCamera(CameraUpdateFactory.newLatLng(myLatLng));
        myLocationTXT.setText(String.format(getString(R.string.my_location_stats), myLocation.getLatitude(), myLocation.getLongitude(), myRotation));

    }


}
