package com.example.pothole.Citizen.image;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.pothole.Connectivity;
import com.example.pothole.R;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.channels.ConnectionPendingException;


public class ML_Connectivity extends AppCompatActivity {
    Socket s;
    DataOutputStream dos;
    String resp = "";
    ObjectOutputStream pw;
    String ans = null;
    double latitude,longitude;
    byte[] byteArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ml__connectivity);
        byteArray = getIntent().getByteArrayExtra("imageOfPothole");

        latitude = getIntent().getDoubleExtra("latitude", 45.0);
        longitude = getIntent().getDoubleExtra("longitude", 55.0);

        Log.i("shubham", "in ML__Connectivity");
        ProgressDialog progressDialog = new ProgressDialog(ML_Connectivity.this);
        progressDialog.setMessage("VALIDATING...");
        progressDialog.show();

        /*Log.i("shubham","in ML__CONNECTIVITY");
        byte[] byteArray = getIntent().getByteArrayExtra("imageOfPothole");

        //#####################ML########################
        Log.i("shubham","in c.execute1");
        try {
            s = new Socket("192.168.43.217", 7800);
            pw = new ObjectOutputStream(s.getOutputStream());
            pw.writeObject(byteArray);
            InputStreamReader isr=new InputStreamReader(s.getInputStream());
            BufferedReader br=new BufferedReader(isr);
            Log.i("shubham","in c.execute2");
            //received no of potholes
            resp= br.readLine();
            Log.i("shubham","evdhya lavkat");
            Log.i("shubhamRESPONSE",resp);
            pw.flush();
            pw.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("shubham","caught in exception"+e);
        }
        //#####################ML########################
        progressDialog.dismiss();
        Intent i2 = new Intent();
        Log.i("shubham","result from MLCONNECTIVITY IS"+resp);
        i2.putExtra("result",resp);
        setResult(RESULT_OK,i2);
        finish();*/


        /*try {
            Log.i("shubham","in ML CONNc.execute1");
            s = new Socket("192.168.43.217", 7800);
            progressDialog.show();

            Log.i("shubham","in ML CONNc.execute1.15");
            pw = new ObjectOutputStream(s.getOutputStream());
            Log.i("shubham","in c.execute1.25");
            progressDialog.dismiss();
            pw.writeObject(byteArray);
            Log.i("shubham","in c.execute1.5");
            InputStreamReader isr=new InputStreamReader(s.getInputStream());
            Log.i("shubham","in c.execute1.75");

            BufferedReader br=new BufferedReader(isr);
            Log.i("shubham","in c.execute2");
            //received no of potholes
            resp = br.readLine();

            Log.i("shubham","evdhya lavkat");
            Log.i("RESPONSEin connectivity",resp);
            pw.flush();
            pw.close();
            s.close();
            Intent i = new Intent(this, ImageFragment.class);
            i.putExtra("byteArrayHere", byteArray);
            i.putExtra("resp", resp);
            i.putExtra("latitude", latitude);
            i.putExtra("longitude", longitude);
            progressDialog.dismiss();
            startActivity(i);
            finish();


        } catch (Exception e) {
            e.printStackTrace();
            Log.i("shubham","error in ML_CONN"+e);
        }*/




        //Connectivity c = new Connectivity(ML_Connectivity.this);
        //  c.execute(byteArray);

        //  Log.i("shubham", "now c.execute(byte)done");
       /* if (byteArray != null)
        {
            Log.i("shubham","means byterray is not null");
            if (!(c.getResp()).equals(" ")) {
                c.getResp();
                Log.i("shubham", c.getResp() + "found");
                Log.i("shubham", "result from MLCONNECTIVITY IS" + resp);
                Intent i = new Intent(this, ImageFragment.class);
                i.putExtra("byteArrayHere", byteArray);
                i.putExtra("resp", resp);
                i.putExtra("latitude", latitude);
                i.putExtra("longitude", longitude);
                startActivity(i);
                finish();
            }
        Log.i("shubham", "resp is still null");
    }*/
    }
}
