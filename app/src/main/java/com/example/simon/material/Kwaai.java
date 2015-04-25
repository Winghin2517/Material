package com.example.simon.material;

import android.app.Application;
import android.content.Context;

/**
 * Created by Simon on 2015/04/12.
 */
public class Kwaai extends Application {

    public static Context mContext;

    @Override
    public void onCreate(){
        mContext = this;
    }
}
