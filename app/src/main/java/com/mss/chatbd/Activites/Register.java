package com.mss.chatbd.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mss.chatbd.R;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    TextInputEditText firstName, lastName, register_email, register_password;
    AppCompatButton createAccount;
    TextView alreadyAcc;
    String currentUser;


    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        register_email = findViewById(R.id.register_email);
        register_password = findViewById(R.id.register_password);
        createAccount = findViewById(R.id.createAccount);
        alreadyAcc = findViewById(R.id.alreadyAcc);


        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");


        alreadyAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, LogIn.class));
                finish();

            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fName = firstName.getText().toString().trim();
                String lName = lastName.getText().toString().trim();
                String email = register_email.getText().toString().trim();
                String password = register_password.getText().toString().trim();

                if (fName.isEmpty()) {
                    ShowError(firstName,"First name can't be empty");
                } else if (lName.isEmpty()) {
                    ShowError(lastName,"Last name can't be empty");
                } else if (email.isEmpty()) {
                    ShowAlert("Email can't be empty");
                } else if (password.isEmpty()) {
                    ShowAlert("Password can't be empty");
                }else if (password.length()<6) {
                    ShowAlert("Password should be more than 6");
                }else {
                    progressDialog.show();
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                firebaseUser = firebaseAuth.getCurrentUser();
                                if(firebaseUser!= null){
                                    currentUser = firebaseUser.getUid();
                                }

                                HashMap<String,String> userMap = new HashMap<>();
                                userMap.put("UserFirstName",fName);
                                userMap.put("UserLastName",lName);
                                userMap.put("UserEmail",email);
                                userMap.put("UserPassword",password);
                                userMap.put("UserId",currentUser);

                                databaseReference.child(currentUser).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.dismiss();
                                        if(task.isSuccessful()){

                                            Toast.makeText(Register.this, "Sucessful", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(Register.this,LogIn.class));
                                            finish();

                                        }
                                    }
                                });

                            } else {
                                progressDialog.dismiss();
                                String error = task.getException().getLocalizedMessage();
                                ShowAlert(error);
                            }
                        }
                    });
                }


            }
        });

    }

    private void ShowError(TextInputEditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }


    private void ShowAlert(String s) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
        builder.setTitle("Error");
        builder.setMessage(s);
        builder.setIcon(R.drawable.ic_error);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}