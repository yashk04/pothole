package com.example.pothole.login_signup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pothole.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static com.google.firebase.auth.FirebaseAuth.*;

public class register extends AppCompatActivity {
    CheckBox userCheckBox;
    CheckBox contractorCheckBox;
    EditText username,password,cpassword,fullname;
    String   category=null;
    Button   btn_register_here;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userCheckBox = (CheckBox)findViewById(R.id.checkBoxCitizen);
        contractorCheckBox = (CheckBox)findViewById(R.id.checkBoxContractor);
        fullname  = (EditText)findViewById(R.id.txtname);
        username=(EditText)findViewById(R.id.txtid);
        password  = (EditText)findViewById(R.id.txtpassword);
        cpassword = (EditText)findViewById(R.id.txtconfirmPassword);
        btn_register_here = (Button)findViewById(R.id.btnSignup);

        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setMessage("Registration In Process...");

        mAuth = FirebaseAuth.getInstance();

        userCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contractorCheckBox.setChecked(false);
            }
        });

        contractorCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userCheckBox.setChecked(false);
            }
        });


        btn_register_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailstr      =(username).getText().toString().trim();
                final String passwordstr   =(password).getText().toString().trim();
                final String conpasswordstr=(cpassword).getText().toString().trim();
                final String fname=(fullname).getText().toString().trim();
                if (TextUtils.isEmpty(emailstr)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(passwordstr)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (passwordstr.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!passwordstr.equals(conpasswordstr)){
                    Toast.makeText(getApplicationContext(), "Both the Passwords should be same!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(userCheckBox.isChecked())
                    category = "user";
                else if(contractorCheckBox.isChecked())
                    category = "contractor";
                else{
                    Toast.makeText(getApplicationContext(),"Please Select Category",Toast.LENGTH_LONG).show();
                    return;
                }


                mAuth.createUserWithEmailAndPassword(emailstr, passwordstr)
                        .addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(register.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(register.this, "createUserWithEmail:onComplete:", Toast.LENGTH_SHORT).show();
                                    if(category.equals("user")) {
                                        reference = FirebaseDatabase.getInstance().getReference().child("user");
                                        (reference.child(getInstance().getUid())).child("full name").setValue(fname);
                                        (reference.child(getInstance().getUid())).child("email").setValue(emailstr);
                                        (reference.child(getInstance().getUid())).child("points").setValue(0);
                                    }
                                    else{
                                        reference = FirebaseDatabase.getInstance().getReference().child("contractor");
                                        (reference.child(getInstance().getUid())).child("email").setValue(emailstr);
                                        (reference.child(getInstance().getUid())).child("busyFlag").setValue(0);
                                    }
                                    startActivity(new Intent(getApplicationContext(), login.class));
                                    finish();
                                }
                            }
                        });




            }
        });
    }
}