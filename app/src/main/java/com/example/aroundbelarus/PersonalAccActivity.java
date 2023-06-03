package com.example.aroundbelarus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import com.example.aroundbelarus.Clases.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
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
    boolean s = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_acc);

        email =  findViewById(R.id.PAEdit_email);
        name =  findViewById(R.id.PAEdit_Person_name);
        phone =  findViewById(R.id.PAEdit_Phone_number);
        password = findViewById(R.id.PAEdit_Password_reg);
        login = findViewById(R.id.PAEdit_Login_regis);

        LogOut = findViewById(R.id.log_outbut);
        Change = findViewById(R.id.Change_data);
        cur_page = findViewById(R.id.persacc_activ);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override // получаем данные по текущему пользователю
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userData = snapshot.getValue(User.class);
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

                AlertDialog.Builder dialog = new AlertDialog.Builder(PersonalAccActivity.this);
                dialog.setTitle("Registration");
                dialog.setMessage("Enter data for registration");

                LayoutInflater inflater = LayoutInflater.from(PersonalAccActivity.this);
                View regWin = inflater.inflate(R.layout.registration_activity, null);
                dialog.setView(regWin);

                if (s == true)
                {
                    email.setText(savedname);
                    name.setText(savedName);
                    phone.setText(savedPhone);
                    password.setText(savedPassword);
                    login.setText(savedLogin);

                }
                else
                {
                    email.setText("");
                    name.setText("");
                    phone.setText("");
                    password.setText("");
                    login.setText("");


                }

                dialog.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                });
                dialog.setPositiveButton("Sign up", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                        savedEmail = email.getText().toString(); // Сохранение значения поля
                        savedName = name.getText().toString();
                        savedPhone = phone.getText().toString();
                        savedPassword = password.getText().toString();
                        savedLogin = login.getText().toString();

                        if(TextUtils.isEmpty(email.getText().toString()))
                        {
                            Mess("Enter your email", "Error of registration", reg_activ);
                            return;
                        }
                        if(TextUtils.isEmpty(phone.getText().toString()))
                        {
                            Mess("Enter your phone", "Error of registration", reg_activ);
                            return;
                        }
                        if(TextUtils.isEmpty(name.getText().toString()))
                        {
                            Mess("Enter your name", "Error of registration", reg_activ);
                            return;
                        }
                        if(TextUtils.isEmpty(login.getText().toString()))
                        {
                            Mess("Enter your login", "Error of registration", reg_activ);
                            return;
                        }
                        if(password.getText().toString().length() < 6)
                        {
                            Mess("Enter your password", "Error of registration", reg_activ);
                            return;
                        }

                        // Если все данные были введены, то мы регистрируем пользователя

                        String for_reg = login.getText().toString() + "@dev.com";

                        auth.createUserWithEmailAndPassword(for_reg, password.getText().toString())
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        User user = new User(name.getText().toString(),password.getText().toString(),
                                                login.getText().toString(),email.getText().toString(),phone.getText().toString());
                                        user.SetLogin(login.getText().toString());

                                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        userRef.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Snackbar.make(root_page, "The user is registered",Snackbar.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        String er  = e.getMessage();
                                        er = er.replace("email address", "login");

                                        Mess(er,"Error of registration", reg_activ);
                                        return;
                                    }
                                });
                    }
                });
                dialog.show();


            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish(); // Закрыть текущую активность
    }
}