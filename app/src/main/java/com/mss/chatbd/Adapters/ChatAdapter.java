package com.mss.chatbd.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.internal.ParcelableSparseIntArray;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mss.chatbd.Activites.Chat;
import com.mss.chatbd.Activites.ChatViewHolder;
import com.mss.chatbd.Model.ChatModel;
import com.mss.chatbd.Model.User;
import com.mss.chatbd.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {

    DatabaseReference databaseReference;

    Context context;
    List<ChatModel> chatList;
    FirebaseUser firebaseUser;
    String currentUserId, receiverProfileImg;

    final int senderId = 1;
    final int receiverId = 2;

    public ChatAdapter(Context context, List<ChatModel> chatList, String receiverProfileImg) {
        this.context = context;
        this.chatList = chatList;
        this.receiverProfileImg = receiverProfileImg;



        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            currentUserId = firebaseUser.getUid();
        }

    }


    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType==senderId){
            View view = LayoutInflater.from(context).inflate(R.layout.sender,parent,false);
            return new ChatViewHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.receiver,parent,false);
            return new ChatViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {

        ChatModel chatModel = chatList.get(position);

        holder.msgView.setText(chatModel.getMessage());

        Picasso.get().load(receiverProfileImg).into(holder.chatProfileImg);

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {

        if(currentUserId.equals(chatList.get(position).getSenderId())){
            return senderId;
        } else {
            return receiverId;
        }
    }
}
