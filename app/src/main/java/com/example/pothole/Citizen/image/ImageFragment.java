package com.example.pothole.Citizen.image;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.pothole.Connectivity;
import com.example.pothole.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class ImageFragment extends Fragment {
    FirebaseAuth mAuth;
    int flagforterminate = 0;
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
    DatabaseReference databaseReference;
    String imageUrl,tempPotholeUid;
    ProgressDialog progressDialog;
    int flagForUpvoteOrUpload;
    int temppoints;
    Upload_Upvote obj;
    Potholesnearmy pnm;
    boolean flag;
    double mainLatitude,mainLongitude;
    Upload_Upvote objUpvote,objUpload;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        imageViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);
       final View root = inflater.inflate(R.layout.fragment_image, container, false);
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
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    final byte[] byteArray = stream.toByteArray();

                    //##########ANAND'S CODE FOR DECIDING UPVOTE OR UPLOAD....##########
                    //decide flagForUpvoteOrUpload ..1 if upload and 0 if upvote
                   //obj=new Upload_Upvote();
                   // pnm = new Potholesnearmy(latitude,longitude,getContext());
                   // obj=pnm.getPothole();

                    progressDialog = new ProgressDialog(getContext());
                    progressDialog.setMessage("Processing...");
                    progressDialog.show();
                   // obj = getPothole();
                    objUpvote=new Upload_Upvote();
                    objUpload=new Upload_Upvote();

                   DatabaseReference firebaseDatabase =  FirebaseDatabase.getInstance().getReference();

