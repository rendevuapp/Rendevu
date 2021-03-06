package com.rendevu.main;
/**
*
* Class for handling exceptions for the Logout method
* */

public class ExceptionClass extends Exception {

    private String errorCode;

    ExceptionClass(String message, String errorCode){
        super(message);
        this.errorCode=errorCode;
    }

    public String getErrorCode(){
        return this.errorCode;
    }
}
