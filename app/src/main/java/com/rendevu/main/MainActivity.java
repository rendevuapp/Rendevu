package com.rendevu.main;
/*
    Josh Davenport
 */
import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends Activity {
    private FirebaseAuth auth;
    Intent i=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        auth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
                if (auth.getCurrentUser() != null) {
                    Toast.makeText(this, "User: "+auth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
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
                    i=new Intent(this,Login.class);
                    startActivityForResult(i, 500);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    break;
                case R.id.register:
                    i=new Intent(this,Register.class);
                    startActivityForResult(i, 500);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    break;
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error switching pages", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
