package com.example.chala.group12_hw09_part_a;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RemovePlaces extends AppCompatActivity implements removePlaceAdapter.ItemClickCallback{

    String tripName;
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<PlaceObject> placeList;
    removePlaceAdapter removePlaceAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_places);
        tripName = getIntent().getStringExtra("tripName");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewRemove);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        placeList = new ArrayList<PlaceObject>();
        databaseReference.child("trips").child(tripName).child("places").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("demo","entering here");
                placeList.clear();
                int i =0;
                if(!dataSnapshot.hasChildren())
                {
                    recyclerView.setLayoutManager(new LinearLayoutManager(RemovePlaces.this));
                    removePlaceAdapter = new removePlaceAdapter(placeList, RemovePlaces.this);
                    recyclerView.setAdapter(removePlaceAdapter);
                    removePlaceAdapter.setItemClickCallback(RemovePlaces.this);
                }
                for(DataSnapshot postsnapshot: dataSnapshot.getChildren()) {
                    Log.d("demo","getting new place");
                     String key = postsnapshot.getKey();
                     PlaceObject myPlace = postsnapshot.getValue(PlaceObject.class);
                     myPlace.setKey(key);
                     placeList.add(myPlace);
                    i=i+1;
                    Log.d("demo",i+"i"+ dataSnapshot.getChildrenCount()+"children count");
                    if(dataSnapshot.getChildrenCount()==i) {
                        Log.d("demo","entered children"+placeList.size());
                        recyclerView.setLayoutManager(new LinearLayoutManager(RemovePlaces.this));
                        removePlaceAdapter = new removePlaceAdapter(placeList, RemovePlaces.this);
                        recyclerView.setAdapter(removePlaceAdapter);
                        removePlaceAdapter.setItemClickCallback(RemovePlaces.this);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    @Override
    public void onSecondaryIconClick(int p) {
        databaseReference.child("trips").child(tripName).child("places").child(placeList.get(p).getKey()).removeValue();
    }
}
