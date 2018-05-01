package com.rendevu.main;
/*
    Josh Davenport
 */
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

import java.util.NoSuchElementException;

@SuppressWarnings("ConstantConditions")
public class MainActivity extends UncaughtExceptionActivity {

    private final String TAG = this.getClass().getName();
    private static final int PERMISSION_REQUEST_LOCATION = 34;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (auth.getCurrentUser() != null) {
            Toast.makeText(this, "Welcome Back  "+ auth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, Main2Activity.class));
            finish();
        }
        requestPermissions();

    }

    public void login_register(View v)
    {
        try {
            switch(v.getId())
            {
                case R.id.log_in:
                    try {
                        Intent i = new Intent(this, Login.class);
                        startActivityForResult(i, 500);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        finish();
                        break;
                    }catch (NoSuchElementException e) {
                        Toast.makeText(getApplicationContext(), "Broken path to Login screen.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                case R.id.register:
                    try {
                    Intent i =new Intent(this,Register.class);
                    startActivityForResult(i, 500);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                    break;
                    }catch (NoSuchElementException e) {
                        Toast.makeText(getApplicationContext(), "Broken path to Registration screen.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "General Exception", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        finish();
    }


    /*
    * Josh
    *
    * User asked to press back button twice
    * to completely shutdown app
    * */
    private boolean twice;
    @Override
    public void onBackPressed() {
        // Add the Back key handler here.

        Log.d(TAG, "click");

        if(twice){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            System.exit(0);
        }

        twice = true;
        Log.d(TAG, "twice: " + twice);

        Toast.makeText(getApplicationContext(), "Press BACK again to exit...", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                twice = false;
                Log.d(TAG, "twice: " + twice);
            }
        }, 3000);

        //this.moveTaskToBack(true);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                false;
        try {
            shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            startLocationPermissionRequest();
                        }
                    });

        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest();
        }
    }

    private void startLocationPermissionRequest() {
        try {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_LOCATION);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        try {
            Snackbar.make(this.getCurrentFocus().findViewById(android.R.id.content),
                    getString(mainTextStringId),
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(actionStringId), listener).show();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
