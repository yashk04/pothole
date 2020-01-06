package com.example.pothole;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
    Context mycontext;

    public Connectivity(Context context){
        mycontext = context;
        resp=" ";
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
            s = new Socket("192.168.43.217", 7800);
            pw = new ObjectOutputStream(s.getOutputStream());
            pw.writeObject(byteArray);
            InputStreamReader isr=new InputStreamReader(s.getInputStream());
            BufferedReader br=new BufferedReader(isr);

            //received no of potholes
            resp= br.readLine();

            Log.i("shubham","evdhya lavkat");
            Log.i("RESPONSEin connectivity",resp);
            pw.flush();
            pw.close();
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
        if(resp.charAt(0)!='0')
            Toast.makeText(mycontext,resp+" Potholes Detected",Toast.LENGTH_LONG).show();
        else if(resp.equals(" "))
        {
            Toast.makeText(mycontext,"Server Down :(",Toast.LENGTH_LONG).show();
        }
    }
    public String getResp(){
        return resp;
    }
}