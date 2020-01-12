package com.example.pothole.Citizen.maps_citizen;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.pothole.R;
import com.example.pothole.after_clicking_marker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMyLocationButtonClickListener {

    FirebaseAuth mAuth;
    private GoogleMap mMap;
    LocationManager locationManager;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    Marker marker;
    LocationListener locationListener;
    double latitude;
    double longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        Objects.requireNonNull(mapFragment).getMapAsync(this);
        locationManager=(LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
        else
        {
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    //get the location name from latitude and longitude
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addresses =
                                geocoder.getFromLocation(latitude, longitude, 1);
                        String result = addresses.get(0).getLocality() + ":";
                        result += addresses.get(0).getCountryName();
                        LatLng latLng = new LatLng(latitude, longitude);
                        if (marker != null) {
                            marker.remove();
                            marker = mMap.addMarker(new MarkerOptions().position(latLng).title(result));
                            mMap.setMaxZoomPreference(40);
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        } else {
                            marker = mMap.addMarker(new MarkerOptions().position(latLng).title(result));
                            mMap.setMaxZoomPreference(40);
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 0, locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    latitude= location.getLatitude();
                    longitude = location.getLongitude();
                    //get the location name from latitude and longitude
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addresses =
                                geocoder.getFromLocation(latitude, longitude, 1);
                        String result = addresses.get(0).getLocality() + ":";
                        result += addresses.get(0).getCountryName();
                        LatLng latLng = new LatLng(latitude, longitude);
                        if (marker != null) {
                            marker.remove();
                            marker = mMap.addMarker(new MarkerOptions().position(latLng).title(result));
                            mMap.setMaxZoomPreference(40);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 21.0f));
                        } else {
                            marker = mMap.addMarker(new MarkerOptions().position(latLng).title(result));
                            mMap.setMaxZoomPreference(40);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 21.0f));
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }

            };
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 0, locationListener);
        }
    }

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
        Log.i("tejas","marker2");

        LatLngBounds boundsIndia = new LatLngBounds(new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466));
        int padding = 0; // offset from edges of the map in pixels
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(boundsIndia, padding);
        mMap.animateCamera(cameraUpdate);
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("pothole");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double latitude,longitude;
                mAuth = FirebaseAuth.getInstance();
                String uid=mAuth.getUid();
                //mMap.clear();
                for(DataSnapshot p:dataSnapshot.getChildren()) {
                    String potuid = p.getKey().toString();
                    int statusflag = p.child("statusFlag").getValue(Integer.class);
                    String str = p.child("uploadBy").getValue(String.class);
                    if (statusflag < 4) {
                        if (uid.equals(str)) {
                            latitude = p.child("lat").getValue(double.class);
                            longitude = p.child("longi").getValue(double.class);
                            LatLng plot = new LatLng(latitude, longitude);
                            mMap.addMarker(new MarkerOptions().position(plot).title("Potholes reported by me").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))).setTag(potuid);
                            //mMap.moveCamera(CameraUpdateFactory.newLatLng(plot));
                            // Toast.makeText(getApplicationContext(),"Pothole reported by me",Toast.LENGTH_SHORT).show();
                        } else {
                            latitude = p.child("lat").getValue(double.class);
                            longitude = p.child("longi").getValue(double.class);
                            LatLng plot = new LatLng(latitude, longitude);
                            mMap.addMarker(new MarkerOptions().position(plot).title("Potholes reported from here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))).setTag((potuid));
                            //mMap.moveCamera(CameraUpdateFactory.newLatLng(plot));
                        }
                    }
                }

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {


                    @Override
                    public boolean onMarkerClick(final Marker marker) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 25));
                        String title = marker.getTitle();
                        if (title.equals("Potholes reported by me")) {
                            LatLng m = marker.getPosition();
                            String address = getAddressLine(m);
                            final Marker plot = marker;
                            View view = findViewById(android.R.id.content);
                            Snackbar snackbar = Snackbar
                                    .make(view, address, Snackbar.LENGTH_LONG)
                                    .setAction("View", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            String obj = plot.getTag().toString();

                                            Intent i = new Intent(MapsActivity.this, after_clicking_marker.class);
                                            i.putExtra("co-ordinates", obj);
                                            startActivity(i);
                                        }
                                    });
                            snackbar.show();
                            // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(m.latitude,m.longitude), 5.0f));
                        }

                        return false;
                    }
                });



            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
    public  String getAddressLine(LatLng m)
    {

        Geocoder geo=new Geocoder(getApplicationContext());
        String address="";
        try {
            List<Address> addresses =null;
            try {
                addresses = geo.getFromLocation(m.latitude, m.longitude, 1);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

            // Toast.makeText(getActivity(),fulladdress,Toast.LENGTH_LONG).show();
        }catch(Exception e)
        {

        }
        return address;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }
}
