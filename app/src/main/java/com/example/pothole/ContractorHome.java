package com.example.pothole;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ContractorHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contractor_home);
        BottomNavigationView navViewc = findViewById(R.id.nav_viewc);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfigurationc = new AppBarConfiguration.Builder(
                R.id.navigation_mapsc, R.id.navigation_notificationsc, R.id.navigation_accountc)
                .build();
        NavController navControllerc = Navigation.findNavController(this, R.id.nav_host_fragmentc);
        //NavigationUI.setupActionBarWithNavController(this, navControllerc, appBarConfiguration);
        NavigationUI.setupActionBarWithNavController(this,navControllerc,appBarConfigurationc);

        NavigationUI.setupWithNavController(navViewc, navControllerc);

    }
}
