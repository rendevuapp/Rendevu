package com.rendevu.main;
/*
    Josh Davenport
 */
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
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

}
