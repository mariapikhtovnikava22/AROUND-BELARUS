package com.example.aroundbelarus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.aroundbelarus.Clases.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActivityProj extends AppCompatActivity {

    ImageButton persacc, mapactiv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proj);
        persacc = findViewById(R.id.PersonalAcc);
        mapactiv = findViewById(R.id.toMapActivity);

        persacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityProj.this, PersonalAccActivity.class));

            }
        });
        mapactiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityProj.this, MapActivity.class));
            }
        });

    }

    public void handleSelection(View view)
    {
        Toast.makeText(this, "Hi", Toast.LENGTH_SHORT).show();
    }

}