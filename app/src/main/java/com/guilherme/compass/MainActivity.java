package com.guilherme.compass;

import android.app.Activity;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.guilherme.compass.services.CompassService;
import com.guilherme.compass.services.LocationService;
import com.guilherme.compass.interfaces.SensorChangedInterface;

public class MainActivity extends Activity {

    // record the compass picture angle turned
    private float currentCompassDegree = 0f;
    private float currentArrowDegree = 0f;

    // Location inserted on the textviews
    Location destiny = new Location("");

    private ImageView compass;
    private ImageView arrow;
    private TextView txtLatitude;
    private TextView txtLongitude;

    private CompassService mCompassService;
    private SensorManager mSensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set components from the layout
        compass = (ImageView) findViewById(R.id.imgCompass);
        arrow = (ImageView) findViewById(R.id.imgArrow);
        txtLatitude = (TextView) findViewById(R.id.txtLatitude);
        txtLongitude = (TextView) findViewById(R.id.txtLongitude);

        // add watchers to TextViews
        txtLongitude.addTextChangedListener(textChangeWatcher);
        txtLatitude.addTextChangedListener(textChangeWatcher);

        // register the callback for compass sensor changes
        mCompassService = new CompassService(this, sensorChangedCallback);
        mSensorManager = mCompassService.getmSensorManager();
    }

    /**
     * Listener for compass sensor changes
     */
    SensorChangedInterface sensorChangedCallback = new SensorChangedInterface() {
        @Override
        public void onSensorChanged(float degrees) {
            PointNorth(degrees);
            PointToLocation(degrees);
        }
    };

    /**
     * Animate compass to point north
     * @param degree amount of degrees in rad needed to rotate the compass to point north
     */
    private void PointNorth(float degree){
        Animations.rotateBetweenTwoDegrees(compass, currentCompassDegree, -degree);
        currentCompassDegree = -degree;
    }

    /**
     * Animate the arrow to point to the destination, if there is any
     * @param degree amount of degrees in rad nedded to rotate the compass to point destination
     */
    private void PointToLocation(float degree)
    {
        Location currentLoc = LocationService.getLocationManager(this).getLocation();

        GeomagneticField geoField = new GeomagneticField(
                (float) currentLoc.getLatitude(),
                (float) currentLoc.getLongitude(),
                0, // we do not use the z axis
                System.currentTimeMillis());
        float bearing = currentLoc.bearingTo(destiny);
        float newDegree = degree + geoField.getDeclination() - bearing;

        Animations.rotateBetweenTwoDegrees(arrow, currentArrowDegree, -newDegree);

        currentArrowDegree = -newDegree;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // for the system's orientation sensor registered listeners
        mCompassService.getmSensorManager().registerListener(mCompassService, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // to stop the listener and save battery
        mSensorManager.unregisterListener(mCompassService);
    }

    TextWatcher textChangeWatcher = new TextWatcher(){

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (txtLatitude.getText().length() > 0 && txtLongitude.getText().length() > 0) {
                try {
                    String strLatitude = txtLatitude.getText().toString();
                    Double latitude = Double.parseDouble(strLatitude);
                    destiny.setLatitude(latitude);
                    String strLongitude = txtLongitude.getText().toString();
                    Double longitude = Double.parseDouble(strLongitude);
                    destiny.setLongitude(longitude);
                } catch (Exception e) {
                    Log.d("destination value error", e.getLocalizedMessage());
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
