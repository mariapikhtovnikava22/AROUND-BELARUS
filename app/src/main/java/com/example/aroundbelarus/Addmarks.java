package com.example.aroundbelarus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.aroundbelarus.Clases.MapSingleton;
import com.example.aroundbelarus.Clases.Mark;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Addmarks extends AppCompatActivity {

    ConstraintLayout add_activ;
    GoogleMap my_Map;
    Button applybtn;
    String savedname, savelat, savelong, savedescrip, typemark;
    RadioButton saveragiobutton;
    FirebaseDatabase database;
    DatabaseReference markersRef;
    TextInputEditText latitudeEdit, longitudeEdit, Edit_description, Editmarkname;
    RadioGroup radioGroup;
    HashMap<String, Mark> listofmarks;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmarks);
        add_activ = findViewById(R.id.addmarkactiv);



        latitudeEdit = findViewById(R.id.latitudeEdit);
        longitudeEdit = findViewById(R.id.longitudeEdit);
        Edit_description = findViewById(R.id.Edit_description);
        Editmarkname = findViewById(R.id.Editmarkname);
        radioGroup = findViewById(R.id.selecttypegroup);
        applybtn = findViewById(R.id.applybtn);

        my_Map = MapSingleton.getInstance().getGoogleMap();

        applybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int radioId = radioGroup.getCheckedRadioButtonId();
                saveragiobutton = findViewById(radioId);
                typemark = saveragiobutton.getText().toString();

                savedescrip = Edit_description.getText().toString();
                savelat = latitudeEdit.getText().toString();
                savelong = longitudeEdit.getText().toString();
                savedname = Editmarkname.getText().toString();
                if(!Check())
                {
                    latitudeEdit.setText(savelat);
                    longitudeEdit.setText(savelong);
                    Edit_description.setText(savedescrip);
                    Editmarkname.setText(savedname);
                    return;
                }
                if(!isValidCoordinates(Float.parseFloat(latitudeEdit.getText().toString()),Float.parseFloat(longitudeEdit.getText().toString())))
                {
                    latitudeEdit.setText(savelat);
                    longitudeEdit.setText(savelong);
                    Edit_description.setText(savedescrip);
                    Editmarkname.setText(savedname);
                    Toast.makeText(Addmarks.this, "Invalid coordinates", Toast.LENGTH_SHORT).show();
                    return;
                }
                AddDatainBD();
            }
        });

    }

    private boolean isValidCoordinates(Float lat, Float lng) {
        if (lat >= -90 && lat <= 90 && lng >= -180 && lng <= 180) {
            return true;
        } else {
            return false;
        }
    }


    public void CheckButton(View v)
    {
        int radioId = radioGroup.getCheckedRadioButtonId();
        saveragiobutton = findViewById(radioId);
    }

    public void GetAllObjectinBD(FirebaseDatabase database, HashMap<String, Mark> listofmarks) // перенести в другую функцию
    {
        DatabaseReference marksRef = database.getReference("marks");

        marksRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String markerKey = childSnapshot.getKey();
                        Mark marker = childSnapshot.getValue(Mark.class);
                        listofmarks.put(markerKey,marker);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "List of marks is empty", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void AddDatainBD()
    {
        Mark marker = new Mark(Editmarkname.getText().toString(),typemark, Float.parseFloat(latitudeEdit.getText().toString()),
                        Float.parseFloat(longitudeEdit.getText().toString()), Edit_description.getText().toString());

        database = FirebaseDatabase.getInstance();
        markersRef = database.getReference("markers");
        String markerName = Editmarkname.getText().toString();
        DatabaseReference markerRef = markersRef.child(markerName);
        markersRef.child(markerName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Метка с таким же названием уже существует
                    Toast.makeText(getApplicationContext(), "The marker already exists", Toast.LENGTH_SHORT).show();
                } else {
                    // Добавляем новую метку
                    DatabaseReference markerRef = markersRef.child(markerName);
                    markerRef.setValue(marker)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(), "Mark added", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Error when adding a mark", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error reading marker data", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public boolean Check()
    {
        if(TextUtils.isEmpty(Editmarkname.getText().toString()))
        {
            Mess("Fill in all the details", "Error of add mark", add_activ);
            return false;
        }
        if(TextUtils.isEmpty(latitudeEdit.getText().toString()))
        {
            Mess("Fill in all the details", "Error of add mark", add_activ);
            return false;
        }
        if(TextUtils.isEmpty(longitudeEdit.getText().toString()))
        {
            Mess("Fill in all the details", "Error of add mark", add_activ);
            return false;
        }
        if(TextUtils.isEmpty(Edit_description.getText().toString()))
        {
            Mess("Fill in all the details", "Error of add mark", add_activ);
            return false;
        }

        return  true;

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
}