package com.mss.chatbd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mss.chatbd.Model.User;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {
    Intent intent;
    String userId;
    CircleImageView profile_image;
    TextView fName,lName;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getSupportActionBar().hide();




        profile_image = findViewById(R.id.profile_image);
        fName = findViewById(R.id.fName);
        lName = findViewById(R.id.lName);

        intent = getIntent();
        userId = intent.getStringExtra("userId");


        databaseReference = FirebaseDatabase.getInstance().getReference("User").child(userId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                fName.setText(user.getUserFirstName());
                lName.setText(user.getUserLastName());
                Picasso.get().load(user.getUserProfilePic()).into(profile_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}