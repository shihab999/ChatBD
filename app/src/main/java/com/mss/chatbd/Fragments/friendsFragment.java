package com.mss.chatbd.Fragments;

import static com.airbnb.lottie.L.TAG;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mss.chatbd.Adapters.DesignAdapter;
import com.mss.chatbd.Model.User;
import com.mss.chatbd.R;

import java.util.ArrayList;
import java.util.List;


public class friendsFragment extends Fragment {

    String currentuser;
    List<User> userList;
    DatabaseReference databaseReference;
    DesignAdapter designAdapter;
    RecyclerView recyclerView;
    FirebaseUser firebaseUser;


    public friendsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        userList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null){
            currentuser = firebaseUser.getUid();
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();

                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);

                    if(!user.getUserId().equals(currentuser)){
                        userList.add(user);
                    }

                }

                designAdapter = new DesignAdapter(getActivity(),userList);
                recyclerView.setAdapter(designAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;

    }

}