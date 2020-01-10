package com.example.pothole.Contractor.notifications_C;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pothole.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static java.lang.Boolean.FALSE;

public class PotholeInfo_c extends AppCompatActivity {
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference ref;
    ImageView potholeImage;
    Spinner statusddlist,experiment;
    TextView startdate,lastdate;
    int flag=0,iflag=0,no;
    Button btnSubmitUpdatehere;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pothole_info_c);
        potholeImage=(ImageView)findViewById(R.id.potholeImage);
        statusddlist=(Spinner)findViewById(R.id.spinner);
        startdate=(TextView)findViewById(R.id.startdate);
        lastdate=(TextView)findViewById(R.id.lastdate);
        btnSubmitUpdatehere = (Button)findViewById(R.id.buttonSubmitUpdate);
        final String key = String.valueOf(getIntent().getStringExtra("selectedpotholeId"));
        ref = database.getReference("pothole/"+key);

        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Glide.with(getApplicationContext())
                        .load(dataSnapshot.child("potholeImageUid").getValue().toString())
                        .into(potholeImage);
                no = Objects.requireNonNull(dataSnapshot).child("statusFlag").getValue(Integer.class)-1;
                if(no+1==3)
                {
                    statusddlist.setEnabled(FALSE);
                    btnSubmitUpdatehere.setVisibility(View.GONE);
                }


                statusddlist.setSelection(no);
                if(no>=1)
                {
                    startdate.setText(Objects.requireNonNull(dataSnapshot.child("startdate").getValue()).toString());
                    lastdate.setText(Objects.requireNonNull(dataSnapshot.child("lastdate").getValue()).toString());
                    flag=1;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnSubmitUpdatehere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int status = statusddlist.getSelectedItemPosition() + 1;
                switch (status)
                {
                    case 1:
                        if(no==1)
                        {
                            statusddlist.setSelection(no);
                            Toast.makeText(PotholeInfo_c.this, "Cant go back!", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 2:
                        if(no==0)
                        {
                            Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                            String formattedDate = df.format(c);

                            Date referenceDate = new Date();
                            Calendar c1 = Calendar.getInstance();
                            c1.setTime(referenceDate);
                            c1.add(Calendar.MONTH, 1);
                            Date n = (Date) c1.getTime();
                            SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");
                            String formattedDate1 = df1.format(n);
                            ref.child("startdate").setValue(formattedDate);
                            ref.child("lastdate").setValue(formattedDate1);
                            ref.child("statusFlag").setValue(status);
                        }
                        break;
                    case 3:
                        if(no==0)
                        {
                            statusddlist.setSelection(no);
                            Toast.makeText(PotholeInfo_c.this, "First Complete stage 2", Toast.LENGTH_SHORT).show();
                        }
                        else if(no==1)
                        {
                            ref.child("statusFlag").setValue(status);
                            statusddlist.setEnabled(FALSE);
                            btnSubmitUpdatehere.setVisibility(View.GONE);
                        }
                        break;
                }
                finish();

//                int status = statusddlist.getSelectedItemPosition() + 1;
//
//                Log.i("Number", String.valueOf(status));
//                if (status!=3 && status!=2 &&no!=1 && (no)!=status) {
//                    Log.i("Numberstatus", String.valueOf(status));
//                    Log.i("Numberno", no+"");
//
//                    Date c = Calendar.getInstance().getTime();
//                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
//                    String formattedDate = df.format(c);
//
//                    Date referenceDate = new Date();
//                    Calendar c1 = Calendar.getInstance();
//                    c1.setTime(referenceDate);
//                    c1.add(Calendar.MONTH, 1);
//                    Date n = (Date) c1.getTime();
//                    SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");
//                    String formattedDate1 = df1.format(n);
//                    ref.child("startdate").setValue(formattedDate);
//                    ref.child("lastdate").setValue(formattedDate1);
//                    ref.child("statusFlag").setValue(status);
//                    iflag = 1;
//                    }
//                else if(status==3)
//                {
//                    ref.child("statusFlag").setValue(3);
//                    statusddlist.setEnabled(FALSE);
//                    btnSubmitUpdatehere.setVisibility(View.GONE);
//                }
            }
        });
    }
}
