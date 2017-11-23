package com.example.chala.group12_hw09_part_a;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by chala on 4/20/2017.
 */

public class findfrndsadap extends ArrayAdapter<String>{
    ArrayList<String> mdata;
    Context mcontext;
    int mResouce;
    String user_email,user_email_trim;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
    String sent_to_user,sent_to_user_trim;

    public findfrndsadap(Context context, int resource, ArrayList<String> objects, String email) {
        super(context, resource, objects);
        this.mcontext=context;
        this.mResouce=resource;
        this.mdata=objects;
        user_email=email;
        firebaseDatabase=FirebaseDatabase.getInstance();
        ref=firebaseDatabase.getReference();
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResouce,parent,false);
        }

        if(user_email.contains(".")){
            user_email_trim= user_email.substring(0, user_email.indexOf("."));
        }

        sent_to_user=null;sent_to_user_trim=null;

        TextView name=(TextView) convertView.findViewById(R.id.name);
        name.setText(mdata.get(position));

        final Button fo=(Button) convertView.findViewById(R.id.frndop);
        fo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("demo",fo.getText().toString()+"Button Text");

                if(fo.getText().toString().equals("add")){

                    fo.setText("Request sent");
                    sent_to_user=mdata.get(position);
                    if(sent_to_user.contains(".")){
                        sent_to_user_trim= sent_to_user.substring(0, sent_to_user.indexOf("."));
                    }
                    ref.child(user_email_trim).child("request_sent").push().setValue(sent_to_user_trim);
                    ref.child(sent_to_user_trim).child("request_received").push().setValue(user_email_trim);
                }else{
                    Toast.makeText(mcontext,"request sent",Toast.LENGTH_SHORT).show();
                }

            }
        });

        return convertView;
    }

}
