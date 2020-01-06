package com.example.pothole.ui.login_signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.pothole.MainActivity;
import com.example.pothole.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity {
    CheckBox userCheckBox;
    CheckBox citizenCheckBox;
    EditText username=null,password=null,cpassword=null;
    String   category=null;
    Button   btn_register;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userCheckBox = (CheckBox)findViewById(R.id.checkBoxCitizen);
        citizenCheckBox = (CheckBox)findViewById(R.id.checkBoxContractor);
        username  = (EditText)findViewById(R.id.et_lusername);
        password  = (EditText)findViewById(R.id.et_password);
        cpassword = (EditText)findViewById(R.id.et_cpassword);
        btn_register = (Button)findViewById(R.id.btn_register);

        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setMessage("Registration In Process...");

        mAuth = FirebaseAuth.getInstance();

        final String emailstr      =(username).getText().toString().trim();
        final String passwordstr   =(password).getText().toString().trim();
        final String conpasswordstr=(cpassword).getText().toString().trim();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                if(userCheckBox.isChecked())
                    category = "user";
                else if(citizenCheckBox.isChecked())
                    category = "contractor";
                else{
                    Toast.makeText(getApplicationContext(),"Please Select Category",Toast.LENGTH_LONG).show();
                    return;
                }
                progressDialog.show();

                mAuth.createUserWithEmailAndPassword(emailstr, passwordstr)
                        .addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(register.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(register.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    if(category.equals("user")) {
                                        reference = FirebaseDatabase.getInstance().getReference().child("user");
                                        reference.child("uid").setValue(mAuth.getCurrentUser().getUid());
                                    }
                                    else{
                                        reference = FirebaseDatabase.getInstance().getReference().child("user");
                                        reference.child("uid").setValue(mAuth.getCurrentUser().getUid());
                                    }
                                    progressDialog.dismiss();
                                    startActivity(new Intent(register.this, login.class));
                                    finish();
                                }
                            }
                        });





            }
        });
    }
}