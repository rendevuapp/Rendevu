package com.rendevu.main;
/*
    Ricardo Cantu
    This class holds adapter configurations for the recycle view
 */
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;



public class cardAdapter extends RecyclerView.Adapter<cardAdapter.PersonViewHolder> {

    List<User> list;

    public cardAdapter(List<User> list){
        this.list = list;
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder{
        TextView fullname, dob;
        CardView cardView;
        ImageView userPhoto;

        PersonViewHolder(View itemView){
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.card_view);
            fullname = (TextView)itemView.findViewById(R.id.person_name);
            dob = (TextView)itemView.findViewById(R.id.person_age);
            userPhoto = (ImageView)itemView.findViewById(R.id.person_photo);

        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        try {
            super.onAttachedToRecyclerView(recyclerView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        PersonViewHolder personViewHolder = null;
        try {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_item, viewGroup, false);
            personViewHolder = new PersonViewHolder(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return personViewHolder;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i){
        try {
            User mylist = list.get(i);
            personViewHolder.fullname.setText(mylist.getFullName());
            personViewHolder.dob.setText(mylist.getDOB());
            personViewHolder.userPhoto.setImageResource(R.drawable.contact_pic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount(){`
        int arr = 0;
        try{
            if(list.size() == 0)
                arr = 0;
            else
                arr = list.size();
        }catch (Exception e){
            e.printStackTrace();
        }
        return arr;
    }
}

