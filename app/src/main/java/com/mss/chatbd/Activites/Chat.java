package com.mss.chatbd.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mss.chatbd.Adapters.ChatAdapter;
import com.mss.chatbd.Model.ChatModel;
import com.mss.chatbd.Model.User;
import com.mss.chatbd.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {
    Intent intent;
    String receiverId, senderId;
    CircleImageView profile_image, chatProfileImg;
    TextView fName,lName;
    EditText typeMassage;
    ImageView sendMassage;
    RecyclerView recyclerView;

    String receiverProfileImg;


    DatabaseReference databaseReference, chatReference;
    FirebaseUser firebaseUser;

    List<ChatModel> chatList;
    ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getSupportActionBar().hide();



        profile_image = findViewById(R.id.profile_image);
        chatProfileImg = findViewById(R.id.chatProfileImg);
        fName = findViewById(R.id.fName);
        lName = findViewById(R.id.lName);
        typeMassage = findViewById(R.id.typeMassage);
        sendMassage = findViewById(R.id.sendMassage);
        recyclerView = findViewById(R.id.recycler);
        chatList = new ArrayList<>();


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        intent = getIntent();
        receiverId = intent.getStringExtra("userId");
        if(firebaseUser != null){
            senderId = firebaseUser.getUid();
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(Chat.this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);


        databaseReference = FirebaseDatabase.getInstance().getReference("User").child(receiverId);
        chatReference = FirebaseDatabase.getInstance().getReference("Chats");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                fName.setText(user.getUserFirstName());
                lName.setText(user.getUserLastName());
                Picasso.get().load(user.getUserProfilePic()).into(profile_image);

                 receiverProfileImg = user.getUserProfilePic();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendMassage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = typeMassage.getText().toString().trim();
                String chatId = chatReference.push().getKey();

                HashMap<String,Object> chatMap = new HashMap<>();

                chatMap.put("SenderId",senderId);
                chatMap.put("ReceiverId",receiverId);
                chatMap.put("Message",msg);
                chatMap.put("ChatId",chatId);

                chatReference.child(chatId).setValue(chatMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            typeMassage.setText("");
                        }
                    }
                });
            }
        });


            chatReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    chatList.clear();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);

                        if (senderId.equals(chatModel.getSenderId()) && receiverId.equals(chatModel.getReceiverId()) ||
                                senderId.equals(chatModel.getReceiverId()) && receiverId.equals(chatModel.getSenderId())){

                            chatList.add(chatModel);
                        }
                    }


                    chatAdapter = new ChatAdapter(Chat.this,chatList,receiverProfileImg);
                    recyclerView.setAdapter(chatAdapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });




    }

}