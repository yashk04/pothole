package com.example.pothole.Contractor.account_C;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.pothole.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountFragment extends Fragment {

    private AccountViewModel accountViewModel;
    DatabaseReference rootRef, demoRef;
    FirebaseUser mUser;
    FirebaseAuth mAuth;
    TextView txtemail;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accountViewModel =
                ViewModelProviders.of(this).get(AccountViewModel.class);
        View root = inflater.inflate(R.layout.fragment_account, container, false);
        final TextView textView = root.findViewById(R.id.text_account);

        txtemail=(TextView)root.findViewById(R.id.txtemail);

        //Fetching of data done here
        mUser=FirebaseAuth.getInstance().getCurrentUser();      //gives current user UID
        rootRef = FirebaseDatabase.getInstance().getReference("contractor").child(mUser.getUid());
        //Toast.makeText(getActivity(),mUser.getUid(), Toast.LENGTH_SHORT).show();
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String email=dataSnapshot.child("email").getValue(String.class);
                txtemail.setText(email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return root;
    }
}