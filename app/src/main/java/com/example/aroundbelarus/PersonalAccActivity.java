package com.example.aroundbelarus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.aroundbelarus.Clases.PersonalAccount;
import com.example.aroundbelarus.Clases.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PersonalAccActivity extends AppCompatActivity {
    ImageButton LogOut, Change;
    TextInputEditText email, name, phone,password,login;
    String temp_name, temp_email, temp_phone, temp_login, temp_password;
    ConstraintLayout cur_page;
    FirebaseUser user;
    FirebaseAuth auth;
    String userId;
    DatabaseReference userRef;
    User userData;
    FirebaseDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_acc);
        init();
        ShowData();


        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(PersonalAccActivity.this, MainActivity.class));
                finish();
            }
        });
        Change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonalAccount pa = new PersonalAccount(temp_name, temp_email,temp_phone,temp_login,temp_password, PersonalAccActivity.this, PersonalAccActivity.this, cur_page);
                pa.Create_DialogWin(user,LogOut,userRef);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void init()
    {
        email =  findViewById(R.id.PAEdit_email);
        name =  findViewById(R.id.PAEdit_Person_name);
        phone =  findViewById(R.id.PAEdit_Phone_number);
        password = findViewById(R.id.PAEdit_Password_reg);
        login = findViewById(R.id.PAEdit_Login_regis);

        LogOut = findViewById(R.id.log_outbutmoder);
        Change = findViewById(R.id.Change_data);
        cur_page = findViewById(R.id.persacc_activ);

        user = FirebaseAuth.getInstance().getCurrentUser();
        auth = FirebaseAuth.getInstance();
        userId = user.getUid();
        db = FirebaseDatabase.getInstance();

        userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
    }

    public void ShowData()
    {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override // получаем данные по текущему пользователю
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userData = snapshot.getValue(User.class);
                if (userData != null)
                {
                    temp_email = userData.getEmail();
                    email.setText(temp_email);
                    temp_login = userData.getLogin();
                    login.setText(temp_login);
                    temp_password = userData.getPassword();
                    password.setText(temp_password);
                    temp_phone = userData.getNumber();
                    phone.setText(temp_phone);
                    temp_name = userData.getName();
                    name.setText(temp_name);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Snackbar.make(cur_page, error.getMessage(),Snackbar.LENGTH_SHORT).show();
            }
        });
    }




}