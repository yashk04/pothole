package com.example.pothole.Citizen.notifications;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.pothole.Contractor.notifications_C.PotholeInfo_c;

import com.example.pothole.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;


//upvote Pothole update baki hai        $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$


public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    LinearLayout CardHolder;
    TextView txtnotice;
    int phere=0;
    int index=0;
    ArrayList<String>idObj;
    ArrayList<Integer>idObjFlag;
    ArrayList<String>idObjPhotos;

    int l=0;
    int count2=0;
    int h=0;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        phere = 0;
        idObj = new ArrayList<String>();
        idObjFlag = new ArrayList<>();
        idObjPhotos = new ArrayList<>();
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        /*notificationsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        CardHolder = root.findViewById(R.id.tripCardHolder1);
        txtnotice=root.findViewById(R.id.txtnotice);
        txtnotice.setVisibility(View.INVISIBLE);
        CardHolder.removeAllViews();
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("uploadedPothole"))
                {
                    DatabaseReference ref1 = ref.child("uploadedPothole");
                    ref1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int ihere = (int)dataSnapshot.getChildrenCount()-1;
                            Log.i("tejas",(ihere+1)+"");
                            int k=0;
                            for(DataSnapshot snaps : dataSnapshot.getChildren())
                            {
                                phere++;
                                idObj.add(snaps.getKey());
                                Log.i("tejas should get ids",idObj.get(k));
                                k++;
                                //createCardLayout(snaps.getKey(),phere,ihere);
                            }
                            if(!idObj.isEmpty()) {
                                Log.i("tejas", "id is not empty");
                                Log.i("tejas", "list length" + idObj.size());
                                DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference().child("pothole");

                                ref3.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {

                                        for (l = 0; l < idObj.size(); l++) {
                                            String tempUrl = dataSnapshot3.child(idObj.get(l)).child("potholeImageUid").getValue(String.class);
                                            idObjPhotos.add(l,tempUrl);
                                            DataSnapshot dataSnapshot4 = dataSnapshot3.child(idObj.get(l)).child("statusFlag");
                                            Log.i("tejas l value", l + "");
                                            int m = dataSnapshot4.getValue(Integer.class);
                                            if (m == 4) {
                                                Log.i("tejas", "added 1 at " + l);
                                                idObjFlag.add(l, 1);
                                                count2++;
                                            } else {
                                                Log.i("tejas", "added 0 at " + l);
                                                idObjFlag.add(l, 0);
                                            }
                                        }
                                        int count=0;
                                        for(h=0;h<idObjFlag.size();h++)
                                        {
                                            Log.i("tejas iterator",idObjFlag.get(h)+"");

                                            if(idObjFlag.get(h)==1)
                                            {
                                                //createCardLayout(idObj.get(h),count,(count2-1));

                                                //########
                                                String photo = idObjPhotos.get(h);
                                                final CardView cardView = new CardView(getContext());
                                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.card_height));
                                                cardView.setLayoutParams(params);
                                                ViewGroup.MarginLayoutParams params1 = new ViewGroup.MarginLayoutParams(cardView.getLayoutParams());
                                                if (count == (count2-1)) {
                                                    params1.setMargins((int) getResources().getDimension(R.dimen.card_marginLeft), 0, (int) getResources().getDimension(R.dimen.card_marginRight), (int) getResources().getDimension(R.dimen.card_marginBottom2));
                                                } else {
                                                    params1.setMargins((int) getResources().getDimension(R.dimen.card_marginLeft), 0, (int) getResources().getDimension(R.dimen.card_marginRight), (int) getResources().getDimension(R.dimen.card_marginBottom1));
                                                }
                                                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(params1);
                                                cardView.setLayoutParams(layoutParams);
                                                cardView.setTag(idObj.get(h));

                                                ImageView imageView = new ImageView(getContext());
                                                LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension((R.dimen.card_image_height)));
                                                imageView.setLayoutParams(params2);
                                                Glide.with(getContext())
                                                        .load(photo)
                                                        .into(imageView);
                                                imageView.setScaleType(ImageView.ScaleType.FIT_XY);

                                                TextView tripName = new TextView(getContext());
                                                LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                                tripName.setLayoutParams(params3);
                                                ViewGroup.MarginLayoutParams params4 = new ViewGroup.MarginLayoutParams(tripName.getLayoutParams());
                                                params4.setMargins((int) getResources().getDimension(R.dimen.card_tripName_marginLeft), (int) getResources().getDimension(R.dimen.card_tripName_marginTop), 0, 0);
                                                RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(params4);
                                                tripName.setLayoutParams(layoutParams1);
                                                tripName.setText("Pothole " + (count));
                                                tripName.setTypeface(tripName.getTypeface(), Typeface.BOLD);
                                                tripName.setTextColor(Color.parseColor("#424242"));
                                                tripName.setTextSize(19);

                                                cardView.addView(imageView);
                                                cardView.addView(tripName);


                                                CardHolder.addView(cardView);
                                                cardView.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        Intent intent = new Intent(getContext(), PotholeFeedback.class);
                                                        intent.putExtra("selectedpotholeId", cardView.getTag().toString());
                                                        startActivity(intent);
                                                    }
                                                });
                                                //#########
                                                count++;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else {
                    txtnotice.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        return root;
    }

    public void createCardLayout(final String key, final int p, final int i) {


        String photo = "shubham";
        CardView cardView = new CardView(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.card_height));
        cardView.setLayoutParams(params);
        ViewGroup.MarginLayoutParams params1 = new ViewGroup.MarginLayoutParams(cardView.getLayoutParams());
        if (p == i) {
            params1.setMargins((int) getResources().getDimension(R.dimen.card_marginLeft), 0, (int) getResources().getDimension(R.dimen.card_marginRight), (int) getResources().getDimension(R.dimen.card_marginBottom2));
        } else {
            params1.setMargins((int) getResources().getDimension(R.dimen.card_marginLeft), 0, (int) getResources().getDimension(R.dimen.card_marginRight), (int) getResources().getDimension(R.dimen.card_marginBottom1));
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(params1);
        cardView.setLayoutParams(layoutParams);
        cardView.setTag(key);

        ImageView imageView = new ImageView(getContext());
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension((R.dimen.card_image_height)));
        imageView.setLayoutParams(params2);
        Glide.with(getContext())
                .load(photo)
                .into(imageView);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        TextView tripName = new TextView(getContext());
        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tripName.setLayoutParams(params3);
        ViewGroup.MarginLayoutParams params4 = new ViewGroup.MarginLayoutParams(tripName.getLayoutParams());
        params4.setMargins((int) getResources().getDimension(R.dimen.card_tripName_marginLeft), (int) getResources().getDimension(R.dimen.card_tripName_marginTop), 0, 0);
        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(params4);
        tripName.setLayoutParams(layoutParams1);
        tripName.setText("Pothole " + p);
        tripName.setTypeface(tripName.getTypeface(), Typeface.BOLD);
        tripName.setTextColor(Color.parseColor("#424242"));
        tripName.setTextSize(19);

        cardView.addView(imageView);
        cardView.addView(tripName);


        CardHolder.addView(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PotholeFeedback.class);
                intent.putExtra("selectedpotholeId", key);
                startActivity(intent);
            }
        });

    }





}