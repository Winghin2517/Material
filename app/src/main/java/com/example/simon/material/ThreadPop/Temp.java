package com.example.simon.material.ThreadPop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.example.simon.material.R;

/**
 * Created by Simon on 2015/04/10.
 */
public class Temp extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thread_pop);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        //Toolbar will now take on default Action Bar characteristics
        toolbar.setTitle("Hello from Toolbar");
        setSupportActionBar(toolbar);

        TextView textView = (TextView) findViewById(R.id.name);
        Intent intent = getIntent();
        int num = intent.getIntExtra("ListNo", 0);
        textView.setText("Card " + num + " was selected");
    }

}
