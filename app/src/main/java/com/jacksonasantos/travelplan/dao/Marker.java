package com.jacksonasantos.travelplan.dao;

public class Marker {
    public Integer id;
    public Integer travel_id;
    public int marker_type;
    public Integer sequence;
    public String name;
    public String address;
    public String city;
    public String state;
    public String country;
    public String abbr_country;
    public int category_type;
    public String description;
    public String latitude;
    public String longitude;
    public String zoom_level;

    public Marker() {
        this.id = id;
        this.travel_id = travel_id;
        this.marker_type = marker_type;
        this.sequence = sequence;
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.country = country;
        this.abbr_country = abbr_country;
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

    public int getMarker_type() { return marker_type; }
    public void setMarker_type(int marker_type) { this.marker_type = marker_type; }

    public Integer getSequence() { return sequence; }
    public void setSequence(Integer sequence) { this.sequence = sequence; }

    public String getName() {return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getAbbr_country() { return abbr_country; }
    public void setAbbr_country(String abbr_country) { this.abbr_country = abbr_country; }

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
