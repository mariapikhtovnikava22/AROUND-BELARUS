package com.example.aroundbelarus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.service.autofill.UserData;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aroundbelarus.Clases.Authorization;
import com.example.aroundbelarus.Clases.Registration;
import com.example.aroundbelarus.Clases.User;
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
import com.rengwuxian.materialedittext.MaterialEditText;

public class MainActivity extends AppCompatActivity {


    EditText Login, Password;
    Button Log_IN, Create_ACC;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;
    ConstraintLayout root;
    String tmpLogin;
    User userData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Login = findViewById(R.id.Login);
        Password = findViewById(R.id.Password);
        Log_IN = findViewById(R.id.ButLog_IN);
        Create_ACC = findViewById(R.id.But_Create_Acc);
        root = findViewById(R.id.root_page);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");

        Log_IN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Authorization au = new Authorization(root, auth,db, MainActivity.this, users,
                       Login.getText().toString(), Password.getText().toString(),MainActivity.this);
               au.showAuthWindow();

            }
        });

        Create_ACC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Registration reg = new Registration(root, auth,db, MainActivity.this, users);
                reg.showRegWindow(false);
                FirebaseAuth.getInstance().signOut();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser cUser = auth.getCurrentUser();

        if(cUser != null)
        {
            String userId = cUser.getUid();
            DatabaseReference userRef;
            userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    final DatabaseReference userReference = userRef;
                    userData = snapshot.getValue(User.class);
                    if(userData != null)
                    {
                        tmpLogin = userData.getLogin();
                        if(userData.getModerator())
                        {
                            startActivity(new Intent(MainActivity.this, ModeratorAcc.class));
                            finish();

                        }
                        else
                        {
                            startActivity(new Intent(MainActivity.this, ActivityProj.class));
                            finish();

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        else
        {
            return;
        }
    }


    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}