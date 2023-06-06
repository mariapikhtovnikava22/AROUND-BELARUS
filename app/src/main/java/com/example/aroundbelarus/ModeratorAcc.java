package com.example.aroundbelarus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.aroundbelarus.Clases.Mark;
import com.example.aroundbelarus.Clases.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class ModeratorAcc extends AppCompatActivity {

    Button add,open,show;
    ImageButton logOUT;
    String savedname, savelat, savelong, savedescrip, typemark;
    RadioButton saveragiobutton;
    CardView reg_activ;
    FirebaseDatabase database;
    DatabaseReference markersRef;
    String markerId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moderator_acc);

        add = findViewById(R.id.addnewbtn);
        open = findViewById(R.id.openmapbtn);
        show = findViewById(R.id.allmarksbtn);
        logOUT = findViewById(R.id.log_outbutmoder);

        logOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ModeratorAcc.this, MainActivity.class));
                finish();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ModeratorAcc.this, Addmarks.class));

            }
        });
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ModeratorAcc.this, MapActivity.class));
            }
        });

    }


    public void Mess(String mess_of_error, String type_Error, CardView reg_activ)
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

}