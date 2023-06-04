package com.example.aroundbelarus.Clases;

import java.util.List;

public class Mark {

    String name;
    String category;
    Float latitude;
    Float longitude;
    String description;


    public Mark() {
    }

    public Mark(String name, String category, Float latitude, Float longitude, String description) {
        this.name = name;
        this.category = category;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String name) {
        this.description = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

}
