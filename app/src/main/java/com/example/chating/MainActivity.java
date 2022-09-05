package com.example.chating;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
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
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<User> userList = new ArrayList<>();
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference database;
    LinearLayout scrollView;
    ScrollView views;
    boolean wtf = false;
    StorageReference st1;
    Button signOutButton, usersButton, musicButton, backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("User");
        scrollView = findViewById(R.id.list);
        views = findViewById(R.id.views);
        signOutButton = findViewById(R.id.buttonSignOut);
        usersButton = findViewById(R.id.buttonUsers);
        musicButton = findViewById(R.id.buttonCabinet);
        backButton = findViewById(R.id.buttonBackToMenu);
    }

    public void goToUsers(View view) {
        getDataFromDb();
        Intent intent = new Intent(getApplicationContext(), Users.class);
        intent.putExtra("list", new ArrayList<>(userList));
        startActivity(intent);

    }
    public void signOut(View view){
        firebaseAuth.signOut();
        Intent i = new Intent(getApplicationContext(), Login.class);
        startActivity(i);
    }
    public void loadUsers(View view){
        loadUsersB();
    }

    public void loadUsersB() {
        scrollView.removeAllViews();

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

            linearLayout.setBackgroundColor(Color.argb(255, 221, 160, 221));
            linearLayout.setPadding(20, 10, 20, 10);



            TextView name = new TextView(this);
            name.setTextSize(20);
            name.setText(userList.get(i).userName);


            linearLayout.addView(name);

            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
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
        userList.clear();
        if (wtf){
            signOutButton.setVisibility(View.INVISIBLE);
            usersButton.setVisibility(View.INVISIBLE);
            musicButton.setVisibility(View.INVISIBLE);
            backButton.setVisibility(View.VISIBLE);


        }
        else wtf = true;

    }

    public void goToCabinet(View view){
        Intent i = new Intent(getApplicationContext(), Cabinet.class);
        startActivity(i);
    }

    public void backToMenu(View view){
        signOutButton.setVisibility(View.VISIBLE);
        usersButton.setVisibility(View.VISIBLE);
        musicButton.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.INVISIBLE);
        scrollView.removeAllViews();
    }

    private void getDataFromDb() {

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    DataSnapshot d1 = ds.getChildren().iterator().next();
                    User u = d1.getValue(User.class);
                    if (!u.uid.equals(firebaseAuth.getCurrentUser().getUid())){
                        userList.add(u);
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("error");
            }
        };
        database.addValueEventListener(valueEventListener);

    }
}