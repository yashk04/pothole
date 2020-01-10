package com.example.pothole.Contractor.notifications_C;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
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
import java.util.HashMap;
import java.util.Map;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    LinearLayout CardHolder;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications_c, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        CardHolder = root.findViewById(R.id.tripCardHolder);

        fetchTrips();
        return root;
    }
    public void fetchTrips(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference ref = database.getReference("contractor/"+user.getUid()+"/allotedPothole");     //contractor ka user mill gaya
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Map<String, Object> tripHash = (HashMap<String,Object>) dataSnapshot.getValue();

                if(tripHash != null) {
                    final JSONObject tripJson = new JSONObject(tripHash);
                    Log.i("tripHash",tripHash.toString());
                    Log.i("Key",tripHash.keySet().toString());
                    CardHolder.removeAllViews();
                    if(getContext() != null) {
                        int p = 0;
                        for (final String key : tripHash.keySet()) {
                            String get=dataSnapshot.child(key).child("potholeImageUid").getValue().toString();
                            try {
                                createCardLayout(tripJson.getJSONObject(key), key, p, tripHash.keySet().size() - 1,get);       //ref.child(key).child("potholeImageUid").toString()
                                //Toast.makeText(getContext(),ref.child(key).child("potholeImageUid").toString() , Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            p++;
                        }

                        for (int i = 0; i < CardHolder.getChildCount(); i++) {
                            final int finalI = i;
                            CardHolder.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Intent intent = new Intent(getContext(), PotholeInfo_c.class);
                                    intent.putExtra("selectedpotholeId", String.valueOf(CardHolder.getChildAt(finalI).getTag()));
                                    startActivity(intent);
                                }
                            });
                        }
                    }

                }else{
                    CardHolder.removeAllViews();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void createCardLayout(final JSONObject tripJson, String key, int p, int i,String photo){
        Uri uri=Uri.parse(photo);
        //Toast.makeText(getContext(), uri.toString(), Toast.LENGTH_SHORT).show();
        CardView cardView = new CardView(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.card_height));
        cardView.setLayoutParams(params);
        ViewGroup.MarginLayoutParams params1 = new ViewGroup.MarginLayoutParams(cardView.getLayoutParams());
        if(p == i) {
            params1.setMargins((int) getResources().getDimension(R.dimen.card_marginLeft), 0, (int) getResources().getDimension(R.dimen.card_marginRight), (int) getResources().getDimension(R.dimen.card_marginBottom2));
        }else{
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
        params4.setMargins((int) getResources().getDimension(R.dimen.card_tripName_marginLeft),(int) getResources().getDimension( R.dimen.card_tripName_marginTop),0, 0);
        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(params4);
        tripName.setLayoutParams(layoutParams1);
        tripName.setText(key);
        tripName.setTypeface(tripName.getTypeface(), Typeface.BOLD);
        tripName.setTextColor(Color.parseColor("#424242"));
        tripName.setTextSize(19);

//        TextView creatorName = new TextView(getContext());
//        LinearLayout.LayoutParams params5 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        creatorName.setLayoutParams(params5);
//        ViewGroup.MarginLayoutParams params6 = new ViewGroup.MarginLayoutParams(creatorName.getLayoutParams());
//        params6.setMargins((int) getResources().getDimension(R.dimen.card_createdBy_marginLeft),(int) getResources().getDimension( R.dimen.card_createdBy_marginTop),0, 0);
//        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(params6);
//        creatorName.setLayoutParams(layoutParams2);
//        creatorName.setText("By "+FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
//        creatorName.setTextSize(14);

//        final LinearLayout sharedUsersHolder = new LinearLayout(getContext());
//        LinearLayout.LayoutParams paramsy = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.card_sharedUserHolder_image_height));
//        sharedUsersHolder.setLayoutParams(paramsy);
//        ViewGroup.MarginLayoutParams paramsi = new ViewGroup.MarginLayoutParams(sharedUsersHolder.getLayoutParams());
//        paramsi.setMargins((int) getResources().getDimension(R.dimen.card_sharedUserHolder_image_marginLeft),(int) getResources().getDimension( R.dimen.card_sharedUserHolder_image_marginTop),0, 0);
//        RelativeLayout.LayoutParams layoutParamsi = new RelativeLayout.LayoutParams(paramsi);
//        sharedUsersHolder.setLayoutParams(layoutParamsi);
//        sharedUsersHolder.setOrientation(LinearLayout.HORIZONTAL);
//        sharedUsersHolder.setGravity(Gravity.CENTER_VERTICAL);


//        TextView itemsCount = new TextView(getContext());
//        LinearLayout.LayoutParams params7 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        itemsCount.setLayoutParams(params7);
//        ViewGroup.MarginLayoutParams params8 = new ViewGroup.MarginLayoutParams(itemsCount.getLayoutParams());
//        params8.setMargins((int) getResources().getDimension(R.dimen.card_items_marginLeft),(int) getResources().getDimension( R.dimen.card_items_marginTop),0, 0);
//        RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(params8);
//        itemsCount.setLayoutParams(layoutParams3);
//        try {
//            itemsCount.setText("Featuring: "+tripJson.getString("noOfItems")+" items");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        itemsCount.setTextSize(14);

        cardView.addView(imageView);
        cardView.addView(tripName);
//        cardView.addView(creatorName);
//        cardView.addView(sharedUsersHolder);
//        cardView.addView(itemsCount);

        CardHolder.addView(cardView);
    }
}