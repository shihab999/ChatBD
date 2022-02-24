package com.mss.chatbd.Activites;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.mss.chatbd.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatViewHolder extends RecyclerView.ViewHolder {
    public TextView msgView;
    public CircleImageView chatProfileImg;
    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);

        msgView = itemView.findViewById(R.id.msgView);
        chatProfileImg = itemView.findViewById(R.id.chatProfileImg);

    }
}
