package com.rendevu.main;
/*
    Josh Davenport
 */


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private EditText loginEmail, loginPass;
    private FirebaseAuth auth;
    //private ProgressBar progressBar;
    private Button btnSignup, start, btnReset;
    private ImageView hidePass=null;
    boolean flag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*if (auth.getCurrentUser() != null) {
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }*/

        // set the view now
        setContentView(R.layout.login);

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        hidePass = findViewById(R.id.show_hide2);
        loginEmail = (EditText) findViewById(R.id.email_id2);
        loginPass = (EditText) findViewById(R.id.password2);
        //progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.backRegister);
        start = (Button) findViewById(R.id.btnLogin);
        btnReset = (Button) findViewById(R.id.forgotPass);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        /*
        * Josh
        * adding try/catch to navigation and button actions
        * */
        try {
            //to go back to the register page
            btnSignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Login.this, Register.class));
                    finish(); //closes current activity before moving to the next.
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error switching pages", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        try {
            btnReset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Login.this, ResetPassword.class));
                    finish(); //closes current activity before moving to the next.
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error switching pages", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        try {
            //show or hide the password
            hidePass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {

                    if(flag==false)
                    {
                        hidePass.setImageResource(R.drawable.hide);
                        loginPass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        flag=true;
                    }
                    else
                    {
                        hidePass.setImageResource(R.drawable.show);
                        loginPass.setInputType(129);
                        flag=false;

                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Cannot show password", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        try {
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = loginEmail.getText().toString();
                    final String password = loginPass.getText().toString();

                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(password)) {
                        Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //progressBar.setVisibility(View.VISIBLE);

                    //authenticate user with firebase
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    //progressBar.setVisibility(View.GONE);
                                    if (!task.isSuccessful()) {
                                        // there was an error
                                        if (password.length() < 6) {
                                            loginPass.setError("Invalid Password!");
                                        } else {
                                            Toast.makeText(Login.this, "Authentication failed, check your email or password...", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Intent intent = new Intent(Login.this, Main2Activity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Authentication process failed, cannot login", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}





















/*import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Login extends Activity{
    Intent i=null;
    ImageView im=null;
    EditText loginEmail, loginPass;
    boolean flag=false;
    SQLiteDatabase db=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        im=(ImageView)findViewById(R.id.show_hide2);
        loginEmail=(EditText)findViewById(R.id.loginEmail);
        loginPass=(EditText)findViewById(R.id.password2);
        db=openOrCreateDatabase("mydb", MODE_PRIVATE, null);
        //	db.execSQL("create table if not exists login(name varchar,mobile_no varchar,email_id varchar,password varchar,flag varchar)");

        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if(flag==false)
                {
                    im.setImageResource(R.drawable.hide);
                    loginPass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    flag=true;
                }
                else
                {
                    im.setImageResource(R.drawable.show);
                    tv4.setInputType(129);
                    flag=false;

                }
            }
        });
    }

    public void action(View v)
    {
        switch(v.getId())
        {
            case R.id.signin2:
                i=new Intent(this,Register.class);
                startActivityForResult(i, 500);
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
                finish();
                break;
            case R.id.start:
                String username=tv6.getText().toString();
                String password=tv4.getText().toString();
                if(username==null||username==""||username.length()<3)
                {
                    show("Please Enter Correct Username.");
                }
                else if(password==null||password==""||password.length()<6)
                {
                    show("Please Enter Correct Password.");
                }
                else
                {
                    Cursor c=db.rawQuery("select * from login where username='"+username+"' and password='"+password+"'",null);
                    c.moveToFirst();
                    if(c.getCount()>0)
                    {
                        i=new Intent(this,Main2Activity.class);
                        startActivityForResult(i,500);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        db.close();
                        finish();
                    }
                    else
                        show("Wrong Username or Password.");

                }
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void show(String str)
    {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }

}*/
