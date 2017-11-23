package com.example.chala.group12_hw09_part_a;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chala on 4/30/2017.
 */

public class removePlaceAdapter extends RecyclerView.Adapter<removePlaceAdapter.PlaceViewHolder> {

    Context mcontext;
    int mResource;
    ArrayList<PlaceObject> placeObjectList;

  /*  public removePlaceAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<PlaceObject> objects) {
        super(context, resource, objects);
        this.mcontext = context;
        this.mResource = resource;
        this.placeObjectList = objects;


    }*/

   //private List<ListItem> listData;
    private LayoutInflater inflater;

    private ItemClickCallback ItemClickCallback;

    public interface ItemClickCallback{
       // void onItemClick(int p);
        void onSecondaryIconClick(int p);
    }


    public void setItemClickCallback(final ItemClickCallback ItemClickCallback)
    {
        this.ItemClickCallback = ItemClickCallback;
    }
    public removePlaceAdapter(List<PlaceObject> listData, Context c) {
        this.placeObjectList = (ArrayList<PlaceObject>) listData;
        this.inflater = LayoutInflater.from(c);
    }

    @Override
    public removePlaceAdapter.PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.remove_place_item,parent,false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        PlaceObject place = placeObjectList.get(position);
        holder.title.setText(place.getPlaceName());



    }

    @Override
    public int getItemCount() {
        return placeObjectList.size();
    }

    /*  @Override
    public void onBindViewHolder(DerpAdapter.DerpHolder holder, int position) {
        ListItem item = listData.get(position);
        holder.title.setText(item.getTitle());
        holder.subTitle.setText(item.getSubTitle());
        if(item.isFavourite())
        {
            holder.secondaryIcon.setImageResource(R.drawable.ic_star_black_24dp);
        }else
        {
            holder.secondaryIcon.setImageResource(R.drawable.ic_star_border_black_24dp);
        }

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }*/


   /* public void setListData(ArrayList<ListItem> exerciseList) {
        this.listData.clear();
        this.listData.addAll(exerciseList);
    }*/

    public void setListData(ArrayList<PlaceObject> exerciseList) {
        this.placeObjectList.clear();
        this.placeObjectList.addAll(exerciseList);
    }


    class PlaceViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title;
         Button delete;
         private View container;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.containerPlace);
            title = (TextView) itemView.findViewById(R.id.placeNameRemove);
            delete = (Button) itemView.findViewById(R.id.deletePlace);
            delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
             if (view.getId() == R.id.deletePlace) {
                ItemClickCallback.onSecondaryIconClick(getAdapterPosition());
            }
        }
    }

}
