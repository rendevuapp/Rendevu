package com.example.user.rendevu;

/**
 * Josh Davenport
 *
 * Pop-up dialog fragment for adding contacts to user's profile
 * --Not yet fully functional--
 */

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;


public class MyDialogFragment extends DialogFragment implements TextView.OnEditorActionListener {

    private EditText nameEditText;
    private EditText numberEditText;


    public interface UserNameListener {
        void onFinishUserDialog(String addedName, String addedNum);
    }

    // Empty constructor required for DialogFragment
    public MyDialogFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        try {
            view = inflater.inflate(R.layout.fragment_addcontact, container);
            nameEditText = (EditText) view.findViewById(R.id.addName);
            numberEditText = (EditText) view.findViewById(R.id.addNum);

            // set this instance as callback for editor action
            nameEditText.setOnEditorActionListener(this);
            nameEditText.requestFocus();
            getDialog().getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            getDialog().setTitle("Please enter username");

            numberEditText.setOnEditorActionListener(this);
            numberEditText.requestFocus();
            getDialog().getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            getDialog().setTitle("Please enter phone number");
        } catch (Exception e) {
            e.printStackTrace();
        }


        return view;
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        // Return input text to activity
        try {
            UserNameListener activity = (UserNameListener) getActivity();
            activity.onFinishUserDialog(nameEditText.getText().toString(), numberEditText.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.dismiss();
        return true;
    }
}
