package com.example.chating;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chating.model.User;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class Cabinet extends AppCompatActivity {
    EditText login, oldPassword, newPassword;
    ImageView img;
    DatabaseReference database;
    FirebaseUser cUser;
    FirebaseAuth firebaseAuth;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cabinet);
        init();
    }



    public void init(){
        firebaseAuth = FirebaseAuth.getInstance();
        cUser = firebaseAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("User/avatar/"+firebaseAuth.getCurrentUser().getUid());
        login = findViewById(R.id.cabinetLogin);
        login.setText(cUser.getEmail());
        oldPassword = findViewById(R.id.cabinetOldPass);
        newPassword = findViewById(R.id.cabinetNewPass);
    }

    public void onClickSave(View view) {

       if (!TextUtils.isEmpty(oldPassword.getText()) && !TextUtils.isEmpty(newPassword.getText())) {
            changePass();
            System.out.println("gasg");
            firebaseAuth.signOut();
            Intent i = new Intent(getApplicationContext(),Login.class);
            startActivity(i);

        }


        try{
            uploadImg();
        }
        catch (Exception ex){

        }



    }
    public void uploadImg() {
        img.setDrawingCacheEnabled(true);
        img.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageReference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.println("err");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                System.out.println("suc");


            }
        });


    }

    public void backToUsers(View view){
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
    private void changePass() {
        final String email = cUser.getEmail();
        final String newPass = newPassword.getText().toString();
        AuthCredential credential = EmailAuthProvider.getCredential(email, oldPassword.getText().toString());
        cUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    cUser.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                System.out.println(1221412);
                                Toast.makeText(getApplicationContext(), "Пароль сменен", Toast.LENGTH_LONG).show();
                                firebaseAuth.signOut();
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(getApplicationContext(), "Ошибка", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Ошибка жесть", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}