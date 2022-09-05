package com.example.chating;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {
    private EditText login, password;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            Toast.makeText(this, "User not null", Toast.LENGTH_SHORT);
        } else {
            Toast.makeText(this, "User null", Toast.LENGTH_SHORT);
        }
    }

    public void init() {
        login = findViewById(R.id.loginLogin);
        password = findViewById(R.id.loginPass);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("User");
        firebaseAuth.signOut();
    }

    public void onClickLogin(View view) {
        if (!TextUtils.isEmpty(login.getText()) && !TextUtils.isEmpty(password.getText())) {
            firebaseAuth.signInWithEmailAndPassword(login.getText().toString(), password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Успешно вошел", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Пользователь все забыл и не вошел", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }


    }
    public void goToRegister(View view){

        Intent intent = new Intent(getApplicationContext(), Register.class);
        startActivity(intent);
    }
}
