package com.example.aroundbelarus.Clases;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;


import com.example.aroundbelarus.ActivityProj;
import com.example.aroundbelarus.MainActivity;
import com.example.aroundbelarus.ModeratorAcc;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Authorization extends Registration {


    String Login, Password;
    MainActivity mainActivity;
    DatabaseReference userRef;
    boolean a;


    public Authorization(ConstraintLayout root_page, FirebaseAuth auth, FirebaseDatabase db, Context context, DatabaseReference users, String Login, String Password, MainActivity mn)
    {
        super(root_page,auth,db,context,users);
        this.Login = Login;
        this.Password = Password;
        this.mainActivity = mn;
    }

    public Authorization()
    {}


    public void showAuthWindow()
    {
        if(TextUtils.isEmpty(Login))
        {
            super.Mess("Enter your Login", "Error of authorization", super.root_page);
            return;
        }
        if(TextUtils.isEmpty(Password))
        {
            super.Mess("Enter a password of at least 6 characters!", "Error of authorization", super.root_page);
            return;
        }
        ConstraintLayout root = super.root_page;
        String for_reg = Login+"@dev.com";
        super.auth.signInWithEmailAndPassword(for_reg, Password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
                        Check(mainActivity, userRef);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String er  = e.getMessage();
                        er = er.replace("email address", "login");
                        Snackbar.make(root,"User is not authorization on app! " + er,Snackbar.LENGTH_SHORT).show();
                    }
                });

    }

    public void Check(MainActivity mainActivity, DatabaseReference userRef) {
        ConstraintLayout root = super.root_page;

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String login = userSnapshot.child("login").getValue(String.class);
                    if(login.equals("moderator")) {
                        mainActivity.startActivity(new Intent(mainActivity, ModeratorAcc.class));
                        mainActivity.finish();
                        return;
                    }
                }
                mainActivity.startActivity(new Intent(mainActivity, ActivityProj.class));
                mainActivity.finish();
                // Здесь можно выполнить дополнительные действия с переменной "a" или вызвать другие методы, которым требуется результат
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Обработка ошибок при получении данных
            }
        });

    }




}
