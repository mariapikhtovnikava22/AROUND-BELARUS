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
    FirebaseDatabase database;
    FirebaseUser currentUser;
    boolean tempmod, tmpadmin;
    String userId;
    DatabaseReference currentUserRef;


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
                        database = FirebaseDatabase.getInstance();
                        currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
                        Check(mainActivity, userRef, Login);
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

    public void Check(MainActivity mainActivity, DatabaseReference userRef, String Login) {

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean moderator, admin;
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String templog = userSnapshot.child("login").getValue(String.class);
                    if (templog.equals(Login))
                    {
                        moderator = Boolean.TRUE.equals(userSnapshot.child("moderator").getValue(boolean.class));
                        admin = Boolean.TRUE.equals(userSnapshot.child("admin").getValue(boolean.class));
                        if(moderator)
                        {
                            tempmod = true;
                        }
                        if(admin)
                        {
                            tmpadmin =true;
                        }
                    }

                }
                if(tempmod)
                {
                    mainActivity.startActivity(new Intent(mainActivity, ModeratorAcc.class));
                    mainActivity.finish();
                    return;
                }
                else
                {
                    mainActivity.startActivity(new Intent(mainActivity, ActivityProj.class));
                    mainActivity.finish();
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Обработка ошибок при получении данных
            }
        });

    }




}
