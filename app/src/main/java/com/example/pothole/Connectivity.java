package com.example.pothole;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.pothole.Citizen.image.ImageFragment;
import com.example.pothole.Citizen.image.ML_Connectivity;
import com.example.pothole.Citizen.image.tempCallingActivity;
import com.example.pothole.login_signup.login;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Connectivity extends AsyncTask<byte [], Void, Void> {

    Socket s;
    DataOutputStream dos;
    String resp = "";
    ObjectOutputStream pw;
    String ans = null;
    ProgressDialog progressDialog;
    Context mycontext,tempContext;
    double latitude,longitude;
    byte[] byteArray;
    public Connectivity(Context context)
    {
        tempContext = context;
    }
    public Connectivity(Context context,double latitude,double longitude,byte[]byteArray){
        mycontext = context;
        resp=" ";
        this.latitude = latitude;
        this.longitude = longitude;
        this.byteArray = byteArray;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(mycontext);
        progressDialog.setMessage("LOADING...");
        progressDialog.show();
    }

    @Override
    protected Void doInBackground(byte []... voids) {
        byte[] byteArray = voids[0];
        try {
            Log.i("shubham","in c.execute1");
            s = new Socket("192.168.43.177", 7800);
            pw = new ObjectOutputStream(s.getOutputStream());
            pw.writeObject(byteArray);
            InputStreamReader isr=new InputStreamReader(s.getInputStream());
            BufferedReader br=new BufferedReader(isr);
            Log.i("shubham","in c.execute2");
            //received no of potholes
            resp = br.readLine();

            Log.i("shubham","evdhya lavkat");
            Log.i("RESPONSEin connectivity",resp);
            pw.flush();
            pw.close();
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("shubham","exception in connectivity.java"+e);
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
        if(resp.charAt(0)!='0'){
            Toast.makeText(mycontext,resp+" Potholes Detected",Toast.LENGTH_LONG).show();
        }
        else if(resp.equals(" "))
        {
            Toast.makeText(mycontext,"Server Down :(",Toast.LENGTH_LONG).show();
            return;
        }
        else{
            resp = "zero";
        }

        Intent i3 = new Intent(mycontext,tempCallingActivity.class);
        i3.putExtra("byteArrayHere", byteArray);
        i3.putExtra("resp", resp);
        i3.putExtra("latitude", latitude);
        i3.putExtra("longitude", longitude);
        mycontext.startActivity(i3);

    }
    public String getResp(){
        return resp;
    }
}