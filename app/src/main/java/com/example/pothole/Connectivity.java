package com.example.pothole;

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
    String resp = null;
    ObjectOutputStream pw;


    @Override
    protected Void doInBackground(byte []... voids) {
        byte[] byteArray = voids[0];
        try {
            s = new Socket("192.168.43.217", 7800);
            pw = new ObjectOutputStream(s.getOutputStream());
            pw.writeObject(byteArray);
            InputStreamReader isr=new InputStreamReader(s.getInputStream());
            BufferedReader br=new BufferedReader(isr);
            resp= br.readLine();
            Log.i("RESPONSE",resp);
            pw.flush();
            pw.close();
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }
}
