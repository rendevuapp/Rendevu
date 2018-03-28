package com.rendevu.main;

/**
 * Josh Davenport
 *
 * Pop-up dialog fragment for adding contacts to user's profile
 *
 */

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyDialogFragment extends DialogFragment {

    private EditText codeEditText;

    // Empty constructor required for DialogFragment
    public MyDialogFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        final FirebaseDatabase database;
        final DatabaseReference userDatabaseReference, quarryDatabaseReference;
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        try {
            database = FirebaseDatabase.getInstance();
            userDatabaseReference = database.getReference().child("UserData").child(uid);
            quarryDatabaseReference = database.getReference().child("UserData");

            view = inflater.inflate(R.layout.fragment_addcontact, container);
            codeEditText = (EditText) view.findViewById(R.id.addCode);


            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            alertDialogBuilder.setTitle("Please enter user code.").create();

            final RadioButton outerCircle = (RadioButton) view.findViewById(R.id.radioButton_outerCircle);
            final RadioButton innerCircle = (RadioButton) view.findViewById(R.id.radioButton_innerCircle);

            Button addContactButton = (Button) view.findViewById(R.id.addContactButton);
            addContactButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String circleCode = codeEditText.getText().toString();
                    quarryDatabaseReference.orderByChild("CircleCode").equalTo(circleCode).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                String key = dataSnapshot1.getKey();
                                String displayName = dataSnapshot1.child("fullname").getValue().toString();
                                String dob = dataSnapshot1.child("dob").getValue().toString();
                                if(innerCircle.isChecked()) {
                                    userDatabaseReference.child("CircleMembers").child(key).child("avail").setValue("false");
                                    userDatabaseReference.child("CircleMembers").child(key).child("fullname").setValue(displayName);
                                    userDatabaseReference.child("CircleMembers").child(key).child("Circle").setValue("innerCircle");
                                    userDatabaseReference.child("CircleMembers").child(key).child("dob").setValue(dob);
                                    getDialog().dismiss();
                                }
                                else if(outerCircle.isChecked()){
                                    userDatabaseReference.child("CircleMembers").child(key).child("avail").setValue("false");
                                    userDatabaseReference.child("CircleMembers").child(key).child("fullname").setValue(displayName);
                                    userDatabaseReference.child("CircleMembers").child(key).child("Circle").setValue("outerCircle");
                                    userDatabaseReference.child("CircleMembers").child(key).child("dob").setValue(dob);
                                    getDialog().dismiss();
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            getDialog().dismiss();
                        }
                    });
                }
            });
            Button cancelButton = (Button) view.findViewById(R.id.cancelButton);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getDialog().dismiss();
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
}
