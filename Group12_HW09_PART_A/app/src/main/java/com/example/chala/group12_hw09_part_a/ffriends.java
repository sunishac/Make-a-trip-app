package com.example.chala.group12_hw09_part_a;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ffriends extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
    String email,email_trim;
    ListView lv;
    ArrayList<String> fds,frnds,reqst,fds_trim;
    int counter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ffriends);
        lv= (ListView) findViewById(R.id.lv);
        fds=new ArrayList<String>();
        frnds=new ArrayList<String>();
        reqst=new ArrayList<String>();
        fds_trim=new ArrayList<String>();

        if(getIntent().getExtras()!=null){
            email=getIntent().getStringExtra("Email");
            Log.d("demo","email from intent "+email);
        }

        if(email.contains(".")){
            email_trim= email.substring(0, email.indexOf("."));
            System.out.println(email_trim);
        }

        firebaseDatabase=FirebaseDatabase.getInstance();
        ref=firebaseDatabase.getReference("users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("demol","On Users Listener ff");

                Log.e("Count " ,""+dataSnapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String post = postSnapshot.getValue(String.class);

                    fds.add(post);
                    fds_trim.add(post.substring(0, post.indexOf(".")));
                    Log.d("demo","fds in users" +fds);
                    Log.d("demo","fds_trim in users" +fds_trim);

                }
                for(int i=0;i<fds.size();i++){
                    //Log.d("demo","values in arraylist "+fds.get(i));
                    if(fds.get(i).equals(email)){
                        fds.remove(i);
                        fds_trim.remove(i);
                    }
                }
                counter=counter+1;
                if(counter==4)
                {
                    Log.d("demo","entered counter in users");
                    reqst.addAll(frnds);
                    for(int i=0;i<reqst.size();i++){
                        Log.d("demo","fds trim is "+fds_trim);


                        for(int z=0;z<fds_trim.size();z++) {
                            if (reqst.get(i).equals(fds_trim.get(z).trim())) {
                                fds_trim.remove(z);
                                fds.remove(z);
                                Log.d("demo", "fds in for " + fds);
                            }
                        }
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        DatabaseReference ref1=firebaseDatabase.getReference();
        ref1.child(email_trim).child("friends").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("demol","On Friends Listener ff");

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String post = postSnapshot.getValue(String.class);
                    frnds.add(post);
                }
                counter=counter+1;
                if(counter==4)
                {
                    Log.d("demo","entered counter in friends");
                    reqst.addAll(frnds);
                    for(int i=0;i<reqst.size();i++){
                        Log.d("demo","fds trim is "+fds_trim);


                        for(int z=0;z<fds_trim.size();z++) {
                            if (reqst.get(i).equals(fds_trim.get(z).trim())) {
                                fds_trim.remove(z);
                                fds.remove(z);
                                Log.d("demo", "fds in for " + fds);
                            }
                        }
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
/*
        DatabaseReference ref3 = firebaseDatabase.getReference();
        ref3.child(email_trim).child("request_")*/


        DatabaseReference ref2=firebaseDatabase.getReference();
        ref2.child(email_trim).child("request_sent").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("demol","On Request Sent listener ff");

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Log.d("demo","enntered for");
                    String post = postSnapshot.getValue(String.class);
                    reqst.add(post);
                    Log.d("demol","reqst "+reqst+"  fds_trim  "+fds_trim);

                }
                counter=counter+1;
                if(counter==4)
                {
                    Log.d("demo","entered counter in request");
                    reqst.addAll(frnds);
                    for(int i=0;i<reqst.size();i++){
                        Log.d("demo","fds trim is "+fds_trim);


                        for(int z=0;z<fds_trim.size();z++) {
                            if (reqst.get(i).equals(fds_trim.get(z).trim())) {
                                fds_trim.remove(z);
                                fds.remove(z);
                                Log.d("demo", "fds in for " + fds);
                            }
                        }
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DatabaseReference ref3=firebaseDatabase.getReference();
        ref3.child(email_trim).child("request_received").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("demol","On Request Sent listener ff");

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Log.d("demo","enntered for");
                    String post = postSnapshot.getValue(String.class);
                    reqst.add(post);
                    Log.d("demol","reqst "+reqst+"  fds_trim  "+fds_trim);

                }
                counter=counter+1;
                if(counter==4)
                {
                    Log.d("demo","entered counter in request");
                    reqst.addAll(frnds);
                    for(int i=0;i<reqst.size();i++){
                        Log.d("demo","fds trim is "+fds_trim);


                        for(int z=0;z<fds_trim.size();z++) {
                            if (reqst.get(i).equals(fds_trim.get(z).trim())) {
                                fds_trim.remove(z);
                                fds.remove(z);
                                Log.d("demo", "fds in for " + fds);
                            }
                        }
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        findfrndsadap adap = new findfrndsadap(ffriends.this, R.layout.simple_adap, fds, email );
        adap.setNotifyOnChange(true);
        lv.setAdapter(adap);

    }
}
