package com.example.aroundbelarus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.Manifest;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.aroundbelarus.Clases.Mark;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap my_Map;
    ImageButton curLocbtn;
    final int FINE_PREMISSION_CODE = 1;
    Location current_locatoin;
    FusedLocationProviderClient fusedLocationProviderClient;
    FirebaseDatabase database;
    DatabaseReference markersRef;
    String markerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        database = FirebaseDatabase.getInstance();
        markersRef = database.getReference("markers");
        markerId = markersRef.push().getKey(); // Генерируем уникальный идентификатор метки
        Float latitude, longitude;
        latitude = 53.2281f;
        longitude = 26.6994f;

        String key = String.format(Locale.getDefault(), "%.6f_%.6f", latitude, longitude);

        Mark marker = new Mark();
        marker.setName("Название метки");
        marker.setDescription("Описание метки");
        marker.setCategory("Категория метки");
        marker.setLatitude(53.2281f);
        marker.setLongitude(26.6994f);

        markersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Метка с такими координатами уже существует
                    Toast.makeText(getApplicationContext(), "Метка уже существует", Toast.LENGTH_SHORT).show();
                } else {
                    // Добавляем новую метку
                    markersRef.setValue(marker)
                            .addOnSuccessListener(aVoid -> {
                                // Метка успешно добавлена в базу данных
                                Toast.makeText(getApplicationContext(), "Метка добавлена", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                // Ошибка при добавлении метки в базу данных
                                Toast.makeText(getApplicationContext(), "Ошибка при добавлении метки", Toast.LENGTH_SHORT).show();
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Ошибка чтения данных из базы данных
                Toast.makeText(getApplicationContext(), "Ошибка чтения данных", Toast.LENGTH_SHORT).show();
            }
        });


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        curLocbtn = findViewById(R.id.cur_locationBut);
        curLocbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastLocation();
                Toast.makeText(MapActivity.this, "Hi", Toast.LENGTH_SHORT).show();
                if (current_locatoin != null) {
                    AddMark((float) current_locatoin.getLatitude(), (float) current_locatoin.getLongitude(), "You");
                }
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        requestLocationPermission();
    }

    private void requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Запрашиваем разрешение у пользователя
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PREMISSION_CODE);
        }
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PREMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    current_locatoin = location;
                    AddMark((float) current_locatoin.getLatitude(), (float) current_locatoin.getLongitude(), "You");
                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        my_Map = googleMap;
        LatLng southwestBound = new LatLng(51.259920, 23.178448); // Юго-западная граница
        LatLng northeastBound = new LatLng(56.166726, 32.767492); // Северо-восточная граница
        LatLngBounds belarusBounds = new LatLngBounds(southwestBound, northeastBound);
        my_Map.setLatLngBoundsForCameraTarget(belarusBounds);


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PREMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Location permission is denied, please allow the permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void AddMark(Float coord1, Float coord2, String mess) {
        LatLng place = new LatLng(coord1, coord2);
        my_Map.addMarker(new MarkerOptions().position(place).title(mess));
        float zoomLevel = 17.0f; // Уровень приближения
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(place, zoomLevel);
        my_Map.animateCamera(cameraUpdate);
    }
}