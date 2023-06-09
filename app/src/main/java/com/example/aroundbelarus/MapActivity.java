package com.example.aroundbelarus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.Manifest;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.aroundbelarus.Clases.MapSingleton;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    GoogleMap my_Map;
    ImageButton curLocbtn;
    final int FINE_PREMISSION_CODE = 1;
    Location current_locatoin;
    FusedLocationProviderClient fusedLocationProviderClient;
    FirebaseDatabase database;
    DatabaseReference markersRef;
    String markerId;
    HashMap<String, Mark> listofmarks;
    ConstraintLayout map_activ;
    SearchView mapserchView;
    Button sortBtn;
    boolean isMyLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        sortBtn = findViewById(R.id.sortbtn);

        map_activ = findViewById(R.id.map_activity);

        mapserchView = findViewById(R.id.mapSearch);

        listofmarks = new HashMap<>();

        database = FirebaseDatabase.getInstance();

        GetAllObjectinBD(database);

        mapserchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                 String Location = mapserchView.getQuery().toString();
                 List<Address> addressList = null;
                 if(Location != null)
                 {
                     Geocoder geocoder = new Geocoder(MapActivity.this);
                     try {
                         addressList = geocoder.getFromLocationName(Location,1 );
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                     Address address = addressList.get(0);
                     LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                     my_Map.addMarker(new MarkerOptions().position(latLng).title(Location));
                     my_Map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                 }

                 return false;


            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        requestLocationPermission();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        curLocbtn = findViewById(R.id.cur_locationBut);
        curLocbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastLocation();
                if (current_locatoin != null) {
                    AddMark((float) current_locatoin.getLatitude(), (float) current_locatoin.getLongitude(), "You");
                }
            }
        });

        sortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialWin();
            }
        });
    }


    public void createDialWin()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MapActivity.this);
        LayoutInflater inflater = LayoutInflater.from(MapActivity.this);
        View regWin = inflater.inflate(R.layout.sort, null);
        dialog.setView(regWin);

        CheckBox food,nature,hotels,entertainments,all,attractions;
        food = regWin.findViewById(R.id.foodsel);
        nature = regWin.findViewById(R.id.naturesel);
        hotels = regWin.findViewById(R.id.hotelssel);
        entertainments = regWin.findViewById(R.id.entertainmentssel);
        all = regWin.findViewById(R.id.showallmap);
        attractions = regWin.findViewById(R.id.attractionssel);

        ArrayList<String> selectedValues = new ArrayList<>();


        dialog.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (nature.isChecked()) {

//                    Toast.makeText(getApplicationContext(), "Hi", Toast.LENGTH_SHORT).show();
                    selectedValues.add(nature.getText().toString());
                }

                if (food.isChecked()) {
                    selectedValues.add(food.getText().toString());
                }

                if (hotels.isChecked()) {
                    selectedValues.add(hotels.getText().toString());
                }
                if (entertainments.isChecked()) {
                    selectedValues.add(entertainments.getText().toString());
                }

                if (all.isChecked()) {
                    selectedValues.add(all.getText().toString());
                }

                if (attractions.isChecked()) {
                    selectedValues.add(attractions.getText().toString());
                }

                if(selectedValues.size() != 0)
                {
                    if(selectedValues.size() == 1)
                    {
                        if(selectedValues.get(0).equals("all"))
                        {
                            for (Mark mark : listofmarks.values()) {
                                        AddMarkonMap(mark.getLatitude(),mark.getLongitude(),mark.getName(),mark.getCategory());
                                }
                        }
                    }
                    for(String check : selectedValues)
                    {
                        if(check.equals("all"))
                        {
                            dialog.dismiss();
                            return;
                        }
                    }
                    my_Map.clear();
                    for (Mark mark : listofmarks.values()) {
                        for (String check : selectedValues)
                        {
                            if(mark.getCategory().equals(check))
                            {
                                AddMarkonMap(mark.getLatitude(),mark.getLongitude(),mark.getName(),mark.getCategory());
                            }
                        }
                    }
                }

            }
        });
        dialog.show();
    }



    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        my_Map = googleMap;
        LatLng southwestBound = new LatLng(51.259920, 23.178448); // Юго-западная граница
        LatLng northeastBound = new LatLng(56.166726, 32.767492); // Северо-восточная граница
        LatLngBounds belarusBounds = new LatLngBounds(southwestBound, northeastBound);
        int padding = 100; // Отступы в пикселях
        my_Map.moveCamera(CameraUpdateFactory.newLatLngBounds(belarusBounds, padding));
        if(listofmarks.size() != 0)
        {
            for (Mark mark : listofmarks.values()) {
                AddMarkonMap(mark.getLatitude(),mark.getLongitude(),mark.getName(),mark.getCategory());
            }
        }
        my_Map.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String markerTitle = marker.getTitle();
        LatLng markerPosition = marker.getPosition();
        String description = "";
        for (Mark mark : listofmarks.values()) {

            if (mark.getName().equals(markerTitle)) {
                description = mark.getDescription();
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(markerTitle)
                .setMessage(description)
                .setPositiveButton("Add to favorites", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Plot a route", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Код для построения маршрута
                        String url = "https://www.google.com/maps/dir/?api=1&destination=" + markerPosition.latitude + "," + markerPosition.longitude;
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    }
                });

        // Отобразите диалоговое окно
        AlertDialog dialog = builder.create();
        dialog.show();


        return true;
    }




    public void AddMarkonMap(Float coord1, Float coord2, String mess, String typemark)
    {
        LatLng place = new LatLng(coord1, coord2);
        MarkerOptions markerOptions = setMark(typemark, place, mess);
        my_Map.addMarker(markerOptions);
        if(isMyLocation)
        {
            float zoomLevel = 6.0f; // Уровень приближения
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(place, zoomLevel);
            my_Map.animateCamera(cameraUpdate);
        }

    }

    public MarkerOptions setMark(String typemark, LatLng place, String mess)
    {
        MarkerOptions markerOptions;
        float hue;
        switch (typemark) {
            case "entertainments":
//                Bitmap markerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.complexmark);
//                Bitmap resizedBitmap = Bitmap.createScaledBitmap(markerBitmap, 30, 30, false);
                hue  = BitmapDescriptorFactory.HUE_ROSE;
                markerOptions = new MarkerOptions()
                        .icon(BitmapDescriptorFactory.defaultMarker(hue))
                        .position(place)
                        .title(mess);
                return markerOptions;

            case "food":
                  hue = BitmapDescriptorFactory.HUE_VIOLET;
                 markerOptions = new MarkerOptions()
                        .icon(BitmapDescriptorFactory.defaultMarker(hue))
                        .position(place)
                        .title(mess);
                return markerOptions;

            case "hotels":
                hue = BitmapDescriptorFactory.HUE_ORANGE;
                markerOptions = new MarkerOptions()
                        .icon(BitmapDescriptorFactory.defaultMarker(hue))
                        .position(place)
                        .title(mess);
                return markerOptions;
            case "nature":
                hue = BitmapDescriptorFactory.HUE_GREEN;
                markerOptions = new MarkerOptions()
                        .icon(BitmapDescriptorFactory.defaultMarker(hue))
                        .position(place)
                        .title(mess);
                return markerOptions;
            case "attractions":
                hue = BitmapDescriptorFactory.HUE_BLUE;
                markerOptions = new MarkerOptions()
                        .icon(BitmapDescriptorFactory.defaultMarker(hue))
                        .position(place)
                        .title(mess);
                return markerOptions;
            case "historical":
                hue = BitmapDescriptorFactory.HUE_YELLOW;
                markerOptions = new MarkerOptions()
                        .icon(BitmapDescriptorFactory.defaultMarker(hue))
                        .position(place)
                        .title(mess);
                return markerOptions;
            default://красную обычную
                markerOptions = new MarkerOptions()
                        .position(place)
                        .title(mess);
                return markerOptions;
        }

    }

    public void GetAllObjectinBD(FirebaseDatabase database)
    {
        DatabaseReference marksRef = database.getReference("markers");

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
                    ShowMyCurrentLocation((float) current_locatoin.getLatitude(), (float) current_locatoin.getLongitude(), "You");
                }
            }
        });
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

    public void ShowMyCurrentLocation(Float coord1, Float coord2, String mess) {
        LatLng place = new LatLng(coord1, coord2);
        my_Map.addMarker(new MarkerOptions().position(place).title(mess));
        float zoomLevel = 17.0f; // Уровень приближения
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(place, zoomLevel);
        my_Map.animateCamera(cameraUpdate);
        isMyLocation = true;
    }

    public void AddMark(Float coord1, Float coord2, String mess) {
        LatLng place = new LatLng(coord1, coord2);
        my_Map.addMarker(new MarkerOptions().position(place).title(mess));
        float zoomLevel = 17.0f; // Уровень приближения
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(place, zoomLevel);
        my_Map.animateCamera(cameraUpdate);
    }
}