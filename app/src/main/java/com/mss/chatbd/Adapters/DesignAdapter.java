package com.mss.chatbd.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.mss.chatbd.Activites.DesignViewHolder;
import com.mss.chatbd.Chat;
import com.mss.chatbd.Fragments.friendsFragment;
import com.mss.chatbd.Model.User;
import com.mss.chatbd.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
public class DesignAdapter extends RecyclerView.Adapter<DesignViewHolder> {

    Context context;
    List<User> userList;

    public DesignAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public DesignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.design,parent,false);

        return new DesignViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DesignViewHolder holder, int position) {

        User user = userList.get(position);

        holder.firstName.setText(user.getUserFirstName());
        holder.lastName.setText(user.getUserLastName());
        holder.email.setText(user.getUserEmail());

        Picasso.get().load(user.getUserProfilePic()).into(holder.profileImage);

        holder.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Chat.class);
                intent.putExtra("userId",user.getUserId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
