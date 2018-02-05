package com.example.user.rendevu;
/*
    Josh Davenport

    This class is not used

 */
import android.app.Activity;
import android.os.Bundle;

public class Welcome extends Activity{

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
