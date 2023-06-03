package com.example.aroundbelarus.Clases;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.aroundbelarus.MainActivity;
import com.example.aroundbelarus.PersonalAccActivity;
import com.example.aroundbelarus.R;
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

import java.util.HashMap;

public class PersonalAccount {

    TextInputEditText emailtmp, nametmp,phonetmp,passwordtmp,logintmp;
    String temp_name, temp_email, temp_phone, temp_login, temp_password;
    FirebaseUser user;
    FirebaseAuth auth;
    String userId;
    DatabaseReference userRef;
    User userData;
    FirebaseDatabase db;
    Context context;
    String log_rofBD;
    PersonalAccActivity pers_activ;
    ConstraintLayout root;
    String firstdataLogin, firstDataPassword;

    public PersonalAccount(){}

    public PersonalAccount(String temp_name, String temp_email, String temp_phone,
                           String temp_login, String temp_password, Context con, PersonalAccActivity pa, ConstraintLayout root) {
        this.temp_name = temp_name;
        this.temp_email = temp_email;
        this.temp_phone = temp_phone;
        this.temp_login = temp_login;
        this.temp_password = temp_password;
        this.firstDataPassword = temp_password;
        this.firstdataLogin = temp_login;
        this.pers_activ = pa;
        this.context = con;
        this.root = root;
    }

    public void Create_DialogWin(FirebaseUser user, ImageButton btn, DatabaseReference dbref)
    {
        this.userRef = dbref;
        this.user = user;
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Updating data");
        dialog.setMessage("Enter data for update");

        LayoutInflater inflater = LayoutInflater.from(context);
        View regWin = inflater.inflate(R.layout.registration_activity2, null);
        dialog.setView(regWin);
        init(regWin);

        dialog.setNegativeButton("Delete account", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Delete_Acc(user, btn,userRef);
            }
        });
        dialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!Check())
                {
                    return;
                }
                HashMap<String, Object> updates = create_HashMap();
                UpdateRealTimeBD(updates,userRef);
                if (!log_rofBD.equals(firstdataLogin+"@dev.com"))
                {
                    UpdateAuthBDLogin(btn,user);
                }
                String n = passwordtmp.getText().toString();
                if(!passwordtmp.getText().toString().equals(firstDataPassword))
                {
                    UpdateAuthBDPassword(btn,user);
                }
            }
        });
        dialog.show();


    }

    private void UpdateAuthBDLogin(ImageButton btn, FirebaseUser user)
    {
        user.updateEmail(log_rofBD)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Snackbar.make(root, "Login is update. Log in again!!!",Snackbar.LENGTH_LONG).show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    btn.performClick(); // Вызывайте метод выхода после задержки
                                }
                            }, 2000);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar.make(root, e.getMessage(),Snackbar.LENGTH_SHORT).show();
                        }
                    });
    }

    private void UpdateAuthBDPassword(ImageButton btn, FirebaseUser user)
    {
        user.updatePassword(passwordtmp.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Snackbar.make(root, "Password is update",Snackbar.LENGTH_LONG).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                btn.performClick(); // Вызывайте метод выхода после задержки
                            }
                        }, 2000);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(btn, e.getMessage(),Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    private void UpdateRealTimeBD(HashMap<String, Object> updates, DatabaseReference userRef)
    {
        userRef.updateChildren(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Snackbar.make(root, "Data has been successfully updated",Snackbar.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(root, e.getMessage(),Snackbar.LENGTH_SHORT).show();
                    }
                });
    }


    private HashMap<String, Object> create_HashMap()
    {
        HashMap<String, Object> updates = new HashMap<>();
        updates.put("name", nametmp.getText().toString());
        updates.put("number", phonetmp.getText().toString());
        updates.put("email", emailtmp.getText().toString());
        updates.put("login", logintmp.getText().toString());
        updates.put("password", passwordtmp.getText().toString());

        return updates;
    }


    private void Delete_Acc(FirebaseUser user, ImageButton btn, DatabaseReference userRef)
    {
        user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Snackbar.make(root, "You have deleted your account",Snackbar.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btn.performClick(); // Вызывайте метод выхода после задержки
                    }
                }, 2000);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(root, e.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });

        userRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(root, e.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });

        Authorization();
    }

    private void Authorization()
    {
        auth.signInWithEmailAndPassword(log_rofBD, passwordtmp.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(root, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    private void init(View regWin)
    {
        emailtmp = regWin.findViewById(R.id.Edit_email);
        nametmp = regWin.findViewById(R.id.Edit_Person_name);
        phonetmp = regWin.findViewById(R.id.Edit_Phone_number);
        passwordtmp = regWin.findViewById(R.id.Edit_Password_reg);
        logintmp = regWin.findViewById(R.id.Edit_Login_regis);
        emailtmp.setText(temp_email);
        nametmp.setText(temp_name);
        phonetmp.setText(temp_phone);
        passwordtmp.setText(temp_password);
        logintmp.setText(temp_login);
        log_rofBD =  logintmp.getText().toString()+"@dev.com";
    }

    private boolean Check()
    {
        Registration re = new Registration();
        ConstraintLayout cur_page = root;

        if(TextUtils.isEmpty(emailtmp.getText().toString()))
        {
            re.Mess("Enter your email", "Update error", cur_page);
            return false;
        }
        if(TextUtils.isEmpty(phonetmp.getText().toString()))
        {
            re.Mess("Enter your phone", "Update error", cur_page);
            return false;
        }
        if(TextUtils.isEmpty(nametmp.getText().toString()))
        {
            re.Mess("Enter your name", "Update error", cur_page);
            return false;
        }
        if(TextUtils.isEmpty(logintmp.getText().toString()))
        {
            re.Mess("Enter your login", "Update error", cur_page);
            return false;
        }
        if(passwordtmp.getText().toString().length() < 6)
        {
            re.Mess("Enter a password of at least 6 characters", "Update error", cur_page);
            return false;
        }

        return true;
    }



}
