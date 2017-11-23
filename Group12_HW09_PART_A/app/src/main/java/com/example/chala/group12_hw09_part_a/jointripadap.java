package com.example.chala.group12_hw09_part_a;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by chala on 4/21/2017.
 */

public class jointripadap extends ArrayAdapter {
    ArrayList<tripdet> mdata;
    Context mcontext;
    int mResouce;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    String email;

    public jointripadap(Context context, int resource, ArrayList<tripdet> objects,String email) {
        super(context, resource, objects);
        this.mcontext=context;
        this.mResouce=resource;
        this.mdata=objects;
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        this.email = email;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResouce,parent,false);
        }
        TextView tripttl=(TextView) convertView.findViewById(R.id.name);
        final Button btn=(Button) convertView.findViewById(R.id.frndop);
        btn.setText("JOIN");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn.getText().equals("JOIN"))
                {
                    btn.setText("JOINED");
                    databaseReference.child(email).child("trips").child(mdata.get(position).getTitle()).setValue(mdata.get(position));
                    databaseReference.child(email).child("trips").child(mdata.get(position).getTitle()).child("status").setValue("active");
                    databaseReference.child("trips").child(mdata.get(position).getTitle()).child("members").push().setValue(email);
                }
            }
        });
        tripttl.setText(mdata.get(position).getTitle());

        return convertView;
    }

}
