package com.example.pothole.Contractor.maps_C;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.pothole.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MapLoad extends FragmentActivity implements OnMapReadyCallback {
    FirebaseDatabase ref;
    FirebaseAuth mAuth;
    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_load);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String cid=user.getUid();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference potuidRef=database.getReference("contractor").child(cid).child("allotedPothole");
        potuidRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Map<String, Object> tripHash = (HashMap<String,Object>) dataSnapshot.getValue();
                if(tripHash != null) {
                    final JSONObject tripJson = new JSONObject(tripHash);

                    for (final String key : tripHash.keySet()) {
                        plotOnMap(key);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void plotOnMap( String key)
    {
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference potRefinfo=database.getReference("pothole").child(key);
        potRefinfo.addValueEventListener(new ValueEventListener() {
            double longi;
            double lat;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final CameraUpdate cu;
                longi=dataSnapshot.child("longi").getValue(double.class);
                lat=dataSnapshot.child("lat").getValue(double.class);
                LatLng plot = new LatLng(lat,longi);
                mMap.addMarker(new MarkerOptions().position(plot).title("Pothole Alloted").snippet(dataSnapshot.child("fullAddress").getValue(String.class)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(plot,10f));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
