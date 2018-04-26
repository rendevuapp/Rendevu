package com.rendevu.main;

/**
 * Josh Davenport
 * Rick Cantu
 * Pop-up dialog fragment for adding contacts to user's profile
 *
 */

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddUserToCircleDialog extends DialogFragment {

    private EditText codeEditText;

    // Global database references
    DatabaseReference userDatabaseReference, quarryDatabaseReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    // UID string based on the current logged on user
    String uid = user.getUid();

    // circle string that holds the value of the circle the user will be added to
    String circle = "";


    // Empty constructor required for DialogFragment
    public AddUserToCircleDialog() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        final FirebaseDatabase database;

        try {
            // Defining the reference's location on the database
            database = FirebaseDatabase.getInstance();
            userDatabaseReference = database.getReference().child("UserData").child(uid);
            quarryDatabaseReference = database.getReference().child("UserData");

            // getting the view layount from fragment_addcontact
            view = inflater.inflate(R.layout.fragment_addcontact, container);

            // defining a text field for user input, and referencing ot from the layout
            codeEditText = (EditText) view.findViewById(R.id.addCode);



            // Button to add the contact to the specified circle.
            final Button addContactButton = (Button) view.findViewById(R.id.addContactButton);

            // Setting the button to inactive by default
            addContactButton.setAlpha(.5f);
            addContactButton.setClickable(false);

            /**
             *
             * This listener detects if codeEditText has a value in it.  If it does not,
             * the button remains inactive, but if codeEditText does have a value, the
             * button is made active.
             *
             */
            codeEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    addContactButton.setAlpha(.5f);
                    addContactButton.setClickable(false);
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.toString().trim().length() == 0) {
                        addContactButton.setAlpha(0.5f);
                        addContactButton.setClickable(false);
                    }
                    else{
                        addContactButton.setAlpha(1.0f);
                        addContactButton.setClickable(true);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            // Button to cancel the activity
            final Button cancelButton = (Button) view.findViewById(R.id.cancelButton);

            // Spinner to select what circle the user will be placed in.
            Spinner spinner = (Spinner) view.findViewById(R.id.circle_spinner);

            // Creates the dialog window
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            alertDialogBuilder.setTitle("Please enter user code.").create();

            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.circle_name_array, android.R.layout.simple_spinner_item);

            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Apply the adapter to the spinner
            spinner.setAdapter(adapter);

            /**
             *
             * Listener that makes sure a circle is selected from the spinner.  If an item is selected,
             * the item from the spinner is set to circle, the code in codeEditText is set to circleCode
             * then the circle code is searched in a database snapshot.  Once it is found, UID that
             * circle code belongs to is added to the user's specified circle on the database.  Lastly,
             * the code is deleted from the user's list of codes so that it is no longer a valid code
             * and the dialog is dismissed.
             *
             */
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    circle = parent.getItemAtPosition(position).toString();
                    addContactButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final String circleCode = codeEditText.getText().toString();
                            quarryDatabaseReference.orderByChild("CircleCodes").equalTo(circleCode).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                        String key = dataSnapshot1.getKey();
                                        userDatabaseReference.child("CircleMembers").child(key).child("Circle").setValue(circle);
                                        quarryDatabaseReference.child("CircleCodes").child(circleCode).removeValue();
                                        getDialog().dismiss();
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    getDialog().dismiss();
                                }
                            });
                        }
                    });
                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getDialog().dismiss();
                        }
                    });
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    addContactButton.setAlpha(.5f);
                    addContactButton.setClickable(false);
                }
            });

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
}
