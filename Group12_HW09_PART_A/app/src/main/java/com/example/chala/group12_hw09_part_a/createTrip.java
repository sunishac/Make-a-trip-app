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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class createTrip extends AppCompatActivity {
    String email;
    EditText loc,tit;
    ImageView adpic;
    Button adimgbut,cret;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
    String imgDecodableString;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;

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
                uploadToCloud(b);

            }
        }
    }

    public void uploadToCloud(Bitmap bp){

        StorageReference mountainsRef= storageReference.child(tit.getText().toString()).child("image");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(createTrip.this,"could not upload picture in storage",Toast.LENGTH_SHORT).show();
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                adpic.setVisibility(View.VISIBLE);
                adimgbut.setVisibility(View.INVISIBLE);

                adpic.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                //Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);
        loc=(EditText) findViewById(R.id.loc_enter);
        tit=(EditText) findViewById(R.id.title_enter);
        adpic=(ImageView) findViewById(R.id.addimg);
        adimgbut=(Button) findViewById(R.id.addimgbutton);
        cret=(Button) findViewById(R.id.create);
        adpic.setVisibility(View.INVISIBLE);
        imgDecodableString=null;
        firebaseDatabase=FirebaseDatabase.getInstance();
        ref=firebaseDatabase.getReference();
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();

        if(getIntent().getExtras()!=null){
            email=getIntent().getStringExtra("Email");
        }

        adimgbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tit.getText().toString().equals(null)){
                    Toast.makeText(createTrip.this,"fill the title",Toast.LENGTH_SHORT).show();
                }else{
                    Intent t=new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(t,30);
                }
            }
        });



        cret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.child("trips").child(tit.getText().toString()).child("title").setValue(tit.getText().toString());
                ref.child("trips").child(tit.getText().toString()).child("location").setValue(loc.getText().toString());
                ref.child("trips").child(tit.getText().toString()).child("created_by").setValue(email);
                ref.child(email).child("trips").child(tit.getText().toString()).child("title").setValue(tit.getText().toString());
                ref.child(email).child("trips").child(tit.getText().toString()).child("location").setValue(loc.getText().toString());
                ref.child(email).child("trips").child(tit.getText().toString()).child("status").setValue("active");
                ref.child("trips").child(tit.getText().toString()).child("members").push().setValue(email);
                finish();
            }
        });


    }
}
