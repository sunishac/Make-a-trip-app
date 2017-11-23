package com.example.chala.group12_hw09_part_a;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.example.chala.group12_hw09_part_a.R.id.rl;

/**
 * Created by chala on 4/21/2017.
 */

public class mytripsAdapter extends RecyclerView.Adapter<mytripsAdapter.DataObjectHolder> {
    private ArrayList<tripdet> mDataset=new ArrayList<tripdet>();
    private static MyClickListener myClickListener;
    private static Context context;
    static String email;


    public mytripsAdapter(ArrayList<tripdet> mDataset,String email) {

        this.mDataset=mDataset;
        this.email=email;
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    @Override
    public mytripsAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("demo","Entered here");
        RelativeLayout l = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyc_mytrips, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(l);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final mytripsAdapter.DataObjectHolder holder,final int position) {
        Log.d("demo","position"+position+"holder"+holder);
        Log.d("demo","size"+mDataset.size());
        holder.title.setText(mDataset.get(position).getTitle());
        holder.loc.setVisibility(View.INVISIBLE);
        holder.cvr.setVisibility(View.INVISIBLE);
        /*holder.loc.setText(mDataset.get(position).getLocation());
        Picasso.with(holder.cvr.getContext())
                .load(mDataset.get(position).getImg())
                .into(holder.cvr);*/
    }




    @Override
    public int getItemCount() {
        return mDataset.size();
    }



    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView loc;
        RelativeLayout rll;
        ImageView cvr;
        Button upload;


        public DataObjectHolder(final View itemView) {
            super(itemView);
            context=itemView.getContext();

            title = (TextView) itemView.findViewById(R.id.title_myt);
            loc = (TextView) itemView.findViewById(R.id.loc_myt);
            rll=(RelativeLayout) itemView.findViewById(R.id.rll);
            cvr = (ImageView) itemView.findViewById(R.id.cover);
            rll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(context,chatroom.class);
                    i.putExtra("chatroom",""+title.getText().toString());
                    i.putExtra("email",email);
                    context.startActivity(i);
                }
            });
        }


    }


    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}
