package com.mss.chatbd.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mss.chatbd.Activites.PostViewHolder;
import com.mss.chatbd.Activites.Profile;
import com.mss.chatbd.Model.Post;
import com.mss.chatbd.Model.User;
import com.mss.chatbd.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {

    Context context;
    List<Post> postList;

    DatabaseReference databaseReference;

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post,parent,false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);

        if (post.getPostText() != null){
            holder.postText.setText(post.getPostText());
            holder.postText.setVisibility(View.VISIBLE);
        }
        if (post.getPostImg()!= null ){
            Picasso.get().load(post.getPostImg()).into(holder.postImage);
            holder.postImage.setVisibility(View.VISIBLE);
        }


        databaseReference = FirebaseDatabase.getInstance().getReference("User").child(post.getPostCreatorId());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                Picasso.get().load(user.getUserProfilePic()).into(holder.profile_image);
                holder.firstName.setText(user.getUserFirstName());
                holder.lastName.setText(user.getUserLastName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}
