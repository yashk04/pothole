package com.example.pothole.Citizen.maps_citizen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.pothole.R;

public class MapsFragment extends Fragment {

    private MapsViewModel mapsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mapsViewModel =
                ViewModelProviders.of(this).get(MapsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_maps, container, false);
        final TextView textView = root.findViewById(R.id.text_maps);
        /*mapsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        setHasOptionsMenu(true);
        Intent i =new Intent(getActivity(),MapsActivity.class);
        startActivity(i);


        return root;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.map_option, menu);
        // return super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.map_option, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.btnReportedMe:
                Toast.makeText(getActivity(),"You will see your reports",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.btnNearMe:
                Toast.makeText(getActivity(),"You will see near by reports",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}