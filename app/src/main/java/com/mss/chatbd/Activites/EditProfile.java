package com.mss.chatbd.Activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mss.chatbd.Model.User;
import com.mss.chatbd.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {

    CircleImageView profile_image;
    EditText firstName, lastName;
    Button update_btn;
    String currentUserId, profileImage_url;
    Uri profileImage_uri;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        update_btn = findViewById(R.id.update_btn);
        profile_image = findViewById(R.id.profile_image);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            currentUserId = firebaseUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("User").child(currentUserId);
            storageReference = FirebaseStorage.getInstance().getReference("Profile_image").child(currentUserId);
        }

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                if (user != null) {
                    firstName.setText(user.getUserFirstName());
                    lastName.setText(user.getUserLastName());
                    if (user.getUserProfilePic() != null) {
                        Picasso.get()
                                .load(user.getUserProfilePic())
                                .into(profile_image);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

                String firstName_str = firstName.getText().toString().trim();
                String lastName_str = lastName.getText().toString().trim();


                if (profileImage_uri != null) {
                    StorageReference storageRef = storageReference.child("Profile_pic__" + firstName_str + " " + lastName_str + "." + imageExtension(profileImage_uri));

                    storageRef.putFile(profileImage_uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            if (task.isSuccessful()) {
                                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        profileImage_url = String.valueOf(uri);

                                        HashMap<String, Object> UpdateMap = new HashMap<>();
                                        UpdateMap.put("UserFirstName", firstName_str);
                                        UpdateMap.put("UserLastName", lastName_str);
                                        UpdateMap.put("UserProfilePic", profileImage_url);

                                        databaseReference.updateChildren(UpdateMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressDialog.dismiss();
                                                Toast.makeText(EditProfile.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        });

                                    }
                                });
                            }
                        }
                    });
                } else {
                    HashMap<String, Object> UpdateMap = new HashMap<>();
                    UpdateMap.put("UserFirstName", firstName_str);
                    UpdateMap.put("UserLastName", lastName_str);

                    databaseReference.updateChildren(UpdateMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            Toast.makeText(EditProfile.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }


            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 202);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 202) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    profileImage_uri = data.getData();
                    profile_image.setImageURI(profileImage_uri);
                }
            }
        }
    }

    private String imageExtension(Uri img) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(img));
    }
}

