package com.example.chala.group12_hw09_part_a;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class places extends AppCompatActivity {
    Button s;
    TextView ps;
    int PLACE_PICKER_REQUEST = 1;
    Button checkPlaces;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    String tripName;
    Button removePlaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);
        ps=(TextView) findViewById(R.id.placestv);
        checkPlaces = (Button) findViewById(R.id.checkPlaces);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        tripName = getIntent().getStringExtra("tripName");
        removePlaces = (Button) findViewById(R.id.removePlaces);

        s=(Button) findViewById(R.id.showmap);
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(places.this,MapsActivity.class);
                i.putExtra("tripName",tripName);
                startActivity(i);
            }
        });

        checkPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(places.this), PLACE_PICKER_REQUEST);
                }catch(GooglePlayServicesRepairableException e)
                {
                    Log.d("demo","e");
                }catch (GooglePlayServicesNotAvailableException e1)
                {
                    Log.d("demo","e1");
                }

            }

        });

        removePlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(places.this,RemovePlaces.class);
                i.putExtra("tripName",tripName);
                startActivity(i);




            }
        });



    }

   /* protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==1001){
            if(resultCode==RESULT_OK){
                Place place=PlacePicker.getPlace(data, this);
                String address= String.format("first ",place.getAddress());
                ps.setText(address);
            }
        }
    }*/

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Log.d("demo","entered place picker");
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                String address= place.getAddress().toString();
                String name = place.getName().toString();
                LatLng latitudeLongitude= place.getLatLng();
                ps.setText(address);
                PlaceObject newPlace = new PlaceObject();
                newPlace.setPlaceName(name);
                newPlace.setLatitude(place.getLatLng().latitude);
                newPlace.setLongitude(place.getLatLng().longitude);
                databaseReference.child("trips").child(tripName).child("places").push().setValue(newPlace);

                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

}
