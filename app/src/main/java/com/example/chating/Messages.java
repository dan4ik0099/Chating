package com.example.chating;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.chating.model.Message;
import com.example.chating.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.EventListener;
import java.util.List;


public class Messages extends AppCompatActivity {
    public String recieverUid;
    public String userName;
    List<Message> messageList = new ArrayList<>();
    FirebaseAuth firebaseAuth;
    DatabaseReference database;
    ScrollView scroll;
    EditText textMsg;
    TextView labelName;
    StorageReference st1, st2;
    List<Message> newMes = new ArrayList<>();
    boolean deleteMode = false;


    LinearLayout listMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        init();

    }
    public void init(){
        recieverUid = getIntent().getStringExtra("RecieverUid");
        userName = getIntent().getStringExtra("UserName");
        firebaseAuth = FirebaseAuth.getInstance();
        textMsg = findViewById(R.id.textMessage);
        database = FirebaseDatabase.getInstance().getReference("Messages/");
        listMsg = findViewById(R.id.listMessages);
        labelName = findViewById(R.id.name);
        scroll = findViewById(R.id.scroll);
        labelName.setText(userName);

        st1 = FirebaseStorage.getInstance().getReference("User/avatar/"+firebaseAuth.getCurrentUser().getUid());
        st2 = FirebaseStorage.getInstance().getReference("User/avatar/"+recieverUid);

        getDataFromDb();

    }
    public void sendMessages(View view){
        if (!textMsg.getText().toString().equals("")) {
            Message msg = new Message(userName, firebaseAuth.getCurrentUser().getUid(), recieverUid, textMsg.getText().toString(), new Date().getTime());
            database.push().setValue(msg);
            textMsg.setText("");
            getDataFromDb();
            messageList.clear();
            scroll.fullScroll(ScrollView.FOCUS_DOWN);
        }
    }
    public void deleteAllMsg(View view){
        deleteMode = true;

    }

    public void loadMessages(){
        scroll.fullScroll(ScrollView.FOCUS_DOWN);
        listMsg.removeAllViews();

        for (int i = 0; i < messageList.size(); i++) {
            LinearLayout linearLayout = new LinearLayout(this);
            LinearLayout linearLayout2 = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 0, 10, 20);

            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setLayoutParams(params);
            linearLayout2.setLayoutParams(params);
            ImageView img = new ImageView(this);

            linearLayout2.setOrientation(LinearLayout.HORIZONTAL);
            if (!messageList.get(i).UidSender.equals(firebaseAuth.getCurrentUser().getUid())){
                linearLayout.setBackgroundColor(Color.argb(255, 173, 216, 230));




            }else{
                linearLayout.setBackgroundColor(Color.argb(255, 255, 255, 255));




            }


            linearLayout.setPadding(20, 20, 20, 20);
            TextView name = new TextView(this);
            if (!messageList.get(i).UidSender.equals(firebaseAuth.getCurrentUser().getUid())){
                name.setText(userName+": ");
            }else{
                name.setText("Ты: ");
            }

            name.setTextSize(18);

            TextView text = new TextView(this);
            text.setText(messageList.get(i).text);
            text.setTextSize(18);
            name.setTypeface(null, Typeface.BOLD);

            TextView date = new TextView(this);
            Date d = new Date(messageList.get(i).time);

            date.setText(d.toString());

            date.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);


            linearLayout.addView(date);

            linearLayout2.addView(name);
            linearLayout2.addView(text);
            linearLayout.addView(linearLayout2);


            listMsg.addView(linearLayout);


            int g = i;
            linearLayout.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick (View v){
                        if(deleteMode) {
                            getDataFromDb();
                            loadMessages();
                            newMes.remove(g);
                            DatabaseReference refDel = FirebaseDatabase.getInstance().getReference("Message/");
                            refDel.removeValue();
                            for (int f = 0;f<newMes.size();f++){
                                database.push().setValue(newMes.get(f));
                            }
                        }

                }

            });


        }
        scroll.fullScroll(ScrollView.FOCUS_DOWN);
        messageList.clear();
    }
    private void getDataFromDb() {
        listMsg.removeAllViews();
        messageList.clear();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Message msg = ds.getValue(Message.class);
                    if ((msg.UidSender.equals(firebaseAuth.getCurrentUser().getUid()) && (msg.UidReciever.equals(recieverUid)) || (msg.UidReciever.equals(firebaseAuth.getCurrentUser().getUid()) && (msg.UidSender.equals(recieverUid))))){
                        messageList.add(msg);
                    }

                }
                newMes = new ArrayList<>(messageList);
                loadMessages();
                scroll.fullScroll(ScrollView.FOCUS_DOWN);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("error");
            }
        };
        database.addValueEventListener(valueEventListener);
        messageList.clear();

    }
    public void backToUsers(View view){
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }

}