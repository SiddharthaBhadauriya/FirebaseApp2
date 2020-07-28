package com.example.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
  EditText userETLogin, passETLogin;
  Button loginBtn;
  FirebaseAuth auth;
  Button RegisterBtn;
  FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser!= null)
        {
            Intent i = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userETLogin = findViewById(R.id.editText2);
        passETLogin = findViewById(R.id.editText3);
        loginBtn = findViewById(R.id.buttonLogin);
        RegisterBtn = findViewById(R.id.registerBttn);
        auth = FirebaseAuth.getInstance();





        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

       loginBtn .setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String email_text = userETLogin.getText().toString();
               String pass_text = passETLogin.getText().toString();

               if(TextUtils.isEmpty(email_text) || TextUtils.isEmpty(pass_text))
               {
                   Toast.makeText(LoginActivity.this, "Please fill the fields", Toast.LENGTH_SHORT).show();
               }
               else
               {
                   auth.signInWithEmailAndPassword(email_text,pass_text)
                           .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                               @Override
                               public void onComplete(@NonNull Task<AuthResult> task) {
                                   if(task.isSuccessful())
                                   {
                                       Intent i= new Intent(LoginActivity.this,MainActivity.class);
                                       i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                       startActivity(i);
                                       finish();
                                   }
                                   else
                                   {
                                       Toast.makeText(LoginActivity.this,"Login Failed!!", Toast.LENGTH_SHORT).show();
                                   }
                               }
                           });
               }
           }
       });

    }
}