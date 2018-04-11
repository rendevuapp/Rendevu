package com.rendevu.main;
/*
    Josh Davenport
 */
import android.app.Activity;
import android.os.Bundle;

public class Welcome extends UncaughtExceptionActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.welcome);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
