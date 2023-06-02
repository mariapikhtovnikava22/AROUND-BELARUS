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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

public class MainActivity extends AppCompatActivity {


    EditText Login, Password;
    Button Log_IN, Create_ACC;
    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;
    ConstraintLayout root;


    String savedEmail = "" , savedName = "", savedPhone = "",
            savedPassword = "", savedLogin = "";

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
            }
        });

    }

    private void showAuthWindow()
    {
        if(TextUtils.isEmpty(Login.getText().toString()))
        {
            Snackbar.make(root,"Enter a password of at least 6 characters!",Snackbar.LENGTH_SHORT).show();
            return;
        }
        if(Password.getText().toString().length() < 6)
        {
            Snackbar.make(root,"Enter a password of at least 6 characters!",Snackbar.LENGTH_SHORT).show();
            return;
        }
        auth.signInWithEmailAndPassword(Login.getText().toString()+"@dev.com", Password.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                startActivity(new Intent(MainActivity.this, AllProjActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(root,"User is not authorization on app! " + e.getMessage(),Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

//    private void showRegWindow(boolean s ) {
//
//        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//        dialog.setTitle("Registration");
//        dialog.setMessage("Enter data for registration");
//
//        LayoutInflater inflater = LayoutInflater.from(this);
//        View regWin = inflater.inflate(R.layout.registration_activity, null);
//        dialog.setView(regWin);
//
//        final MaterialEditText email, name, phone,password,login;
//        final CardView reg_activ;
//
//        email = regWin.findViewById(R.id.email);
//        name = regWin.findViewById(R.id.Person_name);
//        phone = regWin.findViewById(R.id.Phone_number);
//        password = regWin.findViewById(R.id.Password_reg);
//        login = regWin.findViewById(R.id.Login_regis);
//        reg_activ = regWin.findViewById(R.id.reg_activit);
//
//        if (s == true)
//        {
//            email.setText(savedEmail);
//            name.setText(savedName);
//            phone.setText(savedPhone);
//            password.setText(savedPassword);
//            login.setText(savedLogin);
//
//        }
//        else
//        {
//            email.setText("");
//            name.setText("");
//            phone.setText("");
//            password.setText("");
//            login.setText("");
//
//
//        }
//
//        dialog.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int which) {
//                dialogInterface.dismiss();
//            }
//        });
//        dialog.setPositiveButton("Sign up", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int which) {
//
//                savedEmail = email.getText().toString(); // Сохранение значения поля
//                savedName = name.getText().toString();
//                savedPhone = phone.getText().toString();
//                savedPassword = password.getText().toString();
//                savedLogin = login.getText().toString();
//
//                if(TextUtils.isEmpty(email.getText().toString()))
//                {
//                    AlertDialog.Builder dialog = new AlertDialog.Builder(reg_activ.getContext());
//                    dialog.setTitle("Error of registration");
//                    dialog.setMessage("Enter your email");
//                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            showRegWindow(true);
//                            dialog.dismiss();
//                        }
//                    });
//                    dialog.show();
//                    return;
//                }
//                if(TextUtils.isEmpty(phone.getText().toString()))
//                {
//                    AlertDialog.Builder dialog = new AlertDialog.Builder(reg_activ.getContext());
//                    dialog.setTitle("Error of registration");
//                    dialog.setMessage("Enter your phone");
//                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            showRegWindow(true);
//                            dialogInterface.dismiss();
//                        }
//                    });
//                    dialog.show();
//                    return;
//                }
//                if(TextUtils.isEmpty(name.getText().toString()))
//                {
//                    Snackbar.make(reg_activ,"Enter your name!",Snackbar.LENGTH_SHORT).show();
//                    return;
//                }
//                if(TextUtils.isEmpty(login.getText().toString()))
//                {
//                    Snackbar.make(reg_activ,"Enter your login!",Snackbar.LENGTH_SHORT).show();
//                    return;
//                }
//                if(password.getText().toString().length() < 6)
//                {
//                    Snackbar.make(reg_activ,"Enter a password of at least 6 characters!",Snackbar.LENGTH_SHORT).show();
//                    return;
//                }
//
//                // Если все данные были введены, то мы регистрируем пользователя
//
//                auth.createUserWithEmailAndPassword(login.getText().toString()+"@dev.com", password.getText().toString())
//                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                            @Override
//                            public void onSuccess(AuthResult authResult) {
//                                User user = new User(name.getText().toString(),password.getText().toString(),
//                                        login.getText().toString(),email.getText().toString(),phone.getText().toString());
//
//                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void unused) {
//                                        Snackbar.make(root, "The user is registered",Snackbar.LENGTH_SHORT).show();
//                                    }
//                                });
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                AlertDialog.Builder dialog = new AlertDialog.Builder(reg_activ.getContext());
//                                dialog.setTitle("Error of registration");
//                                dialog.setMessage(e.getMessage());
//                                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        showRegWindow(true);
//                                        dialogInterface.dismiss();
//                                    }
//                                });
//                                dialog.show();
//                                return;
//                            }
//                        });
//            }
//        });
//        dialog.show();
//
//    }

}