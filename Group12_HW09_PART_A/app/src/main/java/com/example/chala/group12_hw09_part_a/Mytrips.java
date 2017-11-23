package com.example.chala.group12_hw09_part_a;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class Mytrips extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    /*private RecyclerView.LayoutManager mLayoutManager;*/
    LinearLayoutManager mLayoutManager;
    String email;
    ArrayList<tripdet> trps;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mytrips);
        firebaseDatabase=FirebaseDatabase.getInstance();
        ref=firebaseDatabase.getReference();
        trps=new ArrayList<tripdet>();
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();

        mRecyclerView=(RecyclerView) findViewById(R.id.rv_mytrips);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(Mytrips.this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new mytripsAdapter(trps,email);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        if(getIntent().getExtras()!=null){
            email=getIntent().getStringExtra("Email");
        }

        ref.child(email).child("trips").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                trps.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                      final tripdet t =postSnapshot.getValue(tripdet.class);

                    Log.d("demo",t.getTitle()+"");
                    trps.add(t);
                    /*storageReference.child(t.getTitle()).child("image").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Got the download URL for 'users/me/profile.png'avat.setVisibility(View.VISIBLE);
                            int i = trps.indexOf(t);
                            Log.d("demo","successImg");
                            tripdet t1 = new tripdet();

                            t1.setTitle(trps.get(i).getTitle());
                            t1.setLocation(trps.get(i).getLocation());
                            t1.setImg(uri.toString());

                            trps.remove(i);
                            trps.add(t1);
                            mAdapter = new mytripsAdapter(trps,email);
                            mRecyclerView.setAdapter(mAdapter);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.d("demo","Failure");

                        }
                    });*/

                    mAdapter = new mytripsAdapter(trps,email);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    Log.d("demo",trps.size()+"");

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
