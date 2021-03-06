package com.example.pothole.ui.login_signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.pothole.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {

    FirebaseAuth mAuth;
    Button btn_login_here,btn_register_here;
    EditText username_here,password_here;
    DatabaseReference ref;
    Context mycontext;
    String password_str;
    String email_str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        ref   = FirebaseDatabase.getInstance().getReference();
        btn_register_here = (Button)findViewById(R.id.btn_lregister);
        btn_login_here = (Button)findViewById(R.id.btn_llogin);

        username_here = (EditText)findViewById(R.id.et_lusername);
        password_here = (EditText)findViewById(R.id.et_lpassword);

        btn_register_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this,register.class));
            }
        });


        btn_login_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               email_str = username_here.getText().toString().trim();
               password_str = password_here.getText().toString().trim();

                if (TextUtils.isEmpty(email_str)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password_str)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                decideCategoryLogin();
            }
        });
    }
    public void decideCategoryLogin()
    {
        mAuth.signInWithEmailAndPassword(email_str, password_str)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Authentication Failure.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            ref.child("user").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild(uid))
                                    {
                                        Toast.makeText(getApplicationContext(),"Citizen Login",Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            ref.child("contractor").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild(uid))
                                    {
                                        Toast.makeText(getApplicationContext(),"Contractor Login",Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });

    }

}



