package com.example.user.rendevu;
/*
    Josh Davenport
 */
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;


public class Register extends AppCompatActivity {

    private DatabaseReference myDatabaseReference;
    private String userId;

    private EditText fullName, inputEmail, phoneNum,
            loginPass, dateofBirth, userName;
    private ImageView hidePass=null;
    private Button btnSignUp;
    private ProgressBar progressBar;
    private DatePickerDialog datePickerDialog;
    boolean flag=false;

    /*
    * User is authenticated through Firebase
    * */
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        /**
         * Josh
         * Adding persistence for data stored in firebase.
         * also gets unique id for current user
         * */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        myDatabaseReference=FirebaseDatabase.getInstance().getReference("User");
        userId= myDatabaseReference.push().getKey();


        hidePass = findViewById(R.id.show_hide);
        fullName = findViewById(R.id.fullname);
        inputEmail = findViewById(R.id.email_id);
        phoneNum = findViewById(R.id.userPhone);
        loginPass = findViewById(R.id.password);
        dateofBirth = findViewById(R.id.dateOfBirth);
        userName = findViewById(R.id.username);
        btnSignUp = findViewById(R.id.register);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        dateofBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

                // date picker dialog
                datePickerDialog = new DatePickerDialog(Register.this, android.R.style.Theme_Holo_Dialog,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                dateofBirth.setText((monthOfYear + 1) + "/"
                                        + dayOfMonth + "/" + year);

                                //Set max limit for no future dates.

                            }
                        }, mYear, mMonth, mDay);  //Use month/day/year to calculate age
                datePickerDialog.show();
                //}
            }
        });

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

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();
                String password = loginPass.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

				progressBar.setVisibility(View.VISIBLE);

                /*
                * create user and store data in authorization database
                * */
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(Register.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
								progressBar.setVisibility(View.GONE);

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(Register.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivity(new Intent(Register.this, Main2Activity.class));
                                    finish();
                                }
                            }
                        });

                addUser(((EditText)findViewById(R.id.fullname)).getText().toString(),
                        ((EditText)findViewById(R.id.username)).getText().toString(),
                        ((EditText)findViewById(R.id.email_id)).getText().toString(),
                        ((EditText)findViewById(R.id.dateOfBirth)).getText().toString(),
                        Integer.parseInt(((EditText)findViewById(R.id.userPhone)).getText().toString()));

            }
        });
    }


    private void addUser(String fullname, String username,
                            String email, String dob, int phoneNumber){
        User user = new User(fullname, username, email, dob, phoneNumber);
        myDatabaseReference.child(userId).setValue(user);
    }

    @Override
    protected void onResume() {
        super.onResume();
		progressBar.setVisibility(View.GONE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}

