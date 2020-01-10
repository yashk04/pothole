package com.example.pothole.Citizen.image;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pothole.Citizen.image.PotholeLocation;
import com.example.pothole.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.pothole.Citizen.image.PotholeLocation;
import com.example.pothole.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Potholesnearmy {
    Context mycontext;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mDatabase;
    //FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    ArrayList<PotholeLocation> potholeLocations = new ArrayList<PotholeLocation>();
    Upload_Upvote obj;
    Upload_Upvote obj1;
    private double latitude,longitude;
    boolean flag;
    int flagforterminate;
    ProgressDialog progressDialog1;

    public Potholesnearmy(double latitude,double longitude,Context mycontext){
        this.latitude = latitude;
        this.longitude = longitude;
        this.mycontext = mycontext;


    }

    public Upload_Upvote getPothole() {
        obj=new Upload_Upvote();
        obj1=new Upload_Upvote();
        flagforterminate = 0;
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_potholesnearme);
       firebaseDatabase =  FirebaseDatabase.getInstance();
        mDatabase = firebaseDatabase.getReference("pothole");
        progressDialog1 = new ProgressDialog(mycontext);
        progressDialog1.setMessage("In Potholes near me class");
        Log.i("shubhamoriginallat",latitude+"");
        Log.i("shubhamoriginallon",longitude+"");
        progressDialog1.show();
        mDatabase.addValueEventListener( new ValueEventListener(){
            public void onDataChange(DataSnapshot dataSnapshot) {
                flag=false;
                //an arraylist to save all the latitude and longitude values
                if(flagforterminate!=1) {
                    for (DataSnapshot post : dataSnapshot.getChildren()) {
                        // Iterate through all your users and get latitude and longitudea
                        // if(post.child("lat").getValue(double.class)!=null && post.child("longi").getValue(double.class)!=null )
                        // {
                        double latitud = post.child("lat").getValue(double.class);
                        double longitud = post.child("longi").getValue(double.class);
                        String potholeid = post.child("potholeUid").getValue(String.class);
                        Log.i("shubhampotholeid", potholeid);
                        Log.i("shubhamlatitude", latitud + "");
                        Log.i("shubhamlongitu", longitud + "");

                        // Toast.makeText(getApplicationContext(), Double.toString(longitud)+"Hi", Toast.LENGTH_LONG).show();
                        //store your latlang

                        if (distance(latitude, longitude, latitud, longitud) <= 50.0)//checking distance
                        {
                            // Toast.makeText(getApplicationContext(), Double.toString(latitud)+"hello", Toast.LENGTH_SHORT).show();
                            //go for upvote
                            Log.i("shubham", "inUpvote");
                            flag = true;
                            obj.setLati(latitud);
                            obj.setLongi(longitud);
                            obj.setPotholeid(potholeid);
                            flagforterminate = 1;
                            return;
                        } else {
                            //go for upload
                            Log.i("shubham", "inUpload");
                            Toast.makeText(mycontext, "mark lat and  map", Toast.LENGTH_LONG).show();
                            flag = false;
                            obj1.setLongi(0.0);
                            obj1.setLati(0.0);
                            obj1.setPotholeid(potholeid);
                        }
                        //}
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        if(flag==false)//return upload object
        {
            progressDialog1.dismiss();
            return obj1;
        }
        else//return upvote object
        {
            progressDialog1.dismiss();
            return obj;
        }
    }

    public float distance(double currentlatitude, double currentlongitude, double originLat, double originLon) {

        float[] results = new float[1];
        Location.distanceBetween(currentlatitude, currentlongitude, originLat, originLon, results);
        float distanceInMeters = results[0];
        //  Toast.makeText(getApplicationContext(),Float.toString(distanceInMeters)+"tu",Toast.LENGTH_LONG).show();
        return distanceInMeters;
    }
}

