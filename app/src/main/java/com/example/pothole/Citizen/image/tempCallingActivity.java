package com.example.pothole.Citizen.image;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.pothole.CitizenHome;
import com.example.pothole.R;

public class tempCallingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_calling);
        Log.i("shubham","tempcALLING came");

        double latitude = getIntent().getDoubleExtra("latitude",45);
        double longitude = getIntent().getDoubleExtra("longitude",55);
        byte[] byteArray = getIntent().getByteArrayExtra("byteArrayHere");
        String resp       = getIntent().getStringExtra("resp");

        Intent i3 = new Intent(this, CitizenHome.class);
        i3.putExtra("byteArrayHere", byteArray);
        i3.putExtra("resp", resp);
        i3.putExtra("latitude", latitude);
        i3.putExtra("longitude", longitude);
        startActivity(i3);
        finish();
    }
}
