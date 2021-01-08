package com.jacksonasantos.travelplan.dao;

public class Itinerary {
    public Integer id;
    public Integer travel_id;
    public String name;
    public String address;
    public int category_type;
    public String description;
    public String latitude;
    public String longitude;
    public String zoom_level;

    public Itinerary() {
        this.id = id;
        this.travel_id = travel_id;
        this.name = name;
        this.address = address;
        this.category_type = category_type;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.zoom_level = zoom_level;
    }

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public Integer getTravel_id() { return travel_id; }
    public void setTravel_id(Integer travel_id) { this.travel_id = travel_id; }

    public String getName() {return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public int getCategory_type() { return category_type; }
    public void setCategory_type(int category_type) { this.category_type = category_type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLatitude() { return latitude; }
    public void setLatitude(String latitude) { this.latitude = latitude; }

    public String getLongitude() {return longitude;}
    public void setLongitude(String longitude) { this.longitude = longitude; }

    public String getZoom_level() { return zoom_level; }
    public void setZoom_level(String zoom_level) { this.zoom_level = zoom_level; }
}
