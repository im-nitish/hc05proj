package com.example.android.bluetoothchat;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Nitish on 31-Dec-16.
 */

public class App extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("OpenSansBoldItalic.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}