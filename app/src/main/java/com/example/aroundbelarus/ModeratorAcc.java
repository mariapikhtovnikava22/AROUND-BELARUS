package com.example.aroundbelarus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;

public class ModeratorAcc extends AppCompatActivity {

    Button add,open,show;
    ImageButton logOUT;

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

    }
}