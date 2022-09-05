package com.example.chating;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.chating.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Users extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference database;
    LinearLayout scrollView;
    ScrollView views;
    List<User> userList = new ArrayList<>();
    Button signOutButton, usersButton, musicButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        init();
    }

    public void init() {
        scrollView = findViewById(R.id.list);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("User");
        views = findViewById(R.id.views);
        signOutButton = findViewById(R.id.buttonSignOut);
        usersButton = findViewById(R.id.buttonUsers);
        musicButton = findViewById(R.id.buttonCabinet);


    }

    public void loadUsers(View view) {
        signOutButton.setVisibility(View.GONE);
        usersButton.setVisibility(View.INVISIBLE);
        musicButton.setVisibility(View.INVISIBLE);
        getDataFromDb();
        System.out.println(222);
        System.out.println("SUI " + userList.size());
        for (int i = 0; i < userList.size(); i++) {
            LinearLayout linearLayout = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 0, 10, 20);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setLayoutParams(params);
            linearLayout.setBackgroundColor(-256);
            linearLayout.setPadding(20, 10, 20, 10);

            TextView name = new TextView(this);
            name.setText(userList.get(i).userName);


            linearLayout.addView(name);

            scrollView.addView(linearLayout);
            int d = i;
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), Messages.class);
                    intent.putExtra("RecieverUid", userList.get(d).uid);
                    intent.putExtra("UserName", userList.get(d).userName);
                    startActivity(intent);
                }
            });
        }
    }

    private void getDataFromDb() {
        List<User> ul = new ArrayList<>();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot ds : snapshot.getChildren()) {
                    DataSnapshot d1 = ds.getChildren().iterator().next();
                    User u = d1.getValue(User.class);
                    userList.add(u);


                }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("error");
            }
        };

        database.addValueEventListener(valueEventListener);
        System.out.println("suak " + ul.size());

    }


}