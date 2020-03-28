package com.example.mydoctor.ui.mapactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.example.mydoctor.R;
import com.example.mydoctor.baseviews.BaseActivity;
import com.example.mydoctor.di.viewmodels.ViewModelProviderFactory;
import com.example.mydoctor.ui.dashboardactivity.DashBoardActivity;
import com.example.mydoctor.utils.ViewSnackBar;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.Task;
import com.google.maps.GeoApiContext;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapActivity extends BaseActivity implements OnMapReadyCallback {

    private static final String TAG = "MapActivity";

    @BindView(R.id._toolbar_dashboard)
    Toolbar _toolbar_dashboard;

    @Inject
    ViewModelProviderFactory providerFactory;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static float DEFAULT_ZOOM = 15f;
    public Boolean mLocationPermissionsGranted = false;
    MarkerOptions placeOne, placeTwo;
    private GeoApiContext geoApiContext;
    Polyline polyline;
    SupportMapFragment supportMapFragment;
    double userLang, userLong;
    private Polyline currentPolyline;
    String parentActivity;
    private ViewSnackBar viewSnackBar;
    private MapViewModel mapViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        View myView = findViewById(android.R.id.content);

        viewSnackBar = new ViewSnackBar(myView, this);

        if (canGetLocation()) {

            getLocationPermissions();

        }

        mapViewModel = ViewModelProviders.of(this, providerFactory).get(MapViewModel.class);

        subscribeObservers();

        ButterKnife.bind(this);
    }

    private void subscribeObservers(){
        mapViewModel.observeUserLocation().observe(this, apiSuccessModelResource -> {

            if (apiSuccessModelResource != null) {

                switch (apiSuccessModelResource.status) {

                    case LOADING:

                        break;

                    case SUCCESS:

                        if (apiSuccessModelResource.data != null) {

                        }
                        break;


                    case ERROR:

                        viewSnackBar.viewMySnack(apiSuccessModelResource.message, R.color.blue);

                }

            }

        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.d(TAG, "onMapReady: Map is ready");
        mMap = googleMap;
        viewSnackBar.viewMySnack("Map is Ready", R.color.green);

        if (mLocationPermissionsGranted) {

            getDeviceLocation();

            mMap.setMyLocationEnabled(true);
           /* mMap.addMarker(placeOne);
            mMap.addMarker(placeTwo);*/
        }

    }

    private void getDeviceLocation() {

        Log.d(TAG, "getDeviceLocation:  device Location");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {

            Task task = fusedLocationProviderClient.getLastLocation();
            task.addOnCompleteListener(task1 -> {

                if (task1.isSuccessful()) {
                    Log.d(TAG, "getDeviceLocation:  found Location");
                    Location location = (Location) task1.getResult();

                    if (location != null) {
                        userLang = location.getLatitude();
                        userLong = location.getLongitude();

                        Log.d(TAG, "getDeviceLocation: " + "Lang" + userLang + "Long" + userLong);

                        moveCamera(new LatLng(location.getLatitude(), location.getLongitude()), DEFAULT_ZOOM);

                        mapViewModel.saveUserLocation(userLang, userLong);

                    }else {

                        Log.d(TAG, "getDeviceLocation: location is null");

                    }

                } else {

                    viewSnackBar.viewMySnack("Unable to get Current Location", R.color.primarycolor);

                }
            });


        } catch (SecurityException e) {

            Log.d(TAG, "getDeviceLocation: Security Exception: " + e.getMessage());

        }

    }

    private void getLocationPermissions() {

        Log.d(TAG, "getLocationPermissions: getting Location Permission ");

        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                mLocationPermissionsGranted = true;
                initMap();

                if (geoApiContext == null) {

                    geoApiContext = new GeoApiContext.Builder()
                            .apiKey(getString(R.string.google_api_key2))
                            .build();

                }

            } else {

                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);

            }

        } else {

            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);

        }

    }

    public void initMap() {

        Log.d(TAG, "initMap: Initializing Map");


        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (supportMapFragment != null) {
            supportMapFragment.getMapAsync(this);
        }else {

            Log.d(TAG, "initMap: Error Map Initialization");

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d(TAG, "onRequestPermissionsResult: onCalled");

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                for (int grantResult : grantResults) {

                    if (grantResult != PackageManager.PERMISSION_GRANTED) {

                        mLocationPermissionsGranted = false;
                        return;

                    }
                }
                Log.d(TAG, "onRequestPermissionsResult: permission Granted");
                mLocationPermissionsGranted = true;
                // Initialize Map

                initMap();

            }
        }

    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        supportMapFragment.onLowMemory();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void moveCamera(LatLng latLng, float zoom) {

        Log.d(TAG, "moveCamera:  moving camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

    }


    public boolean canGetLocation() {

        boolean result = false;

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (locationManager != null) {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {


                buildAlertMessageNoGps();

            } else {

                result = true;

                Log.d(TAG, "canGetLocation: "+result);

            }
        }

        return result;

    }

    private void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS?...")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialogInterface, i) -> getGpsSettings())
                .setNegativeButton("No", (dialogInterface, i) -> {
                    dialogInterface.cancel();
                    startActivity(new Intent(MapActivity.this, DashBoardActivity.class));
                });

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public void getGpsSettings(){

        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        finish();

    }


}
