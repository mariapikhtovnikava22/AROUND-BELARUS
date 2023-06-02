package com.example.aroundbelarus.Clases;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import com.example.aroundbelarus.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registration
{
    protected User user;
    protected ConstraintLayout root_page;
    protected FirebaseAuth auth;
    protected FirebaseDatabase db;
    protected DatabaseReference users;
    protected Context context;


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

        final TextInputLayout layemail, layname, layphone,laypassword,laylogin;
        final TextInputEditText email, name, phone,password,login;
        final CardView reg_activ;


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

                auth.createUserWithEmailAndPassword(login.getText().toString()+"@dev.com", password.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                User user = new User(name.getText().toString(),password.getText().toString(),
                                        login.getText().toString(),email.getText().toString(),phone.getText().toString());

                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Snackbar.make(root_page, "The user is registered",Snackbar.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Mess(e.getMessage(),"Error of registration", reg_activ);
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
