package com.example.pothole;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button enterNewWorld;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        enterNewWorld=findViewById(R.id.enter);
        enterNewWorld.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(), CitizenHome.class);
                startActivity(i);
            }
        });
    }
}
