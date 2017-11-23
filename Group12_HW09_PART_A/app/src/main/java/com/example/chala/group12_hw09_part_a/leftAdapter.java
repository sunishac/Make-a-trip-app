package com.example.chala.group12_hw09_part_a;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by chala on 4/21/2017.
 */

public class leftAdapter extends RecyclerView.Adapter<leftAdapter.DataObjectHolder> {
    public ArrayList<message> mDataset= new ArrayList<message>();
    static String email,trip;
    private static Context context;
    FirebaseDatabase firebase;
    DatabaseReference ref;

    public leftAdapter(ArrayList<message> mDataset,String email, String trip) {
        this.email=email;
        this.trip=trip;
        this.mDataset = mDataset;
        firebase=FirebaseDatabase.getInstance();
        ref=firebase.getReference();
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView right,left;
        ImageView ivupload_right,ivupload_left;
        RelativeLayout rl_left,rl_right;

        public DataObjectHolder(View itemView) {
            super(itemView);
            Log.d("demol","entered dataob");
            rl_left=(RelativeLayout) itemView.findViewById(R.id.rl_left);
            rl_right=(RelativeLayout) itemView.findViewById(R.id.rl_right);
            right = (TextView) itemView.findViewById(R.id.left);
            left = (TextView) itemView.findViewById(R.id.right);
            ivupload_left= (ImageView) itemView.findViewById(R.id.ivupload_left);
            ivupload_right= (ImageView) itemView.findViewById(R.id.ivupload_right);
            ivupload_right.setVisibility(View.INVISIBLE);
            ivupload_left.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onClick(View v) {

        }
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();
        RelativeLayout l  = (RelativeLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.leftallign, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(l);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        Log.d("demol","entered "+position);
        if(mDataset.get(position).getSender().equals(email)){
           if(mDataset.get(position).getImage()==null){
               holder.right.setText(mDataset.get(position).getSender()+" :  "+mDataset.get(position).getMessage());
           }else{
               holder.ivupload_right.setVisibility(View.VISIBLE);
               holder.right.setText(mDataset.get(position).getSender()+" :  ");
               Picasso.with(context)
                       .load(mDataset.get(position).getImage())
                       .into(holder.ivupload_right);
           }

        }else{
            if(mDataset.get(position).getImage()==null){
                holder.left.setText(mDataset.get(position).getSender()+" :  "+mDataset.get(position).getMessage());
            }else{
                holder.ivupload_left.setVisibility(View.VISIBLE);
                holder.left.setText(mDataset.get(position).getSender()+" :  ");
                Picasso.with(context)
                        .load(mDataset.get(position).getImage())
                        .into(holder.ivupload_left);
            }
        }

        holder.rl_right.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String g=mDataset.get(position).getKey();
                ref.child(email).child("trips").child(trip).child("message").child(g).removeValue();
                return true;
            }
        });

        holder.rl_left.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String g=mDataset.get(position).getKey();
                ref.child(email).child("trips").child(trip).child("message").child(g).removeValue();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
