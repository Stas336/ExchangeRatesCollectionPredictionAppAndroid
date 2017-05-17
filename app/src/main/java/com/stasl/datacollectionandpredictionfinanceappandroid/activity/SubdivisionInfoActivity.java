package com.stasl.datacollectionandpredictionfinanceappandroid.activity;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.stasl.datacollectionandpredictionfinanceappandroid.R;
import com.stasl.datacollectionandpredictionfinanceappandroid.gps.LocationService;

import java.io.IOException;
import java.math.BigDecimal;
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
    private LatLng subdivisionCoordinates;
    private List<Marker> markers;
    private final int padding = 20; // offset from edges of the map in pixels

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
            finish();
            e.printStackTrace();
        }
        subdivisionCoordinates = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(subdivisionCoordinates, 17);
        markers = new ArrayList<>();
        markers.add(map.addMarker(new MarkerOptions()
                .position(subdivisionCoordinates)
                .title(name)));
        map.animateCamera(cameraUpdate);
    }

    private void drawPrimaryLinePath(ArrayList<LatLng> coordinates)
    {
        if (map == null)
        {
            return;
        }
        if (coordinates.size() < 2)
        {
            return;
        }
        PolylineOptions options = new PolylineOptions();
        options.color(Color.parseColor("#CC0000FF"));
        options.width(5);
        options.visible(true);
        for (LatLng coordinate : coordinates)
        {
            options.add(coordinate);
        }
        map.addPolyline( options );
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers)
        {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        map.animateCamera(cameraUpdate);
        Snackbar.make(findViewById(android.R.id.content), getString(R.string.distance_info) + " " + calculateDistance(coordinates.get(0), coordinates.get(1)) + " " + getString(R.string.km), Snackbar.LENGTH_SHORT).show();
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
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[] { android.Manifest.permission.ACCESS_COARSE_LOCATION }, LocationService.MY_PERMISSION_ACCESS_COURSE_LOCATION);
        }
        else
        {
            findMyLocation();
        }
    }
    private void findMyLocation()
    {
        LocationService locationService = LocationService.getLocationManager(this);
        Log.d("GPS lat", String.valueOf(locationService.getLatitude()));
        Log.d("GPS lng", String.valueOf(locationService.getLongitude()));
        markers.add(map.addMarker(new MarkerOptions()
                .position(new LatLng(locationService.getLatitude(), locationService.getLongitude()))
                .title(getString(R.string.user_place))));
        ArrayList<LatLng> coordinates = new ArrayList<>();
        coordinates.add(subdivisionCoordinates);
        coordinates.add(new LatLng(locationService.getLatitude(), locationService.getLongitude()));
        drawPrimaryLinePath(coordinates);
    }
    public double calculateDistance(LatLng start, LatLng end)
    {
        final int Radius = 6371; //radius of earth in km
        double lat1 = start.latitude;
        double lat2 = end.latitude;
        double lon1 = start.longitude;
        double lon2 = end.longitude;
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return new BigDecimal(Radius * c).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch (requestCode)
        {
            case LocationService.MY_PERMISSION_ACCESS_COURSE_LOCATION:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    findMyLocation();
                }
                else
                {
                    Snackbar.make(findViewById(android.R.id.content), R.string.permission, Snackbar.LENGTH_SHORT).show();
                }
            }
        }
    }
}
