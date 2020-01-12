package com.example.pothole;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.baoyachi.stepview.VerticalStepView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class after_clicking_marker extends AppCompatActivity implements OnMapReadyCallback, Serializable {
    int status;
    String startdate,enddate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_clicking_marker);
        Intent i=getIntent();
        final String obj=(String) i.getSerializableExtra("co-ordinates");
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myref=database.getReference("pothole");
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot p:dataSnapshot.getChildren())
                {
                    if(obj.equals(p.getKey()))
                    {

                        status=p.child("statusFlag").getValue(Integer.class);
                        // Toast.makeText(getApplicationContext(),"The Status of the work is:"+status+" whose pothole id is:"+p.getKey(),Toast.LENGTH_LONG).show();
                        if(p.hasChild("startdate")&&p.hasChild("enddate"))
                        {
                            startdate=p.child("startdate").getValue(String.class);
                            enddate=p.child("enddate").getValue(String.class);
                        }
                        break;
                    }
                }
                VerticalStepView mSetpview0 = (VerticalStepView) findViewById(R.id.step_view0);

                List<String> list0 = new ArrayList<>();
                list0.add("step-1:Pothole assigned to Civil agencies.");
                list0.add("step-2:Work in progress.\nStartDate:"+startdate+"\nEstimated EndDate:"+enddate);
                list0.add("step-3:Work successfully completed by contractor.");
                list0.add("step-4:Confirmation pending by Government.");
                list0.add("step-5:Pothole work officially done. ");
                mSetpview0.setStepsViewIndicatorComplectingPosition(status)//设置完成的步数
                        .reverseDraw(false)//default is true
                        .setStepViewTexts(list0)//总步骤
                        .setTextSize(16)
                        .setLinePaddingProportion(1.7f)//设置indicator线与线间距的比例系数
                        .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(after_clicking_marker.this, android.R.color.holo_green_light))//设置StepsViewIndicator完成线的颜色
                        .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(after_clicking_marker.this, R.color.uncompleted_text_color))//设置StepsViewIndicator未完成线的颜色
                        .setStepViewComplectedTextColor(ContextCompat.getColor(after_clicking_marker.this, android.R.color.white))//设置StepsView text完成线的颜色
                        .setStepViewUnComplectedTextColor(ContextCompat.getColor(after_clicking_marker.this, R.color.uncompleted_text_color))//设置StepsView text未完成线的颜色
                        .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(after_clicking_marker.this, R.drawable.complted))//设置StepsViewIndicator CompleteIcon
                        .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(after_clicking_marker.this, R.drawable.default_icon))//设置StepsViewIndicator DefaultIcon
                        .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(after_clicking_marker.this, R.drawable.attention));

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        //mSetpview0.setStepsViewIndicatorComplectingPosition(status);
        //  Toast.makeText(getApplicationContext(),"The Status of the work is:"+status+" whose pothole id is:"+obj,Toast.LENGTH_LONG).show();
//        Intent s=new Intent(after_clicking_marker.this,Vtimeline.class);
//        startActivity(s);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

}