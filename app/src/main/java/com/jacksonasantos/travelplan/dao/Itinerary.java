package com.jacksonasantos.travelplan.dao;

import android.annotation.SuppressLint;
import android.graphics.Color;

import androidx.annotation.NonNull;

import com.jacksonasantos.travelplan.ui.utility.Globals;

public class Itinerary {
    public Integer id;
    public Integer travel_id;
    public int sequence;
    public String orig_location;
    public String dest_location;
    public int daily;
    public int distance;
    public int time;

    Globals g = Globals.getInstance();

    public Itinerary() {
        this.id = id;
        this.travel_id = travel_id;
        this.sequence = sequence;
        this.orig_location = orig_location;
        this.dest_location = dest_location;
        this.daily = daily;
        this.distance = distance;
        this.time = time;
    }
    @NonNull
    @Override
    public String toString() { return sequence+"-"+orig_location+" p/ "+dest_location; }

    public int getColorItinerary( int sequence){
        int color;
        switch(sequence) {
            case 1: color = Color.BLUE; break;
            case 2: color = Color.MAGENTA; break;
            case 3: color = Color.RED; break;
            case 4: color = Color.CYAN; break;
            case 5: color = Color.GRAY; break;
            case 6: color = Color.YELLOW; break;
            case 7: color = Color.LTGRAY; break;
            case 8: color = Color.GREEN; break;
            case 9: color = Color.DKGRAY; break;
            default: color = Color.BLACK; break;
        }
        return color;
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

    public int getDaily() { return daily; }
    public void setDaily(int daily) { this.daily = daily; }

    public int getDistance() { return distance/g.getMeasureIndexInMeter(); }
    public void setDistance(int distance) { this.distance = distance; }

    public int getTime() { return time; }
    public void setTime(int time) { this.time = time; }

    @SuppressLint("DefaultLocale")
    public String getDuration() {
        int totalHr = time / 3600;
        int totalMin = (time-(totalHr * 3600)) / 60;
        return String.format("%3d:%02d", totalHr, totalMin);
    }
}
