package com.example.simon.material;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.simon.material.home.ThreadPop;

import java.util.ArrayList;

/**
 * Created by Simon on 2015/03/31.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private String mDataset[];
    private Context mContext;
    private RecyclerView mRecyclerView;
    private static CardView cardView;
    private int lastPosition = -1;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTxtName, mTxtSurname;

        public ViewHolder(View v) {
            super(v);
            cardView = (CardView) v.findViewById(R.id.card_view);
            mTxtName = (TextView) v.findViewById(R.id.txtName);
            mTxtSurname = (TextView) v.findViewById(R.id.txtSurname);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<String> myDataset, Context context, RecyclerView recyclerView) {
        mDataset = myDataset.toArray(new String[myDataset.size()]);
        mContext = context;
        mRecyclerView = recyclerView;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ThreadPop.class);
                intent.putExtra("ListNo",mRecyclerView.getChildPosition(v));
                mContext.startActivity(intent);
            }
        });

        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTxtSurname.setText(mDataset[position]);
        holder.mTxtName.setText(mDataset[position]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}