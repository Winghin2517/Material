package com.example.simon.material.WelcomeTabs;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.simon.material.R;
import com.example.simon.material.WelcomeTabs.MainActivity;
import com.example.simon.material.WelcomeTabs.MyAdapter;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import java.util.ArrayList;

/**
 * Created by hp1 on 21-01-2015.
 */
public class Tab1 extends Fragment implements ObservableScrollViewCallbacks {

    private ObservableRecyclerView mRecyclerView;
    private ObservableRecyclerView.Adapter mAdapter;
    private ObservableRecyclerView.LayoutManager mLayoutManager;
    ArrayList<String> myDataset = new ArrayList<String>();
    static Context mContext;


    public Tab1(){}

    public Tab1(Context context) {
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab1,container,false);

        mRecyclerView = (ObservableRecyclerView) v.findViewById(R.id.my_recycler_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView

        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        for (int i = 0 ; i<20; i++){
            myDataset.add("Number: " + i);
        }
        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(myDataset, mContext, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        //mRecyclerView.setScrollViewCallbacks(this); don't want the tab bar to be hidden anymore.
        setUpSwipeRefreshView(v);
        return v;

    }
    //This is the latest way to refresh your views from Google
    private void setUpSwipeRefreshView(View v) {
        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.accent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                Toast.makeText(mContext, "Refreshing for 3 secs", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });
    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b2) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override //To hid the tab bar
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        ActionBar ab = ((MainActivity) getActivity()).getSupportActionBar();
        if (scrollState == ScrollState.UP) {
            if (ab.isShowing()) {
                ab.hide();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (!ab.isShowing()) {
                ab.show();
            }
        }
    }
}