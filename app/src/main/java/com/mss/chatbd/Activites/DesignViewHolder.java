package com.mss.chatbd.Activites;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mss.chatbd.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class DesignViewHolder extends RecyclerView.ViewHolder {

    public CircleImageView profileImage;
    public TextView firstName,lastName,email,message;


    public DesignViewHolder(@NonNull View itemView) {
        super(itemView);

        profileImage = itemView.findViewById(R.id.profile_image);
        firstName = itemView.findViewById(R.id.firstName);
        lastName = itemView.findViewById(R.id.lastName);
        email = itemView.findViewById(R.id.email);
        message = itemView.findViewById(R.id.message);

    }
}
