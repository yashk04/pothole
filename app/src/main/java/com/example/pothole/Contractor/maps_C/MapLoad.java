package com.example.pothole.Contractor.maps_C;

import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MapLoad extends FragmentActivity implements OnMapReadyCallback {
    FirebaseDatabase ref;
    FirebaseAuth mAuth;
    private GoogleMap mMap;
    int flag3=0;
    SupportMapFragment mapFragment;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_load);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Log.i("tejas","on create called");
        flag3 = 0;
        //getCallingActivity()
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
        Log.i("tejas","map ready");

        DatabaseReference potuidRef=FirebaseDatabase.getInstance().getReference("contractor")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());//.child("allotedPothole");

        potuidRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // if(flag3 !=1)
                // {
                if(dataSnapshot.hasChild("allotedPothole")){
                    Log.i("tejas", "ekdach yaycha");
                    final HashMap<String, Object> tripHash = (HashMap<String, Object>) dataSnapshot.child("allotedPothole").getValue();
                    Log.i("tejassize",tripHash.size()+"");
                    if (tripHash != null) {
                        //final JSONObject tripJson = new JSONObject(tripHash);
                        List<String> onlyIds = new ArrayList<String>(tripHash.keySet());

                        for (int i=0;i<onlyIds.size();i++) {

                            Log.i("tejas id",onlyIds.get(i));
                            DatabaseReference potRefinfo = FirebaseDatabase.getInstance().getReference("pothole").child(onlyIds.get(i));
                            potRefinfo.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    final CameraUpdate cu;
                                    double longi = dataSnapshot.child("longi").getValue(double.class);
                                    double lat = dataSnapshot.child("lat").getValue(double.class);
                                    Log.i("tejaslat",lat+"");
                                    Log.i("tejaslongi",longi+"");
                                    LatLng plot = new LatLng(lat, longi);
                                    mMap.addMarker(new MarkerOptions().position(plot).title("Pothole Alloted"));//.snippet(dataSnapshot.child("fullAddress").getValue(String.class)));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(plot, 10f));
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        flag3=1;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}