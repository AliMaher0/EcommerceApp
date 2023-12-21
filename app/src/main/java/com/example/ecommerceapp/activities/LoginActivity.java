package com.example.ecommerceapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText email, password;
    CheckBox rememberMe;
    TextView txtForget;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean login;
    String Uid;
    String name ;
    String pass ;

    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        email= findViewById(R.id.Email);
        password = findViewById(R.id.password);
        rememberMe = findViewById(R.id.remember);
        txtForget = findViewById(R.id.textForget);

        //remember me
        sharedPreferences=getSharedPreferences("remember file",MODE_PRIVATE);
        login= sharedPreferences.getBoolean("login",false);



        name = email.getText().toString();
        pass = password.getText().toString();

        if(login){

            SharedPreferences prefs = getSharedPreferences("remember file", MODE_PRIVATE);

            String  name1 = prefs.getString("username","null");
            //String  pass1 =  prefs.getString("password","null");

            auth = FirebaseAuth.getInstance();

            Toast.makeText(this,"Welcome"+" "+ name1, Toast.LENGTH_SHORT).show();

            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

        txtForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String recoverEmail = email.getText().toString().trim();
                if(recoverEmail.length()>6){

                    FirebaseAuth.getInstance().sendPasswordResetEmail(recoverEmail)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this,"Check your email address...",Toast.LENGTH_LONG).show();
                                    }else {
                                        Toast.makeText(LoginActivity.this,"Link not sent...",Toast.LENGTH_LONG).show();

                                    }
                                }
                            });
                }else{
                    email.setError("please enter your email!");
                }
            }
        });


    }

    public void signIn(View view) {

        String userEmail= email.getText().toString();
        String userPassword= password.getText().toString();

        if(TextUtils.isEmpty(userEmail)){
            Toast.makeText(this, "Enter Email !", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(userPassword)){
            Toast.makeText(this, "Enter Password !", Toast.LENGTH_SHORT).show();
            return;
        }
        if(userPassword.length() <8){
            Toast.makeText(this, "The Password is too short , Enter minimum 8 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(userEmail,userPassword)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    if (rememberMe.isChecked())
                                        keepLogin(userEmail, userPassword);
                                   //Toast.makeText(LoginActivity.this, userEmail, Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                }else {
                                    Toast.makeText(LoginActivity.this, "Error"+task.getException(), Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

    }

    protected void keepLogin(String username , String pass){
        editor=sharedPreferences.edit();
        editor.putString("username",username) ;
        editor.putString("password",pass);
        editor.putBoolean("login",true);
        editor.apply();
    }

    public void signUp(View view) {
        startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
    }

}