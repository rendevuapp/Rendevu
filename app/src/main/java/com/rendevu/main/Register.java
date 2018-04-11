package com.rendevu.main;
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
import android.widget.ViewSwitcher;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;


public class Register extends UncaughtExceptionActivity {
    //private static final String TAG = "Register";

    private DatabaseReference myDatabaseReference, userDatRef;

    private FirebaseDatabase mFirebaseInstance;

    private String userId;

    private EditText fullName, inputEmail, phoneNum,
            loginPass, dateofBirth, userName;
    private ImageView hidePass=null;
    private Button btnSignUp;
    private ProgressBar progressBar;
    private DatePickerDialog datePickerDialog;
    boolean flag=false;

    /**
     * The ViewSwitcher to switch between the login buttons and the progress indicator
     */
    private ViewSwitcher mSwitcher;

    /*
    * User is authenticated through Firebase
    * */
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        try {
            //Get Firebase auth instance
            auth = FirebaseAuth.getInstance();

            /**
             * Josh
             * Adding persistence for data stored in firebase.
             * also gets unique id for current user
             * */
            //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

            mFirebaseInstance = FirebaseDatabase.getInstance();
            mFirebaseInstance.setPersistenceEnabled(true);

            myDatabaseReference=FirebaseDatabase.getInstance().getReference("User");
            userDatRef = FirebaseDatabase.getInstance().getReference("UserData");
        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), "Firebase error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        hidePass = findViewById(R.id.show_hide);
        fullName = findViewById(R.id.fullname);
        inputEmail = findViewById(R.id.email_id);
        phoneNum = findViewById(R.id.userPhone);
        loginPass = findViewById(R.id.password);
        dateofBirth = findViewById(R.id.dateOfBirth);
        userName = findViewById(R.id.username);
        btnSignUp = findViewById(R.id.register);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        try {
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
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Cannot set date of birth", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        try {
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
            btnSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String email = inputEmail.getText().toString().trim();
                    final String password = loginPass.getText().toString().trim();

                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(getApplicationContext(), "Enter your email address!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(password)) {
                        Toast.makeText(getApplicationContext(), "Enter a password!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    /*
                    * create user and store data in authorization database
                    * */
                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Toast.makeText(Register.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                    //progressBar.setVisibility(View.GONE);

                                    // If registration fails, display exception handler message to the user. If registration succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // registered user can be handled in the listener.
                                    if (!task.isSuccessful()) {

                                        //clear the invalid input
                                        loginPass.setText(null);
                                        inputEmail.setText(null);

                                        try {
                                            final Exception exception = task.getException();
                                            if(exception == null) {
                                                //exception for no error code found
                                                throw new NullPointerException("exception is null");
                                            }
                                            throw exception;
                                        } catch(FirebaseAuthWeakPasswordException e) {
                                            //for a password less than 6 characters
                                            loginPass.setError(getString(R.string.error_weak_password));
                                            loginPass.requestFocus();
                                        } catch(FirebaseAuthInvalidCredentialsException e) {
                                            inputEmail.setError(getString(R.string.error_invalid_email));
                                            inputEmail.requestFocus();
                                        } catch(FirebaseAuthUserCollisionException e) {
                                            inputEmail.setError(getString(R.string.error_user_exists));
                                            inputEmail.requestFocus();
                                        } catch(Exception e) {
                                            //default message also displayed on bottom of screen
                                            Toast.makeText(Register.this, "Registration failed." + task.getException(),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        startActivity(new Intent(Register.this, Main2Activity.class));
                                        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                                        mFirebaseInstance.setPersistenceEnabled(true);
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        String uid = user.getUid();
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        String CircleCode = database.getReference().push().getKey();
                                        addUser(uid, CircleCode, ((EditText)findViewById(R.id.fullname)).getText().toString(),
                                                ((EditText)findViewById(R.id.username)).getText().toString(),
                                                ((EditText)findViewById(R.id.email_id)).getText().toString(),
                                                ((EditText)findViewById(R.id.dateOfBirth)).getText().toString(),
                                                Integer.parseInt(((EditText)findViewById(R.id.userPhone)).getText().toString()));
                                    }
                                }
                            });
                }
            });
        } catch (Exception e) {
            //catches any residual errors
            Toast.makeText(getApplicationContext(), "Registration failure", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    private void addUser(String uid, String CircleCode, String fullname, String username,
                            String email, String dob, int phoneNumber){
        String userId = uid;
        String code = CircleCode;
        User user = new User(fullname, username, email, dob, phoneNumber);
        myDatabaseReference.child(userId).setValue(user);
        userDatRef.child(userId).child("fullname").setValue(fullname);
        userDatRef.child(userId).child("avail").setValue("false");
        userDatRef.child(userId).child("lat").setValue("0");
        userDatRef.child(userId).child("lng").setValue("0");
        userDatRef.child(userId).child("CircleCode").setValue(code);
    }

    @Override
    protected void onResume() {
        super.onResume();
		progressBar.setVisibility(View.GONE);
    }


    /*
    * Josh
    *   Back button from login or register closes firebase,
    *   and takes you back to home screen.
    *
    * */
    @Override
    public void onBackPressed() {
        throw new RuntimeException("this will cause a crash");
        // Add the Back key handler here.
        /*FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(Register.this, MainActivity.class);
        startActivity(intent);
        finish();*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}

