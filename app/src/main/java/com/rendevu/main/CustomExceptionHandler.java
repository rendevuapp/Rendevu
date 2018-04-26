package com.rendevu.main;
/**
 *  Josh Davenport
 -redirects the unhandled exception to the title screen,
 and terminates active processes for the current session.
 */

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

public class CustomExceptionHandler implements
        java.lang.Thread.UncaughtExceptionHandler{


    private final Activity myContext;

    public CustomExceptionHandler(Activity context) {
        myContext = context;
    }

    public void uncaughtException(Thread thread, Throwable exception) {

        Intent intent = new Intent(myContext, MainActivity.class);
        //intent.putExtra("error", errorReport.toString());
        //Toast.makeText(myContext, "Unhandled exception! Redirecting to title screen.", Toast.LENGTH_LONG).show();
        myContext.startActivity(intent);

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);

        Toast.makeText(myContext, "Unhandled exception! Redirecting to title screen.", Toast.LENGTH_LONG).show();
    }

}
