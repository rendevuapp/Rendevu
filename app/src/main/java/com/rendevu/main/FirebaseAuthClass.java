package com.rendevu.main;

import com.google.firebase.FirebaseException;

public class FirebaseAuthClass extends FirebaseException{

    private String ErrorCode;


    FirebaseAuthClass (String message, String errorAuthCode){
        super(message);
        this.ErrorCode=errorAuthCode;
    }

    public String getErrorCode (){return this.ErrorCode;}
}
