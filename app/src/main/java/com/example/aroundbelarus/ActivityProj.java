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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override // получаем данные по текущему пользователю
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userData = snapshot.getValue(User.class);
                if (userData != null)
                {
                    String lo = userData.getLogin();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        persacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityProj.this, PersonalAccActivity.class));

            }
        });

    }


    public void handleSelection(View view)
    {
        Toast.makeText(this, "Hi", Toast.LENGTH_SHORT).show();
    }

}