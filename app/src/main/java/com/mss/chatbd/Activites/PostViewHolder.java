package com.mss.chatbd.Activites;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mss.chatbd.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostViewHolder extends RecyclerView.ViewHolder {

    public CircleImageView profile_image;
    public TextView firstName,lastName,postText;
    public ImageView postImage;

    public PostViewHolder(@NonNull View itemView) {
        super(itemView);

        profile_image = itemView.findViewById(R.id.profile_image);
        firstName = itemView.findViewById(R.id.firstName);
        lastName = itemView.findViewById(R.id.lastName);
        postText = itemView.findViewById(R.id.postText);
        postImage = itemView.findViewById(R.id.postImage);
    }
}
