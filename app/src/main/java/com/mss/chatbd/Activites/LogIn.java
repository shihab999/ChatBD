package com.mss.chatbd.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mss.chatbd.R;

public class LogIn extends AppCompatActivity {

    TextInputEditText login_email, login_password;
    AppCompatButton login_btn;
    TextView forgotten_password, noAcc;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        login_btn = findViewById(R.id.login_btn);
        forgotten_password = findViewById(R.id.forgotten_password);
        noAcc = findViewById(R.id.no_Acc);

        firebaseAuth= FirebaseAuth.getInstance();
        progressDialog= new ProgressDialog(LogIn.this);

        progressDialog.setMessage("Please wait...");

        noAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogIn.this,Register.class));
                finish();

            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = login_email.getText().toString().trim();
                String password = login_password.getText().toString().trim();

                if(email.isEmpty()){
                    ShowAlert("Enter your email!");
                } else if (password.isEmpty()){
                    ShowAlert("Enter your password");
                } else {
                    progressDialog.show();
                    firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if(task.isSuccessful()){
                                startActivity(new Intent(LogIn.this, MainActivity.class));
                                finish();
                            }else{
                                String error = task.getException().getLocalizedMessage();
                                ShowAlert(error);
                            }
                        }
                    });
                }

            }
        });


    }

    private void ShowAlert(String s) {

        AlertDialog.Builder builder = new AlertDialog.Builder(LogIn.this);
        builder.setTitle("Error");
        builder.setMessage(s);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseAuth.getCurrentUser()!=null){
            startActivity(new Intent(LogIn.this,MainActivity.class));
            finish();
        }
    }
}