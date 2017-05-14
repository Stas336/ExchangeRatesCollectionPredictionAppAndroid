package com.stasl.datacollectionandpredictionfinanceappandroid.activity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.stasl.datacollectionandpredictionfinanceappandroid.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SubdivisionInfoActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener
{
    private String name, address;
    private final String town = "г. Минск, ";
    private TextView textViewAddress;
    private Button routeButton;
    private MapView mapView;
    private GoogleMap map;
    private CameraUpdate cameraUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_activity);
        name = getIntent().getStringExtra("name");
        address = town + getIntent().getStringExtra("address");
        textViewAddress = (TextView)findViewById(R.id.address);
        textViewAddress.setText(address);
        getSupportActionBar().setTitle(name);
        routeButton = (Button)findViewById(R.id.routeButton);
        routeButton.setOnClickListener(this);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        MapsInitializer.initialize(this);
        List<Address> addresses = new ArrayList<>();
        Geocoder geo = new Geocoder(getApplicationContext(), Locale.getDefault());
        try
        {
            addresses = geo.getFromLocationName(address, 1);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        LatLng coordinates = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(coordinates, 17);
        map.addMarker(new MarkerOptions()
                .position(coordinates)
                .title(name));
        map.animateCamera(cameraUpdate);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onClick(View v)
    {

    }
}
