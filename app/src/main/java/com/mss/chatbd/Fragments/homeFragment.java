package com.mss.chatbd.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mss.chatbd.Activites.EditPost;
import com.mss.chatbd.Activites.Profile;
import com.mss.chatbd.Adapters.PostAdapter;
import com.mss.chatbd.Model.Post;
import com.mss.chatbd.Model.User;
import com.mss.chatbd.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class homeFragment extends Fragment {

    CircleImageView topProfile_image;
    TextView postText;
    ImageView postImg;
    List<Post> postList;
    RecyclerView recyclerView;
    String currentUserId;
    PostAdapter postAdapter;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference, postDatabaseReference;

    public homeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        topProfile_image =view.findViewById(R.id.topProfile_image);
        postText =view.findViewById(R.id.postText);
        postImg =view.findViewById(R.id.postImg);
        recyclerView =view.findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        postList = new ArrayList<>();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            currentUserId = firebaseUser.getUid();
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("User").child(currentUserId);
        postDatabaseReference = FirebaseDatabase.getInstance().getReference("Posts");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                if (user.getUserProfilePic() != null){
                    Picasso.get().load(user.getUserProfilePic()).into(topProfile_image);
                } else {
                    topProfile_image.setImageDrawable(getResources().getDrawable(R.drawable.profile));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        topProfile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),Profile.class));
            }
        });

        postText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),EditPost.class));
            }
        });

        postImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),EditPost.class));
            }
        });


        postDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                postList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Post post = dataSnapshot.getValue(Post.class);

                    postList.add(post);
                }

                postAdapter = new PostAdapter(getActivity(),postList);
                recyclerView.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






        return view;
    }
}