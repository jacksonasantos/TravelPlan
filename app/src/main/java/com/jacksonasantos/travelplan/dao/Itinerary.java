package com.jacksonasantos.travelplan.dao;

public class Itinerary {
    public Integer id;
    public Integer travel_id;
    public int sequence;
    public String orig_location;
    public String dest_location;
    public String latlng_trip_orig;
    public String latlng_trip_dest;
    public int distance;
    public String time;

    public Itinerary() {
        this.id = id;
        this.travel_id = travel_id;
        this.sequence = sequence;
        this.orig_location = orig_location;
        this.dest_location = dest_location;
        this.latlng_trip_orig = latlng_trip_orig;
        this.latlng_trip_dest = latlng_trip_dest;
        this.distance = distance;
        this.time = time;
    }

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public Integer getTravel_id() { return travel_id; }
    public void setTravel_id(Integer travel_id) { this.travel_id = travel_id; }

    public int getSequence() { return sequence; }
    public void setSequence(int sequence) { this.sequence = sequence; }

    public String getOrig_location() {return orig_location;}
    public void setOrig_location(String orig_location) {this.orig_location = orig_location;}

    public String getDest_location() {return dest_location;}
    public void setDest_location(String dest_location) {this.dest_location = dest_location;}

    public String getLatlng_trip_orig() { return latlng_trip_orig; }
    public void setLatlng_trip_orig(String latlng_trip_orig) { this.latlng_trip_orig = latlng_trip_orig; }

    public String getLatlng_trip_dest() {return latlng_trip_dest;}
    public void setLatlng_trip_dest(String latlng_trip_dest) { this.latlng_trip_dest = latlng_trip_dest; }

    public int getDistance() { return distance; }
    public void setDistance(int distance) { this.distance = distance; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

}
