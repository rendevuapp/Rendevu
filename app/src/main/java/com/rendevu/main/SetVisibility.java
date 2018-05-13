package com.rendevu.main;

/**
 *
 * Rick Cantu
 * Pop-up dialog fragment for setting user's visibility
 *
 */

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SetVisibility extends DialogFragment {

    // Global database references
    DatabaseReference userDatabaseReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    // UID string based on the current logged on user
    String uid = user.getUid();

    // Empty constructor required for DialogFragment
    public SetVisibility() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        final FirebaseDatabase database;

        try {
            // Defining the reference's location on the database
            database = FirebaseDatabase.getInstance();
            userDatabaseReference = database.getReference().child("UserData").child(uid);

            // getting the view layount from dialog_addcontact
            view = inflater.inflate(R.layout.dialog_setvisibility, container);

            // Button to add the contact to the specified circle.
            final Button setVisibilityButton = (Button) view.findViewById(R.id.close_setvisability_button);

            // Spinner to select what circle the user will be placed in.
            Spinner spinner = (Spinner) view.findViewById(R.id.setvisibility_spinner);

            // Creates the dialog window
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            alertDialogBuilder.setTitle("Set your visibility.").create();

            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.visibility_array, android.R.layout.simple_spinner_item);

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

                    String visibility = parent.getItemAtPosition(position).toString();
                    //current user's uid
                    final String uid = user.getUid();
                    int selection = 0;

                    if(visibility.equals("No one")){
                        selection = 0;
                    }
                    else if(visibility.equals("My inner circle")){
                        selection = 1;
                    }
                    else if(visibility.equals("My outer circle")){
                        selection = 2;
                    }
                    else if(visibility.equals("Everyone in my circles")){
                        selection = 3;
                    }

                    final int finalSelection = selection;
                    setVisibilityButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            userDatabaseReference.child("availability").setValue(finalSelection);
                            getDialog().dismiss();
                        }
                    });
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
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
