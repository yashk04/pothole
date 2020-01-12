package com.example.pothole.Citizen.notifications;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.pothole.CitizenHome;
import com.example.pothole.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PotholeFeedback extends AppCompatActivity {


    Button getRating;
    RatingBar ratingBar;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pothole_feedback);
        getRating = (Button) findViewById(R.id.getRating);
        ratingBar= (RatingBar) findViewById(R.id.rating);
        getRating.setOnClickListener(new View.OnClickListener() {
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            final DatabaseReference ref = database.getReference("user/"+user.getUid());
            @Override
            public void onClick(View v) {
                String rating;
                rating=Float.toString(ratingBar.getRating());
                //int rate;
//                Toast.makeText(Rating.this, rating, Toast.LENGTH_LONG).show();
                // rate=Integer.parseInt(rating);
                //  FirebaseAuth=
                String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                final String puid= String.valueOf(getIntent().getStringExtra("selectedpotholeId"));//Note: the assigning of pothole id is yet to be done
                firebaseDatabase=FirebaseDatabase.getInstance();
                databaseReference=firebaseDatabase.getReference("feedback");
                databaseReference.child(uid).child(puid).setValue(rating).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.child("uploadedPothole").hasChild(puid))
                                {
                                    ref.child("uploadedPothole").child(puid).removeValue(new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                            Toast.makeText(PotholeFeedback.this, "Submitted Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    startActivity(new Intent(getApplicationContext(), CitizenHome.class));
                                    finish();
                                }
                                else if(dataSnapshot.child("upvotedPothole").hasChild(puid))
                                {
                                    ref.child("upvotedPothole").child(puid).removeValue(new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                            Toast.makeText(PotholeFeedback.this, "Submitted Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    startActivity(new Intent(getApplicationContext(), CitizenHome.class));
                                    finish();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        finish();

                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PotholeFeedback.this,"Feedback submissoin failed",Toast.LENGTH_LONG);

                            }
                        });

            }
        });


    }


}