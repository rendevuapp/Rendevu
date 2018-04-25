package com.rendevu.main;
/**
 Josh Davenport
 -This class creates a thread which monitors the whole
 app (by extending AppCompatActivity) for any unhandled
 exceptions.  If an unhandled exception is thrown, it is
 redirected to the CustomExceptionHandler class where it
 is then handled.
 */

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class UncaughtExceptionActivity extends AppCompatActivity {
    private Context context  = UncaughtExceptionActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(this));
    }
}

