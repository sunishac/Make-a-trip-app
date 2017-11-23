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

public class myfriends extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
    String email;
    ListView lv;
    ArrayList<String> friendsForever;
    String user_email_trim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myfriends);

        lv= (ListView) findViewById(R.id.lvfriends);
        friendsForever=new ArrayList<String>();

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
        ref.child("friends").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friendsForever.clear();
                Log.e("Count " ,""+dataSnapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String post = postSnapshot.getValue(String.class);
                    friendsForever.add(post);
                }
                for(int i=0;i<friendsForever.size();i++){
                    Log.d("demo","values in arraylist "+friendsForever.get(i));
                    if(friendsForever.get(i).equals(email)){
                        friendsForever.remove(i);
                    }
                }

                myfriendsadap adap = new myfriendsadap(myfriends.this, R.layout.myfriends_adap, friendsForever, email );
                adap.setNotifyOnChange(true);
                lv.setAdapter(adap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }});
    }
}