//        progressDialog1 = new ProgressDialog(mycontext);
//        progressDialog1.setMessage("In Potholes near me class");
                    Log.i("shubhamoriginallat",latitude+"");
                    Log.i("shubhamoriginallon",longitude+"");

                    firebaseDatabase.child("pothole").addListenerForSingleValueEvent( new ValueEventListener(){
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            flag=false;
                            Log.i("shubham","Inchecker");
                           // progressDialog.show();
                            //an arraylist to save all the latitude and longitude values

                                for (DataSnapshot post : dataSnapshot.getChildren()) {
                                    // Iterate through all your users and get latitude and longitudea
                                    // if(post.child("lat").getValue(double.class)!=null && post.child("longi").getValue(double.class)!=null )
                                    // {
                             if(flagforterminate!=1) {
                                    double latitud = post.child("lat").getValue(double.class);
                                    double longitud = post.child("longi").getValue(double.class);
                                    String potholeid = post.child("potholeUid").getValue(String.class);
                                    Log.i("shubhampotholeid", potholeid);
                                    Log.i("shubhamlatitude", latitud + "");
                                    Log.i("shubhamlongitu", longitud + "");
                                    if (distance(latitude, longitude, latitud, longitud) <= 5.0)//checking distance
                                    {
                                        // Toast.makeText(getApplicationContext(), Double.toString(latitud)+"hello", Toast.LENGTH_SHORT).show();
                                        //go for upvote
                                        Log.i("shubham", "inUpvote");
                                        flag = true;
                                        objUpvote.setLati(latitud);
                                        objUpvote.setLongi(longitud);
                                        objUpvote.setPotholeid(potholeid);
                                        flagforterminate = 1;
                                        break;
                                    } else {
                                        //go for upload
                                        Log.i("shubham", "inUpload");
                                        Toast.makeText(getContext(), "mark lat and  map", Toast.LENGTH_LONG).show();
                                        flag = false;
                                        objUpload.setLongi(0.0);
                                        objUpload.setLati(0.0);
                                        objUpload.setPotholeid(potholeid);
                                    }
                                    //}
                                }
                            }
                            if(flag==false)//return upload object
                            {
                                Log.i("shubhamassigningobj","in Upload");
                                obj=objUpload;
                            }
                            else//return upvote object
                            {
                                Log.i("shubhamassigningobj","in Upvote");
                                obj=objUpvote;
                            }
                            Log.i("shubhamanandLat",obj.getLati()+"");
                            Log.i("shubhamanandLong",obj.getLongi()+"");
                            mainLatitude = obj.getLati();
                            mainLongitude= obj.getLongi();

                            if(obj.getLati()!=0.0 && obj.getLongi()!=0.0)
                            {
                                //Upvoted;
                                Toast.makeText(getContext(),"Pothole present already,so upvoted",Toast.LENGTH_LONG).show();
                                Log.i("shubham","Pothole upvoted");
                               final String potholeid=obj.getPotholeid();

                                databaseReference = FirebaseDatabase.getInstance().getReference().child("pothole").child(potholeid);
                                databaseReference.child("noOfUpvotes").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                       //upvotes count increment
                                        int upvotesCount = dataSnapshot.getValue(Integer.class);
                                        upvotesCount++;
                                        databaseReference.child("noOfUpvotes").setValue(upvotesCount);

                                        //add pothole eid in user's upvotedPotholes
                                        databaseReference = FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("upvotedPothole");
                                        databaseReference.child(potholeid).setValue(1);

                                        //add 5 points to user
                                        databaseReference = FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid());

                                        databaseReference.child("points").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                int upvotePoints = dataSnapshot.getValue(Integer.class);
                                                upvotePoints = upvotePoints + 5;
                                                databaseReference.child("points").setValue(upvotePoints);
                                                progressDialog.dismiss();
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) { }
                                        });

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
//                                databaseReference.runTransaction(new Transaction.Handler() {
//                                    @NonNull
//                                    @Override
//                                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
//                                        int tempUpvotes = mutableData.getValue(Integer.class);
//                                        mutableData.setValue(tempUpvotes + 1);
//                                        return Transaction.success(mutableData);
//                                    }
//                                    @Override
//                                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
//                                        Log.i("shubham", "increment error in Upvote");
//                                    }
//                                });

                            }
                            else {
                                //Connectivity c = new Connectivity(getContext());
                                //c.execute(byteArray);

                                //######for adding pothole image in firebase storage###########
                                Toast.makeText(getContext(),"New Pothole detected, so uploaded",Toast.LENGTH_LONG).show();
                                Log.i("shubham","Pothole uploaded");

                                mAuth = FirebaseAuth.getInstance();
                                String uid = mAuth.getUid();
                                String path = "Pothole_photos/" + System.currentTimeMillis() + ".jpg";
                                mStorageRef = FirebaseStorage.getInstance().getReference(path);

                                final UploadTask uploadTask = mStorageRef.putBytes(byteArray);
                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle unsuccessful uploads
                                        Toast.makeText(getContext(), "fail to upload", Toast.LENGTH_SHORT);

                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Toast.makeText(getContext(), "Successful to upload", Toast.LENGTH_SHORT);
                                        taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                imageUrl = uri.toString();
                                                //#########for adding pothole data into pothole node###########
                                                databaseReference = FirebaseDatabase.getInstance().getReference().child("pothole");
                                                tempPotholeUid = databaseReference.push().getKey();

                                                PotholeLocation pht = new PotholeLocation(latitude, longitude, 1, 1, FirebaseAuth.getInstance().getUid(), tempPotholeUid, imageUrl);
                                                databaseReference.child(tempPotholeUid).setValue(pht);

                                                databaseReference = FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid());
                                                databaseReference.child("uploadedPothole").child(tempPotholeUid).setValue(1);

                                                databaseReference = FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//                                                databaseReference.runTransaction(new Transaction.Handler() {
//                                                    @NonNull
//                                                    @Override
//                                                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
//                                                        temppoints = mutableData.child("points").getValue(Integer.class);
//                                                        mutableData.setValue(temppoints + 10);
//                                                        return Transaction.success(mutableData);
//                                                    }
//                                                    @Override
//                                                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
//                                                        Log.i("shubham", "increment error in Upload");
//                                                    }
//                                                });
                                                databaseReference.child("points").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        temppoints = dataSnapshot.getValue(Integer.class);
                                                        temppoints = temppoints + 10;
                                                        databaseReference.child("points").setValue(temppoints);
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });
                                            }
                                        });
                                        progressDialog.dismiss();
                                    }
                                });
                            }
                            Log.i("shubham","its out of checker");
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Server Down! :(", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
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
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageURi);
            }catch (IOException e)
            {
                e.printStackTrace();
            }
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

   // public Upload_Upvote getPothole() {

   // }

    public float distance(double currentlatitude, double currentlongitude, double originLat, double originLon) {

        float[] results = new float[1];
        Location.distanceBetween(currentlatitude, currentlongitude, originLat, originLon, results);
        float distanceInMeters = results[0];
        //  Toast.makeText(getApplicationContext(),Float.toString(distanceInMeters)+"tu",Toast.LENGTH_LONG).show();
        return distanceInMeters;
    }

}