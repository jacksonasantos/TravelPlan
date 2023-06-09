package com.jacksonasantos.travelplan.dao;

import androidx.annotation.NonNull;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.ui.utility.Globals;

import java.util.Date;

public class Tour {
    public Integer id;
    public Integer travel_id;
    public Integer itinerary_id;
    public int tour_type;
    public String local_tour;
    public int currency_type;
    public Double value_adult;
    public Double value_child;
    public int number_adult;
    public int number_child;
    public Date tour_date;
    public int tour_sequence;
    public String opening_hours;
    public int distance;
    public String visitation_time;
    public String note;
    public String address_tour;
    public String city_tour;
    public String state_tour;
    public String country_tour;
    public String latlng_tour;
    public Integer achievement_id;

    final Globals g = Globals.getInstance();

    public Tour() {
    }

    @NonNull
    @Override
    public String toString() { return local_tour; }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getTravel_id() { return travel_id; }
    public void setTravel_id(Integer travel_id) { this.travel_id = travel_id; }

    public Integer getItinerary_id() { return itinerary_id; }
    public void setItinerary_id(Integer itinerary_id) { this.itinerary_id = itinerary_id; }

    public String getLocal_tour() { return local_tour; }
    public void setLocal_tour(String local_tour) { this.local_tour = local_tour; }

    public int getTour_type() { return tour_type; }
    public void setTour_type(int tour_type) { this.tour_type = tour_type; }

    public static int getTourTypeImage( int tour_type ) {
        switch(tour_type) {
            case 0: return R.drawable.ic_tour_museum;
            case 1: return R.drawable.ic_tour_panoramic_view;
            case 2: return R.drawable.ic_tour_square;
            case 3: return R.drawable.ic_tour_monument;
            case 4: return R.drawable.ic_tour_restaurant;
            case 5: return R.drawable.ic_tour_church;
            default: return R.drawable.ic_error;
        }
    }

    public int getCurrency_type() { return currency_type; }
    public void setCurrency_type(int currency_type) { this.currency_type = currency_type; }

    public Double getValue_adult() { return value_adult; }
    public void setValue_adult(Double value_adult) { this.value_adult = value_adult; }

    public Double getValue_child() { return value_child; }
    public void setValue_child(Double value_child) { this.value_child = value_child; }

    public int getNumber_adult() { return number_adult; }
    public void setNumber_adult(int number_adult) { this.number_adult = number_adult; }

    public int getNumber_child() { return number_child; }
    public void setNumber_child(int number_child) { this.number_child = number_child; }

    public Date getTour_date() { return tour_date; }
    public void setTour_date(Date tour_date) { this.tour_date = tour_date; }

    public int getTour_sequence() { return tour_sequence; }
    public void setTour_sequence(int tour_sequence) { this.tour_sequence = tour_sequence; }

    public String getOpening_hours() { return opening_hours; }
    public void setOpening_hours(String opening_hours) { this.opening_hours = opening_hours; }

    public int getDistance() { return distance; }
    public void setDistance(int distance) { this.distance = distance; }

    public int getDistanceMeasureIndex() { return getDistance()/g.getMeasureIndexInMeter(); }

    public String getVisitation_time() { return visitation_time; }
    public void setVisitation_time(String visitation_time) { this.visitation_time = visitation_time; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getAddress_tour() {return address_tour;}
    public void setAddress_tour(String address_tour) {this.address_tour = address_tour;}

    public String getCity_tour() {return city_tour;}
    public void setCity_tour(String city_tour) {this.city_tour = city_tour;}

    public String getState_tour() {return state_tour;}
    public void setState_tour(String state_tour) {this.state_tour = state_tour;}

    public String getCountry_tour() {return country_tour;}
    public void setCountry_tour(String country_tour) {this.country_tour = country_tour;}

    public String getLatlng_tour() {return latlng_tour;}
    public void setLatlng_tour(String latlng_tour) {this.latlng_tour = latlng_tour;}

    public Integer getAchievement_id() {return achievement_id;}
    public void setAchievement_id(Integer achievement_id) {this.achievement_id = achievement_id;}
}