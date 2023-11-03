package com.jacksonasantos.travelplan.dao;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.ui.utility.Globals;

import java.util.Objects;

public class Marker {
    public Integer id;
    public Integer travel_id;
    public Integer itinerary_id;
    public Integer achievement_id;
    public int marker_type;
    public Integer sequence;
    public String name;
    public String address;
    public String city;
    public String state;
    public String country;
    public String abbr_country;
    public String description;
    public String latitude;
    public String longitude;
    public int predicted_stop_time;

    public Marker() {
    }

    @NonNull
    @Override
    public String toString() { return sequence==null||sequence==0?"":sequence+"-"+marker_type+"-"+city; } // TODO - Alterar o marker_typepelo texto do array  - context.getResources().getStringArray(R.array.marker_type_array)[marker_type];

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public Integer getTravel_id() { return travel_id; }
    public void setTravel_id(Integer travel_id) { this.travel_id = travel_id; }

    public Integer getItinerary_id() { return itinerary_id; }
    public void setItinerary_id(Integer itinerary_id) { this.itinerary_id = itinerary_id; }

    public Integer getAchievement_id() { return achievement_id; }
    public void setAchievement_id(Integer achievement_id) { this.achievement_id = achievement_id; }

    public int getMarker_type() { return marker_type; }
    public void setMarker_type(int marker_type) { this.marker_type = marker_type; }

    public static int getMarker_typeImage(int marker_type) {
        int draw;
        switch(marker_type) {
            case 0:
            case 1: draw = R.drawable.ic_trip_target; break;
            case 2: draw = R.drawable.ic_supply; break;
            case 3: draw = R.drawable.ic_restaurant; break;
            case 4: draw = R.drawable.ic_menu_accommodation; break;
            case 5: draw = R.drawable.ic_toll; break;
            case 6: draw = R.drawable.ic_tour; break;
            case 7: draw = R.drawable.ic_landmark; break;
            case 8: draw = R.drawable.ic_waypoints; break;
            case 9: draw = R.drawable.ic_menu_achievement; break;
            default: draw = R.drawable.ic_error; break;
        }
        return draw;
    }

    public int getMarker_typePredictedTime( int marker_type){
        int multiplier = 60; // seconds
        int time = 0;
        switch(marker_type) {
            case 2:
            case 7:
            case 8: time = 15; break;
            case 3:
            case 6: time = 60; break;
            case 5: time = 5; break;
            case 9: time = 10; break;
        }
        return time*multiplier;
    }

    public Double getMarker_typeExpectedValue( int marker_type){
        final Globals g = Globals.getInstance();
        Double value;
        switch(marker_type) {
            case 3:
            case 8: value = g.getExpectedValueRestaurant(); break;
            case 4: value = g.getExpectedValueAccommodation(); break;
            case 5: value = g.getExpectedValueToll(); break;
            case 6: value = g.getExpectedValueTour(); break;
            case 7: value = g.getExpectedValueLandmark(); break;
            default: value = 0.0; break;
        }
        return value;
    }

    public int getMarker_typeExpenseType( int marker_type) {
        /* Get for translate of Type Marker for Type Expense */
        int value ;
        switch(marker_type) {
            case 3:                     // 3-food          -> 1-food
            case 8: value = 1; break;   // 8-waypoints     -> 1-food
            case 4: value = 4; break;   // 4-Accommodation -> 4-accommodation
            case 5: value = 2; break;   // 5-Toll          -> 2-Toll
            case 6:                     // 6-Tour          -> 3-Tours
            case 7:                     // 7-Landmark      -> 3-Tours
            case 9: value = 3; break;   // 9-Achievement   -> 3-Tours
            default:
                throw new IllegalStateException("Unexpected value: " + marker_type);
        }
        return value;
    }

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

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLatitude() { return latitude; }
    public void setLatitude(String latitude) { this.latitude = latitude; }

    public String getLongitude() {return longitude;}
    public void setLongitude(String longitude) { this.longitude = longitude; }

    public int getPredicted_stop_time() { return predicted_stop_time; }
    public void setPredicted_stop_time(int predicted_stop_time) {this.predicted_stop_time = predicted_stop_time;}

    @SuppressLint("DefaultLocale")
    public String getDuration_Predicted_stop_time() {
        int totalHr = getPredicted_stop_time() / 3600;
        int totalMin = (getPredicted_stop_time()-(totalHr * 3600)) / 60;
        return String.format("%3d:%02d", totalHr, totalMin);
    }

    @SuppressLint("DefaultLocale")
    public String getDuration_Predicted_stop_time(int duration) {
        int totalHr = duration / 3600;
        int totalMin = (duration-(totalHr * 3600)) / 60;
        return String.format("%3d:%02d", totalHr, totalMin);
    }
    public void setDuration_Predicted_stop_time( String duration) {
        if (!Objects.equals(duration, "")) {
            int totalHr = Integer.parseInt(duration.substring(0, duration.indexOf(":")).trim()) * 3600;
            int totalMin = Integer.parseInt(duration.substring(duration.length() - 2)) * 60;
            this.predicted_stop_time = totalHr + totalMin;
        }
    }
}
