package com.rendevu.main;
/*
    Josh Davenport
    -Class separated from Main2Activity
 */



/**
* Class for handling exceptions for the Logout method
* */
public class ExceptionClass extends Exception {

    private String errorCode;

    ExceptionClass(String message, String errorCode){
        super(message);
        this.errorCode=errorCode;
    }

    /*FirebaseAuthException(String message, String errorAuthCode){
        super(message);
        this.errorCode=errorAuthCode;
    }*/

    public String getErrorCode(){
        return this.errorCode;
    }
}
