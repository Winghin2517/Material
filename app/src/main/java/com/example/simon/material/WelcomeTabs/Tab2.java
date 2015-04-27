package com.example.simon.material.WelcomeTabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.simon.material.Database.PlaceDatabaseHelper;
import com.example.simon.material.Model.Place;
import com.example.simon.material.R;

import java.util.List;

/**
 * Created by hp1 on 21-01-2015.
 */
public class Tab2 extends Fragment {
    private PlaceDatabaseHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab2,container,false);
        db = new PlaceDatabaseHelper(getActivity());
        setUpDB();
        final EditText name = (EditText) v.findViewById(R.id.name);
        final EditText description = (EditText) v.findViewById(R.id.description);
        final EditText picurl = (EditText) v.findViewById(R.id.picurl);
        final TextView displaytextview = (TextView) v.findViewById(R.id.textView);

        Button save = (Button) v.findViewById(R.id.save_in_db);
        Button display = (Button) v.findViewById(R.id.display_saved);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Place place = new Place(name.getText().toString(), description.getText().toString(), picurl.getText().toString());
                db.addPlace(place);
            }
        });

        display.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                List<Place> placeList = db.getAllPlaces();
                StringBuilder stringBuilder = new StringBuilder();
                for (Place p : placeList) {
                    stringBuilder.append(p.getName_of_place());
                }
                displaytextview.setText(stringBuilder.toString());
            }
        });
        return v;

    }

    //Setting up the DB
    public void setUpDB() {

    }
}