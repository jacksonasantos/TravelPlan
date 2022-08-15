package com.jacksonasantos.travelplan.dao;

import com.jacksonasantos.travelplan.R;
import com.jacksonasantos.travelplan.ui.utility.Globals;

public class Marker {
    public Integer id;
    public Integer travel_id;
    public Integer itinerary_id;
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
    }

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public Integer getTravel_id() { return travel_id; }
    public void setTravel_id(Integer travel_id) { this.travel_id = travel_id; }

    public Integer getItinerary_id() { return itinerary_id; }
    public void setItinerary_id(Integer itinerary_id) { this.itinerary_id = itinerary_id; }

    public int getMarker_type() { return marker_type; }
    public void setMarker_type(int marker_type) { this.marker_type = marker_type; }

    public int getMarker_typeImage(int marker_type) {
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
            default: draw = R.drawable.ic_error; break;
        }
        return draw;
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
            case 7: value = 3; break;   // 7-Landmark      -> 3-Tours
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
