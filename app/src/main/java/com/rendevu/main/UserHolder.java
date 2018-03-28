package com.rendevu.main;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by ricardo.cantu on 3/27/2018.
 */

public class UserHolder extends RecyclerView.ViewHolder{

    TextView fullName;
    TextView dob;
    TextView avail;

    public UserHolder(View itemView) {
        super(itemView);

        fullName = (TextView)itemView.findViewById(R.id.person_name);
        dob = (TextView)itemView.findViewById(R.id.person_age);
        avail = (TextView)itemView.findViewById(R.id.avail);
    }

    public void setName(String i){
        fullName.setText(i);
    }

    public void setDOB(String i){
        dob.setText(i);
    }

    public String setAvail(String i){
        avail.setText(i);
        return i;
    }

}
