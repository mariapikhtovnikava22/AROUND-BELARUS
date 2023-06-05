package com.example.aroundbelarus.Clases;

import com.google.android.gms.maps.GoogleMap;

public class MapSingleton {

    private static MapSingleton instance;
    private GoogleMap googleMap;

    private MapSingleton() {
        // Приватный конструктор, чтобы предотвратить создание экземпляров класса извне
    }

    public static MapSingleton getInstance() {
        if (instance == null) {
            synchronized (MapSingleton.class) {
                if (instance == null) {
                    instance = new MapSingleton();
                }
            }
        }
        return instance;
    }

    public GoogleMap getGoogleMap() {
        return googleMap;
    }

    public void setGoogleMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }
}
