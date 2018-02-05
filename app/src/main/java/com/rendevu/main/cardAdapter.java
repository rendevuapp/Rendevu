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

    List<Person> list;

    public cardAdapter(List<Person> list){
        this.list = list;
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView personName;
        TextView personAge;
        ImageView personPhoto;

        PersonViewHolder(View itemView){
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.card_view);
            personName = (TextView)itemView.findViewById(R.id.person_name);
            personAge = (TextView)itemView.findViewById(R.id.person_age);
            personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);

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
            personViewHolder.personName.setText(list.get(i).name);
            personViewHolder.personAge.setText(list.get(i).age);
            personViewHolder.personPhoto.setImageResource(list.get(i).photoID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount(){
        return list.size();
    }
}

