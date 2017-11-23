package com.example.chala.group12_hw09_part_a;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class joinTrips extends AppCompatActivity {
    ListView listView;
    ArrayList<String> ffds;
    ArrayList<tripdet> trpsf;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
    String email=null;
    ArrayList<tripdet> myTrips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_trips);
        listView=(ListView) findViewById(R.id.joinlist);
        firebaseDatabase=FirebaseDatabase.getInstance();
        ref=firebaseDatabase.getReference();
        trpsf=new ArrayList<tripdet>();
        ffds=new ArrayList<String>();
        myTrips = new ArrayList<tripdet>();

        if(getIntent().getExtras()!=null){
            email=getIntent().getStringExtra("Email");
        }

        ref.child(email).child("trips").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                    tripdet t = new tripdet();
                    t= postSnapshot.getValue(tripdet.class);
                    myTrips.add(t);
                    Log.d("demol","entered my trips"+myTrips);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ref.child(email).child("friends").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("demo","entered frieds trips");
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                    ffds.add((String) postSnapshot.getValue());
                    Log.d("demo","myfrnds"+ffds);
                }
                for(int i =0;i<ffds.size();i++)
                {
                    Log.d("demo","entered first for");
                    ref.child(ffds.get(i)).child("trips").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d("demo","entered second for");
                            int cnt=1;
                            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                Log.d("demo","entered third for");

                                tripdet t = new tripdet();
                                t= postSnapshot.getValue(tripdet.class);

                                trpsf.add(t);
                                /*if(trpsf.size()==0)
                                {
                                    for(int j=0;j<myTrips.size();j++){
                                        if(!(myTrips.get(j).getTitle().contains(t.getTitle()))) {
                                            cnt++;
                                        }
                                    }
                                    if(cnt==myTrips.size()){
                                        trpsf.add(t);
                                    }
                                }
                                int ct=1;
                                for(int z =0;z<trpsf.size();z++)
                                {
                                Log.d("demo","entered fourth for");
                                if(!(t.getTitle().equals(trpsf.get(z).getTitle()))) {
                                    for(int j=0;j<myTrips.size();j++){
                                        if(myTrips.get(j).getTitle().contains(t.getTitle())){

                                        }else{
                                            ct++;
                                        }
                                    }
                                }
                                else
                                {
                                    Log.d("demo",t.getTitle()+"title");
                                    Log.d("demo",trpsf.get(z).title+"frtriop");
                                    Log.d("demo","mytrips "+ myTrips +" t is "+ t);

                                }
                                }
                                if(ct==myTrips.size()){
                                    trpsf.add(postSnapshot.getValue(tripdet.class));
                                    Log.d("demo", "trps size" + trpsf.size() + trpsf);
                                }
*/
                            }

                            Log.d("demo","before "+trpsf);
                            for(int z=0;z<trpsf.size();z++){
                                for(int zk=z+1;zk<trpsf.size();zk++){
                                    if(trpsf.get(z).getTitle().equals(trpsf.get(zk).getTitle())){
                                        Log.d("demo","z "+trpsf.get(z).getTitle()+"zk "+trpsf.get(zk).getTitle());
                                        trpsf.remove(zk);
                                    }
                                }
                            }
                            Log.d("demo","after "+trpsf);

                            for(int j=0;j<trpsf.size();j++){
                                for(int jk=0;jk<myTrips.size();jk++){
                                    if(trpsf.get(j).getTitle().equals(myTrips.get(jk).getTitle())){
                                        trpsf.remove(j);
                                    }
                                }
                            }
                            Log.d("demo","after2 "+trpsf);
                            jointripadap adapter = new jointripadap(joinTrips.this, R.layout.simple_adap, trpsf,email);
                            listView.setAdapter(adapter);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        jointripadap adapter = new jointripadap(joinTrips.this, R.layout.simple_adap, trpsf,email);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
}
