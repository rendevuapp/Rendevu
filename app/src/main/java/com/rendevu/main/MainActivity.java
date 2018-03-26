package com.rendevu.main;
/*
    Josh Davenport
 */
import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.util.NoSuchElementException;

public class MainActivity extends Activity {

    Intent i=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    public void login_register(View v)
    {
        try {
            switch(v.getId())
            {
                case R.id.log_in:
                    try {
                        i = new Intent(this, Login.class);
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
                    i=new Intent(this,Register.class);
                    startActivityForResult(i, 500);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
    *
    * Back button minimizes home screen
    * */
    @Override
    public void onBackPressed() {
        // Add the Back key handler here.
        this.moveTaskToBack(true);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
