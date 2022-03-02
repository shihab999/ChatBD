package com.mss.chatbd.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mss.chatbd.Adapters.PostAdapter;
import com.mss.chatbd.Model.Post;
import com.mss.chatbd.Model.User;
import com.mss.chatbd.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {

    CircleImageView profile_image;
    TextView name, firstName, lastName, email, userEmail;
    String currentUserId;
    List<Post> postList;
    RecyclerView recyclerView;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference, postDatabaseReference;
    PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        profile_image = findViewById(R.id.profile_image);
        name = findViewById(R.id.name);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        userEmail = findViewById(R.id.userEmail);
        postList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null){
            currentUserId = firebaseUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("User").child(currentUserId);
        }

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                if(user != null){
                    firstName.setText(user.getUserFirstName());
                    lastName.setText(user.getUserLastName());
                    userEmail.setText(user.getUserEmail());
                    if(user.getUserProfilePic()!=null){
                        Picasso.get()
                                .load(user.getUserProfilePic())
                                .into(profile_image);
                    } else {
                        profile_image.setImageDrawable(getResources().getDrawable(R.drawable.profile));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        postDatabaseReference = FirebaseDatabase.getInstance().getReference("Posts");
        postDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Post post = dataSnapshot.getValue(Post.class);

                    if (currentUserId.equals(post.getPostCreatorId())){
                        postList.add(post);
                    }

                }
                postAdapter = new PostAdapter(Profile.this,postList);
                recyclerView.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(Profile.this,EditProfile.class));
        return super.onOptionsItemSelected(item);
    }
}