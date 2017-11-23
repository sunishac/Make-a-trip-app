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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static java.lang.StrictMath.toIntExact;

public class Signup extends AppCompatActivity {
    EditText et1,et2,et3,ps1,ps2,et4;
    Button sg,cn;
    String wor;
    FirebaseDatabase database;
    DatabaseReference ref;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        sg=(Button) findViewById(R.id.signup_s);
        cn=(Button) findViewById(R.id.cancel);
        et1=(EditText) findViewById(R.id.firstname);
        et2=(EditText) findViewById(R.id.lastname);
        et3=(EditText) findViewById(R.id.email_s);
        et4=(EditText) findViewById(R.id.gender_s);
        ps1=(EditText) findViewById(R.id.password_s);
        ps2=(EditText) findViewById(R.id.repeatpass);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        ref=database.getReference();
        wor=null;


        cn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Signup.this,"Did not signup",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        sg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((et1.getText().toString().length()==0)||et2.getText().toString().length()==0||et3.getText().length()==0||et4.getText().toString().length()==0||ps1.getText().toString().length()==0||ps2.getText().toString().length()==0){
                    Toast.makeText(Signup.this,"Fill all the details",Toast.LENGTH_LONG).show();
                } else{
                    if(ps1.getText().toString().equals(ps2.getText().toString())){
                        ref=database.getReference();
                        String word=et3.getText().toString();

                        if(word.contains(".")){
                            word= word.substring(0, word.indexOf("."));
                            System.out.println(word);
                            wor=word;
                        }

                        String email = et3.getText().toString();
                        String password = ps1.getText().toString();
                        if(email.length()==0){
                            Toast.makeText(Signup.this,"email field is empty", Toast.LENGTH_LONG).show();
                        }
                        if(password.length()==0){
                            Toast.makeText(Signup.this,"password field is empty", Toast.LENGTH_LONG).show();
                        }

                        //creating a new user
                        firebaseAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        Toast.makeText(Signup.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                        // If sign in fails, display a message to the user. If sign in succeeds
                                        // the auth state listener will be notified and logic to handle the
                                        // signed in user can be handled in the listener.
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(Signup.this, "Authentication failed." + task.getException(),
                                                    Toast.LENGTH_SHORT).show();
                                            Log.d("demo","error is "+task.getException());
                                        } else {
                                            ref.child(wor).child("First name").setValue(et1.getText().toString());
                                            ref.child(wor).child("Last name").setValue(et2.getText().toString());
                                            ref.child(wor).child("Email").setValue(et3.getText().toString());
                                            ref.child(wor).child("Password").setValue(ps1.getText().toString());
                                            ref.child(wor).child("Gender").setValue(et4.getText().toString());
                                            ref.child(wor).child("friends");
                                            ref.child(wor).child("request_sent");
                                            ref.child(wor).child("request_received");
                                            ref.child(wor).child("non_friends");

                                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    long l=dataSnapshot.child("users").getChildrenCount();

                                                    ref.child("users").child(String.valueOf(l)).setValue(et3.getText().toString());

                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });


                                            Toast.makeText(Signup.this,"Signup successful",Toast.LENGTH_SHORT).show();
                                            //startActivity(new Intent(Signup.this, MainActivity.class));
                                            finish();
                                        }
                                    }
                                });
                        //finish();

                    }else{
                        Toast.makeText(Signup.this,"Signup not successful",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }
}
