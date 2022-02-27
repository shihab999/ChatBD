package com.mss.chatbd.Activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mss.chatbd.Model.Post;
import com.mss.chatbd.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditPost extends AppCompatActivity {

    EditText postText;
    Button postBtn;
    TextView postSelectImg;
    ImageView postPreviewImg;
    Uri postImgUri;
    String postText_str,pushId;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    String currentUserId, time;

    Calendar calendar;
    SimpleDateFormat simpleDateFormat;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        postText = findViewById(R.id.postText);
        postBtn = findViewById(R.id.postBtn);
        postSelectImg = findViewById(R.id.postSelectImg);
        postPreviewImg = findViewById(R.id.postPreviewImg);

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("hh:mm a");
        time = simpleDateFormat.format(calendar.getTime());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait ...");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            currentUserId = firebaseUser.getUid();
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
        storageReference = FirebaseStorage.getInstance().getReference("PostImages").child(currentUserId);


        postText.addTextChangedListener(textWatcher);

        postSelectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,10);
            }
        });


        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

                postText_str = postText.getText().toString().trim();


                if(postImgUri != null){
                    StorageReference storageRef = storageReference.child("Img_"+System.currentTimeMillis()+"."+imageExtension(postImgUri));
                    storageRef.putFile(postImgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()){
                                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String img = String.valueOf(uri);

                                        pushId = databaseReference.push().getKey();
                                        Post post = new Post(pushId,postText_str,img,currentUserId,time);
                                        databaseReference.child(pushId).setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                progressDialog.dismiss();
                                                Toast.makeText(EditPost.this,"Post Create Successfully",Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });
                } else {
                    pushId = databaseReference.push().getKey();
                    String img = null;
                    Post post = new Post(pushId,postText_str,img,currentUserId,time);
                    databaseReference.child(pushId).setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressDialog.dismiss();
                            Toast.makeText(EditPost.this,"Post Create Successfully",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }

            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 10 && resultCode == RESULT_OK && data != null){
            postImgUri = data.getData();
            postPreviewImg.setVisibility(View.VISIBLE);
            postPreviewImg.setImageURI(postImgUri);
            postBtn.setEnabled(true);
        }
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length()>0){
                postBtn.setEnabled(true);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private String imageExtension (Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}