package com.example.simon.material.WelcomeTabs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.simon.material.Database.DatabaseHelper;
import com.example.simon.material.Model.Place;
import com.example.simon.material.R;
import com.example.simon.material.ThreadPop.ThreadPop;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Simon on 2015/03/31.
 */
public class MySQLAdapter extends RecyclerView.Adapter<MySQLAdapter.ViewHolder> {
    private String mDataset[];
    private Context mContext;
    private RecyclerView mRecyclerView;
    private static CardView cardView;
    private int lastPosition = -1;
    private boolean removed_welcome_screen = false;
    private DatabaseHelper db;
    List<Place> placeList;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MySQLAdapter(ArrayList<String> myDataset, Context context, RecyclerView recyclerView) {
        mDataset = myDataset.toArray(new String[myDataset.size()]);
        mContext = context;
        mRecyclerView = recyclerView;
        db = new DatabaseHelper(mContext);
        placeList = db.getAllPlaces();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder  extends RecyclerView.ViewHolder{
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
            if (viewType == 0 && !removed_welcome_screen) {
                mCancel = (ImageView) v.findViewById(R.id.cancel);
                homescreen = v.findViewById(R.id.homescreen);
                search_button = (ImageView) v.findViewById(R.id.search_button_inside_edittext);
                cancel_button = (ImageView) v.findViewById(R.id.cancel_button_inside_edittext);
                search_box = (EditText) v.findViewById(R.id.search_box);
            } else if (viewType == 1) {
                name_of_place = (TextView) v.findViewById(R.id.name_of_place);
                description_of_place = (TextView) v.findViewById(R.id.description_place);
                unipics = (ImageView) v.findViewById(R.id.unipics);
               // entry = v.findViewById(R.id.recycler_row_entry);
            }
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType)
    {
        View v;
        ViewHolder vh;
        // create a new view
        switch (viewType) {
            case 0:
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
                        Intent intent = new Intent(mContext, ThreadPop.class);
                        intent.putExtra("ListNo",mRecyclerView.getChildPosition(v));
                        mContext.startActivity(intent);
                    }
                });
                return vh;
        }
    }

    //Overriden so that I can display custom rows in the recyclerview
    @Override
    public int getItemViewType(int position) {
        int viewType = 1;
        if (position == 0) viewType = 0;
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
            Place place = placeList.get(position-1);
            holder.name_of_place.setText(place.getName_of_place());
            holder.description_of_place.setText(place.getDescription_place());

            try {
                holder.unipics.setImageBitmap(new LoadPicAsyncTask().execute(place).get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }
        }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return db.getRowCount()+1;

    }


    public void removeItem(int position) {
        mRecyclerView.removeViewAt(position);
        notifyItemRangeChanged(position, getItemCount());
        notifyItemRemoved(position);
        removed_welcome_screen = true;
    }

    //design flaw as i had to block the loading of the pic...I will look at building picasso in.
    private class LoadPicAsyncTask extends AsyncTask<Place, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Place... params) {
            Bitmap bm = null;
            try {
                URL aURL = new URL(params[0].getPic_url());
                URLConnection conn = aURL.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                bm = BitmapFactory.decodeStream(bis);
                bis.close();
                is.close();
            } catch (IOException e) {
                Log.e("MyAdapter", "Error getting bitmap", e);
            }
            return bm;
        }

    }
}
/**
if (position % 2 == 0) {
        holder.unipics.setImageDrawable(mContext.getResources().getDrawable(R.drawable.uni_cpt));
        }
        if (position % 2 == 1) {
        holder.unipics.setImageDrawable(mContext.getResources().getDrawable(R.drawable.uni_wits));

 }

 // Return the size of your dataset (invoked by the layout manager)
 @Override
 public int getItemCount() {
 return mDataset.length;
 }

 public void removeItem(int position) {
 mRecyclerView.removeViewAt(position);
 notifyItemRangeChanged(position, mDataset.length);
 notifyItemRemoved(position);
 removed_welcome_screen = true;
 }
 }


 **/