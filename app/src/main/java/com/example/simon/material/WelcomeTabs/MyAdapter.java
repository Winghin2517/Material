package com.example.simon.material.WelcomeTabs;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.simon.material.Model.Place;
import com.example.simon.material.R;
import com.example.simon.material.SubjectList.SubjectListActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Simon on 2015/03/31.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private static final int VIEW_TYPE_WELCOME  = 0;
    private static final int VIEW_TYPE_LIST = 1;

    private RecyclerView mRecyclerView;
    private static CardView cardView;
    private int lastPosition = -1;
    private boolean removed_welcome_screen = false;

    ArrayList<Place> mPlaceList;

    //internet Mongo variables
    private ArrayList<Place> placeArrayList;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<Place> placeArrayList, RecyclerView recyclerView) {
        mPlaceList = placeArrayList;
        mRecyclerView = recyclerView;
        //db = new DatabaseHelper(mContext);
        //placeList = db.getAllPlaces();
    }

    public void updateList(ArrayList<Place> placeArrayList) {
        mPlaceList = placeArrayList;
        notifyDataSetChanged();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case
        //These are the university items in the Recycler
        public TextView name_of_place;
        public TextView description_of_place;
        public ImageView unipics;
        //public View entry;
        //This is the headerview on the Recycler (viewType = 0)
        public ImageView mCancel;
        public View homescreen;
        private ImageView search_button;
        private ImageView cancel_button;
        private EditText search_box;

        //This constructor would switch what to findViewBy according to the type of viewType
        public ViewHolder(View v, int viewType) {
            super(v);

            if (viewType == VIEW_TYPE_WELCOME && !removed_welcome_screen) {
                mCancel = (ImageView) v.findViewById(R.id.cancel);
                homescreen = v.findViewById(R.id.homescreen);
                search_button = (ImageView) v.findViewById(R.id.search_button_inside_edittext);
                cancel_button = (ImageView) v.findViewById(R.id.cancel_button_inside_edittext);
                search_box = (EditText) v.findViewById(R.id.search_box);
            } else if (viewType == VIEW_TYPE_LIST) {
                name_of_place = (TextView) v.findViewById(R.id.name_of_place);
                description_of_place = (TextView) v.findViewById(R.id.description_place);
                unipics = (ImageView) v.findViewById(R.id.unipics);
               // entry = v.findViewById(R.id.recycler_row_entry);
            }
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent,
                                         int viewType)
    {
        View v;
        ViewHolder vh;
        // create a new view
        switch (viewType) {
            case VIEW_TYPE_WELCOME:
                if (!removed_welcome_screen) {
                    v = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.recyclerview_tab1_welcome, parent, false);
                    vh = new ViewHolder(v, viewType);
                    return vh;
                } else {
                    v = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.recyclerview_tab1_empty, parent, false);
                    vh = new ViewHolder(v, viewType);
                    return vh;
                }

            default: //This would be the normal list with the pictures of the university
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recyclerview_tab1_picture, parent, false);
                vh = new ViewHolder(v, viewType);
                v.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(parent.getContext(), SubjectListActivity.class);
                        //Intent intent = new Intent(parent.getContext(), ThreadPop.class);
                        //intent.putExtra("NameOfPlace",mPlaceList.get(mRecyclerView.getChildAdapterPosition(v)-1).getName_of_place());
                        parent.getContext().startActivity(intent);
                    }
                });
                return vh;
        }
    }

    //Overriden so that I can display custom rows in the recyclerview
    @Override
    public int getItemViewType(int position) {
        int viewType = VIEW_TYPE_LIST;
        if (position == VIEW_TYPE_WELCOME) return position;
        else
        // add here your booleans or switch() to set viewType at your needed
        // I.E if (position == 0) viewType = 1; etc. etc.
        return viewType;
    }
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //position == 0 means its the info header view on the Recycler
        if (position == 0 && !removed_welcome_screen) {
            holder.mCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItem(0);
                }
            });
            holder.search_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.cancel_button.setVisibility(View.VISIBLE);
                    holder.search_button.setVisibility(View.GONE);
                    holder.search_box.setCursorVisible(true);
                }
            });
            //this means it is beyond the headerview now
        } else if (position > 0) {

                Place place = mPlaceList.get(position-1);
                holder.name_of_place.setText(place.getName_of_place());
                holder.description_of_place.setText(place.getDescription_place());
                Picasso.with(holder.unipics.getContext()).load(place.getPic_url()).into(holder.unipics);
                //holder.unipics.setImageBitmap(new LoadPicAsyncTask().execute(place).get());

        }
        }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mPlaceList.size()+1;

    }

    public void removeItem(int position) {
        mRecyclerView.removeViewAt(position);
        notifyItemRangeChanged(position, getItemCount());
        notifyItemRemoved(position);
        removed_welcome_screen = true;
    }


}

