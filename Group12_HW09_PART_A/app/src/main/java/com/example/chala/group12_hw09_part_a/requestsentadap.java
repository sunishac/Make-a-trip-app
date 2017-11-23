package com.example.chala.group12_hw09_part_a;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by chala on 4/20/2017.
 */

public class requestsentadap extends ArrayAdapter<String> {
    ArrayList<String> mdata;
    Context mcontext;
    int mResouce;
    int pos = 0;
    String user_email,user_email_trim;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
    String sent_to_user,sent_to_user_trim;

    public requestsentadap(Context context, int resource, ArrayList<String> objects, String email) {
        super(context, resource, objects);
        this.mcontext=context;
        this.mResouce=resource;
        this.mdata=objects;
        user_email=email;

    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        pos = position;
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResouce,parent,false);
        }

        if(user_email.contains(".")){
            user_email_trim= user_email.substring(0, user_email.indexOf("."));
        }

        sent_to_user=null;sent_to_user_trim=null;

        TextView name=(TextView) convertView.findViewById(R.id.rsenttv);
        name.setText(mdata.get(pos));

        final Button fo=(Button) convertView.findViewById(R.id.rsentbutton);
        fo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    sent_to_user = mdata.get(position);

                    if (sent_to_user.contains(".")) {
                        sent_to_user_trim = sent_to_user.substring(0, sent_to_user.indexOf("."));
                    }
                firebaseDatabase=FirebaseDatabase.getInstance();
                ref=firebaseDatabase.getReference();
                    Log.d("test", "user_email_trim: " +user_email_trim);
                    ref.child(user_email_trim).child("request_sent").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                String post = postSnapshot.getValue(String.class);
                                if (post.equals(sent_to_user)) {
                                    ref.child(user_email_trim).child("request_sent").child(postSnapshot.getKey()).removeValue();
                                    fo.setText("Removed");
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d("demo", "OnCancelled");
                        }
                    });

                    Log.d("demo", "posit" + position + " " + pos);
                    Log.d("test", "sent_to_user: " +sent_to_user);

                    ref.child(sent_to_user).child("request_received").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d("demo", "sen " + sent_to_user);
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                String post = postSnapshot.getValue(String.class);
                                if (post.equals(user_email_trim)) {
                                    //sent_to_user = mdata.get(pos);
                                    String key = postSnapshot.getKey();
                                    Log.d("demo", "sen " + sent_to_user + "post " + key);
                                    ref.child(sent_to_user).child("request_received").child(key).removeValue();
                              // mdata.remove(pos);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d("demo", "OnCancelled");
                        }
                    });


                }

                    });

        return convertView;
    }
}
