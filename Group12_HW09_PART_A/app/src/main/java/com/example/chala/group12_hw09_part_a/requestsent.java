package com.example.chala.group12_hw09_part_a;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class requestsent extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
    String email;
    ListView lv;
    ArrayList<String> fdsrequestsent=new ArrayList<String>();
    String user_email_trim;
    requestsentadap adap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requestsent);

        lv= (ListView) findViewById(R.id.lvsent);
        if(getIntent().getExtras()!=null){
            email=getIntent().getStringExtra("Email");
            Log.d("demo","email from intent "+email);
        }
        user_email_trim=null;
        if(email.contains(".")){
            user_email_trim= email.substring(0, email.indexOf("."));
        }

        firebaseDatabase=FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference(user_email_trim);
        ref.child("request_sent").addValueEventListener/*addListenerForSingleValueEvent*/(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("demo" ,"count----------------------"+dataSnapshot.getChildrenCount());
                fdsrequestsent.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String post = postSnapshot.getValue(String.class);
                    fdsrequestsent.add(post);

                }

                for(int i=0;i<fdsrequestsent.size();i++){
                    Log.d("demo","values in arraylist "+fdsrequestsent.get(i));
                    if(fdsrequestsent.get(i).equals(email)){
                        fdsrequestsent.remove(i);
                    }
                }
                Log.d("demo",fdsrequestsent.size()+"sixe");
                adap= new requestsentadap(requestsent.this, R.layout.rsent_adap, fdsrequestsent, email );
                adap.setNotifyOnChange(true);
                lv.setAdapter(adap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }});

       /*ref.child("request_sent").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                //    fdsrequestsent.add((String) dataSnapshot.getValue());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("demo","child removed in requestsent" +dataSnapshot.getValue()+"index"+fdsrequestsent.indexOf(dataSnapshot.getValue()));


                int i = fdsrequestsent.indexOf(dataSnapshot.getValue());
                if(i>0){
                    fdsrequestsent.remove(i);
                    lv.setAdapter(adap);
                }else{
                    lv.setVisibility(View.INVISIBLE);
                    Toast.makeText(requestsent.this,"no requests",Toast.LENGTH_SHORT).show();
                }


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
