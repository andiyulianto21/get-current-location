package com.example.getcurrentlocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.getcurrentlocation.databinding.ActivityMapsBinding;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, EasyPermissions.PermissionCallbacks{

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    FusedLocationProviderClient mFusedLocationClient;

    private final int REQUEST_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        binding.btnCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                requestPermissionGranted();
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    Toast.makeText(MapsActivity.this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
                    locationPermission();
                }else{
                    showGPSDisabledAlertToUser();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mFusedLocationClient != null) {
//            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Location GPS Disabled, Please enable to use this feature?")
                .setCancelable(false)
                .setPositiveButton("Enable Gps in Settings",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public void locationPermission() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            startActivity(new Intent(MapsActivity.this, Lapor.class));
        }else {
            EasyPermissions.requestPermissions(this, "Melapor butuh akses lokasi sekarang!",
                    REQUEST_LOCATION_PERMISSION, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this, "Permision Granted: " + requestCode, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this, "Permission Denied : " + requestCode, Toast.LENGTH_SHORT).show();

        EasyPermissions.requestPermissions(this, "Melapor butuh akses lokasi sekarang!",
                REQUEST_LOCATION_PERMISSION, Manifest.permission.ACCESS_FINE_LOCATION);
    }

//    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
//    public void requestPermissionGranted(){
//        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
//        if(EasyPermissions.hasPermissions(this, perms)) {
//            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
//        }
//        else {
//            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
//        }
//    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


}