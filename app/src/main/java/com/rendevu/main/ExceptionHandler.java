package com.rendevu.main;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
/*
* Josh
*
* New Global Exception Handler Class
*   -Triggers alert dialog for specific type
*    of exception, including options for
*    recovery, if necessary.
*
* */

public class ExceptionHandler {

    public static void makeExceptionAlert(Context context, Exception ex) {
        String headerText = "";
        String messageText = "";

        if (ex instanceof ArrayIndexOutOfBoundsException) {
            headerText = context.getText(R.string.error).toString();
            messageText = context.getText(R.string.ArrayIndexOutOfBoundsException).toString();
        } else if (ex instanceof ClassNotFoundException) {
            headerText = context.getText(R.string.error).toString();
            messageText = context.getText(R.string.ClassNotFoundException).toString();
        } else if (ex instanceof IndexOutOfBoundsException) {
            headerText = context.getText(R.string.error).toString();
            messageText = context.getText(R.string.IndexOutOfBoundsException).toString();
        } else if (ex instanceof NullPointerException) {
            headerText = context.getText(R.string.error).toString();
            messageText = context.getText(R.string.NullPointerException).toString();
        } else if (ex instanceof NumberFormatException) {
            headerText = context.getText(R.string.error).toString();
            messageText = context.getText(R.string.NumberFormatException).toString();
        } else if (ex instanceof RuntimeException) {
            headerText = context.getText(R.string.error).toString();
            messageText = context.getText(R.string.RuntimeException).toString();
        } /*else if (ex instanceof ActivityNotFoundException) {
            headerText = context.getText(R.string.error).toString();
            messageText = context.getText(R.string.ActivityNotFoundException).toString();
        } */else if (ex instanceof FirebaseAuthWeakPasswordException) {
            headerText = context.getText(R.string.error).toString();
            messageText = context.getText(R.string.FirebaseAuthWeakPasswordException).toString();
        } else if (ex instanceof FirebaseAuthInvalidCredentialsException) {
            headerText = context.getText(R.string.error).toString();
            messageText = context.getText(R.string.FirebaseAuthInvalidCredentialsException).toString();
        } else if (ex instanceof FirebaseAuthUserCollisionException) {
            headerText = context.getText(R.string.error).toString();
            messageText = context.getText(R.string.FirebaseAuthUserCollisionException).toString();
        } else {
            headerText = context.getText(R.string.error).toString();
            messageText = context.getText(R.string.Exception).toString();
        }
        showErrorDialog(context, headerText, messageText);
    }

    private static void showErrorDialog(Context context, String titletext,
                                        String messagetext) {
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(titletext)
                .setMessage(messagetext)
                .setPositiveButton(R.string.error_close,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }

                        }).show();
    }

    public static void exceptionThrower(Exception ex) throws Exception {
        if (ex != null) {
            if (ex instanceof ArrayIndexOutOfBoundsException) {
                throw ex;
            } else if (ex instanceof ClassNotFoundException) {
                throw ex;
            } else if (ex instanceof IndexOutOfBoundsException) {
                throw ex;
            } else if (ex instanceof NullPointerException) {
                throw ex;
            } else if (ex instanceof NumberFormatException) {
                throw ex;
            } else if (ex instanceof RuntimeException) {
                throw ex;
            } /*else if (ex instanceof ActivityNotFoundException) {
                throw (ActivityNotFoundException) ex;
            } */else if (ex instanceof FirebaseAuthWeakPasswordException) {
                throw ex;
            } else if (ex instanceof FirebaseAuthInvalidCredentialsException) {
                throw ex;
            } else if (ex instanceof FirebaseAuthUserCollisionException) {
                throw ex;
            } else {
                throw ex;
            }
        }
    }
}
