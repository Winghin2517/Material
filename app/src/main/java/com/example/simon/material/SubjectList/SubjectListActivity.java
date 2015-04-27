package com.example.simon.material.SubjectList;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.example.simon.material.R;

/**
 * Created by Simon on 2015/04/27.
 */
public class SubjectListActivity extends ActionBarActivity {
    private static final String FRAGMENT_LIST_VIEW = "list view";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjectlist_container); //empty container

        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new RecyclerListViewFragment(), FRAGMENT_LIST_VIEW)
                    .commit();
        }
    }

    public AbstractExpandableDataProvider getDataProvider() {
        return new ExpandableDataProvider();
    }
}
