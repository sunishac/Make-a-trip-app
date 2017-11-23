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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.ArrayList;

public class chatroom extends AppCompatActivity {
    ImageView coverpic;
    ImageButton attach,send;
    EditText enter;
    TextView title,location;
    RelativeLayout rel;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    String trip=null,email=null;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    ArrayList<message> msgs;
    String imgDecodableString=null;
    private static int z=0,y=0,x=0;
    public  Bitmap bp;
    ArrayList<String> mbrs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);
        coverpic=(ImageView) findViewById(R.id.coverpic);
        attach=(ImageButton) findViewById(R.id.attach);
        enter=(EditText) findViewById(R.id.enterMsg);
        send=(ImageButton) findViewById(R.id.send);
        title=(TextView) findViewById(R.id.tt);
        location=(TextView) findViewById(R.id.lc);
        mRecyclerView=(RecyclerView) findViewById(R.id.recycw);
        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(chatroom.this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        msgs=new ArrayList<message>();
        mbrs=new ArrayList<String>();



        if(getIntent().getExtras()!=null){
            trip=getIntent().getStringExtra("chatroom");
            email=getIntent().getStringExtra("email");
        }

        databaseReference.child(email).child("trips").child(trip).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                     if(dataSnapshot.child("status").getValue()=="removed"){
                         enter.setVisibility(View.INVISIBLE);
                         send.setVisibility(View.INVISIBLE);
                         attach.setVisibility(View.INVISIBLE);
                         mRecyclerView.setVisibility(View.INVISIBLE);
                         Toast.makeText(chatroom.this,"No future posts allowed, trip removed ",Toast.LENGTH_SHORT).show();
                     }else{
                         enter.setVisibility(View.VISIBLE);
                     }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                title.setText("Title: "+(String) dataSnapshot.child("trips").child(trip).child("title").getValue());
                location.setText("Location: "+(String) dataSnapshot.child("trips").child(trip).child("location").getValue());
                storageReference.child(trip).child("image").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Got the download URL for 'users/me/profile.png'avat.setVisibility(View.VISIBLE);
                        Picasso.with(chatroom.this)
                                .load(uri.toString())
                                .into(coverpic);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d("demo","Failure");

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        databaseReference.child(email).child("trips").child(trip).child("message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                msgs.clear();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    message post = postSnapshot.getValue(message.class);
                    post.setKey(postSnapshot.getKey());
                    msgs.add(post);
                }

                mAdapter = new leftAdapter(msgs,email,trip);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(mAdapter);

                Log.d("demo"," outt"+msgs);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.child("trips").child(trip).child("members").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mbrs.clear();
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    mbrs.add(postSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(enter.getText().toString().equals(null)){
                    Toast.makeText(chatroom.this,"No message to send",Toast.LENGTH_SHORT).show();
                }else{

                    /*mAdapter = new leftAdapter(msgs,email,trip);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();*/

                    Log.d("demo"," msgs"+msgs);
                    message m=new message();
                    m.setMessage(enter.getText().toString());
                    m.setSender(email);
                    m.setKey(null);

                    if(z==0) {
                        databaseReference.child("trips").child(trip).child("message").push().setValue(m);
                        //databaseReference.child(email).child("trips").child(trip).child("message").push().setValue(m);
                        for(int i=0;i<mbrs.size();i++){
                            databaseReference.child(mbrs.get(i)).child("trips").child(trip).child("message").push().setValue(m);
                        }
                    }else if(z==1)
                    {
                        uploadToCloud(bp);
                        z=0;
                    }

                    enter.setText("  ");
                }
            }
        });

        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent t=new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(t,30);
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 30) {
            if(resultCode == RESULT_OK
                    && null != data){
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                // Set the Image in ImageView after decoding the String

                Bitmap b= BitmapFactory
                        .decodeFile(imgDecodableString);

                bp = b;

                z=1;
                enter.setText("send image");
                Toast.makeText(chatroom.this,"Image attached click send to send message",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void uploadToCloud(Bitmap bp){
        Toast.makeText(chatroom.this,"Loading image",Toast.LENGTH_SHORT).show();

        y=msgs.size();
        StorageReference mountainsRef= storageReference.child(trip).child("message").child("image"+y);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(chatroom.this,"could not upload picture in storage",Toast.LENGTH_SHORT).show();
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                x=0;
                Toast.makeText(chatroom.this,"Image uploaded",Toast.LENGTH_SHORT).show();
                storageReference.child(trip).child("message").child("image"+y).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        message m=new message();
                        m.setMessage("image");
                        m.setSender(email);
                        m.setImage(uri.toString());
                        m.setKey(null);

                        databaseReference.child("trips").child(trip).child("message").push().setValue(m);
                        //databaseReference.child(email).child("trips").child(trip).child("message").push().setValue(m);
                        for(int i=0;i<mbrs.size();i++){
                            databaseReference.child(mbrs.get(i)).child("trips").child(trip).child("message").push().setValue(m);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(chatroom.this,"unable to download this image, try to reupload",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.remove, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.remove:
                enter.setVisibility(View.INVISIBLE);
                send.setVisibility(View.INVISIBLE);
                mRecyclerView.setVisibility(View.INVISIBLE);
                attach.setVisibility(View.INVISIBLE);
                databaseReference.child(email).child("trips").child(trip).child("status").setValue("removed");
                Toast.makeText(chatroom.this,"No future posts allowed",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.locations:
                Intent i=new Intent(chatroom.this,places.class);
                i.putExtra("tripName",trip);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
