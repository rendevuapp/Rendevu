package com.rendevu.main;
/**
 *   Ricardo Cantu
 *  This class holds the configurations for the tab view.
 *  This implements each of the three fragment.
 */

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

/**
 *  THIS IS THE FRAGMENT THAT CONTAINS THE VIEW FOR THE CONTACT TAB.
 *  Ricardo Cantu
 */
public  class contactActivity extends Fragment {
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    RecyclerView recyclerView;
    FirebaseRecyclerAdapter<User, UserHolder> adapter;

    private List<User> list;

    public contactActivity() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //innDatabaseQuarryRef.orderByChild("InnerCircleMembers").
        //contRef = database.getReference("User");
        //contRef.addValueEventListener(new ValueEventListener() {
        //    @Override
        //    public void onDataChange(DataSnapshot dataSnapshot) {
        //        list = new ArrayList<>();
        //        for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
        //            User value = dataSnapshot1.getValue(User.class);
        //            User fire = new User();
        //            String fullname = value.getFullName();
        //            String dob = value.getDOB();
        //            fire.setFullName(fullname);
        //            fire.setDOB(dob);
        //            list.add(fire);
        //        }
        //    }
        //    @Override
        //    public void onCancelled(DatabaseError databaseError) {
        //        Log.w("Error", "Failed to read Value.", databaseError.toException());
        //    }
        //});
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.contact_tab, container, false);


        database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        databaseReference = database.getReference().child("UserData").child(uid).child("CircleMembers");
        Query query = databaseReference.orderByKey();

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        FirebaseRecyclerOptions recyclerOptions = new FirebaseRecyclerOptions.Builder<User>().setQuery(query, User.class).build();

        adapter = new FirebaseRecyclerAdapter<User, UserHolder>(recyclerOptions){

            @Override
            protected void onBindViewHolder(@NonNull UserHolder holder, int position, @NonNull User model) {
                holder.setName(model.getFullName());
                holder.setDOB(model.getDOB());
                holder.setAvail(model.getAvail());

                String avail = holder.setAvail(model.getAvail());

                if(avail.equals("true")){
                    holder.setAvail("Available");
                }
                else if(avail.equals("false")) {
                    holder.setAvail("Not Available");
                }
            }

            @Override
            public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item,parent, false);
                return new UserHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        return rootView;
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
