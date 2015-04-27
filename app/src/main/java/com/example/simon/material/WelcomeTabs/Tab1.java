package com.example.simon.material.WelcomeTabs;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.simon.material.Database.PlaceDatabaseHelper;
import com.example.simon.material.Model.Place;
import com.example.simon.material.R;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by hp1 on 21-01-2015.
 */
public class Tab1 extends Fragment implements ObservableScrollViewCallbacks {

    private ObservableRecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private PlaceDatabaseHelper db;
    private static boolean dataUpdate;
    public Tab1(){}

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab1,container,false);

        mRecyclerView = (ObservableRecyclerView) v.findViewById(R.id.my_recycler_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView

        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        //db helper class initialised here
        db = new PlaceDatabaseHelper(getActivity());
        //testing the loading of the data - load from DB first then if there are differences betweem db and mongolab, then we notifydatasetchanged
        mAdapter = new MyAdapter(new ArrayList<>(db.getAllPlaces()), mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        //this asynctask below will set the adapter after the list is downloaded in the fragment - the download will not happen in the adapter
        if (checkNetwork()) {
            new HttpRequestTask().execute();
        }
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
                Toast.makeText(getActivity(), "Refreshing for 3 secs", Toast.LENGTH_SHORT).show();
                if (checkNetwork()) {
                    new HttpRequestTask().execute();
                }
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



    private class HttpRequestTask extends AsyncTask<Void, Void, ArrayList<Place>> {
        @Override
        protected ArrayList<Place> doInBackground(Void... params) {
            try {
                final String urlcount = "https://morning-cove-7696.herokuapp.com/getcount";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Long count = restTemplate.getForObject(urlcount, Long.class);
                if (count.intValue() != db.getRowCount()) {
                    final String url = "https://morning-cove-7696.herokuapp.com/getallplaces";
                    Place[] mongolabPlacesArray = restTemplate.getForObject(url, Place[].class);
                    ArrayList<Place> mongolab_places = new ArrayList<>(Arrays.asList(mongolabPlacesArray));
                    //deleting the old database and replace it with the new
                    db.deleteAllPlaces();
                    for (Place place: mongolab_places) {
                        db.addPlace(place);
                    }
                    dataUpdate = true;
                    return mongolab_places;
                }
/*                else {
                    ArrayList<Place> mongolab_places = new ArrayList<>(db.getAllPlaces());
                    return mongolab_places;
                }*/
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Place> Places) {
            // specify an adapter (see also next example) but will be putting this into an asynctask
            if (dataUpdate) {
                mAdapter.updateList(Places);
                mAdapter.notifyDataSetChanged();
            }
            dataUpdate = false;
        }

    }

    private boolean checkNetwork() {
        boolean wifiDataAvailable = false;
        boolean mobileDataAvailable = false;
        ConnectivityManager conManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfo = conManager.getAllNetworkInfo();
        for (NetworkInfo netInfo : networkInfo) {
            if (netInfo.getTypeName().equalsIgnoreCase("WIFI"))
                if (netInfo.isConnected())
                    wifiDataAvailable = true;
            if (netInfo.getTypeName().equalsIgnoreCase("MOBILE"))
                if (netInfo.isConnected())
                    mobileDataAvailable = true;
        }
        //if both is not available, do not use the HTTPAsynctask
        if (!wifiDataAvailable && !mobileDataAvailable) {
            Toast.makeText(getActivity(),"No internet available. Please connect.", Toast.LENGTH_SHORT).show();
        }
        return wifiDataAvailable || mobileDataAvailable;
    }

}