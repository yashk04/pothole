package com.example.pothole.Citizen.image;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.pothole.Connectivity;
import com.example.pothole.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class ImageFragment extends Fragment {
    private StorageReference mStorageRef;
    public static final int REQUESTCODE = 999;
    public static final int RequestPermissionCode = 1;
    public static final int RequestPermissionCodeGPS = 2;
    private ImageViewModel imageViewModel;
    ImageView afterphototaken;
    Bitmap bitmap;
    LinearLayout content;
    Button btntakephoto;
    Button btnsubmit;
    Button btngallery;
    Uri imageURi;
    double longitude;
    LocationTrack locationTrack;
    double latitude;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    DatabaseReference databaseReference=firebaseDatabase.getReference().child("/Pothole");
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        imageViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);
        View root = inflater.inflate(R.layout.fragment_image, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        /*imageViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        btntakephoto = (Button) root.findViewById(R.id.btnimage);
        btnsubmit = (Button) root.findViewById(R.id.btnsubmit);
        content = (LinearLayout) root.findViewById(R.id.content);
        afterphototaken = (ImageView) root.findViewById(R.id.imageView);
        btngallery = (Button) root.findViewById(R.id.btngallery);


        EnableRuntimePermission();
        btngallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, 100);
            }
        });

        btntakephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(getActivity(),"You have already given permission",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getActivity(),"Hello", Toast.LENGTH_LONG).show();
                    requestGPSPermission();
                }
                try{
                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 7);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
              locationTrack = new LocationTrack(getActivity());
                if (locationTrack.canGetLocation()) {


                    longitude = locationTrack.getLongitude();
                    latitude = locationTrack.getLatitude();


                    Toast.makeText(getActivity(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
                    PotholeLocation pht=new PotholeLocation(1,latitude,longitude,1,1,"abc");
                    // String id  = databaseReference.push().getKey();

                    pht.setFlag(1);
                    pht.setLat(latitude);
                    pht.setLongi(longitude);
                    pht.setPhotoid(1);
                    pht.setStatus(1);
                    pht.setUserid("abc");
                    //databaseReference.setValue(pht);

                } else {
                    locationTrack.showSettingsAlert();
                }

            }
        });

        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                    byte[] byteArray = stream.toByteArray();

                    Connectivity c = new Connectivity(getContext());
                    c.execute(byteArray);
//                    mStorageRef= FirebaseStorage.getInstance().getReference("Pothole_photos").child("pothole.jpg");
//                    StorageReference mstore=mStorageRef.child("Pothole_photos/pothole.jpg");
//                    UploadTask uploadTask = mStorageRef.putBytes(byteArray);
//                    uploadTask.addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception exception) {
//                            // Handle unsuccessful uploads
//                        }
//                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//
//                        }
//                    });

//                    //$$$$$$$$$$$$$$$$$$$data(location) goes into database
                    PotholeLocation pht=new PotholeLocation(1,latitude,longitude,1,1,"abc");
                    databaseReference.setValue(pht);


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Server Down! :(", Toast.LENGTH_SHORT).show();
                }
            }

        });
        return root;
    }

    private void requestGPSPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(this.getActivity()), Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Permission needed!").setMessage("To access your location")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, RequestPermissionCodeGPS);

                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create().show();
        } else {
            ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, RequestPermissionCodeGPS);

        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7 && resultCode == RESULT_OK) {

            bitmap = (Bitmap) data.getExtras().get("data");
            // bitmap.compress(Bitmap.CompressFormat.JPEG, 0 , out);
            afterphototaken.setImageBitmap(bitmap);
        } else if (requestCode == 100 && resultCode == RESULT_OK) {
            imageURi = data.getData();
            afterphototaken.setImageURI(imageURi);
        }
    }

    public void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(this.getActivity()),
                Manifest.permission.CAMERA)) {

            Toast.makeText(this.getActivity(), "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(this.getActivity(), new String[]{

                    Manifest.permission.CAMERA}, RequestPermissionCode);

        }

    }

    @Override
    public void onRequestPermissionsResult(int RC, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(RC, permissions, grantResults);
        switch (RC) {
            case RequestPermissionCode:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this.getActivity(), "Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this.getActivity(), "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();
                }
                break;
            case 2:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this.getActivity(), "Permission Granted", Toast.LENGTH_LONG).show();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this.getActivity(), "Permission Canceled", Toast.LENGTH_LONG).show();
                }
        }

    }
}