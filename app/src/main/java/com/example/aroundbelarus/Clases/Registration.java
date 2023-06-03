package com.example.aroundbelarus.Clases;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import com.example.aroundbelarus.MainActivity;
import com.example.aroundbelarus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Registration
{
    protected User user;
    protected ConstraintLayout root_page;
    protected FirebaseAuth auth;
    protected FirebaseDatabase db;
    protected DatabaseReference users;
    protected Context context;
    CardView reg_activ;


    private String savedEmail = "" , savedName = "", savedPhone = "",
            savedPassword = "", savedLogin = "";

    public Registration(ConstraintLayout root_page, FirebaseAuth auth, FirebaseDatabase db, Context context, DatabaseReference users) {
        this.root_page = root_page;
        this.auth = auth;
        this.db = db;
        this.context = context;
        this.users = users;
    }

    public Registration(){
    }

    public void showRegWindow(boolean s) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Registration");
        dialog.setMessage("Enter data for registration");

        LayoutInflater inflater = LayoutInflater.from(context);
        View regWin = inflater.inflate(R.layout.registration_activity, null);
        dialog.setView(regWin);

        TextInputEditText email, name, phone,password,login;


        email = regWin.findViewById(R.id.Edit_email);
        name = regWin.findViewById(R.id.Edit_Person_name);
        phone = regWin.findViewById(R.id.Edit_Phone_number);
        password = regWin.findViewById(R.id.Edit_Password_reg);
        login = regWin.findViewById(R.id.Edit_Login_regis);
        reg_activ = regWin.findViewById(R.id.reg_activit);

        if (s == true)
        {
            email.setText(savedEmail);
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

    public void Mess(String mess_of_error, String type_Error, CardView reg_activ)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(reg_activ.getContext());
        dialog.setTitle(type_Error);
        dialog.setMessage(mess_of_error);
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showRegWindow(true);
                dialog.dismiss();
            }
        });
        dialog.show();
        return;
    }

    public void Mess(String mess_of_error, String type_Error, ConstraintLayout reg_activ)
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(reg_activ.getContext());
        dialog.setTitle(type_Error);
        dialog.setMessage(mess_of_error);
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
        return;
    }

    private void SendEmailVer() //сработает если вход будет через почту а не через логин
    {
        FirebaseUser us = auth.getCurrentUser();
        assert us != null;
        us.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Mess("A confirmation email has been sent to your email address!" +
                            "Confirm your email and log in again.","End of registration", reg_activ);
                }
                else
                {
                    Mess("There is no such address",
                            "Registration completion error", reg_activ);
                    showRegWindow(true);
                }
            }
        });

    }


    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof Registration)) {
            return false;
        }
        Registration us  = (Registration) obj;
        return this.user.getLogin().equals(us.user.getLogin());
    }
}
