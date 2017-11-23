package com.example.chala.group12_hw09_part_a;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class requestreceived extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
    String email;
    ListView lv;
    ArrayList<String> fdsrequestreceived;
    String user_email_trim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requestreceived);

        lv= (ListView) findViewById(R.id.lvreceived);
        fdsrequestreceived=new ArrayList<String>();

        if(getIntent().getExtras()!=null){
            email=getIntent().getStringExtra("Email");
            Log.d("demo","email from intent "+email);
        }
        user_email_trim=null;
        if(email.contains(".")){
            user_email_trim= email.substring(0, email.indexOf("."));
        }

        firebaseDatabase=FirebaseDatabase.getInstance();
        ref= firebaseDatabase.getReference(user_email_trim);
        ref.child("request_received").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fdsrequestreceived.clear();
                Log.e("Count " ,""+dataSnapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String post = postSnapshot.getValue(String.class);
                    fdsrequestreceived.add(post);
                }
                for(int i=0;i<fdsrequestreceived.size();i++){
                    Log.d("demo","values in arraylist "+fdsrequestreceived.get(i));
                    if(fdsrequestreceived.get(i).equals(email)){
                        fdsrequestreceived.remove(i);
                    }
                }
                requestreceivedadap adap = new requestreceivedadap(requestreceived.this, R.layout.rreceieved_adap, fdsrequestreceived, email );
                adap.setNotifyOnChange(true);
                lv.setAdapter(adap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }});

        /*ref.child("request_received").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
             //   fdsrequestreceived.add((String) dataSnapshot.getValue());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                int i = fdsrequestreceived.indexOf(dataSnapshot.getValue());
                fdsrequestreceived.remove(i);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/






    }
}
