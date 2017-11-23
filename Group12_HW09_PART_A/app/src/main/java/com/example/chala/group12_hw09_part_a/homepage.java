package com.example.chala.group12_hw09_part_a;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class homepage extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener{
    private FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference ref;
    String email,fullemail;
    TextView fn,ln,gn;
    ImageView avat;
    Button av,ff,requests_sentButton,requests_receivedButton,myFriends,creatTp, mytp, joinTp;
    String frn,lrn,grn;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    String imgDecodableString=null;
    boolean googleAuthentication=false;
    String googleSignIn="googleSignIn";
    String googleSignInBoolean = "false";
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        ref=database.getReference();
        fn= (TextView) findViewById(R.id.first);
        ln=(TextView) findViewById(R.id.last);
        gn=(TextView) findViewById(R.id.genderHome);
        avat=(ImageView) findViewById(R.id.imageView);
        ff=(Button) findViewById(R.id.finffrnds);
        avat.setVisibility(View.INVISIBLE);
        database = FirebaseDatabase.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        av=(Button) findViewById(R.id.avatarbutton);
        requests_sentButton = (Button) findViewById(R.id.requestsent);
        requests_receivedButton = (Button) findViewById(R.id.requestreceived);
        myFriends = (Button) findViewById(R.id.friends);
        creatTp=(Button) findViewById(R.id.createtrip);
        mytp=(Button) findViewById(R.id.mytrips);
        joinTp=(Button) findViewById(R.id.jointrips);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        if(getIntent().getExtras()!=null){
            email=getIntent().getStringExtra("Email");
            fullemail=email;
            Log.d("demo","email in homepage is "+email);
            if(getIntent().getExtras().containsKey("googleSignIn"))
            {
                Log.d("demo","google sign in extra");
               googleSignInBoolean=  getIntent().getStringExtra("googleSignIn");
                Log.d("demo",googleSignInBoolean+"googleSignInBoolean");
            }

        }

        String word=email;

        if(word.contains(".")){
            word= word.substring(0, word.indexOf("."));
            System.out.println(word);
        }
        email=word;

        storageReference.child(email).child("image").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                avat.setVisibility(View.VISIBLE);

                Picasso.with(homepage.this)
                        .load(uri.toString())
                        .into(avat);

                av.setVisibility(View.INVISIBLE);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                avat.setVisibility(View.INVISIBLE);
                av.setVisibility(View.VISIBLE);
            }
        });

        ref=database.getReference();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("demo","frn is "+dataSnapshot.child(email).child("First name").getValue());
                frn= (String) dataSnapshot.child(email).child("First name").getValue();
                lrn= (String) dataSnapshot.child(email).child("Last name").getValue();
                grn= (String) dataSnapshot.child(email).child("Gender").getValue();
                fn.setText(frn);
                Log.d("demo","printing.."+fn.getText());
                ln.setText(lrn);
                gn.setText("Gender: "+grn);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        av.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent t=new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                /*Intent intent = new Intent();
                intent.setType("image*//*");
                intent.setAction(Intent.ACTION_GET_CONTENT);*/
                startActivityForResult(t,30);
            }
        });

        ff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(homepage.this,ffriends.class);
                it.putExtra("Email",fullemail);
                startActivityForResult(it,1000);
            }
        });

        requests_sentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(homepage.this,requestsent.class);
                i.putExtra("Email",fullemail);
                startActivityForResult(i,2000);
            }
        });

        requests_receivedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(homepage.this,requestreceived.class);
                it.putExtra("Email",fullemail);
                startActivityForResult(it,3000);

            }
        });

        myFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(homepage.this,myfriends.class);
                it.putExtra("Email",fullemail);
                startActivityForResult(it,4000);
            }
        });

        creatTp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(homepage.this,createTrip.class);
                i.putExtra("Email",email);
                startActivity(i);
            }
        });

        mytp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(homepage.this,Mytrips.class);
                i.putExtra("Email",email);
                startActivity(i);
            }
        });

        joinTp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(homepage.this,joinTrips.class);
                i.putExtra("Email",email);
                startActivity(i);
            }
        });

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.d("demo", "onConnectionFailed:" + connectionResult);
    }

    private void signOut() {
        Log.d("demo","status is ne bondha");
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Log.d("demo","status is "+status);
// [START_EXCLUDE]

// [END_EXCLUDE]
                    }
                });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu, menu);
            return true;
        }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                if(googleSignInBoolean.equals("false")) {
                    auth.signOut();
                    /*setResult(RESULT_OK);
                    startActivity(new Intent(homepage.this,MainActivity.class));
                    return true;*/
                }else
                {
                    Log.d("demo","entering else");
                    signOut();
                }
                setResult(RESULT_OK);
                startActivity(new Intent(homepage.this,MainActivity.class));
                return true;

            case R.id.settings:
                Intent n=new Intent(homepage.this, changeDetails.class);
                n.putExtra("Email",email);
                startActivityForResult(n,10);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10){
            if(resultCode==RESULT_OK){
                ref=database.getReference();
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("demo","frn is "+dataSnapshot.child(email).child("First name").getValue());
                        frn= (String) dataSnapshot.child(email).child("First name").getValue();
                        lrn= (String) dataSnapshot.child(email).child("Last name").getValue();
                        grn= (String) dataSnapshot.child(email).child("Gender").getValue();
                        fn.setText(frn);
                        Log.d("demo","printing.."+fn.getText());
                        ln.setText(lrn);
                        gn.setText("Gender: "+grn);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                storageReference.child(email).child("image").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Got the download URL for 'users/me/profile.png'
                        avat.setVisibility(View.VISIBLE);

                        Picasso.with(homepage.this)
                                .load(uri.toString())
                                .into(avat);

                        av.setVisibility(View.INVISIBLE);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        avat.setVisibility(View.INVISIBLE);
                        av.setVisibility(View.VISIBLE);
                    }
                });
            }
        }

        if (requestCode == 30) {
            if (resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                // Set the Image in ImageView after decoding the String

                Bitmap b = BitmapFactory
                        .decodeFile(imgDecodableString);
                uploadToCloud(b);

            } else {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void uploadToCloud(Bitmap bp){
            StorageReference mountainsRef= storageReference.child(email).child("image");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = mountainsRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(homepage.this,"could not upload picture in storage",Toast.LENGTH_SHORT).show();
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    avat.setVisibility(View.VISIBLE);
                    avat.setImageBitmap(BitmapFactory
                            .decodeFile(imgDecodableString));
                    av.setVisibility(View.INVISIBLE);
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                }
            });

        }
    //}
}
