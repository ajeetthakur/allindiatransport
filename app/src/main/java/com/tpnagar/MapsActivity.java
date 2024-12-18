package com.tpnagar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;

import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    /**
     * GoogleApiClient wraps our service connection to Google Play Services and provides access
     * to the user's sign in state as well as the Google's APIs.
     */
    double lat = 0.0, long_ = 0.0;

    StringRequest post;
    LocationRequest mLocationRequest;
    ImageView back;
    String mAddress;
    LatLng latLng;

    SupportMapFragment mFragment;
    // Marker currLocationMarker;


    private GoogleMap mMap;
    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;
    ConnectionDetector cd;
    SharedPreferences prefs = null;

    String string_Lat_, string_Long_, Location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        cd = new ConnectionDetector(this);
        prefs = getSharedPreferences("com.tpnagar", MODE_PRIVATE);

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();

        string_Lat_ = intent.getStringExtra("LatStr");
        string_Long_ = intent.getStringExtra("LongStr");
        Location = intent.getStringExtra("Location");

        double lat = Double.parseDouble(string_Lat_);
        double lng = Double.parseDouble(string_Long_);
        latLng = new LatLng(lat, lng);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);


        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap geMap) {
        mMap = geMap;


        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(Location);
        // markerOptions.icon(icon);
        mMap.addMarker(markerOptions);

        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(14).build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


}



